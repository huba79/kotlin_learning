package com.example.todolist

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.ListStatus
import com.example.todolist.data.TodoListEntity
import com.example.todolist.data.TodoItemEntity
import com.example.todolist.data.TodoListRepository
import com.example.todolist.notifications.DeadlineReminderScheduler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoListRepositoryTest {

    private class FakeReminderScheduler : DeadlineReminderScheduler {
        val scheduledLists = mutableListOf<TodoListEntity>()
        val cancelledIds = mutableListOf<Long>()

        override fun schedule(list: TodoListEntity) {
            scheduledLists.add(list)
        }

        override fun cancel(listId: Long) {
            cancelledIds.add(listId)
        }
    }

    private lateinit var db: AppDatabase
    private lateinit var repository: TodoListRepository
    private lateinit var fakeScheduler: FakeReminderScheduler

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        fakeScheduler = FakeReminderScheduler()
        repository = TodoListRepository(db.todoListDao(), fakeScheduler)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun switchingStatusToDoneChecksAllItems() = runBlocking {
        val listId = repository.insertList("Shopping List", ListStatus.TODO)
        repository.insertItem(listId, "Apples")
        repository.insertItem(listId, "Milk")

        val listEntity = TodoListEntity(id = listId, title = "Shopping List", status = ListStatus.TODO)
        repository.updateListStatus(listEntity, ListStatus.DONE)

        val result = repository.getListById(listId).first()
        assertNotNull(result)
        assertEquals(ListStatus.DONE, result!!.list.status)
        assertTrue(result.items.all { it.isDone })
        assertTrue(fakeScheduler.cancelledIds.contains(listId))
    }

    @Test
    fun switchingStatusBackToTodoUnchecksAllItems() = runBlocking {
        val listId = repository.insertList("Work Tasks", ListStatus.DONE)
        val itemId1 = repository.insertItem(listId, "Task 1")
        repository.updateItemDone(TodoItemEntity(id = itemId1, listId = listId, text = "Task 1", isDone = true), true)

        val listEntity = TodoListEntity(id = listId, title = "Work Tasks", status = ListStatus.DONE)
        repository.updateListStatus(listEntity, ListStatus.TODO)

        val result = repository.getListById(listId).first()
        assertNotNull(result)
        assertEquals(ListStatus.TODO, result!!.list.status)
        assertTrue(result.items.none { it.isDone })
        assertTrue(fakeScheduler.scheduledLists.any { it.id == listId && it.status == ListStatus.TODO })
    }

    @Test
    fun deletingOrUpdatingDeadlineUpdatesReminderScheduler() = runBlocking {
        val listId = repository.insertList("Errands", ListStatus.TODO)
        assertEquals(1, fakeScheduler.scheduledLists.size)

        val inserted = repository.getListById(listId).first()!!.list
        val newDeadline = inserted.deadline + 3_600_000L
        repository.updateListDeadline(inserted, newDeadline)
        assertTrue(fakeScheduler.scheduledLists.any { it.id == listId && it.deadline == newDeadline })

        repository.deleteList(inserted.copy(deadline = newDeadline))
        assertTrue(fakeScheduler.cancelledIds.contains(listId))
    }
}
