package com.example.testfragmentview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataModel : ViewModel() {
    val on_click: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val back_event: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val zero_event: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val point_event: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}
