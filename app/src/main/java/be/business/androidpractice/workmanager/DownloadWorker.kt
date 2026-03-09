package be.business.androidpractice.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import be.business.androidpractice.workmanager.DownloadWorker.Companion.CHANNEL_ID
import be.business.androidpractice.workmanager.DownloadWorker.Companion.NOTIF_ID
import kotlinx.coroutines.delay

class DownloadWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    companion object {
        const val CHANNEL_ID = "download_channel"
        const val NOTIF_ID = 1
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    override suspend fun doWork(): Result {
        return try {
            createNotificationChannel()

            val inputData = inputData.keyValueMap
            val fileUrl = inputData["url"]?.toString()?.takeIf { it.isNotBlank() }
                ?: return Result.failure(workDataOf("message" to "No file URL provided"))

            val fileName = fileUrl.substringAfterLast("/")

            setForeground(createForegroundInfo(0, "Starting download..."))

            for (i in 1..100) {
                delay(100) // Simulate progress
                setProgress(workDataOf("progress" to i))
                setForeground(createForegroundInfo(i, "Downloading $fileName: $i%"))
            }

            val downloadedPath = "/storage/emulated/0/Download/$fileName"
            val output = workDataOf(
                "file_name" to fileName,
                "file_path" to downloadedPath
            )

            setForeground(createForegroundInfo(100, "Download complete ✅"))
            Result.success(output)

        } catch (e: Exception) {
            val output = workDataOf("message" to (e.message ?: "Unknown error"))
            Result.failure(output)
        }
    }

    fun createForegroundInfo(progress: Int, message: String): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Downloading File")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setOngoing(progress < 100)
            .setProgress(100, progress, false)
            .build()
        return ForegroundInfo(NOTIF_ID, notification)

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Download Notifications",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}