package com.example.ud_giaohang.KhachHang_Screen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.adapter.KH_DonHangAdapter;
import com.example.ud_giaohang.model.DonHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class KH_XemDonHang extends AppCompatActivity {

    ListView lv_dsdonhang;
    DatabaseReference mDatabase;
    ArrayList<DonHang> donHangs = new ArrayList<>();
    ArrayList<DonHang> display;
    DonHang donHang;
    private FirebaseAuth auth;
    TextView tenNguoiNhan, soDienThoai, diaChi, tongCuoc, tenHang, ghiChu;
    TextView maDH;
    KH_DonHangAdapter KHDonHangAdapter;
    SearchView searchView;
    Spinner spLoc;
    ImageButton ibback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kh_xem_don_hang_);
        auth = FirebaseAuth.getInstance();
        donHang = new DonHang();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setControll();
        setEvent();
        LoadData("");

    }

    private void LoadData(String key) {
        donHangs.clear();
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangs.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DonHang donHang = postSnapshot.getValue(DonHang.class);
                    if (key.isEmpty()) {
                        if (donHang.getNguoiGui().getMa().equals(auth.getUid())) {
                            donHangs.add(donHang);
                        }
                    } else if (donHang.getNguoiGui().getMa().equals(auth.getUid()) && (donHang.getMaDonHang().contains(key))) {
                        donHangs.add(donHang);
                    }

                }
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                KHDonHangAdapter = new KH_DonHangAdapter(KH_XemDonHang.this, R.layout.custom_item_don_hang_kh, donHangs);
                                lv_dsdonhang.setAdapter(KHDonHangAdapter);
                            }
                        },
                        300);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void setEvent() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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

        ibback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),KH_Home.class);
                startActivity(intent);
            }
        });

        spLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

                switch (spLoc.getSelectedItem().toString()) {

                    case "Đã tạo đơn":
                        display = (ArrayList<DonHang>) donHangs.stream().
                                filter(donHang -> donHang.getTrangThaiDonHang().equals("Đã tạo đơn hàng"))
                                .collect(Collectors.toList());
                        KHDonHangAdapter = new KH_DonHangAdapter(KH_XemDonHang.this, R.layout.custom_item_don_hang_kh, display);
                        lv_dsdonhang.setAdapter(KHDonHangAdapter);
                        break;

                    case "Đã từ chối":
                        display = (ArrayList<DonHang>) donHangs.stream().
                                filter(donHang -> donHang.getTrangThaiDonHang().equals("Đã từ chối"))
                                .collect(Collectors.toList());
                        KHDonHangAdapter = new KH_DonHangAdapter(KH_XemDonHang.this, R.layout.custom_item_don_hang_kh, display);
                        lv_dsdonhang.setAdapter(KHDonHangAdapter);
                        break;

                    case "Chờ lấy":
                        display = (ArrayList<DonHang>) donHangs.stream().
                                filter(donHang -> donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi lấy hàng"))
                                .collect(Collectors.toList());
                        KHDonHangAdapter = new KH_DonHangAdapter(KH_XemDonHang.this, R.layout.custom_item_don_hang_kh, display);
                        lv_dsdonhang.setAdapter(KHDonHangAdapter);
                        break;

                    case "Đã lấy":
                        display = (ArrayList<DonHang>) donHangs.stream().
                                filter(donHang -> donHang.getTrangThaiDonHang().equals("Đã lấy hàng"))
                                .collect(Collectors.toList());
                        KHDonHangAdapter = new KH_DonHangAdapter(KH_XemDonHang.this, R.layout.custom_item_don_hang_kh, display);
                        lv_dsdonhang.setAdapter(KHDonHangAdapter);
                        break;

                    case "Đang giao":
                        display = (ArrayList<DonHang>) donHangs.stream().
                                filter(donHang -> donHang.getTrangThaiDonHang().equals("Nhân viên đang đi giao hàng"))
                                .collect(Collectors.toList());
                        KHDonHangAdapter = new KH_DonHangAdapter(KH_XemDonHang.this, R.layout.custom_item_don_hang_kh, display);
                        lv_dsdonhang.setAdapter(KHDonHangAdapter);
                        break;

                    case "Giao thành công":
                        display = (ArrayList<DonHang>) donHangs.stream().
                                filter(donHang -> donHang.getTrangThaiDonHang().equals("Đã giao hàng"))
                                .collect(Collectors.toList());
                        KHDonHangAdapter = new KH_DonHangAdapter(KH_XemDonHang.this, R.layout.custom_item_don_hang_kh, display);
                        lv_dsdonhang.setAdapter(KHDonHangAdapter);
                        break;

                    case "Đã hủy":
                        display = (ArrayList<DonHang>) donHangs.stream().
                                filter(donHang -> donHang.getTrangThaiDonHang().equals("Đã hủy"))
                                .collect(Collectors.toList());
                        KHDonHangAdapter = new KH_DonHangAdapter(KH_XemDonHang.this, R.layout.custom_item_don_hang_kh, display);
                        lv_dsdonhang.setAdapter(KHDonHangAdapter);
                        break;
                        //nếu không thuộc các trường hợp trên thì lấy all
                    default:
                        display = donHangs;
                        KHDonHangAdapter = new KH_DonHangAdapter(KH_XemDonHang.this, R.layout.custom_item_don_hang_kh, display);
                        lv_dsdonhang.setAdapter(KHDonHangAdapter);
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


        private void setControll () {
            lv_dsdonhang = findViewById(R.id.lv_dsdonhang);
            tenNguoiNhan = findViewById(R.id.tv_tennguoinhan);
            soDienThoai = findViewById(R.id.tv_sodienthoai);
            diaChi = findViewById(R.id.tv_diachi);
            tongCuoc = findViewById(R.id.tv_tongcuoc);
            ghiChu = findViewById(R.id.tv_ghichu);
            tenHang = findViewById(R.id.tv_tenhanghoa);
            maDH = findViewById(R.id.tv_madonhang);
            searchView = findViewById(R.id.searchView);
            spLoc = findViewById(R.id.spLoc);
            ibback = findViewById(R.id.ibBackKH);
        }


    }