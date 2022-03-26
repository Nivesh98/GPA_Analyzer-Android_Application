package com.nivacreation.gpa_analyzer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nivacreation.gpa_analyzer.adapter.SemestersAdapter;
import com.nivacreation.gpa_analyzer.adapter.SubjectsAdapter;
import com.nivacreation.gpa_analyzer.adapter.SubjectsNewAdapter;
import com.nivacreation.gpa_analyzer.model.Semesters;
import com.nivacreation.gpa_analyzer.model.Subjects;
import com.nivacreation.gpa_analyzer.model.SubjectsNew;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.nivacreation.gpa_analyzer.R.layout.*;

public class Subjects_Activity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    RecyclerView recyclerView;

    ArrayList<Subjects> subjectsArrayList;
    ArrayList<SubjectsNew> subjectsNewArrayList;

    SubjectsAdapter subjectsAdapter;
    SubjectsNewAdapter subjectsNewAdapter;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    Map<String,Object> user = new HashMap<>();

    static Subjects subject;

    String sName;

    String countGetValue;

    DocumentReference documentReference;
    String semesterName;

    int countValueToInt;

    Button evaluateBtn;


    //for swip
    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static  int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_subjects);

        this.gestureDetector = new GestureDetector(Subjects_Activity.this,this);

        recyclerView = findViewById(R.id.recyclerSubject);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        evaluateBtn = findViewById(R.id.evaluateBtn);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        sName = PreferenceManager.getDefaultSharedPreferences(this).getString("sName", "");

        if (fAuth.getCurrentUser().getUid() != null){
            FirebaseFirestore.getInstance().collection("Subjects").document(userId.trim()).delete();
        }

        subjectsArrayList = new ArrayList<Subjects>();
        subjectsNewArrayList = new ArrayList<SubjectsNew>();

        subjectsAdapter = new SubjectsAdapter(this,getMyList());
        subjectsNewAdapter = new SubjectsNewAdapter(this, subjectsNewArrayList);

        String checkValue = PreferenceManager.getDefaultSharedPreferences(this).getString("iGetSubDetail", "");

        Log.d("1245"," checkValue "+checkValue);

        //

        if (checkValue.equals("*")){
            recyclerView.setAdapter(subjectsAdapter);
        }

        if (checkValue.equals("#")){
            recyclerView.setAdapter(subjectsNewAdapter);
        }
        eventChangeListner();
        //eventChangeListner();


        SubjectsNew s = new SubjectsNew();
        Log.d("7896","grade = "+s.getGrade()+" credit = "+s.getCredit()+" gpa = "+s.getGpa()+" sub nam = "+s.getSubjectName()+" sub code = "+s.getSubjectCode());
        evaluateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


    }

    private void eventChangeListner() {

        fStore = FirebaseFirestore.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        Log.d("111","sName "+sName);
        String kk = sName.substring(sName.length()-1);

        Subjects s;
        String b = PreferenceManager.getDefaultSharedPreferences(this).getString("b", "");

        Log.d("123"," b "+b);
        String nam = "courseCount"+kk;
        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(nam.trim(), "");

        countGetValue = isShow;

        if (isShow != null){
            countValueToInt = Integer.parseInt(countGetValue);
        }
        sName = PreferenceManager.getDefaultSharedPreferences(this).getString("sName", "");
        for(int i=1; i<=countValueToInt; i++){
            fStore.collection("Subjects").document(userId).collection("Semester")
                    .document(sName).collection(String.valueOf(i)).orderBy("number",Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                            if(error != null){
                                Log.e("12345",error.getMessage());
                                return;
                            }

                            for (DocumentChange dc : value.getDocumentChanges()){

                                if (dc.getType() == DocumentChange.Type.ADDED){

                                    subjectsNewArrayList.add(dc.getDocument().toObject(SubjectsNew.class));

                                }

                                subjectsNewAdapter.notifyDataSetChanged();

                            }
                        }
                    });
        }


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


                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Subjects_Activity.this);
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
                        documentReference.addSnapshotListener(Subjects_Activity.this, new EventListener<DocumentSnapshot>() {
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
    public void onBackPressed() {
        super.onBackPressed();

        String kk = sName.substring(sName.length()-1);
        String b = PreferenceManager.getDefaultSharedPreferences(this).getString("b", "");
        Log.d("123"," b "+b);
        String nam = "courseCount"+kk;
        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(nam.trim(), "");

        if (isShow != null){
            Intent intent = new Intent(Subjects_Activity.this, Semesters_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private ArrayList<Subjects> getMyList() {



        Log.d("111","sName "+sName);
        String kk = sName.substring(sName.length()-1);

        ArrayList<Subjects> subjects = new ArrayList<>();

        Subjects s;
        String b = PreferenceManager.getDefaultSharedPreferences(this).getString("b", "");

        Log.d("123"," b "+b);
        String nam = "courseCount"+kk;
        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(nam.trim(), "");

        countGetValue = isShow;

        if (isShow != null){
            countValueToInt = Integer.parseInt(countGetValue);
        }

        for(int i=1; i<=countValueToInt; i++){
            s = new Subjects();
            s.setNumber(String.valueOf(i));
           subjects.add(s);
        }
        return subjects;
    }

    private void EventChangeListner() {
        String userId = fAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
        sName = PreferenceManager.getDefaultSharedPreferences(this).getString("sName", "");
        Log.d("111","sName "+sName);
        String kk = sName.substring(sName.length()-1);
        Subjects s;
        String b = PreferenceManager.getDefaultSharedPreferences(this).getString("b", "");

        Log.d("123"," b "+b);
        String nam = "courseCount"+kk;
        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(nam.trim(), "");

        countGetValue = isShow;

        if (isShow != null){
            countValueToInt = Integer.parseInt(countGetValue);
        }

        for(int i=1; i<=countValueToInt; i++){
            fStore.collection("Subjects").document(userId).collection("Semester").document(sName).collection(String.valueOf(i))
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                            if(error != null){
                                return;
                            }

                            for (DocumentChange dc : value.getDocumentChanges()){

                                if (dc.getType() == DocumentChange.Type.ADDED){

                                    subjectsArrayList.add(dc.getDocument().toObject(Subjects.class));
                                }
                                subjectsAdapter.notifyDataSetChanged();

                            }
                        }
                    });
        }
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