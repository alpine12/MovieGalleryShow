package com.alpine12.moviegalleryshow.ui.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alpine12.moviegalleryshow.BuildConfig
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import com.alpine12.moviegalleryshow.databinding.ItemListPagerBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import timber.log.Timber

class PagerMovieAdapter(private val onPagerClick: OnPagerClick) :
    ListAdapter<Movie, PagerMovieAdapter.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        Timber.d(currentItem.title)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: ItemListPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = position
                    if (position != RecyclerView.NO_POSITION) {
                        val idMovie = getItem(position).id
                        onPagerClick.onPagerClick(idMovie)
                    }
                }
            }
        }

        fun bind(movie: Movie) {
            Glide.with(binding.root)
                .load(BuildConfig.imageUrl + movie.backdrop_path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_movie_nav)
                .centerCrop()
                .into(binding.ivMoviePoster)

            binding.tvTitleMovie.text = movie.title
            binding.tvRatingMovie.text = movie.vote_average.toString()
        }
    }

    interface OnPagerClick {
        fun onPagerClick(idMovie: Int)
    }

    class DiffCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem

    }
}