package com.example.pokedexkotlinsample.domain

import okhttp3.ResponseBody

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T) : ApiResult<T>()
    data class Failure(val errorCode: Int, val errorBody: ResponseBody?) : ApiResult<Nothing>()
    object NetworkError : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}