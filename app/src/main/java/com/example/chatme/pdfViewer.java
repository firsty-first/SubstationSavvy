package com.example.chatme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
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
//        PopupMenu popupMenu=new PopupMenu(this,binding.uploadpdf);
//       popupMenu.getMenuInflater().inflate(R.menu.fileupload,popupMenu.getMenu());
//        binding.uploadpdf.setOnClickListener(v -> {
//            popupMenu.show();
//        });
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                uploadPdf(String.valueOf(menuItem.getTitle()));
//                Toast.makeText(pdfViewer.this, String.valueOf(menuItem.getTitle()), Toast.LENGTH_SHORT).show();
//                return  true;
//            }
//        });


    }
    //upload pdf to specific child
    private void uploadPdf(String child)
    {
        database=FirebaseDatabase.getInstance();
        //database.getReference().child("pdf").child()

    }
    void viewPdf()
    {
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.getSettings().setJavaScriptEnabled(true);


        binding.webView.loadUrl("http://docs.google.com/gview?embedded=true&url=\" + myPdfUr");
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);
    }
}