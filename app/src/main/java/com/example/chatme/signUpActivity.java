package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chatme.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class signUpActivity extends AppCompatActivity {
    ActivityMainBinding binding;
  private   FirebaseAuth firebaseAuth;
  FirebaseDatabase database;
  ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // getSupportActionBar().hide();
        progressDialog=new ProgressDialog(signUpActivity.this);
        progressDialog.setTitle("Creating acccount");
        progressDialog.setMessage("creatign your acount");
        binding.signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signUpActivity.this,signInActivity.class));
            }
        });
       // Toast.makeText(this, firebaseAuth.getUid(), Toast.LENGTH_SHORT).show();
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(binding.editTextTextEmailAddress.getText().toString().length()<10 || binding.editTextTextPassword.getText().toString().length()<5)) {
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(binding.editTextTextEmailAddress.getText().toString(),
                            binding.editTextTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                UserModel user = new UserModel(binding.edittextName.getText().toString(), binding.editTextTextEmailAddress.getText().toString(), binding.editTextTextPassword.getText().toString());
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("user").child(id).setValue(user);
                                Toast.makeText(signUpActivity.this, "User created successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signUpActivity.this, signInActivity.class));

                            } else {
                                Toast.makeText(signUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(signUpActivity.this, "Enter proper data", Toast.LENGTH_SHORT).show();
                    binding.editTextTextEmailAddress.setHint("Enter proper email id");
                    binding.editTextTextPassword.setHint("length should be minimum 4");
                }
            }
        });
//        if (firebaseAuth.getCurrentUser()!=null)
//            startActivity(new Intent(MainActivity.this, HomeActivity.class));

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
    }
}