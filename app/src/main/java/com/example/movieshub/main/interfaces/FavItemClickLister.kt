package com.example.movieshub.main.interfaces

interface FavItemClickLister {
    fun onClickedGrid(movieOrTvID: Int,isMovie:Boolean)
    fun onClickedDelete(movieOrTvID: Int)
}