package com.alpine12.moviegalleryshow.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.movie.Genres
import com.alpine12.moviegalleryshow.data.model.movie.ResponseGenres
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
        MutableLiveData<ResultData<ResponseMovie>>()
    private val _topRatedMovieList = MutableLiveData<ResultData<ResponseMovie>>()
    private val _upComingMovieList = MutableLiveData<ResultData<ResponseMovie>>()
    private val _genres = MutableLiveData<ResultData<ResponseGenres>>()

    val popularMovieList: LiveData<ResultData<ResponseMovie>> = _popularMovieList
    val topRatedMovieList: LiveData<ResultData<ResponseMovie>> = _topRatedMovieList
    val upComingMovieList: LiveData<ResultData<ResponseMovie>> = _upComingMovieList
    val genres: LiveData<ResultData<ResponseGenres>> = _genres


    init {
        getGenres()
        getPopularMovie()
        getTopRated()
        getUpComing()

    }

    private fun getGenres() = viewModelScope.launch {
        remoteRepository.getGenres().collect {
            it.data.let {data ->
                data?.genres?.get(0)?.selected = true
            }

            Timber.d(it.data!!.genres.toString())
            _genres.postValue(it)
        }
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
           Timber.d("Size of list ${ it?.data?.results?.size}")
        }
    }
}