package com.test.examplekmp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.test.examplekmp.presentation.ui.AppView

class MainActivity : ComponentActivity() {

    companion object {

        fun startActivityMain(context: Context, isNewTask: Boolean = false) =
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                if (isNewTask) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppView(this.defaultComponentContext())
        }
    }

    private fun selfStartActivityMain() {
        startActivityMain(this, true)
    }
}
