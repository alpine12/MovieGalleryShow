package com.alpine12.moviegalleryshow.ui.movie.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.movie.Genres
import com.alpine12.moviegalleryshow.databinding.ItemListTextCategoryBinding
import timber.log.Timber

class GenreAdapter(private val listener: OnGenreClickListener) :
    ListAdapter<Genres, GenreAdapter.GenreViewHolder>(DiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding =
            ItemListTextCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class GenreViewHolder(private val binding: ItemListTextCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    listener.onGenreClick(getItem(bindingAdapterPosition))
                }
            }
        }

        fun bind(genres: Genres) {

            binding.apply {
                tvCategorySuggestion.text = genres.name
                if (genres.selected) changeBg() else defaultBg()
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun changeBg() {
            binding.parentCategories.background =
                binding.root.context.getDrawable(R.drawable.rounded_corner_textview)
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun defaultBg() {
            binding.parentCategories.background = binding.root.context.getDrawable(R.drawable.rounded_corner_textview_black)
        }
    }

    class DiffUtils : DiffUtil.ItemCallback<Genres>() {
        override fun areItemsTheSame(oldItem: Genres, newItem: Genres): Boolean {
            return oldItem.selected == newItem.selected
        }

        override fun areContentsTheSame(oldItem: Genres, newItem: Genres): Boolean {
            return oldItem == newItem
        }
    }

    interface OnGenreClickListener {
        fun onGenreClick(genres: Genres)
    }
}