package com.example.movieshub.main.models

import com.google.gson.annotations.SerializedName

class ActorsModel (

    @SerializedName("id") var id : Int,
    @SerializedName("cast") var cast : List<Cast>
)
data class Cast (

//    @SerializedName("cast_id") var cast_id : Int = 0,
//    @SerializedName("character") var character : String,
//    @SerializedName("credit_id") var credit_id : String,
//    @SerializedName("gender") var gender : Int,
//    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String? = null,
//    @SerializedName("order") var order : Int,
    @SerializedName("profile_path") var profile_path : String? = null
)