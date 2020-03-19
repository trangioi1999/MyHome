package com.example.myhome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

public class SignupActivity extends AppCompatActivity {
    EditText edt_username,edt_password,edt_passwordagain,
            edt_fullname,edt_phone;
    Button btn_signup,btn_signin;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        edt_passwordagain = findViewById(R.id.edt_passwordagain);
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_phone = findViewById(R.id.edt_phone);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signin = findViewById(R.id.btn_signin);
        mAuth = FirebaseAuth.getInstance();

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //

                String email = edt_username.getText().toString();
                String password = edt_password.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Please enter  email...", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignupActivity.this, "Please enter  password...", Toast.LENGTH_SHORT).show();
                } else {


                    //đăng ký
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        //chuyển màn hình đăng nhập
                                        Intent loginIntent = new Intent(SignupActivity.this,LoginActivity.class);
                                        startActivity(loginIntent);
                                        finish();
                                        Toast.makeText(SignupActivity.this, "Account Created Susscessfully...", Toast.LENGTH_SHORT).show();


                                    } else {
                                        String message = task.getException().toString();
                                        Toast.makeText(SignupActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }
            }


        });

    }




}
