package com.example.todolist.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.example.todolist.R
import com.example.todolist.ui.ListDetailActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object NotificationHelper {

    const val CHANNEL_ID = "todo_deadline_reminders"

    fun ensureNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun showDeadlineNotification(
        context: Context,
        listId: Long,
        listTitle: String,
        deadlineMillis: Long
    ) {
        ensureNotificationChannel(context)

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) &&
            (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            return
        }

        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val formattedDeadline = dateFormat.format(Date(deadlineMillis))

        val detailIntent = Intent(context, ListDetailActivity::class.java).apply {
            putExtra(ListDetailActivity.EXTRA_LIST_ID, listId)
        }

        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(detailIntent)
            getPendingIntent(
                listId.toInt(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_deadline)
            .setContentTitle(context.getString(R.string.notification_deadline_title, listTitle))
            .setContentText(context.getString(R.string.notification_deadline_body, formattedDeadline))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(listId.toInt(), notification)
    }
}
