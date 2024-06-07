package com.example.techradar.ui.home

import androidx.lifecycle.ViewModel
import com.example.techradar.data.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val dataRepository: DataRepository
) : ViewModel() {
    // TODO: Implement the ViewModel
}