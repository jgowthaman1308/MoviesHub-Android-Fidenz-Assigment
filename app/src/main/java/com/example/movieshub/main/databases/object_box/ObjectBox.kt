package com.example.movieshub.main.databases.object_box

import android.content.Context
import com.example.movieshub.main.models.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.exception.DbException

object ObjectBox {
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        try {
            boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
        }
        catch (err: DbException){
            boxStore.close()
            boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
        }

    }
}