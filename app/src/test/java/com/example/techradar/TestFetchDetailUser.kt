package com.example.techradar

import com.example.techradar.data.DataRepository
import com.example.techradar.model.Content
import com.example.techradar.room.dao.ListDao
import com.example.techradar.room.dto.ListDto
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)

class TestFetchDetailUser {

    @Mock
    private lateinit var listDao: ListDao

    private lateinit var dataRepository: DataRepository




    @Before
    fun setup() {


        MockitoAnnotations.openMocks(this)
        dataRepository = DataRepository(listDao)

    }

    @Test
    fun `should fetch all detail of user`(): Unit = runBlocking {

        val fakeUserDto = ListDto(
            id = 1,
            listName = "jeannot",
            listFirstname = "lapin",
            listPhone = "044324785",
            listEmail = "jeannot@exemple.fr",
            listBirthday = "14/12/2023",
            listWage = 45.0,
            listNote = "to be or not to be that is the question",
            listFavorite = true,
            listPicture = null


        )




        `when`(listDao.getDetailUser(1)).thenReturn(flowOf(fakeUserDto))

        val result = dataRepository.fetchDetailUser(1)

        assert(result != null)

        result?.let { user ->
            assertEquals(user.id, 1)
            assertEquals(user.name, "jeannot")
            assertEquals(user.firstname, "lapin")
            assertEquals(user.phone, "044324785")
            assertEquals(user.email, "jeannot@exemple.fr")
            assertEquals(user.birthday, "14/12/2023")
            assertEquals(user.wage, 45)
            assertEquals(user.note, "to be or not to be that is the question")
            assertEquals(user.favorite, true)
            assertEquals(user.picture, null)
        }



    }


}