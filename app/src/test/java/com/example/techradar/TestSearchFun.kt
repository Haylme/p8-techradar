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

@RunWith(JUnit4::class)

class TestSearchFun {


    @Mock
    private lateinit var listDao:ListDao
    private lateinit var dataRepository:DataRepository

    @Before
    fun setup(){

        MockitoAnnotations.openMocks(this)
        dataRepository = DataRepository(listDao)


    }

    @Test
    fun `should find the right item`(): Unit = runBlocking{

        val fakeUserDto = ListDto(
            id = 1,
            listName = "john",
            listFirstname = "kennedy",
            listPhone = "0614238745",
            listEmail = "john@exemple.fr",
            listBirthday = "14/05/1970",
            listWage = 3852,
            listNote = "j'aime les sport de balles",
            listFavorite = false,
            listPicture = null
        )
        val fakeUser = Content(
            id = 1,
            name = "john",
            firstname = "kennedy",
            phone = "0614238745",
            email = "john@exemple.fr",
            birthday = "14/05/1970",
            wage = 3852,
            note = "j'aime les sport de balles",
            favorite = false,
            picture = null
        )

        `when`(listDao.searchList("john")).thenReturn(flowOf(listOf(fakeUserDto)))

        val result = dataRepository.searchFun("john")

        assert(result.isNotEmpty())


    }





}