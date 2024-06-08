package com.example.techradar.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techradar.data.DataRepository
import com.example.techradar.model.Content
import com.example.techradar.model.SimpleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {


    private val _homeError = MutableStateFlow<String?>(null)
    val homeError: StateFlow<String?> = _homeError

    private val _homeAdd = MutableStateFlow(SimpleResponse.initial<Content?>())
    val homeAdd: StateFlow<SimpleResponse<Content?>> = _homeAdd.asStateFlow()


    fun resetToast() {
        _homeError.value = null

    }

    fun resetAccountValue() {

        _homeAdd.value = SimpleResponse.initial()
    }

    fun allUsers() {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val response = dataRepository.fetchAllUsers()


                if (response.isNotEmpty()) {
                    _homeAdd.value = SimpleResponse.success(response)

                } else {
                    val message = "No data found"
                    _homeError.value = message
                    _homeAdd.value = SimpleResponse.failure(Exception(message))
                }


            } catch (e: Exception) {
                val message = "unknown error occurred, please try again"
                _homeError.value = message
            }
        }

    }


    fun allFavorites() {









    }






}
