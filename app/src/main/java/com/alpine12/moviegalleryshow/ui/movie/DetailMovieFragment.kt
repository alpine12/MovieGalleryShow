package com.alpine12.moviegalleryshow.ui.movie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alpine12.moviegalleryshow.BuildConfig
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.detailmovie.DetailMovie
import com.alpine12.moviegalleryshow.databinding.FragmentDetailMovieBinding
import com.alpine12.moviegalleryshow.ui.movie.adapter.CompaniesAdapter
import com.alpine12.moviegalleryshow.ui.movie.adapter.VideosAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragment_detail_movie),
    VideosAdapter.OnTrailerClickListener {

    private lateinit var binding: FragmentDetailMovieBinding
    private val viewModel: MovieViewModel by viewModels()
    private val args: DetailMovieFragmentArgs by navArgs()
    private lateinit var companiesAdapter: CompaniesAdapter
    private lateinit var videosAdapter: VideosAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDetailMovieBinding.bind(view)
        initUi()
        setupToolbar()
        subscribeUi()
    }

    private fun initUi() {
        companiesAdapter = CompaniesAdapter()
        videosAdapter = VideosAdapter(this)

        viewModel.getDetailMovie(args.idMovie)
        viewModel.getVideos(args.idMovie)

        binding.apply {
            rvProductionCompanies.adapter = companiesAdapter
            rvProductionCompanies.setHasFixedSize(true)
            rvTrailers.adapter = videosAdapter
            rvTrailers.hasFixedSize()

        }
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
            Toast.makeText(context, "Tekan", Toast.LENGTH_SHORT).show()
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

        viewModel.videos.observe(viewLifecycleOwner) {
            when (it.status) {
                ResultData.Status.LOADING -> {
                    Snackbar.make(binding.root, "Loading", Snackbar.LENGTH_SHORT).show()
                }

                ResultData.Status.SUCCESS -> {
                    Timber.d(it.data?.results.toString())
                    val result = it.data?.results
                    val key = result?.get(0)?.key
                    videosAdapter.submitList(result)
                    binding.btnTrailer.root.setOnClickListener {
                        intentVideos(key)
                    }
                }

                ResultData.Status.ERROR -> {
                    Timber.e(it.message.toString())
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
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

    override fun onItemCLick(key: String) {
        intentVideos(key)
    }

    private fun intentVideos(key: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.youtube.com/watch?v=" + key)
        intent.setPackage("com.google.android.youtube")
        startActivity(intent)
    }
}