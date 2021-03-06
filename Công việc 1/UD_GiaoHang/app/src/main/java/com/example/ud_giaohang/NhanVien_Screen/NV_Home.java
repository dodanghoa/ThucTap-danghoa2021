package com.example.ud_giaohang.NhanVien_Screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class NV_Home extends AppCompatActivity {


    boolean doubleBackToExitPressedOnce = false;
    Button btnLogout;
    BottomNavigationView bottomNavigationView;
    ImageView ivNhanDonHang, ivGiaoDonHang, ivDoanhThu;
    TextView tvDonHangChoLay, tvDonHangDaLay, tvDonHangChoGiao, tvDonHangDaGiao,tvDonHangKhongThanhCong, tvHoTen, tvChucVu, tvEmail;
    DatabaseReference mDatabase;
    FirebaseAuth auth;
    ArrayList<DonHang> dataNguoiLayHang = new ArrayList<>();
    ArrayList<DonHang> dataNguoiGiaoHang = new ArrayList<>();
    int donHangCholay, donHangDaLay, donHangChoGiao, donHangDaGiao,donHangKhongThanhCong;
    User user = new User();

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_home);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setControl();
        setEvent();
        loadData();
        bottomNavigationView.setSelectedItemId(R.id.mn_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_home:
                        startActivity(new Intent(getApplicationContext(), NV_Home.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.mn_profile:
                        startActivity(new Intent(getApplicationContext(), NV_Profile.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.mn_About:
                        startActivity(new Intent(getApplicationContext(), NV_About.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.mn_Setting:
                        startActivity(new Intent(getApplicationContext(), NV_Setting.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    //h??m n??y ch???y l???i m???i khi database b??? thay ?????i
    private void loadData() {
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //v?? ????y ph???i set l???i d??? li???u
                //kh??ng n?? l???i
                //xong l??m hi???n th??? th??ng tin m??n h??nh home ??i
                //r???i c??i about ch??p c???a huy v??
                //c??i t??n lay out adapter ?????t l???i theo chu???n
                //l??y 2 module t n??i h??i chi???u gh??p v??o
                //r???i g???p file ??i
                //m??i demo

                donHangDaGiao = 0;
                donHangDaLay = 0;
                donHangCholay = 0;
                donHangChoGiao = 0;
                donHangKhongThanhCong =0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getNguoiLayHang() != null) {
                        dataNguoiLayHang.add(donHang);
                        if (donHang.getNguoiLayHang().getMa().equals(auth.getUid()) && donHang.getTrangThaiDonHang().equals("???? giao nh??n vi??n ??i l???y h??ng")) {
                            donHangCholay++;
                        }
                        if (donHang.getNguoiLayHang().getMa().equals(auth.getUid()) && donHang.getTrangThaiDonHang().equals("???? l???y h??ng")) {
                            donHangDaLay++;
                        }


                        if (donHang.getNguoiGiaoHang() != null) {
                            dataNguoiGiaoHang.add(donHang);
                            if (donHang.getNguoiGiaoHang().getMa().equals(auth.getUid()) && donHang.getTrangThaiDonHang().equals("???? giao nh??n vi??n ??i ph??t")) {
                                donHangChoGiao++;
                            }
                            if (donHang.getNguoiGiaoHang().getMa().equals(auth.getUid()) && donHang.getTrangThaiDonHang().equals("???? giao h??ng")) {
                                donHangDaGiao++;
                            }
                            if (donHang.getNguoiGiaoHang().getMa().equals(auth.getUid()) && donHang.getTrangThaiDonHang().equals("Giao h??ng kh??ng th??nh c??ng")) {
                                donHangKhongThanhCong++;
                            }
                        }
                    }
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvDonHangChoLay.setText(String.valueOf(donHangCholay));
                            tvDonHangDaLay.setText(String.valueOf(donHangDaLay));
                            tvDonHangChoGiao.setText(String.valueOf(donHangChoGiao));
                            tvDonHangDaGiao.setText(String.valueOf(donHangDaGiao));
                            tvDonHangKhongThanhCong.setText(String.valueOf(donHangKhongThanhCong));
                        }
                    }, 100);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child("user").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                tvHoTen.setText(user.getHoTen().toString());
                tvChucVu.setText(user.getViTriCV().toString());
                tvEmail.setText(user.getEmail().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setControl() {
        btnLogout = findViewById(R.id.btnLogout);
        bottomNavigationView = findViewById((R.id.bottom_nav));
        ivNhanDonHang = findViewById(R.id.ivNhanDonHang);
        ivGiaoDonHang = findViewById(R.id.ivGiaoDonHang);
        ivDoanhThu = findViewById(R.id.ivDoanhThu);
        tvDonHangChoLay = findViewById(R.id.tvSoLanDiNhanHang);
        tvDonHangDaLay = findViewById(R.id.tvSoLanDaNhanHang);
        tvDonHangChoGiao = findViewById(R.id.tvSoLanDiGiaoHang);
        tvDonHangDaGiao = findViewById(R.id.tvSoLanDaGiaoHang);
        tvDonHangKhongThanhCong =findViewById(R.id.tvGiaoKhongThanhCong);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvChucVu = findViewById(R.id.tvChucVu);
        tvEmail = findViewById(R.id.tvEmail);
    }

    private void setEvent() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        ivNhanDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NV_NhanDongHang.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        ivGiaoDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NV_GiaoDonHang.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        ivDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NV_DoanhThu.class));
                overridePendingTransition(0, 0);
                finish();
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
        Toast.makeText(this, "Nh???n l???n n???a ????? ????ng xu???t!", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}