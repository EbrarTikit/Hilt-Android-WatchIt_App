package com.example.watchit.ui.trends.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.watchit.common.UIState
import com.example.watchit.data.model.TrendMovie
import com.example.watchit.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendsViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel(){
    private val _trendItem = MutableStateFlow<UIState<TrendMovie>>(UIState.Loading)
    val trendMovie: StateFlow<UIState<TrendMovie>> = _trendItem

    init {
        fetchTrendMovies()
    }

    fun fetchTrendMovies(){
        viewModelScope.launch {
            try {
                val response = repository.getTrends()
                if (response.isSuccessful){
                    _trendItem.value = UIState.Success(response.body() ?: TrendMovie(0, emptyList(), 0, 0))
                } else {
                    _trendItem.value = UIState.Failure(Throwable("Failed to fetch movies"), TrendMovie(0, emptyList(), 0, 0))
                }
            } catch (e: Exception){
                _trendItem.value = UIState.Failure(e, TrendMovie(0, emptyList(), 0, 0))
            }
        }
    }
}