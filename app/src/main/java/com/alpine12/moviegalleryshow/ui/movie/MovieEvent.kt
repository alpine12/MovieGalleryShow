package com.alpine12.moviegalleryshow.ui.movie

import com.alpine12.moviegalleryshow.data.model.movie.Movie

class MovieEvent {

    sealed class PopularMovieEvent{
        object EventLoading : PopularMovieEvent()
        data class EventResponseSuccess(val movie: List<Movie>?) : PopularMovieEvent()
        data class EventResponseError(val message : String) : PopularMovieEvent()
    }

    sealed class NowPlayingMovieEvent{
        object EventLoading : NowPlayingMovieEvent()
        data class EventResponseSuccess(val movie: List<Movie>?) : NowPlayingMovieEvent()
        data class EventResponseError(val message : String) : NowPlayingMovieEvent()
    }

    sealed class TopRatedMovieEvent{
        object EventLoading : TopRatedMovieEvent()
        data class EventResponseSuccess(val movie: List<Movie>?) : TopRatedMovieEvent()
        data class EventResponseError(val message : String) : TopRatedMovieEvent()
    }

}