package com.alpine12.moviegalleryshow.ui.movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alpine12.moviegalleryshow.BuildConfig
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.movie.DetailMovie
import com.alpine12.moviegalleryshow.databinding.FragmentDetailMovieBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragment_detail_movie) {

    private lateinit var binding: FragmentDetailMovieBinding
    private val viewModel: MovieViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDetailMovieBinding.bind(view)
        initUi()
        subscribeUi()
    }

    private fun initUi() {
        viewModel.getDetailMovie(423108)
    }

    private fun subscribeUi() {
        viewModel.detailMovie.observe(viewLifecycleOwner, {
            when (it.status) {
                ResultData.Status.LOADING -> {
                    Snackbar.make(binding.root, "Loading", Snackbar.LENGTH_SHORT).show()
                }

                ResultData.Status.SUCCESS -> {
                    showData(it.data)
                }

                ResultData.Status.ERROR -> {
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showData(data: DetailMovie?) {
        data?.let {
            Glide.with(binding.root)
                .load(BuildConfig.imageUrl + data.backdrop_path)
                .into(binding.imgPosterPath)
            Timber.d(BuildConfig.imageUrl + data.poster_path)
            binding.tvTitleMovie.text = data.original_title
            binding.tvGenreMovie.text =  data.genres.joinToString(" | ") {
                it.name
            }

            binding.tvDescStoryLine.text = data.overview

        }
    }
}