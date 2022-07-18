package com.example.movieshub.main.helpers

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import com.example.movieshub.R
import java.util.*

class CommonFun {
    public fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    public fun initDatePicker(editTextName: EditText,context: Context){
        editTextName.inputType = InputType.TYPE_NULL
        editTextName.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)
            // date picker dialog
            val picker = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    editTextName.setText(
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    )
                }, year, month, day
            )
            picker.show()
        }
    }


    fun initBackBtn(btnBack: RelativeLayout, activity: Activity){
        btnBack.setOnClickListener {
            activity.onBackPressed()
        }
    }
}