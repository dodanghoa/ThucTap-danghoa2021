package com.example.ud_giaohang.KhachHang_Screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ud_giaohang.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class KH_About extends AppCompatActivity {

    BottomNavigationView bottomNavigationView ;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, KH_Home.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kh_about);
        setControl();
        bottomNavigationView.setSelectedItemId(R.id.mn_About);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.mn_home:
                        startActivity(new Intent(getApplicationContext(), KH_Home.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_profile:
                        startActivity(new Intent(getApplicationContext(), KH_Profile.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_About:
                        startActivity(new Intent(getApplicationContext(),KH_About.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_Setting:
                        startActivity(new Intent(getApplicationContext(),KH_Setting.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
    private void setControl() {
        bottomNavigationView = findViewById((R.id.bottom_nav));
    }}