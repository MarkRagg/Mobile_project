package com.project.mobile_project.viewModel

import androidx.lifecycle.MutableLiveData
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
    val userLiveData = MutableLiveData<User>()

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

    fun getUserFromUsername(username: String, password: String) = viewModelScope.launch {
        repository.getUserFromUsernameAndPassw(username, password).collect {
            userLiveData.postValue(it)
        }
    }

    fun getUser(username: String) = viewModelScope.launch {
        repository.getUserFromUsername(username).collect {
            userLiveData.postValue(it)
        }
    }

    private var _userSelected: User? = null
    val userSelected
        get() = _userSelected

    fun selectUser(user: User) {
        _userSelected = user
    }
}