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
import android.widget.TextView;
import android.widget.Toast;

import com.example.eCommerce.Admin.AdminHomeActivity;
import com.example.eCommerce.Model.Users;
import com.example.eCommerce.Prevalent.Prevalent;
import com.example.eCommerce.R;
import com.example.eCommerce.Sellers.SellerLoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputNumber,inputPassword;
    private Button loginButton;
    private ProgressDialog progressDialog;
    private CheckBox checkBox;
    private TextView AdminLink,NotAdminLink;
    private String parentDBName="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDBName="Admins";

            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDBName="Users";
            }
        });

    }
    private void initialization() {
        loginButton=findViewById(R.id.login_btn);
        inputNumber=findViewById(R.id.login_phone_number_input);
        inputPassword=findViewById(R.id.login_password_input);
        progressDialog=new ProgressDialog(this);
        checkBox=findViewById(R.id.remember_me_chkb);
        Paper.init(this);
        AdminLink=findViewById(R.id.admin_panel_link);
        NotAdminLink=findViewById(R.id.not_admin_panel_link);
    }

    public void LoginUser(View view) {
        String phoneNumber=inputNumber.getText().toString().trim();
        String password=inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "Please enter your phone....", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password....", Toast.LENGTH_SHORT).show();
        }
        else {
            if (checkInternet()){
                progressDialog.setTitle("Login Account");
                progressDialog.setMessage("Please waite,while we are checking the credentials");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                AllowAccessToAccount(phoneNumber,password);
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

    private void AllowAccessToAccount(final String phoneNumber, final String password) {

        if (checkBox.isChecked()){
            Paper.book().write(Prevalent.userPhoneKey,phoneNumber);
            Paper.book().write(Prevalent.userPasswordKey,password);

        }


        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBName).child(phoneNumber).exists()){
                    Users usersData=dataSnapshot.child(parentDBName).child(phoneNumber).getValue(Users.class);

                    if (usersData.getPhone().equals(phoneNumber)){
                        if (usersData.getPassword().equals(password)){
                            if (parentDBName.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Login succesfully Admin...", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Welcom "+usersData.getName(), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this, AdminHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else if (parentDBName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Login succesfully...", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Welcom "+usersData.getName(), Toast.LENGTH_SHORT).show();
                                Prevalent.currentOnlineUsers=usersData;

                                Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Password is wrong...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Wrong Phone Number...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
                else {
                    Toast.makeText(LoginActivity.this, "Not exist,please try again ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void GoToResetPassActivity(View view)
    {
        Intent intent=new Intent(LoginActivity.this, ResetPasswordActivity.class);
        intent.putExtra("check","login");
        startActivity(intent);
    }

    public void GoToSellerLoginActivity(View view) {
        startActivity(new Intent(LoginActivity.this, SellerLoginActivity.class));

    }
}
