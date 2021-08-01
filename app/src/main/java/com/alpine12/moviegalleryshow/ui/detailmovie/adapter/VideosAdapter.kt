package com.alpine12.moviegalleryshow.ui.detailmovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alpine12.moviegalleryshow.BuildConfig
import com.alpine12.moviegalleryshow.data.model.detailmovie.Videos
import com.alpine12.moviegalleryshow.databinding.ItemListVideosBinding
import com.bumptech.glide.Glide

class VideosAdapter(private val listener: OnTrailerClickListener) :
    ListAdapter<Videos, VideosAdapter.VideosViewHolder>(DiffCallback()) {

    inner class VideosViewHolder(private val binding: ItemListVideosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = position
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemCLick(getItem(position).key)
                }
            }
        }

        fun bind(video: Videos) {
            Glide.with(binding.root)
                .load(BuildConfig.youtubeUrl + video.key + "/maxresdefault.jpg")
                .centerCrop()
                .into(binding.ivTrailer)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Videos>() {
        override fun areItemsTheSame(oldItem: Videos, newItem: Videos): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Videos, newItem: Videos): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val binding =
            ItemListVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return VideosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    interface OnTrailerClickListener {
        fun onItemCLick(key: String)
    }
}