package com.example.chatme;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.chatme.Adapter.PDFAdapter;
import com.example.chatme.databinding.ActivityDocumentationBinding;
import com.example.chatme.databinding.ChatscreenUiActivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class documentation_activity extends AppCompatActivity{
ActivityDocumentationBinding binding;
    private List<modelDoc> pdfNamesList;
    private PDFAdapter pdfAdapter;
FirebaseDatabase database;
FirebaseStorage storage;
public String pdfCategory="transformer";
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
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(uid);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String roleName = dataSnapshot.child("userName").getValue(String.class);

                        if ("admin".equalsIgnoreCase(roleName)) {
                            popupMenu.show();
                        } else {
                            // The current user is not an admin
                            Toast.makeText(documentation_activity.this, "u do not have permission", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the case where the user's data is not found
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors here
                }
            });


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
        binding.rvDoc.setLayoutManager(new LinearLayoutManager(this));
        pdfNamesList = new ArrayList<>();
        pdfAdapter = new PDFAdapter(pdfNamesList, getApplicationContext(), new PDFAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(documentation_activity.this, "Hiiiiii", Toast.LENGTH_SHORT).show();
//                Intent ipdfViewer=new Intent(documentation_activity.this,pdfViewer.class);
//                ipdfViewer.putExtra("url",)
            }
        });
        pdfAdapter.setOnItemClickListener(new PDFAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("rvclick","yessssssssssssss");
                // Handle the item click here
                modelDoc clickedItem = pdfNamesList.get(position);
                String filename = clickedItem.getFilename();
                String url = clickedItem.getUrl();

                // Launch another activity with the selected item's data
                Intent intent = new Intent(getApplicationContext(), pdfViewer.class);
                intent.putExtra("filename", filename);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        binding.rvDoc.setAdapter(pdfAdapter);
        // Retrieve PDF file names from Firebase Realtime Database
        retrievePDFNames(pdfCategory);
binding.button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        retrievePDFNames(binding.button1.getText().toString());
    }
});
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });
        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });binding.button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });
        binding.button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });
        binding.button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });
        binding.button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });
        binding.button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });
        binding.button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });
        binding.button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrievePDFNames(binding.button1.getText().toString());
            }
        });





    }
    private void retrievePDFNames(String node) {
        database.getReference().child("pdf").child(node).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pdfNamesList.clear();
                for (DataSnapshot pdfSnapshot : dataSnapshot.getChildren()) {
//
                    Map<String, Object> dataMap = (Map<String, Object>) pdfSnapshot.getValue();

                    if (dataMap != null) {
                        // Extract the values from the Map
                        String filename = (String) dataMap.get("filename");
                        String url = (String) dataMap.get("url");

                        // Create a modelDoc object and add it to the ArrayList
                        modelDoc pdfDoc = new modelDoc(filename, url);
                        pdfNamesList.add(pdfDoc);
                    } else {
                        // Handle the case where the dataMap is null or doesn't contain the expected keys
                        Log.e("FirebaseError", "Unexpected data structure in database: " + pdfSnapshot.toString());
                    }




                }
                pdfAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occurred during the data retrieval


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
                    modelDoc docInfo = new modelDoc(pdfName, downloadUrl);
                    docInfo.setFilename(pdfName);
                    docInfo.setUrl(downloadUrl);
                    database.getReference().child("pdf").child(child).push().setValue(docInfo, new DatabaseReference.CompletionListener() {
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


