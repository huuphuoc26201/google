package com.example.google;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Main2 extends AppCompatActivity {
    private Button btnql;
    private Button btnnext;
    private TextView rd_code;
    private EditText SDT,name;
    private DatabaseReference mDatabase;
    final int SEND_SMS_PERMISSION_REQUEST_CODE=1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        rd_code=(TextView) findViewById(R.id.rd_code);
        SDT=(EditText) findViewById(R.id.sdt);
        name=(EditText) findViewById(R.id.name);
        btnql=(Button) findViewById(R.id.d_qlai);
        btnnext=(Button) findViewById(R.id.gui);
        randomString();


        btnql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main2.this, Main1.class);
                startActivity(intent);

            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                if (checkPermission(Manifest.permission.SEND_SMS)) {
                }else {
                    ActivityCompat.requestPermissions(Main2.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);}
                if (!validatename() |!validateSDT()){
                }else{
                onSend();
                Person();}


            }

            private boolean validatename() {

                String val = name.getText().toString();
                if (val.isEmpty()) {
                    name.setError("name không được để trống");
                    return false;
                }
                else {
                    name.setError(null);
                    return true;
                }
            }

            private boolean validateSDT() {
                String val = SDT.getText().toString();
                String sdt = "[0-9]{10}";
                if (val.isEmpty()) {
                    SDT.setError("SĐT không được để trống");
                    return false;
                } else if (!val.matches(sdt)) {
                    SDT.setError("Số điện thoại phải có 10 chữ số");
                    return false;
                } else {
                    SDT.setError(null);
                    return true;
                }
            }

            private void Person() {
                String Name=name.getText().toString();
                String code=rd_code.getText().toString();
                String sdt=SDT.getText().toString();
                qli Qli=new qli(Name,sdt,code);
                mDatabase.setValue(Qli).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Main2.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            private void onSend() {
                String phoneNumber=SDT.getText().toString();
                String code=rd_code.getText().toString();
                if(phoneNumber==null||phoneNumber.length()==0){
                    return;
                }
                if(checkPermission(Manifest.permission.SEND_SMS)){
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber,null,"Mã code: "+ code,null,null);
                    Toast.makeText(getApplicationContext(),"Mã code đã được gửi vào số điện thoại con bạn",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Main2.this, Main3.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Failed message sent",Toast.LENGTH_SHORT).show();
                }
            }

            public boolean checkPermission(String permission){
                int check= ContextCompat.checkSelfPermission(Main2.this,permission);
                return (check == PackageManager.PERMISSION_GRANTED);
            }
        });

    }

   

    private void randomString(){
        rd_code.setText(gettandomString(6));
    }
    public static String gettandomString(int a){
        final String character="ABCDEFGHIJKLMNOPQRSTXYZW0123456789";
        StringBuilder result = new StringBuilder();
        while (a > 0){
            Random r=new Random();
            result.append((character.charAt(r.nextInt(character.length()))));
            a--;
        }
        return result.toString();
    }
}

