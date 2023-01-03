package com.example.todolist

import android.content.Context
import android.util.Log
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

@Suppress("PrivatePropertyName")
class FileHelper(pContext:Context) {
    private val TODOS_FILENAME  = "todos.txt"
    private val context = pContext

    @Suppress("UNCHECKED_CAST")
    fun getData():ArrayList<String> {
        lateinit var todos:ArrayList<String>
        Log.d("Fileops","Source file is: ${context.filesDir}/${TODOS_FILENAME}")
        Log.d("Fileops","trying to read data....")
        try{
            val fis = context.openFileInput(TODOS_FILENAME)
            val ois = ObjectInputStream(fis)
                todos = ois.readObject() as ArrayList<String>
            ois.close()
            fis.close()
            Log.d("Fileops","Data read successful....")
        }
        catch (e:Exception){
            e.printStackTrace()
            Log.d("Fileops","Exception occured ${e.message}")
            Log.d("Fileops","Init empty data....")
            todos = ArrayList()
        }

        return todos
    }

    fun saveData(todos:ArrayList<String>){
        try{
            Log.d("Fileops","Trying to save data....")
            Log.d("Fileops","Target file is ${context.filesDir}/${TODOS_FILENAME}")

            val os = context.openFileOutput(TODOS_FILENAME, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(os)
            oos.writeObject(todos)
            oos.close()
            os.close()
            Log.d("Fileops","Data write successful....")
        }
        catch(e: Exception){
            e.printStackTrace()
            Log.d("Fileops","Failed to to save data....")
        }
    }
}