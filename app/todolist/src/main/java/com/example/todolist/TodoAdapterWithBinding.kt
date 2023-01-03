package com.example.todolist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ListItemBinding;

class TodoAdapterWithBinding(pTodos:ArrayList<String>, pContext :Context):RecyclerView.Adapter<TodoAdapterWithBinding.TodoViewHolder>(){
    private var context = pContext
    private var todos = pTodos

    class TodoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var textView: TextView = itemView.findViewById<TextView>(R.id.todoItemView) //////??????????????
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
//        val itemView = ListItemBinding.inflate(LayoutInflater.from(context))

        Log.d("onCreate","ItemView created")
        return TodoViewHolder(itemView)
    }

    override fun onBindViewHolder(itemViewHolder: TodoViewHolder, itemPosition: Int) {
        itemViewHolder.textView.text = todos[itemPosition]
        Log.d("onBind","itemView bound, ${todos[itemPosition]} added")

        itemViewHolder.itemView.setOnLongClickListener() {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.confirm_delete_title)
            builder.setMessage(R.string.confirm_delete_question)

            builder.setPositiveButton(R.string.caption_yes) { dialog, which ->
                Log.d("onBind","OK selected")
                todos.remove(todos[itemPosition])
                //TODO update file content
                this.notifyDataSetChanged()
            }

            builder.setNegativeButton(R.string.caption_nope) { dialog, which ->
                Log.d("onBind","Nope selected")
            }
            builder.show()
            true //because we have consumed the event - won't throw further along the event queue
        }

    }

    override fun getItemCount(): Int {
        return todos.size
    }

}