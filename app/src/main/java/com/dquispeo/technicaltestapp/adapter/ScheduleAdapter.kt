package com.dquispeo.technicaltestapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dquispeo.technicaltestapp.R
import com.dquispeo.technicaltestapp.databinding.ItemScheduleBinding
import com.dquispeo.technicaltestapp.viewmodel.ScheduleViewModel
import com.dquispeo.technicaltestapp.db.model.Schedule

class ScheduleAdapter(
    private val scheduleList: List<Schedule>,
    private val scheduleViewModel: ScheduleViewModel,
    private val clickListener: (Schedule) -> Unit
) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemScheduleBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_schedule, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scheduleList[position], scheduleViewModel, clickListener)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
}

class ViewHolder(val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(
        schedule: Schedule,
        scheduleViewModel: ScheduleViewModel,
        clickListener: (Schedule) -> Unit
    ) {
        if (schedule.id.toString().length == 1) {
            binding.itemSchId.text = "0" + schedule.id
        } else {
            binding.itemSchId.text = schedule.id.toString()
        }
        binding.itemSchTitle.text = schedule.title
        binding.itemSchContent.text = schedule.content
        if (schedule.dateStart.isNotEmpty() && schedule.dateStart.length > 5) {
            binding.itemSchDateStart.text = "Creada: " + schedule.dateStart.substring(0, 5)
        }
        if (schedule.dateEnd.isNotEmpty() && schedule.dateEnd.length > 5) {
            binding.itemSchDateEnd.text = "Finaliza: " + schedule.dateEnd.substring(0, 5)
        }
        binding.itemSchCompleted.isChecked = schedule.completed
        binding.itemSchCard.setOnClickListener {
            clickListener(schedule)
        }
        binding.itemSchCompleted.setOnClickListener {
            scheduleViewModel.updateSchedule(
                Schedule(
                    schedule.id,
                    schedule.title,
                    schedule.content,
                    schedule.dateStart,
                    schedule.dateEnd,
                    !schedule.completed
                )
            )
        }
    }
}