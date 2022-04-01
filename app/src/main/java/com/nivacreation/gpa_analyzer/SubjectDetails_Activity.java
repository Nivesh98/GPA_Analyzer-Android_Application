package com.nivacreation.gpa_analyzer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import static com.nivacreation.gpa_analyzer.R.layout.popup_grades_details;

public class SubjectDetails_Activity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    TextView textView;
    EditText editText;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    String userId;
    String vui;

    int b;

    //for swip
    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static  int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        this.gestureDetector = new GestureDetector(SubjectDetails_Activity.this,this);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

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
        b = Integer.parseInt(a);
        --b;



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = fAuth.getCurrentUser().getUid();

                if (!editText.getText().toString().isEmpty()){
                    PreferenceManager
                            .getDefaultSharedPreferences(SubjectDetails_Activity.this).edit().putString(uid+"subjectDetails"+b, k.trim()).apply();
                    int intCount = Integer.parseInt(editText.getText().toString());
                    if(intCount >=1 && intCount<=15){
                        String countToString = editText.getText().toString();

                        Intent signInActivity = new Intent(SubjectDetails_Activity.this, Subjects_Activity.class);

                        PreferenceManager
                                .getDefaultSharedPreferences(SubjectDetails_Activity.this).edit().putString(uid+"iGetSubDetail", "*").apply();
                        String num = "courseCount"+a;
                        Log.d("123", " a "+num.trim());
                        PreferenceManager.getDefaultSharedPreferences(SubjectDetails_Activity.this).edit().putString(uid+"b", num.trim()).apply();
                        PreferenceManager
                                .getDefaultSharedPreferences(SubjectDetails_Activity.this).edit().putString(uid+num.trim(), countToString).apply();
//                        PreferenceManager.getDefaultSharedPreferences(SubjectDetails_Activity.this).edit().putString("courseCount", countToString).apply();

                        startActivity(signInActivity);
                        finish();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                float valueX = x2 -x1;
                float valueY = y2 -y1;

                if (Math.abs(valueY) > MIN_DISTANCE){

                    if (y2<y1){


                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SubjectDetails_Activity.this);
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
                        String userId = fAuth.getCurrentUser().getUid();
                        documentReference = fStore.collection("GradesValue").document(userId);
                        documentReference.addSnapshotListener(SubjectDetails_Activity.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                                if (value != null && value.exists()) {
                                    aplusvalue.setText(value.getString("APV"));
                                    avalue.setText(value.getString("AV"));
                                    aminvalue.setText(value.getString("AMV"));
                                    bplusvalue.setText(value.getString("BPV"));
                                    bvalue.setText(value.getString("BV"));
                                    bminvalue.setText(value.getString("BMV"));
                                    cplusvalue.setText(value.getString("CPV"));
                                    cvalue.setText(value.getString("CV"));
                                    cminvalue.setText(value.getString("CMV"));
                                    dplusvalue.setText(value.getString("DPV"));

                                }
                            }
                        });


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

                                if (!apv.isEmpty()){

                                    if (!av.isEmpty()){

                                        if (!amv.isEmpty()){

                                            if (!bpv.isEmpty()){

                                                if (!bv.isEmpty()){

                                                    if (!bmv.isEmpty()){

                                                        if (!cpv.isEmpty()){

                                                            if (!cv.isEmpty()){

                                                                if (!cmv.isEmpty()){

                                                                    if (!dpv.isEmpty()){

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

                                                                    }else{
                                                                        dplusvalue.setError("Grade Value should be entered! ");
                                                                    }

                                                                }else{
                                                                    cminvalue.setError("Grade Value should be entered! ");
                                                                }

                                                            }else{
                                                                cvalue.setError("Grade Value should be entered! ");
                                                            }

                                                        }else{
                                                            cplusvalue.setError("Grade Value should be entered! ");
                                                        }

                                                    }else{
                                                        bminvalue.setError("Grade Value should be entered! ");
                                                    }

                                                }else{
                                                    bvalue.setError("Grade Value should be entered! ");
                                                }

                                            }else{
                                                bplusvalue.setError("Grade Value should be entered! ");
                                            }

                                        }else{
                                            aminvalue.setError("Grade Value should be entered! ");
                                        }

                                    }else{
                                        avalue.setError("Grade Value should be entered! ");
                                    }

                                }else{
                                    aplusvalue.setError("Grade Value should be entered! ");
                                }

                            }
                        });

                        bottomSheetDialog.show();
                    }

                }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}