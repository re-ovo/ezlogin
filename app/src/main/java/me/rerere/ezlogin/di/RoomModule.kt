package me.rerere.ezlogin.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.rerere.ezlogin.App
import me.rerere.ezlogin.room.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase() = Room.databaseBuilder(
        App.instance,
        AppDatabase::class.java,
        "ezlogin"
    ).build()
}