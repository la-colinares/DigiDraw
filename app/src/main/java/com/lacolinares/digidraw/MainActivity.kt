package com.lacolinares.digidraw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lacolinares.digidraw.ui.NavGraphs
import com.lacolinares.digidraw.ui.theme.DigiDrawTheme
import com.lacolinares.digidraw.ui.theme.MineralGreen
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DigiDrawTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MineralGreen) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}