package com.example.deaneryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        Spinner spinnerFaculty = findViewById(R.id.spinnerFaculty);
        Spinner spinnerStudent = findViewById(R.id.spinnerStudent);
        Spinner spinnerBadFaculty = findViewById(R.id.spinnerBadFaculty);
        Spinner spinnerStartDate = findViewById(R.id.spinnerStartDate);
        Spinner spinnerEndDate = findViewById(R.id.spinnerEndDate);

        Button btnStudents = findViewById(R.id.btnStudents);
        Button btnExams = findViewById(R.id.btnExams);
        Button btnBadGrades = findViewById(R.id.btnBadGrades);
        Button btnReminder = findViewById(R.id.btnReminder);

        listResults = findViewById(R.id.listResults);

        setupSpinner(spinnerFaculty, new ArrayList<>(Arrays.asList("ФКСиС", "ФИТУ")));
        setupSpinner(spinnerBadFaculty, new ArrayList<>(Arrays.asList("ФКСиС", "ФИТУ")));

        setupSpinner(spinnerStudent, new ArrayList<>(Arrays.asList(
                "Иванов Иван Иванович",
                "Петров Петр Петрович",
                "Сидоров Алексей Николаевич",
                "Смирнова Анна Викторовна"
        )));

        setupSpinner(spinnerStartDate, new ArrayList<>(Arrays.asList(
                "2026-01-01",
                "2026-01-10",
                "2026-01-15",
                "2026-01-20"
        )));

        setupSpinner(spinnerEndDate, new ArrayList<>(Arrays.asList(
                "2026-01-31",
                "2026-01-20",
                "2026-01-15",
                "2026-01-10"
        )));

        btnStudents.setOnClickListener(v -> {
            String faculty = spinnerFaculty.getSelectedItem().toString();
            showList(dbHelper.getStudentsByFaculty(faculty));
        });

        btnExams.setOnClickListener(v -> {
            String student = spinnerStudent.getSelectedItem().toString();
            showList(dbHelper.getExamResultsByStudent(student));
        });

        btnBadGrades.setOnClickListener(v -> {
            String faculty = spinnerBadFaculty.getSelectedItem().toString();
            String startDate = spinnerStartDate.getSelectedItem().toString();
            String endDate = spinnerEndDate.getSelectedItem().toString();

            showList(dbHelper.getBadGrades(faculty, startDate, endDate));
        });

        btnReminder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
            startActivity(intent);
        });
    }

    private void setupSpinner(Spinner spinner, ArrayList<String> values) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                values
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void showList(ArrayList<String> data) {
        CustomAdapter adapter = new CustomAdapter(this, data);
        listResults.setAdapter(adapter);
    }
}