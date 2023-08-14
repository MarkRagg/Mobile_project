package com.project.mobile_project.di

import android.content.Context
import androidx.room.Room
import com.project.mobile_project.data.AppDatabase
import com.project.mobile_project.data.UserDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDAO {
        return database.UserDAO()
    }
}