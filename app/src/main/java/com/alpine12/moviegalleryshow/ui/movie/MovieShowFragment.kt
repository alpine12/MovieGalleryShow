package com.alpine12.moviegalleryshow.ui.movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.ResultData.Status.*
import com.alpine12.moviegalleryshow.databinding.FragmentMovieShowBinding
import com.alpine12.moviegalleryshow.ui.movie.adapter.MovieAdapter
import com.alpine12.moviegalleryshow.ui.movie.adapter.PagerMovieAdapter
import com.alpine12.moviegalleryshow.ui.movie.adapter.PagerTransformer
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import timber.log.Timber

@AndroidEntryPoint
class MovieShowFragment : Fragment(R.layout.fragment_movie_show),
    MovieAdapter.OnMovieClickListener {

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var topRatedAdapter: MovieAdapter
    private lateinit var upComingAdapter: MovieAdapter
    private lateinit var popularMovieAdapter: PagerMovieAdapter

    private lateinit var binding: FragmentMovieShowBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieShowBinding.bind(view)
        keyboardEvent()
        initUi()
        subscribeUi()

    }

    private fun initUi() {
        popularMovieAdapter = PagerMovieAdapter()
        topRatedAdapter = MovieAdapter(this)
        upComingAdapter = MovieAdapter(this)

        binding.apply {
            viewPagerPopularMovie.adapter = popularMovieAdapter
            viewPagerPopularMovie.setPageTransformer(PagerTransformer())
            rvTopRatedMovie.adapter = topRatedAdapter
            rvTopRatedMovie.setHasFixedSize(true)
            topRatedAdapter.stateRestorationPolicy = PREVENT_WHEN_EMPTY
            rvUpComingMovies.adapter = upComingAdapter
            rvUpComingMovies.setHasFixedSize(true)
            upComingAdapter.stateRestorationPolicy = PREVENT_WHEN_EMPTY
        }
    }

    private fun subscribeUi() {
        viewModel.popularMovieList.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                    result.data?.results.let {
                        popularMovieAdapter.submitList(it)
                    }
                }
                ERROR -> {
                    result.message?.let {
                        Timber.e(it.toString())
                    }
                }
                LOADING -> {
                    Timber.i("Loadinggg")
                }
            }

        }
        viewModel.upComingMovieList.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                    result.data?.results.let {
                        Timber.d(it.toString())
                        upComingAdapter.submitList(it)
                    }
                }
                ERROR -> {
                    result.message?.let {
                        Timber.e(it.toString())
                    }
                }
                LOADING -> {
                    Timber.i("Loadinggg")
                }
            }

        }
        viewModel.topRatedMovieList.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                    result.data?.results.let {
                        Timber.d(it.toString())
                        topRatedAdapter.submitList(it)
                    }
                }
                ERROR -> {
                    result.message?.let {
                        Timber.e(it.toString())
                    }
                }
                LOADING -> {
                    Timber.i("Loadinggg")
                }
            }

        }
    }

    private fun keyboardEvent() {
        KeyboardVisibilityEvent.setEventListener(
            requireActivity()
        ) { isOpen ->
            if (!isOpen) {
                binding.textInputSearch.clearFocus()
            }
        }
    }

    override fun onMovieClick(idMovie: Int) {
        val action = MovieShowFragmentDirections.actionMenuMovieFragmentToDetailMovieFragment2()
        findNavController().navigate(action)
    }
}