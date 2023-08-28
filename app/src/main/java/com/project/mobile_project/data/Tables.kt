package com.project.mobile_project.data

import androidx.room.*
import java.util.*

@Entity(tableName = "user")
data class User (
    @PrimaryKey var username: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var salt: String?,
    var profileImg: String?
)

@Entity(tableName = "activity")
data class Activity(
    @PrimaryKey val activityId: String = UUID.randomUUID().toString(),
    var userCreatorUsername: String,
    var name: String?,
    var description: String?,
    var totalTime: Long,
    var distance: Int,
    var speed: Double,
    var pace: Double,
    var steps: Int?,
    var onFoot: Boolean?
)

data class UserWithActivity(
    @Embedded val user: User,
    @Relation(
        parentColumn = "username",
        entityColumn = "userCreatorUsername"
    )
    val activities: List<Activity>
)

