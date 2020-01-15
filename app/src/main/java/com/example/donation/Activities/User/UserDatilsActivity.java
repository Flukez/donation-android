package com.example.donation.Activities.User;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.donation.MapsActivity;
import com.example.donation.R;

public class UserDatilsActivity extends AppCompatActivity {

    TextView mName, mAdress, mPhonenumber;
    ImageView mImageView;
    Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_datils);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Place Datils");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mName = findViewById(R.id.tName);
        mAdress = findViewById(R.id.tAdress);
        mPhonenumber = findViewById(R.id.tPhonenumber);

        //new code 15/01/63
        btnMap = findViewById(R.id.btnmap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDatilsActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

//        mImageView = findViewById(R.id.Image);

//        byte[] bytes = getIntent().getByteArrayExtra("image");
        String name = getIntent().getStringExtra("name");
        String adress = getIntent().getStringExtra("adress");
        String phonenumber = getIntent().getStringExtra("phonenumber");
//        Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

        mName.setText(name);
        mAdress.setText(adress);
        mPhonenumber.setText(phonenumber);
//        mImageView.setImageBitmap(bmp);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
