package com.dquispeo.technicaltestapp.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "schedule_table")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "date_start")
    val dateStart: String,

    @ColumnInfo(name = "date_end")
    val dateEnd: String,

    @ColumnInfo(name = "completed")
    val completed: Boolean
)