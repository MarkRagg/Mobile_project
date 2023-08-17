package com.project.mobile_project.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.mobile_project.data.User
import com.project.mobile_project.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {

    val allUsers = repository.allUsers

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    fun updateUser(user: User) = viewModelScope.launch {
        repository.updateUser(user)
    }

    fun deleteAllUsers() = viewModelScope.launch {
        repository.deleteAllUser()
    }

    private var _userSelected: User? = null
    val userSelected
        get() = _userSelected

    fun selectUser(user: User) {
        _userSelected = user
    }
}

class UsersViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}