package com.example.pokedexcomposesample.data

import android.content.res.Resources
import com.example.pokedexcomposesample.domain.exception.BadRequestException
import com.example.pokedexcomposesample.domain.exception.NetworkException
import com.example.pokedexcomposesample.domain.exception.UnAuthorizedException
import com.example.pokedexkotlinsample.domain.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> apiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): T {
    return withContext(dispatcher) {
        try {
            apiCall.invoke()
        } catch (e: Throwable) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw UnAuthorizedException()
                        HttpURLConnection.HTTP_BAD_REQUEST -> throw BadRequestException(
                            e.localizedMessage ?: "unknown error"
                        )
                        HttpURLConnection.HTTP_NOT_FOUND -> throw Resources.NotFoundException(
                            e.localizedMessage ?: "unknown error"
                        )
                        else -> throw e
                    }
                }
                is UnknownHostException, is ConnectException, is SocketTimeoutException -> throw NetworkException()
                else -> throw e
            }
        }
    }
}

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): ApiResult<T> {
    return withContext(dispatcher) {
        try {
            ApiResult.Success(apiCall())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    ApiResult.Failure(throwable.code(), throwable.response()?.errorBody())
                }
                is UnknownHostException, is ConnectException, is SocketTimeoutException -> {
                    ApiResult.NetworkError
                }
                else -> {
                    ApiResult.NetworkError
                }
            }
        }
    }
}