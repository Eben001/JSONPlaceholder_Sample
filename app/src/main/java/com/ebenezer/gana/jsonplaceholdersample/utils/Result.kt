package com.ebenezer.gana.jsonplaceholdersample.utils

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    class Loading<T : Any> : Result<T>()
}