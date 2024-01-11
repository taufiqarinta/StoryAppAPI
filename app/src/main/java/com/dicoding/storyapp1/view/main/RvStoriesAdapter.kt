package com.dicoding.storyapp1.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp1.data.Stories
import com.dicoding.storyapp1.databinding.StoryItemBinding

class RvStoriesAdapter : PagingDataAdapter<Stories, RvStoriesAdapter.RvStoriesViewHolder>(diffUtil){

    inner class RvStoriesViewHolder (val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Stories) {
            binding.tvUsername.text = data.name
            Glide.with(itemView.context).load(data.photoUrl).into(binding.imgPost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvStoriesViewHolder {
        return RvStoriesViewHolder(
            StoryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RvStoriesViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener{
                onClick?.invoke(item)
            }
        }
    }

    var onClick: ((Stories) -> Unit)? = null

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Stories>() {
            override fun areItemsTheSame(oldItem: Stories, newItem: Stories): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Stories, newItem: Stories): Boolean {
                return oldItem == newItem
            }
        }
    }
}