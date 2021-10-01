package com.example.eCommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.eCommerce.Buyers.HomeActivity;
import com.example.eCommerce.MainActivity;
import com.example.eCommerce.R;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void Logout(View view) {

        Intent intent=new Intent(AdminHomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void GoToAdminNewOrders(View view) {

        Intent intent=new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
        startActivity(intent);
    }

    public void GoToMaintainProducts(View view) {

        Intent intent=new Intent(AdminHomeActivity.this, HomeActivity.class);
        intent.putExtra("Type", "Admin");
        startActivity(intent);
    }

    public void GoToAdminNewProducts(View view)
    {
        Intent intent=new Intent(AdminHomeActivity.this, AdminCheckNewProductsActivity.class);
        startActivity(intent);
    }
}
