package com.example.movieshub.main.services.api_interface

import com.example.movieshub.main.models.ActorsModel
import com.example.movieshub.main.models.MoviesAndTvModelByID
import com.example.movieshub.main.models.MoviesAndTvModelBulk
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/popular")
    fun getInfoPopMovie(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<MoviesAndTvModelBulk>

    @GET("tv/popular")
    fun getInfoPopTV(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<MoviesAndTvModelBulk>

    @GET("movie/{movie_id}")
    fun getInfoMovie(@Path("movie_id") movieID: Int, @Query("api_key") apiKey: String): Call<MoviesAndTvModelByID>

    @GET("tv/{tv_id}")
    fun getInfoTV(@Path("tv_id") id: Int, @Query("api_key") apiKey: String): Call<MoviesAndTvModelByID>

    @GET("movie/{movie_id}/credits")
    fun getInfoActorsMovie(@Path("movie_id") movieID: Int, @Query("api_key") apiKey: String): Call<ActorsModel>

    @GET("tv/{tv_id}/credits")
    fun getInfoActorsTV(@Path("tv_id") tvID: Int, @Query("api_key") apiKey: String): Call<ActorsModel>

    @GET("search/movie")
    fun getInfoSearchMovie(@Query("query") searchKey: String, @Query("api_key") apiKey: String): Call<MoviesAndTvModelBulk>

    @GET("search/tv")
    fun getInfoSearchTV(@Query("query") searchKey: String, @Query("api_key") apiKey: String): Call<MoviesAndTvModelBulk>

    @GET("movie/upcoming")
    fun getInfoUpcomingMovie(@Query("api_key") apiKey: String): Call<MoviesAndTvModelBulk>

    @GET("genre/movie/list")
    fun getInfoMovieGenre(@Query("api_key") apiKey: String): Call<MoviesAndTvModelByID>
}