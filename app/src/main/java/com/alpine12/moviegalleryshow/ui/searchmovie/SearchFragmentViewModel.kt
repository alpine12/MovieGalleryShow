package com.alpine12.moviegalleryshow.ui.searchmovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import com.alpine12.moviegalleryshow.data.repository.RemotePagingDataSource
import com.alpine12.moviegalleryshow.data.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val remotePagingDataSource: RemotePagingDataSource
) :
    ViewModel() {

    private val _stateLaunch = MutableStateFlow<Boolean>(true)
    private val _moviePaged = MutableLiveData<PagingData<Movie>>()
    val moviePaged: LiveData<PagingData<Movie>> = _moviePaged

    fun searchQuery(query: String) {
        if (_stateLaunch.value) {
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
}