package com.example.dmitrykurilov.vkfriendviewer.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.dmitrykurilov.vkfriendviewer.R
import com.example.dmitrykurilov.vkfriendviewer.data.User

import kotlinx.android.synthetic.main.friend_layout.view.*
import com.example.dmitrykurilov.vkfriendviewer.util.getIconBitmap
import com.example.dmitrykurilov.vkfriendviewer.util.inflate

class FriendsAdapter(val onIconClickListener: OnIconClickListener) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    private var friends = ArrayList<User>()

    inner class FriendsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                onIconClickListener.onClick(layoutPosition)
            }
        }

        fun bind(user: User) {
            val name = user.first_name + " " + user.last_name
            itemView.friend_layout_name.text = name
            itemView.friend_layout_image.setImageBitmap(user.getIconBitmap())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(parent.inflate(R.layout.friend_layout))
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.bind(friends[position])
    }

    fun addFriends(friends: List<User>) {
        clearFriends()
        this.friends.addAll(friends)
        notifyDataSetChanged()
    }

    fun addFriend(user: User) {
        friends.add(user)
        notifyDataSetChanged()
    }

    interface OnIconClickListener {
        fun onClick(position: Int)
    }

    fun clearFriends() {
        friends.clear()
    }
}