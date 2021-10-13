package me.rerere.ezlogin.ui.screen.edit

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import me.rerere.ezlogin.ui.component.SimpleEzTopBar
import me.rerere.ezlogin.ui.public.LocalNavController

@Composable
fun EditScreen(
    id: Int,
    editViewModel: EditViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            SimpleEzTopBar(
                navController = navController,
                title = "编辑"
            )
        }
    ) {

    }
}