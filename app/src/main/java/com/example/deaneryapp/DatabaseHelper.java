package com.example.deaneryapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "deanery.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Faculty (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
        db.execSQL("CREATE TABLE StudentGroup (id INTEGER PRIMARY KEY AUTOINCREMENT, number TEXT, faculty_id INTEGER)");
        db.execSQL("CREATE TABLE Student (id INTEGER PRIMARY KEY AUTOINCREMENT, full_name TEXT, group_id INTEGER)");
        db.execSQL("CREATE TABLE Exam (id INTEGER PRIMARY KEY AUTOINCREMENT, subject_name TEXT, date TEXT)");
        db.execSQL("CREATE TABLE Grade (id INTEGER PRIMARY KEY AUTOINCREMENT, exam_id INTEGER, student_id INTEGER, value INTEGER)");

        insertTestData(db);
    }

    private void insertTestData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Faculty (name) VALUES ('ФКСиС')");
        db.execSQL("INSERT INTO Faculty (name) VALUES ('ФИТУ')");

        db.execSQL("INSERT INTO StudentGroup (number, faculty_id) VALUES ('253551', 1)");
        db.execSQL("INSERT INTO StudentGroup (number, faculty_id) VALUES ('253552', 1)");
        db.execSQL("INSERT INTO StudentGroup (number, faculty_id) VALUES ('253553', 2)");

        db.execSQL("INSERT INTO Student (full_name, group_id) VALUES ('Иванов Иван Иванович', 1)");
        db.execSQL("INSERT INTO Student (full_name, group_id) VALUES ('Петров Петр Петрович', 1)");
        db.execSQL("INSERT INTO Student (full_name, group_id) VALUES ('Сидоров Алексей Николаевич', 2)");
        db.execSQL("INSERT INTO Student (full_name, group_id) VALUES ('Смирнова Анна Викторовна', 3)");

        db.execSQL("INSERT INTO Exam (subject_name, date) VALUES ('Математика', '2026-01-10')");
        db.execSQL("INSERT INTO Exam (subject_name, date) VALUES ('Программирование', '2026-01-15')");
        db.execSQL("INSERT INTO Exam (subject_name, date) VALUES ('Базы данных', '2026-01-20')");

        db.execSQL("INSERT INTO Grade (exam_id, student_id, value) VALUES (1, 1, 9)");
        db.execSQL("INSERT INTO Grade (exam_id, student_id, value) VALUES (2, 1, 8)");
        db.execSQL("INSERT INTO Grade (exam_id, student_id, value) VALUES (3, 1, 2)");

        db.execSQL("INSERT INTO Grade (exam_id, student_id, value) VALUES (1, 2, 6)");
        db.execSQL("INSERT INTO Grade (exam_id, student_id, value) VALUES (2, 2, 2)");
        db.execSQL("INSERT INTO Grade (exam_id, student_id, value) VALUES (3, 2, 7)");

        db.execSQL("INSERT INTO Grade (exam_id, student_id, value) VALUES (1, 3, 5)");
        db.execSQL("INSERT INTO Grade (exam_id, student_id, value) VALUES (2, 3, 9)");

        db.execSQL("INSERT INTO Grade (exam_id, student_id, value) VALUES (1, 4, 10)");
        db.execSQL("INSERT INTO Grade (exam_id, student_id, value) VALUES (3, 4, 2)");
    }

    public ArrayList<String> getStudentsByFaculty(String facultyName) {
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT s.full_name, sg.number " +
                        "FROM Student s " +
                        "JOIN StudentGroup sg ON s.group_id = sg.id " +
                        "JOIN Faculty f ON sg.faculty_id = f.id " +
                        "WHERE f.name = ?",
                new String[]{facultyName}
        );

        while (cursor.moveToNext()) {
            result.add("👤 " + cursor.getString(0) + "\nГруппа: " + cursor.getString(1));
        }

        cursor.close();
        return result;
    }

    public ArrayList<String> getExamResultsByStudent(String studentName) {
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT e.subject_name, e.date, g.value " +
                        "FROM Grade g " +
                        "JOIN Exam e ON g.exam_id = e.id " +
                        "JOIN Student s ON g.student_id = s.id " +
                        "WHERE s.full_name = ?",
                new String[]{studentName}
        );

        while (cursor.moveToNext()) {
            result.add("📘 " + cursor.getString(0) +
                    "\nДата: " + cursor.getString(1) +
                    "\nОценка: " + cursor.getInt(2));
        }

        cursor.close();
        return result;
    }

    public ArrayList<String> getBadGrades(String facultyName, String startDate, String endDate) {
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT s.full_name, e.subject_name, e.date, g.value " +
                        "FROM Grade g " +
                        "JOIN Student s ON g.student_id = s.id " +
                        "JOIN Exam e ON g.exam_id = e.id " +
                        "JOIN StudentGroup sg ON s.group_id = sg.id " +
                        "JOIN Faculty f ON sg.faculty_id = f.id " +
                        "WHERE g.value = 2 AND f.name = ? AND e.date BETWEEN ? AND ?",
                new String[]{facultyName, startDate, endDate}
        );

        while (cursor.moveToNext()) {
            result.add("❌ " + cursor.getString(0) +
                    "\nПредмет: " + cursor.getString(1) +
                    "\nДата: " + cursor.getString(2) +
                    "\nОценка: " + cursor.getInt(3));
        }

        cursor.close();
        return result;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Grade");
        db.execSQL("DROP TABLE IF EXISTS Exam");
        db.execSQL("DROP TABLE IF EXISTS Student");
        db.execSQL("DROP TABLE IF EXISTS StudentGroup");
        db.execSQL("DROP TABLE IF EXISTS Faculty");
        onCreate(db);
    }
}