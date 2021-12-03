package com.alpine12.moviegalleryshow.ui.searchmovie

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.databinding.FragmentSearchBinding
import com.alpine12.moviegalleryshow.ui.adapter.LoaderStateAdapter
import com.alpine12.moviegalleryshow.ui.adapter.MoviesPagedAdapter
import com.alpine12.moviegalleryshow.ui.showallmovie.adapter.AllMovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search),
    MoviesPagedAdapter.OnItemCLickListener {
    private val args: SearchFragmentArgs by navArgs()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchFragmentViewModel by viewModels()
    private lateinit var adapterPaged: MoviesPagedAdapter

    private lateinit var adapter: AllMovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        initUi()
        subscribeUi()
        onClick()
    }

    private fun initUi() {
        adapterPaged = MoviesPagedAdapter(this)
        binding.rvMovieList.adapter = adapterPaged.withLoadStateHeaderAndFooter(
            header = LoaderStateAdapter { adapterPaged.retry() },
            footer = LoaderStateAdapter { adapterPaged.retry() }
        )
        binding.rvMovieList.setHasFixedSize(true)

        viewModel.searchQuery(args.movieTitle)

        adapterPaged.addLoadStateListener { state ->

//            if (state.source.refresh is LoadState.NotLoading && adapterPaged.itemCount >= 1){
//                showToast("Not Loading");
//                showToast("Not loading and have 1")
//            }
//
//            if (state.source.refresh is LoadState.Loading){
//                showToast("Loading")
//            }
//
//            if (state.source.refresh is LoadState.Error){
//                showToast("Error")
//            }


        }
    }


    private fun subscribeUi() {

        lifecycleScope.launchWhenStarted {

            viewModel.moviePaged.observe(viewLifecycleOwner, {
                adapterPaged.submitData(viewLifecycleOwner.lifecycle, it)
            })

//            viewModel.movieUiState.collectLatest {
//                when (it) {
//                    is SearchFragmentViewModel.ResponseNetwork.ERROR -> {
//                        Timber.d("view status error")
//                        Snackbar.make(binding.root, it.msg, Snackbar.LENGTH_SHORT).show()
//                    }
//                    is SearchFragmentViewModel.ResponseNetwork.LOADING -> {
//                        Timber.d("view status loading")
//                        Snackbar.make(binding.root, "Loading", Snackbar.LENGTH_SHORT).show()
//                    }
//                    is SearchFragmentViewModel.ResponseNetwork.SUCCESS -> {
//                        Timber.d("view status  success")
//                        adapter.submitList(it.data.results)
//                        Timber.d("data searh ${it.data.results}")
//                    }
//                }
//            }
        }
    }

    private fun onClick() {
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
//            viewModel.setLoading()
//            viewModel.getSearchMovie(args.movieTitle, 1)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(idMovie: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailMovieFragment(idMovie)
        findNavController().navigate(action)
    }
}
