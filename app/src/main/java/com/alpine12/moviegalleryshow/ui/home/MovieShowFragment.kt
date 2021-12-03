package com.alpine12.moviegalleryshow.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.alpine12.moviegalleryshow.databinding.BottomsheetErrorBinding
import com.alpine12.moviegalleryshow.databinding.FragmentMovieShowBinding
import com.alpine12.moviegalleryshow.ui.home.adapter.GenreAdapter
import com.alpine12.moviegalleryshow.ui.home.adapter.MovieAdapter
import com.alpine12.moviegalleryshow.ui.home.adapter.PagerMovieAdapter
import com.alpine12.moviegalleryshow.ui.home.adapter.PagerTransformer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import timber.log.Timber
import kotlin.system.exitProcess

@AndroidEntryPoint
class MovieShowFragment : Fragment(R.layout.fragment_movie_show),
    MovieAdapter.OnMovieClickListener, PagerMovieAdapter.OnPagerClick,
    GenreAdapter.OnGenreClickListener {

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var genresAdapter: GenreAdapter
    private lateinit var upComingAdapter: PagerMovieAdapter
    private lateinit var topRatedAdapter: MovieAdapter
    private lateinit var popularMovieAdapter: MovieAdapter
    private lateinit var genreData: MutableList<Genres>
    private lateinit var errorDialog: BottomSheetDialog

    private lateinit var binding: FragmentMovieShowBinding
    private lateinit var sheetBinding: BottomsheetErrorBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieShowBinding.bind(view)
        keyboardEvent()
        initUi()
        initShimmer()
        subscribeUi()
        onClick()

    }

    private fun initUi() {
        genreData = mutableListOf()
        genresAdapter = GenreAdapter(this)
        upComingAdapter = PagerMovieAdapter(this)
        popularMovieAdapter = MovieAdapter(this)
        topRatedAdapter = MovieAdapter(this)

        binding.apply {
            rvGenres.adapter = genresAdapter
            genresAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            viewPagerUpcomingMovie.adapter = upComingAdapter
            viewPagerUpcomingMovie.setPageTransformer(PagerTransformer())

            rvTopRatedMovie.adapter = topRatedAdapter
            rvTopRatedMovie.setHasFixedSize(true)
            topRatedAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            rvPopularMovie.adapter = popularMovieAdapter
            rvPopularMovie.setHasFixedSize(true)
            upComingAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        sheetBinding = BottomsheetErrorBinding.inflate(layoutInflater)
        errorDialog = BottomSheetDialog(requireContext())
        errorDialog.setContentView(sheetBinding.root)
        errorDialog.setCancelable(false)
    }

    private fun initShimmer() {
        binding.containerShimmerPager.startShimmer()
        binding.containerShimmerPopular.startShimmer()
        binding.containerShimmerTopRated.startShimmer()
    }

    private fun onClick() {

        binding.btnSearchMovie.setOnClickListener {

            val searchQuery = binding.textInputSearch.text.toString()
            if (searchQuery.isEmpty()) {
                showToast("Query Tidak Boleh Kosong !")
                closeKeyBoard()
                return@setOnClickListener
            }
            closeKeyBoard()
            val action =
                MovieShowFragmentDirections.actionMenuMovieFragmentToSearchFragment(binding.textInputSearch.text.toString())
            findNavController().navigate(action)
        }

        binding.tvSeeAllPopular.setOnClickListener {
            val action =
                MovieShowFragmentDirections.actionMenuMovieFragmentToShowAllMovieFragment("popular")
            findNavController().navigate(action)
        }

        binding.tvYouMayLike.setOnClickListener {
            val action =
                MovieShowFragmentDirections.actionMenuMovieFragmentToShowAllMovieFragment("top_rated")
            findNavController().navigate(action)
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
                    log("GetFromAPi", result.message.toString())
                }
                ERROR -> {
                    log("GetFromAPi", result.message.toString())
                }
            }
        }

        viewModel.upComingMovieList.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                    result.data?.results.let {
                        Timber.d(it.toString())
                        upComingAdapter.submitList(it)
                        getLoadingUpcomingPager(false);
                    }
                }
                ERROR -> {
                    result.message?.let { msg ->
                        showError()
                    }
                }
                LOADING -> {
                    getLoadingUpcomingPager(true)
                }
            }
        }

        viewModel.popularMovieList.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                    result.data?.results.let {
                        popularMovieAdapter.submitList(it)
                        getLoadingPopular(false)
                    }
                }
                ERROR -> {
                    result.message?.let {
                        showError()
                    }
                }
                LOADING -> {
                    getLoadingPopular(true)
                }
            }
        }

        viewModel.topRatedMovieList.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                    result.data?.results.let {
                        topRatedAdapter.submitList(it)
                        getLoadingTopRated(false)
                    }
                }
                ERROR -> {
                    result.message?.let {
                        showError()
                    }
                }
                LOADING -> {
                    getLoadingTopRated(true)
                }
            }
        }
    }

    private fun getLoadingUpcomingPager(loading: Boolean) {
        if (loading) {
            binding.viewPagerUpcomingMovie.visibility = View.INVISIBLE
            binding.containerShimmerPager.visibility = View.VISIBLE
        } else {
            binding.viewPagerUpcomingMovie.visibility = View.VISIBLE
            binding.containerShimmerPager.visibility = View.INVISIBLE
            binding.containerShimmerPager.stopShimmer()
        }
    }

    private fun getLoadingPopular(loading: Boolean) {
        binding.apply {
            if (loading) {
                rvPopularMovie.visibility = View.INVISIBLE
                containerShimmerPopular.visibility = View.VISIBLE
            } else {
                rvPopularMovie.visibility = View.VISIBLE
                containerShimmerPopular.visibility = View.INVISIBLE
                binding.containerShimmerPopular.stopShimmer()
            }
        }
    }

    private fun getLoadingTopRated(loading: Boolean) {
        binding.apply {
            if (loading) {
                rvTopRatedMovie.visibility = View.INVISIBLE
                containerShimmerTopRated.visibility = View.VISIBLE
            } else {
                rvTopRatedMovie.visibility = View.VISIBLE
                containerShimmerTopRated.visibility = View.INVISIBLE
                binding.containerShimmerTopRated.stopShimmer()
            }
        }
    }

    private fun showError() {

        if (!errorDialog.isShowing) {
            errorDialog.show()
            sheetBinding.btnOk.setOnClickListener {
                viewModel.retryConnection()
                errorDialog.dismiss()
            }

            sheetBinding.btnExit.setOnClickListener {
                activity?.finish()
                exitProcess(0)
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun closeKeyBoard() {
        activity?.hideKeyboard(binding.root)
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

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

    private fun log(title: String, message: String) {
        Timber.d("$title pesan $message")
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
            Snackbar.make(binding.root, genres.name, Snackbar.LENGTH_SHORT).show()
        }
        with(genresAdapter) {
            submitList(genreData)
            notifyDataSetChanged()
        }
    }
}