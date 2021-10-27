package com.dquispeo.technicaltestapp.db.repository

import com.dquispeo.technicaltestapp.db.ScheduleDAO
import com.dquispeo.technicaltestapp.db.model.Schedule

class ScheduleRepository(private val dao: ScheduleDAO) {

    val schedules = dao.getAllSchedules()

    suspend fun insert(schedule: Schedule) {
        dao.insertSchedule(schedule)
    }

    suspend fun update(schedule: Schedule) {
        dao.updateSchedule(schedule)
    }

    suspend fun delete(schedule: Schedule) {
        dao.deleteSchedule(schedule)
    }
}