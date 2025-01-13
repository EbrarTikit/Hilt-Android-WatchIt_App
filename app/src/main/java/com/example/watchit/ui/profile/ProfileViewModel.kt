package com.example.watchit.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchit.common.UIState
import com.example.watchit.data.model.AccountDetails
import com.example.watchit.data.model.Avatar
import com.example.watchit.data.model.Tmdb
import com.example.watchit.data.model.Gravatar
import com.example.watchit.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserStats(
    val watchlistCount: Int = 0,
    val ratedCount: Int = 0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<UIState<AccountDetails>>(UIState.Loading)
    val profileState = _profileState.asStateFlow()

    private val _statsState = MutableStateFlow<UIState<UserStats>>(UIState.Loading)
    val statsState = _statsState.asStateFlow()

    fun loadProfileData(sessionId: String, accountId: Int) {
        viewModelScope.launch {
            try {
                val accountDetails = repository.getAccountDetails(sessionId, accountId)
                _profileState.value = UIState.Success(accountDetails)
                
                val watchlistCount = repository.getWatchlistCount(accountId, sessionId)
                val ratedCount = repository.getRatedMoviesCount(accountId, sessionId)
                
                _statsState.value = UIState.Success(UserStats(watchlistCount, ratedCount))
            } catch (e: Exception) {
                _profileState.value = UIState.Failure(e, AccountDetails(0, "", "", false, 
                    Avatar(Gravatar(""), Tmdb(null))))
                _statsState.value = UIState.Failure(e, UserStats())
            }
        }
    }
} 