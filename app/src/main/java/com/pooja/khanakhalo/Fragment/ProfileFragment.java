package com.pooja.khanakhalo.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pooja.khanakhalo.Constant;
import com.pooja.khanakhalo.MainActivity;
import com.pooja.khanakhalo.ModelClass.User;
import com.pooja.khanakhalo.R;
import com.pooja.khanakhalo.Utils;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false); ctx = getContext();
        final EditText naam = view.findViewById(R.id.nam);
        final EditText mobile = view.findViewById(R.id.phone);
        final EditText address = view.findViewById(R.id.address);
        naam.setText(Utils.currentUser.getName());
        mobile.setText(Utils.currentUser.getPhone());
        address.setText(Utils.currentUser.getAddress());
        Button save = view.findViewById(R.id.btnsave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!naam.getText().toString().equals("") || !mobile.getText().toString().equals("") || !address.getText().toString().equals("")) {
                    final ProgressDialog progressDialog = new ProgressDialog(ctx);
                    progressDialog.setMessage("Please wait..!");
                    progressDialog.show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference table_user = database.getReference(Constant.UserBucket);
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(Utils.currentUser.getPhone()).exists()) {
                                User user = new User(Objects.requireNonNull(naam.getText()).toString(), Utils.currentUser.getPassword(), Objects.requireNonNull(address.getText()).toString());
                                table_user.child(Utils.currentUser.getPhone()).setValue(user);
                                user.setPhone(Utils.currentUser.getPhone()); Utils.currentUser = user;
                                progressDialog.dismiss();
                                Utils.showToast(ctx, "Update successfully!");
                            } else {
                                progressDialog.dismiss();
                                Utils.showToast(ctx, "Geting Error!");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Utils.showToast(ctx, databaseError.getMessage());
                        }
                    });
                }
            }
        });
        return view;
    }
}
