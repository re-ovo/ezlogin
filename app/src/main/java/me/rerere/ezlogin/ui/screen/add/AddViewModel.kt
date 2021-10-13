package me.rerere.ezlogin.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.ezlogin.room.model.Account
import me.rerere.ezlogin.room.model.AppDatabase
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val appDatabase: AppDatabase
): ViewModel() {
    fun addAccount(
        key: String,
        website: String,
        account: String
    ){
        viewModelScope.launch {
            appDatabase.getAccountDao().insert(
                Account(
                    key = key,
                    website = website,
                    account = account
                )
            )
        }
    }
}