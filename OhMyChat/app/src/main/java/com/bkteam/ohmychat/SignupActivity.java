package com.bkteam.ohmychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignupActivity extends AppCompatActivity {
    EditText username, email, pass, re_pass;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.edt_username);
        email = findViewById(R.id.edt_email);
        pass = findViewById(R.id.edt_pass);
        re_pass = findViewById(R.id.edt_repass);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_pass = pass.getText().toString();
                String txt_repass = re_pass.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass) || TextUtils.isEmpty(txt_repass)){
                    Toast.makeText(SignupActivity.this, "All filled are required", Toast.LENGTH_SHORT).show();
                }else if (txt_pass.length() < 6){
                    Toast.makeText(SignupActivity.this, "Password must be greater than 6 character", Toast.LENGTH_SHORT).show();
                }else if (txt_pass != txt_repass){
                    Toast.makeText(SignupActivity.this, "Password don't match", Toast.LENGTH_SHORT).show();
                }
                else{
                    register(txt_username, txt_email, txt_pass);
                }
            }
        });
    }

    private void register(String username, String email, String pass){

    }
}

