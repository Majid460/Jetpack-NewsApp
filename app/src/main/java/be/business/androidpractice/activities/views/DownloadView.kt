package be.business.androidpractice.activities.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import be.business.androidpractice.ui.components.ElevatedBtn
import be.business.androidpractice.workmanager.DownloadWorker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Preview
@Composable
fun DownloadFileWithWorker(context: Context? = null) {
    var status by remember { mutableStateOf(mapOf("File1" to "Idle", "File2" to "Idle")) }
    val scope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(Modifier.height(5.dp))
        Text("WorkManager Demo", style = MaterialTheme.typography.titleLarge)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            status.forEach { (key, value) ->
                Text(text = "Status of $key :: $value")
            }
        }

        Spacer(Modifier.height(5.dp))
        ElevatedBtn("Start Work") {
            scope.launch {
                context?.let {
                    startParallelDownloads(it) { updated ->
                        status = status + updated
                    }
                }
            }
        }
    }

}

suspend fun startParallelDownloads(context: Context, onStatusUpdate: (Map<String, String>) -> Unit) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

    val file1 = OneTimeWorkRequestBuilder<DownloadWorker>()
        .setInputData(workDataOf("urlss" to "https://raw.githubusercontent.com/github/explore/main/topics/kotlin/kotlin.png"))
        .setConstraints(constraints)
        .build()

    val file2 = OneTimeWorkRequestBuilder<DownloadWorker>()
        .setInputData(workDataOf("url" to "https://raw.githubusercontent.com/github/explore/main/topics/flutter/flutter.png"))
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context)
        .beginWith(listOf(file1, file2))
        .enqueue()

    val wm = WorkManager.getInstance(context)

    coroutineScope {
        launch {
            wm.getWorkInfoByIdFlow(file1.id).collectLatest { info ->
                info?.let {
                    onStatusUpdate(mapOf("File1" to it.state.name))
                }
            }
        }

        launch {
            wm.getWorkInfoByIdFlow(file2.id).collectLatest { info ->
                info?.let {
                    onStatusUpdate(mapOf("File2" to it.state.name))
                    println(it.outputData)
                }
            }
        }
    }

}