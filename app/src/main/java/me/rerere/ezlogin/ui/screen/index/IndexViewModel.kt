package me.rerere.ezlogin.ui.screen.index

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.ezlogin.room.AppDatabase
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val appDatabase: AppDatabase
) : ViewModel(){
    val allAccounts = appDatabase.getAccountDao().getAllAccounts().also {
        viewModelScope.launch {
            appDatabase.getAccountDao().cleanBadAccount()
        }
    }
}