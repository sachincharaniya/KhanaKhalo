package com.pooja.khanakhalo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pooja.khanakhalo.ModelClass.Cart;
import com.pooja.khanakhalo.ModelClass.Order;
import com.pooja.khanakhalo.ModelClass.Payment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  ctx = this;
        Objects.requireNonNull(getSupportActionBar()).setTitle("Process to Pay");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final WebView webview = findViewById(R.id.webview);
        webview.setBackgroundColor(Color.parseColor("#ffffff"));
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new Payment(this),Constant.INTERFACE);
        webview.getSettings().setDefaultFontSize(14);
        int amount = Utils.paidAmount; Utils.showOrder = false;
        webview.loadUrl(Constant.PAYMENT_URL + Utils.currentUser.getName() + "&TXN_AMOUNT=" + amount);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ProgressBar loader = findViewById(R.id.loader);
                loader.setVisibility(View.GONE);

            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                ImageView lyt_failed = findViewById(R.id.lyt_failed);
                webview.setVisibility(View.GONE);
                lyt_failed.setVisibility(View.VISIBLE);
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void placeOrder(final Context ctx){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_order = database.getReference(Constant.OrderBucket).child(Utils.currentUser.getPhone());
        for (Cart cart : Utils.cartList) {
            final Order order = new Order(cart.getName(), cart.getImage(), cart.getPrice(), cart.getQuantity(), cart.getKitchen(), Utils.Address, "pending","paid");
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
        ((Activity) ctx).finish();
    }

    public static void closeScreen(Activity activity){
        activity.finish();
    }

    @Override
    public void onBackPressed() {
        ((Activity) ctx).finish();
    }
}