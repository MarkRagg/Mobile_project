package com.project.mobile_project.data

import androidx.room.*

@Dao
interface UserDAO {
    @Query("SELECT * FROM user")
    fun getUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT user.* FROM activity, user WHERE userCreatorId = userId")
    fun getUserFromActivity(): User

    @Transaction
    @Query("SELECT activity.* FROM activity, user WHERE userCreatorId = userId")
    fun getActivitiesFromUser(): List<Activity>
}