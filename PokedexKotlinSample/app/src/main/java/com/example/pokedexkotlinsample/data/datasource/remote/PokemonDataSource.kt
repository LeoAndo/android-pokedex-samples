package com.example.pokedexkotlinsample.data.datasource.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokedexkotlinsample.data.api.PokemonService
import com.example.pokedexkotlinsample.data.api.response.toModels
import com.example.pokedexkotlinsample.domain.exception.BadRequestException
import com.example.pokedexkotlinsample.domain.exception.NetworkException
import com.example.pokedexkotlinsample.domain.exception.NotFoundException
import com.example.pokedexkotlinsample.domain.exception.UnAuthorizedException
import com.example.pokedexkotlinsample.domain.model.PokemonModel
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * query If empty, search all pokemon`s
 */
class PokemonDataSource(private val api: PokemonService, private val query: String) :
    PagingSource<Int, PokemonModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonModel> {
        val offset = params.key ?: STARTING_PAGE_INDEX
        Log.d(
            LOG_TAG,
            "params.key: ${params.key} , offset: $offset , params.loadSize: ${params.loadSize}"
        )

        return try {
            val data = api.getPokemons(params.loadSize, offset)
            val filteredData = if (query.isNotEmpty()) {
                data.results.filter { it.name.contains(query, true) }
            } else {
                data.results
            }.toModels()

            val prevKey = if (offset == STARTING_PAGE_INDEX) null else offset - params.loadSize
            val nextKey =
                if (data.next == null) null else offset + params.loadSize // paging終了するときは、nullをセット.
            Log.d(LOG_TAG, "prevKey: $prevKey nextKey: $nextKey")
            LoadResult.Page(data = filteredData, prevKey = prevKey, nextKey = nextKey)
        } catch (e: Throwable) {
            when (e) {
                is HttpException -> {
                    val message = e.localizedMessage ?: "unknown error"
                    when (e.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> LoadResult.Error(
                            UnAuthorizedException()
                        )
                        HttpURLConnection.HTTP_BAD_REQUEST -> LoadResult.Error(
                            BadRequestException(message)
                        )
                        HttpURLConnection.HTTP_NOT_FOUND -> LoadResult.Error(
                            NotFoundException(message)
                        )
                        else -> LoadResult.Error(e)
                    }
                }
                is UnknownHostException, is ConnectException, is SocketTimeoutException -> LoadResult.Error(
                    NetworkException()
                )
                else -> LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonModel>): Int? {
        // return state.anchorPosition
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 0
        private const val LOG_TAG = "PokemonDataSource"
    }
}