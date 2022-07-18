package com.example.movieshub.main.models

import com.google.gson.annotations.SerializedName

// http://www.json2kotlin.com

class MoviesAndTvModelBulk(

    @SerializedName("page") var page : Int = 0,
    @SerializedName("total_results") var total_results : Int = 0,
    @SerializedName("total_pages") var total_pages : Int = 0,
    @SerializedName("results") var results : ArrayList<Results>
)

data class Results (

    @SerializedName("popularity") var popularity : Double = 0.toDouble(),
    @SerializedName("vote_count") var vote_count : Int = 0,
    @SerializedName("video") var video : Boolean = false,
    @SerializedName("poster_path") var poster_path : String? = null,
    @SerializedName("id") var id : Int = 0,
    @SerializedName("adult") var adult : Boolean = false,
    @SerializedName("backdrop_path") var backdrop_path : String? = null,
    @SerializedName("original_language") var original_language : String? = null,
    @SerializedName("original_title") var original_title :String? = null,//for movie
    @SerializedName("genre_ids") var genre_ids : ArrayList<Int>,
    @SerializedName("title") var title : String? = null,//for movies
    @SerializedName("name") var name : String? = null,//for tv series
    @SerializedName("vote_average") var vote_average : Double = 0.toDouble(),
    @SerializedName("overview") var overview : String? = null,
    @SerializedName("release_date") var release_date : String? = null,//for movie
    @SerializedName("first_air_date") var first_air_date : String? = null//gir tv

)