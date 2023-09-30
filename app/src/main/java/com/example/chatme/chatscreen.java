package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatme.Adapter.ChatAdapter;
import com.example.chatme.databinding.ChatscreenUiActivityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chatscreen extends AppCompatActivity {
    ChatscreenUiActivityBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String senderRoom,reciverRoom;
    String senderId;
    ArrayList<messagesModel> messagesModels;
    ChatAdapter chatAdapter;
    private ApiService apiService;
    Retrofit retrofit;
    private final String baseUrl = "https://testapi-8skm.onrender.com";
String prediction;

    @Override
    protected void onStart() {
        super.onStart();
   messagesModels=new ArrayList<>();
      chatAdapter=new ChatAdapter(messagesModels,getApplicationContext());
        binding.chatRv.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        binding.chatRv.setLayoutManager(layoutManager);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ChatscreenUiActivityBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
      senderId=auth.getUid();
        String recieverId=getIntent().getStringExtra("userId");
        String recieverImg=getIntent().getStringExtra("profilePic");
        String recieverName=getIntent().getStringExtra("userName");
binding.name.setText(recieverName);
        Picasso.get().load(recieverImg).placeholder(R.drawable.parrot).into(binding.userImage);
        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//final ArrayList<messagesModel> messagesModels=new ArrayList<>();
// ChatAdapter chatAdapter=new ChatAdapter(messagesModels,getApplicationContext());
//binding.chatRv.setAdapter(chatAdapter);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
//        binding.chatRv.setLayoutManager(layoutManager);



 senderRoom=senderId+recieverId;
reciverRoom=recieverId+senderId;
//
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeMsgIndatabase();
                String msg=binding.editTextText.getText().toString();
                getMessageFromBot(msg);
            }
            });

database.getReference().child("chats")
        .child(senderRoom)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        messagesModel model=snapshot1.getValue(messagesModel.class);
                        messagesModels.add(model);
                        Log.d("hii","got into if");
                        Log.d("hii",model.getMessages());
                    }
                chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
Log.d("db","Dberror");
            }
        });
    }


    void storeMsgIndatabase()
    {

                String msg=binding.editTextText.getText().toString();
                if(msg.length()>0)
                {
                    final messagesModel model=new messagesModel(senderId,msg);

                    model.setTimestamp(new Date().getTime());
                    binding.editTextText.setText("");
                    model.isBot=false;
                    database.getReference().child("chats")
                            .child(senderRoom)
                            .push()
                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    model.isBot=false;
                                    database.getReference().child("chats")
                                            .child(reciverRoom)
                                            .push()
                                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });

                                }
                            });
                }
    }

    void storeBOTMsgIndatabase(String botmsg)
    {


        if(botmsg.length()>0)
        {
            final messagesModel model=new messagesModel(senderId,botmsg);
model.isBot=true;
            model.setTimestamp(new Date().getTime());
            binding.editTextText.setText("");

            database.getReference().child("chats")
                    .child(senderRoom)
                    .push()
                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            model.isBot=true;
                            database.getReference().child("chats")
                                    .child(reciverRoom)
                                    .push()
                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });

                        }
                    });
        }
    }

    String getMessageFromBot(String usermessage)
    {
        Log.d("check","got into godplzhelp");
        Log.d("here is the uri",usermessage);


// Create a ModelInput object with the necessary data
        Log.d("uriiiiiiii check mid",usermessage);
        ModelInput input = new ModelInput(usermessage);
        input.setMessage(usermessage);
        Log.d("uriiiiiiii chek after mid",input.getMessage().toString());

        //input.setImgUrl("https://example.com/image.jpg"); // Replace with your image URL
// Make the API call
        Call<String> call = apiService.getPredictionString(input);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("checkkkkkk","api call godHelp");
                Log.i("checkkkkkk",input.getMessage());
                if (response.isSuccessful()) {
                    // Handle the string response here
                    prediction = response.body();
                    storeBOTMsgIndatabase(prediction);
                    Toast.makeText(getApplicationContext(), prediction, Toast.LENGTH_SHORT).show();

                    // Example: Display the prediction in a TextView
                    Log.d("god",prediction);
                } else {
                    // Handle error responses
                    // Example: Display an error message
                    prediction="api error";
                    Log.d("god",prediction);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle network or other errors
                // Example: Display a network error message
                Log.d("Network Error: " , t.getMessage());
            }
        });
        return prediction;
    }
}