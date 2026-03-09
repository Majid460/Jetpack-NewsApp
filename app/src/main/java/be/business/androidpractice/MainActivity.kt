package be.business.androidpractice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import be.business.androidpractice.activities.HomeActivity
import be.business.androidpractice.coroutines.createAChannel
import be.business.androidpractice.coroutines.passMessages
import be.business.androidpractice.ui.components.ElevatedBtn
import be.business.androidpractice.ui.theme.AndroidPracticeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val context: Context = this
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            this.callAllCoroutines()
        }
        enableEdgeToEdge()
        setContent {
            AndroidPracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        context
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    suspend fun CoroutineScope.callAllCoroutines() {
//        val job1 = async {coroutines()}
//        val job2 = async { supervisorScopeFetchAll()}
//        val job3 = async { scope.launchAsBuilder() }
//        val job4 = async { scope.asyncAsBuilder() }
        val job5 = async { createAChannel() }
        val job6 = async { passMessages() }
        job6.await()
//        awaitAll(job1,job2,job3,job4,job5)
    }

}

fun navigateToHome(context: Context) {
    val intent = Intent(context, HomeActivity::class.java)
    intent.putExtra("Name", "Majid")
    context.startActivity(intent)

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, context: Context? = null) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    println("In on start")
                }

                Lifecycle.Event.ON_RESUME -> {
                    println("In on resume")
                }

                Lifecycle.Event.ON_PAUSE -> {
                    println("In on Pause")
                }

                Lifecycle.Event.ON_STOP -> {
                    println("In on resume")
                }

                Lifecycle.Event.ON_DESTROY -> {
                    println("In on Destroy")
                }

                else -> {
                    println("else events")
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Box(Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Hello $name!",
                modifier = modifier
            )
            ElevatedBtn("To Home") {
                context?.let { navigateToHome(context = it) }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidPracticeTheme {
        Greeting("Android")
    }
}