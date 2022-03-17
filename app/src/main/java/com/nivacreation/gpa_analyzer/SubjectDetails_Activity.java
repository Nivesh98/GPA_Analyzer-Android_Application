package com.nivacreation.gpa_analyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SubjectDetails_Activity extends AppCompatActivity {

    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        textView = findViewById(R.id.semesterTxt);
        Button button = findViewById(R.id.continueBtn);
        editText = findViewById(R.id.course_countEdt);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!editText.getText().toString().isEmpty()){
                    int intCount = Integer.parseInt(editText.getText().toString());
                    if(intCount >=1 && intCount<=10){
                        String countToString = editText.getText().toString();

                        Intent signInActivity = new Intent(SubjectDetails_Activity.this, Subjects_Activity.class);
                        PreferenceManager
                                .getDefaultSharedPreferences(SubjectDetails_Activity.this).edit().putString("courseCount", countToString).apply();
                        //signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(signInActivity);
                        //finish();
                    }else {
                        editText.setError("Semester should be \"1 - 10\" !");
                    }

                }else {
                    editText.setError("Empty Fields Are Not Allowed !");
                }


            }
        });

        String semesterName = "Semester name not set";

        Bundle extras = getIntent().getExtras();

        if (extras != null){

            semesterName = extras.getString("SemesterName");
        }

        textView.setText(semesterName);

    }
}