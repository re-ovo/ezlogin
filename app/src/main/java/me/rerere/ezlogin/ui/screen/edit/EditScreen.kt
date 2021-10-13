package me.rerere.ezlogin.ui.screen.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.rerere.ezlogin.room.model.Account
import me.rerere.ezlogin.ui.component.EzTopBar
import me.rerere.ezlogin.ui.component.navigationBackIcon
import me.rerere.ezlogin.ui.public.LocalNavController
import me.rerere.ezlogin.util.DataState
import me.rerere.ezlogin.util.toast

@Composable
fun EditScreen(
    id: Int,
    editViewModel: EditViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            EzTopBar(
                navigationIcon = navigationBackIcon(navController),
                title = {
                    Text(text = "编辑")
                },
                actions = {
                    IconButton(onClick = {
                        editViewModel.delete(id){
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.Delete, null)
                    }
                }
            )
        }
    ) {
        Body(id, editViewModel)
    }
}

@Composable
private fun Body(
    id: Int,
    editViewModel: EditViewModel
) {
    val account by remember { editViewModel.loadAccount(id) }.collectAsState(initial = DataState.Empty)
    when(account){
        is DataState.Loading,
        is DataState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "Loading")
            }
        }
        is DataState.Success -> {
            account.readSafely()?.let {
                Editor(it, editViewModel)
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "Account does not exits")
                }
            }
        }
        is DataState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "Error")
            }
        }
    }
}

@Composable
private fun Editor(
    account: Account,
    editViewModel: EditViewModel
) {
    val context = LocalContext.current
    var accountName by remember {
        mutableStateOf(account.account)
    }
    var website by remember {
        mutableStateOf(account.website)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = website,
                onValueChange = {
                    website = it
                },
                label = {
                    Text(text = "网站")
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = accountName,
                onValueChange = {
                    accountName = it
                },
                label = {
                    Text(text = "账号")
                }
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    account.account = accountName
                    account.website = website
                    editViewModel.save(account){
                        context.toast("保存完成")
                    }
                }
            ) {
                Text(text = "保存")
            }
        }
    }
}