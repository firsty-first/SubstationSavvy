package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatme.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signInActivity extends AppCompatActivity {
ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //this portion  is for checking only

        //till here
        progressDialog=new ProgressDialog(signInActivity.this);
        progressDialog.setTitle("Loging in");
        progressDialog.setMessage("wait while we get you there");
        auth=FirebaseAuth.getInstance();
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();

binding.signInTv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(signInActivity.this, signUpActivity.class));

    }
});
        binding.btnSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(binding.editTextTextEmailAddress.getText().toString().length()<10 || binding.editTextTextPassword.getText().toString().length()<5))
                {

                    progressDialog.show();
                    auth.signInWithEmailAndPassword(binding.editTextTextEmailAddress.getText().toString(), binding.editTextTextPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.hide();
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(signInActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(signInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
             }
                            });
                }
                else
                {
                    binding.editTextTextEmailAddress.setHint("please enter email id");
                    binding.editTextTextPassword.setHint("Password is too short");
                  // binding.editTextTextPassword.setHintTextColor(getResources().getColor(color.(R.color.errorHint)));//res.getColor(R.color.green)

                }
            }
        });
        if(  auth.getCurrentUser()==null)
            Log.d("user","NoUser");
        if (auth.getCurrentUser()!=null) {
            startActivity(new Intent(signInActivity.this, HomeActivity.class));
            finish();
        }
  }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //auth=FirebaseAuth.getInstance();
    }
}