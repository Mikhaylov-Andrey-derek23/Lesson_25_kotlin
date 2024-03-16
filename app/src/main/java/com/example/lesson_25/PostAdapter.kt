package com.example.lesson_25

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lesson_25.databinding.PostItemBinding
import com.example.lesson_25.retrofit.Post

class PostAdapter: RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var posts = mutableListOf<Post>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return  PostViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
      return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    inner class PostViewHolder(val binding:PostItemBinding ) : RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            binding.apply {
                tvTitle.text = post.title
                Glide.with(binding.root.context).load(post.url).into(ivPicture)
                tvdescription.text= post.description

            }
        }
    }
}