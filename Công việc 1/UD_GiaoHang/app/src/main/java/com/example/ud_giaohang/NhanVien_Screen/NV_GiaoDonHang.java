package com.example.ud_giaohang.NhanVien_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.adapter.NV_GiaoDonHangAdapter;
import com.example.ud_giaohang.model.DonHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NV_GiaoDonHang extends AppCompatActivity {
    ListView lvGiaoHang;
    ImageView ivLogo;
    ImageButton ibtnBack;
    TextView tvMaDonHang, tvTienCuoc, tvTienThuHo, tvDiaChiGui, tvDiaChiNhan, tvTrangThai;
    Button btnXacNhanGiaoHang;
    private NV_GiaoDonHangAdapter giaoDonHangAdapter;
    DatabaseReference mDatabase;
    FirebaseAuth auth;
    ArrayList<DonHang> data = new ArrayList<>();
    SearchView svGiaoDonHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_giaodonhang);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        giaoDonHangAdapter = new NV_GiaoDonHangAdapter(NV_GiaoDonHang.this, R.layout.custom_item_giaohang_nv, data);
        setControl();
        LoadData("");
        setEvent();
    }

    private void LoadData(String key) {

        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (key.isEmpty()) {
                        if (donHang.getNguoiGiaoHang() != null) {
                            if (donHang.getNguoiGiaoHang().getMa().equals(auth.getUid())) {
                                if (donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi phát")
                                        || donHang.getTrangThaiDonHang().equals("Nhân viên đang đi giao hàng")
                                        || donHang.getTrangThaiDonHang().equals("Đã giao hàng")
                                        || donHang.getTrangThaiDonHang().equals("Giao hàng không thành công")) {
                                    data.add(donHang);
                                }
                            }
                        }
                    } else {
                        if (donHang.getNguoiGiaoHang() != null) {
                            if (donHang.getNguoiGiaoHang().getMa().equals(auth.getUid()) && donHang.getMaDonHang().contains(key)) {
                                if (donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi phát")
                                        || donHang.getTrangThaiDonHang().equals("Nhân viên đang đi giao hàng")
                                        || donHang.getTrangThaiDonHang().equals("Đã giao hàng")
                                        || donHang.getTrangThaiDonHang().equals("Giao hàng không thành công")) {
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
                giaoDonHangAdapter = new NV_GiaoDonHangAdapter(NV_GiaoDonHang.this, R.layout.custom_item_giaohang_nv, data);
                lvGiaoHang.setAdapter(giaoDonHangAdapter);
                giaoDonHangAdapter.notifyDataSetChanged();
            }
        }, 200);
    }

    private void setEvent() {
        svGiaoDonHang.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ivLogo = findViewById(R.id.ivLogo);
        tvTienCuoc = findViewById(R.id.tvTienCuoc);
        tvTienThuHo = findViewById(R.id.tvTienThuHo);
        tvDiaChiGui = findViewById(R.id.tvDiaChiGui);
        tvDiaChiNhan = findViewById(R.id.tvDiaChiNhan);
        tvTrangThai = findViewById(R.id.tv_trangthai);
        tvMaDonHang = findViewById(R.id.tv_madonhang);
        lvGiaoHang = findViewById(R.id.lvGiaoDonHang);
        svGiaoDonHang = findViewById(R.id.searchViewGiaoDonHang);
        ibtnBack = findViewById(R.id.ibtnBackGiaoDonHang);
    }
}