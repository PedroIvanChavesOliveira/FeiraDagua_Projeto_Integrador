package com.feiradagua.feiradagua.api

sealed class ResponseAPI {
    class Success(val data: Any?): ResponseAPI()
    class Error (val message: String): ResponseAPI()
}