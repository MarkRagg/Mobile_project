package com.project.mobile_project.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDAO: UserDAO) {

    val allUsers: Flow<List<User>> = userDAO.getUsers()

    @WorkerThread
    suspend fun insertUser(user: User) {
        userDAO.insert(user)
    }

    @WorkerThread
    suspend fun deleteUser(user: User) {
        userDAO.delete(user)
    }

    @WorkerThread
    suspend fun deleteAllUser() {
        userDAO.deleteAll()
    }

    @WorkerThread
    suspend fun updateUser(user: User) {
        userDAO.updateUser(user)
    }

    @WorkerThread
    suspend fun updateProfileImg(username: String, profileImg: String) {
        userDAO.updateProfileImg(username, profileImg)
    }

    @WorkerThread
    fun getUserFromUsernameAndPassw(usernameSelected: String, passwordSelected: String): Flow<User> {
        return userDAO.getUserFromUsernameAndPassw(usernameSelected, passwordSelected)
    }

    @WorkerThread
    fun getUserFromUsername(usernameSelected: String): Flow<User> {
        return userDAO.getUserFromUsername(usernameSelected)
    }

    /*@WorkerThread
    suspend fun getUserFromActivity() {
        userDAO.getUserFromActivity()
    }

    @WorkerThread
    suspend fun getActivityFromUser() {
        userDAO.getActivitiesFromUser()
    }*/
}