package com.falcon.counter

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.falcon.counter.ui.theme.CounterTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// create datastore
val ComponentActivity.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {

    private val counterKey = intPreferencesKey("counter_value")

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            val bg = MaterialTheme.colorScheme.surface

            window.statusBarColor = bg.toArgb()
            window.navigationBarColor = bg.toArgb()

            CounterTheme {

                val darkMode = isSystemInDarkTheme()

                val controller = WindowInsetsControllerCompat(window, window.decorView)

                SideEffect {
                    // background color
                    window.statusBarColor = androidx.compose.ui.graphics.Color.Transparent.toArgb()
                    window.navigationBarColor = androidx.compose.ui.graphics.Color.Transparent.toArgb()

                    // ICON COLORS
                    controller.isAppearanceLightStatusBars = !darkMode
                    controller.isAppearanceLightNavigationBars = !darkMode

                    // make bars transparent
                    window.setDecorFitsSystemWindows(false)
                }


                val scope = rememberCoroutineScope()

                // load initial value
                var counter by remember { mutableIntStateOf(0) }

                LaunchedEffect(Unit) {
                    counter = dataStore.data.first()[counterKey] ?: 0
                }


                // UI
                CounterScreen(

                    counter = counter,
                    onIncrement = {
                            counter++
                            scope.launch {
                                dataStore.edit { prefs ->
                                    prefs[counterKey] = counter
                                }
                            }
                    },
                    onReset = {
                        counter = 0
                            scope.launch {
                                dataStore.edit { prefs ->
                                    prefs[counterKey] = 0
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun CounterScreen(
    counter: Int,
    onIncrement: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .consumeWindowInsets(WindowInsets(0,0,0,0)),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "$counter",
            color = MaterialTheme.colorScheme.onBackground
        )


        Spacer(Modifier.height(20.dp))

        Button(onClick = onIncrement) {
            Text("Add +1")
        }

        Spacer(Modifier.height(10.dp))

        Button(onClick = onReset) {
            Text("Reset")
        }
    }
}
