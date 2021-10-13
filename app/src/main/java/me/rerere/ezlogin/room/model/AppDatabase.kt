package me.rerere.ezlogin.room.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Account::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAccountDao(): AccountDao
}