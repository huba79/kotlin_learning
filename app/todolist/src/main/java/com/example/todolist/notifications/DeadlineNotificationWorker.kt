package com.example.todolist.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.ListStatus

class DeadlineNotificationWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    companion object {
        const val KEY_LIST_ID = "key_list_id"
    }

    override suspend fun doWork(): Result {
        val listId = inputData.getLong(KEY_LIST_ID, -1L)
        if (listId <= 0L) {
            return Result.failure()
        }

        val (list) = AppDatabase.getDatabase(applicationContext)
            .todoListDao()
            .getListWithItemsByIdSync(listId) ?: return Result.success()

        if (list.status == ListStatus.DONE) {
            return Result.success()
        }

        NotificationHelper.showDeadlineNotification(
            context = applicationContext,
            listId = list.id,
            listTitle = list.title,
            deadlineMillis = list.deadline
        )

        return Result.success()
    }
}
