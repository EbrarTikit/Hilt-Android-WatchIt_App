package com.example.watchit.ui.movieDetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchit.common.UIState
import com.example.watchit.data.model.MovieDetail
import com.example.watchit.data.model.WatchListResponse
import com.example.watchit.data.model.WatchlistRequest
import com.example.watchit.data.network.ApiInterface
import com.example.watchit.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: AppRepository
): ViewModel() {

    val movieDetail: MutableLiveData<MovieDetail> = MutableLiveData()
    val isLoading = MutableLiveData(false)
    val errorMessage: MutableLiveData<String?> = MutableLiveData()

    val genresText: MutableLiveData<String> = MutableLiveData()

    private val _watchlistState = MutableStateFlow<UIState<WatchListResponse>>(UIState.Loading)
    val watchlistState = _watchlistState.asStateFlow()

    fun getMovieDetail(movieId: Int) {
        isLoading.value = true
        
        viewModelScope.launch {
            try {
                val response = repository.getMovieDetail(movieId)
                
                if (response.isSuccessful) {
                    val movie = response.body()
                    movie?.let {
                        movieDetail.postValue(it)
                        genresText.postValue(it.genres.take(3).joinToString(" - ") { genre -> genre.name })
                    }
                } else {
                    errorMessage.value = response.message() ?: "An unknown error occurred"
                }
            } catch (e: Exception) {
                Log.e("MovieDetail", "Error loading movie details: ${e.message}", e)
                errorMessage.value = e.message ?: "An unknown error occurred"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun addToWatchList(accountId: Int,mediaId: Int,isMovie: Boolean, sessionId: String){
        viewModelScope.launch {
            _watchlistState.value = UIState.Loading
            try {
                val request = WatchlistRequest(
                    mediaType = if (isMovie) "movie" else "tv",
                    mediaId = mediaId,
                    watchlist = true
                )
                val response = repository.addWatchlist(accountId,sessionId,request)
                if (response.isSuccessful){
                    _watchlistState.value = UIState.Success(response.body()!!)
                    Log.d("Watchlist", "Added to watchlist: ${response.body()}")
                } else{
                    _watchlistState.value = UIState.Failure(
                        error = Exception(response.message()),
                        data = WatchListResponse(0, response.message(), false)
                    )
                    Log.e("Watchlist", "Failed to add to watchlist: ${response.message()}")
                }
            }catch (e: Exception){
                _watchlistState.value = UIState.Failure(
                    error = e,
                    data = WatchListResponse(0, e.message ?: "Unknown error", false)
                )
                Log.e("Watchlist", "Failed to add to watchlist: ${e.message}")
            }
        }
    }


}