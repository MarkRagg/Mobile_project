package com.project.mobile_project.viewModel

import androidx.lifecycle.*
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

    fun updateProfileImg(username: String, profileImg: String) = viewModelScope.launch {
        repository.updateProfileImg(username, profileImg)
    }

    fun getUserFromUsername(username: String, password: String): User? {
        return repository.getUserFromUsernameAndPassw(username, password)
    }

    fun getUser(username: String): User? {
        return repository.getUserFromUsername(username)
    }

    private var _userSelected: User? = null
    val userSelected
        get() = _userSelected

    fun selectUser(user: User) {
        _userSelected = user
    }
}