package com.alpine12.moviegalleryshow.ui.movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.alpine12.moviegalleryshow.BuildConfig
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.movie.DetailMovie
import com.alpine12.moviegalleryshow.databinding.FragmentDetailMovieBinding
import com.alpine12.moviegalleryshow.ui.movie.adapter.CompaniesAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragment_detail_movie) {

    private lateinit var binding: FragmentDetailMovieBinding
    private val viewModel: MovieViewModel by viewModels()
    private val args: DetailMovieFragmentArgs by navArgs()
    private lateinit var companiesAdapter: CompaniesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDetailMovieBinding.bind(view)
        initUi()
        subscribeUi()
    }

    private fun initUi() {
        companiesAdapter = CompaniesAdapter()


        viewModel.getDetailMovie(args.idMovie)

        binding.apply {
            rvProductionCompanies.adapter = companiesAdapter
            rvProductionCompanies.setHasFixedSize(true)
        }
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
            Glide.with(this@DetailMovieFragment)
                .load(BuildConfig.imageUrl + data.backdrop_path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_movie_nav)
                .into(binding.imgPosterPath)
            binding.tvTitleMovie.text = data.original_title
            val genre = data.genres.joinToString(" ") {
                it.name
            }
            val originalTitle = data.original_language.uppercase(Locale.getDefault())
            val hours = data.runtime / 60
            val minute = data.runtime % 60
            val duration = "${hours}h${minute}m"
            val subTitle = String.format(
                "%s | %s | %s",
                originalTitle, genre, duration
            )
            binding.tvGenreMovie.text = subTitle
            binding.tvDescStoryLine.text = data.overview

            data.production_companies.apply {
                Timber.d("Companies : ${this.toString()}")
                companiesAdapter.submitList(this)
            }

        }
    }
}