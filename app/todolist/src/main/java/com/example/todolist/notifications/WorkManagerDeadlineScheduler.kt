package com.example.todolist.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todolist.data.ListStatus
import com.example.todolist.data.TodoListEntity
import java.util.concurrent.TimeUnit

class WorkManagerDeadlineScheduler(context: Context) : DeadlineReminderScheduler {

    private val appContext = context.applicationContext

    companion object {
        fun uniqueWorkName(listId: Long): String = "deadline_reminder_$listId"

        fun computeReminderDelayMillis(
            deadlineMillis: Long,
            offsetMinutes: Int,
            nowMillis: Long,
            status: ListStatus,
        ): Long? {
            if (status == ListStatus.DONE) return null
            if (deadlineMillis <= nowMillis) return null

            val triggerTime = deadlineMillis - (offsetMinutes.coerceAtLeast(0) * 60_000L)
            return (triggerTime - nowMillis).coerceAtLeast(0L)
        }
    }

    override fun schedule(list: TodoListEntity) {
        enqueueOrCancel(list, ExistingWorkPolicy.REPLACE)
    }

    fun syncIfNeeded(list: TodoListEntity) {
        enqueueOrCancel(list, ExistingWorkPolicy.KEEP)
    }

    override fun cancel(listId: Long) {
        WorkManager.getInstance(appContext).cancelUniqueWork(uniqueWorkName(listId))
    }

    private fun enqueueOrCancel(list: TodoListEntity, policy: ExistingWorkPolicy) {
        if (list.id <= 0L) return

        val delayMillis = computeReminderDelayMillis(
            deadlineMillis = list.deadline,
            offsetMinutes = list.notificationOffsetMinutes,
            nowMillis = System.currentTimeMillis(),
            status = list.status
        )

        if (delayMillis == null) {
            cancel(list.id)
            return
        }

        val request = OneTimeWorkRequestBuilder<DeadlineNotificationWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(DeadlineNotificationWorker.KEY_LIST_ID to list.id))
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            uniqueWorkName(list.id),
            policy,
            request
        )
    }
}
