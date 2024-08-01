package com.example.techradar.room.dao

import android.net.Uri
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


    @Query("UPDATE list SET list_picture = :uri WHERE user_Id = :id")
    suspend fun updateImageUri(id: Long, uri: Uri)






    @Query("SELECT * FROM list WHERE list_favorite = true Order By user_Id DESC")
    fun getAllFavoriteUsers(): Flow<List<ListDto>>



    @Query("""
        UPDATE list
        SET 
           list_name = CASE WHEN :listName IS NOT NULL AND :listName != list_name THEN :listName ELSE list_name END,
    list_firstname = CASE WHEN :listFirstname IS NOT NULL AND :listFirstname != list_firstname THEN :listFirstname ELSE list_firstname END,
    list_phone = CASE WHEN :listPhone IS NOT NULL AND :listPhone != list_phone THEN :listPhone ELSE list_phone END,
    list_email = CASE WHEN :listEmail IS NOT NULL AND :listEmail != list_email THEN :listEmail ELSE list_email END,
    list_birthday = CASE WHEN :listBirthday IS NOT NULL AND :listBirthday != list_birthday THEN :listBirthday ELSE list_birthday END,
    list_wage = CASE WHEN :listWage IS NOT NULL AND :listWage != list_wage THEN :listWage ELSE list_wage END,
    list_note = CASE WHEN :listNote IS NOT NULL AND :listNote != list_note THEN :listNote ELSE list_note END,
    list_favorite = CASE WHEN :listFavorite IS NOT NULL AND :listFavorite != list_favorite THEN :listFavorite ELSE list_favorite END,
    list_picture = CASE WHEN :listPicture IS NOT NULL AND :listPicture != list_picture THEN :listPicture ELSE list_picture END
        WHERE user_Id = :id
    """)
    suspend fun updateUser(
        id: Long,
        listName: String?,
        listFirstname: String?,
        listPhone: String?,
        listEmail: String?,
        listBirthday: String?,
        listWage: Double?,
        listNote: String?,
        listFavorite: Boolean?,
        listPicture: String?
    ): Int


    @Query("UPDATe list SET list_favorite = :listFavorite WHERE user_Id = :id")
    suspend fun updateFavorite(id:Long, listFavorite: Boolean?)





    @Query("DELETE FROM list WHERE user_Id = :id")
    suspend fun deleteUserById(id: Long)



    @Query("SELECT * FROM list WHERE list_name LIKE '%' || :search || '%' OR list_firstname LIKE '%' || :search || '%'")
    fun searchList(search: String): Flow<List<ListDto>>

}
