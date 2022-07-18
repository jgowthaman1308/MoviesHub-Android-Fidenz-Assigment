package com.example.movieshub.main.fragments.favourites_tabs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager

import com.example.movieshub.R
import com.example.movieshub.databinding.FragmentMovieBinding
import com.example.movieshub.databinding.FragmentTvBinding
import com.example.movieshub.main.activities.DetailActivity
import com.example.movieshub.main.adapters.FavouritesRvAdapter
import com.example.movieshub.main.databases.object_box.ObjectBox
import com.example.movieshub.main.helpers.Const
import com.example.movieshub.main.helpers.GridSpacingItemDecoration
import com.example.movieshub.main.helpers.snackbarError
import com.example.movieshub.main.interfaces.FavItemClickLister
import com.example.movieshub.main.models.FavouritesModel
import com.example.movieshub.main.models.FavouritesModel_
import com.example.movieshub.main.models.MoviesAndTvModelByID
import com.example.movieshub.main.services.retrofit_client.ApiClient
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.query
import io.objectbox.query.Query
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class TvFragment : Fragment() {
    private lateinit var layout: FragmentTvBinding

    var dataSet = ArrayList<MoviesAndTvModelByID>()
    private lateinit var favBox: Box<FavouritesModel>
    private lateinit var favQuery: Query<FavouritesModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = FragmentTvBinding.inflate(inflater, container, false)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllData(false)
        layout.tvSwipeRefresh?.setOnRefreshListener {
            getAllData(false)
            layout.tvSwipeRefresh.isRefreshing = false
        }
    }


    companion object {
        fun newInstance() = TvFragment()

    }

    private fun getAllData(isMovie:Boolean){
        dataSet.clear()
        favBox = ObjectBox.boxStore.boxFor()
        favQuery = favBox.query {
            equal(FavouritesModel_.isMovie,isMovie)
            order(FavouritesModel_.movieOrTvID)
        }
        val favArr = favQuery.find()

        for (x in favArr){
            loadSingleData(x.movieOrTvID, isMovie)
        }

        layout.rvFavouriteTV.layoutManager = GridLayoutManager(requireContext(), 2)

        val spanCount = 2 // 3 columns
        val spacing = 50 // 50px
        val includeEdge = true
        if (layout.rvFavouriteTV.itemDecorationCount > 0){
            layout.rvFavouriteTV.removeItemDecorationAt(0)
        }
        layout.rvFavouriteTV.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))

        layout.rvFavouriteTV.adapter = FavouritesRvAdapter(dataSet,isMovie,requireContext(), object : FavItemClickLister {
            override fun onClickedGrid(movieOrTvID: Int, isMovie: Boolean) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id",movieOrTvID)
                intent.putExtra("isMovie",isMovie)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
            }

            override fun onClickedDelete(movieOrTvID: Int) {
                val del = favBox.query {
                    equal(FavouritesModel_.movieOrTvID, movieOrTvID)
                }.find()

                if (del.size > 0) {
                    val mAlertDialog = AlertDialog.Builder(requireContext())
                    mAlertDialog.setTitle("Warning..!") //set alert dialog title
                    mAlertDialog.setMessage("Are you sure to delete?") //set alert dialog message
                    mAlertDialog.setPositiveButton("Yes") { dialog, id ->
                        favBox.remove(del)
                        Toast.makeText(requireContext(),  "Favourite Removed..", Toast.LENGTH_LONG).show()

                        getAllData(false)
                    }
                    mAlertDialog.setNegativeButton("No") { dialog, id ->   }
                    mAlertDialog.setNeutralButton("Cancel"){dialog, id ->  }
                    mAlertDialog.show()
                }
            }

        })

        //rvFavourite.adapter?.notifyDataSetChanged()
    }

    private fun loadSingleData(id:Int, isMovie:Boolean) {

        val call: Call<MoviesAndTvModelByID> = if (isMovie){
            ApiClient.getClient.getInfoMovie(id, Const.API_KEY)
        }
        else {
            ApiClient.getClient.getInfoTV(id, Const.API_KEY)
        }

        call.enqueue(object : Callback<MoviesAndTvModelByID> {

            override fun onResponse(call: Call<MoviesAndTvModelByID>?, response: Response<MoviesAndTvModelByID>?) {
                if (!response!!.isSuccessful || response.body() == null){
                    activity?.snackbarError("Problem with the server...")
                }
                else{
                    val x = response?.body()!!
                    dataSet.add(x)
                    layout.rvFavouriteTV.adapter?.notifyDataSetChanged()
                    layout.defaultText.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<MoviesAndTvModelByID>?, t: Throwable?) {
                activity?.snackbarError("Problem with the server...")
            }

        })
    }
}
