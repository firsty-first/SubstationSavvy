package com.example.chatme;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.chatme.databinding.ActivityDocumentationBinding;
import com.example.chatme.databinding.ChatscreenUiActivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class documentation_activity extends AppCompatActivity {
ActivityDocumentationBinding binding;
FirebaseDatabase database;
FirebaseStorage storage;
Uri pdfUri;
FirebaseAuth auth;
        String pdfName,child;
    private ActivityResultLauncher<Intent> pdfPickerLauncher;

    private StorageReference storageReference;

    @Override
    protected void onStart() {
        super.onStart();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDocumentationBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        PopupMenu popupMenu=new PopupMenu(this,binding.uploadpdf);
        popupMenu.getMenuInflater().inflate(R.menu.fileupload,popupMenu.getMenu());
        binding.uploadpdf.setOnClickListener(v -> {
            popupMenu.show();
        });
        pdfPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                     pdfUri = data.getData();
                             pdfName = getFileName(pdfUri);
                             uploadPdf(child,pdfName,pdfUri);
                            Toast.makeText(this, pdfName, Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No PDF selected", Toast.LENGTH_SHORT).show();
                    }
                });
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                child=String.valueOf(menuItem.getTitle());
                openPdfPicker();
                return  true;
            }
        });


    }



//uploaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaad
    private void uploadPdf(String child, String pdfName,Uri pdfUri)
    {


        StorageReference pdfReference = storageReference.child("pdfs/" + child+"/"+pdfName+auth.getUid());
if(pdfUri!=null) {
    // Upload the PDF file to Firebase Storage
    UploadTask uploadTask = pdfReference.putFile(pdfUri);
    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Task<Uri> uriTask = pdfReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUri) {
                    String downloadUrl = downloadUri.toString();
                    database.getReference().child("pdf").child(child).push().setValue(downloadUrl, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(documentation_activity.this, "Uploaded ", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Use the download URL as needed (e.g., save it to a database)

                }
            });
        }
    });
//        .addOnFailureListener(new OnFailureListener() {
//        @Override
//        public void onFailure(@NonNull Exception e) {
//            Toast.makeText(documentation_activity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
//            Log.d("upload failedddddddddddddddddd", String.valueOf(e));
//        }
//    });
}
else
    Log.d("upload failed","no uri");

    }

    private void openPdfPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        pdfPickerLauncher.launch(intent);
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}


