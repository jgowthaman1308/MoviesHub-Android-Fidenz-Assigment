package com.example.movieshub.main.adapters

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.example.movieshub.R
import com.example.movieshub.main.helpers.Const
import com.example.movieshub.main.helpers.snackbarError
import com.example.movieshub.main.interfaces.SeeMoreClickListener
import com.example.movieshub.main.models.Genres
import com.example.movieshub.main.models.MoviesAndTvModelByID
import com.example.movieshub.main.models.Results
import com.example.movieshub.main.services.retrofit_client.ApiClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class UpcomingSlideShowAdapter(context: Context, private val dataSet: ArrayList<Results>, private val frameClickListener: SeeMoreClickListener) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var genreIdWithNames = ArrayList<Genres>()

    init {
        getMovieGenreStr()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return dataSet.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val slidItemLayout = inflater.inflate(R.layout.widget_slide_show, view, false)!!

//        val imageView = slidItemLayout
//            .findViewById(R.id.image) as ImageView

        slidItemLayout.findViewById<TextView>(R.id.tvName).text = dataSet[position].original_title
        slidItemLayout.findViewById<TextView>(R.id.tvYear).text = dataSet[position].release_date.toString().take(4)
        slidItemLayout.findViewById<TextView>(R.id.tvRating).text = dataSet[position].vote_average.toString()
        slidItemLayout.findViewById<TextView>(R.id.tvGenre).text = getGenreStr(dataSet[position].genre_ids)

        val posterImage = dataSet[position].poster_path.toString()
        val urlPoster = "https://image.tmdb.org/t/p/w500$posterImage"
        Picasso.get().load(urlPoster).fit().centerCrop().placeholder(R.drawable.progress_animation).into(slidItemLayout.findViewById<ImageView>(R.id.ivPosterImage))

        slidItemLayout.findViewById<RelativeLayout>(R.id.frame).setOnClickListener {
            frameClickListener.onClickedFrame(dataSet[position].id,true)
        }

        view.addView(slidItemLayout, 0)

        return slidItemLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

    private fun getMovieGenreStr() {
        val call: Call<MoviesAndTvModelByID> = ApiClient.getClient.getInfoMovieGenre(Const.API_KEY)

        call.enqueue(object : Callback<MoviesAndTvModelByID> {

            override fun onResponse(call: Call<MoviesAndTvModelByID>?, response: Response<MoviesAndTvModelByID>?) {
                if (!response!!.isSuccessful || response.body() == null){

                }
                else{
                    val x = response?.body()!!

                    for (item in x.genres){
                        genreIdWithNames.add(item)
                    }
                }

                //popularRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<MoviesAndTvModelByID>?, t: Throwable?) {
                //loadingEffect.dismiss()
            }

        })
    }

    private fun getGenreStr(genreIDs: ArrayList<Int>): String {

        var genreStr = ""

        var i = 0
        while (i < genreIDs.size){
            if(i > 2){
                break
            }
            else if (i == 0){
                genreStr += checkIntoGENRES(genreIDs[i])
            }
            else {
                genreStr += " " + checkIntoGENRES(genreIDs[i])
            }
            i++
        }
        return genreStr
    }
    private fun checkIntoGENRES(id:Int): String {
        var str = ""
        for (genre in genreIdWithNames){
            if (genre.id == id){
                str = genre.name.toString()
                break
            }
        }
        return str
    }


}