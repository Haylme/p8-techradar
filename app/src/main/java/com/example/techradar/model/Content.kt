package com.example.techradar.model

import android.net.Uri
import com.example.techradar.room.dto.ListDto

data class Content(
    val id: Long = 0,
    val name: String,
    val firstname: String,
    val phone: String,
    val email: String,
    val birthday: String,
    val wage: Int,
    val note: String,
    var favorite: Boolean,
    var picture: String?
) {


    companion object {


        fun fromDto(dto: ListDto): Content {

            return Content(

                id = dto.id,
                name = dto.listName,
                firstname = dto.listFirstname,
                phone = dto.listPhone,
                email = dto.listEmail,
                birthday = dto.listBirthday,
                wage = dto.listWage,
                note = dto.listNote,
                favorite = dto.listFavorite,
                picture = dto.listPicture
            )


        }
    }


    fun toDto(): ListDto {
        return ListDto(
            id = this.id,
            listName = this.name,
            listFirstname = this.firstname,
            listPhone = this.phone,
            listEmail = this.email,
            listBirthday = this.birthday,
            listWage = this.wage,
            listNote = this.note,
            listFavorite = this.favorite,
            listPicture = this.picture


        )
    }

}













