package com.example.donation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.donation.ModelClasses.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainMapsActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabase;
    private Button btnsave;
    private Button btnproceed;
    private EditText editTextName;
    private EditText editTextLatitude;
    private EditText editTextLongitude;

    private EditText editTextAdress;
    private EditText editTextPhonenumber;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_maps);
        btnproceed = (Button) findViewById(R.id.btnproceed);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("place");
        editTextName = (EditText) findViewById(R.id.editTextName);

        editTextAdress = (EditText) findViewById(R.id.editTextAdress);
        editTextPhonenumber = (EditText) findViewById(R.id.editTextPhonenumber);

        editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);
        btnsave = (Button) findViewById(R.id.btnsave);
        btnsave.setOnClickListener(this);
        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMapsActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            // go to logij
            startActivity(new Intent(MainMapsActivity.this, LoginActivity.class));
        }

    }
    private void saveUserInformation(){
        String name = editTextName.getText().toString().trim();

        String adress = editTextAdress.getText().toString().trim();
        String phonenumber = editTextPhonenumber.getText().toString().trim();

        double latitude = Double.parseDouble(editTextLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(editTextLongitude.getText().toString().trim());
        UserInformation userInformation=new UserInformation(name, adress, phonenumber, latitude, longitude);
        mDatabase.child(firebaseAuth.getUid()).setValue(userInformation);
        Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view==btnproceed){
            finish();
        }
        if (view==btnsave){
            saveUserInformation();
            editTextName.getText().clear();

            editTextAdress.getText().clear();
            editTextPhonenumber.getText().clear();

            editTextLatitude.getText().clear();
            editTextLongitude.getText().clear();
        }
    }
}
