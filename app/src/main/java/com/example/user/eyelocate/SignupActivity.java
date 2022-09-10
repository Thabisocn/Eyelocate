package com.example.user.eyelocate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.eyelocate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {


    private EditText mEmailET,mPasswordET;     //hit option + enter if you on mac , for windows hit ctrl + enter
    private Button btnSignIn, mRegisterBtn ;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Get Firebase auth instance


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        mRegisterBtn= (Button) findViewById(R.id.registerBtn);
        mEmailET = (EditText) findViewById(R.id.emailEt);
        mPasswordET = (EditText) findViewById(R.id.passwordEt);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Account...");




        mAuth = FirebaseAuth.getInstance();



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = mEmailET.getText().toString().trim();
                String password = mPasswordET.getText().toString().trim();


                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    mEmailET.setError("Invalid Email");
                    mEmailET.setFocusable(true);
                }else if (password.length()<6){

                    mPasswordET.setError("Password length should at least be 6+ characters");
                    mPasswordET.setFocusable(true);
                }
                else {
                    CreateUserAccount(email, password);
                }

            }
        });






}


    private void CreateUserAccount(String email, String password) {


        progressDialog.show();
        // this method create user account with specific email and password

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // user account created successfully
                           progressDialog.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();


                            String email = user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object, String> hashMap = new HashMap<>();
                            //put info in hash map

                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", "" );
                            hashMap.put("onlineStatus", "online" );
                            hashMap.put("typingTo", "noOne" );
                            hashMap.put("Phone", "" );
                            hashMap.put("image", "" );
                            hashMap.put("cover", "" );


                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference reference = database.getReference("Users");

                            reference.child(uid).setValue(hashMap);



                            Toast.makeText(SignupActivity.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();

                        }
                        else
                        {

                            // account creation failed
                            progressDialog.dismiss();
                            showMessage("account creation failed" + task.getException().getMessage());


                        }
                    }
                });








    }




    // simple method to show toast message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




    }

}
