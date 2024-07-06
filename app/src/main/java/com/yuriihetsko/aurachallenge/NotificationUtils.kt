package com.yuriihetsko.aurachallenge

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.yuriihetsko.aurachallenge.data.BootEventStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object NotificationUtils {

    // todo investigate why notification doesn't show
    // todo 1) check is everything is ok with smallIcon
    // todo 2) check is
    fun showNotification(context: Context) {

        //todo handle permission if not granted
        //todo show notification for older android versions
        if (SDK_INT >= TIRAMISU) {
            if (checkSelfPermission(context, POST_NOTIFICATIONS) == PERMISSION_GRANTED) {
                val bootEvents = BootEventStorage.getBootEvents(context)
                println("GETZ.NotificationUtils.showNotification--> bootEvents=$bootEvents")

                //todo check this logic and extract it to another method or use case
                val bodyText = when (bootEvents.size) {
                    0 -> "No boots detected"
                    1 -> "The boot was detected = ${formatDate(bootEvents[0])}"
                    else -> {
                        val lastBoot = bootEvents.last()
                        val secondLastBoot = bootEvents[bootEvents.size - 2]
                        val delta = TimeUnit.MILLISECONDS.toMinutes(lastBoot - secondLastBoot)
                        "Last boots time delta = $delta minutes"
                    }
                }


                //todo test dismiss of notification
                val dismissIntent = Intent(context, NotificationDismissalReceiver::class.java)
                val dismissPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    dismissIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val notificationManager = NotificationManagerCompat.from(context)
                val notification = NotificationCompat.Builder(context, AuraChallengeApplication.BOOT_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Device Booted")
                    .setContentText(bodyText)
                    .setAutoCancel(true)
                    .setDeleteIntent(dismissPendingIntent) // Set the dismissal action
                    .build()

                notificationManager.notify(1, notification)
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}