package com.example.todolist.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.ListStatus
import com.example.todolist.data.TodoListEntity
import com.example.todolist.data.TodoListWithItems
import com.example.todolist.databinding.ItemTodoListCardBinding

class TodoListAdapter(
    private val context: Context,
    private val onListClick: (Long) -> Unit,
    private val onEditClick: (TodoListEntity) -> Unit,
    private val onNotificationSettingsClick: (TodoListEntity) -> Unit,
    private val onDeleteClick: (TodoListEntity) -> Unit
) : RecyclerView.Adapter<TodoListAdapter.ListViewHolder>() {

    private var lists: List<TodoListWithItems> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<TodoListWithItems>) {
        lists = newList
        notifyDataSetChanged()
    }

    inner class ListViewHolder(val binding: ItemTodoListCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemTodoListCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val itemWithItems = lists[position]
        val list = itemWithItems.list
        val items = itemWithItems.items
        val binding = holder.binding

        binding.textListTitle.text = list.title

        // Status styling
        when (list.status) {
            ListStatus.TODO -> {
                binding.chipStatus.text = context.getString(R.string.filter_todo)
                binding.chipStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.status_todo_bg)
                )
                binding.chipStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.status_todo)
                )
            }
            ListStatus.IN_PROGRESS -> {
                binding.chipStatus.text = context.getString(R.string.filter_in_progress)
                binding.chipStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.status_in_progress_bg)
                )
                binding.chipStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.status_in_progress)
                )
            }
            ListStatus.DONE -> {
                binding.chipStatus.text = context.getString(R.string.filter_done)
                binding.chipStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.status_done_bg)
                )
                binding.chipStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.status_done)
                )
            }
        }

        // Progress text & indicator
        val totalCount = items.size
        val doneCount = items.count { it.isDone }
        binding.textProgress.text = if (totalCount == 0) {
            "0 items"
        } else {
            "$doneCount of $totalCount items completed"
        }

        val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
        binding.textDeadline.text = context.getString(R.string.due_date_format, dateFormat.format(java.util.Date(list.deadline)))

        val progressPercent = if (totalCount == 0) 0 else (doneCount * 100) / totalCount
        binding.progressIndicator.progress = progressPercent

        // Click listeners
        binding.root.setOnClickListener {
            onListClick(list.id)
        }

        binding.btnMenu.setOnClickListener { view ->
            val popup = PopupMenu(context, view)
            popup.menu.add(R.string.action_edit)
            popup.menu.add(R.string.action_notification_settings)
            popup.menu.add(R.string.action_delete)
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title) {
                    context.getString(R.string.action_edit) -> {
                        onEditClick(list)
                        true
                    }
                    context.getString(R.string.action_notification_settings) -> {
                        onNotificationSettingsClick(list)
                        true
                    }
                    context.getString(R.string.action_delete) -> {
                        onDeleteClick(list)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = lists.size
}
