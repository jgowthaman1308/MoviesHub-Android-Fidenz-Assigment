package com.example.movieshub.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieshub.R
import com.example.movieshub.main.interfaces.RecyclerViewItemClickListener
import com.example.movieshub.main.models.Results
import com.squareup.picasso.Picasso

class SearchResultRvAdapter(private var dataList: ArrayList<Results>, private val context: Context,
                            private val recyclerViewItemClickListener: RecyclerViewItemClickListener
) : RecyclerView.Adapter<ViewHolderSearchResult>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSearchResult {
        return ViewHolderSearchResult(LayoutInflater.from(context).inflate(R.layout.widget_popular_movies_small, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderSearchResult, position: Int) {
        var isMovie:Boolean = true
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

        val iconName = dataList[position].poster_path
        val url = "https://image.tmdb.org/t/p/w500$iconName"
        Picasso.get().load(url).placeholder(R.drawable.progress_animation).into(holder.icon)

        holder.rowFrame.setOnClickListener {
            recyclerViewItemClickListener.onClickedFrameFromSeeMore(dataList[position].id, isMovie)
        }
    }
}

class ViewHolderSearchResult (view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.movieTitle)
    val desc: TextView = view.findViewById(R.id.movieDesc)
    val icon: ImageView = view.findViewById(R.id.movieIcon)
    val rowFrame: View = view
    val mediaType:TextView = view.findViewById(R.id.mediaType)
    val year: TextView = view.findViewById(R.id.year)

}