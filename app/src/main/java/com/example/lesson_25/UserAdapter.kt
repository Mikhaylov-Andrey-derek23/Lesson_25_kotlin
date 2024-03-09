package com.example.lesson_25

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lesson_25.databinding.UserItemBinding

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var users = mutableListOf<Users>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return  UserViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class UserViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(users: Users){
            binding.apply {
                tvName.text = users.first_name
                Glide.with(binding.root.context).load(users.avatar).into(ivPicture)
                rvSecondName.text= users.last_name
                rvEmail.text = users.email

            }
        }
    }
}