package com.alpine12.moviegalleryshow.ui.searchmovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import com.alpine12.moviegalleryshow.data.model.movie.ResponseMovie
import com.alpine12.moviegalleryshow.data.repository.RemotePagingDataSource
import com.alpine12.moviegalleryshow.data.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val remotePagingDataSource: RemotePagingDataSource
) :
    ViewModel() {

    private val _stateLaunch = MutableStateFlow<Boolean>(true)
    private val _movieUiState = MutableStateFlow<ResponseNetwork>(ResponseNetwork.LOADING)
    val movieUiState: StateFlow<ResponseNetwork> = _movieUiState

    private val _moviePaged = MutableLiveData<PagingData<Movie>>()
    val moviePaged: LiveData<PagingData<Movie>> = _moviePaged

    fun searchQuery(query: String) {
        if (_stateLaunch.value) {
//            getSearchMovie(query, 1)
            getSearchMovie(query)
            _stateLaunch.value = false
        }
    }

    private fun getSearchMovie(query: String) = viewModelScope.launch {
        remotePagingDataSource.getSearchMovies(query).cachedIn(viewModelScope)
            .distinctUntilChanged().collectLatest {
            _moviePaged.value = it
        }
    }

    fun getSearchMovie(query: String, page: Int) = viewModelScope.launch {
        Timber.d("search Trigger")
        delay(2000L)
        remoteRepository.getSearchMovie(query, 1).collect {
            Timber.d("search Result")
            it?.let { res ->
                when (res.status) {
                    ResultData.Status.SUCCESS -> {
                        Timber.d("search Result success")
                        _movieUiState.value = ResponseNetwork.SUCCESS(res.data!!)
                    }
                    ResultData.Status.ERROR -> {
                        Timber.d("search Result error")
                        _movieUiState.value = ResponseNetwork.ERROR(res.message.toString())
                    }
                    ResultData.Status.LOADING -> {
                        Timber.d("search Result Loading")
                        _movieUiState.value = ResponseNetwork.LOADING
                    }
                }
            }
        }
    }

    sealed class ResponseNetwork() {
        object LOADING : ResponseNetwork()
        data class SUCCESS(val data: ResponseMovie) : ResponseNetwork()
        data class PAGED(val data: PagingData<Movie>) : ResponseNetwork()
        data class ERROR(val msg: String) : ResponseNetwork()
    }

}