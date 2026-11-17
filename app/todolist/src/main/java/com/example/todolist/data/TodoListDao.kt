package com.example.todolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {
    @Transaction
    @Query("SELECT * FROM todo_lists WHERE title LIKE '%' || :searchQuery || '%' ORDER BY id DESC")
    fun getAllListsWithItems(searchQuery: String = ""): Flow<List<TodoListWithItems>>

    @Transaction
    @Query("SELECT * FROM todo_lists WHERE title LIKE '%' || :searchQuery || '%' AND status = :status ORDER BY id DESC")
    fun getListsWithItemsByStatus(searchQuery: String = "", status: ListStatus): Flow<List<TodoListWithItems>>

    @Transaction
    @Query("SELECT * FROM todo_lists WHERE id = :listId")
    fun getListWithItemsById(listId: Long): Flow<TodoListWithItems?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: TodoListEntity): Long

    @Update
    suspend fun updateList(list: TodoListEntity)

    @Delete
    suspend fun deleteList(list: TodoListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: TodoItemEntity): Long

    @Update
    suspend fun updateItem(item: TodoItemEntity)

    @Delete
    suspend fun deleteItem(item: TodoItemEntity)

    @Query("UPDATE todo_items SET isDone = :isDone WHERE listId = :listId")
    suspend fun updateAllItemsStatusForList(listId: Long, isDone: Boolean)

    @Query("UPDATE todo_lists SET status = :status WHERE id = :listId")
    suspend fun updateListStatusField(listId: Long, status: ListStatus)

    @Query("UPDATE todo_lists SET notificationOffsetMinutes = :offset WHERE id = :listId")
    suspend fun updateNotificationOffset(listId: Long, offset: Int)

    @Transaction
    @Query("SELECT * FROM todo_lists WHERE id = :listId")
    suspend fun getListWithItemsByIdSync(listId: Long): TodoListWithItems?

    @Transaction
    suspend fun updateListStatusWithItems(listId: Long, newStatus: ListStatus, updateItems: Boolean, isDone: Boolean) {
        updateListStatusField(listId, newStatus)
        if (updateItems) {
            updateAllItemsStatusForList(listId, isDone)
        }
    }
}
