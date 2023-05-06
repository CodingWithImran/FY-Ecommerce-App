package com.codingwithimran.fycommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.codingwithimran.fycommerce.R;
import com.codingwithimran.fycommerce.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    ActivityRegistrationBinding binding;
    FirebaseAuth auth;
    TextView name, email, password;
    CheckBox checkBoxTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        name = findViewById(R.id.inpName);
        email = findViewById(R.id.inpemail);
        password = findViewById(R.id.inpPassword);
        auth = FirebaseAuth.getInstance();
        checkBoxTerms = findViewById(R.id.checkBoxTerms);


        // if already login then go for login
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

//        Registrations using firebase Auth method
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxTerms.isChecked()){
                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                        Toast.makeText(RegistrationActivity.this, "successfully account created", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(RegistrationActivity.this, "Registrations failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(RegistrationActivity.this, "Please Accept Terms and policy", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}