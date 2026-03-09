package be.business.androidpractice.services

import android.app.BroadcastOptions
import android.app.Service
import android.content.Intent
import android.os.BatteryManager
import android.os.Handler
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class BatteryService() : Service() {
    private lateinit var handler: Handler
    private val checkInterval = 5000L // check every 5 seconds
    private var isRunning = false
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        handler = Handler(mainLooper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        startCheckingBattery()
        return START_STICKY
    }

    fun startCheckingBattery() {
//        executorService.scheduleWithFixedDelay({
//            if (!isRunning) return@scheduleWithFixedDelay
//            val bm = getSystemService(BATTERY_SERVICE) as BatteryManager
//            val batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
//            BatteryRepository.updateBatteryLevel(batteryLevel)
//
//            if (batteryLevel <= 80) {
//                val alertIntent = Intent("LOW_BATTERY_ALERT")
//                sendBroadcast(alertIntent)
//
//            }
//        }, 0, 5, TimeUnit.SECONDS)

        serviceScope.launch {
            while (isRunning) {
                val bm = getSystemService(BATTERY_SERVICE) as BatteryManager
                val batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

                // update shared flow
                BatteryRepository.updateBatteryLevel(batteryLevel)

                if (batteryLevel <= 20) {
                    val alertIntent = Intent("LOW_BATTERY_ALERT")
                    sendBroadcast(alertIntent)
                }

                delay(checkInterval)
            }
        }

    }
    override fun onDestroy() {
        isRunning = false
        handler.removeCallbacksAndMessages(null)
        executorService.shutdown()
        serviceScope.cancel()
        super.onDestroy()
    }
}

object BatteryRepository {
    private val _batteryLevel = MutableStateFlow(-1)
    val batteryLevel = _batteryLevel.asStateFlow()

    fun updateBatteryLevel(level: Int) {
        _batteryLevel.value = level
    }
}