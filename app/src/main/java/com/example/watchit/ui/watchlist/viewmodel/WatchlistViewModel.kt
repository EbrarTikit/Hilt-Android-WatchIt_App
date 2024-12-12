package com.example.watchit.ui.watchlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchit.common.UIState
import com.example.watchit.data.model.MyWatchListMovies
import com.example.watchit.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _watchlist = MutableStateFlow<UIState<MyWatchListMovies>>(UIState.Loading)
    val watchlist: MutableStateFlow<UIState<MyWatchListMovies>> = _watchlist

    fun getWatchList(
        accountId: Int,
        sessionId: String
    ) {
        viewModelScope.launch {
            try {
                val response = repository.getWatchlist(accountId,sessionId)
                if (response.isSuccessful) {
                    _watchlist.value = UIState.Success(response.body() ?: MyWatchListMovies(0, emptyList(), 0, 0))
                } else {
                    _watchlist.value = UIState.Failure(Throwable("Failed to fetch watchlist"), MyWatchListMovies(0, emptyList(), 0, 0))
                }
            } catch (e: Exception) {
                _watchlist.value = UIState.Failure(e, MyWatchListMovies(0, emptyList(), 0, 0))
            }
        }
    }

}