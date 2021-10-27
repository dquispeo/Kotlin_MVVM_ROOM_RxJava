package com.dquispeo.technicaltestapp.db.model.relations

import androidx.lifecycle.LiveData
import androidx.room.Embedded
import androidx.room.Relation
import com.dquispeo.technicaltestapp.db.model.Schedule

data class ScheduleWithComment(
    @Embedded
    val schedule: Schedule,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_schedule"
    )
    val schedules: LiveData<List<Schedule>>
)