package com.yuriihetsko.aurachallenge

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object NotificationSchedulerWorkManager {

    fun scheduleNotificationTask(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
            .build()

        println("GETZ.BootBroadcastReceiver.scheduleNotificationTask--> ")

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork("NotificationWork", ExistingPeriodicWorkPolicy.UPDATE, workRequest)
    }
}