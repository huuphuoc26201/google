package com.example.google;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class Main_te1 extends AppCompatActivity {

    private Button btnql;
    private EditText rcode;
    private Button btnnext;
    private TextView tv;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_te1);

        btnql=(Button) findViewById(R.id.d_qlai);
        btnnext=(Button) findViewById(R.id.btnnext);
        rcode=(EditText) findViewById(R.id.code);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        btnql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main_te1.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              mDatabase.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      qli Qli=snapshot.getValue(qli.class);
                      String re_code= Qli.getCode();
                      String r_code = rcode.getText().toString().trim();
                      if (!validatecode()){
                      }
                      else if (r_code.equals(re_code)) {
                          Toast.makeText(Main_te1.this, "Nhập mã code thành công", Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(Main_te1.this, Main_te2.class);
                          startActivity(intent);
                      } else {
                          Toast.makeText(Main_te1.this, "Nhập mã code không thành công! Thử lại!", Toast.LENGTH_SHORT).show();
                      }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });
          }
        });
    }

    private boolean validatecode() {
        String val = rcode.getText().toString();
        if (val.isEmpty()) {
            rcode.setError("Mã lời mời không được để trống");
            return false;
        }
        else {
            rcode.setError(null);
            return true;
        }
    }
}