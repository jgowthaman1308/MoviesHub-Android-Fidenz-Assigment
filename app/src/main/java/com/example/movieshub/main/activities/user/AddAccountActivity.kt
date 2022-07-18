package com.example.movieshub.main.activities.user

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chibatching.kotpref.Kotpref
import com.example.movieshub.R
import com.example.movieshub.databinding.ActivityAddAccountBinding
import com.example.movieshub.databinding.ActivityEditAccountBinding
import com.example.movieshub.main.databases.kotpref.UserModel
import com.example.movieshub.main.helpers.CommonFun
import java.util.*


class AddAccountActivity : AppCompatActivity() {
    private lateinit var layout: ActivityAddAccountBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityAddAccountBinding.inflate(layoutInflater)
        setContentView(layout.root)

        Kotpref.init(this)
        CommonFun().initDatePicker(layout.etDateOfBirth,this)
        layout.btnSubmit.setOnClickListener {
            createAccount()
        }
        CommonFun().initBackBtn(findViewById(R.id.btnBack),this)
    }

    private fun createAccount(){
        val userName = layout.etName.text.toString()
        val password = layout.etPassword.text.toString()
        val email = layout.etEmail.text.toString()
        val dob = layout.etDateOfBirth.text.toString()

        if (userName == ""){
            Toast.makeText(this,"Name field is empty...",Toast.LENGTH_SHORT).show()
        }
        else if(password.length < 5){
            Toast.makeText(this,"Password should be at least 5 characters...",Toast.LENGTH_SHORT).show()
        }
        else if(!CommonFun().isEmailValid(email)){
            Toast.makeText(this,"Your Email is NOT valid...",Toast.LENGTH_SHORT).show()
        }
        else if(dob == ""){
            Toast.makeText(this,"Date of Birth field is empty...",Toast.LENGTH_SHORT).show()
        }
        else{
            UserModel.userName = userName
            UserModel.password = password
            UserModel.dateOfBirth = dob
            UserModel.email = email
            Toast.makeText(this,"Account Created Successfully...",Toast.LENGTH_SHORT).show()

            startActivity(Intent(this@AddAccountActivity, LoginActivity::class.java))
            this.finish()
        }

    }
}
