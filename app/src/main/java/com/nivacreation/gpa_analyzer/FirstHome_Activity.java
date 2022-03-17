package com.nivacreation.gpa_analyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.nivacreation.gpa_analyzer.R.layout.*;

public class FirstHome_Activity extends AppCompatActivity {

    EditText count ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_first_home);

        Button continueBtn = findViewById(R.id.continueBtn);

        count = findViewById(R.id.countEtx);

        PreferenceManager
                .getDefaultSharedPreferences(this).edit().putString("semesterDetail", "1").apply();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!count.getText().toString().isEmpty()){
                    int intCount = Integer.parseInt(count.getText().toString());
                    if(intCount >=1 && intCount<=10){
                        String countToString = count.getText().toString();

                        Toast.makeText(FirstHome_Activity.this, "Semester Count = " +countToString, Toast.LENGTH_LONG).show();

                        Intent signInActivity = new Intent(FirstHome_Activity.this, Semesters_Activity.class);
                        PreferenceManager
                                .getDefaultSharedPreferences(FirstHome_Activity.this).edit().putString("isShow", countToString).apply();
                        signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(signInActivity);
                        finish();
                    }else {
                        count.setError("Semester should be \"1 - 10\" !");
                    }

                }else {
                    count.setError("Empty Fields Are Not Allowed !");
                }


            }
        });



    }
}