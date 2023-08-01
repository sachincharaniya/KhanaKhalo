package com.pooja.khanakhalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pooja.khanakhalo.ModelClass.User;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private View login,register,forgot;
    private EditText logPhone,logPassword,regName,regPhone,regPassword,forPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Main view
        login = findViewById(R.id.login);
        forgot = findViewById(R.id.forgot);
        register = findViewById(R.id.register);
        // Login page view
        logPhone = findViewById(R.id.logPhone);
        logPassword = findViewById(R.id.logPassword);
        TextView goregister = findViewById(R.id.gosignup);
        TextView goforget = findViewById(R.id.txtforgot);
        Button btnLogin = findViewById(R.id.btnSignIn);
        // Forget page view
        forPhone = findViewById(R.id.forPhone);
        Button btnForget = findViewById(R.id.btnForgot);
        // Register page view
        regName = findViewById(R.id.regName);
        regPhone = findViewById(R.id.regPhone);
        regPassword = findViewById(R.id.regPassword);
        TextView gologin = findViewById(R.id.gosignin);
        Button btnRegister = findViewById(R.id.btnSignUp);
        gologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setVisibility(View.GONE);
                forgot.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
            }
        });
        goregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.GONE);
                forgot.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);
            }
        });
        goforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.GONE);
                register.setVisibility(View.GONE);
                forgot.setVisibility(View.VISIBLE);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProcess();
            }
        });
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetProcess();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerProcess();
            }
        });


    }

    private void loginProcess() {
        if (logPhone.getText().toString().equals("") || logPassword.getText().toString().equals("")) {
            Utils.showToast(this, "Fill All Blank..!");
        } else if (logPhone.getText().toString().length() < 10) {
            Utils.showToast(this, "Invalid Mobile Number..!");
        } else if (logPassword.getText().toString().length() < 6) {
            Utils.showToast(this, "Min Password Length 6..!");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait!"); progressDialog.show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table_user = database.getReference(Constant.UserBucket);
            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(logPhone.getText().toString()).exists()) {
                        progressDialog.dismiss();
                        User user = dataSnapshot.child(logPhone.getText().toString()).getValue(User.class);
                        assert user != null;
                        user.setPhone(logPhone.getText().toString());
                        if (user.getPassword().equals(logPassword.getText().toString())) {
                            Utils.setValue(MainActivity.this,Constant.IsLogin,Constant.IsLogin);
                            Utils.setValue(MainActivity.this,Constant.Mobile,logPhone.getText().toString());
                            Utils.setValue(MainActivity.this,Constant.Password,logPassword.getText().toString());
                            Intent home = new Intent(MainActivity.this, HomeActivity.class);
                            Utils.currentUser = user; startActivity(home); finish();
                        } else {
                            Utils.showToast(MainActivity.this, "Phone no. or Password is incorrect..");
                        }
                    } else {
                        progressDialog.dismiss();
                        Utils.showToast(MainActivity.this, "Please Sign Up First..!");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Utils.showToast(MainActivity.this, databaseError.getMessage());
                }
            });
        }
    }

    private void registerProcess() {
        if (regPhone.getText().toString().equals("") || regPassword.getText().toString().equals("") || regName.getText().toString().equals("")) {
            Utils.showToast(this, "Fill All Blank..!");
        } else if (regPhone.getText().toString().length() < 10) {
            Utils.showToast(this, "Invalid Mobile Number..!");
        } else if (regPassword.getText().toString().length() < 6) {
            Utils.showToast(this, "Min Password Length 6..!");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait..!"); progressDialog.show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_user = database.getReference(Constant.UserBucket);
            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(Objects.requireNonNull(regPhone.getText()).toString()).exists()) {
                        progressDialog.dismiss();
                        Utils.showToast(MainActivity.this, "User already exists!");
                    } else {
                        progressDialog.dismiss();
                        User user = new User(Objects.requireNonNull(regName.getText()).toString(), Objects.requireNonNull(regPassword.getText()).toString(),"");
                        table_user.child(regPhone.getText().toString()).setValue(user);
                        Utils.showToast(MainActivity.this, "SignUp successfully!");
                        register.setVisibility(View.GONE);
                        forgot.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Utils.showToast(MainActivity.this, databaseError.getMessage());
                }
            });
        }
    }

    private void forgetProcess() {
        if (forPhone.getText().toString().equals("")) {
            Utils.showToast(this, "Fill All Blank..!");
        } else if (forPhone.getText().toString().length() < 10) {
            Utils.showToast(this, "Invalid Mobile Number..!");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait!"); progressDialog.show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table_user = database.getReference(Constant.UserBucket);
            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(forPhone.getText().toString()).exists()) {
                        progressDialog.dismiss();
                        User user = dataSnapshot.child(forPhone.getText().toString()).getValue(User.class);
                        assert user != null;
                        user.setPhone(forPhone.getText().toString());
                        Utils.showToast(MainActivity.this,"Your Password is " + user.getPassword());
                        register.setVisibility(View.GONE);
                        forgot.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                    } else {
                        progressDialog.dismiss();
                        Utils.showToast(MainActivity.this, "You don't have any account..!");
                        login.setVisibility(View.GONE);
                        forgot.setVisibility(View.GONE);
                        register.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Utils.showToast(MainActivity.this, databaseError.getMessage());
                }
            });
        }
    }
}