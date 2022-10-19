package com.ebenezer.gana.jsonplaceholdersample.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebenezer.gana.jsonplaceholdersample.data.models.PostItem
import com.ebenezer.gana.jsonplaceholdersample.databinding.ListItemPostBinding

class PostsListAdapter(private val onItemClicked: (PostItem) -> Unit) : ListAdapter<PostItem,
        PostsListAdapter.PostViewHolder>(DiffCallback) {


    class PostViewHolder(private var binding: ListItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(postItem: PostItem) {
            binding.apply {
                title.text = postItem.title
                postBody.text = postItem.body
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            ListItemPostBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PostItem>() {
            override fun areItemsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
                return oldItem.id == newItem.id && oldItem.title == newItem.title
            }
        }
    }


}