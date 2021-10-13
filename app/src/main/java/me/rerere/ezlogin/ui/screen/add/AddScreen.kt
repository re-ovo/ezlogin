package me.rerere.ezlogin.ui.screen.add

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import io.github.g00fy2.quickie.content.QRContent
import me.rerere.ezlogin.ui.component.EzTopBar
import me.rerere.ezlogin.ui.component.navigationBackIcon
import me.rerere.ezlogin.ui.public.LocalNavController
import me.rerere.ezlogin.util.toast

@Composable
fun AddScreen(
    addViewModel: AddViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    Scaffold(
        topBar = {
            EzTopBar(
                navigationIcon = navigationBackIcon(navController),
                title = {
                    Text(text = "添加账号")
                }
            )
        }
    ) {
        Body(addViewModel)
    }
}

@Composable
private fun Body(
    addViewModel: AddViewModel
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    var key by remember {
        mutableStateOf("")
    }
    var account by remember {
        mutableStateOf("")
    }
    var website by remember {
        mutableStateOf("")
    }
    val scanner =
        rememberLauncherForActivityResult(contract = ScanQRCode(), onResult = { qrResult ->
            when (qrResult) {
                is QRResult.QRSuccess -> {
                    when (qrResult.content) {
                        is QRContent.Plain -> {
                            val url = Uri.parse(qrResult.content.rawValue)
                            println(url)
                            if (url.scheme == "otpauth" && url.host == "totp") {
                                url.path?.let {
                                    account = it.let { str ->
                                        if (str.startsWith("/") && str.length > 1) {
                                            str.substring(1)
                                        } else {
                                            str
                                        }
                                    }
                                }
                                url.getQueryParameter("issuer")?.let {
                                    website = it
                                }
                                url.getQueryParameter("secret")?.let {
                                    key = it
                                }
                            }
                        }
                        else -> {
                            context.toast("二维码格式错误！")
                        }
                    }
                }
                is QRResult.QRError -> {
                    context.toast(
                        text = "扫描二维码时发生错误: ${qrResult.exception.javaClass.name}"
                    )
                }
                is QRResult.QRMissingPermission -> {
                    context.toast("缺少相机权限，无法扫描二维码!")
                }
                else -> {
                    // IGNORE
                }
            }
        })
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = key,
                onValueChange = {
                    key = it
                },
                label = {
                    Text(text = "秘钥")
                }
            )

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
                value = account,
                onValueChange = {
                    account = it
                },
                label = {
                    Text(text = "账号")
                }
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    addViewModel.addAccount(
                        key, website, account
                    )
                    navController.popBackStack()
                }
            ) {
                Text(text = "添加")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scanner.launch(null)
                }
            ) {
                Text(text = "扫描二维码快速添加")
            }
        }
    }
}