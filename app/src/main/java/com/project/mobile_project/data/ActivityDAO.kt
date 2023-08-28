package com.project.mobile_project.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDAO {
    @Query("SELECT * FROM activity")
    fun getActivities(): Flow<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(activity: Activity)

    @Update
    fun updateActivity(activity: Activity)

    @Delete
    suspend fun delete(activity: Activity)

    @Query("DELETE FROM activity")
    suspend fun deleteAll()

    @Query("UPDATE activity SET name = :name WHERE activityId = :activityId")
    suspend fun updateActivityName(activityId: String, name: String)

    @Query("UPDATE activity SET favourite = :favourite WHERE activityId = :activityId")
    suspend fun updateActivityFavourite(activityId: String, favourite: Boolean)

    @Transaction
    @Query("SELECT * FROM activity WHERE userCreatorUsername = :usernameSelected")
    fun getActivitiesFromUser(usernameSelected: String): Flow<List<Activity>>

}