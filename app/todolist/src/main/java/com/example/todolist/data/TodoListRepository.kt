package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

class TodoListRepository(private val dao: TodoListDao) {

    fun getLists(searchQuery: String = "", filterStatus: ListStatus? = null): Flow<List<TodoListWithItems>> {
        return if (filterStatus == null) {
            dao.getAllListsWithItems(searchQuery)
        } else {
            dao.getListsWithItemsByStatus(searchQuery, filterStatus)
        }
    }

    fun getListById(listId: Long): Flow<TodoListWithItems?> {
        return dao.getListWithItemsById(listId)
    }

    suspend fun insertList(
        title: String,
        status: ListStatus = ListStatus.TODO,
        deadline: Long = System.currentTimeMillis() + 24 * 60 * 60 * 1000L,
        notificationOffsetMinutes: Int = 60
    ): Long {
        val list = TodoListEntity(
            title = title,
            status = status,
            deadline = deadline,
            notificationOffsetMinutes = notificationOffsetMinutes
        )
        return dao.insertList(list)
    }

    suspend fun updateListDetails(list: TodoListEntity, newTitle: String, newStatus: ListStatus, newDeadline: Long) {
        val updatedList = list.copy(title = newTitle, deadline = newDeadline)
        dao.updateList(updatedList)
        updateListStatus(updatedList, newStatus)
    }

    suspend fun updateListDeadline(list: TodoListEntity, newDeadline: Long) {
        val updatedList = list.copy(deadline = newDeadline)
        dao.updateList(updatedList)
    }

    suspend fun updateNotificationOffset(list: TodoListEntity, offsetMinutes: Int) {
        val updated = list.copy(notificationOffsetMinutes = offsetMinutes)
        dao.updateList(updated)
    }

    suspend fun updateListStatus(list: TodoListEntity, newStatus: ListStatus) {
        if (list.status == newStatus) return

        when (newStatus) {
            ListStatus.DONE -> {
                dao.updateListStatusWithItems(list.id, ListStatus.DONE, true, true)
            }
            ListStatus.TODO -> {
                dao.updateListStatusWithItems(list.id, ListStatus.TODO, true, false)
            }
            ListStatus.IN_PROGRESS -> {
                dao.updateListStatusWithItems(list.id, ListStatus.IN_PROGRESS, false, false)
            }
        }
    }

    suspend fun updateListTitle(list: TodoListEntity, newTitle: String) {
        val updatedList = list.copy(title = newTitle)
        dao.updateList(updatedList)
    }

    suspend fun deleteList(list: TodoListEntity) {
        dao.deleteList(list)
    }

    suspend fun insertItem(listId: Long, text: String): Long {
        val item = TodoItemEntity(listId = listId, text = text)
        return dao.insertItem(item)
    }

    suspend fun updateItemDone(item: TodoItemEntity, isDone: Boolean) {
        val updatedItem = item.copy(isDone = isDone)
        dao.updateItem(updatedItem)

        // Automatically synchronize parent list status based on items completion
        val listWithItems = dao.getListWithItemsByIdSync(item.listId) ?: return
        val list = listWithItems.list
        val items = listWithItems.items

        if (items.isNotEmpty()) {
            val allDone = items.all { it.isDone }

            if (allDone && list.status != ListStatus.DONE) {
                dao.updateListStatusField(list.id, ListStatus.DONE)
            } else if (!allDone && list.status == ListStatus.DONE) {
                dao.updateListStatusField(list.id, ListStatus.IN_PROGRESS)
            }
        }
    }

    suspend fun deleteItem(item: TodoItemEntity) {
        dao.deleteItem(item)
    }
}
