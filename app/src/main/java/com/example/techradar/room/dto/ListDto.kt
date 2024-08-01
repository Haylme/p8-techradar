package com.example.techradar.room.dto

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "list")
data class ListDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_Id")
    var id: Long = 0,



    @ColumnInfo(name = "list_name")
    var listName: String,

    @ColumnInfo(name = "list_firstname")
    var listFirstname: String,

    @ColumnInfo(name = "list_phone")
    var listPhone: String,

    @ColumnInfo(name = "list_email")
    var listEmail: String,

    @ColumnInfo(name = "list_birthday")
    var listBirthday: String,

    @ColumnInfo(name = "list_wage")
    var listWage: Double,

    @ColumnInfo(name = "list_note")
    var listNote: String,

    @ColumnInfo(name = "list_favorite")
    var listFavorite: Boolean = false,

    @ColumnInfo(name = "list_picture")
    var listPicture: String?


)



