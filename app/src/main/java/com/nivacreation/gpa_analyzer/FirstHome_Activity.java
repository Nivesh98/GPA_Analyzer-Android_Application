package com.nivacreation.gpa_analyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.nivacreation.gpa_analyzer.R.layout.*;

public class FirstHome_Activity extends AppCompatActivity {

    EditText count ;

    Button editBtn;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_first_home);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        Button continueBtn = findViewById(R.id.continueBtn);

        editBtn = findViewById(R.id.editBtn);

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

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(FirstHome_Activity.this);
                bottomSheetDialog.setContentView(popup_grades_details);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                EditText aplusvalue = bottomSheetDialog.findViewById(R.id.APlusValue),
                        avalue = bottomSheetDialog.findViewById(R.id.AValue),
                        aminvalue = bottomSheetDialog.findViewById(R.id.AMinValue),
                        bplusvalue = bottomSheetDialog.findViewById(R.id.BPlusValue),
                        bvalue = bottomSheetDialog.findViewById(R.id.BValue),
                        bminvalue = bottomSheetDialog.findViewById(R.id.BMinValue),
                        cplusvalue = bottomSheetDialog.findViewById(R.id.CPlusValue),
                        cvalue = bottomSheetDialog.findViewById(R.id.CValue),
                        cminvalue = bottomSheetDialog.findViewById(R.id.CMinValue),
                        dplusvalue = bottomSheetDialog.findViewById(R.id.DPlusValue);

                String apv = aplusvalue.getText().toString(), av = avalue.getText().toString(), amv = aminvalue.getText().toString(),
                        bpv = bplusvalue.getText().toString(), bv = bvalue.getText().toString(), bmv = bminvalue.getText().toString(),
                        cpv = cplusvalue.getText().toString(), cv = cvalue.getText().toString(), cmv = cminvalue.getText().toString(),
                        dpv = dplusvalue.getText().toString();




                Button okBtn = bottomSheetDialog.findViewById(R.id.okBtn);

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        documentReference = fStore.collection("GradesValue").document(userId);
                        Map<String,Object> grade = new HashMap<>();
                        EditText aplusvalue = bottomSheetDialog.findViewById(R.id.APlusValue),
                                avalue = bottomSheetDialog.findViewById(R.id.AValue),
                                aminvalue = bottomSheetDialog.findViewById(R.id.AMinValue),
                                bplusvalue = bottomSheetDialog.findViewById(R.id.BPlusValue),
                                bvalue = bottomSheetDialog.findViewById(R.id.BValue),
                                bminvalue = bottomSheetDialog.findViewById(R.id.BMinValue),
                                cplusvalue = bottomSheetDialog.findViewById(R.id.CPlusValue),
                                cvalue = bottomSheetDialog.findViewById(R.id.CValue),
                                cminvalue = bottomSheetDialog.findViewById(R.id.CMinValue),
                                dplusvalue = bottomSheetDialog.findViewById(R.id.DPlusValue);

                        String apv = aplusvalue.getText().toString(), av = avalue.getText().toString(), amv = aminvalue.getText().toString(),
                                bpv = bplusvalue.getText().toString(), bv = bvalue.getText().toString(), bmv = bminvalue.getText().toString(),
                                cpv = cplusvalue.getText().toString(), cv = cvalue.getText().toString(), cmv = cminvalue.getText().toString(),
                                dpv = dplusvalue.getText().toString();

                        grade.put("APV",apv);
                        grade.put("AV",av);
                        grade.put("AMV",amv);
                        grade.put("BPV",bpv);
                        grade.put("BV",bv);
                        grade.put("BMV",bmv);
                        grade.put("CPV",cpv);
                        grade.put("CV",cv);
                        grade.put("CMV",cmv);
                        grade.put("DPV",dpv);
                        documentReference.set(grade);

                    }
                });

                bottomSheetDialog.show();
            }
        });



    }
}