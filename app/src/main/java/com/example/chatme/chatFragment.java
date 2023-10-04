package com.example.chatme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatme.Adapter.UserAdapter;
import com.example.chatme.databinding.FragmentChatBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class chatFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    FragmentChatBinding binding;
    private final String baseUrl = "https://testapi-8skm.onrender.com";

    ArrayList<UserModel> arrayList=new ArrayList<>();
    FirebaseDatabase database;
    private ApiService apiService;
    Retrofit retrofit;

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
        database=FirebaseDatabase.getInstance();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
                retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
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
        });

//        database.getReference().child("user").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//               arrayList.clear();
//                for(DataSnapshot dataSnapshot: snapshot.getChildren())
//                {
//                    UserModel userModel=dataSnapshot.getValue(UserModel.class);
//
//              userModel.setUserId(dataSnapshot.getKey());
//                    arrayList.add(userModel);
//
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



      return  binding.getRoot();
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

}