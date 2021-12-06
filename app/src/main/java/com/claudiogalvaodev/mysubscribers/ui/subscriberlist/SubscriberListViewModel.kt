package com.claudiogalvaodev.mysubscribers.ui.subscriberlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.claudiogalvaodev.mysubscribers.data.db.entity.SubscriberEntity
import com.claudiogalvaodev.mysubscribers.repository.SubscriberRepository
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class SubscriberListViewModel(
    private val repository: SubscriberRepository
) : ViewModel() {

    private var _allSubscribersEvent = MutableLiveData<List<SubscriberEntity>>()
    val allSubscribersEvent: LiveData<List<SubscriberEntity>>
        get() = _allSubscribersEvent

    fun getSubscribers() = viewModelScope.launch {
        _allSubscribersEvent.postValue(repository.getAllSubscribers())
    }
}