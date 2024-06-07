package com.example.techradar.data
import com.example.techradar.model.Content
import com.example.techradar.room.dao.ListDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


class DataRepository(private val listDao: ListDao) {


    private val _userError = MutableStateFlow<String?>(null)
    private val error: StateFlow<String?> = _userError


    suspend fun addUser(content: Content) {

        try {
            listDao.insertUser(content.toDto())
        } catch (e: Exception) {
            _userError.value = "Something went wrong, please try again"
        }

    }


    suspend fun fetchDetailUser(id: Long): Content? {
        return try {
            val user = listDao.getDetailUser(id)
                .map { listDto -> Content.fromDto(listDto) }
                .firstOrNull()

            if (user == null) {
                _userError.value = "User not found, please try again"
            }
            user
        } catch (e: Exception) {
            _userError.value = "Something went wrong, please try again"
            null
        }
    }

    suspend fun fetchAllUsers(): List<Content>? {
        return try {

            val user = listDao.getAllUsers()
                .first()
                .map { Content.fromDto(it) }

            if (user.isEmpty()) {
                _userError.value = "No users found, please try again"

            }
                user


            } catch (e: Exception) {
                _userError.value = "Something went wrong, please)"
                null
            }


        }

    }



