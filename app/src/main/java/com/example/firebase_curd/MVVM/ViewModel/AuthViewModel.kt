package com.example.firebase_curd.MVVM.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_curd.MVVM.Model.User
import com.example.firebase_curd.MVVM.Repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _signUpResult = MutableLiveData<Result<String>>()
    val signUpResult: LiveData<Result<String>> get() = _signUpResult

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> get() = _loginResult

    private val _usersList = MutableLiveData<Result<List<User>>>()
    val usersList: LiveData<Result<List<User>>> get() = _usersList

    private val _updateUserResult = MutableLiveData<Result<String>>()
    val updateUserResult: LiveData<Result<String>> get() = _updateUserResult

    private val _deleteUserResult = MutableLiveData<Result<String>>()
    val deleteUserResult: LiveData<Result<String>> get() = _deleteUserResult

    fun signUp(user: User) {
        viewModelScope.launch {
            val result = repository.signUp(user)
            _signUpResult.value = result

            // Fetch updated users list if sign-up is successful
            if (result.isSuccess) {
                getAllUsersExceptLoggedIn()
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginResult.value = result
        }
    }

    fun getAllUsersExceptLoggedIn() {
        viewModelScope.launch {
            val result = repository.getAllUsersExceptLoggedIn()
            _usersList.value = result
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            val result = repository.updateUser(user)
            _updateUserResult.value = result
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            val result = repository.deleteUser(userId)
            _deleteUserResult.value = result
        }
    }
}