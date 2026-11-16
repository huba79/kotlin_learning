package com.example.todolist.ui

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.TodoItemEntity
import com.example.todolist.databinding.ItemTodoCheckItemBinding

class CheckableItemAdapter(
    private val onItemCheckChanged: (TodoItemEntity, Boolean) -> Unit,
    private val onItemDeleteClick: (TodoItemEntity) -> Unit
) : RecyclerView.Adapter<CheckableItemAdapter.ItemViewHolder>() {

    private var items: List<TodoItemEntity> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<TodoItemEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ItemViewHolder(val binding: ItemTodoCheckItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemTodoCheckItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        val binding = holder.binding

        binding.textItem.text = item.text

        // Avoid triggering listener during view recycled binding
        binding.checkBoxItem.setOnCheckedChangeListener(null)
        binding.checkBoxItem.isChecked = item.isDone

        // Strike-through effect for checked items
        if (item.isDone) {
            binding.textItem.paintFlags = binding.textItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            binding.textItem.paintFlags = binding.textItem.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        binding.checkBoxItem.setOnCheckedChangeListener { _, isChecked ->
            onItemCheckChanged(item, isChecked)
        }

        binding.btnDeleteItem.setOnClickListener {
            onItemDeleteClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
