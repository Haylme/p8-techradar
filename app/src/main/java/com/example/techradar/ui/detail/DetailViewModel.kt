package com.example.techradar.ui.detail

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
class DetailViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {


    private val _detailError = MutableStateFlow<String?>(null)
    val detailError: StateFlow<String?> = _detailError

    private val _detailAdd = MutableStateFlow(SimpleResponse.initial<Content?>())
    val detailAdd: StateFlow<SimpleResponse<Content?>> = _detailAdd.asStateFlow()


    fun resetToast() {
        _detailError.value = null

    }

    fun resetError() {

        _detailAdd.value = SimpleResponse.initial()
    }


    fun detailUser(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val detail = dataRepository.fetchDetailUser(id)
                if (detail != null) {


                    _detailAdd.value = SimpleResponse.success(detail)

                } else {
                    _detailAdd.value = SimpleResponse.failure(Exception("No user found"))
                    _detailError.value = "No user found"
                }


            } catch (e: Exception) {

                val message = "Error when fetching user account, please try again"
                _detailError.value = message

            }


        }


    }

    fun updateData(content: Content) {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val result = dataRepository.updateFav(content)

                if (result) {
                    _updateResult.value = SimpleResponse.success(result.body())
                    )

                }


            }


        }
    }
    fun deleteCandidate (){


    }

}