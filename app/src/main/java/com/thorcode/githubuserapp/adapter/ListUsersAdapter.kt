package com.thorcode.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thorcode.githubuserapp.database.FavoriteUser
import com.thorcode.githubuserapp.databinding.ListItemUsersBinding

class ListUsersAdapter(private val onItemListener: OnItemListener?=null) : ListAdapter<FavoriteUser, ListUsersAdapter.ViewHolder>(
    DiffUtils
) {
    class ViewHolder(val binding: ListItemUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemUsersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.binding.tvUsername.text = user.username
        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .into(holder.binding.avatarImg)
        onItemListener?.let {
            holder.itemView.setOnClickListener {
                onItemListener.onClickListener(user.username)
            }
        }
    }

    companion object DiffUtils : DiffUtil.ItemCallback<FavoriteUser> (){
        override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean =
            oldItem == newItem
    }

    interface OnItemListener {
        fun onClickListener(username: String)
    }
}