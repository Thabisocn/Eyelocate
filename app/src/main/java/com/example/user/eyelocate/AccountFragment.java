package com.example.user.eyelocate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.eyelocate.Adapters.AdapterUsers;
import com.example.user.eyelocate.Models.ModelUsers;
import com.eyelocate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUsers> userList;
    FirebaseAuth mAuth;


    public AccountFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        recyclerView = view.findViewById(R.id.users_recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //


        mAuth = FirebaseAuth.getInstance();

        userList = new ArrayList<>();


        getAllUsers();

        return view;

    }

    private void getAllUsers() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelUsers modelUser = ds.getValue(ModelUsers.class);


                    if (!fUser.getUid().equals(modelUser.getUid())) {
                        userList.add(modelUser);
                    }

                    adapterUsers = new AdapterUsers(getActivity(), userList);

                    recyclerView.setAdapter(adapterUsers);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUsers(final String query) {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelUsers modelUser = ds.getValue(ModelUsers.class);

                    if (!fuser.getUid().equals(modelUser.getUid())) {

                        if (modelUser.getName().toLowerCase().contains(query.toLowerCase()) ||
                                modelUser.getEmail().toLowerCase().contains(query.toLowerCase())) {
                            userList.add(modelUser);

                        }


                    }
                    adapterUsers = new AdapterUsers(getActivity(), userList);
                    adapterUsers.notifyDataSetChanged();

                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void checkUserStatus() {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {


        } else {

            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);


        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s.trim())) {

                    searchUsers(s);
                } else {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s.trim())) {

                    searchUsers(s);
                } else {
                    getAllUsers();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Profile) {
            Toast.makeText(getActivity(), "Profile is Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), ProfileActivity.class));

        } else if (id == R.id.Settings) {
            Toast.makeText(getActivity(), "settings menu is Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), SettingsActivity.class));

        } else if (id == R.id.Help) {
            Toast.makeText(getActivity(), "Help menu is Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), HelpActivity.class));


        } else if (id == R.id.Logout) {
            Toast.makeText(getActivity(), "LOGOUT menu is Clicked", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            checkUserStatus();



        }

        return super.onOptionsItemSelected(item);
    }
}