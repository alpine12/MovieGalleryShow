package com.alpine12.moviegalleryshow.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.detailmovie.DetailMovie
import com.alpine12.moviegalleryshow.data.model.detailmovie.ResponseVideos
import com.alpine12.moviegalleryshow.data.model.movie.ResponseMovie
import com.alpine12.moviegalleryshow.data.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(private val remoteRepository: RemoteRepository) :
    ViewModel() {

    private val _popularMovieList =
        MutableLiveData<ResultData<ResponseMovie>>()
    private val _topRatedMovieList = MutableLiveData<ResultData<ResponseMovie>>()
    private val _upComingMovieList = MutableLiveData<ResultData<ResponseMovie>>()
    private val _detailMovie = MutableLiveData<ResultData<DetailMovie>>()
    private val _videos = MutableLiveData<ResultData<ResponseVideos>>()

    val popularMovieList: LiveData<ResultData<ResponseMovie>> = _popularMovieList
    val topRatedMovieList: LiveData<ResultData<ResponseMovie>> = _topRatedMovieList
    val upComingMovieList: LiveData<ResultData<ResponseMovie>> = _upComingMovieList
    val detailMovie: LiveData<ResultData<DetailMovie>> = _detailMovie
    val videos: LiveData<ResultData<ResponseVideos>> = _videos

    init {
        getPopularMovie()
        getTopRated()
        getUpComing()

    }

    private fun getPopularMovie() = viewModelScope.launch {
        remoteRepository.getPopularMovie().collect {
            _popularMovieList.postValue(it)
        }
    }

    private fun getTopRated() = viewModelScope.launch {
        remoteRepository.getTopRatedMovie().collect {
            _topRatedMovieList.postValue(it)
        }
    }

    private fun getUpComing() = viewModelScope.launch {
        remoteRepository.getNowUpComingMovie().collect {
            _upComingMovieList.postValue(it)
        }
    }

    fun getDetailMovie(idMovie: Int) = viewModelScope.launch {
        remoteRepository.getDetailMovie(idMovie).collect {
            _detailMovie.postValue(it)
        }
    }

    fun getVideos(idMovie: Int) = viewModelScope.launch {
        remoteRepository.getVideos(idMovie).collect {
            _videos.postValue(it)
        }
    }

}