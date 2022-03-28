package com.nivacreation.gpa_analyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.collection.LLRBNode;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ResultSheet extends AppCompatActivity {

    TextView gpaValue, ygpaValue, fgpaValue;

    TextView classValue, classStatus;
    ImageView classImage;

    String sName;

    private static final DecimalFormat df = new DecimalFormat("0.0000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_sheet);

        gpaValue = findViewById(R.id.gpaValue);
        ygpaValue = findViewById(R.id.ygpaValue);
        fgpaValue = findViewById(R.id.fgpaValue);

        classValue = findViewById(R.id.classValue);
        classStatus = findViewById(R.id.classStatus);
        classImage = findViewById(R.id.classImg);

        PreferenceManager
                .getDefaultSharedPreferences(ResultSheet.this).edit().putString("iGetSubDetail", "#").apply();
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

        double finClass = Double.parseDouble(fg);
        df.setRoundingMode(RoundingMode.UP);

        if (finClass>=3.7000){

            classValue.setText("First Class - ");
            classValue.setTextColor(Color.rgb(100,190,10));
            classStatus.setText("Excellent!");
            classStatus.setTextColor(Color.rgb(100,190,10));
            classImage.setBackgroundResource(R.drawable.star);

        }else if (finClass>=3.3 && finClass<3.7){

            classValue.setText("Second Upper - ");
            classValue.setTextColor(Color.rgb(50,90,190));
            classStatus.setText("Great!");
            classStatus.setTextColor(Color.rgb(50,90,190));
            classImage.setBackgroundResource(R.drawable.smiling);

        }else if (finClass>=3 && finClass<3.3){

            classValue.setText("Second Lower - ");
            classValue.setTextColor(Color.rgb(255,190,90));
            classStatus.setText("Good!");
            classStatus.setTextColor(Color.rgb(255,190,90));
            classImage.setBackgroundResource(R.drawable.happy);

        }else if (finClass>=2 && finClass<3){

            classValue.setText("Pass - ");
            classValue.setTextColor(Color.rgb(190,90,90));
            classStatus.setText("Fair!");
            classStatus.setTextColor(Color.rgb(190,90,90));
            classImage.setBackgroundResource(R.drawable.confused);

        }else{

            classValue.setText("Bad!");
            classValue.setTextColor(Color.rgb(255,0,0));
            classStatus.setVisibility(View.INVISIBLE);
            classImage.setBackgroundResource(R.drawable.sad);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ResultSheet.this, Subjects_Activity.class);
        startActivity(intent);
        finish();
    }
}