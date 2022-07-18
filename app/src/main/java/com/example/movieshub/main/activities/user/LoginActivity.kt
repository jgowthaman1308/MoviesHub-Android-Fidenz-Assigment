package com.example.movieshub.main.activities.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotpref.Kotpref
import com.example.movieshub.R
import com.example.movieshub.databinding.ActivityLoginBinding
import com.example.movieshub.databinding.ActivitySeeMoreBinding
import com.example.movieshub.main.activities.HomeActivity
import com.example.movieshub.main.databases.kotpref.UserModel
import com.example.movieshub.main.helpers.CommonFun
import com.example.movieshub.main.helpers.Const
import com.example.movieshub.main.helpers.snackbarError
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class LoginActivity : AppCompatActivity() {
    private lateinit var layout: ActivityLoginBinding

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(layout.root)

        Kotpref.init(this)

        layout.loginButton.setOnClickListener { login() }

        initGoogleLogin()

        layout.tvCreateAcc.setOnClickListener {
            startActivity(Intent(this@LoginActivity, AddAccountActivity::class.java))
            overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
        }

        CommonFun().initBackBtn(findViewById(R.id.btnBack),this)
    }

    override fun onStart() {
        super.onStart()

        //val account = GoogleSignIn.getLastSignedInAccount(this)
        //signOut()

    }

    private fun initGoogleLogin(){
        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.default_web_client_id))
            .requestIdToken(Const.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        layout.btnGoogleLogin.setOnClickListener {
            googleLogin()
        }
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            try {
                // Google Sign In was successful, authenticate with Firebase

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)


                handleSignInResult(task) // Signed in successfully, show authenticated UI.
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this@LoginActivity,"Google sign in failed...",Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
                //updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
            // Signed in successfully, show authenticated UI.
            Toast.makeText(this@LoginActivity,"Signed in by Google...",Toast.LENGTH_SHORT).show()

            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.putExtra("profileName",account.givenName.toString())
            intent.putExtra("profileEmail",account.email.toString())
            intent.putExtra("profileUrl",account.photoUrl.toString())
            intent.putExtra("loggedByGoogle",true)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_bottomlayer_display, R.anim.keep_active)
            this.finish()
        }
        catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //updateUI(null)
            Toast.makeText(this@LoginActivity,"Failed to login...\n$e",Toast.LENGTH_SHORT).show()
        }
    }

    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            //updateUI(null)
        }
    }

    private fun login(){
        val username = layout.usernameEditText.text.toString()
        val password = layout.passwordEditText.text.toString()

        if ((username == UserModel.userName) && (password == UserModel.password)){

            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
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
            Toast.makeText(this,"Invalid Login...",Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}





//package com.example.movieshub.main.activities.user
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import com.example.movieshub.R
//import com.example.movieshub.main.activities.HomeActivity
//import com.example.movieshub.main.helpers.Const
//import com.google.android.gms.auth.api.Auth
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.SignInButton
//import com.google.android.gms.common.api.GoogleApiClient
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import kotlinx.android.synthetic.main.activity_login.*
//
//class LoginActivity : AppCompatActivity() {
//
//    val TAG = "CreateAccount"
//    lateinit var googleSignInButton: SignInButton//Init views
//    val GOOGLE_LOG_IN_RC = 1//Request codes
//    var googleApiClient: GoogleApiClient? = null// Google API Client object.
//    var firebaseAuth: FirebaseAuth? = null // Firebase Auth Object.
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//        initGoogle()
//    }
//
//    fun initGoogle(){
//        googleSignInButton = btnGoogleLogin
//        firebaseAuth = FirebaseAuth.getInstance()
//
//        // Configure Google Sign In
//        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(Const.GOOGLE_CLIENT_ID)
//            .requestEmail()
//            .build()
//
//        // Creating and Configuring Google Api Client.
//        googleApiClient = GoogleApiClient.Builder(this@LoginActivity)
//            .enableAutoManage(this@LoginActivity  /* OnConnectionFailedListener */) { }
//            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
//            .build()
//
//        googleSignInButton.setOnClickListener {
//            googleLogin()
//        }
//    }
//
//    private fun googleLogin() {
//        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
//        startActivityForResult(signInIntent, GOOGLE_LOG_IN_RC)
//    }
//
//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == GOOGLE_LOG_IN_RC) {
//            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//            Log.i(TAG, "With Google LogIn, is result a success? ${result.isSuccess}.")
//            if (result.isSuccess) {
//                // Google Sign In was successful, authenticate with Firebase
//                firebaseAuthWithGoogle(result.signInAccount!!)
//            } else {
//                Toast.makeText(this@LoginActivity, "Some error occurred.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//        Log.i(TAG, "Authenticating user with firebase.")
//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//        firebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
//            Log.i(TAG, "Firebase Authentication, is result a success? ${task.isSuccessful}.")
//            if (task.isSuccessful) {
//                // Sign in success, update UI with the signed-in user's information
//                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
//            } else {
//                // If sign in fails, display a message to the user.
//                Toast.makeText(this@LoginActivity, "Authenticating with Google credentials in firebase FAILED !!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}

