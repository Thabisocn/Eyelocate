package com.example.user.eyelocate;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.eyelocate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileActivity extends AppCompatActivity {




    private ProgressBar progressBar2 ;

    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;
    private Button signOut, save;
    FirebaseUser currentUser ;


    EditText uname, emaill,phone,Field,Location;


    private FirebaseAuth mAuth;


    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        save = (Button) findViewById(R.id.save);

        uname = (EditText) findViewById(R.id.name);
        emaill = (EditText) findViewById(R.id.emaill);
        phone = (EditText) findViewById(R.id.phone);
        Field = (EditText) findViewById(R.id.field);
        Location = (EditText) findViewById(R.id.location);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBarrr);







//get firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();







        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();





        UpDateMainActivity();










    }









    // when user picked an image ..


    public void UpDateMainActivity(){




        TextView navUsername = findViewById(R.id.nav_username);
        TextView navUserMail = findViewById(R.id.nav_user_mail);
        EditText uname = findViewById(R.id.name);
        EditText emaill = findViewById(R.id.emaill);
        ImageView navUserPhot = findViewById(R.id.nav_user_photo);

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());
        uname.setText(currentUser.getDisplayName());
        emaill.setText(currentUser.getEmail());

        // now we will use Glide to load user image
        // first we need to import the library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);

    }

}
