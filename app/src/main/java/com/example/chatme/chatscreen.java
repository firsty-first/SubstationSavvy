package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;

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
public  String spokenText;
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
        String m=binding.editTextText.getText().toString();
        binding.editTextText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String m=binding.editTextText.getText().toString();
                if(m.length()>0)
                {
                    Log.d("visiblity","check yes");
                    binding.sendBtn.setVisibility(View.GONE);
                    binding.voiceassistbtn.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.sendBtn.setVisibility(View.VISIBLE);
                    binding.voiceassistbtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
              binding.chatRv.scrollToPosition(messagesModels.size() - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
Log.d("db","Dberror");
            }
        });



    }//end of oncreate

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
                    speechRecognizer(prediction);
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



        private static final int SPEECH_REQUEST_CODE = 0;

// Create an intent that can start the Speech Recognizer activity
        private void speechRecognizer(String s) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// This starts the activity and populates the intent with the speech text.
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

// This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
        @Override
        protected void onActivityResult(int requestCode, int resultCode,
        Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
             spokenText = results.get(0);
            // Do something with spokenText.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


//    private void handleInput() {
//        String input = editText.getText().toString().trim();
//
//        if (input.equals("1")) {
//            imageView1.setVisibility(View.VISIBLE);
//            imageView2.setVisibility(View.INVISIBLE);
//        } else if (input.equals("2")) {
//            imageView1.setVisibility(View.INVISIBLE);
//            imageView2.setVisibility(View.VISIBLE);
//        } else {
//            // Handle other cases or provide feedback for invalid input
//            // For example, you can show a Toast message.
//        }
//    }
}