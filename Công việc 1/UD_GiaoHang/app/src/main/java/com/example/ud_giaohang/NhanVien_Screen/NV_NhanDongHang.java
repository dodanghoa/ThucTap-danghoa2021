package com.example.ud_giaohang.NhanVien_Screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.adapter.NV_NhanDonHangAdapter;
import com.example.ud_giaohang.model.DonHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NV_NhanDongHang extends AppCompatActivity {
    ImageButton ibtnBack;
    ListView lvNhanDonHang;
    ImageView ivLogo;
    TextView tvMaDonHang, tvTienCuoc, tvTienThuHo, tvDiaChiGui, tvDiaChiNhan, tvTrangThai;
    Button btn;
    private NV_NhanDonHangAdapter nhanDonHangAdapter;
    DatabaseReference mDatabase;
    FirebaseAuth auth;
    ArrayList<DonHang> data = new ArrayList<>();
    androidx.appcompat.widget.SearchView svNhanDonHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_nhandonhang);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        nhanDonHangAdapter = new NV_NhanDonHangAdapter(NV_NhanDongHang.this, R.layout.custom_item_donhang_nv, data);
        setControl();
        setEvent();
        LoadData("");
    }

    private void LoadData(String key) {
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (key.isEmpty()) {
                        if (donHang.getNguoiLayHang() != null) {
                            if (donHang.getNguoiLayHang().getMa().equals(auth.getUid())) {
                                if (donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi lấy hàng")
                                        || donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi lấy")
                                        || donHang.getTrangThaiDonHang().equals("Nhân viên đang đi lấy hàng")
                                        || donHang.getTrangThaiDonHang().equals("Đã lấy hàng")) {
                                    data.add(donHang);
                                }
                            }
                        }
                    } else {
                        if (donHang.getNguoiLayHang() != null) {
                            if (donHang.getNguoiLayHang().getMa().equals(auth.getUid()) && (donHang.getMaDonHang().contains(key))) {
                                if (donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi lấy hàng")
                                        || donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi lấy")
                                        || donHang.getTrangThaiDonHang().equals("Nhân viên đang đi lấy hàng")
                                        || donHang.getTrangThaiDonHang().equals("Đã lấy hàng")) {
                                    data.add(donHang);
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nhanDonHangAdapter = new NV_NhanDonHangAdapter(NV_NhanDongHang.this, R.layout.custom_item_donhang_nv, data);
                lvNhanDonHang.setAdapter(nhanDonHangAdapter);
                nhanDonHangAdapter.notifyDataSetChanged();
            }
        }, 200);
    }

    private void setEvent() {
        svNhanDonHang.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LoadData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LoadData(newText);
                return false;
            }
        });
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NV_Home.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    private void setControl() {
        lvNhanDonHang = findViewById(R.id.lvNhanDonHang);
        ivLogo = findViewById(R.id.ivLogo);
        tvTienCuoc = findViewById(R.id.tvTienCuoc);
        tvTienThuHo = findViewById(R.id.tvTienThuHo);
        tvDiaChiGui = findViewById(R.id.tvDiaChiGui);
        tvDiaChiNhan = findViewById(R.id.tvDiaChiNhan);
        tvTrangThai = findViewById(R.id.tv_trangthai);
        tvMaDonHang = findViewById(R.id.tv_madonhang);
        ibtnBack = findViewById(R.id.ibtnBackNhanDonHang);
        svNhanDonHang = findViewById(R.id.seachViewNhanDonHang);
    }
}