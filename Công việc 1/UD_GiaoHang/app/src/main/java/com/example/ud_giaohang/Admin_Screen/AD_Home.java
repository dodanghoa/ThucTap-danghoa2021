package com.example.ud_giaohang.Admin_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.adapter.AD_RecycleVeiwDonHang;
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

import java.util.ArrayList;
import java.util.Collections;

public class AD_Home extends AppCompatActivity {
    CardView cvTaoDonHang,cvDanhSachDonHang,cvTaiKhoan,cvThongKe;
    TextView txtTenNguoiDung,txtChucVu,txtEmail,gotoDonHang;
    AD_RecycleVeiwDonHang ad_recycleVeiwDonHang;
    LinearLayout lnDonHangGanDay;
    RecyclerView recyclerDonHangGanDay;
    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    boolean doubleBackToExitPressedOnce = false;

    BottomNavigationView bottomNavigationView ;
    ArrayList<DonHang>donHangs= new ArrayList<>();
    User nguoiDungHienTai = new User();
    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()==null)
        {
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        auth = FirebaseAuth.getInstance();

        mDatabase  = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setControl();
        setEvent();
        GetData();
        bottomNavigationView.setSelectedItemId(R.id.mn_home);
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

    private void GetData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                nguoiDungHienTai = dataSnapshot.getValue(User.class);
                txtTenNguoiDung.setText( nguoiDungHienTai.getHoTen().toString());
                txtEmail.setText( nguoiDungHienTai.getEmail().toString());
                // ..
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
        mDatabase.child("user").child(auth.getUid()).addValueEventListener(postListener);

        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangs.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (donHangs.size() < 6) {
                        DonHang donHang = postSnapshot.getValue(DonHang.class);

                            if (donHang.getTrangThaiDonHang().equals("Đã tạo đơn hàng") )
                            {
                                donHangs.add(donHang);
                            }
                    }

                }
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                ad_recycleVeiwDonHang = new AD_RecycleVeiwDonHang(AD_Home.this);
                                ad_recycleVeiwDonHang.setData(donHangs);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AD_Home.this, LinearLayoutManager.HORIZONTAL, false);
                                recyclerDonHangGanDay.setLayoutManager(linearLayoutManager);
                                recyclerDonHangGanDay.setAdapter(ad_recycleVeiwDonHang);
                                if(donHangs.size()==0)
                                {
                                    lnDonHangGanDay.setVisibility(View.GONE);
                                }
                            }
                        },
                        100);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setControl() {
        bottomNavigationView = findViewById((R.id.bottom_nav));
        txtTenNguoiDung = findViewById((R.id.txtTenNguoiDung));
        txtChucVu = findViewById((R.id.txtChucVu));
        txtEmail = findViewById((R.id.txtEmail));
        cvTaoDonHang = findViewById((R.id.cvTaoDonHang));
        cvTaiKhoan = findViewById((R.id.cvTaiKhoan));
        cvDanhSachDonHang = findViewById((R.id.cvDanhSachDonHang));
        cvThongKe = findViewById((R.id.cvThongKe));
        recyclerDonHangGanDay = findViewById(R.id.rcDonHangGanDay);
        lnDonHangGanDay = findViewById(R.id.lnDonHangGanDay);
        gotoDonHang = findViewById(R.id.gotoDonHang);
    }
    private void setEvent() {
        cvTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AD_DanhSachTaiKhoan.class);
                startActivity(intent);
            }
        });
        cvDanhSachDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AD_DanhSachDonHang.class);
                startActivity(intent);
            }
        });
        cvThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AD_ThongKe.class);
                startActivity(intent);
            }
        });
        gotoDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AD_DanhSachDonHang.class);
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