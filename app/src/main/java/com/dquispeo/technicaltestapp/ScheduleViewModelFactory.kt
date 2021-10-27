package com.dquispeo.technicaltestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dquispeo.technicaltestapp.db.repository.ScheduleRepository
import com.dquispeo.technicaltestapp.viewmodel.ScheduleViewModel

class ScheduleViewModelFactory(private val repository: ScheduleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow View Model class")
    }

}