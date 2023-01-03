package com.example.gridview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class GridAdapter(pContext: Context, pImageList:ArrayList<Image>): BaseAdapter() {
    var imageList = pImageList
    var context = pContext

    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(p0: Int): Any? {
        return null //TODO
    }

    override fun getItemId(p0: Int): Long {
        return 0L //TODO
    }

    override fun getView(position: Int, itemView: View?, parentView: ViewGroup?): View {
        val view:View = LayoutInflater.from(parentView!!.context).inflate(R.layout.grid_item_layout,parentView,false)

        var imgView: ImageView = view.findViewById(R.id.imageViewId)
        var titleview: TextView= view.findViewById(R.id.titleViewId)

        imgView.setImageResource(imageList[position].imgId)
        titleview.text = imageList[position].title

        return view
    }
}