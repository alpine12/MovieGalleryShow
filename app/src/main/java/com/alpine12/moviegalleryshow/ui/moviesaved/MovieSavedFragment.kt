package com.alpine12.moviegalleryshow.ui.moviesaved

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.database.MovieEntity
import com.alpine12.moviegalleryshow.databinding.FragmentMovieSavedBinding
import com.alpine12.moviegalleryshow.ui.adapter.MoviesSavedAdapter
import com.alpine12.moviegalleryshow.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import timber.log.Timber

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

        binding.textInputSearch.addTextChangedListener(object : TextWatcher {
            private var searchJob: Job? = null
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = p0.toString()
                searchJob?.cancel()
                searchJob = lifecycleScope.launchWhenStarted {
                    delay(300L)
                   viewModel.getSearchMovie(searchText)
                }
            }
            override fun afterTextChanged(p0: Editable?) = Unit
        })
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