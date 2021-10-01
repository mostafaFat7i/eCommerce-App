package com.example.eCommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eCommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private EditText Sname, Sphone, Semail, Spassword, Saddress;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
        Initialization();
    }

    private boolean checkInternet() {


        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

        return connected;

    }

    private void Initialization() {
        Sname = findViewById(R.id.seller_regester_name);
        Sphone = findViewById(R.id.seller_regester_phone);
        Semail = findViewById(R.id.seller_register_email);
        Spassword = findViewById(R.id.seller_register_password);
        Saddress = findViewById(R.id.seller_regester_address);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

    }


    public void RegisterSeller(View view) {
        final String name = Sname.getText().toString();
        final String phone = Sphone.getText().toString();
        final String email = Semail.getText().toString();
        final String password = Spassword.getText().toString();
        final String address = Saddress.getText().toString();


        if (!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals("")) {
            if (checkInternet()){
                progressDialog.setTitle("Creating Seller Account");
                progressDialog.setMessage("Please waite,while we are checking the credentials");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference();
                            String uID = mAuth.getCurrentUser().getUid();

                            HashMap<String, Object> sellerData = new HashMap<>();
                            sellerData.put("sid", uID);
                            sellerData.put("name", name);
                            sellerData.put("phone", phone);
                            sellerData.put("password", password);
                            sellerData.put("email", email);
                            sellerData.put("address", address);

                            sellerRef.child("Sellers").child(uID).updateChildren(sellerData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(SellerRegistrationActivity.this, "you are register successfully...", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });


                        }
                    }
                });
            }
            else
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            
        } else {
            Toast.makeText(this, "Please, make sure you are full all data in the form.", Toast.LENGTH_LONG).show();
        }


    }
}
