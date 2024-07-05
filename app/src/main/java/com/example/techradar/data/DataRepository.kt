package com.example.techradar.data

import Curencies
import android.net.Uri
import com.example.techradar.model.Content
import com.example.techradar.retrofit.CallApi
import com.example.techradar.room.dao.ListDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


class DataRepository(
    private val listDao: ListDao,


    ) {


    suspend fun addUser(content: Content): Boolean {
        val result = listDao.insertUser(content.toDto())
        return result != -1L // Indicate success if the insertion was successful
    }





    suspend fun fetchDetailUser(id: Long): Content? {

        val user = listDao.getDetailUser(id)
            .map { listDto -> Content.fromDto(listDto) }
            .firstOrNull()
        return user


    }

    suspend fun fetchAllUsers(): List<Content> {


        val user = listDao.getAllUsers()
            .first()
            .map { Content.fromDto(it) }

        return user


    }


    suspend fun fetchAllFavorites(): List<Content> {


        val user = listDao.getAllFavoriteUsers()
            .first()
            .map { Content.fromDto(it) }


        return user


    }


    suspend fun editUser(content: Content,id:Long): Boolean {


        val rowUpdate = listDao.updateUser(
            id = content.id,
            listName = content.name,
            listFirstname = content.firstname,
            listPhone = content.phone,
            listEmail = content.email,
            listBirthday = content.birthday,
            listWage = content.wage,
            listNote = content.note,
            listFavorite = content.favorite,
            listPicture = content.picture.toString()

        )
        return rowUpdate > 0

    }


    suspend fun updateFav(id: Long, bool: Boolean?) {
        listDao.updateFavorite(
            id = id,
            listFavorite = bool
        )
    }


    suspend fun suppressUser(id: Long) {

        listDao.deleteUserById(id)


    }


    suspend fun searchFun(search: String):List<Content> {

       val result = listDao.searchList(search)
           .first()
           .map { Content.fromDto(it) }

        return result
    }

    suspend fun fetchTranslate(date:String?, to:Int?): Curencies {
        return CallApi.fetchTranslateData(date,to)
    }




}








