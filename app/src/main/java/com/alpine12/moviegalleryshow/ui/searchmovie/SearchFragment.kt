package com.alpine12.moviegalleryshow.ui.searchmovie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.databinding.FragmentSearchBinding
import com.alpine12.moviegalleryshow.ui.showallmovie.adapter.AllMovieAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), AllMovieAdapter.OnItemCLickListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchFragmentViewModel by viewModels()
    private val args: SearchFragmentArgs by navArgs()
    private lateinit var adapter: AllMovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        initUi()
        subscribeUi()
        onClick()
    }

    private fun initUi() {
        adapter = AllMovieAdapter(this)
        binding.rvMovieList.adapter = adapter
        binding.rvMovieList.setHasFixedSize(true)

        viewModel.searchQuery(args.movieTitle)
    }

    @ExperimentalCoroutinesApi
    private fun subscribeUi() {

        lifecycleScope.launchWhenStarted {
            viewModel.movieUiState.collectLatest {
                when (it) {
                    is SearchFragmentViewModel.ResponseNetwork.ERROR -> {
                        Timber.d("view status error")
                        Snackbar.make(binding.root, it.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is SearchFragmentViewModel.ResponseNetwork.LOADING -> {
                        Timber.d("view status loading")
                        Snackbar.make(binding.root, "Loading", Snackbar.LENGTH_SHORT).show()
                    }
                    is SearchFragmentViewModel.ResponseNetwork.SUCCESS -> {
                        Timber.d("view status  success")
                        adapter.submitList(it.data.results)
                        Timber.d("data searh ${it.data.results}")
                    }
                }
            }
        }
    }

    private fun onClick() {
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
//            viewModel.setLoading()
//            viewModel.getSearchMovie(args.movieTitle, 1)
        }
    }

    override fun onItemClick(idMovie: Int) {

    }
}
