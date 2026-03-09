package be.business.androidpractice.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay

/*
Task scheduling

When you want to execute tasks that will continue to run even if the app leaves the visible state, we recommend using the Jetpack library WorkManager. WorkManager features a robust scheduling mechanism that lets tasks persist across app restarts and device reboots.

Types of work
WorkManager handles three types of work:

Immediate: Tasks that must begin immediately and complete soon. May be expedited.
Long Running: Tasks which might run for longer, potentially longer than 10 minutes.
Deferrable: Scheduled tasks that start at a later time and can run periodically.

Use WorkManager for reliable work
WorkManager is intended for work that is required to run reliably even if the user navigates off a screen, the app exits, or the device restarts. For example:

Sending logs or analytics to backend services.
Periodically syncing application data with a server.
WorkManager is not intended for in-process background work that can safely be terminated if the app process goes away. It is also not a general solution for all work that requires immediate execution. Please review the background processing guide to see which solution meets your needs.


*/
class WorkBasic(context: Context,params: WorkerParameters): CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {
        val inputA = inputData.keyValueMap
        println(inputA)
        for (i in 1..5) {
            Log.d("WorkManagerDemo", "Working... step $i")
            delay(1000) // wait 1 second
        }

        Log.d("WorkManagerDemo", "Work completed ✅")
        return Result.success()
    }

}