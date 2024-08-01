package com.example.techradar.ui.detail

import Curencies
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
import java.util.Currency
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val dataRepository: DataRepository,

) : ViewModel() {


    private val _detailError = MutableStateFlow<String?>(null)
    val detailError: StateFlow<String?> = _detailError

    private val _detailAdd = MutableStateFlow(SimpleResponse.initial<Content?>())
    val detailAdd: StateFlow<SimpleResponse<Content?>> = _detailAdd.asStateFlow()


    private val _updateUserFav = MutableStateFlow(SimpleResponse.initial<Boolean>())
    val updateUserFav: StateFlow<SimpleResponse<Boolean>> = _updateUserFav.asStateFlow()

    private val _translate = MutableStateFlow(SimpleResponse.initial<Double>())
    val translate: StateFlow<SimpleResponse<Double>> = _translate.asStateFlow()

    fun resetToast() {
        _detailError.value = null

    }

    fun resetError() {

        _detailAdd.value = SimpleResponse.initial()
    }




    fun translateDate(to: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = dataRepository.fetchTranslate(to)
                _translate.value = SimpleResponse.success(result)
            } catch (e: Exception) {
                _translate.value = SimpleResponse.failure(e)
                _detailError.value = "Translation error: ${e.message}"
            }
        }
    }




    fun updateData(id: Long, bool: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.updateFav(id, bool)


            _updateUserFav.value = SimpleResponse.success(bool)
        }
    }


    fun deleteCandidate(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataRepository.suppressUser(id)
                _detailAdd.value = SimpleResponse.success(null)
            } catch (e: Exception) {
                _detailAdd.value = SimpleResponse.failure(Exception("Unknown error"))
                _detailError.value = "Unknown error"
            }
        }
    }

}

