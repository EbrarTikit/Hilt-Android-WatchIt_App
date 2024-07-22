package com.example.watchit.ui.movieList.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchit.common.UIState
import com.example.watchit.data.model.Movies
import com.example.watchit.data.model.Result
import com.example.watchit.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

// it will check the dependencies in constructor from module
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _mainItem = MutableStateFlow<UIState<Movies>>(UIState.Loading)
    val mainItem: StateFlow<UIState<Movies>> = _mainItem

    init {
        fetchMovies(page = 1)
    }

    fun fetchMovies(page: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMovies(page)
                if (response.isSuccessful) {
                    _mainItem.value = UIState.Success(response.body() ?: Movies(0, emptyList(), 0, 0))
                } else {
                    _mainItem.value = UIState.Failure(Throwable("Failed to fetch movies"), Movies(0, emptyList(), 0, 0))
                }
            } catch (e: Exception) {
                _mainItem.value = UIState.Failure(e, Movies(0, emptyList(), 0, 0))
            }
        }
    }
}


