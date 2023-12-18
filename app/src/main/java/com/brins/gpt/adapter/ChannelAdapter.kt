package com.brins.gpt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brins.gpt.databinding.ItemUserBinding
import io.getstream.chat.android.models.User
import javax.inject.Inject

class ChannelAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<User>()
    var userClickListener: ((user: User) -> Unit)? = null

    fun setUsers(users: List<User>) {
        items.clear()
        items.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserViewHolder(ItemUserBinding.inflate(inflater, parent, false))

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = items[position]
        if (holder is UserViewHolder) {
            holder.bindUser(user)
            holder.itemView.setOnClickListener {
                userClickListener?.invoke(user)
            }
        }

    }

}

class UserViewHolder(
    private val binding: ItemUserBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bindUser(user: User) {
        itemView.apply {
            binding.nameTextView.text = user.name
            binding.userAvatarView.setUser(
                user
            )
        }
    }
}