package com.example.smartsocket.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartsocket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class registerActivity extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationID;
    private String phoneNumber = "";

    EditText mFirstName ,  mLastName , mPhoneNumber , mPassword , mRePassword  , emailValidate ;
    ProgressBar mProgressbar , mVerificationProgressbar;
    FirebaseAuth fAuth;
    Button mRegisterButton;
    SignInMethodQueryResult  validity;
    Dialog verificationWindow;
    Boolean codeVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstName =  findViewById(R.id.signup_input_first_name);
        mLastName =  findViewById(R.id.signup_input_last_name);
        mPhoneNumber = findViewById(R.id.signup_input_phone_name);
        mPassword =  findViewById(R.id.signup_input_password);
        mRePassword =  findViewById(R.id.signup_input_reenter_password);
        emailValidate = findViewById(R.id.signup_input_email);

        mProgressbar = findViewById(R.id.progressBar2);
        mVerificationProgressbar = findViewById(R.id.progressBar4);

        codeVerified = false;

        mRegisterButton = findViewById(R.id.btn_Register);

        fAuth = FirebaseAuth.getInstance();

        mProgressbar.setVisibility(View.GONE);
        mVerificationProgressbar.setVisibility(View.GONE);

        final String emailPattern = "([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})";
        final String firstNamePattern = "(^[a-zA-Z][a-z]{1,15})";
        final String lastNamePattern = "(^[a-zA-Z][a-z]{1,15})";
        final String passPattern = "([a-zA-Z0-9]{6,15})";




        verificationWindow = new Dialog(this);


//        mFirstName.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//                String fName = s.toString().trim();
//                if (fName.matches(firstNamePattern) && s.length() > 0)
//                {
//                    mFirstName.setError(null);
//
//                }
//                else if (fName.isEmpty())
//                {
//                    mFirstName.setError("Empty Name");
//                }
//                else
//                {
//                    mFirstName.setError("Use only letters. Only first letter can be Uppercase.");
//                }
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // other stuffs
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // other stuffs
//            }
//        });
//        mLastName.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//                String lName = s.toString().trim();
//                if(mFirstName.getText().toString().trim().isEmpty()){
//                    mFirstName.setError("First Name Required");
//                }
//                if (lName.matches(lastNamePattern) && s.length() > 0)
//                {
//                    mLastName.setError(null);
//
//                }
//                else if (lName.isEmpty())
//                {
//                    mLastName.setError("Empty Name");
//                }
//                else
//                {
//                    mLastName.setError("Use only letters. Only first letter can be Uppercase.");
//                }
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // other stuffs
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // other stuffs
//            }
//        });
//        emailValidate .addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//                String email = s.toString().trim();
//                if(mFirstName.getText().toString().trim().isEmpty()){
//                    mFirstName.setError("First Name Required");
//                }
//                if(mLastName.getText().toString().trim().isEmpty()){
//                    mLastName.setError("Last Name Required");
//                }
//
//                if (email.matches(emailPattern) && s.length() > 0)
//                {
//                    emailValidate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                    mProgressbar.setVisibility(View.VISIBLE);
//                    fAuth.fetchSignInMethodsForEmail(email)
//                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
//                                    if (task.isSuccessful()) {
//                                        SignInMethodQueryResult result = task.getResult();
//                                        List<String> signInMethods = result.getSignInMethods();
//                                        if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD) || signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD) ) {
//                                            // User can sign in with email/password
//                                            mProgressbar.setVisibility(View.INVISIBLE);
//                                            emailValidate.setError("Email is already in use.");
//                                        } else{
//                                            mProgressbar.setVisibility(View.INVISIBLE);
//                                            emailValidate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                                            emailValidate.setCompoundDrawablesWithIntrinsicBounds(0, 0 , R.drawable.ic_done_green_40dp , 0);
//                                            emailValidate.setError(null);
//
//                                        }
//                                    } else {
//                                        Log.e("registerActivity", "Error getting sign in methods for user", task.getException());
//                                    }
//                                }
//                            });
//                }
//                else if (email.isEmpty())
//                {
//                    emailValidate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                    emailValidate.setError("Empty email");
//                }
//                else
//                {
//                    emailValidate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                    emailValidate.setError("Invalid email");
//                }
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // other stuffs
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // other stuffs
//            }
//        });

//        mPhoneNumber.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//                if(mFirstName.getText().toString().trim().isEmpty()){
//                    mFirstName.setError("First Name Required");
//                }
//                if(mLastName.getText().toString().trim().isEmpty()){
//                    mLastName.setError("Last Name Required");
//                }
//
//                if(emailValidate.getText().toString().trim().isEmpty()){
//                    emailValidate.setError("Email Required");
//                }
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // other stuffs
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // other stuffs
//            }
//        });
//        mPassword.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//                String password = s.toString().trim();
//                if(mFirstName.getText().toString().trim().isEmpty()){
//                    mFirstName.setError("First Name Required");
//                }
//                if(mLastName.getText().toString().trim().isEmpty()){
//                    mLastName.setError("Last Name Required");
//                }
//
//                if(emailValidate.getText().toString().trim().isEmpty()){
//                    emailValidate.setError("Email Required");
//                }
//                if(mPhoneNumber.getText().toString().trim().isEmpty()){
//                    mPhoneNumber.setError("Phone Number Required");
//                }
//                if (password.matches(passPattern) && s.length() > 0)
//                {
//                    mPassword.setError(null);
//
//                }
//                else if (password.isEmpty())
//                {
//                    mPassword.setError("Empty Password");
//                }
//                else if(s.length() < 6){
//                    mPassword.setError("Password should be at least 6 characters.");
//                }
//                else
//                {
//                    mPassword.setError("Use only letters and numbers.");
//                }
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // other stuffs
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // other stuffs
//            }
//        });

//        mRePassword.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//                String rePassword = s.toString().trim();
//                String password = mPassword.getText().toString().trim();
//                if(mFirstName.getText().toString().trim().isEmpty()){
//                    mFirstName.setError("First Name Required");
//                }
//                if(mLastName.getText().toString().trim().isEmpty()){
//                    mLastName.setError("Last Name Required");
//                }
//
//                if(emailValidate.getText().toString().trim().isEmpty()){
//                    emailValidate.setError("Email Required");
//                }
//                if(mPhoneNumber.getText().toString().trim().isEmpty()){
//                    mPhoneNumber.setError("Phone Number Required");
//                }
//                if(mPassword.getText().toString().trim().isEmpty()){
//                    mPassword.setError("Password Required");
//                }
//                if (rePassword.equals(password) && s.length() > 0)
//                {
//                    mRePassword.setError(null);
//                    mRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                    mRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0 , R.drawable.ic_done_green_40dp , 0);
//                }
//                else if (rePassword.isEmpty())
//                {
//                    mRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                    mRePassword.setError("Empty Password");
//                }
//                else
//                {
//                    mRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                    mRePassword.setError("Passwords Do not Match!");
//                }
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // other stuffs
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // other stuffs
//            }
//        });
        mFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String fName = mFirstName.getText().toString().trim();
                    if (fName.matches(firstNamePattern) && fName.length() > 0) {
                        mFirstName.setError(null);

                    } else if (fName.isEmpty()) {
                        mFirstName.setError("Empty Name");
                    } else {
                        mFirstName.setError("Use only letters. Only first letter can be Uppercase.");
                    }
                }
            }
        });

        mLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    String lName = mLastName.getText().toString().trim();
                    if (lName.matches(lastNamePattern) && lName.length() > 0)
                    {
                        mLastName.setError(null);

                    }
                    else if (lName.isEmpty())
                    {
                        mLastName.setError("Empty Name");
                    }
                    else
                    {
                        mLastName.setError("Use only letters. Only first letter can be Uppercase.");
                    }

                }else{
                    if(mFirstName.getText().toString().trim().isEmpty()){
                        mFirstName.setError("First Name Required");
                    }
                }
            }
        });

        emailValidate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    String email = emailValidate.getText().toString().trim();
                    if (email.matches(emailPattern) && email.length() > 0)
                    {
                        emailValidate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mProgressbar.setVisibility(View.VISIBLE);
                        fAuth.fetchSignInMethodsForEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                        if (task.isSuccessful()) {
                                            SignInMethodQueryResult result = task.getResult();
                                            List<String> signInMethods = result.getSignInMethods();
                                            if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD) || signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD) ) {
                                                // User can sign in with email/password
                                                mProgressbar.setVisibility(View.INVISIBLE);
                                                emailValidate.setError("Email is already in use.");
                                            } else{
                                                mProgressbar.setVisibility(View.INVISIBLE);
                                                emailValidate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                                emailValidate.setCompoundDrawablesWithIntrinsicBounds(0, 0 , R.drawable.ic_done_green_40dp , 0);
                                                emailValidate.setError(null);

                                            }
                                        } else {
                                            Log.e("registerActivity", "Error getting sign in methods for user", task.getException());
                                        }
                                    }
                                });
                    }
                    else if (email.isEmpty())
                    {
                        emailValidate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        emailValidate.setError("Empty email");
                    }
                    else
                    {
                        emailValidate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        emailValidate.setError("Invalid email");
                    }

                }else{
                    if(mFirstName.getText().toString().trim().isEmpty()){
                        mFirstName.setError("First Name Required");
                    }
                    if(mLastName.getText().toString().trim().isEmpty()){
                        mLastName.setError("Last Name Required");
                    }
                }
            }
        });

        mPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    if (mFirstName.getText().toString().trim().isEmpty()) {
                        mFirstName.setError("First Name Required");
                    }
                    if (mLastName.getText().toString().trim().isEmpty()) {
                        mLastName.setError("Last Name Required");
                    }

                    if (emailValidate.getText().toString().trim().isEmpty()) {
                        emailValidate.setError("Email Required");
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
                    if(mFirstName.getText().toString().trim().isEmpty()){
                        mFirstName.setError("First Name Required");
                    }
                    if(mLastName.getText().toString().trim().isEmpty()){
                        mLastName.setError("Last Name Required");
                    }
                    if(emailValidate.getText().toString().trim().isEmpty()){
                        emailValidate.setError("Email Required");
                    }
                    if(mPhoneNumber.getText().toString().trim().isEmpty()){
                        mPhoneNumber.setError("Phone Number Required");
                    }

                }
            }
        });

        mRePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    String rePassword = mRePassword.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();
                    if (rePassword.equals(password) && rePassword.length() > 0)
                    {
                        mRePassword.setError(null);
                        mRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0 , R.drawable.ic_done_green_40dp , 0);
                    }
                    else if (rePassword.isEmpty())
                    {
                        mRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mRePassword.setError("Empty Password");
                    }
                    else
                    {
                        mRePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mRePassword.setError("Passwords Do not Match!");
                    }

                }else{
                    if(mFirstName.getText().toString().trim().isEmpty()){
                        mFirstName.setError("First Name Required");
                    }
                    if(mLastName.getText().toString().trim().isEmpty()){
                        mLastName.setError("Last Name Required");
                    }
                    if(emailValidate.getText().toString().trim().isEmpty()){
                        emailValidate.setError("Email Required");
                    }
                    if(mPhoneNumber.getText().toString().trim().isEmpty()){
                        mPhoneNumber.setError("Phone Number Required");
                    }
                    if(mPassword.getText().toString().trim().isEmpty()){
                        mPassword.setError("Password Required");
                    }

                }
            }
        });



        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFirstName.getError() == null && mLastName.getError() == null && emailValidate.getError() == null && mPhoneNumber.getError() == null && mPassword.getError() == null && mRePassword.getError() == null
                 &&  mFirstName.getText().length() > 0  && mLastName.getText().length() > 0 && emailValidate.getText().length() > 0 && mPhoneNumber.getText().length() > 0 && mPassword.getText().length() > 0 && mRePassword.getText().length() > 0) {
                    mVerificationProgressbar.setVisibility(View.VISIBLE);
                    phoneNumber = "+94" +  mPhoneNumber.getText().toString().trim().substring(1);
                    Log.i("registerActivity", "Phone Number :" + " " + phoneNumber);
                    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            signInWithPhoneAuthCredential(phoneAuthCredential);
                            if(verificationWindow.isShowing()){
                                verificationWindow.dismiss();
                            }
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            codeVerified = false;
                            Toast.makeText(registerActivity.this , "Invalid Phone Number!" , Toast.LENGTH_SHORT).show();
                            Log.i("registerActivity", "onVerificationFailed: " + e.toString());

                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            mVerificationID = s;
                            mResendToken = forceResendingToken;
                            ShowPopup();
                        }


                    };
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber , 120 , TimeUnit.SECONDS ,registerActivity.this , mCallbacks);
                    Log.i("registerActivity", " code verify status : " + codeVerified);

                }
            }
        });

    }


    public void LoginAccount(View view) {
        startActivity(new Intent(registerActivity.this , LoginActivity.class));
    }
    public void ShowPopup(){
        final EditText mCode;
        final TextView textClose;
        final Button submit;
        final TextView resend;

        verificationWindow.setContentView(R.layout.activity_sms_verify_custom_popup);
        textClose = verificationWindow.findViewById(R.id.close_custom_pop_up_button);
        submit =  verificationWindow.findViewById(R.id.submitButton);
        mCode = verificationWindow.findViewById(R.id.verification_code);
        resend = verificationWindow.findViewById(R.id.ResendToken);

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationWindow.dismiss();
                mVerificationProgressbar.setVisibility(View.INVISIBLE);
                Toast.makeText(registerActivity.this, "Registration Failed!" , Toast.LENGTH_SHORT).show();

            }
        });

        Objects.requireNonNull(verificationWindow.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        verificationWindow.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mCode.getText().toString().trim().equals("")){
                    PhoneAuthCredential mCredentials = PhoneAuthProvider.getCredential( mVerificationID , mCode.getText().toString().trim());
                    signInWithPhoneAuthCredential(mCredentials);
                    verificationWindow.dismiss();
                }else{
                    Toast.makeText(registerActivity.this, "Please Enter the code first." , Toast.LENGTH_SHORT).show();
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    resendVerificationCode(phoneNumber , mResendToken);
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            codeVerified = true;
                            createUser();
                        }
                    }
                });
    }

    public void createUser( ){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser() ;

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String email = emailValidate.getText().toString().trim();
                            String password = mRePassword.getText().toString().trim();
                            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mVerificationProgressbar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(registerActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MenuDisplayActivity.class));
                                    } else {
                                        mVerificationProgressbar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(registerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });




    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                registerActivity.this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
}
