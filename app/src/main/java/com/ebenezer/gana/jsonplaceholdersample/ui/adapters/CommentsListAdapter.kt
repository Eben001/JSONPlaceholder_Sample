package com.ebenezer.gana.jsonplaceholdersample.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebenezer.gana.jsonplaceholdersample.data.models.CommentItem
import com.ebenezer.gana.jsonplaceholdersample.databinding.ListItemCommentBinding

class CommentsListAdapter : ListAdapter<CommentItem,
        CommentsListAdapter.CommentViewHolder>(DiffCallback) {


    class CommentViewHolder(private var binding: ListItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(commentItem: CommentItem) {
            binding.apply {
                name.text = commentItem.name
                email.text = commentItem.email
                commentBody.text = commentItem.body
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ListItemCommentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)


    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CommentItem>() {
            override fun areItemsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
                return oldItem.id == newItem.id && oldItem.postId == newItem.postId
            }
        }
    }


}