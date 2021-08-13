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
        Timber.d("State Movie Created")
        binding = FragmentListAllMoviesBinding.bind(view)
        initUi()
        subscribeOnUi()
    }

    private fun initUi() {
        adapterPager = AllMoviesPagedAdapter(this)

        args.movieType.apply {
            val title = if (this.toString() == "top_rated") {
                "You may like"
            } else {
                this.toString()
            }
            binding.tvTitleBar.text = title
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.rvAllMovie.adapter = adapterPager
        adapterPager.addLoadStateListener { state ->

            Timber.d("State 1 $state")
            Timber.d("State 2 ${state.source}")
            when (state.source.refresh) {
                is LoadState.NotLoading -> {
                    Timber.d("pager not Loading")
                }
                LoadState.Loading -> {
                    Timber.d("pager Loading")
                }
                is LoadState.Error -> {
                    Timber.d("pager Error")
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