package com.example.chatme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.chatme.databinding.ActivityPdfViewerBinding;
import com.google.firebase.database.FirebaseDatabase;


public class pdfViewer extends AppCompatActivity {
ActivityPdfViewerBinding binding;
FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Intent intent = getIntent();

        // Retrieve the data from the intent using the keys you used in the sender activity
        String filename = intent.getStringExtra("filename");
        String url = intent.getStringExtra("url");
        binding.pdfheader.setText(filename);
viewPdf(url,filename);

    }

    void viewPdf(String pdfUrl, String filename)
    {
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.getSettings().setJavaScriptEnabled(true);
//        String pdfurl="http://docs.google.com/gview?embedded=true&url=/";
        Log.d("pdfurl","https://docs.google.com/gview?embedded=true&url=" +pdfUrl);
        binding.webView.loadUrl(pdfUrl);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);

    }
}