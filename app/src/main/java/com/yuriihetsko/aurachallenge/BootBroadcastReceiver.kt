package com.yuriihetsko.aurachallenge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.yuriihetsko.aurachallenge.data.BootEventStorage

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