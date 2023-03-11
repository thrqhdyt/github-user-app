package com.thorcode.githubuserapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thorcode.githubuserapp.databinding.ListItemUsersBinding

class ListUsersAdapter(private val onItemListener: OnItemListener?=null) :
    ListAdapter<ItemsItem, ListUsersAdapter.ViewHolder>(DiffUtils) {
    class ViewHolder(val binding: ListItemUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUsersAdapter.ViewHolder {
        return ViewHolder(
            ListItemUsersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListUsersAdapter.ViewHolder, position: Int) {
        val user = getItem(position)
        holder.binding.tvUsername.text = user.login
        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .into(holder.binding.avatarImg)
        onItemListener?.let {
            holder.itemView.setOnClickListener {
                onItemListener.onClickListener(user.login)
            }
        }

    }

    companion object DiffUtils : DiffUtil.ItemCallback<ItemsItem>() {
        override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean =
            oldItem == newItem

    }

    interface OnItemListener {
        fun onClickListener(username: String)
    }
}