package io.github.lamba92.corpore.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.lamba92.corpore.app.core.ui.CorporeApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorporeApp(onboardingBackHandler = { BackHandler(onBack = it::onBackClick) })
        }
    }
}
