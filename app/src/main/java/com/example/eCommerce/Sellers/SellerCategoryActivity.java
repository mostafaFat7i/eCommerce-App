package com.example.eCommerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.eCommerce.Buyers.HomeActivity;
import com.example.eCommerce.R;

public class SellerCategoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_category);
    }

    public void GoToAddTShirt(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","tShirts");
        startActivity(intent);
    }

    public void GoToAddSportTShirt(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","sportTShirts");
        startActivity(intent);
    }

    public void GoToAddFemailDress(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","femailDress");
        startActivity(intent);
    }

    public void GoToAddSweater(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","sweater");
        startActivity(intent);
    }

    public void GoToAddGlasses(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","glasses");
        startActivity(intent);
    }

    public void GoToAddWalletBag(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","walletBag");
        startActivity(intent);
    }

    public void GoToAddHatesCap(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","hatesCap");
        startActivity(intent);
    }

    public void GoToAddShoeses(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","shoesses");
        startActivity(intent);
    }

    public void GoToAddHeadPhones(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","headPhones");
        startActivity(intent);
    }

    public void GoToAddLabtops(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","labtop");
        startActivity(intent);
    }

    public void GoToAddWatches(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","watches");
        startActivity(intent);
    }

    public void GoToAddMobile(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerAddNewProductActivity.class);
        intent.putExtra("category","mobiles");
        startActivity(intent);
    }

    public void GoToSellerMaintainProducts(View view)
    {
        Intent intent=new Intent(SellerCategoryActivity.this, HomeActivity.class);
        intent.putExtra("Type", "Seller");
        startActivity(intent);
    }

    public void GoToHomeActivity(View view) {
        Intent intent=new Intent(SellerCategoryActivity.this, SellerHomeActivity.class);
        startActivity(intent);
    }
}
