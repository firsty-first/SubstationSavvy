package com.example.chatme;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatme.Adapter.UserAdapter;
import com.example.chatme.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class chatFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String Weather_BASE_URL = "https://api.weatherstack.com/";
    private static final String Weather_API_KEY = "0a9e43b9a6c24ddbdaad613bae8fa7cd";

    private WeatherApiService weatherApiService;
TextToSpeech textToSpeech;
    String coordinate;
    private String mParam1;
    private String mParam2;
    FragmentChatBinding binding;
    private final String baseUrl = "https://testapi-8skm.onrender.com";

FirebaseAuth auth;
    FirebaseDatabase database;
    private ApiService apiService;
    Retrofit retrofit,retrofitWeather;

    public chatFragment() {
        // Required empty public constructor
    }


    public static chatFragment newInstance(String param1, String param2) {
        chatFragment fragment = new chatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
                retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        //weather api
        retrofitWeather = new Retrofit.Builder()
                .baseUrl(Weather_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location();
 }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
      binding= FragmentChatBinding.inflate(inflater, container, false);
//        UserAdapter adapter=new UserAdapter(arrayList,getContext());
//        binding.chatRv.setAdapter(adapter);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
//        binding.chatRv.setLayoutManager(layoutManager);

//getWeather();
        binding.suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),suggestActivity.class));
            }
        });
        binding.docBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), documentation_activity.class));
            }
        });
        binding.chabotimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkHealth();
            }
        });
        binding.customCardViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), chatscreen.class);

                intent.putExtra("userId","Bot-help");
                intent.putExtra("userName","Bot");
                startActivity(intent);
 }
        }   );

        database.getReference().child("user").child(auth.getUid()).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username=snapshot.getValue(String.class);
                if(username=="" || username==null|| username.length()<3)

                    username="User";

                binding.username.setText(username);
                Log.d("username",username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
if(mydate.contains("AM"))
    binding.Greeting.setText("Good Morning");
else
    binding.Greeting.setText("Good Evening");
        return  binding.getRoot();
    }
void tts(String text)
{
    textToSpeech.setLanguage(new Locale("en", "IN"));


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
    void checkHealth()
    {
        Call<PredictionResponse> welcomeMessageCall = apiService.getWelcomeMessage();
        welcomeMessageCall.enqueue(new Callback<PredictionResponse>() {

            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                    Log.d("result",response.body().toString());
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d("result",t.toString());

                // Handle failure
            }
        });
    }
    void getWeather()
    {
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Handle location updates here
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                 coordinate=Double.toString(latitude)+","+Double.toString(longitude);
                 Log.d("weather",coordinate);
            }

            public void onProviderEnabled(String provider) {
                // Provider enabled
            }

            public void onProviderDisabled(String provider) {

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

coordinate="28.7041,77.1025";
        // Make an API request

        Call<WeatherResponse> call = weatherApiService.getCurrentWeather(Weather_API_KEY, coordinate);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    Log.d("weather",weatherResponse.toString());
                    Location location = weatherResponse.getLocation();

                    CurrentWeather currentWeather = weatherResponse.getCurrentWeather();
                    Log.d("weather",currentWeather.toString());
                    // Access weather details (e.g., temperature and location)

                    // Access location name from the response JSON
                   //String cityName weatherResponse.getCurrentWeather().getLocation().getName();

                    int temperature = currentWeather.getTemperature();
                    binding.weather.setText(temperature+"c innnnn "+location);

                    // Update your UI with the weather information
                    // (You might want to run UI updates on the main thread)
                }  else {
                    // Handle errors

                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Handle network or other errors
            }
        });
    }

void Location()
{
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Handle location updates here
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d("location function",latitude+","+longitude);
            //getWeather(latitude+","+longitude);
        }

        public void onProviderEnabled(String provider) {
            // Provider enabled
        }

        public void onProviderDisabled(String provider) {

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };


}
}