package com.example.randommovie.mapper

import com.example.randommovie.api.model.MovieListApi
import com.example.randommovie.repository.model.MovieList

interface MovieListApiToMovieListMapper : Mapper<MovieListApi?, MovieList?>

class MovieListApiToMovieListMapperImpl(
    private val movieApiToMovieMapper: MovieApiToMovieMapper
) : MovieListApiToMovieListMapper {

    override fun map(input: MovieListApi?): MovieList? =
        input?.let {
            MovieList(
                results = it.results?.map { movieApi ->
                    movieApiToMovieMapper.map(movieApi)
                }
            )
        }
}