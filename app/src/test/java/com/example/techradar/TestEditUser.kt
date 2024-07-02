package com.example.techradar

import com.example.techradar.data.DataRepository
import com.example.techradar.model.Content
import com.example.techradar.room.dao.ListDao
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)

class TestEditUser {


    @Mock
    private lateinit var listDao: ListDao
    private lateinit var dataRepository: DataRepository


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        dataRepository = DataRepository(listDao)
    }





    @Test
    fun `should edit user's data`(): Unit = runBlocking {
        val fakeUserContent = Content(
            id = 1,
            name = "jeannot",
            firstname = "lapin",
            phone = "044324785",
            email = "jeannot@exemple.fr",
            birthday = "14/12/2023",
            wage = 4500000,
            note = "to be or not to be that is the question",
            favorite = true,
            picture = null
        )


        `when`(listDao.updateUser(
            id = fakeUserContent.id,
            listName = fakeUserContent.name,
            listFirstname = fakeUserContent.firstname,
            listPhone = fakeUserContent.phone,
            listEmail = fakeUserContent.email,
            listBirthday = fakeUserContent.birthday,
            listWage = fakeUserContent.wage,
            listNote = fakeUserContent.note,
            listFavorite = fakeUserContent.favorite,
            listPicture = fakeUserContent.picture.toString()
        )).thenReturn(1)


        val result = dataRepository.editUser(fakeUserContent, fakeUserContent.id)


        Assert.assertTrue(result)


        Mockito.verify(listDao).updateUser(
            id = fakeUserContent.id,
            listName = fakeUserContent.name,
            listFirstname = fakeUserContent.firstname,
            listPhone = fakeUserContent.phone,
            listEmail = fakeUserContent.email,
            listBirthday = fakeUserContent.birthday,
            listWage = fakeUserContent.wage,
            listNote = fakeUserContent.note,
            listFavorite = fakeUserContent.favorite,
            listPicture = fakeUserContent.picture.toString()
        )
    }


}