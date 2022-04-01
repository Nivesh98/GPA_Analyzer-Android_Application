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
import android.widget.Toast;

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
import com.nivacreation.gpa_analyzer.model.GetGradePointValue;
import com.nivacreation.gpa_analyzer.model.Semesters;
import com.nivacreation.gpa_analyzer.model.Subjects;
import com.nivacreation.gpa_analyzer.model.SubjectsNew;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static com.nivacreation.gpa_analyzer.R.layout.*;

public class Subjects_Activity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    RecyclerView recyclerView;

    ArrayList<Subjects> subjectsArrayList;
    ArrayList<SubjectsNew> subjectsNewArrayList;

    SubjectsAdapter subjectsAdapter;
    SubjectsNewAdapter subjectsNewAdapter;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    private static final DecimalFormat df = new DecimalFormat("0.0000");

    Map<String,Object> user = new HashMap<>();

    static Subjects subject;

    String sName;

    String countGetValue;

    DocumentReference documentReference;

    int countValueToInt;

    Button evaluateBtn;

    double sum = 0,sums=0,sumss=0,
            tots=0,totcs=0,totcss=0,totss=0,
            gpaValues=0,gpaValuess=0,
            tot = 0, totC =0, gpaValue =0;

    GetGradePointValue getGradePointValue;
    //for swip
    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static  int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_subjects);

        getGradePointValue = new GetGradePointValue();

        this.gestureDetector = new GestureDetector(Subjects_Activity.this,this);

        recyclerView = findViewById(R.id.recyclerSubject);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        evaluateBtn = findViewById(R.id.evaluateBtn);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        sName = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+"sName", "");

        if (fAuth.getCurrentUser().getUid() != null){
            FirebaseFirestore.getInstance().collection("Subjects").document(userId.trim()).delete();
        }

        subjectsArrayList = new ArrayList<Subjects>();
        subjectsNewArrayList = new ArrayList<SubjectsNew>();

        subjectsAdapter = new SubjectsAdapter(this,getMyList());
        subjectsNewAdapter = new SubjectsNewAdapter(this, subjectsNewArrayList);

        String checkValue = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+"iGetSubDetail", "");

        Log.d("1245"," checkValue "+checkValue);

        //

        if (checkValue.equals("*")){
            recyclerView.setAdapter(subjectsAdapter);
        }

        if (checkValue.equals("#")){
            recyclerView.setAdapter(subjectsNewAdapter);
            eventChangeListner();
        }

        evaluateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getResult();
                sum = 0;
                tot = 0;
                totC =0;
                gpaValue =0;

                sums =0;
                totcs =0;
                tots =0;
                gpaValues=0;

                sumss =0;
                totcss =0;
                totss =0;
                gpaValuess=0;

            }
        });




    }

    private void getResult() {

        Log.d("852","Inside getResult Method");
        sum = 0;
        tot = 0;
        totC =0;
        gpaValue =0;
        fStore = FirebaseFirestore.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        Log.d("111","sName "+sName);
        String kk = sName.substring(sName.length()-1);

        int kkk = Integer.parseInt(kk);

        Log.d("1111", " kkk "+kkk);
        for (int ii=1; ii<=kkk; ii++){

            Log.d("852","Inside getResult Method - for loop");
            //String b = PreferenceManager.getDefaultSharedPreferences(this).getString("b", "");

            //Log.d("123"," b "+b);
            String nam = "courseCount"+ii;
            String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+nam.trim(), "");
            Log.d("1111", " ii "+ii+" isShow "+isShow);
           // Log.d("1111","isShowk "+ isShowk +" kkk = "+kkk+ " isShow "+isShow+" ii "+ii);

            countGetValue = isShow;

            if (isShow != null){
                Log.d("852","Inside getResult Method - for loop - if function");
                countValueToInt = Integer.parseInt(countGetValue);
                sName = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+"sName", "");

                String[] subName = new String[countValueToInt];
                String[] subCode = new String[countValueToInt];
                String[] subGrade = new String[countValueToInt];
                String[] subCredit = new String[countValueToInt];
                String[] subGPA = new String[countValueToInt];
                String[] subNum = new String[countValueToInt];
                final int y = countValueToInt-1;
                for(int i=1; i<=countValueToInt; i++){
                    Log.d("852","Inside getResult Method - for loop - if function - for loop");

                    DocumentReference documentReference = fStore.collection("Subjects").document(userId).collection("Semester")
                            .document("Semester "+ii).collection(String.valueOf(i)).document(String.valueOf(i));
                    int finalI = i;

                    documentReference.addSnapshotListener(Subjects_Activity.this, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable @org.jetbrains.annotations
                                .Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                            if (value != null && value.exists()) {

                                Log.d("852","Inside getResult Method - for loop - if function - for loop - document inside");
                                String sN = value.getString("subjectName");
                                String sC = value.getString("subjectCode");
                                String sNum = value.getString("number");
                                String sGra = value.getString("Grade");
                                String sCre = value.getString("Credit");
                                String sGpa = value.getString("Gpa");

                                int a = finalI -1;
                                int z = y;
                                subName[a] = sN;
                                subCode[a] = sC;
                                subGPA[a] = sGpa;
                                subGrade[a] = sGra;
                                subNum[a] = sNum;
                                subCredit[a] = sCre;

                                getDataFromDatabaseSG(subName,subCode,subGPA,subGrade, subNum, subCredit,a,z);

                                if (finalI==countValueToInt){
                                    return;

                                }

                            }
                        }
                    });

                }

                if(((kkk-1) !=0)&&(ii==(kkk-1))&&(kkk%2==0)){

                    Log.d("753","inside ygpa loop kkk = "+kkk);
                    sumss = 0;
                    totss = 0;
                    totcss =0;
                    gpaValuess =0;
                    String namk = "courseCount"+(kkk-1);

                    String isShowk = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+namk.trim(), "");

                    //Log.d("753", "kkk")

                    Log.d("1111", "ii==kkk kkk "+(kkk-1)+" isShowk "+isShowk+ " ii "+ii+" isShow "+isShow);
                    if (isShowk != null){

                        Log.d("753","inside ygpa loop inside if 1");
                        int hh = Integer.parseInt(isShowk);
                        String[] subNamey = new String[hh];
                        String[] subCodey = new String[hh];
                        String[] subGradey = new String[hh];
                        String[] subCredity = new String[hh];
                        String[] subGPAy = new String[hh];
                        String[] subNumy = new String[hh];

                            String namkk = "courseCount"+(kkk);

                            String isShowkk = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+namkk.trim(), "");

                            if (isShowkk != null){
                                Log.d("753","inside ygpa loop inside if 2");
                                int hhh = Integer.parseInt(isShowkk);
                                String[] subNameyy = new String[hhh];
                                String[] subCodeyy = new String[hhh];
                                String[] subGradeyy = new String[hhh];
                                String[] subCredityy = new String[hhh];
                                String[] subGPAyy = new String[hhh];
                                String[] subNumyy = new String[hhh];
                                for (int ll=(kkk-1); ll<=kkk; ll++){

                                    Log.d("753","inside ygpa loop inside for 1");
                                    //Log.d("753","inside ygpa loop inside for 1 aaa = "+aaa);

                                    if (ll==(kkk-1)){
                                        for(int i=1; i<=hh; i++){
                                            Log.d("753","inside ygpa loop inside for 2"+" hh = "+hh+" ll = "+ll+" kkk "+kkk);
                                            DocumentReference documentReference = fStore.collection("Subjects").document(userId).collection("Semester")
                                                    .document("Semester "+(kkk-1)).collection(String.valueOf(i)).document(String.valueOf(i));
                                            int finalI = i;
                                            int jjj = kkk-1;
                                            Log.d("753","inside ygpa loop inside for ii  = "+ii);
                                            documentReference.addSnapshotListener(Subjects_Activity.this, new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable @org.jetbrains.annotations
                                                        .Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                                                    if (value != null && value.exists()) {
                                                        Log.d("753","inside ygpa loop inside document");
                                                        String sN = value.getString("subjectName");
                                                        String sC = value.getString("subjectCode");
                                                        String sNum = value.getString("number");
                                                        String sGra = value.getString("Grade");
                                                        String sCre = value.getString("Credit");
                                                        String sGpa = value.getString("Gpa");

                                                        int a = finalI -1;
                                                        int z = hh-1;
                                                        subNamey[a] = sN;
                                                        subCodey[a] = sC;
                                                        subGPAy[a] = sGpa;
                                                        subGradey[a] = sGra;
                                                        subNumy[a] = sNum;
                                                        subCredity[a] = sCre;

                                                        String jj = "Semester "+String.valueOf(jjj);

                                                        getDataFromDatabaseYG(subNamey,subCodey,subGPAy,subGradey, subNumy, subCredity,a,z,jj);

                                                        if (finalI==hh){
                                                            return;

                                                        }

                                                    }
                                                }
                                            });

                                        }
                                    }else if (ll==kkk){
                                        for(int i=1; i<=hhh; i++){
                                            Log.d("753","inside ygpa loop inside for 2"+" hhh = "+hhh+" ll = "+ll+" kkk "+kkk);
                                            DocumentReference documentReference = fStore.collection("Subjects").document(userId).collection("Semester")
                                                    .document("Semester "+kkk).collection(String.valueOf(i)).document(String.valueOf(i));
                                            int finalI = i;
                                            int jjj = kkk;
                                            Log.d("753","inside ygpa loop inside for ii  = "+ii);
                                            documentReference.addSnapshotListener(Subjects_Activity.this, new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable @org.jetbrains.annotations
                                                        .Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                                                    if (value != null && value.exists()) {
                                                        Log.d("753","inside ygpa loop inside document");
                                                        String sN = value.getString("subjectName");
                                                        String sC = value.getString("subjectCode");
                                                        String sNum = value.getString("number");
                                                        String sGra = value.getString("Grade");
                                                        String sCre = value.getString("Credit");
                                                        String sGpa = value.getString("Gpa");

                                                        int a = finalI -1;
                                                        int z = hhh-1;
                                                        Log.d("753"," i = "+finalI+" a "+a);
                                                        subNameyy[a] = sN;
                                                        subCodeyy[a] = sC;
                                                        subGPAyy[a] = sGpa;
                                                        subGradeyy[a] = sGra;
                                                        subNumyy[a] = sNum;
                                                        subCredityy[a] = sCre;

                                                        String jj = "Semester "+String.valueOf(jjj);

                                                        getDataFromDatabaseYG(subNameyy,subCodeyy,subGPAyy,subGradeyy, subNumyy, subCredityy,a,z,jj);

                                                        if (finalI==hhh){
                                                            return;

                                                        }

                                                    }
                                                }
                                            });

                                        }

                                    }

                                }

                            }



                    }

                }
                if (ii==kkk){
                    sums = 0;
                    tots = 0;
                    totcs =0;
                    gpaValues =0;
                    String namk = "courseCount"+kkk;
                    String isShowk = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+namk.trim(), "");

                    Log.d("1111", "ii==kkk kkk "+kkk+" isShowk "+isShowk+ " ii "+ii+" isShow "+isShow);
                    if (isShowk != null){
                        int hh = Integer.parseInt(isShowk);
                        for(int i=1; i<=hh; i++){

                            DocumentReference documentReference = fStore.collection("Subjects").document(userId).collection("Semester")
                                    .document("Semester "+ii).collection(String.valueOf(i)).document(String.valueOf(i));
                            int finalI = i;
                            int jjj = ii;

                            documentReference.addSnapshotListener(Subjects_Activity.this, new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable @org.jetbrains.annotations
                                        .Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                                    if (value != null && value.exists()) {

                                        String sN = value.getString("subjectName");
                                        String sC = value.getString("subjectCode");
                                        String sNum = value.getString("number");
                                        String sGra = value.getString("Grade");
                                        String sCre = value.getString("Credit");
                                        String sGpa = value.getString("Gpa");

                                        int a = finalI -1;
                                        int z = y;
                                        subName[a] = sN;
                                        subCode[a] = sC;
                                        subGPA[a] = sGpa;
                                        subGrade[a] = sGra;
                                        subNum[a] = sNum;
                                        subCredit[a] = sCre;

                                        String jj = "Semester "+String.valueOf(jjj);

                                        getDataFromDatabase(subName,subCode,subGPA,subGrade, subNum, subCredit,a,z,jj);

                                        if (finalI==hh){
                                            return;

                                        }

                                    }
                                }
                            });

                        }
                    }


                }
            }

        }

    }

    private void getDataFromDatabaseYG(String[] subName, String[] subCode, String[] subGPA, String[] subGrade, String[] subNum, String[] subCredit, int a, int z, String jj) {
        Log.d("753","inside ygpa loop inside getdataFromDatabaseYG");
        Log.d("753"," a & z = "+a+" "+z);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("GradesValue").document(userId);
        documentReference.addSnapshotListener( Subjects_Activity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations
                    .Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if (value != null && value.exists()) {
                    Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document");
                    String g = subGrade[a];
                    String gpa = subGPA[a];
                    String cret = subCredit[a];
                    double cr = Double.parseDouble(cret);

                    if (gpa.equals("GPA")){
                        Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document if 1");
                        if (g.equals("A+")){
                            Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document grade = "+g);
                            String v = value.getString("APV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            totss = cc*vc;
                            sumss = sumss +totss;

                            // Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("A")){
                            Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document grade = "+g);
                            String v = value.getString("AV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            totss = cc*vc;
                            sumss = sumss +totss;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("A-")){
                            Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document grade = "+g);
                            String v = value.getString("AMV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            totss = cc*vc;
                            sumss = sumss +totss;

                            // Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("B+")){
                            Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document grade = "+g);
                            String v = value.getString("BPV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            totss = cc*vc;
                            sumss = sumss +totss;

                            // Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("B")){
                            Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document grade = "+g);
                            String v = value.getString("BV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            totss = cc*vc;
                            sumss = sumss +totss;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("B-")){
                            Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document grade = "+g);
                            String v = value.getString("BMV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            totss = cc*vc;
                            sumss = sumss +totss;

                            // Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("C+")){
                            Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document grade = "+g);
                            String v = value.getString("CPV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            totss = cc*vc;
                            sumss = sumss +totss;

                            // Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("C")){
                            Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document grade = "+g);
                            String v = value.getString("CV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            totss = cc*vc;
                            sumss = sumss +totss;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("C-")){
                            Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document grade = "+g);
                            String v = value.getString("CMV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            totss = cc*vc;
                            sumss = sumss +totss;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("D+")){
                            Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document grade = "+g);
                            String v = value.getString("DPV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            totss = cc*vc;
                            sumss = sumss +totss;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else {

                        }

                        totcss = totcss+ cr;
                        Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document if 1 totcss = "+totcss);
                        //Toast.makeText(Subjects_Activity.this, "tot cre  "+totcs,Toast.LENGTH_SHORT).show();

                        gpaValuess = sumss/totcss;
                        Log.d("753","inside ygpa loop inside getdataFromDatabaseYG document if 1 gpaValuess = "+gpaValuess);
                        //df.setRoundingMode(RoundingMode.UP);
                        getGradePointValue.setGradeValue(gpaValuess);
                        if (a==z){
                            //Toast.makeText(Subjects_Activity.this, "inside  ",Toast.LENGTH_SHORT).show();
                            PreferenceManager
                                    .getDefaultSharedPreferences(Subjects_Activity.this).edit().putString(userId+"isGpaValYGPA"+jj, String.valueOf(gpaValuess)).apply();
                            Log.d("753","gpaValuess = "+gpaValuess);
                            Log.d("753","gpaValuess jj = "+jj);

                        }
                        //Toast.makeText(Subjects_Activity.this, "gpa Value  "+gpaValues,Toast.LENGTH_SHORT).show();
                    }




                }
            }
        });
        //String k = subGrade[a];
        // Toast.makeText(this," k = "+k+" grade value = ",Toast.LENGTH_SHORT).show();

    }

    private void getDataFromDatabase(String[] subName, String[] subCode, String[] subGPA, String[] subGrade, String[] subNum, String[] subCredit, int a, int z, String jj) {

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("GradesValue").document(userId);
        documentReference.addSnapshotListener( Subjects_Activity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations
                    .Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if (value != null && value.exists()) {

                    String g = subGrade[a];
                    String gpa = subGPA[a];
                    String cret = subCredit[a];
                    double cr = Double.parseDouble(cret);

                    if (gpa.equals("GPA")){

                        if (g.equals("A+")){

                            String v = value.getString("APV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tots = cc*vc;
                            sums = sums +tots;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("A")){
                            String v = value.getString("AV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                             double vc = Double.parseDouble(v);

                            tots = cc*vc;
                            sums = sums +tots;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("A-")){
                            String v = value.getString("AMV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tots = cc*vc;
                            sums = sums +tots;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("B+")){
                            String v = value.getString("BPV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tots = cc*vc;
                            sums = sums +tots;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("B")){
                            String v = value.getString("BV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tots = cc*vc;
                            sums = sums +tots;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("B-")){
                            String v = value.getString("BMV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tots = cc*vc;
                            sums = sums +tots;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("C+")){
                            String v = value.getString("CPV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tots = cc*vc;
                            sums = sums +tots;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("C")){
                            String v = value.getString("CV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tots = cc*vc;
                            sums = sums +tots;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("C-")){
                            String v = value.getString("CMV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tots = cc*vc;
                            sums = sums +tots;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("D+")){
                            String v = value.getString("DPV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tots = cc*vc;
                            sums = sums +tots;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sums,Toast.LENGTH_SHORT).show();

                        }else {

                        }

                        totcs = totcs+ cr;
                        //Toast.makeText(Subjects_Activity.this, "tot cre  "+totcs,Toast.LENGTH_SHORT).show();

                        gpaValues = sums/totcs;
                        //df.setRoundingMode(RoundingMode.UP);
                        getGradePointValue.setGradeValue(gpaValues);
                        if (a==z){
                            //Toast.makeText(Subjects_Activity.this, "inside  ",Toast.LENGTH_SHORT).show();
                            PreferenceManager
                                    .getDefaultSharedPreferences(Subjects_Activity.this).edit().putString(userId+"isGpaVal"+jj, String.valueOf(gpaValues)).apply();
                            Intent intent = new Intent(Subjects_Activity.this, ResultSheet.class);
                            intent.putExtra("gpaValue",g);
                            startActivity(intent);
                            finish();
                        }
                        //Toast.makeText(Subjects_Activity.this, "gpa Value  "+gpaValues,Toast.LENGTH_SHORT).show();
                    }




                }
            }
        });
                      //String k = subGrade[a];
           // Toast.makeText(this," k = "+k+" grade value = ",Toast.LENGTH_SHORT).show();

    }

    private void getDataFromDatabaseSG(String[] subName, String[] subCode, String[] subGPA, String[] subGrade, String[] subNum, String[] subCredit, int a, int z) {

        Log.d("852","Inside getResult Method - for loop - if function - for loop - document inside-" +
                "inside getDataFromDatabaseSG");

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("GradesValue").document(userId);
        documentReference.addSnapshotListener( Subjects_Activity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations
                    .Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if (value != null && value.exists()) {
                    Log.d("852","Inside getResult Method - for loop - if function - for loop - document inside-" +
                            "inside getDataFromDatabaseSG inside document");
                    String g = subGrade[a];
                    String gpa = subGPA[a];
                    String cret = subCredit[a];
                    double cr = Double.parseDouble(cret);

                    if (gpa.equals("GPA")){

                        if (g.equals("A+")){

                            String v = value.getString("APV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tot = cc*vc;
                            sum = sum +tot;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sum,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("A")){
                            String v = value.getString("AV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tot = cc*vc;
                            sum = sum +tot;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sum,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("A-")){
                            String v = value.getString("AMV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tot = cc*vc;
                            sum = sum +tot;

                            //Toast.makeText(Subjects_Activity.this, "sum "+sum,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("B+")){
                            String v = value.getString("BPV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tot = cc*vc;
                            sum = sum +tot;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sum,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("B")){
                            String v = value.getString("BV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tot = cc*vc;
                            sum = sum +tot;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sum,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("B-")){
                            String v = value.getString("BMV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tot = cc*vc;
                            sum = sum +tot;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sum,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("C+")){
                            String v = value.getString("CPV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tot = cc*vc;
                            sum = sum +tot;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sum,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("C")){
                            String v = value.getString("CV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tot = cc*vc;
                            sum = sum +tot;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sum,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("C-")){
                            String v = value.getString("CMV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tot = cc*vc;
                            sum = sum +tot;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sum,Toast.LENGTH_SHORT).show();

                        }else if (g.equals("D+")){
                            String v = value.getString("DPV");
                            String c = subCredit[a];
                            double cc = Double.parseDouble(c);
                            double vc = Double.parseDouble(v);

                            tot = cc*vc;
                            sum = sum +tot;

                           // Toast.makeText(Subjects_Activity.this, "sum "+sum,Toast.LENGTH_SHORT).show();

                        }else {

                        }

                        totC = totC+ cr;
                       // Toast.makeText(Subjects_Activity.this, "tot cre  "+totC,Toast.LENGTH_SHORT).show();

                        gpaValue = sum/totC;
                        //df.setRoundingMode(RoundingMode.UP);
                        getGradePointValue.setGradeValue(gpaValue);
                        if (a==z){
                            Log.d("852","Inside getResult Method - for loop - if function - for loop - document inside-" +
                                    "inside getDataFromDatabaseSG get GPA value");
                            //Toast.makeText(Subjects_Activity.this, "inside  ",Toast.LENGTH_SHORT).show();
                            PreferenceManager
                                    .getDefaultSharedPreferences(Subjects_Activity.this).edit().putString(userId+"isGpaValFinal", String.valueOf(gpaValue)).apply();

                            PreferenceManager
                                    .getDefaultSharedPreferences(Subjects_Activity.this).edit()
                                    .putString(userId+"isGpaValFinalToCre", String.valueOf(totC)).apply();

                            PreferenceManager
                                    .getDefaultSharedPreferences(Subjects_Activity.this).edit()
                                    .putString(userId+"isGpaValFinalTotalS", String.valueOf(sum)).apply();

                            Intent intent = new Intent(Subjects_Activity.this, ResultSheet.class);
                            intent.putExtra("gpaValue",g);
                            startActivity(intent);
                        }
                       // Toast.makeText(Subjects_Activity.this, "gpa Value  "+gpaValue,Toast.LENGTH_SHORT).show();
                    }




                }
            }
        });
        //String k = subGrade[a];
        // Toast.makeText(this," k = "+k+" grade value = ",Toast.LENGTH_SHORT).show();

    }

    private void eventChangeListner() {

        fStore = FirebaseFirestore.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        Log.d("111","sName "+sName);
        String kk = sName.substring(sName.length()-1);

        Subjects s;
        String b = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+"b", "");

        Log.d("123"," b "+b);
        String nam = "courseCount"+kk;
        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+nam.trim(), "");

        countGetValue = isShow;

        if (isShow != null){
            countValueToInt = Integer.parseInt(countGetValue);
        }
        sName = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+"sName", "");
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

        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        String kk = sName.substring(sName.length()-1);
        String b = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+"b", "");
        Log.d("123"," b "+b);
        String nam = "courseCount"+kk;
        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+nam.trim(), "");

        if (isShow != null){
            Intent intent = new Intent(Subjects_Activity.this, Semesters_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private ArrayList<Subjects> getMyList() {

        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        Log.d("111","sName "+sName);
        String kk = sName.substring(sName.length()-1);

        ArrayList<Subjects> subjects = new ArrayList<>();

        Subjects s;
        String b = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+"b", "");

        Log.d("123"," b "+b);
        String nam = "courseCount"+kk;
        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+nam.trim(), "");

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
        sName = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+"sName", "");
        Log.d("111","sName "+sName);
        String kk = sName.substring(sName.length()-1);
        Subjects s;
        String b = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+"b", "");

        Log.d("123"," b "+b);
        String nam = "courseCount"+kk;
        String isShow = PreferenceManager.getDefaultSharedPreferences(this).getString(userId+nam.trim(), "");

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