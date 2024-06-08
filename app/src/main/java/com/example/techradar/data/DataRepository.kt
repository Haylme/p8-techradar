package com.example.techradar.data

import com.example.techradar.model.Content
import com.example.techradar.room.dao.ListDao
import com.example.techradar.room.dto.ListDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


class DataRepository(private val listDao: ListDao) {


    suspend fun addUser(content: Content): Content {
        listDao.insertUser(content.toDto())
        return content // Return the added content
    }


    suspend fun fetchDetailUser(id: Long): Content? {

        val user = listDao.getDetailUser(id)
            .map { listDto -> Content.fromDto(listDto) }
            .firstOrNull()
        return user


    }

    suspend fun fetchAllUsers(): List<Content>? {


        val user = listDao.getAllUsers()
            .first()
            .map { Content.fromDto(it) }

        return user


    }


    suspend fun fetchAllFavorites(): List<Content>? {


        val user = listDao.getAllFavoriteUsers()
            .first()
            .map { Content.fromDto(it) }


        return user


    }


    suspend fun editUser(content: Content): Boolean {


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
            listPicture = content.picture

        )
        return rowUpdate > 0

    }

    suspend fun suppressUser(id: Long) {

        listDao.deleteUserById(id)


    }


}








