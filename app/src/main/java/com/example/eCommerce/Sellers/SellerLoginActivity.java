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

public class SellerLoginActivity extends AppCompatActivity {

    private EditText logMail,logPass;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        Initialization();
    }

    private void Initialization() {
        logMail=findViewById(R.id.seller_login_email);
        logPass=findViewById(R.id.seller_login_password);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();


    }
    private boolean checkInternet(){


        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

        return connected;

    }

    public void LoginSeller(View view) {
        final String email=logMail.getText().toString();
        final String password=logPass.getText().toString();

        if (!email.equals("") && !password.equals("")) {
            if (checkInternet()){
                progressDialog.setTitle("Seller Account Login");
                progressDialog.setMessage("Please waite,while we are checking the credentials");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(SellerLoginActivity.this, "you are login successfully...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(SellerLoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
            else
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "Please, make sure you are write email and password.", Toast.LENGTH_LONG).show();

        }
    }
}
