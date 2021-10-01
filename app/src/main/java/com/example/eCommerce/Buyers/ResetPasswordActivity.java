package com.example.eCommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eCommerce.Prevalent.Prevalent;
import com.example.eCommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check="";
    private TextView pageTitle,questionTitle;
    private EditText question1,question2,phone;
    private Button verifybtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Initialization();
        check=getIntent().getStringExtra("check");

    }

    private void Initialization() {
        pageTitle=findViewById(R.id.reset_title);
        phone=findViewById(R.id.find_phone_number);
        question1=findViewById(R.id.question1);
        question2=findViewById(R.id.question2);
        questionTitle=findViewById(R.id.question_title_textView);
        verifybtn=findViewById(R.id.verify_answers_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();

        phone.setVisibility(View.GONE);


        if (check.equals("settings"))
        {
            pageTitle.setText("Set Questions");
            questionTitle.setText("Please, set answers for security questions");
            verifybtn.setText("Set");

            DisplayPreviousAnswers();

            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetAnswers();
                }
            });

        }

        else if (check.equals("login"))
        {
            phone.setVisibility(View.VISIBLE);

            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerifyUser();
                }
            });

        }
    }

    private void SetAnswers(){

        String answer1=question1.getText().toString().toLowerCase();
        String answer2=question2.getText().toString().toLowerCase();

        if (question1.equals("") && question2.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this, "Please, answer both questions", Toast.LENGTH_SHORT).show();
        }
        else if (question1.equals("")){
            question1.setError("required");
            Toast.makeText(ResetPasswordActivity.this, "Please, answer question1", Toast.LENGTH_SHORT).show();

        }
        else if (question2.equals(""))
        {
            question2.setError("required");
            Toast.makeText(ResetPasswordActivity.this, "Please, answer question2", Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUsers.getPhone());
            HashMap<String,Object> userData=new HashMap<>();
            userData.put("answer1",answer1);
            userData.put("answer2",answer2);

            ref.child("security questions").updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this, "you have saved your answers about security questions", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPasswordActivity.this, SettingActivity.class));

                    }
                }
            });

        }
    }
    private void DisplayPreviousAnswers(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Prevalent.currentOnlineUsers.getPhone());
        ref.child("security questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()){
                    String answer1=dataSnapshot.child("answer1").getValue().toString().trim();
                    String answer2=dataSnapshot.child("answer2").getValue().toString().trim();

                    question1.setText(answer1);
                    question2.setText(answer2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void VerifyUser() {
        final String userPhone = phone.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question2.getText().toString().toLowerCase();

        if (!userPhone.equals("") && !answer1.equals("") && !answer2.equals("")) {

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(userPhone);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String mPhone = dataSnapshot.child("phone").getValue().toString();
                            if (dataSnapshot.hasChild("security questions")) {
                                String ans1 = dataSnapshot.child("security questions").child("answer1").getValue().toString().trim();
                                String ans2 = dataSnapshot.child("security questions").child("answer2").getValue().toString().trim();
                                ;
                                if (!ans1.equals(answer1)) {
                                    Toast.makeText(ResetPasswordActivity.this, "The answer of first question is wrong try again.", Toast.LENGTH_SHORT).show();
                                }
                                if (!ans1.equals(answer1)) {
                                    Toast.makeText(ResetPasswordActivity.this, "The answer of first question is wrong try again.", Toast.LENGTH_SHORT).show();
                                } else if (!ans2.equals(answer2)) {
                                    Toast.makeText(ResetPasswordActivity.this, "The answer of second question is wrong try again.", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                    builder.setTitle("New Password");
                                    final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                    newPassword.setHint("write new password...");
                                    builder.setView(newPassword);
                                    builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!newPassword.getText().toString().equals("")) {
                                                ref.child("password").setValue(newPassword.getText().toString().trim())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(ResetPasswordActivity.this, "Password change successfully...", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();
                                }

                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "you don't have any security questions.", Toast.LENGTH_SHORT).show();

                            }


                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "this user phone not exist", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {
            phone.setError("required");
            question1.setError("required");
            question2.setError("required");
            Toast.makeText(this, "Please, enter your phone,answer1 and answer2 ", Toast.LENGTH_SHORT).show();
        }
    }
}
