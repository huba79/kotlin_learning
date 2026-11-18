package com.example.todolist

import com.example.todolist.data.ListStatus
import com.example.todolist.notifications.WorkManagerDeadlineScheduler
import com.example.todolist.utils.PreferencesManager
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testPreferencesManagerThemeKeys() {
        assertEquals("default", PreferencesManager.COLOR_THEME_DEFAULT)
        assertEquals("ocean", PreferencesManager.COLOR_THEME_OCEAN)
        assertEquals("emerald", PreferencesManager.COLOR_THEME_EMERALD)
    }

    @Test
    fun testListStatusEnum() {
        val statuses = ListStatus.entries
        assertEquals(3, statuses.size)
        assertTrue(statuses.contains(ListStatus.TODO))
        assertTrue(statuses.contains(ListStatus.IN_PROGRESS))
        assertTrue(statuses.contains(ListStatus.DONE))
    }

    @Test
    fun testSplashScreenKeepCondition() {
        fun shouldKeepSplashScreen(elapsedTimeMs: Long, isDataLoaded: Boolean): Boolean {
            return elapsedTimeMs < 1000L || !isDataLoaded
        }

        // Under 1 second, even if data loaded -> keep splash screen
        assertTrue(shouldKeepSplashScreen(elapsedTimeMs = 300L, isDataLoaded = true))

        // Over 1 second, but data not yet loaded -> keep splash screen
        assertTrue(shouldKeepSplashScreen(elapsedTimeMs = 1500L, isDataLoaded = false))

        // Both conditions satisfied (>= 1s and data loaded) -> dismiss splash screen
        assertFalse(shouldKeepSplashScreen(elapsedTimeMs = 1000L, isDataLoaded = true))
        assertFalse(shouldKeepSplashScreen(elapsedTimeMs = 1200L, isDataLoaded = true))
    }

    @Test
    fun testComputeReminderDelay_futureTriggerReturnsRemainingDelay() {
        val now = 1_000_000L
        val deadline = now + 120 * 60_000L // +120 minutes
        val delay = WorkManagerDeadlineScheduler.computeReminderDelayMillis(
            deadlineMillis = deadline,
            offsetMinutes = 30,
            nowMillis = now,
            status = ListStatus.TODO
        )
        assertEquals(90 * 60_000L, delay)
    }

    @Test
    fun testComputeReminderDelay_pastThresholdFutureDeadlineClampsToZero() {
        val now = 1_000_000L
        val deadline = now + 20 * 60_000L // +20 minutes
        val delay = WorkManagerDeadlineScheduler.computeReminderDelayMillis(
            deadlineMillis = deadline,
            offsetMinutes = 60, // threshold was 40 minutes ago
            nowMillis = now,
            status = ListStatus.IN_PROGRESS
        )
        assertEquals(0L, delay)
    }

    @Test
    fun testComputeReminderDelay_doneOrExpiredReturnsNull() {
        val now = 1_000_000L
        assertNull(
            WorkManagerDeadlineScheduler.computeReminderDelayMillis(
                deadlineMillis = now + 60 * 60_000L,
                offsetMinutes = 15,
                nowMillis = now,
                status = ListStatus.DONE
            )
        )
        assertNull(
            WorkManagerDeadlineScheduler.computeReminderDelayMillis(
                deadlineMillis = now - 1_000L,
                offsetMinutes = 15,
                nowMillis = now,
                status = ListStatus.TODO
            )
        )
    }
}