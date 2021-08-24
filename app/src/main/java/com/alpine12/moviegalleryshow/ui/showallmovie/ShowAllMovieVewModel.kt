package com.alpine12.moviegalleryshow.ui.showallmovie


import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import com.alpine12.moviegalleryshow.data.repository.RemotePagingDataSource
import com.alpine12.moviegalleryshow.data.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ShowAllMovieVewModel @Inject constructor(
    private val repository: RemoteRepository,
    private val remotePagingDataSource: RemotePagingDataSource
) :
    ViewModel() {

    private val _movieType = MutableLiveData<String>()

    private val _moviePaging = MutableLiveData<PagingData<Movie>>()
    val moviePaging: LiveData<PagingData<Movie>> = _moviePaging

    fun setMovieType(movieType: String) {
       if (_movieType.value == movieType){
           return
       }
        _movieType.value = movieType
        getPagingMovies(_movieType.value.toString())
    }

    private fun getPagingMovies(movieType: String) = viewModelScope.launch {
        remotePagingDataSource.getAllMovies(movieType).cachedIn(viewModelScope).distinctUntilChanged().collectLatest {
            _moviePaging.postValue(it)
        }
    }
}