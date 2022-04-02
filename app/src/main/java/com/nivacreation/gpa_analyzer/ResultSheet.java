package com.nivacreation.gpa_analyzer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nivacreation.gpa_analyzer.model.SubjectsNew;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ResultSheet extends AppCompatActivity {

    TextView gpaValue, ygpaValue, fgpaValue, gradeTxt,predictTxt;

    EditText enterTotCre;

    LinearLayout linearLayout;

    TextView classValue, classStatus;
    ImageView classImage;

    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;

    String sName;

    double gg;
    double fgg;

    double ygg;

    double totalCredit =0, totalSum=0, gradeValue= 0;

    private static final DecimalFormat df = new DecimalFormat("0.0000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_sheet);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        enterTotCre = findViewById(R.id.enterTotalCredit);

        linearLayout = findViewById(R.id.linearlayPredict);

        gpaValue = findViewById(R.id.gpaValue);
        ygpaValue = findViewById(R.id.ygpaValue);
        fgpaValue = findViewById(R.id.fgpaValue);

        gradeTxt = findViewById(R.id.gradeTxt);
        predictTxt = findViewById(R.id.predicttxt);

        classValue = findViewById(R.id.classValue);
        classStatus = findViewById(R.id.classStatus);
        classImage = findViewById(R.id.classImg);

        String uid = mAuth.getCurrentUser().getUid();

        PreferenceManager
                .getDefaultSharedPreferences(ResultSheet.this).edit().putString(uid+"iGetSubDetail", "#").apply();
        sName = PreferenceManager.getDefaultSharedPreferences(this).getString(uid+"sName", "");
        Log.d("111","sName "+sName);
        String kk = sName.substring(sName.length()-1);
        int semNum = Integer.parseInt(kk);

        String g = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString(uid+"isGpaVal"+"Semester "+kk, "");
        String fg = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString(uid+"isGpaValFinal", "");

        String totCredit = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString(uid+"isGpaValFinalToCre", "");
        String totSum = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString(uid+"isGpaValFinalTotalS", "");

        String yg = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString(uid+"isGpaValYGPA"+"Semester "+kk, "");

        Log.d("963","Total Credit = "+totCredit+" TotalSum = "+totSum);

        if (!totCredit.equals("")){

            totalCredit = Double.parseDouble(totCredit);
        }

        if (!totSum.equals("")){

            totalSum = Double.parseDouble(totSum);

        }

        if (!yg.equals("")){
            ygg = Double.parseDouble(yg);
        }

        Log.d("753","ygpa = "+ygg+" yg "+yg);
        if(!g.equals("")){
            gg = Double.parseDouble(g);
        }

        if (!fg.equals("")){
            fgg = Double.parseDouble(fg);
        }

        if ((semNum%2)==1){
            //df.setRoundingMode(RoundingMode.UP);
          //  df.setMaximumFractionDigits(4);
           // df.setRoundingMode(RoundingMode.FLOOR);
            ygpaValue.setText(df.format(gg));
            Log.d("963"," gg ="+gg);
        }else{

            int pv = semNum-1;
            String v = String.valueOf(pv);
            String gpv = PreferenceManager.getDefaultSharedPreferences(ResultSheet.this).getString(uid+"isGpaVal"+"Semester "+v, "");
            double psv = Double.parseDouble(g);
            double sv = Double.parseDouble(gpv);

            double t = (psv+sv)/2;
            //df.setRoundingMode(RoundingMode.UP);
            Log.d("963"," yGpa = "+t);
           // df.setMaximumFractionDigits(4);
           // df.setRoundingMode(RoundingMode.FLOOR);
            ygpaValue.setText(String.valueOf(df.format(ygg)));

        }
       // df.setRoundingMode(RoundingMode.UP);
        //df.setMaximumFractionDigits(4);
        //df.setRoundingMode(RoundingMode.FLOOR);
        gpaValue.setText(df.format(gg));
        fgpaValue.setText(df.format(fgg));

        gradeTxt.setVisibility(View.INVISIBLE);

        Log.d("963"," sgpa ="+gg);
        Log.d("963"," fgpa = "+fgg);

        String h = df.format(fgg);

        double finClass = Double.parseDouble(h);
       // df.setRoundingMode(RoundingMode.UP);
       // df.setRoundingMode(RoundingMode.FLOOR);
        if (finClass>=3.7000){

            classValue.setText("First Class - ");
            classValue.setTextColor(Color.rgb(100,190,10));
            classStatus.setText("Excellent!");
            classStatus.setTextColor(Color.rgb(100,190,10));
            classImage.setBackgroundResource(R.drawable.star);

            gradeTxt.setVisibility(View.INVISIBLE);
            predictTxt.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.INVISIBLE);

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
        enterTotCre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String credit = enterTotCre.getText().toString();

                if (!credit.equals("")){
                    int cre = Integer.parseInt(credit);

                    if (finClass>=3.7000){


                        gradeTxt.setVisibility(View.INVISIBLE);
                        predictTxt.setVisibility(View.INVISIBLE);
                        linearLayout.setVisibility(View.INVISIBLE);

                    }else if (finClass>=3.3 && finClass<3.7){


                        // gv = (((3.71)*(totC+3))-sum)/3
                        gradeTxt.setVisibility(View.VISIBLE);
                        gradeValue =(((3.7000)*(totalCredit+cre))-totalSum)/cre;
                        Log.d("963", "gradeValue in second upper= "+gradeValue);
                        getGrade(gradeValue,"Minimum grade for getting First Class ");

                    }else if (finClass>=3 && finClass<3.3){
                        gradeTxt.setVisibility(View.VISIBLE);

                        gradeValue =(((3.3000)*(totalCredit+cre))-totalSum)/cre;
                        Log.d("963", "gradeValue in second lower= "+gradeValue);
                        getGrade(gradeValue,"Minimum grade for getting Second Upper ");

                    }else if (finClass>=2 && finClass<3){
                        gradeTxt.setVisibility(View.VISIBLE);

                        gradeValue =(((3.000)*(totalCredit+cre))-totalSum)/cre;
                        Log.d("963", "gradeValue in passed= "+gradeValue);
                        getGrade(gradeValue,"Minimum grade for getting Second Lower ");

                    }else{
                        gradeTxt.setVisibility(View.VISIBLE);

                        gradeValue =(((2.000)*(totalCredit+cre))-totalSum)/cre;
                        Log.d("963", "gradeValue in bad= "+gradeValue);
                        getGrade(gradeValue,"Minimum grade for getting Pass ");
                    }
                }else{

                    gradeTxt.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Log.d("963", "gradeValue = "+gradeValue);
    }

    private void getGrade(double gradeValue, String description) {
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("GradesValue").document(userId);
        documentReference.addSnapshotListener( ResultSheet.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations
                    .Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if (value != null && value.exists()) {

                    String APV =(value.getString("APV"));
                   String AV =(value.getString("AV"));
                   String AMV =(value.getString("AMV"));
                    String BPV =(value.getString("BPV"));
                    String BV =(value.getString("BV"));
                    String BMV =(value.getString("BMV"));
                   String CPV =(value.getString("CPV"));
                    String CV =(value.getString("CV"));
                    String CMV =(value.getString("CMV"));
                    String DPV =(value.getString("DPV"));

                    double apvd = Double.parseDouble(APV);
                    double avd = Double.parseDouble(AV);
                    double amvd = Double.parseDouble(AMV);
                    double bpvd = Double.parseDouble(BPV);
                    double bvd = Double.parseDouble(BV);
                    double bmvd = Double.parseDouble(BMV);
                    double cpvd = Double.parseDouble(CPV);
                    double cvd = Double.parseDouble(CV);
                    double cmvd = Double.parseDouble(CMV);
                    double dpvd = Double.parseDouble(DPV);

                    double gradevalueArray[] = new double[10];

                    gradevalueArray[0] = dpvd;
                    gradevalueArray[1] = cmvd;
                    gradevalueArray[2] = cvd;
                    gradevalueArray[3] = cpvd;
                    gradevalueArray[4] = bmvd;
                    gradevalueArray[5] = bvd;
                    gradevalueArray[6] = bpvd;
                    gradevalueArray[7] = amvd;
                    gradevalueArray[8] = avd;
                    gradevalueArray[9] = apvd;

                    String gradeArray[] = new String[10];

                    gradeArray[0]="D+";
                    gradeArray[1] ="C-";
                    gradeArray[2] ="C";
                    gradeArray[3] ="C+";
                    gradeArray[4] ="B-";
                    gradeArray[5] ="B";
                    gradeArray[6] ="B+";
                    gradeArray[7] ="A-";
                    gradeArray[8] ="A";
                    gradeArray[9] ="A+";

                    String k = df.format(gradeValue);

                    double kk = Double.parseDouble(k);

                    for (int i=0; i<10; i++){

                        if (gradevalueArray[i]>=kk){

                            gradeTxt.setText(description+"\n"+gradeArray[i]);
                            break;

                        }

                        if (gradevalueArray[9]<kk){

                            gradeTxt.setText("You can't earn other class");

                        }
                    }



                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ResultSheet.this, Subjects_Activity.class);
        startActivity(intent);
        finish();
    }
}