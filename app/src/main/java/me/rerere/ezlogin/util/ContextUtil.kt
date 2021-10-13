package me.rerere.ezlogin.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast

val Context.autoRotation: Boolean
    get() = try {
        Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

fun Context.stringResource(id: Int) = this.resources.getString(id)

fun Context.toast(
    text: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, text, duration).show()
}

fun Context.stringResource(id: Int, vararg formatArgs: Any) =
    this.resources.getString(id, *formatArgs)

fun Context.openUrl(url: String) {
    Toast.makeText(this, "打开链接: $url", Toast.LENGTH_SHORT).show()
    try {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url.let {
                if (!it.startsWith("https://")) "https://$it" else it
            })
        )
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(
            this,
            "打开链接失败: ${e.javaClass.simpleName}(${e.localizedMessage})",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun Context.setClipboard(text: String) {
    val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text))
}

fun Context.getVersionName(): String {
    var versionName = ""
    try {
        //获取软件版本号，对应AndroidManifest.xml下android:versionName
        versionName = this.packageManager.getPackageInfo(this.packageName, 0).versionName;
    } catch (e: Exception) {
        e.printStackTrace();
    }
    return versionName;
}