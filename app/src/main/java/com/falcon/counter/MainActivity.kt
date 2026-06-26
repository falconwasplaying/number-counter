package com.falcon.counter
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.falcon.core.decrease
import com.falcon.core.increase
import com.falcon.core.reset
import com.falcon.counter.ui.theme.CounterTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.time.Duration.Companion.milliseconds

// create datastore for settings
val ComponentActivity.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {

    private val counterKey = intPreferencesKey("counter_value") // counter value as counterKey

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            var counter by remember { mutableIntStateOf(0) }
            val scope = rememberCoroutineScope()
            var showSplash: Boolean by remember { mutableStateOf(true) }
            val darkMode = isSystemInDarkTheme()

            Box(modifier = Modifier.fillMaxSize()) {
                CounterTheme(darkTheme = darkMode) {

                    val view = LocalView.current

                    // using side effect for navbar and status bar to be consistent with theme
                    SideEffect {
                        val window = (view.context as Activity).window

                        // status bar
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                            !darkMode

                        // navbar
                        WindowCompat.getInsetsController(
                            window,
                            view
                        ).isAppearanceLightNavigationBars = !darkMode
                    }

                    // how it works, ig
                    CounterScreen(
                        counter = counter,
                        onIncrease = {
                            counter = increase(counter, scope, dataStore)
                        },
                        onDecrease = {
                            counter = decrease(counter, scope, dataStore)
                        },
                        onReset = {
                            counter = reset(scope, dataStore)
                        }
                    )

                    AnimatedVisibility(
                        visible = showSplash,
                        enter = EnterTransition.None,
                        exit = fadeOut(animationSpec = tween(durationMillis = 500))
                    ) {
                        SplashScreenContent()
                    }
                }
            }
            LaunchedEffect(Unit) {
                counter = dataStore.data.first()[counterKey] ?: 0
                delay(600.milliseconds)
                showSplash = false
            }
        }
    }
}

// ui stuff
@Composable
fun CounterScreen(
    counter: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "$counter",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 64.sp
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onIncrease) {
                Text(
                    text = "+",
                    fontSize = 24.sp
                )
            }

            Spacer(Modifier.width(10.dp))

            Button(onClick = onDecrease) {
                Text(
                    text = "-",
                    fontSize = 24.sp
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Button(onClick = onReset) {
            Text(
                text = "Reset",
                fontSize = 20.sp
            )
        }
    }
}

val TimesNewRoman = FontFamily(
    Font(R.font.times_new_roman_mt_regular, FontWeight.Normal)
)

// MY custom splash screen
@Composable
fun SplashScreenContent() {
    val backgroundColor = MaterialTheme.colorScheme.background
    val contentColor = MaterialTheme.colorScheme.onBackground

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // 1. Draw your solid diagonal path line
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = contentColor,
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                strokeWidth = 4f
            )
        }

        // Shared line alignment multipliers
        val linePositionZero = 0.42f
        val linePositionOne = 0.58f

        // 2. THE NUMBER 0 (Shifted Down-Left off the line)
        Text(
            text = "0",
            color = contentColor,
            fontFamily = TimesNewRoman,
            fontSize = 90.sp, // Made it big like your sketch
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .offset(
                    x = (screenWidth * linePositionZero) - 30.dp, // Shifting Left
                    y = (screenHeight * linePositionZero) + 70.dp  // Shifting Down
                )
        )

        // 3. THE NUMBER 1 (Shifted Up-Right off the line)
        Text(
            text = "1",
            color = contentColor,
            fontFamily = TimesNewRoman,
            fontSize = 90.sp,
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .offset(
                    x = (screenWidth * linePositionOne) + 0.dp, // Shifting Right
                    y = (screenHeight * linePositionOne) - 120.dp  // Shifting Up
                )
        )
    }
}