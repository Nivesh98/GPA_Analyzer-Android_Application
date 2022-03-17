package com.nivacreation.gpa_analyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

        String semesterName = "Semester name not set";

        Bundle extras = getIntent().getExtras();

        if (extras != null){

            semesterName = extras.getString("SemesterName");
        }

        String k ="1"+semesterName;

        Log.d("123"," k "+k);

        String a = semesterName.substring(semesterName.length()-1);
        int b = Integer.parseInt(a);
        --b;
        PreferenceManager
                .getDefaultSharedPreferences(this).edit().putString("subjectDetails"+b, k.trim()).apply();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!editText.getText().toString().isEmpty()){
                    int intCount = Integer.parseInt(editText.getText().toString());
                    if(intCount >=1 && intCount<=15){
                        String countToString = editText.getText().toString();

                        Intent signInActivity = new Intent(SubjectDetails_Activity.this, Subjects_Activity.class);

                        String num = "courseCount"+a;
                        Log.d("123", " a "+num.trim());
                        PreferenceManager.getDefaultSharedPreferences(SubjectDetails_Activity.this).edit().putString("b", num.trim()).apply();
                        PreferenceManager
                                .getDefaultSharedPreferences(SubjectDetails_Activity.this).edit().putString(num.trim(), countToString).apply();
//                        PreferenceManager.getDefaultSharedPreferences(SubjectDetails_Activity.this).edit().putString("courseCount", countToString).apply();

                        startActivity(signInActivity);
                        //finish();
                    }else {
                        editText.setError("Subject should be \"1 - 15\" !");
                    }

                }else {
                    editText.setError("Empty Fields Are Not Allowed !");
                }


            }
        });


        if (extras != null){

            semesterName = extras.getString("SemesterName");
        }

        textView.setText(semesterName);

    }
}