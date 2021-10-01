package com.example.eCommerce.Buyers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.eCommerce.R;
import com.rey.material.widget.ListView;

import java.util.ArrayList;

public class UserCategoryActivity extends AppCompatActivity {


    private android.widget.ListView categoryList;
    private ArrayAdapter adapter;
    private ArrayList<String> listItems=new ArrayList();

    String categories[] = {"T-Shirts","Sport T-Shirts",
            "Femail Dress","Sweater","Glasses","Wallet Bag","Hates Cap",
            "Shoesses","Head Phones","Labtop","Watches","Mobiles" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category);

        categoryList=findViewById(R.id.categories_list);

        ArrayAdapter<String> arr=new ArrayAdapter<String>(this,
                R.layout.category_list,R.id.category_list_item, categories);

        categoryList.setAdapter(arr);

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        GoToShirtsProducts();
                        break;
                    case 1:
                        GoToSportShirtsProducts();
                        break;
                    case 2:
                        GoToFemailDressProducts();
                        break;
                    case 3:
                        GoToSweaterProducts();
                        break;
                    case 4:
                        GoToGlassesProducts();
                        break;
                    case 5:
                        GoToBagsProducts();
                        break;
                    case 6:
                        GoToHatesCapProducts();
                        break;
                    case 7:
                        GoToShoessesProducts();
                        break;
                    case 8:
                        GoToHeadPhonesProducts();
                        break;
                    case 9:
                        GoToLabtopProducts();
                        break;
                    case 10:
                        GoToWatchesProducts();
                        break;
                    case 11:
                        GoToMobilesProducts();
                        break;

                }
            }
        });

    }

    public void GoToShirtsProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","tShirts");
        startActivity(intent);
    }

    public void GoToSportShirtsProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","sportTShirts");
        startActivity(intent);
    }

    public void GoToFemailDressProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","femailDress");
        startActivity(intent);
    }

    public void GoToSweaterProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","sweater");
        startActivity(intent);
    }

    public void GoToGlassesProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","glasses");
        startActivity(intent);
    }

    public void GoToMobilesProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","mobiles");
        startActivity(intent);
    }

    public void GoToLabtopProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","labtop");
        startActivity(intent);
    }

    public void GoToWatchesProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","watches");
        startActivity(intent);
    }

    public void GoToHeadPhonesProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","headPhones");
        startActivity(intent);
    }

    public void GoToShoessesProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","shoesses");
        startActivity(intent);
    }

    public void GoToHatesCapProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","hatesCap");
        startActivity(intent);
    }

    public void GoToBagsProducts() {
        Intent intent=new Intent(UserCategoryActivity.this,CategoryProductsActivity.class);
        intent.putExtra("PType","walletBag");
        startActivity(intent);
    }
}
