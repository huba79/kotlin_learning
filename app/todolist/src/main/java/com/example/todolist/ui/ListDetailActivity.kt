package com.example.todolist.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.ListStatus
import com.example.todolist.data.TodoItemEntity
import com.example.todolist.data.TodoListEntity
import com.example.todolist.data.TodoListRepository
import com.example.todolist.databinding.ActivityListDetailBinding
import com.example.todolist.utils.PreferencesManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LIST_ID = "extra_list_id"
    }

    private lateinit var binding: ActivityListDetailBinding
    private lateinit var repository: TodoListRepository
    private lateinit var itemAdapter: CheckableItemAdapter

    private var listId: Long = -1L
    private var currentListEntity: TodoListEntity? = null
    private var isSettingStatusFromCode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        PreferencesManager.applyColorTheme(this)
        super.onCreate(savedInstanceState)
        PreferencesManager.applyStatusBarColor(this)
        binding = ActivityListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listId = intent.getLongExtra(EXTRA_LIST_ID, -1L)
        if (listId == -1L) {
            finish()
            return
        }

        val dao = AppDatabase.getDatabase(this).todoListDao()
        repository = TodoListRepository(dao)

        setupToolbar()
        setupRecyclerView()
        setupAddItem()
        setupStatusChips()
        setupDeadlineEdit()
        observeData()
    }

    private fun setupToolbar() {
        binding.detailAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        itemAdapter = CheckableItemAdapter(
            onItemCheckChanged = { item, isDone ->
                lifecycleScope.launch {
                    repository.updateItemDone(item, isDone)
                }
            },
            onItemDeleteClick = { item ->
                showDeleteItemDialog(item)
            }
        )

        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewItems.adapter = itemAdapter
    }

    private fun setupAddItem() {
        binding.btnAddItem.setOnClickListener {
            addNewItem()
        }
    }

    private fun addNewItem() {
        val text = binding.editItemText.text?.toString()?.trim() ?: ""
        if (text.isNotEmpty()) {
            lifecycleScope.launch {
                repository.insertItem(listId, text)
                binding.editItemText.setText("")
            }
        }
    }

    private fun setupStatusChips() {
        binding.chipGroupStatus.setOnCheckedStateChangeListener { _, checkedIds ->
            if (isSettingStatusFromCode) return@setOnCheckedStateChangeListener

            val list = currentListEntity ?: return@setOnCheckedStateChangeListener
            val newStatus = when {
                checkedIds.contains(R.id.chipStatusTodo) -> ListStatus.TODO
                checkedIds.contains(R.id.chipStatusInProgress) -> ListStatus.IN_PROGRESS
                checkedIds.contains(R.id.chipStatusDone) -> ListStatus.DONE
                else -> return@setOnCheckedStateChangeListener
            }

            lifecycleScope.launch {
                repository.updateListStatus(list, newStatus)
            }
        }
    }

    private fun setupDeadlineEdit() {
        val clickListener = View.OnClickListener {
            showDatePickerDialog()
        }
        binding.btnEditDeadline.setOnClickListener(clickListener)
        binding.textDetailDeadline.setOnClickListener(clickListener)
    }

    private fun showDatePickerDialog() {
        val list = currentListEntity ?: return
        val calendar = java.util.Calendar.getInstance().apply { timeInMillis = list.deadline }

        android.app.DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                android.app.TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        val newDeadline = calendar.timeInMillis
                        lifecycleScope.launch {
                            repository.updateListDeadline(list, newDeadline)
                        }
                    },
                    calendar.get(java.util.Calendar.HOUR_OF_DAY),
                    calendar.get(java.util.Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun observeData() {
        lifecycleScope.launch {
            repository.getListById(listId).collectLatest { listWithItems ->
                if (listWithItems == null) {
                    finish()
                    return@collectLatest
                }

                currentListEntity = listWithItems.list
                binding.detailAppBar.title = listWithItems.list.title

                // Update status chip selection
                isSettingStatusFromCode = true
                when (listWithItems.list.status) {
                    ListStatus.TODO -> binding.chipGroupStatus.check(R.id.chipStatusTodo)
                    ListStatus.IN_PROGRESS -> binding.chipGroupStatus.check(R.id.chipStatusInProgress)
                    ListStatus.DONE -> binding.chipGroupStatus.check(R.id.chipStatusDone)
                }
                isSettingStatusFromCode = false

                // Update deadline display
                val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
                binding.textDetailDeadline.text = getString(R.string.due_date_format, dateFormat.format(java.util.Date(listWithItems.list.deadline)))

                // Update items list
                itemAdapter.submitList(listWithItems.items)

                if (listWithItems.items.isEmpty()) {
                    binding.textEmptyItems.visibility = View.VISIBLE
                    binding.recyclerViewItems.visibility = View.GONE
                } else {
                    binding.textEmptyItems.visibility = View.GONE
                    binding.recyclerViewItems.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showDeleteItemDialog(item: TodoItemEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.confirm_delete_title)
            .setMessage(R.string.confirm_delete_item_question)
            .setPositiveButton(R.string.caption_yes) { _, _ ->
                lifecycleScope.launch {
                    repository.deleteItem(item)
                }
            }
            .setNegativeButton(R.string.caption_nope, null)
            .show()
    }
}
