package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class recycler_activity : AppCompatActivity() {
    private lateinit var usersRview:RecyclerView
    private lateinit var recyclerViewAdapter:UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        usersRview = findViewById(R.id.rView)
        usersRview.layoutManager= LinearLayoutManager(this@recycler_activity)
        recyclerViewAdapter = UsersAdapter(usersInit(),this@recycler_activity,findViewById(R.id.constraintLayoutId))
        usersRview.adapter = recyclerViewAdapter
    }

    private fun usersInit():ArrayList<User> {
        val users: ArrayList<User> = ArrayList()
        users.add(User(R.drawable.user1,"User1","user1@mail.com"))
        users.add(User(R.drawable.user2,"User2","user2@mail.com"))
        users.add(User(R.drawable.user3,"User3","user3@mail.com"))
        users.add(User(R.drawable.user4,"User4","user4@mail.com"))
        users.add(User(R.drawable.user5,"User5","user5@mail.com"))
        users.add(User(R.drawable.user6,"User6","user6@mail.com"))
        users.add(User(R.drawable.user7,"User7","user7@mail.com"))
        users.add(User(R.drawable.user8,"User8","user8@mail.com"))
        users.add(User(R.drawable.user9,"User9","user9@mail.com"))
        return users
    }
}