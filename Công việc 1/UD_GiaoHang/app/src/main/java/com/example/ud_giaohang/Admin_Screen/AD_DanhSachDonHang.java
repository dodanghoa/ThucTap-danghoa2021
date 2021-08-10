package com.example.ud_giaohang.Admin_Screen;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.adapter.AD_DonHangAdapter;
import com.example.ud_giaohang.adapter.KH_DonHangAdapter;
import com.example.ud_giaohang.model.DonHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AD_DanhSachDonHang extends AppCompatActivity {
    AD_DonHangAdapter ad_donHangAdapter;
    ListView lv_dsdonhang;
    SearchView searchView;
    Spinner spLoc,spSort;
    ImageButton sort;
    DatabaseReference mDatabase;
    ArrayList<DonHang> donHangs = new ArrayList<>();
    ArrayList<DonHang> display = new ArrayList<>();
    DonHang donHang;
    private FirebaseAuth auth;
    TextView tenNguoiNhan, soDienThoai, diaChi, tongCuoc, tenHang, ghiChu;
    TextView maDH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_danh_sach_don_hang);
        auth = FirebaseAuth.getInstance();
        ad_donHangAdapter = new AD_DonHangAdapter(AD_DanhSachDonHang.this, R.layout.custom_item_don_hang_admin, donHangs);
        donHang = new DonHang();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setControll();
        LoadData("");
        setEvent();
    }

    private void LoadData(String key) {
        lv_dsdonhang.setAdapter(ad_donHangAdapter);
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangs.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DonHang donHang = postSnapshot.getValue(DonHang.class);
                    //Nếu chuỗi nhập tìm rỗng
                    if (key.isEmpty()) {
                        //kiểm tra spinner khac All
                        if (!spLoc.getSelectedItem().toString().equals("All")) {
                            //Lấy ra đơn hàng có trạng thái == spinner
                            if (donHang.getTrangThaiDonHang().equals(spLoc.getSelectedItem().toString())) {
                                donHangs.add(donHang);
                            }
                        }
                        //Nếu là All
                        else {
                            donHangs.add(donHang);
                        }
                    }
                    //chuỗi nhập vào khác rỗng
                    else {
                        if (!spLoc.getSelectedItem().toString().equals("All")) {
                            //Lấy ra đơn hàng có trạng thái == spinner và giá trị của đối tượng == key
                            if (donHang.getTrangThaiDonHang().equals(spLoc.getSelectedItem().toString()) &&
                                    (donHang.getHoTen().toLowerCase().contains(key) || donHang.getMaDonHang().toLowerCase().contains(key)
                                            || donHang.getDiaChi().toLowerCase().contains(key) || donHang.getNguoiGui().getHoTen().toLowerCase().contains(key)
                                            || donHang.getNguoiGui().getSoDT().contains(key) || donHang.getSoDT().contains(key))
                            ) {
                                donHangs.add(donHang);
                            }
                        }
                        //Nếu là All
                        else {
                            //Lấy ra đơn hàng có giá trị của đối tượng == key
                            if ( donHang.getHoTen().toLowerCase().contains(key) || donHang.getMaDonHang().toLowerCase().contains(key)
                                    || donHang.getDiaChi().toLowerCase().contains(key) || donHang.getNguoiGui().getHoTen().toLowerCase().contains(key)
                                    || donHang.getNguoiGui().getSoDT().contains(key) || donHang.getSoDT().contains(key)) {
                                donHangs.add(donHang);
                            }
                        }
                    }
                }
                Collections.sort(donHangs, new Comparator<DonHang>() {
                    @Override
                    public int compare(DonHang lhs, DonHang rhs) {
                        return rhs.getTrangThaiDonHang().compareTo(lhs.getTrangThaiDonHang());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ad_donHangAdapter.notifyDataSetChanged();
                    }
                },
                100);
    }
    private void setEvent() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LoadData(query.toLowerCase());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LoadData(newText.toLowerCase());
                return true;
            }
        });
        spLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadData(searchView.getQuery().toString().toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(donHangs);
                ad_donHangAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setControll() {
        lv_dsdonhang = findViewById(R.id.lv_dsdonhang);
        searchView = findViewById(R.id.searchVeiw);
        spLoc = findViewById(R.id.spLoc);
        sort = findViewById(R.id.sort);
    }
}