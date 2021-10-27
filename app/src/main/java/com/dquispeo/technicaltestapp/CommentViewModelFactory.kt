package com.dquispeo.technicaltestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dquispeo.technicaltestapp.db.repository.CommentRepository
import com.dquispeo.technicaltestapp.db.repository.ScheduleRepository
import com.dquispeo.technicaltestapp.viewmodel.CommentViewModel

class CommentViewModelFactory(
    private val repositoryC: CommentRepository,
    private val repositoryS: ScheduleRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            return CommentViewModel(repositoryS, repositoryC) as T
        }
        throw IllegalArgumentException("Unknow View Model class")
    }

}