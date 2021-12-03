package com.alpine12.moviegalleryshow.ui.showallmovie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.databinding.BottomsheetErrorBinding
import com.alpine12.moviegalleryshow.databinding.FragmentListAllMoviesBinding
import com.alpine12.moviegalleryshow.ui.adapter.MoviesPagedAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.system.exitProcess

@AndroidEntryPoint
class ShowAllMovieFragment : Fragment(R.layout.fragment_list_all_movies),
    MoviesPagedAdapter.OnItemCLickListener {

    private val args: ShowAllMovieFragmentArgs by navArgs()

    private val viewModel: ShowAllMovieVewModel by viewModels()
    lateinit var binding: FragmentListAllMoviesBinding
    private lateinit var adapterPager: MoviesPagedAdapter
    private lateinit var errorDialog: BottomSheetDialog
    private lateinit var sheetBinding: BottomsheetErrorBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("State Movie Created")
        binding = FragmentListAllMoviesBinding.bind(view)
        initUi()
        subscribeOnUi()
        initShimmer()
    }

    private fun initUi() {
        adapterPager = MoviesPagedAdapter(this)
        sheetBinding = BottomsheetErrorBinding.inflate(layoutInflater)
        errorDialog = BottomSheetDialog(requireContext()).apply {
            setContentView(sheetBinding.root)
            setCancelable(false)
        }

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

                if (loadState.source.refresh is LoadState.NotLoading && adapterPager.itemCount > 1) {
                  binding.containerShimmer.apply {
                      visibility = View.GONE
                      stopShimmer()
                  }
                    errorDialog.dismiss()
                }

                if (loadState.source.refresh is LoadState.Error) {
                    showErrorMessage()
                }
            }
        }
    }

    private fun initShimmer() {
        binding.containerShimmer.startShimmer()
    }



    private fun showErrorMessage() {
        errorDialog.show()
        sheetBinding.btnOk.setOnClickListener {
            adapterPager.refresh()
            errorDialog.dismiss()
        }

        sheetBinding.btnExit.setOnClickListener {
            activity?.finish()
            exitProcess(0)
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