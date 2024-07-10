package com.example.techradar.ui.edit

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
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class EditViewModel @Inject constructor(
    private val dataRepository: DataRepository,
   // private val credencies: Credencies
) : ViewModel() {


    private val _editError = MutableStateFlow<String?>(null)
    val editError: StateFlow<String?> = _editError

    private val _editAdd = MutableStateFlow(SimpleResponse.initial<Content?>())
    val editAdd: StateFlow<SimpleResponse<Content?>> = _editAdd.asStateFlow()





    private val EMAIL_ADDRESS_PATTERN: Pattern
        get() = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +

                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

    fun checkMail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }


    fun checkField(
        nom: String,
        prenom: String,
        phone: String,
        email: String,
        date: String,
        wage: Int,
        note: String,
        picture: String
    ): Boolean {

        return nom.isNotEmpty() || prenom.isNotEmpty() || phone.isNotEmpty() ||
                email.isNotEmpty() || date.isNotEmpty() || wage > 0 || note.isNotEmpty() || picture.isNotEmpty()
    }

    fun resetToast() {
        _editError.value = null

    }

    fun resetAccountValue() {

        _editAdd.value = SimpleResponse.initial()
    }


    fun editAccount(content: Content,id:Long) {

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = dataRepository.editUser(content,id)

                if (response ) {

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







