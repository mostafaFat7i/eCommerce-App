package com.example.eCommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eCommerce.Model.Products;
import com.example.eCommerce.R;
import com.example.eCommerce.VIewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryProductsActivity extends AppCompatActivity {

    private RecyclerView categoryProductList;
    private DatabaseReference productsRef;
    private String pType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            pType = getIntent().getStringExtra("PType");
        }

        categoryProductList = findViewById(R.id.category_products_list);
        categoryProductList.setHasFixedSize(true);
        categoryProductList.setLayoutManager(new LinearLayoutManager(this));
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        RetrieveProducts(pType);
//        if (pType.equals("tShirts")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("sportTShirts")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("femailDress")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("sweater")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("glasses")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("mobiles")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("labtop")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("watches")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("headPhones")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("shoesses")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("hatesCap")) {
//            RetrieveProducts(pType);
//        }
//        if (pType.equals("walletBag")) {
//            RetrieveProducts(pType);
//        }



    }

    private void RetrieveProducts(String pType) {
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo(pType), Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.productItemName.setText(model.getName());
                        holder.productItemDescription.setText(model.getDescription());
                        holder.productItemPrice.setText("Price: " + model.getPrice() + " $");

                        Picasso.get().load(model.getImage()).into(holder.productItemImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (model.getProductState().equals("approved")) {
                                    Intent intent = new Intent(CategoryProductsActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(CategoryProductsActivity.this, "Sorry,this product not approved yet.Please try later ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        categoryProductList.setAdapter(adapter);
        adapter.startListening();

    }

}
