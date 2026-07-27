package com.alpine12.catalogMovie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alpine12.catalogMovie.data.model.movie.Movie
import com.alpine12.catalogMovie.data.network.ApiService
import com.alpine12.catalogMovie.data.repository.pagging.MoviePagingSource
import com.alpine12.catalogMovie.data.repository.pagging.SearchMoviePagingSource
import com.alpine12.catalogMovie.utils.Constant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemotePagingDataSource @Inject constructor(
    private val apiService: ApiService
) {
    fun getAllMovies(movieType: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constant.NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 2,
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieType, apiService)
            }
        ).flow
    }

    fun getSearchMovies(movieQuery: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constant.NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 2,
                ),
            pagingSourceFactory = {
                SearchMoviePagingSource(movieQuery, apiService)
            }
        ).flow
    }
}