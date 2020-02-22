package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donation.ModelClasses.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView mDetail, Dmatetime;
    Button mbtnMap, mbtnCall;

    private FirebaseDatabase database;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Event Detils");

        mDetail = findViewById(R.id.tDetail);
        Dmatetime = findViewById(R.id.tDatetime);
        mbtnMap = findViewById(R.id.btnMap);

        mbtnCall = findViewById(R.id.btnCall);

        String detail = getIntent().getStringExtra("detail");
        String datetime = getIntent().getStringExtra("dt");
        mDetail.setText(detail);
        Dmatetime.setText(datetime);

        database = FirebaseDatabase.getInstance();
//        String keyEvent = getIntent().getStringExtra("keyEvent");
        String keyPlace = getIntent().getStringExtra("keyPlace");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DatabaseReference placeRef = database.getReference("Place").child(keyPlace);
        placeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Event event = dataSnapshot.getValue(Event.class);

                if (map != null) {
                    LatLng latLng = new LatLng(Double.parseDouble(event.latitude), Double.parseDouble(event.longitude));
                    map.addMarker(new MarkerOptions().position(latLng));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                }

                mbtnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String label = event.name;
                        String uriBegin = "geo:" + event.latitude + "," + event.longitude;
                        String query = event.latitude + "," + event.longitude + "(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery;
                        Uri uri = Uri.parse(uriString);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                mbtnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:"+event.phonenumber));
                                startActivity(callIntent);
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(this, keyPlace, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
