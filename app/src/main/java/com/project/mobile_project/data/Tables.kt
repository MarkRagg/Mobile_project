package com.project.mobile_project.data

import android.graphics.Bitmap
import androidx.room.*
import java.sql.Blob

@Entity(tableName = "user")
data class User (
    @PrimaryKey var username: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var salt: String?,
    var profileImg: String // TODO: convert in Bitmap or Blob
)

@Entity(tableName = "activity")
data class Activity(
    @PrimaryKey var activityId: String,
    var userCreatorId: Int,
    var name: String,
    var description: String?,
    var totalTime: Long,
    var distance: Int,
    var speed: Int,
    var pace: Int?,
    var steps: Int?,
    var onFoot: Boolean?
)

data class UserWithActivity(
    @Embedded val user: User,
    @Relation(
        parentColumn = "username",
        entityColumn = "userCreatorId"
    )
    val activities: List<Activity>
)

