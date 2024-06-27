package com.example.techradar.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    private var _homeError = MutableStateFlow<String?>(null)
    val homeError: StateFlow<String?> = _homeError

    private val _homeAdd = MutableStateFlow(SimpleResponse.initial<List<Content?>>())
    val homeAdd: StateFlow<SimpleResponse<List<Content?>>> = _homeAdd.asStateFlow()


    val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun resetToast() {
        _homeError.value = null

    }

    fun resetAccountValue() {

        _homeAdd.value = SimpleResponse.initial()
    }

    fun allUsers() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val response = dataRepository.fetchAllUsers()


                if (response.isNotEmpty()) {
                    _homeAdd.value = SimpleResponse.success(response)
                    _loading.value = false

                } else {
                    val message = "Aucun candidat"
                    _homeError.value = message
                    _homeAdd.value = SimpleResponse.failure(Exception(message))
                    _loading.value = false
                }


            } catch (e: Exception) {
                val message = "unknown error occurred, please try again"
                _homeError.value = message
                _loading.value = false
            }
        }

    }


    fun allFavorites() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val response = dataRepository.fetchAllFavorites()

                if (response.isNotEmpty()) {
                    _homeAdd.value = SimpleResponse.success(response)
                    _loading.value = false

                } else {
                    val message = "No data found"
                    _homeError.value = message
                    _homeAdd.value = SimpleResponse.failure(Exception(message))
                    _loading.value = false
                }


            } catch (e: Exception) {
                val message = "unknown error occurred, please try again"
                _homeError.value = message
                _loading.value = false

            }
        }


    }

    fun searchBarFunction(search: String) {

        _loading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val result = dataRepository.searchFun(search)
                if (result.isNotEmpty()) {
                    _homeAdd.value = SimpleResponse.success(result)
                    _loading.value = false


                } else {
                    val message = "No data found"
                    _homeError.value = message
                    _homeAdd.value = SimpleResponse.failure(Exception(message))
                    _loading.value = false
                }


            } catch (e:Exception){

                val message = "unknown error occurred, please try again"
                _homeError.value = message
                _loading.value = false

            }


        }
    }
}
