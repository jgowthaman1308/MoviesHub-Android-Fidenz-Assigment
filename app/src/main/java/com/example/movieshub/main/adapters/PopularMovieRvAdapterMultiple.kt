package com.example.movieshub.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieshub.R
import com.example.movieshub.main.interfaces.RecyclerViewItemClickListener
import com.example.movieshub.main.models.Results
import com.squareup.picasso.Picasso

class PopularMovieRecyclerViewAdapter (private var dataList: ArrayList<Results>, private val context: Context,
                                       private val recyclerViewItemClickListener: RecyclerViewItemClickListener
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.widget_popular_movies_small, parent, false))
        if (viewType == 1){
            viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.widget_popular_movies_big, parent, false))
        }
        else if(viewType == 2){
            viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.widget_see_more, parent, false))
        }

        return  viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        var type = 0//normal
        if((position == 0) || (position == 6)){
            type = 1//big
        }
        if (position == 5 || position == 11){
            type = 2//see more
        }
        return type
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isMovie = true
        if ((dataList[position].original_title == null) && (dataList[position].name != null)){
            isMovie = false
        }
        if (isMovie){
            holder.title.text = dataList[position].original_title
            holder.mediaType.text = "Movie"
            holder.year.text = dataList[position].release_date.toString().take(4)
        }
        else{
            holder.title.text = dataList[position].name
            holder.mediaType.text = "TV Series"
            holder.year.text = dataList[position].first_air_date.toString().take(4)
        }

        holder.desc.text = dataList[position].overview

        var iconName = dataList[position].poster_path
        if ((position == 0) || (position == 6)){
            iconName = dataList[position].backdrop_path
        }
        val url = "https://image.tmdb.org/t/p/w500$iconName"

        if ((position == 0) || (position == 6)){
            Picasso.get().load(url).fit().centerCrop().placeholder(R.drawable.progress_animation).into(holder.icon)
        }
        else{
            Picasso.get().load(url).placeholder(R.drawable.progress_animation).into(holder.icon)
        }

        if (position == 0){
            holder.catHeading.text = "Movies"
        }
        if (position == 6){
            holder.catHeading.text = "TV Series"
        }


        holder.rowFrame.setOnClickListener {
            recyclerViewItemClickListener.onClickedFrameFromSeeMore(dataList[position].id, isMovie)
        }
        holder.seeMore.setOnClickListener {

            recyclerViewItemClickListener.onClickedSeeMore(dataList[position].id, isMovie)
        }
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.movieTitle)
    val desc: TextView = view.findViewById(R.id.movieDesc)
    val catHeading:TextView = view.findViewById(R.id.categoryTitle)
    val icon: ImageView = view.findViewById(R.id.movieIcon)
    val seeMore:Button = view.findViewById(R.id.btnSeeMore)
    val rowFrame: View = view
    val mediaType:TextView = view.findViewById(R.id.mediaType)
    val year: TextView = view.findViewById(R.id.year)

}