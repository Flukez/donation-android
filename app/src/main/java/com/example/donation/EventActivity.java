package com.example.donation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donation.Activities.User.UserActivity;
import com.example.donation.View.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventActivity extends AppCompatActivity {

    TextView mName, mAdress, mPhonenumber;
    ImageView mImageView;
    Button btnMap;

    LinearLayoutManager mLinearLayoutManager;
    RecyclerView mRecyclerView;

    private FirebaseDatabase database;
    private FirebaseRecyclerOptions<ModelEvent> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<ModelEvent, ViewHoderEvent> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Event");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mRecyclerView = findViewById(R.id.recyclerView);

        mName = findViewById(R.id.tName);
        mAdress = findViewById(R.id.tAdress);
        mPhonenumber = findViewById(R.id.tPhonenumber);

        database = FirebaseDatabase.getInstance();

        String key = getIntent().getStringExtra("key");

        DatabaseReference reference = database.getReference("/Event/" + key);

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<ModelEvent>().setQuery(reference, ModelEvent.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelEvent, ViewHoderEvent>(firebaseRecyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHoderEvent hoder, int i, @NonNull ModelEvent model) {

                hoder.setDetails(getApplicationContext(), model.getNameevent(), model.getDetail(), model.getDt(), model.getImage());
            }

            @NonNull
            @Override
            public ViewHoderEvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false);

                ViewHoderEvent viewHoderEvent = new ViewHoderEvent(itemView);
                viewHoderEvent.setOnClickListener(new ViewHoderEvent.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(EventActivity.this, " Event", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        Toast.makeText(EventActivity.this, "Long Click", Toast.LENGTH_SHORT).show();
                    }
                });

                return viewHoderEvent;
            }
        };

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


//        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

//        String name = getIntent().getStringExtra("name");
//        String adress = getIntent().getStringExtra("adress");
//        String phonenumber = getIntent().getStringExtra("phonenumber");
//
//        mName.setText(name);
//        mAdress.setText(adress);
//        mPhonenumber.setText(phonenumber);

    }

    protected void onStart() {
        super.onStart();

        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
