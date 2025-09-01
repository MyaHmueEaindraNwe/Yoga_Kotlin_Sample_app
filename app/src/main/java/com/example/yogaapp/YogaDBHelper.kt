package com.example.yogaapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class YogaDBHelper(context: Context) : SQLiteOpenHelper(context, "yoga.db", null, 1) {

    // Table: course
    private val courseTableName = "course"
    private val id = "id"
    private val dayOfWeek = "day_of_week"
    private val timeOfCourse = "time_of_course"
    private val capacity = "capacity"
    private val duration = "duration"
    private val price = "price"
    private val typeOfClass = "type_of_class"
    private val description = "description"

    // Table: class
    private val classTableName = "class"
    private val classId = "class_id"
    private val courseId = "course_id"   // foreign key reference to course
    private val dateOfClass = "date_of_class"
    private val teacher = "teacher"
    private val comments = "comments"

    override fun onCreate(db: SQLiteDatabase?) {
        // SQL for course table
        val createCourseTable = """
        CREATE TABLE $courseTableName (
            $id INTEGER PRIMARY KEY AUTOINCREMENT,
            $dayOfWeek VARCHAR(20) NOT NULL,
            $timeOfCourse VARCHAR(20) NOT NULL,
            $capacity VARCHAR(10),
            $duration VARCHAR(10),
            $price VARCHAR(20),
            $typeOfClass VARCHAR(20),
            $description VARCHAR(255)
        )
    """.trimIndent()

        // SQL for class table
        val createClassTable = """
        CREATE TABLE $classTableName (
            $classId INTEGER PRIMARY KEY AUTOINCREMENT,
            $courseId INTEGER,
            $dateOfClass VARCHAR(20) NOT NULL,
            $teacher VARCHAR(50),
            $comments VARCHAR(255),
            FOREIGN KEY($courseId) REFERENCES $courseTableName($id) ON DELETE CASCADE
        )
    """.trimIndent()

        db?.execSQL(createCourseTable)
        db?.execSQL(createClassTable)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop old tables if exist and recreate
        db?.execSQL("DROP TABLE IF EXISTS $classTableName")
        db?.execSQL("DROP TABLE IF EXISTS $courseTableName")
        onCreate(db)
    }

    fun saveCourse(yogaCourse: YogaCourse): Long {
        Log.i("Yoga DB", "***Save Course***")
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(dayOfWeek, yogaCourse.dayOfWeek)
        values.put(timeOfCourse, yogaCourse.timeOfCourse)
        values.put(capacity, yogaCourse.capacity)
        values.put(duration, yogaCourse.duration)
        values.put(price, yogaCourse.price)
        values.put(typeOfClass, yogaCourse.typeOfClass)
        values.put(description, yogaCourse.description)

        val result = db.insert(courseTableName, null, values)

        db.close()

        Log.i("result", "***Save result:***: $result")

        return result
    }

    fun getAllCourses(): List<YogaCourse> {
        val courses = mutableListOf<YogaCourse>()

        val db = this.readableDatabase
        val query = "SELECT * FROM $courseTableName"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(id))
                val dayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(dayOfWeek))
                val timeOfCourse = cursor.getString(cursor.getColumnIndexOrThrow(timeOfCourse))
                val capacity = cursor.getString(cursor.getColumnIndexOrThrow(capacity))
                val duration = cursor.getString(cursor.getColumnIndexOrThrow(duration))
                val price = cursor.getString(cursor.getColumnIndexOrThrow(price))
                val typeOfClass = cursor.getString(cursor.getColumnIndexOrThrow(typeOfClass))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(description))

                val course = YogaCourse(
                    id,
                    dayOfWeek,
                    timeOfCourse,
                    capacity.toInt(),
                    duration.toInt(),
                    price.toInt(),
                    typeOfClass,
                    description
                )
                courses.add(course)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return courses
    }

    fun editCourse(){
        
    }

    fun deleteCourse(courseID:Int): Int {
        val db = this.writableDatabase

        val result =  db.delete(courseTableName,"id=?",arrayOf(courseID.toString()))

        Log.i("Yoga DB", "Delete record: $result")
        return result
    }
}


