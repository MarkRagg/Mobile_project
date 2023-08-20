package com.project.mobile_project.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.mobile_project.data.Activity
import com.project.mobile_project.data.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivitiesViewModel @Inject constructor(
    private val repository: ActivityRepository
): ViewModel() {

    val allActivities = repository.allActivities
    val activityLiveData = MutableLiveData<List<Activity>>()

    fun insertActivity(activity: Activity) = viewModelScope.launch {
        repository.insertActivity(activity)
    }

    fun deleteActivity(activity: Activity) = viewModelScope.launch {
        repository.deleteActivity(activity)
    }

    fun updateActivity(activity: Activity) = viewModelScope.launch {
        repository.updateActivity(activity)
    }

    fun deleteAllActivities() = viewModelScope.launch {
        repository.deleteAllActivities()
    }

    fun getActivitiesFromUsername(username: String) = viewModelScope.launch {
        repository.getActivitiesFromUser(username).collect {
            activityLiveData.postValue(it)
        }
    }

    private var _activitySelected: Activity? = null
    val activitySelected
        get() = _activitySelected

    fun selectActivity(activity: Activity) {
        _activitySelected = activity
    }
}
