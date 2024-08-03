package com.example.techradar

import com.example.techradar.data.DataRepository
import com.example.techradar.model.Content
import com.example.techradar.model.SimpleResponse
import com.example.techradar.ui.add.AddViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AddViewModelTest {

    @Mock
    private lateinit var dataRepository: DataRepository
    private lateinit var addViewModel: AddViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        addViewModel = AddViewModel(dataRepository)
    }

    @Test
    fun `addNewUser should update userAdd on success`() = runBlocking {
        val content = Content(
            id = 1,
            name = "John",
            firstname = "Doe",
            phone = "1234567890",
            email = "john.doe@example.com",
            birthday = "01/01/2000",
            wage = 100.0,
            note = "Test note",
            favorite = false,
            picture = null
        )

        `when`(dataRepository.addUser(content)).thenReturn(true)

        addViewModel.addNewUser(content)
        testScope.advanceUntilIdle()

        assertTrue(addViewModel.userAdd.first() == SimpleResponse.success(content))
    }
}
