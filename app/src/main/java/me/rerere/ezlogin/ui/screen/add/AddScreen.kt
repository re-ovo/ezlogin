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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import io.github.g00fy2.quickie.content.QRContent
import me.rerere.ezlogin.R
import me.rerere.ezlogin.ui.component.EzTopBar
import me.rerere.ezlogin.ui.component.navigationBackIcon
import me.rerere.ezlogin.ui.public.LocalNavController
import me.rerere.ezlogin.util.checkGoogleSecret
import me.rerere.ezlogin.util.stringResource
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
                    Text(text = stringResource(R.string.add_account))
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
                            context.toast(
                                context.stringResource(
                                    R.string.qrcode_bad_format
                                )
                            )
                        }
                    }
                }
                is QRResult.QRError -> {
                    context.toast(
                        text = context.stringResource(
                            R.string.qrcode_scan_error,
                            qrResult.exception.javaClass.name
                        )
                    )
                }
                is QRResult.QRMissingPermission -> {
                    context.toast(
                        context.stringResource(
                            R.string.qrcode_no_permission
                        )
                    )
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
                    Text(text = stringResource(R.string.secret))
                }
            )

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
                value = account,
                onValueChange = {
                    account = it
                },
                label = {
                    Text(text = stringResource(R.string.account_name))
                }
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (key.isBlank() || website.isBlank() || account.isBlank()) {
                        context.toast(
                            text = context.stringResource(
                                R.string.information_not_complete
                            )
                        )
                    } else if(!checkGoogleSecret(key)){
                        context.toast(
                            text = context.stringResource(
                                R.string.invalid_secret
                            )
                        )
                    } else {
                        addViewModel.addAccount(
                            key, website, account
                        )
                        navController.popBackStack()
                    }
                }
            ) {
                Text(text = stringResource(R.string.add_account))
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scanner.launch(null)
                }
            ) {
                Text(text = stringResource(R.string.scan_qr_code))
            }
        }
    }
}