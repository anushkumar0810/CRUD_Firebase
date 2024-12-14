package com.example.firebase_curd.Views.Main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_curd.MVVM.Model.User
import com.example.firebase_curd.databinding.ItemUserBinding

class UserAdapter(private val listener: (User, String) -> Unit) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    companion object {
        const val ACTION_EDIT = "edit"
        const val ACTION_DELETE = "delete"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userNameText.text = user.name
            binding.userEmailText.text = user.email

            binding.update.setOnClickListener {
                listener(user, ACTION_EDIT)
            }

            binding.delete.setOnClickListener {
                listener(user, ACTION_DELETE)
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}

