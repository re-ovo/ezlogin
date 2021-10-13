package me.rerere.ezlogin.ui.screen.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import me.rerere.ezlogin.room.model.Account
import me.rerere.ezlogin.room.model.AppDatabase
import me.rerere.ezlogin.util.DataState
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val appDatabase: AppDatabase
) : ViewModel() {

    fun loadAccount(id: Int) = flow {
        emit(DataState.Loading)
        val result = appDatabase.getAccountDao().getAccountById(id)
        emit(DataState.Success(result))
    }

    fun save(account: Account, callback: () -> Unit) {
        viewModelScope.launch {
            appDatabase.getAccountDao().insert(account)
            callback()
        }
    }

    fun delete(
        id: Int,
        callback: () -> Unit
    ){
        viewModelScope.launch {
            appDatabase.getAccountDao().deleteById(id)
            callback()
        }
    }
}