package com.yuriihetsko.aurachallenge

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(
    workerParams: WorkerParameters,
    private val appContext: Context
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        // Display notification
        NotificationUtils.showNotification(appContext)

        return Result.success()
    }


}