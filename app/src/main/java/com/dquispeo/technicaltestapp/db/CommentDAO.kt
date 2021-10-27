package com.dquispeo.technicaltestapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dquispeo.technicaltestapp.db.model.Comment

@Dao
interface CommentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComment(comment: Comment): Long

    @Query("SELECT * FROM comment_table WHERE id_schedule=:idSchedule ORDER BY id ASC")
    fun getCommentsBySchedule(idSchedule: Int): LiveData<List<Comment>>
}