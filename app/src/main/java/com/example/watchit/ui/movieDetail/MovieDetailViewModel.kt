package com.example.watchit.ui.movieDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchit.data.model.MovieDetail
import com.example.watchit.data.network.ApiInterface
import com.example.watchit.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: AppRepository
): ViewModel() {

    val movieDetail: MutableLiveData<MovieDetail> = MutableLiveData()
    val isLoading = MutableLiveData(false)
    val errorMessage: MutableLiveData<String?> = MutableLiveData()

    val genresText: MutableLiveData<String> = MutableLiveData()


    fun getMovieDetail(movieId: Int){
        isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getMovieDetail(movieId)

                if (response.isSuccessful)  if (response.isSuccessful) {
                    val movie = response.body()
                    movie?.let {
                        movieDetail.postValue(it)
                        genresText.postValue(it.genres.take(3).joinToString(" - ") { genre -> genre.name })
                    }
                }else {
                    if (response.message().isNullOrEmpty()) {
                        errorMessage.value = "An unknown error"
                    } else {
                        errorMessage.value = response.message()
                    }
                }

            }catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }

    }


}