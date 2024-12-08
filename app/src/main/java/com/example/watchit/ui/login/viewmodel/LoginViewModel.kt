package com.example.watchit.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchit.common.UIState
import com.example.watchit.data.model.SessionResponse
import com.example.watchit.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<UIState<SessionResponse>>(UIState.Loading)
    val loginState: MutableStateFlow<UIState<SessionResponse>> = _loginState

    fun login(username:String,password:String){
        viewModelScope.launch {
            _loginState.value = UIState.Loading
            try {
                val result = repository.login(username, password)
                result.fold(
                    onSuccess = { session ->
                        _loginState.value = UIState.Success(session)
                    },
                    onFailure = { error ->
                        _loginState.value = UIState.Failure(error, SessionResponse(false, ""))
                    }
                )
            } catch (e: Exception) {
                _loginState.value = UIState.Failure(e, SessionResponse(false, ""))
            }
        }
    }

}