package com.example.gridview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView

class gridActivity : AppCompatActivity() {
    lateinit var gridView: GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        gridView = findViewById(R.id.gridViewId)
        gridView.adapter = GridAdapter(this@gridActivity, initImages())


    }

    fun initImages(): ArrayList<Image>{
        val list:ArrayList<Image> = ArrayList()
        list.add( Image(R.drawable.cat,"Macs") )
        list.add( Image(R.drawable.disznyo,"Malac") )
        list.add( Image(R.drawable.octopus,"Polip1") )
        list.add( Image(R.drawable.horse,"Lo") )
        list.add( Image(R.drawable.octopus3,"Polip3") )
        list.add( Image(R.drawable.dog,"Kutya") )
        list.add( Image(R.drawable.octopus2,"Polip2") )
        list.add( Image(R.drawable.giraffe,"Zsiraf") )
        return list
    }
}