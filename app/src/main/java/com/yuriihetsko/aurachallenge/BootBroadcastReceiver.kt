package com.yuriihetsko.aurachallenge

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yuriihetsko.aurachallenge.data.BootEventStorage
import java.util.concurrent.TimeUnit

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println("GETZ.BootBroadcastReceiver.onReceive--> intent.action=${intent.action}")

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            BootEventStorage.addBootEvent(context, System.currentTimeMillis())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val serviceIntent = Intent(context, BootService::class.java)
                context.startForegroundService(serviceIntent)
            } else {
                NotificationSchedulerWorkManager.scheduleNotificationTask(context)
            }
        }
    }


}

class NotificationWorker(
    workerParams: WorkerParameters,
    val appContext: Context
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        println("GETZ.NotificationWorker.doWork--> ")
        // Display notification
        showNotification(appContext)
        return Result.success()
    }

    private fun showNotification(context: Context) {
        println("GETZ.NotificationWorker.showNotification--> ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // Todo Permission is not granted, handle this scenario
                return
            }
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "boot_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Device Booted")
            .setContentText("The device has booted.")
            .setAutoCancel(true)
            .build()

        println("GETZ.NotificationWorker.showNotification--> notification=$notification")

        notificationManager.notify(1, notification)
    }
}