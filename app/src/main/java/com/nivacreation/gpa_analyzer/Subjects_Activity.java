package com.nivacreation.gpa_analyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nivacreation.gpa_analyzer.adapter.SemestersAdapter;
import com.nivacreation.gpa_analyzer.adapter.SubjectsAdapter;
import com.nivacreation.gpa_analyzer.model.Semesters;
import com.nivacreation.gpa_analyzer.model.Subjects;

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

    DocumentReference documentReference;

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

        subjectsArrayList = new ArrayList<Subjects>();

        subjectsAdapter = new SubjectsAdapter(this,getMyList());
        recyclerView.setAdapter(subjectsAdapter);

        evaluateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ArrayList<Subjects> s = new ArrayList<>();
//
//               for (Subjects items : subjectsArrayList){
//
//                   s.add(items);
//               }
//
//               subjectsAdapter.filterList(s);
            }
        });


    }

    private ArrayList<Subjects> getMyList() {

        ArrayList<Subjects> subjects = new ArrayList<>();

        Subjects s;


        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString("courseCount", "");

        String countGetValue = isShow;

        if (isShow != null){
            countValueToInt = Integer.parseInt(countGetValue);
        }

        for(int i=1; i<=countValueToInt; i++){

            s = new Subjects();
            s.setNumber(String.valueOf(i));
//            s.setGetMethodGPA(s.getGetMethodGPA());
//            s.setGetMethodCredit(s.getGetMethodCredit());
//            s.setGetMethodGrade(s.getGetMethodGrade());
           subjects.add(s);
        }
//        user.put("Grade",subject.getGetMethodGrade());
//        user.put("Credit",subject.getGetMethodCredit());
//        user.put("Gpa",subject.getGetMethodGPA());
//        documentReference.set(user);

        return subjects;
    }
}