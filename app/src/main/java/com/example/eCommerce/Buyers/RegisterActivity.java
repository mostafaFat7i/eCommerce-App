package com.example.eCommerce.Buyers;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.eCommerce.MainActivity;
import com.example.eCommerce.R;
import com.example.eCommerce.Sellers.SellerRegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    EditText userNAme,userPhone,userPassword;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialization();
    }

    private void initialization() {
        createAccountButton=findViewById(R.id.register_btn);
        userNAme=findViewById(R.id.register_username_input);
        userPassword=findViewById(R.id.register_password_input);
        userPhone=findViewById(R.id.register_phone_number_input);
        progressDialog=new ProgressDialog(this);
    }

    public void CreateAccount(View view) {
        String name=userNAme.getText().toString();
        String phone=userPhone.getText().toString();
        String password=userPassword.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name....", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone....", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password....", Toast.LENGTH_SHORT).show();
        }
        else {
            if (checkInternet()){
                progressDialog.setTitle("Create Account");
                progressDialog.setMessage("Please waite,while we are checking the credentials");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                VailidatePhoneNumber(name,phone,password);
            }
            else
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean checkInternet(){


        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

        return connected;

    }
    private void VailidatePhoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())){

                    HashMap<String,Object> userData=new HashMap<>();
                    userData.put("name",name);
                    userData.put("password",password);
                    userData.put("phone",phone);

                    RootRef.child("Users").child(phone).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Your account has been created...", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network error please try again after soon time...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else {
                    Toast.makeText(RegisterActivity.this, "this "+phone+" is already exists", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "please try again using another number", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void GoToSellerRegisterActivity(View view) {
        startActivity(new Intent(RegisterActivity.this, SellerRegistrationActivity.class));
    }
}
