package be.business.androidpractice.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL

class DownloadService() : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    override fun onBind(p0: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val fileUrl = intent?.getStringExtra("file_url") ?: return START_NOT_STICKY
        val fileName = fileUrl.substringAfterLast("/")

        // Start foreground immediately with 0% progress
        startForeground(1, buildNotification("Downloading $fileName: 0%", ongoing = true))

        // Start download coroutine
        serviceScope.launch {
            downloadFile(fileUrl, fileName)
        }

        return START_NOT_STICKY
    }


    private suspend fun downloadFile(url: String, fileName: String) {
        var outputStream: OutputStream? = null
        var outputFile: File? = null

        try {
            val connection = URL(url).openConnection()
            val totalSize = connection.contentLength
            val input = connection.getInputStream()

            // Save to public Downloads directory based on Android version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ - Use MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(fileName))
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                outputStream = uri?.let { contentResolver.openOutputStream(it) }
            } else {
                // Android 9 and below - Use legacy storage
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                outputFile = File(downloadsDir, fileName)
                outputStream = FileOutputStream(outputFile)
            }

            if (outputStream == null) {
                throw Exception("Failed to create output stream")
            }

            val buffer = ByteArray(4096)
            var bytesRead: Int
            var downloaded = 0
            var lastProgress = -1

            while (input.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                downloaded += bytesRead

                val progress = if (totalSize > 0) (downloaded * 100 / totalSize) else 0

                // Only update notification if progress changed by at least 1%
                if (progress != lastProgress) {
                    lastProgress = progress
                    DownloadProgress.update(progress)
                    updateNotification("Downloading $fileName: $progress%", ongoing = true)
                }
            }

            input.close()
            outputStream.flush()
            outputStream.close()

            // For Android 9 and below, trigger media scan
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && outputFile != null) {
                MediaScannerConnection.scanFile(
                    this,
                    arrayOf(outputFile.absolutePath),
                    arrayOf(getMimeType(fileName)),
                    null
                )
            }

            // Update final notification and stop foreground
            DownloadProgress.update(100)
            updateNotification("Download complete ✅ File saved to Downloads", ongoing = false)

            // Stop the foreground service and remove the ongoing notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_DETACH)
            } else {
                @Suppress("DEPRECATION")
                stopForeground(false)
            }

            // Stop the service after a short delay to show completion message
            kotlinx.coroutines.delay(2000)
            stopSelf()

        } catch (e: Exception) {
            e.printStackTrace()
            DownloadProgress.update(-1)
            updateNotification("Download failed ❌: ${e.message}", ongoing = false)

            // Stop foreground and service on error
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_DETACH)
            } else {
                @Suppress("DEPRECATION")
                stopForeground(false)
            }

            kotlinx.coroutines.delay(3000)
            stopSelf()
        } finally {
            outputStream?.close()
        }
    }

    private fun getMimeType(fileName: String): String {
        return when (fileName.substringAfterLast('.').lowercase()) {
            "png" -> "image/png"
            "jpg", "jpeg" -> "image/jpeg"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            "pdf" -> "application/pdf"
            "mp4" -> "video/mp4"
            "mp3" -> "audio/mpeg"
            else -> "application/octet-stream"
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "download_channel",
            "Downloads",
            NotificationManager.IMPORTANCE_LOW
        )
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
    }

    private fun updateNotification(message: String, ongoing: Boolean) {
        val notification = buildNotification(message, ongoing)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
    }

    private fun buildNotification(message: String, ongoing: Boolean = false): Notification {
        return NotificationCompat.Builder(this, "download_channel")
            .setContentTitle("File Download")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setOngoing(ongoing)
            .build()
    }

}

object DownloadProgress {
    private val _progress = MutableStateFlow(-1)
    val progress = _progress.asStateFlow()

    fun update(v: Int) {
        _progress.value = v
    }
}