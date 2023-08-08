package com.project.mobile_project.data

import android.graphics.Bitmap
import androidx.room.*
import java.sql.Blob

@Entity(tableName = "user")
data class User (
    @PrimaryKey val userId: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
    val salt: String?,
    val profileImg: String // TODO: convert in Bitmap or Blob
)

@Entity(tableName = "activity")
data class Activity(
    @PrimaryKey val activityId: Int,
    val userCreatorId: Int,
    val name: String,
    val description: String?,
    val totalTime: Long,
    val distance: Int,
    val speed: Int,
    val pace: Int?,
    val steps: Int?,
    val onFoot: Boolean?
)

data class UserWithActivity(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userCreatorId"
    )
    val activities: List<Activity>
)

