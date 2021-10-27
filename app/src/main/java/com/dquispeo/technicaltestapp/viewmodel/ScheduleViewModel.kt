package com.dquispeo.technicaltestapp.viewmodel

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dquispeo.technicaltestapp.databinding.DlgScheduleCreateBinding
import com.dquispeo.technicaltestapp.db.repository.ScheduleRepository
import com.dquispeo.technicaltestapp.db.model.Schedule
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ScheduleViewModel(private val repository: ScheduleRepository) : ViewModel(), Observable {

    val schedules = repository.schedules

    @Bindable
    val inputTitle = MutableLiveData<String>()

    @Bindable
    val inputContent = MutableLiveData<String>()

    //
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun createDialog(context: Context): AlertDialog {
        val binding = DlgScheduleCreateBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)
        binding.dlgSchTilDateEnd.setOnClickListener {
            val dpd =
                //DatePickerDialog(context, R.style.DatePickerTheme, { view, mYear, mMonth, mDay ->
                DatePickerDialog(context, { view, mYear, mMonth, mDay ->
                    if (mMonth < 9 && mDay > 9) {
                        binding.dlgSchTilDateEnd.setText("$mDay/0${mMonth + 1}/$mYear")
                    } else if (mMonth > 8 && mDay < 10) {
                        binding.dlgSchTilDateEnd.setText("0$mDay/${mMonth + 1}/$mYear")
                    } else if (mMonth < 9 && mDay < 10) {
                        binding.dlgSchTilDateEnd.setText("0$mDay/0${mMonth + 1}/$mYear")
                    } else if (mMonth > 8 && mDay > 9) {
                        binding.dlgSchTilDateEnd.setText("$mDay/${mMonth + 1}/$mYear")
                    }
                }, year, month, day)
            dpd.datePicker.minDate = c.timeInMillis
            dpd.show()
        }

        binding.dlgSchBtnAccept.setOnClickListener {
            if (binding.dlgSchTitle.text.toString().trim().isNotEmpty()) {
                if (binding.dlgSchContent.text.toString().trim().isNotEmpty()) {
                    if (binding.dlgSchTilDateEnd.text.toString().trim().isNotEmpty()) {
                        val formatter = SimpleDateFormat("dd/MM/yyyy")
                        val title = binding.dlgSchTitle.text.toString().trim()
                        val content = binding.dlgSchContent.text.toString().trim()
                        val dateStart = formatter.format(Date())
                        val dateEnd = binding.dlgSchTilDateEnd.text.toString()
                        addSchedule(Schedule(0, title, content, dateStart, dateEnd, false))
                        alertDialog.dismiss()
                    } else {
                        Toast.makeText(
                            context,
                            "Debe escribir la fecha de Finalización",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(context, "Debe escribir un Contenido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Debe escribir un Título", Toast.LENGTH_SHORT).show()
            }
        }

        binding.dlgSchBtnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        return alertDialog
    }

    private fun addSchedule(schedule: Schedule): Job = viewModelScope.launch {
        repository.insert(schedule)

    }

    fun updateSchedule(schedule: Schedule): Job = viewModelScope.launch {
        repository.update(schedule)
    }

    fun deleteSchedule(schedule: Schedule): Job = viewModelScope.launch {
        repository.delete(schedule)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }
}