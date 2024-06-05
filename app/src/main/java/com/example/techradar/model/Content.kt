package com.example.techradar.model

data class Content(
    val name: String,
    val firstname: String,
    val phone: String,
    val email: String,
    val birthday: String,
    val wage: Int,
    val note: String,
    var favorite: Boolean,
    var picture: String
)
