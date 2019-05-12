package com.bkteam.ohmychat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {
    EditText email, oldPass, newPass;
    Button update;
    FirebaseUser user;
    AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        InitializeField();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword();
            }
        });


    }

    private void ChangePassword() {
        String userEmail = email.getText().toString();
        String userOldPass = oldPass.getText().toString();
        final String userNewPass = newPass.getText().toString();

        credential = EmailAuthProvider.getCredential(userEmail, userOldPass);

        user = FirebaseAuth.getInstance().getCurrentUser();
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(userNewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePassActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePassActivity.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ChangePassActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InitializeField() {
        email = (EditText) findViewById(R.id.change_pass_email);
        oldPass = (EditText) findViewById(R.id.change_pass_password);
        newPass = (EditText) findViewById(R.id.change_pass_new_password);
        update = (Button) findViewById(R.id.change_pass_button);
    }
}
