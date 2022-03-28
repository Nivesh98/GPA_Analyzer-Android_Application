package com.nivacreation.gpa_analyzer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.nivacreation.gpa_analyzer.R;
import com.nivacreation.gpa_analyzer.SubjectDetails_Activity;
import com.nivacreation.gpa_analyzer.model.Semesters;
import com.nivacreation.gpa_analyzer.model.Subjects;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder> {


    Context context;
    ArrayList<Subjects> subjectsArrayList;

    Map<String,Object> user = new HashMap<>();

    Subjects subjects;

    String isValue;

    int selectedItemGradeInt;

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

        String isShow = PreferenceManager.getDefaultSharedPreferences(context).getString("sName", "");

        if (isShow != null){
            isValue = isShow;
        }

        documentReference = fStore.collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num);

        holder.number.setText(subjects.getNumber());
        holder.subjectName.setText("Subject Name "+num);
        holder.subjectCode.setText("Subject Code "+num);

        subjects.setSubjectName(holder.subjectName.getText().toString());
        subjects.setSubjectCode(holder.subjectCode.getText().toString());

        holder.subjectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                documentReference = fStore.collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num);
                //holder.subjectName.setText(holder.subjectName.getText());
                user.put("subjectName", holder.subjectName.getText().toString());
                documentReference.set(user);
           //     holder.subjectName.setText(holder.subjectName.getText());
//                user.put("subjectName", holder.subjectName.getText().toString());
//                FirebaseFirestore.getInstance().collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num).update(user);
                subjects.setSubjectName(holder.subjectName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.subjectCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                documentReference = fStore.collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num);
                //holder.subjectName.setText(holder.subjectName.getText());
                user.put("subjectCode", holder.subjectCode.getText().toString());
                documentReference.set(user);

                //holder.subjectName.setText(holder.subjectName.getText());
//                user.put("subjectCode", holder.subjectCode.getText().toString());
//                FirebaseFirestore.getInstance().collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num).update(user);
                subjects.setSubjectCode(holder.subjectCode.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.grade.setAdapter(new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,subjects.getGrade()));
        holder.grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                selectedItemGrade = parent.getItemAtPosition(position).toString();
                documentReference = fStore.collection("Subjects").document(userId).collection(num).document(num);
                documentReference = fStore.collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num);

                user.put("subjectName",holder.subjectName.getText().toString());
                user.put("subjectCode", holder.subjectCode.getText().toString());
                user.put("number",holder.number.getText().toString());
                user.put("Grade", holder.grade.getSelectedItem());
                user.put("Credit",holder.credit.getSelectedItem());
                user.put("Gpa",holder.gpa.getSelectedItem());
                user.put("number",holder.number.getText().toString());
                documentReference.set(user);
                subjects.setGetMethodGrade(selectedItemGrade);

//                subjects.setGetMethodGrade(selectedItemGrade);
//                selectedItemGradeInt = position;
//                Log.d("555","grade position inside = "+holder.grade.getAdapter().getItem(selectedItemGradeInt));
//                user.put("subjectName",holder.subjectName.getText().toString());
//                user.put("subjectCode", holder.subjectCode.getText().toString());
//                user.put("Grade", holder.grade.getSelectedItem());
//                user.put("number",holder.number.getText().toString());
//                FirebaseFirestore.getInstance().collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num).update(user);

               // Log.d("1234","Grade = "+selectedItemGrade + "num "+num+ " userid "+userId+" grade "+holder.grade.getSelectedItem());
                //Toast.makeText(context.getApplicationContext(),"Grade = "+selectedItemGrade+" Credit = "+selectedCredit+" GPA = "+selectedItemGPA,Toast.LENGTH_LONG).show();
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
                documentReference = fStore.collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num);
                subjects.setGetMethodCredit(selectedCredit);
                user.put("subjectName",holder.subjectName.getText().toString());
                user.put("subjectCode", holder.subjectCode.getText().toString());
                user.put("number",holder.number.getText().toString());
                user.put("Credit",holder.credit.getSelectedItem());
                user.put("Grade", holder.grade.getSelectedItem());
                user.put("Gpa",holder.gpa.getSelectedItem());
                documentReference.set(user);

//                subjects.setGetMethodCredit(selectedCredit);
//
//                user.put("Credit",holder.credit.getSelectedItem());
//                FirebaseFirestore.getInstance().collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num).update(user);

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
                documentReference = fStore.collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num);
                subjects.setGetMethodGPA(selectedItemGPA);
                user.put("subjectName",holder.subjectName.getText().toString());
                user.put("subjectCode", holder.subjectCode.getText().toString());
                user.put("number",holder.number.getText().toString());
                user.put("Gpa",holder.gpa.getSelectedItem());
                user.put("Credit",holder.credit.getSelectedItem());
                user.put("Grade", holder.grade.getSelectedItem());
                documentReference.set(user);


//                subjects.setGetMethodGPA(selectedItemGPA);
//
//                user.put("Gpa",holder.gpa.getSelectedItem());
//                FirebaseFirestore.getInstance().collection("Subjects").document(userId).collection("Semester").document(isValue).collection(num).document(num).update(user);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subjects.setGetMethodGrade(holder.grade.getSelectedItem().toString());
        subjects.setGetMethodCredit(holder.credit.getSelectedItem().toString());
        subjects.setGetMethodGPA(holder.gpa.getSelectedItem().toString());

        holder.subjectName.setText(subjects.getSubjectName());
        holder.subjectCode.setText(subjects.getSubjectCode());
        for(int i =0; i<holder.grade.getAdapter().getCount(); i++){


            String s = (String) holder.grade.getAdapter().getItem(i);
            if (s.equals(subjects.getGetMethodGrade())){

                holder.grade.setSelection(i);

            }
        }
        for(int i =0; i<holder.credit.getAdapter().getCount(); i++){


            String s = (String) holder.credit.getAdapter().getItem(i);
            if (s.equals(subjects.getGetMethodCredit())){

                holder.credit.setSelection(i);

            }
        }
        for(int i =0; i<holder.gpa.getAdapter().getCount(); i++){


            String s = (String) holder.gpa.getAdapter().getItem(i);
            if (s.equals(subjects.getGetMethodGPA())){

                holder.gpa.setSelection(i);

            }
        }


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

