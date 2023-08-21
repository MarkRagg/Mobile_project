package com.project.mobile_project.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("UPDATE user SET profileImg = :profileImg WHERE username = :username")
    suspend fun updateProfileImg(username: String, profileImg: String)

    @Query("SELECT * FROM user WHERE username = :usernameSelected AND password = :passwordSelected")
    fun getUserFromUsernameAndPassw(usernameSelected: String, passwordSelected: String): Flow<User>

    @Query("SELECT * FROM user WHERE username = :usernameSelected")
    fun getUserFromUsername(usernameSelected: String): Flow<User>

    /*@Transaction
    @Query("SELECT user.* FROM activity, user WHERE userCreatorId = username")
    suspend fun getUserFromActivity(): User

    @Transaction
    @Query("SELECT activity.* FROM activity, user WHERE userCreatorId = username")
    suspend fun getActivitiesFromUser(): List<Activity>*/
}