package me.rerere.ezlogin.ui.screen.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.rerere.ezlogin.R
import me.rerere.ezlogin.room.model.Account
import me.rerere.ezlogin.ui.component.EzTopBar
import me.rerere.ezlogin.ui.component.navigationBackIcon
import me.rerere.ezlogin.ui.public.LocalNavController
import me.rerere.ezlogin.util.DataState
import me.rerere.ezlogin.util.checkGoogleSecret
import me.rerere.ezlogin.util.stringResource
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
                    Text(text = stringResource(R.string.edit_account))
                },
                actions = {
                    IconButton(onClick = {
                        editViewModel.delete(id) {
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
    when (account) {
        is DataState.Loading,
        is DataState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.loading))
            }
        }
        is DataState.Success -> {
            account.readSafely()?.let {
                Editor(it, editViewModel)
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.account_not_exists))
                }
            }
        }
        is DataState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.load_error))
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
                    Text(text = stringResource(R.string.website_name))
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = accountName,
                onValueChange = {
                    accountName = it
                },
                label = {
                    Text(text = stringResource(R.string.account_name))
                }
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (website.isBlank() || accountName.isBlank()) {
                        context.toast(
                            text = context.stringResource(
                                R.string.information_not_complete
                            )
                        )
                    } else {
                        account.account = accountName
                        account.website = website
                        editViewModel.save(account) {
                            context.toast(
                                context.stringResource(R.string.account_saved)
                            )
                        }
                    }
                }
            ) {
                Text(
                    text = stringResource(
                        R.string.save
                    )
                )
            }
        }
    }
}