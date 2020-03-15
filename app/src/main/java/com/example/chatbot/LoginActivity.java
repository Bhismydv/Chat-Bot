package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail,edtPassword;
    Button login;
    TextView signup;

    String email;
    String password;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail=findViewById(R.id.emailText);
        edtPassword=findViewById(R.id.passwordText2);
        signup=findViewById(R.id.signup_text);
        login=findViewById(R.id.logintn);

        auth=FirebaseAuth.getInstance();

login.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
switch (v.getId()){
    case  R.id.logintn:
        login();
        break;

}

signup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(getApplicationContext(),SignUpActivity.class));

    }
});
    }

    private void login() {
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        /*
        Check for empty fields to avoid exception
         */
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter password", Toast.LENGTH_LONG).show();
            return;
        }
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user = auth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Login unsuccessful. Check details", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
