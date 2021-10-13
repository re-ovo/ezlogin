package me.rerere.ezlogin.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Account(
    @PrimaryKey(autoGenerate = true) val primaryKey: Int = 0,
    val account: String,
    val website: String,
    val key: String
)