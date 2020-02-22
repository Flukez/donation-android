package com.example.donation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donation.Activities.LoginActivity;
import com.example.donation.ModelClasses.Event;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    double latitudeMain = 0.0;
    double longitudeMain = 0.0;
    String categoryItemText1;
    PlacesClient placesClient;
    String TAG;
    private GoogleMap mMap;
    private EditText editTextName;
    private EditText editTextAdress;
    private EditText editTextPhonenumber;
    private Button btnsave;
    private Spinner categoryIv;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Place");
        firebaseAuth = FirebaseAuth.getInstance();

//        searchView = findViewById(R.id.sv_location);
//        mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);

        editTextName = findViewById(R.id.editTextName);
        editTextAdress = findViewById(R.id.editTextAdress);
        editTextPhonenumber = findViewById(R.id.editTextPhonenumber);
        categoryIv = (Spinner) findViewById(R.id.category);

        btnsave = findViewById(R.id.bt_save);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainMapActivity.this, latitudeMain + " : " + longitudeMain, Toast.LENGTH_SHORT).show();

                String name = editTextName.getText().toString();
                String address = editTextAdress.getText().toString();
                String phone = editTextPhonenumber.getText().toString();


                if (name.matches("")) {
                    editTextName.setError("กรุณากรอกชื่อสถานที่");
                } else if (address.matches("")) {
                    editTextAdress.setError("กรุณากรอกที่อยู่");
                } else if (phone.length() < 9 || phone.length() > 10) {
                    editTextPhonenumber.setError("กรุณากรอกเบอร์โทรศัพท์");
                } else {
                    mapRegister();
                }

//                String name = editTextName.getText().toString().trim();
//                String address = editTextAdress.getText().toString().trim();
//                String phonenumber = editTextPhonenumber.getText().toString().trim();
//
//                String category = categoryItemText1;
//
//                String latitude = String.valueOf(latitudeMain);
//                String longitude = String.valueOf(longitudeMain);
//
//                Event modelmap = new Event(name, address, phonenumber, latitude, longitude, category);
//                mDatabase.child(firebaseAuth.getUid()).setValue(modelmap);
//                Toast.makeText(MainMapActivity.this, "Saved", Toast.LENGTH_LONG).show();
            }
        });

        //13-02-63
        final String[] category = getResources().getStringArray(R.array.categorys);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_style, category);
        categoryIv.setAdapter(adapter);

        //setOnItemSelectedListener
        categoryIv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainMapActivity.this, "Select: " + category[position], Toast.LENGTH_SHORT).show();

                categoryItemText1 = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        String apikey = "AIzaSyAEPlKilR13frsihRGht4OqczKOKXzm_U0";

        String apikey = "AIzaSyCcWCVcruT2IqT8zr6PNUiZRqlG--vICZ8";

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apikey);

//            Places.initialize(MainMapActivity.this, getResources().getString(R.string.google_maps_key));
        }

        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                Log.i("PlacesApi", "onPlaceSelected: " + latLng.latitude + "\n" + latLng.longitude);

//                mMap.addMarker(new MarkerOptions().position(latLng));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                latitude = latLng.latitude;
//                longitude = latLng.longitude;


                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);
                latitudeMain = latLng.latitude;
                longitudeMain = latLng.longitude;

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                Toast.makeText(MainMapActivity.this, latLng.latitude + " : " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("ShowError", "An error occurred: " + status);
            }
        });

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Add a marker in Sydney and move the camera
        LatLng bkk = new LatLng(13.736717, 100.523186);
//        mMap.addMarker(new MarkerOptions().position(bkk));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(bkk));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(4.2f));//17f

//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                // Creating a marker
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                // Setting the position for the marker3
//                markerOptions.position(latLng);
//                latitude = latLng.latitude;
//                longitude = latLng.longitude;
//
////                Toast.makeText(MainMapActivity.this, latitude + " : " + longitude, Toast.LENGTH_SHORT).show();
//                // Clears the previously touched position
//                googleMap.clear();
//
//                // Animating to the touched position
//                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//                // Placing a marker on the touched position
//                googleMap.addMarker(markerOptions);
//            }
//        });
    }

    private void mapRegister() {

        if (TextUtils.isEmpty(editTextName.getText()) || TextUtils.isEmpty(editTextAdress.getText())
                || (TextUtils.isEmpty(editTextPhonenumber.getText()))) {
            Toast.makeText(MainMapActivity.this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        } else {

            String name = editTextName.getText().toString().trim();
            String address = editTextAdress.getText().toString().trim();
            String phonenumber = editTextPhonenumber.getText().toString().trim();

            String category = categoryItemText1;

            String latitude = String.valueOf(latitudeMain);
            String longitude = String.valueOf(longitudeMain);

            Event modelmap = new Event(name, address, phonenumber, latitude, longitude, category);
            mDatabase.child(firebaseAuth.getUid()).setValue(modelmap);
            Toast.makeText(MainMapActivity.this, "Save", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

}