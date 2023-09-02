package com.project.mobile_project.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ActivityRepository @Inject constructor(private val activityDAO: ActivityDAO) {

    val allActivities: Flow<List<Activity>> = activityDAO.getActivities()

    @WorkerThread
    suspend fun insertActivity(activity: Activity) {
        activityDAO.insert(activity)
    }

    @WorkerThread
    suspend fun deleteActivity(activity: Activity) {
        activityDAO.delete(activity)
    }

    @WorkerThread
    suspend fun deleteAllActivities() {
        activityDAO.deleteAll()
    }

    @WorkerThread
    fun updateActivity(activity: Activity) {
        activityDAO.updateActivity(activity)
    }

    @WorkerThread
    suspend fun updateActivityName(activityId: String, name: String) {
        activityDAO.updateActivityName(activityId, name)
    }

    @WorkerThread
    suspend fun updateActivityFavourite(activityId: String, favourite: Boolean) {
        activityDAO.updateActivityFavourite(activityId, favourite)
    }

    @WorkerThread
    fun getActivitiesFromUser(usernameSelected: String): Flow<List<Activity>> {
        return activityDAO.getActivitiesFromUser(usernameSelected)
    }

    @WorkerThread
    fun getFavouriteActivitiesFromUser(usernameSelected: String): Flow<List<Activity>> {
        return activityDAO.getFavouriteActivitiesFromUser(usernameSelected)
    }
}