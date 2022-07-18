package com.example.movieshub.main.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

import com.example.movieshub.R
import com.example.movieshub.databinding.FragmentSearchBinding
import com.example.movieshub.databinding.FragmentUpcomingBinding
import com.example.movieshub.main.activities.DetailActivity
import com.example.movieshub.main.adapters.UpcomingSlideShowAdapter
import com.example.movieshub.main.helpers.Const
import com.example.movieshub.main.helpers.snackbarError
import com.example.movieshub.main.interfaces.SeeMoreClickListener
import com.example.movieshub.main.models.MoviesAndTvModelBulk
import com.example.movieshub.main.models.Results
import com.example.movieshub.main.services.retrofit_client.ApiClient
import com.viewpagerindicator.CirclePageIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class UpcomingFragment : Fragment() {
    private lateinit var layout: FragmentUpcomingBinding

    private var dataSet = ArrayList<Results>()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = FragmentUpcomingBinding.inflate(inflater, container, false)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout.upcomingSwipeRefresh.isRefreshing = true
        getDataUpcomingMovie()
        init()
        layout.upcomingSwipeRefresh.isRefreshing = false

        layout.upcomingSwipeRefresh?.setOnRefreshListener {
            dataSet.clear()
            getDataUpcomingMovie()
            init()
            layout.upcomingSwipeRefresh.isRefreshing = false

        }
    }

    companion object {
        fun newInstance(): UpcomingFragment =
            UpcomingFragment()

        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
    }

    private fun init() {

        mPager = layout.vpSlideShow as ViewPager
        mPager!!.adapter = UpcomingSlideShowAdapter(requireContext(),dataSet, object : SeeMoreClickListener {
            override fun onClickedFrame(id: Int, isMovie: Boolean) {
                //Toast.makeText(this@SeeMoreActivity,id.toString()+"\n"+isMovie.toString(), Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id",id)
                intent.putExtra("isMovie",isMovie)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
            }
        })

        val indicator = layout.indicator as CirclePageIndicator
        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.radius = 5 * density

        NUM_PAGES = dataSet.size

        // Auto start of viewpager
        val handler = Handler()
        val update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.adapter?.notifyDataSetChanged()
            mPager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position


            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })



    }

    private fun getDataUpcomingMovie() {
        val call: Call<MoviesAndTvModelBulk> = ApiClient.getClient.getInfoUpcomingMovie(Const.API_KEY)

        call.enqueue(object : Callback<MoviesAndTvModelBulk> {

            override fun onResponse(call: Call<MoviesAndTvModelBulk>?, response: Response<MoviesAndTvModelBulk>?) {
                if (!response!!.isSuccessful || response.body() == null){
                    activity?.snackbarError("Problem with the server...")
                }
                else{
                    val x = response?.body()!!

                    dataSet.clear()
                    for (item in x.results){
                        dataSet.add(item)
                    }
                }
            }

            override fun onFailure(call: Call<MoviesAndTvModelBulk>?, t: Throwable?) {
                activity?.snackbarError("Problem with the server...")
            }

        })
    }

}
