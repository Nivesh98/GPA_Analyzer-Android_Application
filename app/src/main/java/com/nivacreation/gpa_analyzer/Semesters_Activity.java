package com.nivacreation.gpa_analyzer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.nivacreation.gpa_analyzer.adapter.SemestersAdapter;
import com.nivacreation.gpa_analyzer.model.Semesters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.nivacreation.gpa_analyzer.R.layout.popup_grades_details;

public class Semesters_Activity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    RecyclerView recyclerView;
    ArrayList<Semesters> semestersArrayList;
    SemestersAdapter semestersAdapter;

    Button logoutBtn;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    String userId;
    String vui;

    //for swip
    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static  int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesters);


        this.gestureDetector = new GestureDetector(Semesters_Activity.this,this);

        recyclerView = findViewById(R.id.semesterRecyclerViewer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        logoutBtn = findViewById(R.id.logOutBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        semestersArrayList = new ArrayList<Semesters>();

        semestersAdapter = new SemestersAdapter(this,getMyList());
        recyclerView.setAdapter(semestersAdapter);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fAuth.signOut();
                Intent signInActivity = new Intent(Semesters_Activity.this, MainActivity.class);
                signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signInActivity);
            }
        });


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


                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Semesters_Activity.this);
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
                        documentReference.addSnapshotListener(Semesters_Activity.this, new EventListener<DocumentSnapshot>() {
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

    private ArrayList<Semesters> getMyList() {

        ArrayList<Semesters> semesters = new ArrayList<>();

        Semesters s;


        String uid = fAuth.getCurrentUser().getUid();
        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(uid+"isShow", "");

        if (isShow != ""){
            String countGetValue = isShow;
            int countValueToInt;

            if(countGetValue != ""){
                countValueToInt = Integer.parseInt(countGetValue);
                for(int i=1; i<=countValueToInt; i++){

                    s = new Semesters();
                    s.setTitle("Semester "+i);
                    semesters.add(s);
                }
            }
        }







        return semesters;
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