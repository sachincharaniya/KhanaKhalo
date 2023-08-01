package com.pooja.khanakhalo.Fragment;

import android.content.Context;
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
import com.pooja.khanakhalo.Constant;
import com.pooja.khanakhalo.ModelClass.Order;
import com.pooja.khanakhalo.R;
import com.pooja.khanakhalo.Utils;

public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private Context ctx;
    private ImageView nocart;
    private FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order,container,false);
        ctx = getContext(); assert ctx != null;
        recyclerView = view.findViewById(R.id.recycler_order);
        nocart = view.findViewById(R.id.nocart);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        loadOrderList();
        adapter.startListening();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
        if(adapter.getItemCount()>0){
            nocart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.GONE);
            nocart.setVisibility(View.VISIBLE);
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView FoodName;
        public ImageView FoodImage;
        public TextView FoodPrice;
        public TextView FoodQuantity;
        public TextView FoodStatus;
        public OrderViewHolder(View itemView) {
            super(itemView);
            FoodName = itemView.findViewById(R.id.food_name);
            FoodImage = itemView.findViewById(R.id.food_image);
            FoodPrice = itemView.findViewById(R.id.food_price);
            FoodQuantity = itemView.findViewById(R.id.food_quantity);
            FoodStatus = itemView.findViewById(R.id.status);
        }
    }

    private void loadOrderList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.OrderBucket).child(Utils.currentUser.getPhone());
        databaseReference.keepSynced(true);
        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child(Constant.OrderBucket).child(Utils.currentUser.getPhone());
        final Query personsQuery = personsRef.orderByKey();
        final FirebaseRecyclerOptions<Order> personsOptions = new FirebaseRecyclerOptions.Builder<Order>().setQuery(personsQuery,Order.class).build();
        adapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(personsOptions) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_view, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final OrderViewHolder viewHolder, final int i, @NonNull final Order model) {
                viewHolder.FoodName.setText(model.getName());
                Glide.with(ctx).load(model.getImage()).into(viewHolder.FoodImage);
                viewHolder.FoodPrice.setText(model.getQuantity() + " Item × Rs. " + String.valueOf(model.getPrice()) + "/-");
                viewHolder.FoodQuantity.setText("From " + model.getKitchen());
                viewHolder.FoodStatus.setText(model.getStatus());
            }

        };
        recyclerView.setAdapter(adapter);
        if(adapter.getItemCount()>0){
            nocart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.GONE);
            nocart.setVisibility(View.VISIBLE);
        }
    }
}
