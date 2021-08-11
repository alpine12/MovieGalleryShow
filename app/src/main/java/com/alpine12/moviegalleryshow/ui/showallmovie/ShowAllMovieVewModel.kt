package com.alpine12.moviegalleryshow.ui.showallmovie


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.movie.ResponseMovie
import com.alpine12.moviegalleryshow.data.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowAllMovieVewModel @Inject constructor(private val repository: RemoteRepository) :
    ViewModel() {

    private val _movieList = MutableLiveData<ResultData<ResponseMovie>>()
    val movieList : LiveData<ResultData<ResponseMovie>> = _movieList

    fun getAllMovie(movieType: String, page: Int) = viewModelScope.launch {

        repository.getAllMovie(movieType, page).collect {
            _movieList.postValue(it)
        }

    }

}