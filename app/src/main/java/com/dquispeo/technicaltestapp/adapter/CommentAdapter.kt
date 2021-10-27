package com.dquispeo.technicaltestapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dquispeo.technicaltestapp.R
import com.dquispeo.technicaltestapp.databinding.ItemCommentBinding
import com.dquispeo.technicaltestapp.db.model.Comment

class CommentAdapter(
    private val commentList: List<Comment>
) : RecyclerView.Adapter<ViewHolderComment>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderComment {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemCommentBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_comment, parent, false
        )
        return ViewHolderComment(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderComment, position: Int) {
        holder.bind(commentList[position])
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

}

class ViewHolderComment(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(comment: Comment) {
        //binding.itemDtlContent.text = comment.content
        binding.itemDtlContent.text = comment.content
    }
}