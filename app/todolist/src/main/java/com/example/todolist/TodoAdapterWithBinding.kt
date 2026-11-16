package com.example.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TodoAdapterWithBinding(pTodos:ArrayList<String>, pContext :Context):RecyclerView.Adapter<TodoAdapterWithBinding.TodoViewHolder>(){
    private var context = pContext
    private var todos = pTodos

    class TodoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var textView: TextView = itemView.findViewById<TextView>(R.id.todoItemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)

        Log.d("onCreate","ItemView created")
        return TodoViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(itemViewHolder: TodoViewHolder, itemPosition: Int) {
        itemViewHolder.textView.text = todos[itemPosition]
        Log.d("onBind","itemView bound, ${todos[itemPosition]} added")

        itemViewHolder.itemView.setOnLongClickListener {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle(R.string.confirm_delete_title)
            builder.setMessage(R.string.confirm_delete_question)

            builder.setPositiveButton(R.string.caption_yes) { _, _ ->
                Log.d("onBind","OK selected")
                todos.remove(todos[itemPosition])
                this.notifyDataSetChanged()
            }

            builder.setNegativeButton(R.string.caption_nope) { _, _ ->
                Log.d("onBind","Nope selected")
            }
            builder.show()
            true
        }

    }

    override fun getItemCount(): Int {
        return todos.size
    }

}