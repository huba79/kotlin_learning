package com.example.todolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var todos:ArrayList<String>
    private lateinit var adapter: TodoAdapterWithBinding
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoInput: TextView
    private val fileHelper = FileHelper(this@MainActivity)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        todos = fileHelper.getData()


        adapter = TodoAdapterWithBinding(todos, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        todoInput=binding.itemInputId

        binding.addItemButton.setOnClickListener{
            if (!todoInput.text.toString().equals("")) {
                todos.add(todoInput.text.toString())
                todoInput.text = ""
                Log.d("button","Add button pressed")
                Log.d("todos:","Todos:...$todos")
                adapter.notifyDataSetChanged()
            }
        }
        binding.root.setOnClickListener {
            Toast.makeText(this@MainActivity,resources.getText(R.string.infoMessage),Toast.LENGTH_SHORT ).show()
        }

        Log.d("onBind","Listeners set")



    }

    override fun onPause() {
        fileHelper.saveData(todos)
        super.onPause()
    }
}