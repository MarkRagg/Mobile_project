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

    @Transaction
    @Query("SELECT user.* FROM activity, user WHERE userCreatorId = username")
    suspend fun getUserFromActivity(): User

    @Transaction
    @Query("SELECT activity.* FROM activity, user WHERE userCreatorId = username")
    suspend fun getActivitiesFromUser(): List<Activity>
}