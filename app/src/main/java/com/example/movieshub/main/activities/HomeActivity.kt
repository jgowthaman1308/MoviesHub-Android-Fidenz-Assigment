package com.example.movieshub.main.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.viewpager.widget.ViewPager
import com.example.movieshub.R
import com.example.movieshub.databinding.ActivityHomeBinding
import com.example.movieshub.databinding.ActivitySeeMoreBinding
import com.example.movieshub.main.activities.user.EditAccountActivity
import com.example.movieshub.main.activities.user.LoginActivity
import com.example.movieshub.main.adapters.HomePagerAdapter
import com.example.movieshub.main.databases.kotpref.UserModel
import com.example.movieshub.main.fragments.FavouritesFragment
import com.example.movieshub.main.fragments.PopularFragment
import com.example.movieshub.main.fragments.SearchFragment
import com.example.movieshub.main.fragments.UpcomingFragment
import com.example.movieshub.main.helpers.Const
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity() {
    private lateinit var layout: ActivityHomeBinding

    var userNameGlo:String = "N/A"
    var emailGlo:String = "N/A"
    var imgUrl:String = ""
    var loggedByGoogle:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(layout.root)

        userNameGlo = intent.extras!!.getString("profileName").toString()
        emailGlo = intent.extras!!.getString("profileEmail").toString()
        imgUrl = intent.extras!!.getString("profileUrl").toString()
        loggedByGoogle = intent.extras!!.getBoolean("loggedByGoogle")

        if (imgUrl != ""){
            Picasso.get().load(imgUrl).fit().centerCrop().into(layout.imgProfile)
        }

        setUpViewPager()

        layout.bottomNavigationMenu.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        layout.bottomNavigationMenu.setOnNavigationItemReselectedListener {
            return@setOnNavigationItemReselectedListener
        }
        layout.imgProfile.setOnClickListener {
            profileImgClicked()
        }

        sendNotificationLocally("Hi...$userNameGlo","Welcome to MovieClub..!")

    }

    private fun setUpViewPager(){
        val adapter = HomePagerAdapter(this.supportFragmentManager)
        adapter.addFrag(PopularFragment.newInstance(), "Popular")
        adapter.addFrag(SearchFragment.newInstance(), "Search")
        adapter.addFrag(FavouritesFragment.newInstance(), "Favourites")
        adapter.addFrag(UpcomingFragment.newInstance(), "Upcoming")

        layout.homeViewPager.adapter = adapter

        layout.homeViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        layout.bottomNavigationMenu.selectedItemId = R.id.tabPopular
                    }
                    1 -> {
                        layout.bottomNavigationMenu.selectedItemId = R.id.tabSearch
                    }
                    2 -> {
                        layout.bottomNavigationMenu.selectedItemId = R.id.tabFavourites
                    }
                    3 -> {
                        layout.bottomNavigationMenu.selectedItemId = R.id.tabUpcoming
                    }
                }
            }
        })

        layout.homeViewPager.currentItem = 0

    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->


        when (item.itemId) {
            R.id.tabPopular -> {
//                toolbarTitle.text = getString(R.string.titleNotification)
                layout.homeViewPager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.tabSearch -> {
                layout.homeViewPager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.tabFavourites -> {
                layout.homeViewPager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
            R.id.tabUpcoming -> {
                layout.homeViewPager.currentItem = 3
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun profileImgClicked(){


        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.widget_profile, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        //show dialog
        val  mAlertDialog = mBuilder.show()


        mDialogView.findViewById<TextView>(R.id.proName).text = userNameGlo
        mDialogView.findViewById<TextView>(R.id.proEmail).text = emailGlo
        if (loggedByGoogle){
            mDialogView.findViewById<TextView>(R.id.proStatus).text = "Google"
            Picasso.get().load(imgUrl).fit().centerCrop().into(mDialogView.findViewById<RoundedImageView>(R.id.proImg))
            mDialogView.findViewById<Button>(R.id.proEdit).visibility = View.GONE
        }
        else{
            mDialogView.findViewById<TextView>(R.id.proStatus).text = "Local"
        }

        mDialogView.findViewById<Button>(R.id.proSignOut).setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            if (loggedByGoogle){
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    //.requestIdToken(getString(R.string.default_web_client_id))
                    .requestIdToken(Const.GOOGLE_CLIENT_ID)
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(this, gso)

                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                googleSignInClient.signOut()

                UserModel.isUserLogged = false
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
                this.finish()
            }
            else{
                UserModel.isUserLogged = false
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
                this.finish()
            }
        }
        mDialogView.findViewById<Button>(R.id.proEdit).setOnClickListener {
            mAlertDialog.dismiss()
            startActivity(Intent(this@HomeActivity, EditAccountActivity::class.java))
            overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
            this.finish()
        }
        //cancel button click of custom layout
        mDialogView.findViewById<Button>(R.id.proCancel).setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    private fun sendNotificationLocally(title: String, messageBody: String) {
//        val intent = Intent(this, DetailActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.splash_2)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            //.setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}
