package com.pooja.khanakhalo;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pooja.khanakhalo.ModelClass.Cart;
import com.pooja.khanakhalo.ModelClass.Kitchen;
import com.pooja.khanakhalo.ModelClass.User;

import java.util.List;

public class Utils {

    private static SharedPreferences sp;
    private static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference mDatabaseReference;
    public static User currentUser;
    public static Kitchen categoryData;
    public static String returnValue = "";
    public static boolean showCart = false;
    public static boolean showOrder = false;
    public static List<Cart> cartList;
    public static String Address;
    public static int paidAmount;

    // Check The Network Information
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo();
    }

    // Check Is There Any Connectivity
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    // Make A Toast Method For All Activity
    public static void showToast(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    // Store Value to Firebase Database
    public static void setData(String bucket, String tag, String value) {
        mDatabaseReference = mDatabase.getReference().child(bucket).child(tag);
        mDatabaseReference.setValue(value);
    }

    // Call Data From Firebase Database
    public static void getData(final Context context, String bucket, String id){
        mDatabaseReference = mDatabase.getReference().child(bucket).child(id);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                returnValue = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Store Value to Shared Preference
    public static void setValue(Context context, String tag, String val) {
        sp = context.getSharedPreferences(Constant.SharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(tag,val);
        editor.apply();
    }

    // Call Data From Shared Preference
    public static String getValue(Context context, String key) {
        sp = context.getSharedPreferences(Constant.SharedPrefName, Context.MODE_PRIVATE);
        String value = null;
        if(sp.contains(key)) {
            value = sp.getString(key,"");
        } else {
            Toast.makeText(context,"Tag Not Found",Toast.LENGTH_SHORT).show();
        }
       return value;
    }

    // Checking User Login Status
    public static boolean isLogin(Context context) {
        sp = context.getSharedPreferences(Constant.SharedPrefName, Context.MODE_PRIVATE);
        return sp.contains(Constant.IsLogin) && sp.getString(Constant.IsLogin,"").equals(Constant.IsLogin);
    }

    // Checking Is User Visit First Time
    public static boolean isFirstTime(Context context) {
        sp = context.getSharedPreferences(Constant.SharedPrefName, Context.MODE_PRIVATE);
        return sp.contains(Constant.IsFirstTime) && sp.getString(Constant.IsFirstTime,"").equals(Constant.IsFirstTime);
    }


}
