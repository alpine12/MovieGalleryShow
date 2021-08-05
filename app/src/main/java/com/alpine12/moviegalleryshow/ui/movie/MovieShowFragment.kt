package com.alpine12.moviegalleryshow.ui.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.ResultData.Status.*
import com.alpine12.moviegalleryshow.data.model.movie.Genres
import com.alpine12.moviegalleryshow.databinding.FragmentMovieShowBinding
import com.alpine12.moviegalleryshow.ui.movie.adapter.GenreAdapter
import com.alpine12.moviegalleryshow.ui.movie.adapter.MovieAdapter
import com.alpine12.moviegalleryshow.ui.movie.adapter.PagerMovieAdapter
import com.alpine12.moviegalleryshow.ui.movie.adapter.PagerTransformer
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import timber.log.Timber

@AndroidEntryPoint
class MovieShowFragment : Fragment(R.layout.fragment_movie_show),
    MovieAdapter.OnMovieClickListener, PagerMovieAdapter.OnPagerClick,
    GenreAdapter.OnGenreClickListener {

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var genresAdapter: GenreAdapter
    private lateinit var topRatedAdapter: MovieAdapter
    private lateinit var upComingAdapter: MovieAdapter
    private lateinit var popularMovieAdapter: PagerMovieAdapter
    private lateinit var genreData: MutableList<Genres>

    private lateinit var binding: FragmentMovieShowBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieShowBinding.bind(view)
        keyboardEvent()
        initUi()
        subscribeUi()

    }

    private fun initUi() {
        genreData = mutableListOf()
        genresAdapter = GenreAdapter(this)
        popularMovieAdapter = PagerMovieAdapter(this)
        topRatedAdapter = MovieAdapter(this)
        upComingAdapter = MovieAdapter(this)

        binding.apply {
            tvSeeAllPopular.setOnClickListener {
                val action =
                    MovieShowFragmentDirections.actionMenuMovieFragmentToShowAllMovieFragment()
                findNavController().navigate(action)
            }


            rvGenres.adapter = genresAdapter
            genresAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            viewPagerPopularMovie.adapter = popularMovieAdapter
            viewPagerPopularMovie.setPageTransformer(PagerTransformer())
            rvTopRatedMovie.adapter = topRatedAdapter
            rvTopRatedMovie.setHasFixedSize(true)
            topRatedAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            rvUpComingMovies.adapter = upComingAdapter
            rvUpComingMovies.setHasFixedSize(true)
            upComingAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    private fun subscribeUi() {


        viewModel.genres.observe(viewLifecycleOwner) { result ->

            when (result.status) {

                SUCCESS -> {
                    val data = result.data?.genres!!
                    genreData.addAll(data)
                    genresAdapter.submitList(data)
                }

                LOADING -> {
                    showToast(result.message.toString())
                }

                ERROR -> {
                    showToast(result.message.toString())
                }
            }

        }

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
//                binding.textInputSearch.clearFocus()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onMovieClick(idMovie: Int) {
        val action =
            MovieShowFragmentDirections.actionMenuMovieFragmentToDetailMovieFragment(idMovie)
        findNavController().navigateSafe(action)


    }

    override fun onPagerClick(idMovie: Int) {
        val action =
            MovieShowFragmentDirections.actionMenuMovieFragmentToDetailMovieFragment(idMovie)
        findNavController().navigate(action)
    }

    private fun NavController.navigateSafe(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.let { navigate(direction) }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onGenreClick(genres: Genres) {
        genreData.apply {
            this.map {
                it.selected = false
                if (it.id == genres.id) {
                    it.selected = true
                }
            }
        }
        with(genresAdapter) {
            submitList(genreData)
            notifyDataSetChanged()
        }
    }


}