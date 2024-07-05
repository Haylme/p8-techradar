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

    private val _translate = MutableStateFlow(SimpleResponse.initial<Curencies>())
    val translate: StateFlow<SimpleResponse<Curencies>> = _translate.asStateFlow()

    fun resetToast() {
        _detailError.value = null

    }

    fun resetError() {

        _detailAdd.value = SimpleResponse.initial()
    }


     fun getSystemLanguage(): String {

        return Locale.getDefault().language

    }

     fun getCurrency():String {

        val defaultCurrency = Currency.getInstance(Locale.getDefault()).currencyCode



        return defaultCurrency


    }


    fun translateDate(date: String, to: Int) {

        viewModelScope.launch(Dispatchers.IO) {

           val lang:String = getSystemLanguage()

            val result: Curencies = dataRepository.fetchTranslate(date, to)



            SimpleResponse.success(result)
            _translate.value = SimpleResponse.success(result)


        }


    }


    /**  fun gettranslate(): Pair<String, Int> {

    val systemLanguage = getSystemLanguage()
    val currency = getCurrency()
    val date: String
    val to: Int
    when (systemLanguage) {


    "fr" -> {
    date = "dd/MM/yyyy"
    to = currency

    }

    "en" -> {
    date = "MM/dd/yyyy"

    to = currency


    }
    }
    return Pair(date,to)
    }**/


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

