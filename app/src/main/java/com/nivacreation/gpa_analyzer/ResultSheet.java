package com.nivacreation.gpa_analyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

public class ResultSheet extends AppCompatActivity {

    TextView gpaValue, ygpaValue, fgpaValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_sheet);

        gpaValue = findViewById(R.id.gpaValue);
        ygpaValue = findViewById(R.id.ygpaValue);
        fgpaValue = findViewById(R.id.fgpaValue);

        String g = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString("isGpaVal", "");
        String fg = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString("isGpaValFinal", "");
        //String s = getIntent().getStringExtra("gpaValue");
        gpaValue.setText(g);
        fgpaValue.setText(fg);
       // Toast.makeText(this, s,Toast.LENGTH_SHORT).show();
    }
}