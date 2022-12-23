package com.evg_ivanoff.roomirovanie.domain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.evg_ivanoff.roomirovanie.data.UserDataBase
import com.evg_ivanoff.roomirovanie.databinding.RvOneElBinding

class UserAdapter :
    ListAdapter<UserDataBase, UserAdapter.ItemViewHolder>(UserDiffCallback()) {

    var onItemClickListener: ((UserDataBase) -> Unit)? = null
    var onItemLongClickListener: ((UserDataBase) -> Unit)? = null

    class ItemViewHolder(private val binding: RvOneElBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserDataBase) {
            binding.tvId.text = user.id.toString()
            binding.tvName.text = user.name
            binding.tvAge.text = user.age
            binding.tvEmail.text = user.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RvOneElBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(item)
            true
        }
    }

}