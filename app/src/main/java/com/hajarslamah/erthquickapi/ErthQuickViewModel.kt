package com.hajarslamah.erthquickapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ErthQuickViewModel : ViewModel() {
    val erthItemsLiveData: LiveData<List<ErthData>>

    init {
        erthItemsLiveData = ErthQuickFetcher().fetchContents()
    }
}