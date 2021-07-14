package com.alpine12.moviegalleryshow.ui.detailmovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.detailmovie.DetailMovie
import com.alpine12.moviegalleryshow.data.model.detailmovie.ResponseVideos
import com.alpine12.moviegalleryshow.data.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(private val remoteRepository: RemoteRepository) :
    ViewModel() {

    private val _detailMovie = MutableLiveData<ResultData<DetailMovie>>()
    private val _videos = MutableLiveData<ResultData<ResponseVideos>>()


    val detailMovie: LiveData<ResultData<DetailMovie>> = _detailMovie
    val videos: LiveData<ResultData<ResponseVideos>> = _videos

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