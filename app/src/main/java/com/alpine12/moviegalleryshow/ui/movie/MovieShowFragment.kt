package com.alpine12.moviegalleryshow.ui.movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.databinding.FragmentMovieShowBinding
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


class MovieShowFragment : Fragment(R.layout.fragment_movie_show) {
    private lateinit var binding: FragmentMovieShowBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keyboardEvent()
        binding = FragmentMovieShowBinding.bind(view)
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