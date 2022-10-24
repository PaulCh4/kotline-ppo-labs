package com.example.testfragmentview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataModel : ViewModel() {
    val bt1_data: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val bt2_data: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val bt_back: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}
