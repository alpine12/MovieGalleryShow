package com.alpine12.moviegaleryshow.ui.detailmovie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alpine12.moviegaleryshow.R
import com.alpine12.moviegaleryshow.databinding.FragmentDetailMovieBinding

class DetailMovieFragment : Fragment(R.layout.fragment_detail_movie) {
    private lateinit var binding: FragmentDetailMovieBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailMovieBinding.bind(view)

    }
}