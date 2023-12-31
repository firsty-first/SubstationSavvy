package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    TextToSpeech textToSpeech;
    private final String baseUrl = "https://loadqua.onrender.com";
String prediction;
public  String spokenText;
    @Override
    protected void onStart() {
        super.onStart();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        messagesModels=new ArrayList<>();
      chatAdapter=new ChatAdapter(messagesModels,getApplicationContext());
        binding.chatRv.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        binding.chatRv.setLayoutManager(layoutManager);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Handle language data missing or not supported
                        Log.d("TTs","lang missing");
                    } else {
                        Log.d("TTS","asa");
                    }
                }
                else
                    Log.d("TTS","Initializatiopn  failed");

            }
        });


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
                    binding.sendBtn.setVisibility(View.VISIBLE);
                    binding.voiceassistbtn.setVisibility(View.GONE);
                }
                else
                {
                    binding.sendBtn.setVisibility(View.GONE);
                    binding.voiceassistbtn.setVisibility(View.VISIBLE);
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
                String msgText=binding.editTextText.getText().toString();
                storeMsgIndatabase(msgText);


              //keep this in try catch with extra precation
                getMessageFromBot(msgText);
            }
            });
        binding.voiceassistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechRecognizer();
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
binding.imagebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

    }
});
    }//end of oncreate

    void storeMsgIndatabase(String msg)
    {


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
    public void getPrediction(String query) {
        Map<String, String> input = new HashMap<>();
        input.put("query", query);

        Call<Map<String, String>> call = apiService.getPrediction(input);
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    Map<String, String> predictionResponse = response.body();
                    // Handle the prediction response as needed

                } else {
                    // Handle unsuccessful response
                }
            }
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                // Handle failure
            }
        });
    }
    String getMessageFromBot(String userMessage) {
        Log.d("check", "got into godplzhelp");
        Log.d("here is the uri", userMessage);

        // Create a Map representing the request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", userMessage);

        // Make the API call
        Call<Map<String, String>> call = apiService.getPredictionString(requestBody);
        call.enqueue(new Callback<Map<String, String>>() {

            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Log.i("checkkkkkk", "api call godHelp");
                Log.i("checkkkkkk", userMessage);
                if (response.isSuccessful()) {
                    // Handle the response here
                    Map<String, String> predictionMap = response.body();
                    Log.d("response",predictionMap.get("responses"));
                    // Assuming your prediction is a key-value pair in the response
                    if (predictionMap != null && predictionMap.containsKey("responses")) {
                        String prediction = predictionMap.get("responses");

                        // Further processing or UI updates
                        storeBOTMsgIndatabase(prediction);
                        tts(prediction);
                        Toast.makeText(getApplicationContext(), prediction, Toast.LENGTH_SHORT).show();

                        // Example: Display the prediction in a TextView
                        Log.d("god", prediction);
                    } else {
                        // Handle the case where the response does not contain the expected data
                        prediction = "Unexpected response format";
                        Log.d("god", prediction);
                    }
                } else {
                    // Handle error responses
                    // Example: Display an error message
                    prediction = "API error";
                    Log.d("god", prediction);
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                // Handle failure
                prediction = "API call failed";
                Log.d("god", prediction);
            }
        });

        // Return value or handle asynchronously as needed
        return prediction;
    }




    private static final int SPEECH_REQUEST_CODE = 0;

// Create an intent that can start the Speech Recognizer activity
        private void speechRecognizer() {
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
            storeMsgIndatabase(spokenText);

            //keep this in try catch with extra precation
            getMessageFromBot(spokenText);

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

    void tts(String text)
    {

        Locale desiredLocale = new Locale("en", "IN");

        textToSpeech.setLanguage(desiredLocale);


        if (!text.isEmpty()) {
            // Add a unique utterance ID
            String utteranceId = "utteranceId";

            // Speak the text
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
            //textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {


            }

            @Override
            public void onDone(String s) {
                textToSpeech.shutdown();
            }

            @Override
            public void onError(String s) {

            }
        });





    }
}