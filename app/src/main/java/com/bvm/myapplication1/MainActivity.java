package com.bvm.myapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String Username;
    String password;
    private static EditText Enter_Username;
    private static EditText Enter_password;

    private static TextView Quiz;
    private static Button btn;
    private static Button Register;
    private static String passcode;

    private static String flag = "Notdone";
    private static String R_Flag;
    DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Enter_Username = findViewById(R.id.enter_username);
        Enter_password = findViewById(R.id.enter_password);
        btn = findViewById(R.id.button);
        Register = findViewById(R.id.Register);

        Quiz = findViewById(R.id.Quiz);


        ref = FirebaseDatabase.getInstance().getReference();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Quiz1 = dataSnapshot.child("Status").getValue().toString();
                R_Flag = dataSnapshot.child("Register").getValue().toString();
                Quiz.setText(Quiz1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!R_Flag.equals("true")) {
                    Intent intent = new Intent(MainActivity.this, Register_Page.class);
                    startActivity(intent);
                }
                if(R_Flag.equals("true")){
                    Toast.makeText(MainActivity.this, "Registration is closed", Toast.LENGTH_SHORT).show();
                }
            }
        });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Username = Enter_Username.getText().toString();
                    password = Enter_password.getText().toString();
                    if (!Username.isEmpty()) {
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                passcode = dataSnapshot.child("Login Info").child(Username).child("pass").getValue().toString();
                                flag = dataSnapshot.child("Login Info").child(Username).child("flag").getValue().toString();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, "Enter correct Username", Toast.LENGTH_SHORT).show();

                            }
                        });
                        if (!flag.equals("done")) {
                            if (password.equals(passcode)) {
                                Toast.makeText(MainActivity.this, "You are successfully logged in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Second.class);
                                intent.putExtra("Username", Username);
                                startActivity(intent);
                            } else if(!password.equals(passcode)) {
                                Toast.makeText(MainActivity.this, "Enter Correct Credentials", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "You have already done the test", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this,"Enter Username First",Toast.LENGTH_SHORT).show();
                    }
                }

            });

    }


}
