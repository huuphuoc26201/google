package com.example.google;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class quen_mk extends AppCompatActivity {
    private Button btnqlai,reset;
    private TextView dangki;
    private EditText Email;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mk);
        mAuth= FirebaseAuth.getInstance();
        btnqlai = (Button) findViewById(R.id.d_qlai);
        dangki = (TextView) findViewById(R.id.nhan);
        Email = (EditText) findViewById(R.id.email);
        reset = (Button) findViewById(R.id.reset_mk);

        btnqlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(quen_mk.this, Login.class);
                startActivity(intent);
            }
        });

        dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(quen_mk.this, sign_up.class);
                startActivity(intent);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e_mail=Email.getText().toString().trim();
                if(TextUtils.isEmpty(e_mail)){
                    Email.setError("Email không được để trống");
                    Email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(e_mail).matches()){
                    Email.setError("Yêu cầu nhập email phù hợp");
                    Email.requestFocus();
                }else {
                    mAuth.sendPasswordResetEmail(e_mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                AlertDialog.Builder ok = new AlertDialog.Builder(quen_mk.this);
                                ok.setTitle("Thông báo");
                                ok.setMessage("Chúng tôi đã hỗ trợ bạn reset mật khẩu. Vui lòng kiểm tra lại email của bạn.");
                                ok.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(quen_mk.this, Login.class);
                                        startActivity(intent);
                                    }
                                });
                                ok.create().show();
                            }else{
                                Toast.makeText(quen_mk.this,"Email chưa được đăng ký tài khoản",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });



    }
}