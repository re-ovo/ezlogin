package me.rerere.ezlogin.ui.screen.edit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.rerere.ezlogin.room.model.AppDatabase
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val appDatabase: AppDatabase
) : ViewModel() {

}