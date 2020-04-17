package com.bvm.myapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register_Page extends AppCompatActivity {
    private static EditText ID;
    private static EditText password;
    private static EditText confrim;
    private static Button btn;
    private  static String pass;
    private  static String conf;
    private  static String id;
    DatabaseReference myref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__page);
        ID = findViewById(R.id.ID);
        password = findViewById(R.id.New_Password);
        confrim = findViewById(R.id.Confrim_Password);
        btn = findViewById(R.id.button);
        myref = FirebaseDatabase.getInstance().getReference();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = ID.getText().toString();
                pass = password.getText().toString();
                conf = confrim.getText().toString();

                if(!id.isEmpty()) {
                    if(pass.equals(conf)){

                    myref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            myref.child("Login Info").child(id).child("pass").setValue(pass);
                            myref.child("Login Info").child(id).child("flag").setValue("Notdone");
                            myref.child("Login Info").child(id).child("score").setValue("0");
                            Intent intent = new Intent(Register_Page.this,MainActivity.class);
                            startActivity(intent);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                        Toast.makeText(Register_Page.this,"Password does not match",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Register_Page.this,"Enter valid ID",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
