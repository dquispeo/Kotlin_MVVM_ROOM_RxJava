package com.dquispeo.technicaltestapp.db.repository

import com.dquispeo.technicaltestapp.db.CommentDAO
import com.dquispeo.technicaltestapp.db.model.Comment

class CommentRepository(private val dao: CommentDAO, private val idSchedule: Int) {

    val comments = dao.getCommentsBySchedule(idSchedule)

    suspend fun insert(comment: Comment) {
        dao.insertComment(comment)
    }
}