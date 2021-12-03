package com.alpine12.moviegalleryshow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.movie.ResponseGenres
import com.alpine12.moviegalleryshow.data.model.movie.ResponseMovie
import com.alpine12.moviegalleryshow.data.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(private val remoteRepository: RemoteRepository) :
    ViewModel() {

    private val _popularMovieList =
        MutableLiveData<ResultData<ResponseMovie>>()
    private val _topRatedMovieList = MutableLiveData<ResultData<ResponseMovie>>()
    private val _upComingMovieList = MutableLiveData<ResultData<ResponseMovie>>()
    private val _genres = MutableLiveData<ResultData<ResponseGenres>>()

    val popularMovieList: LiveData<ResultData<ResponseMovie>> = _popularMovieList
    val topRatedMovieList: LiveData<ResultData<ResponseMovie>> = _topRatedMovieList
    val upComingMovieList: LiveData<ResultData<ResponseMovie>> = _upComingMovieList
    val genres: LiveData<ResultData<ResponseGenres>> = _genres

    init {
        fetchMovie()
    }

    fun retryConnection() {
        fetchMovie()
    }

    private fun fetchMovie() {
        getGenres()
        getUpComing()
        getPopularMovie()
        getTopRated()
    }

    private fun getGenres() = viewModelScope.launch {
        delay(1500L)
        remoteRepository.getGenres().collect {
            it?.data.let { data ->
                data?.genres?.get(0)?.selected = true
            }
            _genres.postValue(it)
        }
    }

    private fun getUpComing() = viewModelScope.launch {
        delay(1500L)
        remoteRepository.getNowUpComingMovie().collect {
            _upComingMovieList.postValue(it)
        }
    }

    private fun getPopularMovie() = viewModelScope.launch {
        delay(1500L)
        remoteRepository.getPopularMovie().collect {
            _popularMovieList.postValue(it)
        }
    }

    private fun getTopRated() = viewModelScope.launch {
        delay(1500L)
        remoteRepository.getTopRatedMovie().collect {
            _topRatedMovieList.postValue(it)
        }
    }
}