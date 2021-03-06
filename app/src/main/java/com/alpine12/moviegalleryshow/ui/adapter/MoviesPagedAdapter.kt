package com.alpine12.moviegalleryshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alpine12.moviegalleryshow.BuildConfig
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import com.alpine12.moviegalleryshow.databinding.ItemListAllMovieBinding
import com.alpine12.moviegalleryshow.utils.Constant
import com.alpine12.moviegalleryshow.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import timber.log.Timber

class MoviesPagedAdapter(private val listener: OnItemCLickListener) :
    PagingDataAdapter<Movie, MoviesPagedAdapter.MovieViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemListAllMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bindItem(movie!!)
    }

    inner class MovieViewHolder(private val binding: ItemListAllMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    Timber.d(getItem(bindingAdapterPosition).toString())
                    listener.onItemClick(getItem(bindingAdapterPosition)!!)
                }
            }
        }

        fun bindItem(movie: Movie) {
            binding.apply {
                tvTitleMovie.text = movie.title
                tvViewersMovie.text = "${movie.popularity} Viewers"
                movie.release_date?.let {
                    Timber.d("dateAdapter $it ${movie.title}")
                    tvYearMovie.text.apply {
                        if (it == "") "Unknown" else tvYearMovie.text =  Utils.dateFormat(it, "yyyy-mm-dd", "yyyy")
                    }
                }
                movie.genre_ids?.let {
                    Timber.d(" genreAdapter ${it.isEmpty()} ${movie.title}")
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

    class DiffCallBack() : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemCLickListener {
        fun onItemClick(movie: Movie)
    }
}
