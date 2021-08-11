package com.alpine12.moviegalleryshow.ui.showallmovie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.databinding.FragmentListAllMoviesBinding
import com.alpine12.moviegalleryshow.ui.showallmovie.adapter.AllMoviesPagedAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ShowAllMovieFragment : Fragment(R.layout.fragment_list_all_movies),
    AllMoviesPagedAdapter.OnItemCLickListener {

    private val args: ShowAllMovieFragmentArgs by navArgs()

    private val viewModel: ShowAllMovieVewModel by viewModels()
    lateinit var binding: FragmentListAllMoviesBinding
    private lateinit var adapterPager: AllMoviesPagedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListAllMoviesBinding.bind(view)
        initUi()
        subscribeOnUi()
    }

    private fun initUi() {
        adapterPager = AllMoviesPagedAdapter(this)

        binding.tvTitleBar.text = args.movieType
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.rvAllMovie.adapter = adapterPager
        adapterPager.addLoadStateListener { state ->
            when (state.source.refresh) {
                is LoadState.NotLoading -> {
                    Timber.d("pager not Loading")
                }
                LoadState.Loading -> {
                    Timber.d("pager state  Loading")
                }
                is LoadState.Error -> {
                    Timber.d("pager state  Error")
                }
            }
        }
    }

    private fun subscribeOnUi() {
        viewModel.setMovieType(args.movieType)
        viewModel.moviePaging.observe(viewLifecycleOwner, {
            adapterPager.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    override fun onItemClick(idMovie: Int) {
        val action =
            ShowAllMovieFragmentDirections.actionShowAllMovieFragmentToDetailMovieFragment(idMovie)
        findNavController().navigate(action)
    }
}