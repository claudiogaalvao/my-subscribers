package com.claudiogalvaodev.mysubscribers.ui.subscriber

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.claudiogalvaodev.mysubscribers.R
import com.claudiogalvaodev.mysubscribers.repository.SubscriberRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class SubscriberViewModel(
    private val repository: SubscriberRepository
) : ViewModel() {

    private val _subscriberStateEventData = MutableLiveData<SubscriberState>()
    val subscriberStateEventData: LiveData<SubscriberState>
        get() = _subscriberStateEventData

    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int>
        get() = _messageEventData

    fun addOrUpdateSubscriber(id: Long = 0, name: String, email: String) {
        if (id > 0) {
            updateSubscriber(id, name, email)
        } else {
            addSubscriber(name, email)
        }
    }

    private fun updateSubscriber(id: Long, name: String, email: String) = viewModelScope.launch {
        try {
            repository.updateSubscriber(id, name, email)
            _subscriberStateEventData.value = SubscriberState.Updated
            _messageEventData.value = R.string.subscriber_updated_successfully
        } catch (exception: Exception) {
            _messageEventData.value = R.string.subscriber_error_to_update
            Log.e(TAG, exception.toString())
        }
    }

    private fun addSubscriber(name: String, email: String) = viewModelScope.launch {
        try {
            val id = repository.insertSubscriber(name, email)
            if (id > 0) {
                _subscriberStateEventData.value = SubscriberState.Inserted
                _messageEventData.value = R.string.subscriber_inserted_successfully
            }
        } catch (exception: Exception) {
            _messageEventData.value = R.string.subscriber_error_to_insert
            Log.e(TAG, exception.toString())
        }
    }

    fun deleteSubscriber(id: Long) = viewModelScope.launch {
        try {
            if (id > 0) {
                repository.deleteSubscriber(id)
                _subscriberStateEventData.value = SubscriberState.Deleted
                _messageEventData.value = R.string.subscriber_deleted_successfully
            }
        } catch (exception: Exception) {
            _messageEventData.value = R.string.subscriber_error_to_insert
            Log.e(TAG, exception.toString())
        }
    }

    sealed class SubscriberState {
        object Inserted: SubscriberState()
        object Updated: SubscriberState()
        object Deleted: SubscriberState()
    }

    companion object {
        private val TAG = SubscriberViewModel::class.java.simpleName
    }

}