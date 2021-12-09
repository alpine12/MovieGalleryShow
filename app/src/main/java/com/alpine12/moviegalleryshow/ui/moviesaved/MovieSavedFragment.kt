package com.alpine12.moviegalleryshow.ui.moviesaved

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.database.MovieEntity
import com.alpine12.moviegalleryshow.databinding.FragmentMovieSavedBinding
import com.alpine12.moviegalleryshow.ui.adapter.MoviesSavedAdapter
import com.alpine12.moviegalleryshow.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieSavedFragment : Fragment(R.layout.fragment_movie_saved),
    MoviesSavedAdapter.OnItemClickListener {

    private val viewModel: MovieSavedVIewModel by viewModels()
    private lateinit var binding: FragmentMovieSavedBinding

    private lateinit var moviesSavedAdapter: MoviesSavedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieSavedBinding.bind(view)
        initUI();
        subscribeOnUi()
    }

    private fun initUI() {
        moviesSavedAdapter = MoviesSavedAdapter(this);
        binding.rvMovieSaved.apply {
            setHasFixedSize(true)
            adapter = moviesSavedAdapter
        }
        viewModel.getMovieSaved()
    }

    private fun subscribeOnUi() {
        viewModel.movieSaved.observe(viewLifecycleOwner, {
            moviesSavedAdapter.submitList(it)
        })
    }

    override fun onItemClick(movie: MovieEntity) {
        val movieSaved = Utils.mapToMovie(movie)
        val action =
            MovieSavedFragmentDirections.actionMenuSaveMovieToDetailMovieFragment(movieSaved)
        findNavController().navigate(action)
    }
}