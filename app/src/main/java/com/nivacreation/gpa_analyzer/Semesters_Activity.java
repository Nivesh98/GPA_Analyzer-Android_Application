package com.nivacreation.gpa_analyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nivacreation.gpa_analyzer.adapter.SemestersAdapter;
import com.nivacreation.gpa_analyzer.model.Semesters;

import java.util.ArrayList;

public class Semesters_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Semesters> semestersArrayList;
    SemestersAdapter semestersAdapter;

    Button logoutBtn;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String vui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesters);

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


    private ArrayList<Semesters> getMyList() {

        ArrayList<Semesters> semesters = new ArrayList<>();

        Semesters s;


        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString("isShow", "");

        String countGetValue = isShow;

        int countValueToInt = Integer.parseInt(countGetValue);

        for(int i=1; i<=countValueToInt; i++){

            s = new Semesters();
            s.setTitle("Semester "+i);
            semesters.add(s);
        }

        return semesters;
    }
}