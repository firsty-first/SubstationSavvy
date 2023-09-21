package com.example.chatme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.chatme.Adapter.fragmentAdapter;
import com.example.chatme.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
ActivityHomeBinding binding;
FirebaseAuth auth;
FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        //  getSupportActionBar().show();
        binding.viewpager.setAdapter(new fragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater() ;
        inflater.inflate(R.menu.menuehome,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//      switch (item.getItemId())
//      {
//          case  R.id.setting:
//              break;
//          case R.id.logout:
//              auth.signOut();
//              startActivity(new Intent(HomeActivity.this,signInActivity.class));
//              break;
    //  }
        if(item.getItemId()==R.id.logout)
        {
            auth.signOut();
            startActivity(new Intent(HomeActivity.this,signInActivity.class));
//
        }
       // else  if (item.getItemId()==R.id.report_issue)

        return super.onOptionsItemSelected(item);
    }
}