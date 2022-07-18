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
import com.example.movieshub.main.interfaces.FavItemClickLister
import com.example.movieshub.main.models.MoviesAndTvModelByID
import com.squareup.picasso.Picasso

class FavouritesRvAdapter(private var dataList: ArrayList<MoviesAndTvModelByID>, private var isMovie: Boolean, private val context: Context,
                          private val favItemClickLister: FavItemClickLister) : RecyclerView.Adapter<ViewHolderFav>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFav {
        return ViewHolderFav(LayoutInflater.from(context).inflate(R.layout.widget_favourites, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderFav, position: Int) {
        if (isMovie){
            holder.title2.text = dataList[position].original_title
            holder.year.text = dataList[position].release_date.toString().take(4)
        }
        else{
            holder.title2.text = dataList[position].name
            holder.year.text = dataList[position].last_air_date.toString().take(4)
        }
        val path = dataList[position].poster_path.toString()
        val urlIcon = "https://image.tmdb.org/t/p/w500$path"
        Picasso.get().load(urlIcon).fit().centerCrop().placeholder(R.drawable.progress_animation).into(holder.poster)

        holder.rowFrame.setOnClickListener {
            favItemClickLister.onClickedGrid(dataList[position].id, isMovie)
        }
        holder.btnDelete.setOnClickListener {
            favItemClickLister.onClickedDelete(dataList[position].id)
        }
    }
}

class ViewHolderFav (view: View) : RecyclerView.ViewHolder(view) {
    val title2: TextView = view.findViewById(R.id.tvTitle)
    val year: TextView = view.findViewById(R.id.tvYear)
    val poster: ImageView = view.findViewById(R.id.ivPoster)
    val btnDelete: Button = view.findViewById(R.id.delete)
    val rowFrame: View = view

}