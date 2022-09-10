package com.example.user.eyelocate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.eyelocate.Adapters.AdapterPosts;
import com.example.user.eyelocate.Models.ModelPost;
import com.eyelocate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    RecyclerView postsRecyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    String uid;

    ImageView avatarTv, coverIv;
    TextView nameTv, emailTv, phoneTv;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        coverIv = findViewById(R.id.coverIv);
        avatarTv =  findViewById(R.id.avatarIv);
        nameTv =  findViewById(R.id.nameTv);
        emailTv =  findViewById(R.id.emailTv);
        phoneTv =  findViewById(R.id.phoneTv);

        postsRecyclerView = findViewById(R.id.recyclerview_posts);

        firebaseAuth = FirebaseAuth.getInstance();

        //get uid for clicked user

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");





        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String name = ""+ ds.child("name").getValue();
                    String email = ""+ ds.child("email").getValue();
                    String phone = ""+ ds.child("Phone").getValue();
                    String image = ""+ ds.child("image").getValue();
                    String cover = ""+ ds.child("cover").getValue();


                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);

                    try{

                        Picasso.get().load(image).into(avatarTv);
                    }catch (Exception e){

                        Picasso.get().load(R.drawable.ic_camera_default_24dp).into(avatarTv);
                    }

                    try{

                        Picasso.get().load(cover).into(coverIv);
                    }catch (Exception e){


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        postList = new ArrayList<>();
        checkUserStatus();
        loadHistPosts();
    }

    private void loadHistPosts() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this);

        //show newestpost first

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        postsRecyclerView.setLayoutManager(layoutManager);

        //init postlist

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        //query to load posts

        Query query = ref.orderByChild("uid").equalTo(uid);

        //get all data from this ref

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost myPosts = ds.getValue(ModelPost.class);

                    postList.add(myPosts);


                    adapterPosts = new AdapterPosts(ProfileActivity.this, postList);


                    postsRecyclerView.setAdapter(adapterPosts);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ProfileActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void searchHistPosts(final String searchQuery) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this);

        //show newestpost first

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        postsRecyclerView.setLayoutManager(layoutManager);

        //init postlist

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        //query to load posts

        Query query = ref.orderByChild("uid").equalTo(uid);

        //get all data from this ref

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost myPosts = ds.getValue(ModelPost.class);



                    if (myPosts.getpTitle().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            myPosts.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())){

                        postList.add(myPosts);
                    }


                    adapterPosts = new AdapterPosts(ProfileActivity.this, postList);


                    postsRecyclerView.setAdapter(adapterPosts);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ProfileActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserStatus() {

        FirebaseUser user =  firebaseAuth.getCurrentUser();
        if (user != null) {



        } else {

            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);


        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s)){

                    //called when user presses the search button

                    searchHistPosts(s);

                } else {
                    //called whenever user types any letter

                    loadHistPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s)) {

                    searchHistPosts(s);

                } else {

                    loadHistPosts();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Profile) {
            Toast.makeText(ProfileActivity.this, "Profile is Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ProfileActivity.class));

        } else if (id == R.id.Settings) {
            Toast.makeText(ProfileActivity.this, "settings menu is Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SettingsActivity.class));

        } else if (id == R.id.Help) {
            Toast.makeText(ProfileActivity.this, "Help menu is Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HelpActivity.class));


        } else if (id == R.id.Logout) {
            Toast.makeText(ProfileActivity.this, "LOGOUT menu is Clicked", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            checkUserStatus();


        }

        return super.onOptionsItemSelected(item);
    }
}

// and yesss we have made it. WE HAVE MADE AN APP WITH LOGIN AND REGISTRATION
// PLEASE DO LIKE AND SUBSCRIBE FOR MORE.
//THANK YOU







