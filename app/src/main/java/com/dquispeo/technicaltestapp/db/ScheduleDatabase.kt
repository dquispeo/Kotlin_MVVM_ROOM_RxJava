package com.dquispeo.technicaltestapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dquispeo.technicaltestapp.db.model.Comment
import com.dquispeo.technicaltestapp.db.model.Schedule

@Database(entities = [Schedule::class, Comment::class], version = 1)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract val scheduleDAO: ScheduleDAO
    abstract val commentDAO: CommentDAO

    companion object {
        @Volatile
        private var INSTANCE: ScheduleDatabase? = null
        fun getInstance(context: Context): ScheduleDatabase {
            synchronized(this) {
                var instance: ScheduleDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ScheduleDatabase::class.java,
                        "schedule_database"
                    ).build()
                }
                return instance
            }
        }
    }
}