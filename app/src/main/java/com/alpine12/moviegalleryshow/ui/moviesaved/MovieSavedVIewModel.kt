package com.alpine12.moviegalleryshow.ui.moviesaved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpine12.moviegalleryshow.data.database.MovieEntity
import com.alpine12.moviegalleryshow.data.repository.local.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieSavedVIewModel @Inject constructor(private val localRepository: LocalRepository) :
    ViewModel() {

    private val _movieSaved = MutableLiveData<List<MovieEntity>>()
    val movieSaved: LiveData<List<MovieEntity>> = _movieSaved

    fun getMovieSaved() = viewModelScope.launch {
        localRepository.getMovie().collectLatest {
            _movieSaved.postValue(it)
        }
    }
}