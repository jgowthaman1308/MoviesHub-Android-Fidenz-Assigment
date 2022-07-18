package com.example.movieshub.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieshub.R
import com.example.movieshub.main.models.Cast
import com.squareup.picasso.Picasso

class ActorsRvAdapter(private var dataList: ArrayList<Cast>, private val context: Context) : RecyclerView.Adapter<ViewHolderActors>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderActors {
        return ViewHolderActors(LayoutInflater.from(context).inflate(R.layout.widget_actors, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderActors, position: Int) {
        holder.actorName.text = dataList[position].name
        val path = dataList[position].profile_path.toString()
        val url = "https://image.tmdb.org/t/p/w500$path"
        Picasso.get().load(url).fit().centerCrop().placeholder(R.drawable.progress_animation).into(holder.actorImage)
    }
}

class ViewHolderActors (view: View) : RecyclerView.ViewHolder(view) {
    val actorImage: ImageView = view.findViewById(R.id.ivActorImage)
    val actorName: TextView = view.findViewById(R.id.tvActorName)

}