package com.example.donation.Activities.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donation.View.Model;
import com.example.donation.R;
import com.example.donation.View.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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
        mDatabaseReference = mFirebaseDatabase.getReference("place"); //place

        showData();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Place List");
    }

    private void showData() {

        options = new  FirebaseRecyclerOptions.Builder<Model>().setQuery(mDatabaseReference,Model.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Model model) {
                holder.setDetails(getApplicationContext(),model.getName(),model.getAdress(),model.getPhonenumber(),model.getImage());

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);

                ViewHolder viewHolder = new ViewHolder(itemView);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Toast.makeText(UserActivity.this, "Hello", Toast.LENGTH_SHORT).show();

                        //Naw Code 14/01/63
                        //View
                        TextView mName = view.findViewById(R.id.rName);
                        TextView mAdress = view.findViewById(R.id.rAdress);
                        TextView mPhonenumber = view.findViewById(R.id.rPhonenumber);
//                        ImageView mImageView = view.findViewById(R.id.rImage);

                        //get data from views
                        String name = mName.getText().toString();
                        String adress = mAdress.getText().toString();
                        String phonenumber = mPhonenumber.getText().toString();
//                        Drawable drawable = mImageView.getDrawable();
//                        Bitmap mBitmap = ((BitmapDrawable)drawable).getBitmap();

                        //pass this data to new activity
                        Intent intent = new Intent(view.getContext(), UserDatilsActivity.class);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        byte[] bytes = stream.toByteArray();
//                        intent.putExtra("image", bytes);
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

    //code New 15/01/63
    private void  firebaseSearch(String searchText){

        String quary = searchText.toLowerCase();

        Query firebaseSearchQuery = mDatabaseReference.orderByChild("name").startAt(quary).endAt(quary + "\uf8ff"); //search
        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(firebaseSearchQuery,Model.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull Model model) {

                holder.setDetails(getApplicationContext(),model.getName(),model.getAdress(),model.getPhonenumber(),model.getImage());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);

                ViewHolder viewHolder = new ViewHolder(itemView);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Toast.makeText(UserActivity.this, "Hello", Toast.LENGTH_SHORT).show();

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

    //code New 15/01/63
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
