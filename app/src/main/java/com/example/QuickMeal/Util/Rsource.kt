package com.example.QuickMeal.Util

sealed class Rsource<T> (
    val data:T?=null,
    val  message:String?=null
){
    class Success<T>(data: T?):Rsource<T>(data)
    class Error<T>(message: String?):Rsource<T>(message=message)
    class Loading<T>:Rsource<T>()
    class Unspecified<T>:Rsource<T>()
}
