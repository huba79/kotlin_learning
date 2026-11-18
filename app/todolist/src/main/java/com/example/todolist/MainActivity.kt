package com.example.todolist

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.ListStatus
import com.example.todolist.data.TodoListEntity
import com.example.todolist.data.TodoListRepository
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.DialogAddEditListBinding
import com.example.todolist.notifications.NotificationHelper
import com.example.todolist.notifications.WorkManagerDeadlineScheduler
import com.example.todolist.ui.ListDetailActivity
import com.example.todolist.ui.SettingsActivity
import com.example.todolist.ui.TodoListAdapter
import com.example.todolist.utils.PreferencesManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: TodoListRepository
    private lateinit var adapter: TodoListAdapter
    private lateinit var deadlineScheduler: WorkManagerDeadlineScheduler

    private var currentSearchQuery: String = ""
    private var currentFilterStatus: ListStatus? = null
    private var appliedColorThemeKey: String = ""

    private var isDataLoaded: Boolean = false
    private var hasSyncedInitialReminders: Boolean = false
    private var loadDataJob: Job? = null
    private val appStartTime: Long = System.currentTimeMillis()

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            val elapsedTime = System.currentTimeMillis() - appStartTime
            (elapsedTime < 1000L) || !isDataLoaded
        }

        PreferencesManager.applySavedPreferencesOnAppLaunch(this)
        PreferencesManager.applyColorTheme(this)
        appliedColorThemeKey = PreferencesManager.getColorThemeKey(this)

        super.onCreate(savedInstanceState)
        PreferencesManager.applyStatusBarColor(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

        NotificationHelper.ensureNotificationChannel(this)
        requestNotificationPermissionIfNeeded()

        val dao = AppDatabase.getDatabase(this).todoListDao()
        deadlineScheduler = WorkManagerDeadlineScheduler(this)
        repository = TodoListRepository(dao, deadlineScheduler)

        setupRecyclerView()
        setupSearchAndFilter()
        setupFab()
        loadData()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) &&
            !NotificationHelper.hasNotificationPermission(this)
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onResume() {
        super.onResume()
        val latestColorThemeKey = PreferencesManager.getColorThemeKey(this)
        if (latestColorThemeKey != appliedColorThemeKey) {
            recreate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notification_settings -> {
                showGlobalNotificationSettingsDialog()
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        adapter = TodoListAdapter(
            context = this,
            onListClick = { listId ->
                val intent = Intent(this, ListDetailActivity::class.java).apply {
                    putExtra(ListDetailActivity.EXTRA_LIST_ID, listId)
                }
                startActivity(intent)
            },
            onEditClick = { list ->
                showAddEditListDialog(listToEdit = list)
            },
            onDeadlineClick = { list ->
                showDatePickerDialogForList(list)
            },
            onNotificationSettingsClick = { list ->
                showPerListNotificationSettingsDialog(list)
            },
            onDeleteClick = { list ->
                showDeleteConfirmationDialog(list)
            },
        )

        binding.recyclerViewLists.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLists.adapter = adapter
    }

    private fun showDatePickerDialogForList(list: TodoListEntity) {
        val calendar = java.util.Calendar.getInstance().apply { timeInMillis = list.deadline }

        android.app.DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                android.app.TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar[java.util.Calendar.YEAR] = year
                        calendar[java.util.Calendar.MONTH] = month
                        calendar[java.util.Calendar.DAY_OF_MONTH] = dayOfMonth
                        calendar[java.util.Calendar.HOUR_OF_DAY] = hourOfDay
                        calendar[java.util.Calendar.MINUTE] = minute
                        val newDeadline = calendar.timeInMillis
                        lifecycleScope.launch {
                            repository.updateListDeadline(list, newDeadline)
                        }
                    },
                    calendar[java.util.Calendar.HOUR_OF_DAY],
                    calendar[java.util.Calendar.MINUTE],
                    true,
                ).show()
            },
            calendar[java.util.Calendar.YEAR],
            calendar[java.util.Calendar.MONTH],
            calendar[java.util.Calendar.DAY_OF_MONTH],
        ).show()
    }

    private fun setupSearchAndFilter() {
        binding.editSearchQuery.addTextChangedListener(
            object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newQuery = s?.toString()?.trim() ?: ""
                if (newQuery != currentSearchQuery) {
                    currentSearchQuery = newQuery
                    loadData()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        },
        )

        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            val newStatus = when {
                checkedIds.contains(R.id.chipFilterTodo) -> ListStatus.TODO
                checkedIds.contains(R.id.chipFilterInProgress) -> ListStatus.IN_PROGRESS
                checkedIds.contains(R.id.chipFilterDone) -> ListStatus.DONE
                else -> null
            }
            if (newStatus != currentFilterStatus) {
                currentFilterStatus = newStatus
                loadData()
            }
        }
    }

    private fun setupFab() {
        binding.fabAddList.setOnClickListener {
            showAddEditListDialog(listToEdit = null)
        }
    }

    private fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = lifecycleScope.launch {
            repository.getLists(currentSearchQuery, currentFilterStatus).collectLatest { lists ->
                adapter.submitList(lists)
                if (lists.isEmpty()) {
                    binding.textEmptyState.visibility = View.VISIBLE
                    binding.recyclerViewLists.visibility = View.GONE
                } else {
                    binding.textEmptyState.visibility = View.GONE
                    binding.recyclerViewLists.visibility = View.VISIBLE
                }
                if ((!hasSyncedInitialReminders) && currentSearchQuery.isEmpty() && (currentFilterStatus == null)) {
                    hasSyncedInitialReminders = true
                    lists.forEach { (list, _) -> deadlineScheduler.syncIfNeeded(list) }
                }
                isDataLoaded = true
            }
        }
    }

    private fun showAddEditListDialog(listToEdit: TodoListEntity?) {
        val dialogBinding = DialogAddEditListBinding.inflate(LayoutInflater.from(this))
        var selectedDeadline: Long = listToEdit?.deadline ?: (System.currentTimeMillis() + (24 * 60 * 60 * 1000L))
        val globalOffset = PreferencesManager.getNotificationOffsetMinutes(this)
        val selectedOffset: Int = listToEdit?.notificationOffsetMinutes ?: globalOffset

        val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())

        fun updateDeadlineButtonText() {
            dialogBinding.btnSelectDeadline.text = getString(R.string.due_date_format, dateFormat.format(java.util.Date(selectedDeadline)))
        }

        updateDeadlineButtonText()

        dialogBinding.btnSelectDeadline.setOnClickListener {
            val calendar = java.util.Calendar.getInstance().apply { timeInMillis = selectedDeadline }
            android.app.DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    android.app.TimePickerDialog(
                        this,
                        { _, hourOfDay, minute ->
                            calendar[java.util.Calendar.YEAR] = year
                            calendar[java.util.Calendar.MONTH] = month
                            calendar[java.util.Calendar.DAY_OF_MONTH] = dayOfMonth
                            calendar[java.util.Calendar.HOUR_OF_DAY] = hourOfDay
                            calendar[java.util.Calendar.MINUTE] = minute
                            selectedDeadline = calendar.timeInMillis
                            updateDeadlineButtonText()
                        },
                        calendar[java.util.Calendar.HOUR_OF_DAY],
                        calendar[java.util.Calendar.MINUTE],
                        true,
                    ).show()
                },
                calendar[java.util.Calendar.YEAR],
                calendar[java.util.Calendar.MONTH],
                calendar[java.util.Calendar.DAY_OF_MONTH],
            ).show()
        }

        if (listToEdit != null) {
            dialogBinding.editListTitle.setText(listToEdit.title)
            when (listToEdit.status) {
                ListStatus.TODO -> dialogBinding.chipGroupDialogStatus.check(R.id.chipDialogTodo)
                ListStatus.IN_PROGRESS -> dialogBinding.chipGroupDialogStatus.check(R.id.chipDialogInProgress)
                ListStatus.DONE -> dialogBinding.chipGroupDialogStatus.check(R.id.chipDialogDone)
            }
        } else {
            dialogBinding.chipGroupDialogStatus.check(R.id.chipDialogTodo)
        }

        val titleRes = if (listToEdit == null) R.string.title_create_list else R.string.title_edit_list

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(titleRes)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.save, null)
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.setOnShowListener {
            val saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            saveButton.setOnClickListener {
                val inputTitle = dialogBinding.editListTitle.text.toString().trim()
                if (inputTitle.isEmpty()) {
                    dialogBinding.inputLayoutTitle.error = getString(R.string.hint_list_title)
                    return@setOnClickListener
                }

                val selectedStatus = when (dialogBinding.chipGroupDialogStatus.checkedChipId) {
                    R.id.chipDialogInProgress -> ListStatus.IN_PROGRESS
                    R.id.chipDialogDone -> ListStatus.DONE
                    else -> ListStatus.TODO
                }

                lifecycleScope.launch {
                    if (listToEdit == null) {
                        repository.insertList(inputTitle, selectedStatus, selectedDeadline, selectedOffset)
                    } else {
                        repository.updateListDetails(listToEdit, inputTitle, selectedStatus, selectedDeadline)
                    }
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun showGlobalNotificationSettingsDialog() {
        val currentOffset = PreferencesManager.getNotificationOffsetMinutes(this)
        val items = arrayOf(
            getString(R.string.offset_15m),
            getString(R.string.offset_30m),
            getString(R.string.offset_1h),
            getString(R.string.offset_2h),
            getString(R.string.offset_6h),
            getString(R.string.offset_24h)
        )
        val minutesValues = intArrayOf(15, 30, 60, 120, 360, 1440)
        var selectedIndex = minutesValues.indexOf(currentOffset).takeIf { it >= 0 } ?: 2

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.title_notification_settings)
            .setSingleChoiceItems(items, selectedIndex) { _, which ->
                selectedIndex = which
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                PreferencesManager.saveNotificationOffsetMinutes(this, minutesValues[selectedIndex])
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showPerListNotificationSettingsDialog(list: TodoListEntity) {
        val currentOffset = list.notificationOffsetMinutes
        val items = arrayOf(
            getString(R.string.offset_15m),
            getString(R.string.offset_30m),
            getString(R.string.offset_1h),
            getString(R.string.offset_2h),
            getString(R.string.offset_6h),
            getString(R.string.offset_24h)
        )
        val minutesValues = intArrayOf(15, 30, 60, 120, 360, 1440)
        var selectedIndex = minutesValues.indexOf(currentOffset).takeIf { it >= 0 } ?: 2

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.title_notification_settings)
            .setSingleChoiceItems(items, selectedIndex) { _, which ->
                selectedIndex = which
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                lifecycleScope.launch {
                    repository.updateNotificationOffset(list, minutesValues[selectedIndex])
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showDeleteConfirmationDialog(list: TodoListEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.confirm_delete_title)
            .setMessage(R.string.confirm_delete_question)
            .setPositiveButton(R.string.caption_yes) { _, _ ->
                lifecycleScope.launch {
                    repository.deleteList(list)
                }
            }
            .setNegativeButton(R.string.caption_nope, null)
            .show()
    }
}
