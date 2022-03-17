package com.nivacreation.gpa_analyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private TextView txtForgetPass;
    private EditText edtEmailSignIn, edtPasswordSignIn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    String isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       isShow = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this).getString("semesterDetail", "Empty");

        txtForgetPass = findViewById(R.id.txtForgetpassword);
        edtEmailSignIn = findViewById(R.id.emailSignIn);
        edtPasswordSignIn = findViewById(R.id.passwordSignIn);
        progressBar = findViewById(R.id.progressBar);

        TextView signupBtn = (TextView) findViewById(R.id.signupTxt);
        Button login = (Button) findViewById(R.id.loginBtn);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //This is a newly added. this is an example.
        if (mAuth.getCurrentUser() != null){

            if(isShow.equals("1")){
                Intent goSplash = new Intent(MainActivity.this,Semesters_Activity.class);
                startActivity(goSplash);
                finish();
            }else{
                Intent goPassengerActivity = new Intent(MainActivity.this, FirstHome_Activity.class);
                startActivity(goPassengerActivity);
                finish();
            }
        }

        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link");
                passwordResetDialog.setView(resetMail);


                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetMail.getText().toString();

                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Reset Link Sent To Your Email. ",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(MainActivity.this, " Error ! Reset Link is Not Sent. " +e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SignIn_Activity.class));

            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValidation();
//                Intent signInActivity = new Intent(MainActivity.this, FirstHome_Activity.class);
//                signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(signInActivity);
//                finish();
            }
        });
    }

    private void checkValidation() {
        String email = edtEmailSignIn.getText().toString().trim();
        String password = edtPasswordSignIn.getText().toString().trim();


        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!password.isEmpty()) {
                if (!(password.length() < 7))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {

                                checkGoActivity();
                            }else
                            {
                                Toast.makeText(MainActivity.this, "Login Error !", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                    });

                }else
                {
                    edtPasswordSignIn.setError("tPasswords length should be >=7 !");
                }

            }else
            {
                edtPasswordSignIn.setError("Empty Fields Are Not Allowed !");
            }
        }else if (email.isEmpty())
        {
            edtEmailSignIn.setError("Empty Fields Are Not Allowed !");
        }else
        {
            edtEmailSignIn.setError("Please Enter Correct Email !");
        }
    }
    private void checkGoActivity() {



        if(isShow.equals("1")){

            Intent goSplash = new Intent(MainActivity.this,Semesters_Activity.class);
            startActivity(goSplash);
            finish();

        }else{
            Intent goPassengerActivity = new Intent(MainActivity.this, FirstHome_Activity.class);
            goPassengerActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            goPassengerActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(goPassengerActivity);
            finish();
            Toast.makeText(MainActivity.this, "Login Successfully !", Toast.LENGTH_SHORT).show();
        }

    }
}