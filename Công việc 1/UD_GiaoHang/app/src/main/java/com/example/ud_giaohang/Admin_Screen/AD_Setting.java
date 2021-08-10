package com.example.ud_giaohang.Admin_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.ud_giaohang.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class AD_Setting extends AppCompatActivity {
    CardView logout,cvDoiMatKhau;
    BottomNavigationView bottomNavigationView ;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,AD_Home.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setting);
        setControl();
        setEvent();
        bottomNavigationView.setSelectedItemId(R.id.mn_Setting);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.mn_home:
                        startActivity(new Intent(getApplicationContext(), AD_Home.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_profile:
                        startActivity(new Intent(getApplicationContext(), AD_Profile.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_Search:
                        startActivity(new Intent(getApplicationContext(), AD_Search.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_Setting:
                        startActivity(new Intent(getApplicationContext(), AD_Setting.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
    private void setEvent() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        cvDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AD_DoiMatKhau.class);
                startActivity(intent);
            }
        });

    }
    private void setControl() {
        bottomNavigationView = findViewById((R.id.bottom_nav));
        logout = findViewById(R.id.logout);
        cvDoiMatKhau = findViewById(R.id.CVDMK);
    }}