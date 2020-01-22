package com.example.donation.Activities.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donation.EventActivity;
import com.example.donation.View.Model;
import com.example.donation.R;
import com.example.donation.View.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity {

    LinearLayoutManager mLinearLayoutManager;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Model> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mRecyclerView = findViewById(R.id.recyclerView);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Place"); //place

        showData();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Place");
    }

    private void showData() {

        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(mDatabaseReference, Model.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Model model) {
                holder.setDetails(getApplicationContext(), model.getName(), model.getAddress(), model.getPhonenumber());//model.getImage()

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

                ViewHolder viewHolder = new ViewHolder(itemView);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        DataSnapshot snapshot = getSnapshots().getSnapshot(position);

                        Toast.makeText(UserActivity.this, "Hello", Toast.LENGTH_SHORT).show();

                        //Naw Code 14/01/63
                        //View
                        TextView mName = view.findViewById(R.id.rName);
                        TextView mAdress = view.findViewById(R.id.rAdress);
                        TextView mPhonenumber = view.findViewById(R.id.rPhonenumber);

                        //get data from views
                        String name = mName.getText().toString();
                        String adress = mAdress.getText().toString();
                        String phonenumber = mPhonenumber.getText().toString();

                        Intent intent = new Intent(view.getContext(), EventActivity.class);

                        intent.putExtra("key", snapshot.getKey());

                        intent.putExtra("name", name);
                        intent.putExtra("adress", adress);
                        intent.putExtra("phonenumber", phonenumber);
                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        Toast.makeText(UserActivity.this, "Long Click", Toast.LENGTH_SHORT).show();

                    }
                });

                return viewHolder;
            }
        };

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    protected void onStart() {
        super.onStart();

        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }
}
