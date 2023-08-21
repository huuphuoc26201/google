package com.example.google;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private CheckBox cb1;
    private CheckBox cb2;
    private Button btnnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnnext = (Button) findViewById(R.id.btnnext);
        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb1.isChecked()&&cb2.isChecked()){
                    Toast.makeText(MainActivity.this,"Vui lòng chọn lại",Toast.LENGTH_SHORT).show();
                    return;
                }
                String chon = "Truy cập vào" + " ";
                if (cb1.isChecked()) {
                    chon += cb1.getText() + " ";
                    Intent intent = new Intent(MainActivity.this, Main_te1.class);
                    startActivity(intent);

                }
                if (cb2.isChecked()){
                    chon += cb2.getText() + " ";
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                Toast.makeText(MainActivity.this, chon, Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ok = new AlertDialog.Builder(MainActivity.this);
        ok.setTitle("Thoát ứng dụng");
        ok.setMessage("Thoát ứng dụng ngay bây giờ ?");
        ok.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        ok.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        ok.create().show();
    }
}
