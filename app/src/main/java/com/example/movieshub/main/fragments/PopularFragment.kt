package com.example.movieshub.main.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.movieshub.R
import com.example.movieshub.databinding.FragmentFavouritesBinding
import com.example.movieshub.databinding.FragmentPopularBinding
import com.example.movieshub.main.activities.DetailActivity
import com.example.movieshub.main.activities.SeeMoreActivity
import com.example.movieshub.main.adapters.PopularMovieRecyclerViewAdapter
import com.example.movieshub.main.helpers.Const
import com.example.movieshub.main.helpers.snackbarError
import com.example.movieshub.main.interfaces.RecyclerViewItemClickListener
import com.example.movieshub.main.models.MoviesAndTvModelBulk
import com.example.movieshub.main.models.Results
import com.example.movieshub.main.services.retrofit_client.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PopularFragment : Fragment() {
    private lateinit var layout: FragmentPopularBinding

    var responseResultsMovie = ArrayList<Results>()
    var responseResultsTV = ArrayList<Results>()
    var totalResponse = ArrayList<Results>()

    // : Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = FragmentPopularBinding.inflate(inflater, container, false)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loadPageContents()
        layout.popularSwipeRefresh?.setOnRefreshListener {
            loadPageContents()
            layout.popularSwipeRefresh.isRefreshing = false
        }

//        Handler().postDelayed({
//            btn.performClick()
//        },5000)

    }

    private fun loadPageContents() {

        totalResponse.clear()
        arrayInit()
        getDataOfPop(true)
        getDataOfPop(false)

        layout.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        layout.popularRecyclerView.adapter = PopularMovieRecyclerViewAdapter(totalResponse,requireContext(), object : RecyclerViewItemClickListener {
            override fun onClickedFrameFromSeeMore(id: Int, isMovie: Boolean) {
                //Toast.makeText(requireContext(),id.toString()+"\n"+isMovie.toString(),Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id",id)
                intent.putExtra("isMovie",isMovie)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)

            }

            override fun onClickedSeeMore(id: Int, isMovie: Boolean) {
                //Toast.makeText(requireContext(),"Btn Clicked.",Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(),SeeMoreActivity::class.java)
                intent.putExtra("id",id)
                intent.putExtra("isMovie",isMovie)
                startActivity(intent)
                //overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
            }
        })

    }

    companion object {

        fun newInstance(): PopularFragment =
            PopularFragment()
    }

    private fun getDataOfPop(isMovie:Boolean) {

        val call: Call<MoviesAndTvModelBulk> = if (isMovie){
            ApiClient.getClient.getInfoPopMovie(Const.API_KEY,1)
        }
        else {
            ApiClient.getClient.getInfoPopTV(Const.API_KEY,1)
        }

        call.enqueue(object : Callback<MoviesAndTvModelBulk> {

            override fun onResponse(call: Call<MoviesAndTvModelBulk>?, response: Response<MoviesAndTvModelBulk>?) {
                if (!response!!.isSuccessful || response.body() == null){
                    activity?.snackbarError("Problem with the server...")
                }
                else{
                    if (isMovie){
                        //responseResultsMovie.clear()
                        var i = 0
                        while (i < 6){
                            //totalResponse.add(response?.body()!!.results[i])
                            totalResponse[i] = response?.body()!!.results[i]
                            i++
                            layout.popularRecyclerView.adapter?.notifyDataSetChanged()
                        }

                    }
                    else{
                        //responseResultsTV.clear()
                        var i = 0
                        var j = 6
                        while (i < 6){
                            //totalResponse.add(response?.body()!!.results[i])
                            totalResponse[j] = response?.body()!!.results[i]
                            i++
                            j++
                            layout.popularRecyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                }

            }

            override fun onFailure(call: Call<MoviesAndTvModelBulk>?, t: Throwable?) {
                activity?.snackbarError("Problem with the server...")
            }

        })

        //test.text = responseResultsMovie.size.toString()
    }

    fun arrayInit(){
        val genID = ArrayList<Int>(0)
        val x = Results(0.0,0,false,"",0,false,"","","",genID,"","",0.0,"","","")
        var i = 1
        while (i <= 12){
            totalResponse.add(x)
            i++
        }
    }
}
