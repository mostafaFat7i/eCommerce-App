package com.example.eCommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eCommerce.Prevalent.Prevalent;
import com.example.eCommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText shipName,shipPhone,shipAddress,shipCity;
    private Button confirm;
    private String totalAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        Initialization();
        totalAmount= getIntent().getStringExtra("Total Price");

    }

    private void Initialization() {

        shipName=findViewById(R.id.shipment_name);
        shipPhone=findViewById(R.id.shipment_phone);
        shipAddress=findViewById(R.id.shipment_address);
        shipCity=findViewById(R.id.shipment_city);
        confirm=findViewById(R.id.shipment_confirm_btn);
    }

    public void Check(View view) {

        if (TextUtils.isEmpty(shipName.getText().toString())){

            shipName.setError("required");
            Toast.makeText(this, "Please provide your full name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(shipPhone.getText().toString())){

            shipPhone.setError("required");
            Toast.makeText(this, "Please provide your phone number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(shipAddress.getText().toString())){

            shipAddress.setError("required");
            Toast.makeText(this, "Please provide your address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(shipCity.getText().toString())){

            shipCity.setError("required");
            Toast.makeText(this, "Please provide your city name", Toast.LENGTH_SHORT).show();
        }
        else {
            ConfirmOrder();
        }

    }

    private void ConfirmOrder() {

        final String saveCurrentDate,saveCurrentTime;

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(Prevalent.currentOnlineUsers.getPhone());

        HashMap<String,Object> orderMap=new HashMap<>();

        orderMap.put("totalAmount",totalAmount);
        orderMap.put("name",shipName.getText().toString());
        orderMap.put("phone",shipPhone.getText().toString());
        orderMap.put("address",shipAddress.getText().toString());
        orderMap.put("city",shipCity.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("state","not shipped");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful()){

                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                            .child(Prevalent.currentOnlineUsers.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(ConfirmFinalOrderActivity.this, "Your final order has been uploaded successfully..", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }

            }
        });

    }
}
