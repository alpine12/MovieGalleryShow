package com.alpine12.moviegalleryshow.ui.movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.data.model.Result.Status.*
import com.alpine12.moviegalleryshow.databinding.FragmentMovieShowBinding
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import timber.log.Timber

@AndroidEntryPoint
class MovieShowFragment : Fragment(R.layout.fragment_movie_show) {

    private val viewModel: MovieViewModel by viewModels()

    private lateinit var binding: FragmentMovieShowBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieShowBinding.bind(view)
        keyboardEvent()
        subscribeUi()

    }

    private fun subscribeUi() {

        viewModel.popularMovieList.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                SUCCESS -> {
                    result.data?.results.let {
                        Timber.d(it.toString())
                    }
                }
                ERROR -> {
                    result.message?.let {
                        Timber.e(it.toString())
                    }
                }
                LOADING -> {
                    Timber.i("Loadinggg")
                }
            }
        }

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
}