package me.rerere.ezlogin.util

import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator

fun checkGoogleSecret(
    secret: String
): Boolean = try {
    GoogleAuthenticator(secret).generate()
    true
} catch (e: Throwable) {
    false
}