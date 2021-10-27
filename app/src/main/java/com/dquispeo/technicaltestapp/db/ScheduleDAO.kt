package com.dquispeo.technicaltestapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dquispeo.technicaltestapp.db.model.Schedule

@Dao
interface ScheduleDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSchedule(schedule: Schedule): Long

    //@Update(onConflict = OnConflictStrategy.REPLACE)
    @Update
    suspend fun updateSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

    @Query("SELECT * FROM schedule_table ORDER BY id DESC")
    fun getAllSchedules(): LiveData<List<Schedule>>
}