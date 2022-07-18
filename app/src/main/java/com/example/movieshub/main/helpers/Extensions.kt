package com.example.movieshub.main.helpers

import android.app.Activity
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.andrognito.flashbar.Flashbar
import com.example.movieshub.R

fun Activity.snackbarError(message: String){
    Flashbar.Builder(this)
        .gravity(Flashbar.Gravity.TOP)
        .title("Something is wrong")
        .message(message)
        .backgroundColor(Color.RED)
        .duration(Flashbar.DURATION_LONG)
        .build()
        .show()
}

fun Activity.snackbarSuccess(message: String){
    Flashbar.Builder(this)
        .gravity(Flashbar.Gravity.TOP)
        .title("Hooray")
        .message(message)
        .backgroundColor(Color.GREEN)
        .duration(Flashbar.DURATION_LONG)
        .build()
        .show()
}