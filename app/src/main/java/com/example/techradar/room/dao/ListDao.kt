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


   // @Query("UPDATE list SET list_name = COALESCE(:listName, list_name), list_firstname = COALESCE(:listFirstname, list_firstname), list_phone = COALESCE(:listPhone, list_phone), list_email = COALESCE(:listEmail, list_email), list_birthday = COALESCE(:listBirthday, list_birthday), list_wage = COALESCE(:listWage, list_wage), list_note = COALESCE(:listNote, list_note), list_favorite = COALESCE(:listFavorite, list_favorite), list_picture = COALESCE(:listPicture, list_picture) WHERE user_Id = :id")

    @Query("""
        UPDATE list
        SET 
            list_name = CASE WHEN :listName IS NOT NULL THEN :listName ELSE list_name END,
            list_firstname = CASE WHEN :listFirstname IS NOT NULL THEN :listFirstname ELSE list_firstname END,
            list_phone = CASE WHEN :listPhone IS NOT NULL THEN :listPhone ELSE list_phone END,
            list_email = CASE WHEN :listEmail IS NOT NULL THEN :listEmail ELSE list_email END,
            list_birthday = CASE WHEN :listBirthday IS NOT NULL THEN :listBirthday ELSE list_birthday END,
            list_wage = CASE WHEN :listWage IS NOT NULL THEN :listWage ELSE list_wage END,
            list_note = CASE WHEN :listNote IS NOT NULL THEN :listNote ELSE list_note END,
            list_favorite = CASE WHEN :listFavorite IS NOT NULL THEN :listFavorite ELSE list_favorite END,
            list_picture = CASE WHEN :listPicture IS NOT NULL THEN :listPicture ELSE list_picture END
        WHERE user_Id = :id
    """)
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
    ): Int


    @Query("UPDATe list SET list_favorite = :listFavorite WHERE user_Id = :id")
    suspend fun updateFavorite(id:Long, listFavorite: Boolean?)





    @Query("DELETE FROM list WHERE user_Id = :id")
    suspend fun deleteUserById(id: Long)



    @Query("SELECT * FROM list WHERE list_name LIKE '%' || :search || '%' OR list_firstname LIKE '%' || :search || '%'")
    fun searchList(search: String): Flow<List<ListDto>>

}
