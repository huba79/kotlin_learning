package com.example.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class UsersAdapter(pUsers:ArrayList<User>,pContext :Context, pParentLayout:ConstraintLayout ):RecyclerView.Adapter<UsersAdapter.UserViewHolder>(){
    private var context = pContext
    private var users = pUsers
    private val layout = pParentLayout

    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var imageView:ImageView = itemView.findViewById(R.id.imageViewId)
        var nameView:TextView = itemView.findViewById(R.id.nameViewId)
        var emailView:TextView = itemView.findViewById(R.id.emailViewId)

        var cardView: CardView = itemView.findViewById(R.id.itemCardId)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.r_item_layout,parent,false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(itemViewHolder: UserViewHolder, itemPosition: Int) {
        itemViewHolder.imageView.setImageDrawable(AppCompatResources.getDrawable(context,
            users[itemPosition].imgId))
        itemViewHolder.nameView.text= users[itemPosition].name
        itemViewHolder.emailView.text= users[itemPosition].email

        itemViewHolder.cardView.setOnClickListener{
            Snackbar
                .make(context,layout,"You have selected ${users[itemPosition].name}!",Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") { }
                .show()
        }

    }

    override fun getItemCount(): Int {
        return users.size
    }

}