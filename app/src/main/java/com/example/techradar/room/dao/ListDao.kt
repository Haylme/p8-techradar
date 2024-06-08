package com.example.techradar.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.techradar.room.dto.ListDto
import kotlinx.coroutines.flow.Flow
@Dao
interface ListDao {

    @Insert
    suspend fun insertUser(user: ListDto): Long

    @Query("SELECT * FROM list WHERE user_Id = :id")
    fun getDetailUser(id: Long): Flow<ListDto>



    @Query("SELECT * FROM list Order By user_Id DESC")
    fun getAllUsers(): Flow<List<ListDto>>








    @Query("SELECT * FROM list WHERE list_favorite = true")
    fun getAllFavoriteUsers(): Flow<List<ListDto>>

    @Query("UPDATE list SET list_name = COALESCE(:listName, list_name), list_firstname = COALESCE(:listFirstname, list_firstname), list_phone = COALESCE(:listPhone, list_phone), list_email = COALESCE(:listEmail, list_email), list_birthday = COALESCE(:listBirthday, list_birthday), list_wage = COALESCE(:listWage, list_wage), list_note = COALESCE(:listNote, list_note), list_favorite = COALESCE(:listFavorite, list_favorite), list_picture = COALESCE(:listPicture, list_picture) WHERE user_Id = :id")
    suspend fun updateUser(
        id: Long,
        listName: String?,
        listFirstname: String?,
        listPhone: String?,
        listEmail: String?,
        listBirthday: String?,
        listWage: Int?,
        listNote: String?,
        listFavorite: Boolean?,
        listPicture: String?
    ):Int

    @Query("DELETE FROM list WHERE user_Id = :id")
    suspend fun deleteUserById(id: Long)
}
