package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donation.Activities.LoginActivity;
import com.example.donation.ModelClasses.UserInformation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double latitude = 0.0;
    Double longitude = 0.0;

    private EditText editTextName;
    private EditText editTextAdress;
    private EditText editTextPhonenumber;
    private Button btnsave;

    String categoryItemText1;
    private Spinner categoryTv1;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    PlacesClient placesClient;
//    SearchView searchView;
//    SupportMapFragment mapFragment;

    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("place");
        firebaseAuth = FirebaseAuth.getInstance();

//        searchView = findViewById(R.id.sv_location);
//        mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);

        editTextName = findViewById(R.id.editTextName);
        editTextAdress = findViewById(R.id.editTextAdress);
        editTextPhonenumber = findViewById(R.id.editTextPhonenumber);
        categoryTv1 = (Spinner) findViewById(R.id.category);

        btnsave = findViewById(R.id.bt_save);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMapActivity.this, latitude + " : " + longitude, Toast.LENGTH_SHORT).show();

                String name = editTextName.getText().toString().trim();
                String address = editTextAdress.getText().toString().trim();
                String phonenumber = editTextPhonenumber.getText().toString().trim();

                String category = categoryItemText1;

                ModelMap modelmap = new ModelMap(name, address, phonenumber, latitude, longitude, category);
                mDatabase.child(firebaseAuth.getUid()).setValue(modelmap);
                Toast.makeText(MainMapActivity.this, "Saved", Toast.LENGTH_LONG).show();
            }
        });

        //13-02-63
        final String[] category = getResources().getStringArray(R.array.categorys);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_style, category);
        categoryTv1.setAdapter(adapter);

        //setOnItemSelectedListener
        categoryTv1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainMapActivity.this, "Select: " + category[position], Toast.LENGTH_SHORT).show();

                categoryItemText1 = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                String location = searchView.getQuery().toString();
//                List<Address> addressList = null;
//
//                if (location != null || !location.equals("")) {
//                    Geocoder geocoder = new Geocoder(MainMapActivity.this);
//                    try {
//                        addressList = geocoder.getFromLocationName(location, 1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Address address = addressList.get(0);
//                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//
//                    latitude = latLng.latitude;
//                    longitude = latLng.longitude;
////                    mMap.clear();
////                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        mapFragment.getMapAsync(this);

        String apikey = "AIzaSyCcWCVcruT2IqT8zr6PNUiZRqlG--vICZ8";

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apikey);

//            Places.initialize(MainMapActivity.this, getResources().getString(R.string.google_maps_key));
        }

        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                Log.i("PlacesApi","onPlaceSelected: "+latLng.latitude+"\n"+latLng.longitude);

//                mMap.addMarker(new MarkerOptions().position(latLng));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                latitude = latLng.latitude;
//                longitude = latLng.longitude;

                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);
                latitude = latLng.latitude;
                longitude = latLng.longitude;

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("ShowError","An error occurred: "+ status);
            }
        });

//        String apikey = "AIzaSyAEPlKilR13frsihRGht4OqczKOKXzm_U0";

//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), apikey);
//        }
//
//        // Initialize the AutocompleteSupportFragment.
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//        // Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//        // Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: " + status);
//            }
//        });
//
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
//                // Setting the position for the marker
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

}