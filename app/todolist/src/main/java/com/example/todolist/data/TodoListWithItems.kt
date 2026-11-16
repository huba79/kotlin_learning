package com.example.todolist.data

import androidx.room.Embedded
import androidx.room.Relation

data class TodoListWithItems(
    @Embedded
    val list: TodoListEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val items: List<TodoItemEntity> = emptyList()
)
