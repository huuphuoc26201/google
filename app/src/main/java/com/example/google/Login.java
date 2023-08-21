package com.example.google;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private Button btndn;
    private LinearLayout googleBtn;
    private EditText email1, password1;
    private TextView quen_mk;
    private LinearLayout dangki;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private static final String TAG="Login";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        btndn = (Button) findViewById(R.id.btndn);
        dangki = (LinearLayout) findViewById(R.id.dangki);
        quen_mk = (TextView) findViewById(R.id.quenmk);
        googleBtn = (LinearLayout) findViewById(R.id.google_img);
        email1 = (EditText) findViewById(R.id.email);
        password1 = (EditText) findViewById(R.id.password);
        progressDialog=new ProgressDialog(this);


        // show passwword
        ImageView showpassword = findViewById(R.id.show_password);
        showpassword.setImageResource(R.drawable.ic_hide_pwd);
        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password1.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showpassword.setImageResource(R.drawable.ic_hide_pwd);
                }else {
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showpassword.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });



        btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateemail() | !validatePassword()){

                } else {
                    checkUser();
                }
            }
        });

        quen_mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, quen_mk.class);
                startActivity(intent);
            }
        });

        dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this, sign_up.class);
                startActivity(intent);
            }
        });

        //Đăng nhập bằng gooogle
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc=GoogleSignIn.getClient(this,gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=gsc.getSignInIntent();
                startActivityForResult(intent,1234);

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1234){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account=task.getResult(ApiException.class);
                AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Đăng nhập bằng tài khoản Google thành công",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getApplicationContext(),Main1.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }catch (ApiException e){
                e.printStackTrace();
            }
        }

    }

    //Đăng ký, đăng nhập tài khoản
    public Boolean validateemail() {
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

    public Boolean validatePassword(){
        String val = password1.getText().toString();
        if (val.isEmpty()){
            password1.setError("Password không được để trống");
            return false;
        } else {
            password1.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String email = email1.getText().toString().trim();
        String password = password1.getText().toString().trim();
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(Login.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Login.this,Main1.class);
                    startActivity(intent);

                }
                else {
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        email1.setError("Người dùng không tồn tại. Vui lòng đăng ký người dùng mới.");
                        email1.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        email1.setError("Thông tin không hợp lệ. vui lòng kiểm tra và nhập lại.");
                        email1.requestFocus();}
                    catch (Exception e) {
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }
}