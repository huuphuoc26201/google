package com.example.google;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class sign_up extends AppCompatActivity {
    private Button signupbtn;
    private EditText email1,password1,sdt1,repassword1;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth= FirebaseAuth.getInstance();
        email1=(EditText) findViewById(R.id.email);
        password1=(EditText) findViewById(R.id.password);
        sdt1=(EditText) findViewById(R.id.sdt);
        repassword1=(EditText) findViewById(R.id.repassword);
        signupbtn=(Button) findViewById(R.id.signupbtn);
        LinearLayout btndn= findViewById(R.id.dangnhap);
        progressDialog=new ProgressDialog(this);
        firebaseFirestore = FirebaseDatabase.getInstance().getReference().child("Tài Khoản");
        password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        repassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());


        btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_up.this, Login.class);
                startActivity(intent);
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateemail() | !validatePassword()|!validateSDT()|!validaterepassword()){

                }else{
                dangki();}
            }
        });
    }

    private boolean validateemail() {
        String val = email1.getText().toString();
        String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email1.setError("Email không được để trống");
            return false;
        }else if(!val.matches(emailPattern)){
            email1.setError("Địa chỉ email không hợp lệ");
            return false;
        }
        else {
            email1.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String val = password1.getText().toString();
        if (val.isEmpty()){
            password1.setError("Password không được để trống");
            return false;
        }else if(password1.length()<5){
            password1.setError(" Độ dài mật khẩu tối thiểu 5 ký tự ");
            return false;
        }
        else {
            password1.setError(null);
            return true;
        }
    }
    private boolean validaterepassword() {
        String password = password1.getText().toString();
        String val = repassword1.getText().toString();
        if (val.isEmpty()){
            repassword1.setError("Repassword không được để trống");
            return false;
        }else if(!password.equals(val)) {
            repassword1.setError("Nhập mật khẩu không khớp");
            return false;

        }
        else {
            repassword1.setError(null);
            return true;
        }
    }

    private boolean validateSDT() {
        String val = sdt1.getText().toString();
        String sdt = "[0-9]{10}";
        if (val.isEmpty()) {
            sdt1.setError("SĐT không được để trống");
            return false;
        } else if (!val.matches(sdt)) {
            sdt1.setError("Số điện thoại phải có 10 chữ số");
            return false;
        } else {
            sdt1.setError(null);
            return true;
        }
    }

    private void dangki() {
        String email=email1.getText().toString().trim();
        String password = password1.getText().toString().trim();
        String phone=sdt1.getText().toString().trim();
        String repassword = repassword1.getText().toString().trim();
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(sign_up.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(sign_up.this,Login.class));
                        progressDialog.cancel();
                        user User=new user(email,phone,password);
                        firebaseFirestore.setValue(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(sign_up.this, "Tạo tài khoản không thành công! Thử lại!", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });


        }
    }

