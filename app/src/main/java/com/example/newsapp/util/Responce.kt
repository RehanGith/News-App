package com.example.newsapp.util

sealed class Responce<T>(val data :T? = null, val message: String? = null) {
    class Success<T>(data :T) : Responce<T>(data)
    class Error<T>(message: String, data: T?=null): Responce<T>(data, message)
    class Loading<T>() : Responce<T>()

}