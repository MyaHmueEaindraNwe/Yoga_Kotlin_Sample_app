package com.example.yogaapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yogaapp.databinding.ActivityShowCoursesBinding

class ShowCoursesActivity : AppCompatActivity() {

    lateinit var binding: ActivityShowCoursesBinding
    lateinit var dbHelper: YogaDBHelper

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = YogaDBHelper(this)

        val courses = dbHelper.getAllCourses()

        val adapter = YogaCourseAdapter(courses,
            onEditClick = {yogaCourse -> editAction(yogaCourse)},
            onDeleteClick =  {courseID -> deleteAction(courseID)})
        adapter.notifyDataSetChanged()
        binding.recyclerViewCourses.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCourses.adapter = adapter
    }

    private fun editAction(yogaCourse: YogaCourse){

    }

    private fun deleteAction(courseID: Int){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Course")
            .setMessage("Are you sure you want to delete it?")
            .setCancelable(false)
            .setPositiveButton("Yes") {
               dialog, _ ->
                val result = dbHelper.deleteCourse(courseID)
                if(result == 0) {
                    Toast.makeText(this,"Delection Error!", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this,"Deleted Course", Toast.LENGTH_LONG).show()
                }
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No"){
                dialog, _ -> dialog.dismiss()
            }

            .create().show()
    }
}

