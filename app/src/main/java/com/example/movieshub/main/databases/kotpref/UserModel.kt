package com.example.movieshub.main.databases.kotpref

import com.chibatching.kotpref.KotprefModel

object UserModel : KotprefModel() {
    var userName: String by stringPref()
    var email: String by stringPref()
    var dateOfBirth: String by stringPref()
    var password: String by stringPref()
    var isUserLogged by booleanPref(false)
}