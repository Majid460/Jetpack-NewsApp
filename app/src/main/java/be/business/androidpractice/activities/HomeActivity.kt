package be.business.androidpractice.activities

import android.R.attr.name
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion.then
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import be.business.androidpractice.MainActivity
import be.business.androidpractice.activities.views.DownloadFileWithWorker
import be.business.androidpractice.services.BatteryRepository
import be.business.androidpractice.services.BatteryRepository.batteryLevel
import be.business.androidpractice.services.BatteryService
import be.business.androidpractice.services.DownloadProgress
import be.business.androidpractice.services.DownloadService
import be.business.androidpractice.ui.components.ElevatedBtn
import be.business.androidpractice.ui.components.TopBarWithBack
import be.business.androidpractice.ui.theme.AndroidPracticeTheme
import be.business.androidpractice.workmanager.WorkBasic
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    val context: Context = this
    private lateinit var username: String
    private var emailAddress by mutableStateOf("")
    var downloadProgress by mutableIntStateOf(-1)


    // Class-level variable to persist across onStop/onStart
    companion object {
        private var emailSaved: String = ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get username from intent
        username = intent.getStringExtra("Name").toString()

        // Restore lastName from saved state OR from savedLastName (for back navigation)
        val restoredFromState = savedInstanceState?.getString("lastname")
        val restoredFromSaved = emailSaved.takeIf { it.isNotEmpty() }

        emailAddress = restoredFromState ?: ""

        Log.d("Lifecycle", "onCreate — savedInstanceState is null: ${savedInstanceState == null}")
        Log.d("Lifecycle", "onCreate — restoredFromState: $restoredFromState")
        Log.d("Lifecycle", "onCreate — restoredFromSaved: $restoredFromSaved")
        Log.d("Lifecycle", "onCreate — final lastName: $emailAddress")
        startService(Intent(this, BatteryService::class.java))

        var batteryLevel by mutableIntStateOf(-1)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                BatteryRepository.batteryLevel.collectLatest {
                    batteryLevel = it
                }
            }
        }
        setContent {
            AndroidPracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    HomeView(
                        name = "Welcome $username to Home Activity",
                        email = emailAddress,
                        onChange = { s -> emailAddress = s },
                        modifier = Modifier.padding(innerPadding),
                        context,
                        batteryLevel,
                        onDownload = { downloadFile() }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Restore from companion object when coming back from another activity
        if (emailSaved.isNotEmpty()) {
            Log.d("Lifecycle", "onStart - Before restore, lastName = '$emailAddress'")
            emailAddress = emailSaved
            Log.d("Lifecycle", "onStart - After restore, lastName = '$emailAddress'")
        } else {
            Log.d("Lifecycle", "onStart called - nothing to restore")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        // Save to companion object for when we come back from MainActivity
        emailSaved = emailAddress
        Log.d("Lifecycle", "onStop called - saved to companion: $emailSaved")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "onDestroy called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("Lifecycle", "Saving lastName: $emailAddress")
        outState.putString("lastname", emailAddress)
        outState.putString("username", username)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadFile() {
        lifecycleScope.launch {
            val testUrl =
                "https://raw.githubusercontent.com/github/explore/main/topics/android/android.png"
            val intent = Intent(context, DownloadService::class.java).apply {
                putExtra("file_url", testUrl)
            }

            startForegroundService(intent)
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                DownloadProgress.progress.collectLatest {
                    println("Progress:: $it")
                    downloadProgress = it
                }
            }
        }

    }
}

suspend fun scheduleWork(context: Context, status: (String) -> Unit) {

    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .setRequiresCharging(false)
        .build()
    // Pass params in work request using setInputData
    val workRequest: WorkRequest =
        OneTimeWorkRequestBuilder<WorkBasic>().setConstraints(constraints)
            .setInputData(workDataOf("name" to "Majid", "A" to "B")).setExpedited(
                OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST
            ).build()

    /*In this example, we initialize an instance of OneTimeWorkRequest and call setExpedited() on it.
    This request then becomes expedited work. If the quota allows, it will begin to run immediately in the background.
    If the quota has been used, the OutOfQuotaPolicy parameter indicates that the request should be run as normal, non-expedited work.*/
    val manager = WorkManager.getInstance(context).enqueue(workRequest)
    val res = manager.result
    println("Result of work:: $res")

    WorkManager.getInstance(context).getWorkInfoByIdFlow(workRequest.id).collectLatest { info ->
        if (info != null) {
            status(info.state.name)
        }
    }


}

fun navigateToMain(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.putExtra("Name", "Majid")
    context.startActivity(intent)
}

fun sendEmail(email: String, context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri() // Ensures only email apps handle it
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, "Hello!")
        putExtra(Intent.EXTRA_TEXT, "This is a test email.")
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
    }

}

@Composable
fun InputField(email: String, onChange: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { s -> onChange(s) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .testTag("email"),
        placeholder = { Text("Enter Email Address..") },
    )
}

@Composable
fun HomeView(
    name: String,
    email: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    context: Context? = null,
    batteryLevel: Int,
    onDownload: () -> Unit,
) {
    var status by remember { mutableStateOf("Idle") }
    val scope = rememberCoroutineScope()
    var showDownloadWorkerView by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        Column {
            TopBarWithBack(title = "Worker", onBackClick = {
                showDownloadWorkerView = false
            }, showBack = showDownloadWorkerView)
            if (showDownloadWorkerView) {

                DownloadFileWithWorker(context)
            } else
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ElevatedBtn("Go To Worker") {
                        showDownloadWorkerView = true
                    }
                    Spacer(modifier.height(5.dp))
                    Text(
                        text = "Hello $name!",
                        modifier = modifier
                    )
                    InputField(email) { s ->
                        onChange(s)
                    }
                    Spacer(modifier.height(5.dp))
                    ElevatedBtn("Send Email") {
                        context?.let {
                            sendEmail(email, it)
                        }
                    }
                    Text(
                        text = "🔋 Battery Level: $batteryLevel%",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    if (batteryLevel in 0..100 && batteryLevel < 80) Toast.makeText(
                        context,
                        "Battery is lower than 80%",
                        Toast.LENGTH_SHORT
                    ).show()
                    Spacer(modifier.height(5.dp))
                    Text("WorkManager Demo", style = MaterialTheme.typography.titleLarge)
                    Text("Status: $status")
                    Spacer(modifier.height(5.dp))
                    ElevatedBtn("Start Work") {
                        scope.launch {
                            context?.let {
                                scheduleWork(it) { s ->
                                    status = s
                                }
                            }
                        }
                    }
                    Spacer(modifier.height(10.dp))
                    ElevatedBtn("To Main") {
                        context?.let {
                            navigateToMain(it)
                        }
                    }
                    Spacer(modifier.height(10.dp))
                    ElevatedBtn("Download File") {
                        onDownload()
                    }
                    DownloadProgressUI()
                }
        }

    }
}

@Composable
fun DownloadProgressUI() {
    val progress by DownloadProgress.progress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (progress >= 0) {
            Text(text = "Progress: $progress%")
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun GreetingPreview() {
    AndroidPracticeTheme {
        HomeView("Home View", "", {}, batteryLevel = -1, onDownload = {})
    }
}