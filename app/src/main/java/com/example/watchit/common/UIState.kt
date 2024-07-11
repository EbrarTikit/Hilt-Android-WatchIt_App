package com.example.watchit.common

import com.example.watchit.data.model.Result
import kotlinx.coroutines.flow.MutableStateFlow

sealed class UIState<out T> {
    data object Loading : UIState<Nothing>()
    data class Success<T>(val data : T) : UIState<T>()
    data class Failure<T>(val error: Throwable, val data:T): UIState<T>()

}