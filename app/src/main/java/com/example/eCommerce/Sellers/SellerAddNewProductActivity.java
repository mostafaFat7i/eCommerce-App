package com.example.eCommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eCommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {

    private String categoryName,description,price,Pname,saveCurrentDate,saveCurrentTimae,productRandomKey,downloadImageUrl;
    private Button addNewProductBtn;
    private ImageView inputProductImage;
    private EditText productName,productDescription,productPrice;
    private static final int gallaryPick=1;
    private Uri ImageUri;
    private ProgressDialog progressDialog;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef,SellerRef;
    private String sName,sPhone,sAddress,sEmail,sID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        categoryName=getIntent().getExtras().get("category").toString();

        Initialization();

        SellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            sName=dataSnapshot.child("name").getValue().toString();
                            sID=dataSnapshot.child("sid").getValue().toString();
                            sAddress=dataSnapshot.child("address").getValue().toString();
                            sPhone=dataSnapshot.child("phone").getValue().toString();
                            sEmail=dataSnapshot.child("email").getValue().toString();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void Initialization() {
        addNewProductBtn=findViewById(R.id.add_new_product);
        inputProductImage=findViewById(R.id.select_product_image);
        productDescription=findViewById(R.id.product_description);
        productName=findViewById(R.id.product_name);
        productPrice=findViewById(R.id.product_price);
        ProductImageRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef=FirebaseDatabase.getInstance().getReference().child("products");
        progressDialog=new ProgressDialog(this);
        SellerRef=FirebaseDatabase.getInstance().getReference().child("Sellers");

    }


    public void SelectImageFromGallary(View view) {

        Intent gallaryIntent=new Intent();
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        gallaryIntent.setType("image/*");
        startActivityForResult(gallaryIntent,gallaryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==gallaryPick && resultCode==RESULT_OK && data!=null){

            ImageUri=data.getData();
            inputProductImage.setImageURI(ImageUri);
        }

    }

    public void ValidateProductData(View view) {

        description=productDescription.getText().toString();
        price=productPrice.getText().toString();
        Pname=productName.getText().toString();
        
        if (ImageUri==null){

            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(description)){
            productDescription.setError("required");
        }
        else if (TextUtils.isEmpty(price)){
            productPrice.setError("required");
        }
        else if (TextUtils.isEmpty(Pname)){
            productName.setError("required");
        }
        else {

            StoreProductInformation();
        }

    }

    private void StoreProductInformation() {

        progressDialog.setTitle("Add New Product");
        progressDialog.setMessage("Please waite,while We are adding new product");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentDate=currentDate.format(calendar.getTime());


        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTimae=currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate+saveCurrentTimae;

        final StorageReference filePath=ProductImageRef.child(ImageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SellerAddNewProductActivity.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this, "Image Uploaded successfully....", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()){
                            downloadImageUrl=task.getResult().toString();
                            SaveProductInformationInDB();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInformationInDB() {
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTimae);
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("name",Pname);

        productMap.put("sellerName",sName);
        productMap.put("sellerAddress",sAddress);
        productMap.put("sellerPhone",sPhone);
        productMap.put("sellerEmail",sEmail);
        productMap.put("sellerID",sID);

        productMap.put("productState","not approved");


        ProductRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    startActivity(new Intent(SellerAddNewProductActivity.this, SellerHomeActivity.class));
                    Toast.makeText(SellerAddNewProductActivity.this, "product is added successfully...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
                else {
                    Toast.makeText(SellerAddNewProductActivity.this, "Error: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }
        });

    }


}
