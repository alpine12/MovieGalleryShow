package com.alpine12.moviegalleryshow.ui.showallmovie

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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
        initShimmer()
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
        adapterPager.addLoadStateListener { loadState ->

            binding.apply {


                if (loadState.source.refresh is LoadState.NotLoading && adapterPager.itemCount > 1){
                    containerShimmer.isVisible = false
                }

                if (loadState.source.refresh is LoadState.Error){
                    showErrorMessage()
                }
            }
        }
    }

    private fun initShimmer() {
        binding.containerShimmer.startShimmer()
    }

    private fun getLoadingMovie() {
        binding.containerShimmer.apply {
            stopShimmer()
            visibility = View.GONE
        }
    }

    private fun showErrorMessage() {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage("Terjadi masalah!")
            .setTitle("Ada masalah")
            .setNeutralButton("Coba Lagi") { _, _ ->
                adapterPager.refresh()
            }
            .setCancelable(false)
            .create()
            .show()
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