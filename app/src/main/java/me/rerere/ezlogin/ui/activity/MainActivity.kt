package me.rerere.ezlogin.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import me.rerere.ezlogin.ui.screen.about.AboutScreen
import me.rerere.ezlogin.ui.public.LocalNavController
import me.rerere.ezlogin.ui.screen.add.AddScreen
import me.rerere.ezlogin.ui.screen.edit.EditScreen
import me.rerere.ezlogin.ui.screen.index.IndexScreen
import me.rerere.ezlogin.ui.theme.EzloginTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberAnimatedNavController()
            ProvideWindowInsets {
                CompositionLocalProvider(LocalNavController provides navController) {
                    EzloginTheme {
                        val systemUiController = rememberSystemUiController()
                        val dark = MaterialTheme.colors.isLight

                        // set ui color
                        SideEffect {
                            systemUiController.setStatusBarColor(
                                Color.Transparent,
                                darkIcons = dark
                            )
                            systemUiController.setNavigationBarColor(
                                Color.Transparent,
                                darkIcons = dark
                            )
                        }

                        AnimatedNavHost(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            startDestination = "index",
                            enterTransition = { _, _ ->
                                slideInHorizontally(
                                    initialOffsetX = {
                                        it
                                    },
                                    animationSpec = tween()
                                )
                            },
                            exitTransition = { _, _ ->
                                slideOutHorizontally(
                                    targetOffsetX = {
                                        -it
                                    },
                                    animationSpec = tween()
                                )
                            },
                            popEnterTransition = { _, _ ->
                                slideInHorizontally(
                                    initialOffsetX = {
                                        -it
                                    },
                                    animationSpec = tween()
                                )
                            },
                            popExitTransition = { _, _ ->
                                slideOutHorizontally(
                                    targetOffsetX = {
                                        it
                                    },
                                    animationSpec = tween()
                                )
                            }
                        ) {
                            composable("index") {
                                IndexScreen()
                            }

                            composable("add") {
                                AddScreen()
                            }

                            composable(
                                route = "edit/{id}",
                                arguments = listOf(
                                    navArgument("id") {
                                        type = NavType.IntType
                                        defaultValue = 0
                                    }
                                )
                            ) {
                                EditScreen(id = it.arguments?.getInt("id") ?: 0)
                            }

                            composable("about") {
                                AboutScreen()
                            }
                        }
                    }
                }
            }
        }

        // Prevent forced secondary inversion
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val existingComposeView = window.decorView
                .findViewById<ViewGroup>(android.R.id.content)
                .getChildAt(0) as? ComposeView
            existingComposeView?.isForceDarkAllowed = false
        }
    }
}