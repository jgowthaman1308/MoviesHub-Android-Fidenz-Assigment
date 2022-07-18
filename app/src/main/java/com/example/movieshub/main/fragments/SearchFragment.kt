package com.example.movieshub.main.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.movieshub.R
import com.example.movieshub.databinding.FragmentPopularBinding
import com.example.movieshub.databinding.FragmentSearchBinding
import com.example.movieshub.main.activities.DetailActivity
import com.example.movieshub.main.adapters.SearchResultRvAdapter
import com.example.movieshub.main.helpers.Const
import com.example.movieshub.main.helpers.snackbarError
import com.example.movieshub.main.interfaces.RecyclerViewItemClickListener
import com.example.movieshub.main.models.MoviesAndTvModelBulk
import com.example.movieshub.main.models.Results
import com.example.movieshub.main.services.retrofit_client.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {
    private lateinit var layout: FragmentSearchBinding

    var totalResponse = ArrayList<Results>()
    var responseResultsMovie = ArrayList<Results>()
    var responseResultsTV = ArrayList<Results>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = FragmentSearchBinding.inflate(inflater, container, false)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout.svSearchBar.setOnClickListener {
            layout.svSearchBar.isIconified = false
        }

        layout.svSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                layout.searchAlert.text = ""
                if (newText != "") {
                    totalResponse.clear()
                    getDataFromSearch(newText.toString(),true)

                }

                return false
            }

        })
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if (isVisibleToUser){
//            svSearchBar.performClick()
//        }
//    }

    private fun loadResults(){

        if (totalResponse.size == 0){
            layout.searchAlert.text = "Results Not Found.."
        }

        layout.rvSearch.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        layout.rvSearch.adapter = SearchResultRvAdapter(totalResponse,requireContext(), object : RecyclerViewItemClickListener {
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
//                val intent = Intent(requireContext(),SeeMoreActivity::class.java)
//                intent.putExtra("id",id)
//                intent.putExtra("isMovie",isMovie)
//                startActivity(intent)
                //overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
            }
        })
    }

    private fun getDataFromSearch(searchKey:String, isMovie:Boolean) {
        val call: Call<MoviesAndTvModelBulk> = if (isMovie){
            ApiClient.getClient.getInfoSearchMovie(searchKey, Const.API_KEY)
        }
        else {
            ApiClient.getClient.getInfoSearchTV(searchKey, Const.API_KEY)
        }

        call.enqueue(object : Callback<MoviesAndTvModelBulk> {

            override fun onResponse(call: Call<MoviesAndTvModelBulk>?, response: Response<MoviesAndTvModelBulk>?) {
                if (!response!!.isSuccessful || response.body() == null){
                    activity?.snackbarError("Problem with the server...")
                }
                else{
                    val x = response?.body()!!
                    layout.welcomeText.visibility = View.GONE
                    if(isMovie){
                        for (item in x.results){
                            totalResponse.add(item)

                        }
                        getDataFromSearch(searchKey,false)
                        loadResults()
                        layout.rvSearch.adapter?.notifyDataSetChanged()
                    }
                    else{
                        for (item in x.results){
                            totalResponse.add(item)
                        }
                        layout.rvSearch.adapter?.notifyDataSetChanged()
                    }
                }



                //popularRecyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<MoviesAndTvModelBulk>?, t: Throwable?) {
                activity?.snackbarError("Problem with the server...")
            }

        })
    }



    companion object {
        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SearchFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }

        fun newInstance(): SearchFragment =
            SearchFragment()
    }
}
