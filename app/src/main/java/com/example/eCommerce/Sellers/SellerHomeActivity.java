package com.example.eCommerce.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eCommerce.MainActivity;
import com.example.eCommerce.Model.Products;
import com.example.eCommerce.R;
import com.example.eCommerce.VIewHolder.SellerItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeActivity extends AppCompatActivity {

    private RecyclerView sellerHomeList;
    private DatabaseReference unverifiedProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        sellerHomeList=findViewById(R.id.seller_home_list);
        sellerHomeList.setLayoutManager(new LinearLayoutManager(this));

        unverifiedProductsRef= FirebaseDatabase.getInstance().getReference().child("products");

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intentHome = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                        startActivity(intentHome);
                        return true;

                    case R.id.navigation_add:
                        Intent intent1 = new Intent(SellerHomeActivity.this, SellerCategoryActivity.class);
                        startActivity(intent1);
                        return true;

                    case R.id.navigation_logout:
                        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        mAuth.signOut();
                        Intent intent2 = new Intent(SellerHomeActivity.this, MainActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        finish();
                        return true;
                }
                return false;
            }
        });

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String sellerId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unverifiedProductsRef.orderByChild("sellerID").equalTo(sellerId),Products.class).build();

        FirebaseRecyclerAdapter<Products, SellerItemViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, SellerItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerItemViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.productItemName.setText(model.getName());
                        holder.productItemDescription.setText(model.getDescription());
                        holder.productItemStatus.setText("State: "+model.getProductState());
                        holder.productItemPrice.setText("Price: "+model.getPrice()+" $");

                        Picasso.get().load(model.getImage()).into(holder.productItemImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                final String productID=model.getPid();
                                CharSequence charSequence[]=new CharSequence[]{"Yes","No"};
                                AlertDialog.Builder builder=new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Delete this product ?");
                                builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which==0)
                                        {
                                            DeleteProduct(productID);
                                        }
                                        if (which==1){

                                        }

                                    }
                                });
                                builder.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SellerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                        SellerItemViewHolder holder = new SellerItemViewHolder(view);
                        return holder;
                    }
                };
        sellerHomeList.setAdapter(adapter);
        adapter.startListening();

    }

    private void DeleteProduct(String productID)
    {
        unverifiedProductsRef.child(productID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SellerHomeActivity.this, "This item has been deleted successfully...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
