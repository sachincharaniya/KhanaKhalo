package com.pooja.khanakhalo.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pooja.khanakhalo.CartList;
import com.pooja.khanakhalo.Constant;
import com.pooja.khanakhalo.ModelClass.Cart;
import com.pooja.khanakhalo.ModelClass.Order;
import com.pooja.khanakhalo.PaymentActivity;
import com.pooja.khanakhalo.R;
import com.pooja.khanakhalo.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CartFragment extends Fragment {

    private int Totalprice = 0;
    private RelativeLayout maincart;
    private RecyclerView recyclerView;
    private List<Cart> cartList;
    private Context ctx;
    private TextView amount;
    private ImageView nocart;
    private EditText Name,Phone,address,landmark;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cart,container,false);
        ctx = getContext(); assert ctx != null;
        recyclerView = view.findViewById(R.id.recycler_cart);
        maincart = view.findViewById(R.id.maincart);
        nocart = view.findViewById(R.id.nocart);
        recyclerView.setHasFixedSize(true); Utils.showCart = false;
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        amount = view.findViewById(R.id.amount);
        initialize();
        Name = view.findViewById(R.id.orderName);
        Name.setText(Utils.currentUser.getName());
        Phone = view.findViewById(R.id.orderPhone);
        Phone.setText(Utils.currentUser.getPhone());
        address = view.findViewById(R.id.orderAddress);
        address.setText(Utils.currentUser.getAddress());
        landmark = view.findViewById(R.id.orderlandmark);
        Button clearorder = view.findViewById(R.id.clearorder);
        clearorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartList.clearAll(ctx);
                maincart.setVisibility(View.GONE);
                nocart.setVisibility(View.VISIBLE);
            }
        });
        Button placeorder = view.findViewById(R.id.placeorder);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Name.getText().toString().equals("")&&!Phone.getText().toString().equals("")&&!address.getText().toString().equals("")){
                    setupPayment();
                } else {
                    Utils.showToast(ctx,"Fill Delivery Adddress");
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize() {
        Totalprice = 0;
        Map<String, ?> allEntries = CartList.getAllCart(ctx);
        if(allEntries.isEmpty()){
            maincart.setVisibility(View.GONE);
            nocart.setVisibility(View.VISIBLE);
        } else {
            cartList = new ArrayList<>();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Cart cart = CartList.getCart(ctx, entry.getKey());
                if (cart.getQuantity() > 0) {
                    Totalprice = (int) (Totalprice + (cart.getQuantity()*cart.getPrice()));
                    cartList.add(cart);
                }
            }
            amount.setText(String.valueOf(Totalprice));
            RecyclerAdapter adapter = new RecyclerAdapter(cartList, ctx);
            recyclerView.setAdapter(adapter);
            nocart.setVisibility(View.GONE);
            maincart.setVisibility(View.VISIBLE);
        }
    }

    private static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private List<Cart> ITEM_LIST;
        private Context activity;
        RecyclerAdapter(List<Cart> ITEM_LIST, Context activity) {
            this.ITEM_LIST = ITEM_LIST;
            this.activity = activity;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final Cart cart = ITEM_LIST.get(position);
            holder.CartName.setText(cart.getName());
            Glide.with(activity).load(cart.getImage()).into(holder.CartImage);
            holder.CartQuantity.setText(String.valueOf(cart.getQuantity()));
            holder.CartPrice.setText(String.valueOf(cart.getPrice()));
            holder.CartTotal.setText("Total - " + String.valueOf(cart.getPrice()*cart.getQuantity()) + "/-");
        }
        @Override
        public int getItemCount() {
            return ITEM_LIST.size();
        }
        static class ViewHolder extends RecyclerView.ViewHolder{
            private TextView CartName;
            private ImageView CartImage;
            private TextView CartPrice;
            private TextView CartQuantity;
            private TextView CartTotal;
            ViewHolder(View itemView) {
                super(itemView);
                CartName = itemView.findViewById(R.id.food_name);
                CartImage = itemView.findViewById(R.id.food_image);
                CartPrice = itemView.findViewById(R.id.food_price);
                CartQuantity = itemView.findViewById(R.id.food_quantity);
                CartTotal = itemView.findViewById(R.id.total);
            }
        }
    }

    private void setupPayment(){
        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.option);
        dialog.setCancelable(true);
        Button cod = dialog.findViewById(R.id.cod);
        Button paytm = dialog.findViewById(R.id.paytm);
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); String add = Constant.Name + "- " + Name.getText().toString() + "\n" +
                        Constant.Phone + "- " + Phone.getText().toString() + "\n" +
                        Constant.Address + "- " + address.getText().toString() + "\n" +
                        Constant.Landmark + "- " + landmark.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table_order = database.getReference(Constant.OrderBucket).child(Utils.currentUser.getPhone());
                for (Cart cart : cartList) {
                    final Order order = new Order(cart.getName(), cart.getImage(), cart.getPrice(), cart.getQuantity(), cart.getKitchen(), add, "pending","COD");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    final String date = simpleDateFormat.format(new Date()) + cart.getName();
                    table_order.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            table_order.child(date).setValue(order);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Utils.showToast(ctx, databaseError.getMessage());
                        }
                    });
                }
                Utils.showToast(ctx, "Order successfully placed!");
                CartList.clearAll(ctx);
                maincart.setVisibility(View.GONE);
                nocart.setVisibility(View.VISIBLE);
            }
        });
        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(ctx, PaymentActivity.class);
                Utils.cartList = cartList; Utils.paidAmount = Totalprice;
                Utils.Address = Constant.Name + "- " + Name.getText().toString() + "\n" +
                        Constant.Phone + "- " + Phone.getText().toString() + "\n" +
                        Constant.Address + "- " + address.getText().toString() + "\n" +
                        Constant.Landmark + "- " + landmark.getText().toString();
                startActivity(i);
                CartList.clearAll(ctx);
                maincart.setVisibility(View.GONE);
                nocart.setVisibility(View.VISIBLE);
            }
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
