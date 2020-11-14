package com.example.yourhistory;


import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourhistory.Fragment.FragmentPersonalInformation;
import com.example.yourhistory.Fragment.FragmentPhotos;
import com.example.yourhistory.Fragment.FragmentPeoples;
import com.example.yourhistory.Fragment.MainFragment;
import com.example.yourhistory.model.UserEntered;
import com.google.android.material.navigation.NavigationView;
//implements in method of class .onNavigationItemSelectedListener
public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainFragment.onFragmentBtnSelected {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBar;
    Toolbar toolbar;
    NavigationView navigationView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    TextView lblHeaderNameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //references layout_toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.layout_drawer);
        navigationView = findViewById(R.id.NavigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBar = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBar);
        actionBar.setDrawerIndicatorEnabled(true);
        actionBar.syncState();

        //load default fragment

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new MainFragment());
        fragmentTransaction.commit();

        //lblHeaderNameUser = findViewById(R.id.headerNameUser);
       // Toast.makeText(this, "user: "+UserEntered.getUserEntered().getNameUser(), Toast.LENGTH_SHORT).show();
        //lblHeaderNameUser.setText(UserEntered.getUserEntered().getNameUser()+"");
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*make the item where is selected, so where he is equals that id referenced
        implements in action
        -change fragment on click*/
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.menu_my_History) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
            fragmentTransaction.commit();
        }

        if(item.getItemId() == R.id.menu_my_photos) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new FragmentPhotos());
            fragmentTransaction.commit();
        }

        if(item.getItemId() == R.id.menu_people) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new FragmentPeoples());
            fragmentTransaction.commit();
        }

        if(item.getItemId() == R.id.menu_my_info) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new FragmentPersonalInformation());
            fragmentTransaction.commit();
        }
        return true;
    }

    @Override
    public void onButtonSelected() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new FragmentPhotos());
        fragmentTransaction.commit();
    }
}