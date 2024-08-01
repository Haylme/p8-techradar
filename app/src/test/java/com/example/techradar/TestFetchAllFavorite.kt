package com.example.techradar

import com.example.techradar.data.DataRepository
import com.example.techradar.model.Content
import com.example.techradar.room.dao.ListDao
import com.example.techradar.room.dto.ListDto
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify


@RunWith(JUnit4::class)
class TestFetchAllFavorite {


    @Mock
    private lateinit var listDao: ListDao
    private lateinit var dataRepository: DataRepository



    @Before
    fun setup() {


        MockitoAnnotations.openMocks(this)
        dataRepository = DataRepository(listDao)

    }

    @Test
    fun `should retrieve all favorite user`(): Unit = runBlocking {

        val fakeUserDto = ListDto(
            id = 1,
            listName = "jeannot",
            listFirstname = "lapin",
            listPhone = "044324785",
            listEmail = "jeannot@exemple.fr",
            listBirthday = "14/12/2023",
            listWage = 45f,
            listNote = "to be or not to be that is the question",
            listFavorite = true,
            listPicture = null


        )

        val fakeUserContent = Content (

            id = 1,
            name = "jeannot",
            firstname = "lapin",
            phone = "044324785",
            email = "jeannot@exemple.fr",
            birthday = "14/12/2023",
            wage = 45f,
            note = "to be or not to be that is the question",
            favorite = true,
            picture = null



        )

        `when`(listDao.getAllFavoriteUsers()).thenReturn(flowOf(listOf(fakeUserDto)))
        val result = dataRepository.fetchAllFavorites()

        assert(result.isNotEmpty()).also { println("result : ${result[0]}") }
        assert(result[0] == fakeUserContent)
        verify(listDao).getAllFavoriteUsers()

    }


}