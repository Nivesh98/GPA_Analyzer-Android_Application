package com.nivacreation.gpa_analyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nivacreation.gpa_analyzer.adapter.SemestersAdapter;
import com.nivacreation.gpa_analyzer.adapter.SubjectsAdapter;
import com.nivacreation.gpa_analyzer.model.Semesters;
import com.nivacreation.gpa_analyzer.model.Subjects;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.nivacreation.gpa_analyzer.R.layout.*;

public class Subjects_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Subjects> subjectsArrayList;
    SubjectsAdapter subjectsAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_subjects);

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

        subjectsAdapter = new SubjectsAdapter(this,getMyList());
        recyclerView.setAdapter(subjectsAdapter);

        evaluateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String kk = sName.substring(sName.length()-1);
        String b = PreferenceManager.getDefaultSharedPreferences(this).getString("b", "");
        Log.d("123"," b "+b);
        String nam = "courseCount"+kk;
        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(nam.trim(), "");

        if (isShow == null){
            Intent intent = new Intent(Subjects_Activity.this, Semesters_Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private ArrayList<Subjects> getMyList() {



        Log.d("111","sName "+sName);
        String kk = sName.substring(sName.length()-1);

        ArrayList<Subjects> subjects = new ArrayList<>();

        Subjects s;
        //String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString("courseCount", "");
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
}