package com.example.yogaapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
        val alertDialog = AlertDialog.Builder(this)

        val dialogLayout = LayoutInflater.from(this).inflate(R.layout.edit_course_layout,null)
        alertDialog.setView(dialogLayout)

        val days = dialogLayout.findViewById<EditText>(R.id.editTextUpdateDow)
        val capacity = dialogLayout.findViewById<EditText>(R.id.editTextCapacity)
        val duration = dialogLayout.findViewById<EditText>(R.id.editTextDuration)
        val price = dialogLayout.findViewById<EditText>(R.id.editTextPrice)
        val btnUpdate = dialogLayout.findViewById<Button>(R.id.btnUpdateCourseDialog)

        days.setText(yogaCourse.dayOfWeek)
        capacity.setText(yogaCourse.capacity.toString())
        price.setText(yogaCourse.price.toString())
        duration.setText(yogaCourse.duration.toString())

        btnUpdate.setOnClickListener{
            val newYogaCourse = YogaCourse(
                yogaCourse.id,
                days.text.toString(),
                yogaCourse.timeOfCourse,
                capacity.text.toString().toInt(),
                duration.text.toString().toInt(),
                price.text.toString().toInt(),
                yogaCourse.typeOfClass,
                yogaCourse.description)

            val result = dbHelper.editCourse(newYogaCourse)
            if(result == 0) {
                Toast.makeText(this,"Edited Error!", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this,"Edited Course", Toast.LENGTH_LONG).show()
            }
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val alert = alertDialog.create()
        alert.show()
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

