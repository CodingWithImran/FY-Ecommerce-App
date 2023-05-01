package com.codingwithimran.fycommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.codingwithimran.fycommerce.Fragments.HomeFragment;
import com.codingwithimran.fycommerce.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    Toolbar toolbar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = findViewById(R.id.home_toolbar);
         setSupportActionBar(toolbar);
//         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//         getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        getSupportActionBar().setTitle("Alleezy Collections");


        auth = FirebaseAuth.getInstance();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment myFragment = new HomeFragment();
        fragmentTransaction.add(R.id.fragment_container, myFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_id) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            finish();
        } else if (id == R.id.mycart_id) {
            startActivity(new Intent(MainActivity.this, ViewCartActivity.class));
        }
        else if (id == R.id.luckydraw_id) {
            startActivity(new Intent(MainActivity.this, LuckydrawActivity.class));
        }else if (id == R.id.tracking_id) {
            startActivity(new Intent(MainActivity.this, TrackingOrderActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


}