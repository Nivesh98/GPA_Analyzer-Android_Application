package com.nivacreation.gpa_analyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.nivacreation.gpa_analyzer.R.layout.*;

public class SignIn_Activity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText txtEmailSign, txtPass, txtComPass , txtFirstName, txtlastName;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    private ProgressBar progressBar;

    String userID, userIdLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_sign_in);

        txtEmailSign = findViewById(R.id.emailSignup);
        txtPass = findViewById(R.id.passwordSignup);
        txtComPass = findViewById(R.id.confirmPassword);
        txtFirstName = findViewById(R.id.firstName);
        txtlastName = findViewById(R.id.lastName);

        progressBar = findViewById(R.id.progressBar);

        TextView loginBtn = (TextView) findViewById(R.id.loginTxt);
        Button login = (Button) findViewById(R.id.signupBtn);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //This is a newly added. this is an example.
        if (mAuth.getCurrentUser() != null){

            startActivity(new Intent(SignIn_Activity.this, FirstHome_Activity.class));
            finish();

        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInActivity = new Intent(SignIn_Activity.this, MainActivity.class);
                signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signInActivity);
                finish();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent signInActivity = new Intent(SignIn_Activity.this, FirstHome_Activity.class);
//                signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(signInActivity);
//                finish();
                createUser();
            }
        });
    }

    private void createUser()
    {

        String email = txtEmailSign.getText().toString().trim();
        String password = txtPass.getText().toString().trim();
        String comPassword = txtComPass.getText().toString().trim();
        String firstName = txtFirstName.getText().toString();
        String lastName = txtlastName.getText().toString();

            if (!firstName.isEmpty())
            {
                if(!lastName.isEmpty())
                {
                    if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    {
                        if(!password.isEmpty())
                        {
                            if (!(password.length() < 7))
                            {
                                if(!comPassword.isEmpty())
                                {
                                    if (password.equals(comPassword))
                                    {
                                        progressBar.setVisibility(View.VISIBLE);
                                        mAuth.createUserWithEmailAndPassword(email,password)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NotNull Task<AuthResult> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                            Toast.makeText(SignIn_Activity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                                            userID = mAuth.getCurrentUser().getUid();
                                                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                                                            Map<String,Object> user = new HashMap<>();
                                                            user.put("firstName",firstName);
                                                            user.put("lastName",lastName);
                                                            user.put("email",email);
                                                            user.put("userID",userID);

                                                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Log.d(TAG,"onSuccess: User profile is created for "+ userID);
                                                                }
                                                            });
                                                            // check whether what type of user is
                                                            startActivity(new Intent(SignIn_Activity.this, FirstHome_Activity.class));
                                                            finish();
                                                        }else
                                                        {
                                                            Toast.makeText(SignIn_Activity.this, "Registration Error!", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        }

                                                    }
                                                });
                                    }else
                                    {
                                        txtComPass.setError("Passwords are not matched");
                                    }

                                }else
                                {
                                    txtComPass.setError("Empty Fields Are Not Allowed");
                                }
                            }else
                            {
                                txtPass.setError("Passwords length should be >=7 !");
                            }


                        }else
                        {
                            txtPass.setError("Empty Fields Are Not Allowed");
                        }
                    }else if (email.isEmpty())
                    {
                        txtEmailSign.setError("Empty Fields Are Not Allowed");
                    }else
                    {
                        txtEmailSign.setError("Please Enter Correct Email");
                    }
                }else
                {
                    txtlastName.setError("Empty Fields Are Not Allowed");
                }

            }else
            {
                txtFirstName.setError("Empty Fields Are Not Allowed");
            }

        }


}