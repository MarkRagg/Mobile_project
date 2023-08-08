package com.project.mobile_project.data

import androidx.room.*

@Dao
interface ActivityDAO {
    @Query("SELECT * FROM activity")
    fun getActivities(): List<Activity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(activity: Activity)

    @Update
    fun updateUser(activity: Activity)

    @Delete
    suspend fun delete(activity: Activity)

    @Query("DELETE FROM activity")
    suspend fun deleteAll()
}