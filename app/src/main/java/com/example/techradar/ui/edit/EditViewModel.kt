package com.example.techradar.ui.edit

import android.net.Uri
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
class EditViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {


    private val _editError = MutableStateFlow<String?>(null)
    val editError: StateFlow<String?> = _editError

    private val _editAdd = MutableStateFlow(SimpleResponse.initial<Content?>())
    val editAdd: StateFlow<SimpleResponse<Content?>> = _editAdd.asStateFlow()


    fun resetToast() {
        _editError.value = null

    }

    fun resetAccountValue() {

        _editAdd.value = SimpleResponse.initial()
    }


    fun editAccount(content: Content) {

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = dataRepository.editUser(content)

                if (response) {

                    _editAdd.value = SimpleResponse.success(content)


                } else {

                    val error = "Error, please complete at least one field"
                    _editAdd.value = SimpleResponse.failure(Exception(error))
                    _editError.value = error

                }


            } catch (


                e: Exception

            ) {

                val message = "Unknown error occurred, please try again"

                _editError.value = message

            }

        }
    }
}







