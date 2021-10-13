package me.rerere.ezlogin.ui.screen.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rerere.ezlogin.R
import me.rerere.ezlogin.ui.component.EzTopBar
import me.rerere.ezlogin.ui.component.navigationBackIcon
import me.rerere.ezlogin.ui.public.LocalNavController
import me.rerere.ezlogin.util.openUrl

@Composable
fun AboutScreen() {
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            EzTopBar(
                navigationIcon = navigationBackIcon(navController),
                title = {
                    Text(text = stringResource(R.string.about))
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(text = "A 2FA android client based on Jetpack Compose, enjoy it!")

            Divider(
                modifier = Modifier.padding(vertical = 5.dp)
            )

            Text(
                text = "Libraries:",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LibraryCard(
                name = "kotlin-onetimepassword",
                description = "A Kotlin one-time password library to generate \"Google Authenticator\", \"Time-based One-time Password\" (TOTP) and \"HMAC-based One-time Password\" (HOTP) codes based on RFC 4226 and 6238.",
                link = "https://github.com/marcelkliemannel/kotlin-onetimepassword"
            )

            LibraryCard(
                name = "Quickie",
                description = "quickie is a Quick Response (QR) Code scanning library for Android that is based on CameraX and ML Kit on-device barcode detection. It's an alternative to ZXing based libraries and written in Kotlin",
                link = "https://github.com/G00fY2/quickie"
            )
        }
    }
}

@Composable
private fun LibraryCard(
    name: String,
    description: String,
    link: String
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp, vertical = 4.dp)
            .clickable {
                context.openUrl(link)
            },
        elevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.medium
            ) {
                Text(
                    text = description
                )
            }
        }
    }
}