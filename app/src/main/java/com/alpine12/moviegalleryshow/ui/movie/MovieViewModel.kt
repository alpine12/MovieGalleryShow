package com.alpine12.moviegalleryshow.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpine12.moviegalleryshow.data.model.Result
import com.alpine12.moviegalleryshow.data.model.movie.ResponseMovie
import com.alpine12.moviegalleryshow.data.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(private val remoteRepository: RemoteRepository) :
    ViewModel() {

    private val _popularMovieList =
        MutableLiveData<Result<ResponseMovie>>()
    val popularMovieList: LiveData<Result<ResponseMovie>> = _popularMovieList

    init {
        getPopularMovie()
        getTopRated()
        getNowPlaying()
    }

    private fun getPopularMovie() = viewModelScope.launch {
        remoteRepository.getPopularMovie().collect {
           Timber.d("Movie list a = ${it?.data.toString()}")
        }
    }

    private fun getTopRated() = viewModelScope.launch {
        remoteRepository.getTopRatedMovie().collect {
            Timber.d("Movie list b = ${it?.data.toString()}")
        }
    }

    private fun getNowPlaying() = viewModelScope.launch {
        remoteRepository.getNowPlayingMovie().collect {
            Timber.d("Movie list c = ${it?.data.toString()}")
        }
    }

}