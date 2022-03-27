package com.nivacreation.gpa_analyzer.adapter;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nivacreation.gpa_analyzer.R;
import com.nivacreation.gpa_analyzer.model.Subjects;
import com.nivacreation.gpa_analyzer.model.SubjectsNew;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubjectsNewAdapter extends RecyclerView.Adapter<SubjectsNewAdapter.SubjectsNewViewHolder> {


    Context context;
    ArrayList<SubjectsNew> subjectsArrayList;
    SubjectsNew subjects;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    DocumentReference documentReference;

    String isValue;

    Map<String,Object> user = new HashMap<>();

    public SubjectsNewAdapter(Context context, ArrayList<SubjectsNew> subjectsArrayList) {
        this.context = context;
        this.subjectsArrayList = subjectsArrayList;

    }

    @NonNull
    @NotNull
    @Override
    public SubjectsNewAdapter.SubjectsNewViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_subject_details,parent,false);
        return new SubjectsNewAdapter.SubjectsNewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubjectsNewAdapter.SubjectsNewViewHolder holder, int position) {

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        SubjectsNew subjects = subjectsArrayList.get(position);

        holder.number.setText(subjects.getNumber());
        String num = subjects.getNumber();
        holder.subjectCode.setText(subjects.getSubjectCode());
        holder.subjectName.setText(subjects.getSubjectName());

        String isShow = PreferenceManager.getDefaultSharedPreferences(context).getString("sName", "");

        if (isShow != null){
            isValue = isShow;
        }
        documentReference = fStore.collection("Subjects").document(userId)
                .collection("Semester").document(isValue).collection(num).document(num);
        holder.subjectCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                String n = holder.number.getText().toString();
//                String code =holder.subjectCode.getText().toString();
//                user.put("subjectCode",code );
//                FirebaseFirestore.getInstance().collection("Subjects").document(userId).collection("Semester").document(isValue).collection(n).document(n).update(user);
//                subjects.setSubjectCode(code);


            }
        });
        subjects.setSubjectName(holder.subjectName.getText().toString());
        subjects.setSubjectCode(holder.subjectCode.getText().toString());

        holder.grade.setAdapter(new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,subjects.getGradeArray()));

        for(int i =0; i<holder.grade.getAdapter().getCount(); i++){


            String s = (String) holder.grade.getAdapter().getItem(i);
            if (s.equals(subjects.getGrade())){

                holder.grade.setSelection(i);

            }
        }

        holder.credit.setAdapter(new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,subjects.getCreditArray()));

        for(int i =0; i<holder.credit.getAdapter().getCount(); i++){


            String s = (String) holder.credit.getAdapter().getItem(i);
            if (s.equals(subjects.getCredit())){

                holder.credit.setSelection(i);

            }
        }

        holder.gpa.setAdapter(new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,subjects.getGpaArray()));

        for(int i =0; i<holder.gpa.getAdapter().getCount(); i++){


            String s = (String) holder.gpa.getAdapter().getItem(i);
            if (s.equals(subjects.getGpa())){

                holder.gpa.setSelection(i);

            }
        }

        holder.subjectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.gpa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                String selectedItemGPA = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.credit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                String selectedCredit = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                String selectedItemGrade = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public int getItemCount() {

        return subjectsArrayList.size();
    }

    public class SubjectsNewViewHolder extends RecyclerView.ViewHolder{

        TextView number;
        Spinner grade, credit, gpa;
        EditText subjectName, subjectCode;

        public SubjectsNewViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.number = itemView.findViewById(R.id.subjectNumberBtn);
            this.grade = itemView.findViewById(R.id.gradeSpinner);
            this.credit = itemView.findViewById(R.id.creditSpinner);
            this.gpa = itemView.findViewById(R.id.gpaSpinner);

            this.subjectCode = itemView.findViewById(R.id.subjectCodeBtn);
            this.subjectName = itemView.findViewById(R.id.subjectNameBtn);

        }

    }
}

