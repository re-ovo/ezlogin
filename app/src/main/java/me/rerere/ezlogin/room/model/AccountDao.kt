package me.rerere.ezlogin.room.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun getAllAccounts() : Flow<List<Account>>

    @Query("SELECT * FROM account WHERE primaryKey=:id")
    suspend fun getAccountById(id: Int): Account?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account)

    @Delete
    suspend fun delete(account: Account)

    @Query("DELETE FROM account WHERE primaryKey=:id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM account WHERE `key`=''")
    suspend fun cleanBadAccount()
}