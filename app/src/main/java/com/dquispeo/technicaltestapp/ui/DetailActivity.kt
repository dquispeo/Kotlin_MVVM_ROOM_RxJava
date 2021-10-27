package com.dquispeo.technicaltestapp.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dquispeo.technicaltestapp.*
import com.dquispeo.technicaltestapp.adapter.CommentAdapter
import com.dquispeo.technicaltestapp.databinding.ActivityDetailBinding
import com.dquispeo.technicaltestapp.db.repository.CommentRepository
import com.dquispeo.technicaltestapp.db.ScheduleDatabase
import com.dquispeo.technicaltestapp.db.repository.ScheduleRepository
import com.dquispeo.technicaltestapp.db.model.Comment
import com.dquispeo.technicaltestapp.db.model.Schedule
import com.dquispeo.technicaltestapp.viewmodel.CommentViewModel
import com.dquispeo.technicaltestapp.viewmodel.ScheduleViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var commentViewModel: CommentViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val bundle = intent.extras
        var schId = bundle!!.getInt("bdl_sch_id", 0)
        var schTitle = bundle.getString("bdl_sch_title", "-")
        var schDateStart = bundle.getString("bdl_sch_date_start", "-")
        var schDateEnd = bundle.getString("bdl_sch_date_end", "-")
        var schContent = bundle.getString("bdl_sch_content", "-")
        var schCompleted = bundle.getBoolean("bdl_sch_completed", false)

        binding.aDtlMtlTitle.title = schTitle
        binding.aDtlTxvDateStart.text = "Creada: ${schDateStart.substring(0, 5)}"
        binding.aDtlTxvDateEnd.text = "Finaliza: ${schDateEnd.substring(0, 5)}"
        binding.aDtlTxvContent.text = schContent

        val daoS = ScheduleDatabase.getInstance(application).scheduleDAO
        val daoC = ScheduleDatabase.getInstance(application).commentDAO
        val repositoryS = ScheduleRepository(daoS)
        val repositoryC = CommentRepository(daoC, schId)
        val factory = CommentViewModelFactory(repositoryC, repositoryS)
        /*scheduleViewModel = ViewModelProvider(this, factory).get(ScheduleViewModel::class.java)
        binding.scheduleDetailViewModel = scheduleViewModel
        binding.lifecycleOwner = this*/
        commentViewModel = ViewModelProvider(this, factory).get(CommentViewModel::class.java)
        binding.scheduleDetailViewModel = commentViewModel
        binding.lifecycleOwner = this

        binding.aDtlCivImage.setOnClickListener {
            if (binding.aDtlTieComment.text.toString().trim().isNotEmpty()) {
                val texto = binding.aDtlTieComment.text.toString().trim()
                commentViewModel.addComment(Comment(0, schId, "imagen.jpg", texto))
                binding.aDtlTieComment.setText("")
            }
        }

        //Añadir icons    binding.aDtlMtlTitle.inflateMenu(R.menu.top_app_bar_2)
        binding.aDtlMtlTitle.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.app_bar_delete -> {
                    commentViewModel.deleteDialog(
                        this, Schedule(
                            schId,
                            schTitle,
                            schContent,
                            schDateStart,
                            schDateEnd,
                            schCompleted
                        )
                    ).show()
                }
                R.id.app_bar_edit -> {
                    commentViewModel.updateDialog(
                        this, Schedule(
                            schId,
                            schTitle,
                            schContent,
                            schDateStart,
                            schDateEnd,
                            schCompleted
                        )
                    ).show()
                }
            }
            true
        }
        init()
    }

    private fun init() {
        binding.aDtlRclListComment.layoutManager = LinearLayoutManager(this)
        displaySchedulesList()
    }

    @SuppressLint("SetTextI18n")
    private fun displaySchedulesList() {
        commentViewModel.comments.observe(this, Observer {
            Log.i("TAG", it.toString())
            Log.i("TAG", it.size.toString())
            binding.aDtlTxvComment.text = "Comentarios (${it.size})"
            binding.aDtlRclListComment.adapter =
                CommentAdapter(it)
        })
    }

}