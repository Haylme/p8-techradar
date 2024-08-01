package com.example.techradar.database

import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.techradar.room.dao.ListDao
import com.example.techradar.room.dto.ListDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import java.io.File

class Converters {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }
}

@Database(entities = [ListDto::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
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

        fun getDatabase(context: Context, scope: CoroutineScope): AppDataBase {
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


            val imagePath2 = "/media/picker_get_content/0/com.android.providers.media.photopicker/media/40"
            val imageUri2 = Uri.fromFile(File(imagePath2))

            listDao.insertUser(
                ListDto(
                    listName = "De Funes",
                    listFirstname = "Louis",
                    listPhone = "0750568754",
                    listEmail = "louis@grandacteur.fr",
                    listBirthday = "31/07/1914",
                    listWage = 3800.0,
                    listNote = "Je suis un très grand acteur du cinéma français.Comme l'eau sur terre, je me fais hélas rare.",
                    listFavorite = false,
                    listPicture = null
                )
            )

            listDao.insertUser(
                ListDto(
                    listName = "kitano",
                    listFirstname = "takeshi",
                    listPhone = "0424785628",
                    listEmail = "beattakeshi@doccomo.jp",
                    listBirthday = "14/07/1962",
                    listWage = 300000.0,
                    listNote = "i can speak japanese and english with more than 20 years experience as famous japanese actor. My favorite role part is playing a yakuza or a samurai",
                    listFavorite = false,
                    listPicture = imageUri2.toString()
                )
            )
        }
    }
}
