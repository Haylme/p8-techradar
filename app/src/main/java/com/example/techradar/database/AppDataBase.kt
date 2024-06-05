package com.example.techradar.database

import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.techradar.room.dao.ListDao
import com.example.techradar.room.dto.ListDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File


@Database(entities = [ListDto::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {


    abstract fun listDao(): ListDao




    private class DataBaseCallback(private val scope: CoroutineScope) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.listDao())
                }
            }
        }




    }

    companion object {

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context, scope: CoroutineScope): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "Techradar"
                )
                    .addCallback(DataBaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance


            }


        }

        suspend fun populateDatabase(listDao: ListDao) {
            val imagePath = "/storage/emulated/0/cours_openclassrooms/projet_8/pictures/louis.jpg"
            val imageUri = Uri.fromFile(File(imagePath))

            val imagePath2 = "/storage/emulated/0/cours_openclassrooms/projet_8/pictures/kitano.jpg"
            val imageUri2 = Uri.fromFile(File(imagePath2))

            listDao.insertUser(

                ListDto(

                    listName = "De Funes", listFirstname = "Louis", listPhone = "0750568754", listEmail = "louis@grandacteur.fr", listBirthday = "31/07/1914", listWage = 3800, listNote = "Je suis un très grand acteur du cinéma français.Comme l'eau sur terre, je me fais hélas rare.",
                    listFavorite = null, listPicture = imageUri.toString()




                )


            )

            listDao.insertUser(

                ListDto(
                    listName = "kitano",
                    listFirstname = "takeshi",
                    listPhone = "0424785628",
                    listEmail = "beattakeshi@doccomo.jp",
                    listBirthday = "14/07/1962",
                    listWage = 300000,
                    listNote = "i can speak japanese and english with more than 20 years experience as famous japanese actor. My favorite role part is playing a yakuza or a samurai",
                    listFavorite = null,
                    listPicture = imageUri2.toString()



                )







            )


        }
    }





    }