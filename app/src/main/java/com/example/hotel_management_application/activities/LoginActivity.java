package com.example.hotel_management_application.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hotel_management_application.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    EditText email , password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); //This Line hides the action bar
        setContentView(R.layout.activity_login_page);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void onRegisterActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onLoginActivity(View view) {
        validate();
    }

    void validate(){
        String uEmail = email.getText().toString().trim();
        String uPassword = password.getText().toString().trim();

        if (!TextUtils.isEmpty(uEmail)
                && !TextUtils.isEmpty(uPassword)) {
            SignInWithEmailAndPassword(uEmail, uPassword);
        } else {
            if (TextUtils.isEmpty(uEmail)) {
                email.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(uPassword)) {
                password.setError("Password is required");
            }

        }
    }

    void SignInWithEmailAndPassword(String uEmail, String uPassword){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(uEmail,uPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        boolean isVerified = Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified();

                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        if(email.equals("basel.salman.1221@gmail.com")){
                            Toast.makeText(LoginActivity.this, "Welcome Back", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, AdminPanelActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            if(isVerified){
                                Toast.makeText(LoginActivity.this, "Welcome Back", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this, "Please verify your email first", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, VerifyEmailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            finish();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("error in login");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}