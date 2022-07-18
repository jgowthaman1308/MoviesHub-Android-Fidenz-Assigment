package com.example.movieshub.main.activities.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.movieshub.R
import com.example.movieshub.databinding.ActivityEditAccountBinding
import com.example.movieshub.databinding.ActivityLoginBinding
import com.example.movieshub.main.databases.kotpref.UserModel
import com.example.movieshub.main.helpers.CommonFun

class EditAccountActivity : AppCompatActivity() {
    private lateinit var layout: ActivityEditAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(layout.root)

        fillFields()
        CommonFun().initDatePicker(layout.etDateOfBirthU,this)
        layout.btnUpdate.setOnClickListener {
            createAccount()
        }
        CommonFun().initBackBtn(findViewById(R.id.btnBack),this)
    }

    private fun fillFields(){
        layout.etNameU.setText(UserModel.userName)
        layout.etEmailU.setText(UserModel.email)
        layout.etDateOfBirthU.setText(UserModel.dateOfBirth)
        layout.etPasswordU.setText(UserModel.password)
    }

    private fun createAccount(){
        val userName = layout.etNameU.text.toString()
        val password = layout.etPasswordU.text.toString()
        val email = layout.etEmailU.text.toString()
        val dob = layout.etDateOfBirthU.text.toString()

        if (userName == ""){
            Toast.makeText(this,"Name field is empty...", Toast.LENGTH_SHORT).show()
        }
        else if(password.length < 5){
            Toast.makeText(this,"Password should be at least 5 characters...", Toast.LENGTH_SHORT).show()
        }
        else if(!CommonFun().isEmailValid(email)){
            Toast.makeText(this,"Your Email is NOT valid...", Toast.LENGTH_SHORT).show()
        }
        else if(dob == ""){
            Toast.makeText(this,"Date of Birth field is empty...", Toast.LENGTH_SHORT).show()
        }
        else{
            UserModel.userName = userName
            UserModel.password = password
            UserModel.dateOfBirth = dob
            UserModel.email = email
            Toast.makeText(this,"Account Updated Successfully...", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this@EditAccountActivity, LoginActivity::class.java))
            this.finish()
        }

    }
}
