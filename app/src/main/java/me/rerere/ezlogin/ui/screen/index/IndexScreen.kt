package me.rerere.ezlogin.ui.screen.index

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import me.rerere.ezlogin.R
import me.rerere.ezlogin.room.model.Account
import me.rerere.ezlogin.ui.component.EzTopBar
import me.rerere.ezlogin.ui.public.LocalNavController
import me.rerere.ezlogin.ui.theme.DigitFont
import me.rerere.ezlogin.util.setClipboard
import me.rerere.ezlogin.util.toast
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IndexScreen(
    indexViewModel: IndexViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            EzTopBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name)
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("add")
                    }) {
                        Icon(Icons.Default.Add, null)
                    }

                    Menu()
                }
            )
        }
    ) {
        Body(
            indexViewModel = indexViewModel
        )
    }
}

@Composable
private fun Menu() {
    var showMenu by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = {
        showMenu = true
    }) {
        Icon(Icons.Default.Menu, null)
    }

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = {
            showMenu = false
        }) {
        DropdownMenuItem(onClick = { /*TODO*/ }) {
            Text(text = "导出/备份")
        }
        DropdownMenuItem(onClick = { /*TODO*/ }) {
            Text(text = "设置")
        }
        DropdownMenuItem(onClick = { /*TODO*/ }) {
            Text(text = "关于")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Body(
    indexViewModel: IndexViewModel
) {
    val accounts by indexViewModel.allAccounts.collectAsState(initial = emptyList())

    // Generate time progress
    val progress by produceState(initialValue = 0f) {
        while (true) {
            kotlinx.coroutines.delay(100)
            val start = (System.currentTimeMillis() / 30_000) * 30_000
            value = (System.currentTimeMillis() - start).toFloat() / 30_000f
            println(value)
        }
    }

    Column {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            progress = progress
        )
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(accounts) {
                AccountCard(
                    it
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AccountCard(
    account: Account
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val code by produceState(initialValue = "") {
        val encryptor = GoogleAuthenticator(
            base32secret = account.key
        )
        value = encryptor.generate()
        while (true) {
            kotlinx.coroutines.delay(1000)
            value = encryptor.generate()
        }
    }
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    context.setClipboard(code)
                    context.toast(
                        text = "已复制到剪贴板 ($code)"
                    )
                },
                onLongClick = {
                    navController.navigate("edit/${account.primaryKey}")
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // website name
            Text(
                text = account.website,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            // account
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.medium
            ) {
                Text(
                    text = account.account,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
            Divider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = code,
                fontWeight = FontWeight.Bold,
                fontSize = 45.sp,
                fontFamily = DigitFont
            )
        }
    }
}