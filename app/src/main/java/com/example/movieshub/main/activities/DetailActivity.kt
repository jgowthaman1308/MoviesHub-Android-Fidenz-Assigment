package com.example.movieshub.main.activities

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieshub.R
import com.example.movieshub.databinding.ActivityDetailBinding
import com.example.movieshub.databinding.ActivitySeeMoreBinding
import com.example.movieshub.main.adapters.ActorsRvAdapter
import com.example.movieshub.main.databases.object_box.ObjectBox
import com.example.movieshub.main.helpers.CommonFun
import com.example.movieshub.main.helpers.Const
import com.example.movieshub.main.helpers.snackbarError
import com.example.movieshub.main.helpers.snackbarSuccess
import com.example.movieshub.main.models.*
import com.example.movieshub.main.services.retrofit_client.*
import com.squareup.picasso.Picasso
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.query
import io.objectbox.query.Query
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NumberFormatException

class DetailActivity : AppCompatActivity() {
    private lateinit var layout: ActivityDetailBinding

    val context:Context = this
    private lateinit var favBox: Box<FavouritesModel>
    private lateinit var favQuery: Query<FavouritesModel>
    var isMovie: Boolean = false
    var id: Int = 0
    var callFromDeepLink: Boolean = false
    var action: String? = null
    var uriData: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(layout.root)

        isMovie = intent.extras!!.getBoolean("isMovie")
        id = intent.extras!!.getInt("id")


        if(isMovie){
            findViewById<TextView>(R.id.toolbarTitle).text = "Movie"
        }
        else{
            findViewById<TextView>(R.id.toolbarTitle).text = "TV Show"
        }
        CommonFun().initBackBtn(findViewById(R.id.btnBack),this)

        loadData(id, isMovie)
        loadActorData(id, isMovie)

        layout.detailSwipeRefresh.setOnRefreshListener {
            loadData(id, isMovie)
            loadActorData(id, isMovie)
            layout.detailSwipeRefresh.isRefreshing = false
        }

        initUI(id,isMovie)
    }

    private fun initUI(id: Int, isMovie: Boolean){
        favBox = ObjectBox.boxStore.boxFor()
        favQuery = favBox.query {
            equal(FavouritesModel_.movieOrTvID, id)
            order(FavouritesModel_.movieOrTvID)
        }

        val favArr = favQuery.find()

        if (favArr.size > 0){
            layout.favIcon.visibility = View.VISIBLE
            layout.btnAddFavourites.text = "Added to Favourites"
            layout.btnAddFavourites.isEnabled = false
        }
        else{
            layout.btnAddFavourites.setOnClickListener {
                val fav = FavouritesModel(movieOrTvID = id, isMovie = isMovie)
                favBox.put(fav)

                Toast.makeText(applicationContext, "Added to Favourites..", Toast.LENGTH_LONG).show()

                layout.btnAddFavourites.text = "Added to Favourites"
                layout.btnAddFavourites.isEnabled = false
                layout.favIcon.visibility = View.VISIBLE

            }
        }
    }

    private fun loadData(id:Int, isMovie:Boolean) {

        val call: Call<MoviesAndTvModelByID> = if (isMovie){
            ApiClient.getClient.getInfoMovie(id, Const.API_KEY)
        }
        else {
            ApiClient.getClient.getInfoTV(id, Const.API_KEY)
        }

        call.enqueue(object : Callback<MoviesAndTvModelByID> {

            override fun onResponse(call: Call<MoviesAndTvModelByID>?, response: Response<MoviesAndTvModelByID>?) {
                if (!response!!.isSuccessful || response.body() == null){
                    snackbarError("Problem with the server...")
                }
                else{
                    val x = response?.body()!!

                    val iconImage = x.poster_path
                    val bannerImage = x.backdrop_path
                    val desc = x.overview
                    val rating = x.vote_average.toString()
                    var genreStr = ""

                    var i = 0
                    while (i < x.genres.size){
                        if (i > 2){
                            break
                        }
                        else if (i == 0){
                            genreStr += x.genres[i].name.toString()
                        }
                        else{
                            genreStr += "  " + x.genres[i].name.toString()
                        }
                        i++
                    }
                    var nameOrTitle:String
                    var year:String
                    if (isMovie){
                        nameOrTitle = x.original_title.toString()
                        year = x.release_date.toString().take(4)
                    }
                    else{
                        nameOrTitle = x.name.toString()
                        year = x.last_air_date.toString().take(4)
                    }

                    //--------------------------------------------------------------------Binding

                    layout.tvTitleOrName.text = nameOrTitle
                    layout.tvDesc.text = desc
                    layout.tvYear.text = year
                    layout.tvRating.text = rating
                    layout.tvGenre.text = genreStr

                    val urlIcon = "https://image.tmdb.org/t/p/w500$iconImage"
                    val urlBanner = "https://image.tmdb.org/t/p/w500$bannerImage"
                    Picasso.get().load(urlIcon).fit().centerCrop().placeholder(R.drawable.progress_animation).into(layout.ivIconImage)
                    Picasso.get().load(urlBanner).fit().centerCrop().placeholder(R.drawable.progress_animation).into(layout.ivBannerImage)
                }
            }

            override fun onFailure(call: Call<MoviesAndTvModelByID>?, t: Throwable?) {
                snackbarError("Problem with the server...")
            }

        })
    }


    private fun loadActorData(id:Int, isMovie:Boolean) {

        val call: Call<ActorsModel> = if (isMovie){
            ApiClient.getClient.getInfoActorsMovie(id, Const.API_KEY)
        }
        else {
            ApiClient.getClient.getInfoActorsTV(id, Const.API_KEY)
        }

        call.enqueue(object : Callback<ActorsModel> {

            override fun onResponse(call: Call<ActorsModel>?, response: Response<ActorsModel>?) {
                if (!response!!.isSuccessful || response.body() == null){
                    snackbarError("Problem with the server...")
                }
                else{
                    val actors = response?.body()!!
                    val responseArr = ArrayList<Cast>()
                    responseArr.clear()
                    for (actor in  actors.cast){
                        responseArr.add(actor)
                    }
                    layout.rvActors.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                    layout.rvActors.adapter = ActorsRvAdapter(responseArr,context)
                }

            }

            override fun onFailure(call: Call<ActorsModel>?, t: Throwable?) {
                snackbarError("Problem with the server...")
            }

        })
    }
}
