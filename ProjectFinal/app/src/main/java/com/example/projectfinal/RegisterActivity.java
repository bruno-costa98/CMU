package com.example.projectfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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


public class RegisterActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegister;
    private FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    mUsername = findViewById(R.id.username);
    mName = findViewById(R.id.name);
    mEmail = findViewById(R.id.email_);
    mPassword = findViewById(R.id.password);
    mRegister = findViewById(R.id.register);

    mAuth = FirebaseAuth.getInstance();

    mRegister.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
               mEmail.setError("Email required");
               return;
            }
            if(TextUtils.isEmpty(password)){
                mPassword.setError("Password required");
                return;
            }

          mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){
                      Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                  } else {
                      Toast.makeText(RegisterActivity.this, "Error" +  task.getException(), Toast.LENGTH_SHORT).show();
                  }
              }
          });

        }
    });
}
}