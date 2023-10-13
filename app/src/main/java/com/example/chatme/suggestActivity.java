package com.example.chatme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class suggestActivity extends AppCompatActivity {
    Button b;
    EditText e1, e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        b = (Button) findViewById(R.id.button);
        e1 = (EditText) findViewById(R.id.editText1);
        e2 = (EditText) findViewById(R.id.editText2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/html");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"gauravrnps1@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback from substationsavvy app");
                i.putExtra(Intent.EXTRA_TEXT, "Name : " + e1.getText() + "\nMessage : " + e2.getText());
                try {
                    startActivity(Intent.createChooser(i, "Send feedback..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

//    @Override    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.       // getMenuInflater().inflate(R.menu.menu_main, menu);        return false;
//    }
//
//    @Override    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement        if (id == R.id.action_settings) {
//        return true;
//    }
//
////        return super.onOptionsItemSelected(item);
//}

}