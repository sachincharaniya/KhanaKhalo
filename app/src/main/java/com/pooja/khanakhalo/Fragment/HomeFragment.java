package com.pooja.khanakhalo.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pooja.khanakhalo.CategoryActivity;
import com.pooja.khanakhalo.Constant;
import com.pooja.khanakhalo.FoodlistActivity;
import com.pooja.khanakhalo.ModelClass.Kitchen;
import com.pooja.khanakhalo.R;
import com.pooja.khanakhalo.Utils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Context ctx;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Kitchen, KitchenViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false); ctx = getContext();

        RecyclerView recycler  = view.findViewById(R.id.recycler_cate); recycler.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false);
        recycler.setLayoutManager(manager); CategoryAdapter categoryAdapter = new CategoryAdapter(ctx);
        recycler.setAdapter(categoryAdapter);

        recyclerView  = view.findViewById(R.id.recycler_kitchen); recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager); loadMenu("All"); adapter.startListening();
        return view;
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

    private void loadMenu(final String type) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.Kitchen);
        databaseReference.keepSynced(true);
        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(Constant.Kitchen);
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
                Glide.with(ctx).load(category.getImage()).into(viewHolder.KitchenImage);
                viewHolder.KitchenType.setText(category.getType());
                viewHolder.KitchenDistance.setText(category.getDistance());
                viewHolder.KitchenCharge.setText("Rs. " +String.valueOf(category.getCharge())+"/KM");
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
                        Intent foodlist = new Intent(ctx, FoodlistActivity.class);
                        Utils.categoryData = category;
                        ctx.startActivity(foodlist);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.UserViewHolder> {
        private List<String> titleList;
        private Context context;
        public CategoryAdapter(Context ctx) {
            this.context = ctx;
            this.titleList = new ArrayList<>();
            titleList.add("All");titleList.add("Meals");titleList.add("Veg");titleList.add("Non Veg");titleList.add("Bakery");
        }
        @NonNull
        @Override
        public CategoryAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.cate,parent,false);
            return new UserViewHolder(view);
        }
        @Override
        public void onBindViewHolder(CategoryAdapter.UserViewHolder holder, final int position) {
            holder.title.setText(titleList.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent list = new Intent(ctx, CategoryActivity.class);
                    list.putExtra("naam",titleList.get(position));
                    ctx.startActivity(list);
                }
            });
        }
        @Override
        public int getItemCount() {
            return titleList.size();
        }
        private class UserViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            public UserViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.catname);
            }
        }
    }
}
