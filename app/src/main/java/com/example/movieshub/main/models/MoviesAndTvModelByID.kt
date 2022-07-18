package com.example.movieshub.main.models

import com.google.gson.annotations.SerializedName

class MoviesAndTvModelByID(

    //@SerializedName("adult") val adult : Boolean,
    @SerializedName("backdrop_path") var backdrop_path : String? = null,
    //@SerializedName("belongs_to_collection") val belongs_to_collection : String,
    //@SerializedName("budget") val budget : Int,
    @SerializedName("genres") var genres : List<Genres>,
    //@SerializedName("homepage") val homepage : String,
    @SerializedName("id") var id : Int = 0,
    //@SerializedName("imdb_id") val imdb_id : String,
    //@SerializedName("original_language") val original_language : String,
    @SerializedName("original_title") var original_title : String? = null,
    @SerializedName("overview") var overview : String? = null,
    //@SerializedName("popularity") val popularity : Double,
    @SerializedName("poster_path") var poster_path : String? = null,
//    @SerializedName("production_companies") val production_companies : List<Production_companies>,
//    @SerializedName("production_countries") val production_countries : List<Production_countries>,
    @SerializedName("release_date") var release_date : String? = null,
//    @SerializedName("revenue") val revenue : Int,
//    @SerializedName("runtime") val runtime : Int,
//    @SerializedName("spoken_languages") val spoken_languages : List<Spoken_languages>,
//    @SerializedName("status") val status : String,
//    @SerializedName("tagline") val tagline : String,
    @SerializedName("title") var title : String? = null,
    //@SerializedName("video") val video : Boolean,
    @SerializedName("vote_average") var vote_average : Double = 0.toDouble(),
    //@SerializedName("vote_count") val vote_count : Int

    @SerializedName("name") val name : String? = null,
    @SerializedName("last_air_date") var last_air_date : String? = null
)
data class Genres (

    @SerializedName("id") val id : Int = 0,
    @SerializedName("name") var name : String? = null
)