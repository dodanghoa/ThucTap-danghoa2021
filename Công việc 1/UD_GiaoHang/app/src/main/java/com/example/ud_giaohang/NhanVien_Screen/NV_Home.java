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

    //hàm này chạy lại mỗi khi database bị thay đổi
    private void loadData() {
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //vô đây phải set lại dữ liệu
                //không nó lỗi
                //xong làm hiền thị thông tin màn hình home đi
                //rồi cái about chép của huy vô
                //cái tên lay out adapter đặt lại theo chuản
                //lây 2 module t nói hòi chiều ghép vào
                //rồi gộp file đi
                //mài demo

                donHangDaGiao = 0;
                donHangDaLay = 0;
                donHangCholay = 0;
                donHangChoGiao = 0;
                donHangKhongThanhCong =0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getNguoiLayHang() != null) {
                        dataNguoiLayHang.add(donHang);
                        if (donHang.getNguoiLayHang().getMa().equals(auth.getUid()) && donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi lấy hàng")) {
                            donHangCholay++;
                        }
                        if (donHang.getNguoiLayHang().getMa().equals(auth.getUid()) && donHang.getTrangThaiDonHang().equals("Đã lấy hàng")) {
                            donHangDaLay++;
                        }


                        if (donHang.getNguoiGiaoHang() != null) {
                            dataNguoiGiaoHang.add(donHang);
                            if (donHang.getNguoiGiaoHang().getMa().equals(auth.getUid()) && donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi phát")) {
                                donHangChoGiao++;
                            }
                            if (donHang.getNguoiGiaoHang().getMa().equals(auth.getUid()) && donHang.getTrangThaiDonHang().equals("Đã giao hàng")) {
                                donHangDaGiao++;
                            }
                            if (donHang.getNguoiGiaoHang().getMa().equals(auth.getUid()) && donHang.getTrangThaiDonHang().equals("Giao hàng không thành công")) {
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
        Toast.makeText(this, "Nhấn lần nữa để đăng xuất!", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}