package com.example.todolist

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.ListStatus
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {

    private val testDbName = "migration-test-db"
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(testDbName)
    }

    @After
    fun tearDown() {
        context.deleteDatabase(testDbName)
    }

    @Test
    fun migrate1To2_preservesExistingListsAndItems(): Unit = runBlocking {
        createV1DatabaseWithSampleData()

        val migratedDb = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            testDbName,
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()

        try {
            val migratedList = migratedDb.todoListDao().getListWithItemsByIdSync(1L)
            assertNotNull(migratedList)
            assertEquals("Groceries", migratedList!!.list.title)
            assertEquals(ListStatus.TODO, migratedList.list.status)
            assertEquals(60, migratedList.list.notificationOffsetMinutes)
            assertTrue(migratedList.list.deadline > System.currentTimeMillis())
            assertEquals(1, migratedList.items.size)
            assertEquals("Milk", migratedList.items[0].text)
            assertFalse(migratedList.items[0].isDone)
        } finally {
            migratedDb.close()
        }
    }

    private fun createV1DatabaseWithSampleData() {
        val config = SupportSQLiteOpenHelper.Configuration.builder(context)
            .name(testDbName)
            .callback(
                object : SupportSQLiteOpenHelper.Callback(1) {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        db.execSQL(
                            "CREATE TABLE IF NOT EXISTS `todo_lists` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `status` TEXT NOT NULL)",
                        )
                        db.execSQL(
                            "CREATE TABLE IF NOT EXISTS `todo_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `listId` INTEGER NOT NULL, `text` TEXT NOT NULL, `isDone` INTEGER NOT NULL, FOREIGN KEY(`listId`) REFERENCES `todo_lists`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )",
                        )
                        db.execSQL(
                            "CREATE INDEX IF NOT EXISTS `index_todo_items_listId` ON `todo_items` (`listId`)",
                        )
                        db.execSQL(
                            "INSERT INTO `todo_lists` (`id`, `title`, `status`) VALUES (1, 'Groceries', 'TODO')",
                        )
                        db.execSQL(
                            "INSERT INTO `todo_items` (`id`, `listId`, `text`, `isDone`) VALUES (1, 1, 'Milk', 0)",
                        )
                    }

                    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
                },
            )
            .build()

        val helper = FrameworkSQLiteOpenHelperFactory().create(config)
        helper.writableDatabase.close()
        helper.close()
    }
}
