package com.example.smartsocket.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.smartsocket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;


public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText mEmail, mPassword;
    ProgressBar progressbar;
    FirebaseAuth fauth;

    @NonNull
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final String emailPattern = "([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})";
        final String passPattern = "([a-zA-Z0-9]{6,15})";

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        login = findViewById(R.id.login);
        fauth = FirebaseAuth.getInstance();

        progressbar = findViewById(R.id.progressBarLogIn);
        progressbar.setVisibility(View.INVISIBLE);



        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    String email = mEmail.getText().toString().trim();
                    if (email.matches(emailPattern) && email.length() > 0)
                    {
                        mEmail.setError(null);
                    }
                    else if (email.isEmpty())
                    {
                        mEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mEmail.setError("Empty email");
                    }
                    else
                    {
                        mEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mEmail.setError("Invalid email");
                    }
                }
            }
        });

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    String password = mPassword.getText().toString().trim();
                    if (password.matches(passPattern) && password.length() > 0)
                    {
                        mPassword.setError(null);

                    }
                    else if (password.isEmpty())
                    {
                        mPassword.setError("Empty Password");
                    }
                    else if(password.length() < 6){
                        mPassword.setError("Password should be at least 6 characters.");
                    }
                    else
                    {
                        mPassword.setError("Use only letters and numbers.");
                    }

                }else{
                    if(mEmail.getText().toString().trim().isEmpty()){
                        mEmail.setError("Email Required");
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                progressbar.setVisibility(View.VISIBLE);
                fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Successful" , Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MenuDisplayActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "Unsuccessful, Try again!" , Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });




    }
    public void CreateAccount(View view) {
        startActivity(new Intent(LoginActivity.this , registerActivity.class));
    }
}
