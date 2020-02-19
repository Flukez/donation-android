package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donation.ModelClasses.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDetailActivity extends AppCompatActivity {

    TextView mDetail, Dmatetime;
    Button mbtnMap;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Event Detils");

        mDetail = findViewById(R.id.tDetail);
        Dmatetime = findViewById(R.id.tDatetime);
        mbtnMap = findViewById(R.id.btnMap);

        String detail = getIntent().getStringExtra("detail");
        String datetime = getIntent().getStringExtra("dt");
        mDetail.setText(detail);
        Dmatetime.setText(datetime);

        database = FirebaseDatabase.getInstance();
        String keyEvent = getIntent().getStringExtra("keyEvent");
        String keyPlace = getIntent().getStringExtra("keyPlace");

        DatabaseReference placeRef = database.getReference("Place").child(keyPlace);
        placeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final UserInformation modelMap = dataSnapshot.getValue(UserInformation.class);
                mbtnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String label = modelMap.name;
                        String uriBegin = "geo:" + modelMap.latitude + "," + modelMap.longitude;
                        String query = modelMap.latitude + "," + modelMap.longitude + "(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery;
                        Uri uri = Uri.parse(uriString);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(this, keyPlace, Toast.LENGTH_SHORT).show();


    }
}
