package com.dquispeo.technicaltestapp.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment_table")
class Comment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "id_schedule")
    val idSchedule: Int,

    @ColumnInfo(name = "photo")
    val photo: String,

    @ColumnInfo(name = "content")
    val content: String
)