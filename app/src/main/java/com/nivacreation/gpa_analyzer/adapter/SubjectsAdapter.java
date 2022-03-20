package com.nivacreation.gpa_analyzer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nivacreation.gpa_analyzer.R;
import com.nivacreation.gpa_analyzer.SubjectDetails_Activity;
import com.nivacreation.gpa_analyzer.model.Semesters;
import com.nivacreation.gpa_analyzer.model.Subjects;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder> {


    Context context;
    ArrayList<Subjects> subjectsArrayList;

    Map<String,Object> user = new HashMap<>();

    static Subjects subjects;

    DocumentReference documentReference;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    static String selectedItemGrade = "", selectedCredit = "", selectedItemGPA = "";

    public SubjectsAdapter(Context context, ArrayList<Subjects> subjectsArrayList) {
        this.context = context;
        this.subjectsArrayList = subjectsArrayList;

    }

    @NonNull
    @NotNull
    @Override
    public SubjectsAdapter.SubjectsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_subject_details,parent,false);
        return new SubjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubjectsAdapter.SubjectsViewHolder holder, int position) {

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        subjects = subjectsArrayList.get(position);
        String num = subjects.getNumber();

        documentReference = fStore.collection("Subjects").document(userId).collection(num).document(num);

        holder.number.setText(subjects.getNumber());
        holder.subjectName.setText("Subject Name "+num);
        holder.subjectCode.setText("Subject Code "+num);
        holder.grade.setAdapter(new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,subjects.getGrade()));
        holder.grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                selectedItemGrade = parent.getItemAtPosition(position).toString();
                documentReference = fStore.collection("Subjects").document(userId).collection(num).document(num);
                subjects.setGetMethodGrade(selectedItemGrade);
                user.put("Grade", holder.grade.getSelectedItem());
                documentReference.set(user);

                Log.d("1234","Grade = "+selectedItemGrade + "num "+num+ " userid "+userId+" grade "+holder.grade.getSelectedItem());
                Toast.makeText(context.getApplicationContext(),"Grade = "+selectedItemGrade+" Credit = "+selectedCredit+" GPA = "+selectedItemGPA,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.credit.setAdapter(new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,subjects.getCredit()));
        holder.credit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                selectedCredit = parent.getItemAtPosition(position).toString();
                documentReference = fStore.collection("Subjects").document(userId).collection(num).document(num);
                subjects.setGetMethodCredit(selectedCredit);

                user.put("Credit",holder.credit.getSelectedItem());
                documentReference.set(user);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.gpa.setAdapter(new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,subjects.getGpa()));
        holder.gpa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                selectedItemGPA = parent.getItemAtPosition(position).toString();
                documentReference = fStore.collection("Subjects").document(userId).collection(num).document(num);
                subjects.setGetMethodGPA(selectedItemGPA);

                user.put("Gpa",holder.gpa.getSelectedItem());
                documentReference.set(user);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        user.put("Grade", holder.grade.getSelectedItem());
//        user.put("Credit",holder.credit.getSelectedItem());
//        user.put("Gpa",holder.gpa.getSelectedItem());
//        documentReference.set(user);
        subjects.setGetMethodGrade(holder.grade.getSelectedItem().toString());
        subjects.setGetMethodCredit(holder.credit.getSelectedItem().toString());
        subjects.setGetMethodGPA(holder.gpa.getSelectedItem().toString());
        //Toast.makeText(context.getApplicationContext(),"Grade = "+selectedItemGrade+" Credit = "+selectedCredit+" GPA = "+selectedItemGPA,Toast.LENGTH_LONG).show();
        Log.d("1234","Grade = "+selectedItemGrade+" Grade"+subjects.getGetMethodGrade());
    }

    @Override
    public int getItemCount() {

        return subjectsArrayList.size();
    }

    public class SubjectsViewHolder extends RecyclerView.ViewHolder{

        TextView number;
       Spinner grade, credit, gpa;
       EditText subjectName, subjectCode;
      // Button evaluateBtn;

        public SubjectsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.number = itemView.findViewById(R.id.subjectNumberBtn);
            this.grade = itemView.findViewById(R.id.gradeSpinner);
            this.credit = itemView.findViewById(R.id.creditSpinner);
            this.gpa = itemView.findViewById(R.id.gpaSpinner);

            this.subjectCode = itemView.findViewById(R.id.subjectCodeBtn);
            this.subjectName = itemView.findViewById(R.id.subjectNameBtn);

        }

    }

    public void filterList(ArrayList<Subjects> filterList){

        subjectsArrayList = filterList;
        notifyDataSetChanged();
    }
}

