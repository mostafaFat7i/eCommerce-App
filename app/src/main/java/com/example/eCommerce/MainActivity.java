package com.example.eCommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eCommerce.Buyers.HomeActivity;
import com.example.eCommerce.Buyers.LoginActivity;
import com.example.eCommerce.Buyers.RegisterActivity;
import com.example.eCommerce.Model.Users;
import com.example.eCommerce.Prevalent.Prevalent;
import com.example.eCommerce.Sellers.SellerHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton,loginButton;
    private ProgressDialog progressDialog;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        checkInternet();

        String userPhoneKey=Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey=Paper.book().read(Prevalent.userPasswordKey);

        if (userPhoneKey != "" && userPasswordKey != ""){
             if (!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey)){
                 AllowAccess(userPhoneKey,userPasswordKey);

                 progressDialog.setTitle("Already Logged In");
                 progressDialog.setMessage("Please waite.....");
                 progressDialog.setCanceledOnTouchOutside(false);
                 progressDialog.show();
             }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser !=null){
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void checkInternet(){


        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

        if (!connected)
            Toast.makeText(this, "Please make sure you are connected to the internet", Toast.LENGTH_SHORT).show();
    }

    private void AllowAccess(final String phoneNumber, final String password) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phoneNumber).exists()){
                    Users usersData=dataSnapshot.child("Users").child(phoneNumber).getValue(Users.class);

                    if (usersData.getPhone().equals(phoneNumber)){
                        if (usersData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "You are already login...", Toast.LENGTH_SHORT).show();
                            Prevalent.currentOnlineUsers=usersData;
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Welcom "+usersData.getName(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Password is wrong...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Wrong Phone Number...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
                else {
                    Toast.makeText(MainActivity.this, "Not exist,please try again ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialization() {
        joinNowButton=findViewById(R.id.main_join_now_btn);
        loginButton=findViewById(R.id.main_login_btn);
        progressDialog=new ProgressDialog(this);
        userRef=FirebaseDatabase.getInstance().getReference();
        Paper.init(this);
    }

    public void ToLoginActivity(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void ToRegisterActivity(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));

    }
}
