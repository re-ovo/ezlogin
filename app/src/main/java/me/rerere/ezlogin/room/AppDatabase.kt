package me.rerere.ezlogin.room

import androidx.room.Database
import androidx.room.RoomDatabase
import me.rerere.ezlogin.room.model.Account

@Database(
    entities = [Account::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAccountDao(): AccountDao
}