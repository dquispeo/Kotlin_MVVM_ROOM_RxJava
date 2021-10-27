package com.dquispeo.technicaltestapp.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dquispeo.technicaltestapp.R
import com.dquispeo.technicaltestapp.ScheduleViewModelFactory
import com.dquispeo.technicaltestapp.viewmodel.ScheduleViewModel
import com.dquispeo.technicaltestapp.adapter.ScheduleAdapter
import com.dquispeo.technicaltestapp.databinding.ActivityMainBinding
import com.dquispeo.technicaltestapp.db.ScheduleDatabase
import com.dquispeo.technicaltestapp.db.repository.ScheduleRepository
import com.dquispeo.technicaltestapp.db.model.Schedule

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var scheduleViewModel: ScheduleViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = ScheduleDatabase.getInstance(application).scheduleDAO
        val repository = ScheduleRepository(dao)
        val factory = ScheduleViewModelFactory(repository)
        scheduleViewModel = ViewModelProvider(this, factory).get(ScheduleViewModel::class.java)
        binding.scheduleViewModel = scheduleViewModel
        binding.lifecycleOwner = this
        init(false)
        binding.aSchImbView.setOnClickListener {
            binding.aSchImbView.visibility = View.GONE
            binding.aSchImbNoView.visibility = View.VISIBLE
            init(true)
        }
        binding.aSchImbNoView.setOnClickListener {
            binding.aSchImbView.visibility = View.VISIBLE
            binding.aSchImbNoView.visibility = View.GONE
            init(false)
        }
        binding.aSchFltAdd.setOnClickListener {
            scheduleViewModel.createDialog(this).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.top_app_bar_1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.app_bar_view) {
            Toast.makeText(this, "Item One Clicked", Toast.LENGTH_LONG).show()
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    private fun init(boolean: Boolean) {
        binding.aSchRclList.layoutManager = LinearLayoutManager(this)
        displaySchedulesList(boolean)
    }

    private fun displaySchedulesList(boolean: Boolean) {
        scheduleViewModel.schedules.observe(this, Observer {
            var lista: MutableList<Schedule> = mutableListOf<Schedule>()
            Log.i("TAG", it.toString())
            for (item in it) {
                if (item.completed == boolean) {
                    lista.add(item)
                }
            }
            binding.aSchRclList.adapter =
                ScheduleAdapter(lista,scheduleViewModel) { selectedItem: Schedule -> listItemClicked(selectedItem) }
        })
    }

    private fun listItemClicked(schedule: Schedule) {
        //oast.makeText(this, "Seleccionado ${schedule.title}", Toast.LENGTH_LONG).show()
        val bundle = Bundle()
        bundle.putInt("bdl_sch_id", schedule.id)
        bundle.putString("bdl_sch_title", schedule.title)
        bundle.putString("bdl_sch_date_start", schedule.dateStart)
        bundle.putString("bdl_sch_date_end", schedule.dateEnd)
        bundle.putString("bdl_sch_content", schedule.content)
        bundle.putBoolean("bdl_sch_completed", schedule.completed)
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}