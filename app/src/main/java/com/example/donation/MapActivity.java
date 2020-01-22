package com.example.donation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapActivity extends AppCompatActivity {

    TextView mName, mLatitude, mLongitude;
    Button mbtn;

    LinearLayoutManager mLinearLayoutManager;
    RecyclerView mRecyclerView;

    private FirebaseDatabase database;
    private FirebaseRecyclerOptions<ModelMap> firebaseRecyclerOptions;
    private FirebaseRecyclerAdapter<ModelMap, ViewHoderMap> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Map");

//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mRecyclerView = findViewById(R.id.recyclerView);

        mName = findViewById(R.id.rName);
        mLatitude = findViewById(R.id.rLatitude);
        mLongitude = findViewById(R.id.rLongitude);
        mbtn = findViewById(R.id.rbtnM);

        database = FirebaseDatabase.getInstance();

        String key = getIntent().getStringExtra("key");

        DatabaseReference reference = database.getReference("/Place/" + key);

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<ModelMap>().setQuery(reference, ModelMap.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelMap, ViewHoderMap>(firebaseRecyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHoderMap holder, int i, @NonNull ModelMap model) {
                holder.setDetails(getApplicationContext(), model.getName(), model.getLatitude(), model.getLongitude());
            }

            @NonNull
            @Override
            public ViewHoderMap onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.map, parent, false);

                ViewHoderMap viewHoderMap = new ViewHoderMap(itemView);
                viewHoderMap.setOnClickListener(new ViewHoderMap.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(MapActivity.this, " Map", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        Toast.makeText(MapActivity.this, "Long Click", Toast.LENGTH_SHORT).show();
                    }
                });

                return viewHoderMap;
            }
        };

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);



        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

    }

    protected void onStart() {
        super.onStart();

        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }
}
