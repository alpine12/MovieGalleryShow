package com.alpine12.moviegalleryshow.ui.showallmovie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.databinding.FragmentListAllMoviesBinding
import com.alpine12.moviegalleryshow.ui.showallmovie.adapter.AllMovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowAllMovieFragment : Fragment(R.layout.fragment_list_all_movies),
    AllMovieAdapter.OnItemCLickListener {

    private val args: ShowAllMovieFragmentArgs by navArgs()

    private val viewModel: ShowAllMovieVewModel by viewModels()
    lateinit var binding: FragmentListAllMoviesBinding
    lateinit var adapter: AllMovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListAllMoviesBinding.bind(view)

        initUi()
        subscribeOnUi()
    }

    private fun initUi() {
        binding.tvTitleBar.text = args.movieType
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        adapter = AllMovieAdapter(this)
        binding.rvAllMovie.adapter = adapter

    }


    private fun subscribeOnUi() {
        viewModel.getAllMovie(args.movieType, 1)

        viewModel.movieList.observe(viewLifecycleOwner, { result ->
            when (result.status) {
                ResultData.Status.SUCCESS -> {
                    val listMovie = result.data?.results
                    adapter.submitList(listMovie)
                }
                ResultData.Status.ERROR -> {

                }
                ResultData.Status.LOADING -> {


                }
            }
        })

    }

    override fun onItemClick(idMovie: Int) {
        val action =
            ShowAllMovieFragmentDirections.actionShowAllMovieFragmentToDetailMovieFragment(idMovie)
        findNavController().navigate(action)
    }
}