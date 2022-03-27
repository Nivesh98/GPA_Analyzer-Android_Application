package com.nivacreation.gpa_analyzer.model;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.nivacreation.gpa_analyzer.Subjects_Activity;

import java.util.concurrent.Executor;

public class GetGradePointValue {

    String grade;
    double gradeValue;

    public double getGradeValue() {

        return gradeValue;
    }
}
