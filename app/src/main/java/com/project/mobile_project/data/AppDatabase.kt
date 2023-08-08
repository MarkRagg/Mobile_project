package com.project.mobile_project.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Activity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun UserDAO(): UserDAO
    abstract fun ActivityDAO(): ActivityDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase ?= null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }

}