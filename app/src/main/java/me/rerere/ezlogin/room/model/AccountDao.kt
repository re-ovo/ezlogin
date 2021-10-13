package me.rerere.ezlogin.room.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun getAllAccounts() : Flow<List<Account>>

    @Insert
    suspend fun insert(account: Account)

    @Delete
    suspend fun delete(account: Account)

    @Query("DELETE FROM account WHERE `key`=''")
    suspend fun cleanBadAccount()
}