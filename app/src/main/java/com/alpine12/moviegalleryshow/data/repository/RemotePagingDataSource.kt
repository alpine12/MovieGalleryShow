package com.alpine12.moviegalleryshow.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import com.alpine12.moviegalleryshow.data.network.ApiService
import com.alpine12.moviegalleryshow.data.repository.pagging.MoviePagingSource
import com.alpine12.moviegalleryshow.utils.Constant
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
}