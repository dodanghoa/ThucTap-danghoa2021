package com.example.ud_giaohang.Admin_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.adapter.AD_RecycleVeiwDonHang;
import com.example.ud_giaohang.adapter.AD_RecycleViewNhanVien;
import com.example.ud_giaohang.model.DonHang;
import com.example.ud_giaohang.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AD_Search extends AppCompatActivity {
    TextView gotoDonHang,gotoNhanVien;
    ArrayList<User> arrayLists;
    ArrayList<User> display;
    SearchView searchView;
    LinearLayout lnsearchDonHang,lnsearchNhanVien;
    Button btnSearch;
    BottomNavigationView bottomNavigationView;
    AD_RecycleViewNhanVien ad_recycleViewNhanVien;
    AD_RecycleVeiwDonHang ad_recycleVeiwDonHang;
    ArrayList<DonHang> donHangs = new ArrayList<>();
    RecyclerView rcvTaiKhoan, rcvDonHang;
    private DatabaseReference mDatabase;
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
        setContentView(R.layout.activity_admin_search);
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        ad_recycleViewNhanVien = new AD_RecycleViewNhanVien(this);
        ad_recycleVeiwDonHang = new AD_RecycleVeiwDonHang(AD_Search.this);
        setControl();
        Search("");
        setEvent();
        bottomNavigationView.setSelectedItemId(R.id.mn_Search);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_home:
                        startActivity(new Intent(getApplicationContext(), AD_Home.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.mn_profile:
                        startActivity(new Intent(getApplicationContext(), AD_Profile.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.mn_Search:
                        startActivity(new Intent(getApplicationContext(), AD_Search.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.mn_Setting:
                        startActivity(new Intent(getApplicationContext(), AD_Setting.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    private void setEvent() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Search(query.toLowerCase());
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Search("");
                return false;
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search(searchView.getQuery().toString().toLowerCase());
            }
        });
        gotoNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AD_Search.this,AD_DanhSachTaiKhoan.class);
                startActivity(intent);
            }
        });
        gotoDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AD_Search.this,AD_DanhSachDonHang.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        bottomNavigationView = findViewById((R.id.bottom_nav));
        rcvTaiKhoan = findViewById((R.id.rcDanhSachTaiKhoan));
        rcvDonHang = findViewById((R.id.rcDonHang));
        searchView = findViewById(R.id.searchVeiw);
        btnSearch = findViewById(R.id.btnSearch);
        lnsearchDonHang = findViewById(R.id.lnSearchDonHang);
        lnsearchNhanVien = findViewById(R.id.lnSearchNhanVien);
        gotoDonHang = findViewById(R.id.gotoDonHang);
        gotoNhanVien = findViewById(R.id.gotoNhanVien);
    }

    public void Search(String key) {

        DatabaseReference userChilds = mDatabase.child("user");
        userChilds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> temp = new ArrayList<>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (temp.size() < 8) {
                        User user = postSnapshot.getValue(User.class);
                        if (key.isEmpty()) {
                            temp.add(user);
                        } else {
                            if (user.getHoTen().toLowerCase().contains(key) || user.getMa().toLowerCase().contains(key)
                                    || user.getDiaChi().toLowerCase().contains(key) || user.getViTriCV().toLowerCase().contains(key)
                            ) {
                                temp.add(user);
                            }
                        }
                    }
                }
                arrayLists = temp;
                display = temp;
                if (arrayLists.size()!=0)
                {
                    lnsearchNhanVien.setVisibility(View.VISIBLE);
                }
                else {
                    lnsearchNhanVien.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ad_recycleViewNhanVien.setData(display);
                        LinearLayoutManager linearLayoutManager = new GridLayoutManager(AD_Search.this,  2, GridLayoutManager.HORIZONTAL, false);
                        rcvTaiKhoan.setLayoutManager(linearLayoutManager);
                        rcvTaiKhoan.setAdapter(ad_recycleViewNhanVien);
                    }
                },
                100);
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangs.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (donHangs.size() < 6) {
                        DonHang donHang = postSnapshot.getValue(DonHang.class);
                        if (key.isEmpty()) {
                            donHangs.add(donHang);
                        } else {
                            if (donHang.getHoTen().toLowerCase().contains(key) || donHang.getMaDonHang().toLowerCase().contains(key)
                                    || donHang.getDiaChi().toLowerCase().contains(key) || donHang.getNguoiGui().getHoTen().toLowerCase().contains(key)
                                    || donHang.getNguoiGui().getSoDT().contains(key) || donHang.getSoDT().contains(key)
                            ) {
                                donHangs.add(donHang);
                            }
                        }
                    }
                }
                Collections.reverse(donHangs);
                if (donHangs.size()!=0)
                {
                    lnsearchDonHang.setVisibility(View.VISIBLE);
                }
                else {
                    lnsearchDonHang.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ad_recycleVeiwDonHang.setData(donHangs);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AD_Search.this, LinearLayoutManager.HORIZONTAL, false);
                        rcvDonHang.setLayoutManager(linearLayoutManager);
                        rcvDonHang.setAdapter(ad_recycleVeiwDonHang);
                    }
                },
                100);
    }
}