package com.example.eCommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckNewProductsActivity extends AppCompatActivity {

    private RecyclerView newProductsList;
    private DatabaseReference newProductsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_products);

        newProductsList=findViewById(R.id.new_products_list);
        newProductsList.setLayoutManager(new LinearLayoutManager(this));

        newProductsRef= FirebaseDatabase.getInstance().getReference().child("products");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(newProductsRef.orderByChild("productState").equalTo("not approved"),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
            {
                holder.productItemName.setText(model.getName());
                holder.productItemDescription.setText(model.getDescription());
                holder.productItemPrice.setText("Price: "+model.getPrice()+" $");

                Picasso.get().load(model.getImage()).into(holder.productItemImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        final String productID=model.getPid();
                        CharSequence charSequence[]=new CharSequence[]{"Yes","No"};
                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminCheckNewProductsActivity.this);
                        builder.setTitle("Approve this product ?");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0)
                                {
                                    ChangeProductState(productID);

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
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        newProductsList.setAdapter(adapter);
        adapter.startListening();
    }

    private void ChangeProductState(String productID)
    {
        newProductsRef.child(productID).child("productState").setValue("approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminCheckNewProductsActivity.this, "This item is approved successfully...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
