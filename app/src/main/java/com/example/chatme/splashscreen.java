package com.example.chatme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.chatme.databinding.ActivitySplashscreenBinding;

public class splashscreen extends AppCompatActivity {
ActivitySplashscreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().hide;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Thread thread=new Thread(){
    @Override
    public void run() {
        super.run();
        try {
sleep(3000);
        }
        catch (Exception ex)
        {
ex.printStackTrace();
        }
        finally {

            startActivity(new Intent(splashscreen.this, signInActivity.class));
finish();
        }
    }
};
thread.start();
    }
}