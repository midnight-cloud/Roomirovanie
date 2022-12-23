package com.evg_ivanoff.roomirovanie.domain

import androidx.recyclerview.widget.DiffUtil
import com.evg_ivanoff.roomirovanie.data.UserDataBase

class UserDiffCallback: DiffUtil.ItemCallback<UserDataBase>() {
    override fun areItemsTheSame(oldItem: UserDataBase, newItem: UserDataBase): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserDataBase, newItem: UserDataBase): Boolean {
        return oldItem == newItem
    }

}