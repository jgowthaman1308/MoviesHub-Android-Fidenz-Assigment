package com.example.movieshub.main.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chibatching.kotpref.Kotpref
import com.example.movieshub.R
import com.example.movieshub.main.activities.user.LoginActivity
import com.example.movieshub.main.databases.kotpref.UserModel
import com.example.movieshub.main.databases.object_box.ObjectBox
import com.google.android.gms.auth.api.signin.GoogleSignIn
import java.lang.NumberFormatException

class SplashActivity : AppCompatActivity() {

    var isMovie: Boolean = false
    var id: Int = 0
    var callFromDeepLink: Boolean = false
    var action: String? = null
    var uriData: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectBox.init(this)
        Kotpref.init(this)
        deepLinkNavigation()

    }

    private fun deepLinkNavigation(){
        if (intent?.data != null){
            callFromDeepLink = true

            action = intent?.action //intent from deep link
            uriData = intent?.data //intent from deep link

            if (uriData != null){
                val pathSeg = uriData!!.pathSegments
                val mediaType = pathSeg[0]
                isMovie = mediaType != "tv"

                if (pathSeg.size < 3){
                    makeSplashAndNavigate()
                }
                else{
                    val idPath = pathSeg[1]
                    id = getID(idPath.toString())

                    val intent = Intent(this@SplashActivity, DetailActivity::class.java)
                    intent.putExtra("isMovie", isMovie)
                    intent.putExtra("id",id)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
                    this.finish()
                }

            }
        }
        else{
            makeSplashAndNavigate()
        }
    }

    private fun getID(str: String): Int {
        var output = 0
        var i = 1
        while (i <= str.length){
            val tempStr = str.take(i)
            try {
                tempStr.toInt()
            }
            catch (x: NumberFormatException){
                output = str.take(i-1).toInt()
                break
            }
            i++
        }
        return output
    }

    private fun makeSplashAndNavigate(){

            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null){
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                intent.putExtra("profileName",account.givenName.toString())
                intent.putExtra("profileEmail",account.email.toString())
                intent.putExtra("profileUrl",account.photoUrl.toString())
                intent.putExtra("loggedByGoogle",true)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
                UserModel.isUserLogged = true
                this.finish()
            }
            else if (UserModel.isUserLogged){
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                intent.putExtra("profileName",UserModel.userName)
                intent.putExtra("profileEmail",UserModel.email)
                intent.putExtra("profileUrl","")
                intent.putExtra("loggedByGoogle",false)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
                UserModel.isUserLogged = true
                this.finish()
            }
            else{
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
                finish()
            }
    }
}
