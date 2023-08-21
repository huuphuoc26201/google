package com.example.google;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main3 extends AppCompatActivity {

    private Button call, sms, khancap;
    private Button btnql;
    private EditText tinnhan;
    Context context = this;
    private DatabaseReference mDatabase;
    public EditText sdt;
    private static final int REQUEST_CALL = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        btnql = (Button) findViewById(R.id.d_qlai);
        call = (Button) findViewById(R.id.call);
        sms = (Button) findViewById(R.id.sms);
        khancap = (Button) findViewById(R.id.goi);
        tinnhan = (EditText) findViewById(R.id.tinnhan);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        btnql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main3.this, Main2.class);
                startActivity(intent);
            }
        });
        khancap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialog();
            }

            private void EditDialog() {
                AlertDialog.Builder builderD = new AlertDialog.Builder(context);

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.goikhancap, null);
                builderD.setView(view);
                builderD.setTitle("Cuộc gọi khẩn cấp");
                builderD.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builderD.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goikhancap();

                    }
                });//Get text from edit text in dialog into display textview
                sdt = view.findViewById(R.id.sdt);
                builderD.create();
                builderD.show();
            }

            private void goikhancap() {
                String sdt_tre = sdt.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + sdt_tre));
                if (ContextCompat.checkSelfPermission(Main3.this, Manifest.permission.CALL_PHONE) != getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Main3.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    return;
                }
                startActivity(intent);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        qli Qli = snapshot.getValue(qli.class);
                        String sdt_tre = Qli.getSdt();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + sdt_tre));
                        if (ContextCompat.checkSelfPermission(Main3.this, Manifest.permission.CALL_PHONE) != getPackageManager().PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Main3.this,
                                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                            return;
                        }
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        qli Qli = snapshot.getValue(qli.class);
                        String loinhan = tinnhan.getText().toString();
                        String sdt_tre = Qli.getSdt();
                        if (checkPermission(Manifest.permission.SEND_SMS)) {
                            SmsManager smsManager = SmsManager.getDefault();
                            if (!loinhan.isEmpty()){
                                smsManager.sendTextMessage(sdt_tre, null, loinhan, null, null);
                                Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Please enter a message!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            ActivityCompat.requestPermissions(Main3.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


    private boolean checkPermission(String sendSms) {
        int check = ContextCompat.checkSelfPermission(Main3.this, sendSms);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}
