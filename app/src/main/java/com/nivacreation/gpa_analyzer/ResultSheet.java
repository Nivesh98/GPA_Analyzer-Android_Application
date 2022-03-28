package com.nivacreation.gpa_analyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ResultSheet extends AppCompatActivity {

    TextView gpaValue, ygpaValue, fgpaValue;

    String sName;

    private static final DecimalFormat df = new DecimalFormat("0.0000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_sheet);

        gpaValue = findViewById(R.id.gpaValue);
        ygpaValue = findViewById(R.id.ygpaValue);
        fgpaValue = findViewById(R.id.fgpaValue);

        sName = PreferenceManager.getDefaultSharedPreferences(this).getString("sName", "");
        Log.d("111","sName "+sName);
        String kk = sName.substring(sName.length()-1);
        int semNum = Integer.parseInt(kk);

        String g = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString("isGpaVal"+"Semester "+kk, "");
        String fg = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString("isGpaValFinal", "");

        if ((semNum%2)==1){
            ygpaValue.setText(g);
        }else{

            int pv = semNum-1;
            String v = String.valueOf(pv);
            String gpv = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString("isGpaVal"+"Semester "+v, "");
            double psv = Double.parseDouble(g);
            double sv = Double.parseDouble(gpv);

            double t = (psv+sv)/2;
            df.setRoundingMode(RoundingMode.UP);
            ygpaValue.setText(String.valueOf(df.format(t)));

        }
        gpaValue.setText(g);
        fgpaValue.setText(fg);
       // Toast.makeText(this, s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultSheet.this, Subjects_Activity.class);
        startActivity(intent);
        finish();
    }
}