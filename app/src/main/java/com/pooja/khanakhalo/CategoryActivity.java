package com.pooja.khanakhalo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pooja.khanakhalo.ModelClass.Kitchen;

import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Kitchen, KitchenViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String nam = Objects.requireNonNull(getIntent().getExtras()).getString("naam");
        Objects.requireNonNull(getSupportActionBar()).setTitle(nam);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView  = findViewById(R.id.recycler_kitchen); recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager); loadMenu(nam); adapter.startListening();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class KitchenViewHolder extends RecyclerView.ViewHolder {
        public TextView KitchenName;
        public ImageView KitchenImage;
        public TextView KitchenType;
        public TextView KitchenDistance;
        public TextView KitchenCharge;
        public TextView KitchenOpen;
        public TextView KitchenClose;
        public KitchenViewHolder(View itemView) {
            super(itemView);
            KitchenName = itemView.findViewById(R.id.cat_name);
            KitchenImage = itemView.findViewById(R.id.cat_image);
            KitchenType = itemView.findViewById(R.id.cat_type);
            KitchenDistance = itemView.findViewById(R.id.cat_distance);
            KitchenCharge = itemView.findViewById(R.id.cat_charge);
            KitchenOpen = itemView.findViewById(R.id.cat_open);
            KitchenClose = itemView.findViewById(R.id.cat_close);
        }
    }

    private void loadMenu(final String kitchen) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(kitchen);
        databaseReference.keepSynced(true);
        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(kitchen);
        Query personsQuery = personsRef.orderByKey();
        final FirebaseRecyclerOptions<Kitchen> personsOptions = new FirebaseRecyclerOptions.Builder<Kitchen>().setQuery(personsQuery, Kitchen.class).build();
        adapter = new FirebaseRecyclerAdapter<Kitchen, KitchenViewHolder>(personsOptions) {
            @NonNull
            @Override
            public KitchenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_view, parent, false);
                return new KitchenViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull KitchenViewHolder viewHolder, int i, @NonNull final Kitchen category) {
                viewHolder.KitchenName.setText(category.getName());
                Glide.with(CategoryActivity.this).load(category.getImage()).into(viewHolder.KitchenImage);
                viewHolder.KitchenType.setText(category.getType());
                viewHolder.KitchenDistance.setText(category.getDistance());
                viewHolder.KitchenCharge.setText("Rs. " + String.valueOf(category.getCharge())+"/KM");
                if (category.getStatus().toUpperCase().equals("OPEN")) {
                    viewHolder.KitchenClose.setVisibility(View.GONE);
                    viewHolder.KitchenOpen.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.KitchenOpen.setVisibility(View.GONE);
                    viewHolder.KitchenClose.setVisibility(View.VISIBLE);
                }
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent foodlist = new Intent(CategoryActivity.this, FoodlistActivity.class);
                        Utils.categoryData = category;
                        startActivity(foodlist); onBackPressed();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}