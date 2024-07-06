package com.yuriihetsko.aurachallenge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationDismissalReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        // Reschedule notification task without the 15-minute rule
        NotificationSchedulerWorkManager.scheduleNotificationTask(context)
    }
}