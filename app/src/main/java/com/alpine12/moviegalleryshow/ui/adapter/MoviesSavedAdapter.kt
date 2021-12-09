package com.alpine12.moviegalleryshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alpine12.moviegalleryshow.BuildConfig
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.database.MovieEntity
import com.alpine12.moviegalleryshow.databinding.ItemListAllMovieBinding
import com.alpine12.moviegalleryshow.utils.Constant
import com.alpine12.moviegalleryshow.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class MoviesSavedAdapter(private val listener: OnItemClickListener) :
    ListAdapter<MovieEntity, MoviesSavedAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListAllMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemListAllMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener.onItemClick(getItem(bindingAdapterPosition))
            }
        }

        fun bind(movie: MovieEntity) {
            binding.apply {
                tvTitleMovie.text = movie.title
                tvViewersMovie.text = "${movie.popularity} Viewers"
                movie.release_date?.let {
                    tvYearMovie.text.apply {
                        if (it == "") "Unknown" else tvYearMovie.text =
                            Utils.dateFormat(it, "yyyy-mm-dd", "yyyy")
                    }
                }
                movie.genre_ids?.let {
                    tvGenreMovie.text.apply {
                        if (it.isEmpty()) "Unknown" else tvGenreMovie.text = Constant.Genres[it[0]]
                    }
                }
                tvRatingMovie.text = "${movie.vote_average}"
                Glide.with(root).load(BuildConfig.imageUrl + movie.backdrop_path)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_movie_nav)
                    .into(binding.imgBannerMovie)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
            oldItem == newItem

    }

    interface OnItemClickListener {
        fun onItemClick(movie: MovieEntity)
    }

}




