package com.example.chatme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chatme.databinding.ActivityDocumentationBinding;
import com.example.chatme.databinding.ChatscreenUiActivityBinding;

public class documentation_activity extends AppCompatActivity {
ActivityDocumentationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDocumentationBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

    }
}