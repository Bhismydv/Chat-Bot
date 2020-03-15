package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText fName,lName,userName,emailEdit,passwordEdit;
    Button submit;
    RadioGroup gendermf;

    FirebaseAuth firebaseAuth;


    FirebaseUser users;
    String email;
    String password;
     String username;

    String firstName;
    String lastName;

    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fName=findViewById(R.id.fname_editText);
        lName=findViewById(R.id.lname_editText2);
        userName=findViewById(R.id.username_editText4);
        passwordEdit=findViewById(R.id.password_editText5);
        emailEdit=findViewById(R.id.email_editText3);
        submit=findViewById(R.id.submit_button);
        gendermf=findViewById(R.id.radioGroup);

firebaseAuth=FirebaseAuth.getInstance();


submit.setOnClickListener(this);
signupUser();
        //Create user with email and password


    }
    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.submit_button:
        signupUser();
        break;
}
    }

    private void signupUser() {
        email=emailEdit.getText().toString();
        password=passwordEdit.getText().toString();
        username=userName.getText().toString();
        firstName=fName.getText().toString();
        lastName=lName.getText().toString();

        switch (gendermf.getCheckedRadioButtonId()) {
            case R.id.male:
                gender = "male";
                break;
            case R.id.female:
                gender = "female";
                break;
        }
        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this,"Enter first name",Toast.LENGTH_LONG).show();
            return;}

        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this,"Enter last name",Toast.LENGTH_LONG).show();
            return;}

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this,"Enter username",Toast.LENGTH_LONG).show();
            return;}

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Enter email",Toast.LENGTH_LONG).show();
            return;}

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Enter password",Toast.LENGTH_LONG).show();
            return;}

        if (TextUtils.isEmpty(gender)) {
            Toast.makeText(this,"Select gender",Toast.LENGTH_LONG).show();
            return;}

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                        /*
                        set username as user's display name
                         */
                            users = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            users.updateProfile(profileUpdate);

                            addUserToDatabase(); //method to store user's data in cloud firestore
                            Toast.makeText(getApplicationContext(), "Registration successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity

                                    .this, MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Registration error. Check your details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addUserToDatabase(){

        FirebaseFirestore database = FirebaseFirestore.getInstance(); //Initialize cloud firestore
        String key = users.getUid(); //retrieve the user id so we can later use as key in the database

        HashMap<String, String> userData = new HashMap<>();
        userData.put("a1_firstName", firstName);
        userData.put("a2_lastname", lastName);
        userData.put("a3_username", username);
        userData.put("a4_email", email);
        userData.put("a5_gender", gender);
        userData.put("a6_imageUrl", "none");

        Map<String, Object> update = new HashMap<>();
        update.put(key, userData);

        database.collection("users").document(key).set(update); //update user's details to Firestore
    }


}
