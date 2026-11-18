package com.example.todolist.notifications

import com.example.todolist.data.TodoListEntity

interface DeadlineReminderScheduler {
    fun schedule(list: TodoListEntity)
    fun cancel(listId: Long)
}
