package com.example.movieshub.main.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieshub.R
import com.example.movieshub.databinding.ActivitySeeMoreBinding
import com.example.movieshub.main.adapters.SeeMoreRecyclerViewAdapter
import com.example.movieshub.main.helpers.CommonFun
import com.example.movieshub.main.helpers.Const
import com.example.movieshub.main.helpers.snackbarError
import com.example.movieshub.main.interfaces.SeeMoreClickListener
import com.example.movieshub.main.models.MoviesAndTvModelBulk
import com.example.movieshub.main.models.Results
import com.example.movieshub.main.services.retrofit_client.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeeMoreActivity : AppCompatActivity() {
    private lateinit var layout: ActivitySeeMoreBinding

    var responseArr = ArrayList<Results>()
    var pageCount = 0
    var isMovie: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivitySeeMoreBinding.inflate(layoutInflater)
        setContentView(layout.root)

        isMovie = intent.extras!!.getBoolean("isMovie")
        if (isMovie){
            findViewById<TextView>(R.id.toolbarTitle).text = "Popular Movies"
        }
        else{
            findViewById<TextView>(R.id.toolbarTitle).text = "Popular TV Series"
        }
        layout.seeMoreSwipeRefresh.isRefreshing = true
        loadPageContents()

        layout.seeMoreSwipeRefresh?.setOnRefreshListener {
            responseArr.clear()
            layout.seeMoreRecyclerView.removeAllViews()
            loadPageContents()

        }
        CommonFun().initBackBtn(findViewById(R.id.btnBack),this)
    }

    private fun loadPageContents() {

        if (isMovie){
            getPopDataFromAPI(1,true)
        }
        else {
            getPopDataFromAPI(1,false)
        }

    }

    private fun loadRV(isMovie: Boolean) {
        layout.seeMoreRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        layout.seeMoreRecyclerView.adapter = SeeMoreRecyclerViewAdapter(responseArr, isMovie, this, object : SeeMoreClickListener {
                override fun onClickedFrame(id: Int, isMovie: Boolean) {
                    //Toast.makeText(this@SeeMoreActivity,id.toString()+"\n"+isMovie.toString(), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SeeMoreActivity, DetailActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("isMovie", isMovie)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
                }
            })
        layout.seeMoreSwipeRefresh.isRefreshing = false
    }

    private fun getPopDataFromAPI(pageNo:Int,isMovie: Boolean) {
        val call: Call<MoviesAndTvModelBulk>
            if (isMovie){
                call = ApiClient.getClient.getInfoPopMovie(Const.API_KEY,pageNo)
            }
            else{
                call = ApiClient.getClient.getInfoPopTV(Const.API_KEY,pageNo)
            }

            call.enqueue(object : Callback<MoviesAndTvModelBulk> {

            override fun onResponse(call: Call<MoviesAndTvModelBulk>?, response: Response<MoviesAndTvModelBulk>?) {

                if (!response!!.isSuccessful || response.body() == null){
                    snackbarError("Problem with the server...")
                }
                else{
                    pageCount = response?.body()!!.total_pages
                    val x = response.body()!!.results
                    var i = 0
                    while (i < x.size){
                        responseArr.add(x[i])
                        i++
                    }
                    if ((pageNo < pageCount) && (pageNo < 5)){
                        getPopDataFromAPI(pageNo + 1,isMovie)
                    }
                    else{
                        loadRV(isMovie)
                    }
                }

                //seeMoreRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<MoviesAndTvModelBulk>?, t: Throwable?) {
                snackbarError("Problem with the server...")
            }

        })
    }
}
