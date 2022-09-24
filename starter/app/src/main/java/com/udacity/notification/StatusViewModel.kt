package com.udacity.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


enum class Status{
   SUCCESS , FAILED
}

class StatusViewModel :  ViewModel(){

    private val _state = MutableLiveData<Status>()
    val state: LiveData<Status>
        get() = _state


    fun setState(state:Status){
        _state.postValue(state)
    }

}