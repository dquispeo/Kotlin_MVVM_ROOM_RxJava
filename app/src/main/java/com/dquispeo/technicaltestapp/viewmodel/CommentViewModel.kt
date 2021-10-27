package com.dquispeo.technicaltestapp.viewmodel

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dquispeo.technicaltestapp.databinding.DlgConfirmBinding
import com.dquispeo.technicaltestapp.databinding.DlgScheduleCreateBinding
import com.dquispeo.technicaltestapp.db.repository.CommentRepository
import com.dquispeo.technicaltestapp.db.repository.ScheduleRepository
import com.dquispeo.technicaltestapp.db.model.Comment
import com.dquispeo.technicaltestapp.db.model.Schedule
import com.dquispeo.technicaltestapp.ui.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class CommentViewModel(
    private val repositoryS: ScheduleRepository,
    private val repositoryC: CommentRepository
) : ViewModel(), Observable {

    val comments = repositoryC.comments

    //
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDialog(context: Context, schedule: Schedule): AlertDialog {
        val binding = DlgScheduleCreateBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)
        binding.dlgSchTxvTitle.text = "Editar actividad"
        binding.dlgSchTitle.setText(schedule.title)
        binding.dlgSchContent.setText(schedule.content)
        binding.dlgSchTilDateEnd.setText(schedule.dateEnd)

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
                        val title = binding.dlgSchTitle.text.toString().trim()
                        val content = binding.dlgSchContent.text.toString().trim()
                        val dateEnd = binding.dlgSchTilDateEnd.text.toString().trim()
                        updateSchedule(
                            Schedule(
                                schedule.id,
                                title,
                                content,
                                schedule.dateStart,
                                dateEnd,
                                schedule.completed
                            )
                        )
                        //addComment(Comment(0, schedule.id, "as", "comentario ejemplo 2"))
                        alertDialog.dismiss()
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
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

    @SuppressLint("SetTextI18n")
    fun deleteDialog(context: Context, schedule: Schedule): AlertDialog {
        val binding = DlgConfirmBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)
        binding.dlgConfirmTxvTitle.setText("¿Seguro quieres eliminar ${schedule.title}?")

        binding.dlgConfirmBtnAccept.setOnClickListener {
            deleteSchedule(
                Schedule(
                    schedule.id,
                    schedule.title,
                    schedule.content,
                    schedule.dateStart,
                    schedule.dateEnd,
                    schedule.completed
                )
            )
            alertDialog.dismiss()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        binding.dlgConfirmBtnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        return alertDialog
    }

    private fun updateSchedule(schedule: Schedule): Job = viewModelScope.launch {
        repositoryS.update(schedule)
    }

    private fun deleteSchedule(schedule: Schedule): Job = viewModelScope.launch {
        repositoryS.delete(schedule)
    }

    fun addComment(comment: Comment): Job = viewModelScope.launch {
        repositoryC.insert(comment)

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

}