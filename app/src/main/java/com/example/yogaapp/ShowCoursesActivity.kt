package com.example.yogaapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
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
            onDeleteClick =  {courseID -> deleteAction(courseID)},
            onGetCourseClick =  {courseID -> showCourseAction(courseID)},
            onAddClassClick =  {courseID -> addClassAction(courseID)})
        adapter.notifyDataSetChanged()
        binding.recyclerViewCourses.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCourses.adapter = adapter
    }

    private fun showCourseAction(courseID: Int) {
        val yogaCourse = dbHelper.getCourse(courseID)

        val stringBuilder = StringBuilder();
        stringBuilder
            .append("Day of week: ${yogaCourse.dayOfWeek}\n")
            .append("Day of time: ${yogaCourse.timeOfCourse}\n")
            .append("capacity: ${yogaCourse.capacity}\n")
            .append("duration: ${yogaCourse.duration}\n")
            .append("price: ${yogaCourse.price}\n")
            .append("Type of class: ${yogaCourse.typeOfClass} \n")
            .append("description: ${yogaCourse.description}\n")

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Course Details")
            .setMessage("")
            .setCancelable(false)
            .setPositiveButton("OK"){dialog, _-> dialog.dismiss()}
            .create().show()
    }

    private fun addClassAction(courseID: Int){
        val alertDialog = AlertDialog.Builder(this)

        val dialogLayout = LayoutInflater.from(this).inflate(R.layout.add_class_layout,null)
        alertDialog.setView(dialogLayout)

        val date = dialogLayout.findViewById<EditText>(R.id.editTxtDateOfClass)
        val teacher = dialogLayout.findViewById<EditText>(R.id.editTxtTeacherOfClass)
        val comment = dialogLayout.findViewById<EditText>(R.id.editTxtComment)
        val btnSave = dialogLayout.findViewById<Button>(R.id.btnSaveClass)

        btnSave.setOnClickListener {
            val yogaClass = YogaClass (
                0, courseID.toString(),date.text.toString(),teacher.text.toString(),comment.text.toString()
            )
            val result = dbHelper.saveClass(yogaClass)
            
            if(result==(-1).toLong()){
                Toast.makeText(this,"", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Save a Class", Toast.LENGTH_LONG).show()
            }
        }

        val alert = alertDialog.create()
        alert.show()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.course_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val result = when(item.itemId) {
            R.id.itemResetCourses -> {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Delete Course")
                    .setMessage("Are you sure you want to reset all courses?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") {
                            dialog, _ ->
                        val result = dbHelper.resetCourse()
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
                true
            }
            R.id.itemSetting -> {
                Toast.makeText(this, "Setting Item", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return result
    }
}


