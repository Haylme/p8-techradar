package com.example.techradar.module

import android.content.Context
import androidx.room.Database
import com.example.techradar.data.DataRepository
import com.example.techradar.database.AppDataBase
import com.example.techradar.room.dao.ListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)



    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context, coroutineScope: CoroutineScope): AppDataBase {
        return Database.getDatabase(context, coroutineScope)
    }


    @Provides
    fun provideUserDao(database: AppDataBase): ListDao {
        return database.listDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(listDao: ListDao): DataRepository {
        return DataRepository()(listDao)
    }

}