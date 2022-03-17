package com.nivacreation.gpa_analyzer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nivacreation.gpa_analyzer.R;
import com.nivacreation.gpa_analyzer.SubjectDetails_Activity;
import com.nivacreation.gpa_analyzer.Subjects_Activity;
import com.nivacreation.gpa_analyzer.model.Semesters;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemestersAdapter extends RecyclerView.Adapter<SemestersAdapter.SemesterViewHolder> {


        Context context;
        ArrayList<Semesters> semestersArrayList;

        public SemestersAdapter(Context context, ArrayList<Semesters> semestersArrayList) {
            this.context = context;
            this.semestersArrayList = semestersArrayList;

        }

        @NonNull
        @NotNull
        @Override
        public SemestersAdapter.SemesterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.list_semester_details,parent,false);
            return new SemesterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull SemestersAdapter.SemesterViewHolder holder, int position) {

            String isShow = PreferenceManager
                    .getDefaultSharedPreferences(context.getApplicationContext()).getString("subjectDetails"+position, "Empty");

            Semesters semesters = semestersArrayList.get(position);

            holder.title.setText(semesters.getTitle());

            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceManager
                            .getDefaultSharedPreferences(context).edit().putString("sName", semestersArrayList.get(position).getTitle()).apply();
                    String k ="1"+semestersArrayList.get(position).getTitle();

                    Log.d("123", "adapter k "+k);

                    if(isShow.equals(k.trim())){
                        Intent intent = new Intent(context, Subjects_Activity.class);
                        intent.putExtra("SemesterName", semestersArrayList.get(position).getTitle());
                        context.startActivity(intent);

                    }else{
                        Intent intent = new Intent(context, SubjectDetails_Activity.class);
                        intent.putExtra("SemesterName", semestersArrayList.get(position).getTitle());
                        context.startActivity(intent);
                    }


                    
                }
            });

        }

        @Override
        public int getItemCount() {

            return semestersArrayList.size();
        }

        public class SemesterViewHolder extends RecyclerView.ViewHolder{

            TextView title;

            public SemesterViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);

                this.title = itemView.findViewById(R.id.semesterBtn);

            }

        }
}
