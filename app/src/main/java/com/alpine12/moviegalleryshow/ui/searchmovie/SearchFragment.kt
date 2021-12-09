package com.alpine12.moviegalleryshow.ui.searchmovie

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import com.alpine12.moviegalleryshow.databinding.BottomsheetErrorBinding
import com.alpine12.moviegalleryshow.databinding.FragmentSearchBinding
import com.alpine12.moviegalleryshow.ui.adapter.LoaderStateAdapter
import com.alpine12.moviegalleryshow.ui.adapter.MoviesPagedAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search),
    MoviesPagedAdapter.OnItemCLickListener {
    private val args: SearchFragmentArgs by navArgs()
    private val viewModel: SearchFragmentViewModel by viewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterPaged: MoviesPagedAdapter
    private lateinit var sheetBindingDialog: BottomsheetErrorBinding
    private lateinit var errorDialog: BottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        initUi()
        initShimmer()
        subscribeUi()
        onClick()
    }

    private fun initUi() {
        binding.topBar.tvTitleBar.text = args.movieTitle
        viewModel.searchQuery(args.movieTitle)
        binding.rvMovieList.setHasFixedSize(true)

        sheetBindingDialog = BottomsheetErrorBinding.inflate(layoutInflater)
        errorDialog = BottomSheetDialog(requireContext()).apply {
            setContentView(sheetBindingDialog.root)
            setCancelable(false)
        }

        adapterPaged = MoviesPagedAdapter(this)
        binding.rvMovieList.adapter =
            adapterPaged.withLoadStateFooter(LoaderStateAdapter { adapterPaged.retry() })

        adapterPaged.addLoadStateListener { state ->
            if (state.source.refresh is LoadState.NotLoading && adapterPaged.itemCount >= 1) {
                binding.containerShimmer.apply {
                    visibility = View.GONE
                    stopShimmer()
                }
                errorDialog.dismiss()
            }
            if (state.source.refresh is LoadState.Error) {
                showErrorMessage()
            }
        }
    }

    private fun subscribeUi() {
        lifecycleScope.launchWhenStarted {
            viewModel.moviePaged.observe(viewLifecycleOwner, {
                adapterPaged.submitData(viewLifecycleOwner.lifecycle, it)
            })
        }
    }

    private fun onClick() {
        binding.topBar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initShimmer() {
        binding.containerShimmer.startShimmer()
    }

    private fun showErrorMessage() {
        errorDialog.show()
        sheetBindingDialog.btnOk.setOnClickListener {
            adapterPaged.refresh();
            errorDialog.dismiss()
        }

        sheetBindingDialog.btnExit.setOnClickListener {
            activity?.finish()
            exitProcess(0)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(movie: Movie) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailMovieFragment(movie)
        findNavController().navigate(action)
    }
}
