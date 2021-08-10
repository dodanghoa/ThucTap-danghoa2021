package com.example.ud_giaohang.KhachHang_Screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ud_giaohang.Admin_Screen.AD_Home;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
import com.example.ud_giaohang.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KH_Home extends AppCompatActivity {

    CardView cvTaoDonHang, cvXemDonHang, cvThongKe;
    boolean doubleBackToExitPressedOnce = false;
    BottomNavigationView bottomNavigationView ;
    private FirebaseAuth auth;
    User nguoiDungHienTai = new User();
    DatabaseReference mDatabase;

    TextView tvNguoidung, tvSo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        mDatabase  = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setContentView(R.layout.activity_kh_home);
        setControl();
        GetData();
        setEvent();
        bottomNavigationView.setSelectedItemId(R.id.mn_home);
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
                        startActivity(new Intent(getApplicationContext(), KH_About.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_Setting:
                        startActivity(new Intent(getApplicationContext(), KH_Setting.class));
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
        cvTaoDonHang = findViewById(R.id.cvTaoDonHang);
        cvXemDonHang = findViewById(R.id.cvXemDonHang);
        cvThongKe = findViewById(R.id.cvThongKe);
        tvNguoidung = findViewById(R.id.tv_tennguoidung);
        tvSo = findViewById(R.id.tv_sodienthoai);
    }
    private void GetData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                nguoiDungHienTai = dataSnapshot.getValue(User.class);
                tvNguoidung.setText(nguoiDungHienTai.getHoTen().toString());
                tvSo.setText(nguoiDungHienTai.getSoDT().toString());
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        mDatabase.child("user").child(auth.getUid()).addValueEventListener(postListener);
    }
    private void setEvent() {
        cvTaoDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), KH_TaoDonHang.class);
                startActivity(intent);
            }
        });

        cvXemDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), KH_XemDonHang.class);
                startActivity(intent);
            }
        });

        cvThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KH_Home.this, KH_ThongKe.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            FirebaseAuth.getInstance().signOut();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn lần nữa để đăng xuất!", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}