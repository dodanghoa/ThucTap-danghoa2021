package com.example.ud_giaohang.Admin_Screen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.adapter.TaiKhoanAdapter;
import com.example.ud_giaohang.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AD_DanhSachTaiKhoan extends AppCompatActivity {
    private DatabaseReference mDatabase;
    Button btnTaoTaiKhoan;
    ListView lvDanhSachTaiKhoan;
    Spinner spLoc;
    TaiKhoanAdapter taiKhoanAdapter;
    ArrayList<User> arrayLists;
    ArrayList<User> display;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_danh_sach_khach_hang);
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        arrayLists = new ArrayList<>();
        display = new ArrayList<>();
        setControl();
        LoadData();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        setEvent();
                    }
                },
                100);
    }

    public void LoadData() {
        DatabaseReference userChilds = mDatabase.child("user");
        userChilds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> temp = new ArrayList<>();
//                Toast.makeText(Admin_DanhSachTaiKhoan.this,snapshot.getChildrenCount()+"", Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    temp.add(user);
                }
                arrayLists = temp;
                display = temp;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        taiKhoanAdapter = new TaiKhoanAdapter(AD_DanhSachTaiKhoan.this, R.layout.custom_danh_sach_tai_khoan, display);
                        lvDanhSachTaiKhoan.setAdapter(taiKhoanAdapter);
                    }
                },
                300);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData();
    }

    private void setControl() {
        lvDanhSachTaiKhoan = findViewById(R.id.lvDanhSachTaiKhoan);
        btnTaoTaiKhoan = findViewById(R.id.btnTaoTaiKhoanMoi);
        searchView = findViewById(R.id.searchVeiw);
        spLoc = findViewById(R.id.spLoc);
    }

    private void setEvent() {
        Context context = this;
        btnTaoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AD_ThongTinTaiKhoan.class);
                startActivity(intent);
            }
        });
        lvDanhSachTaiKhoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson = new Gson();
                String data = gson.toJson(display.get(position));
                Intent intent = new Intent(context, AD_ThongTinTaiKhoan.class);
                intent.putExtra("dataUser",data);
                startActivity(intent);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextSubmit(String query) {
                Search(query);
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                Search(newText);
                return true;
            }
        });

        spLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(searchView.getQuery().toString().isEmpty())
                { Toast.makeText(context, "aaaaa", Toast.LENGTH_SHORT).show();
                    switch (spLoc.getSelectedItem().toString())
                    {
                        case "Khách hàng":
                            display = (ArrayList<User>) arrayLists.stream().
                                    filter(user -> user.getViTriCV().equals("Khách hàng"))
                                    .collect(Collectors.toList());
                            taiKhoanAdapter = new TaiKhoanAdapter(AD_DanhSachTaiKhoan.this, R.layout.custom_danh_sach_tai_khoan, display);
                            lvDanhSachTaiKhoan.setAdapter(taiKhoanAdapter);
                            break;
                        case "Nhân viên":
                            display = (ArrayList<User>) arrayLists.stream().
                                    filter(user ->  user.getViTriCV().equals("Nhân viên"))
                                    .collect(Collectors.toList());
                            taiKhoanAdapter = new TaiKhoanAdapter(AD_DanhSachTaiKhoan.this, R.layout.custom_danh_sach_tai_khoan, display);
                            lvDanhSachTaiKhoan.setAdapter(taiKhoanAdapter);
                            break;
                        case "Quản lý":
                            display = (ArrayList<User>) arrayLists.stream().
                                    filter(user -> user.getViTriCV().equals("Quản lý "))
                                    .collect(Collectors.toList());
                            taiKhoanAdapter = new TaiKhoanAdapter(AD_DanhSachTaiKhoan.this, R.layout.custom_danh_sach_tai_khoan, display);
                            lvDanhSachTaiKhoan.setAdapter(taiKhoanAdapter);
                            break;
                        default:
                            display = arrayLists;
                            taiKhoanAdapter = new TaiKhoanAdapter(AD_DanhSachTaiKhoan.this, R.layout.custom_danh_sach_tai_khoan, display);
                            lvDanhSachTaiKhoan.setAdapter(taiKhoanAdapter);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Search(String key) {
        if(key.equals(""))
        {
            LoadData();
        }
        else {
            switch (spLoc.getSelectedItem().toString())
            {
                case "Khách hàng":
                    arrayLists.stream().
                            filter(user -> (user.getHoTen().toLowerCase().contains(key.toLowerCase())||
                                    user.getDiaChi().toLowerCase().contains(key.toLowerCase())||
                                    user.getEmail().toLowerCase().contains(key.toLowerCase())||
                                    user.getSoDT().toLowerCase().contains(key.toLowerCase())||
                                    user.getMa().toLowerCase().contains(key.toLowerCase())
                            ) &&user.getViTriCV().equals("Khách hàng"))
                            .collect(Collectors.toList());
                    taiKhoanAdapter = new TaiKhoanAdapter(AD_DanhSachTaiKhoan.this, R.layout.custom_danh_sach_tai_khoan, display);
                    lvDanhSachTaiKhoan.setAdapter(taiKhoanAdapter);
                    break;
                case "Nhân viên":
                    display = (ArrayList<User>) arrayLists.stream().
                            filter(user -> (user.getHoTen().toLowerCase().contains(key.toLowerCase())||
                                    user.getDiaChi().toLowerCase().contains(key.toLowerCase())||
                                    user.getEmail().toLowerCase().contains(key.toLowerCase())||
                                    user.getSoDT().toLowerCase().contains(key.toLowerCase())||
                                    user.getMa().toLowerCase().contains(key.toLowerCase())
                            ) &&user.getViTriCV().equals("Nhân viên"))
                            .collect(Collectors.toList());
                    taiKhoanAdapter = new TaiKhoanAdapter(AD_DanhSachTaiKhoan.this, R.layout.custom_danh_sach_tai_khoan, display);
                    lvDanhSachTaiKhoan.setAdapter(taiKhoanAdapter);
                    break;
                case "Quản lý":
                    display = (ArrayList<User>) arrayLists.stream().
                            filter(user -> (user.getHoTen().toLowerCase().contains(key.toLowerCase())||
                                    user.getDiaChi().toLowerCase().contains(key.toLowerCase())||
                                    user.getEmail().toLowerCase().contains(key.toLowerCase())||
                                    user.getSoDT().toLowerCase().contains(key.toLowerCase())||
                                    user.getMa().toLowerCase().contains(key.toLowerCase())
                            ) &&user.getViTriCV().equals("Quản lý"))
                            .collect(Collectors.toList());
                    taiKhoanAdapter = new TaiKhoanAdapter(AD_DanhSachTaiKhoan.this, R.layout.custom_danh_sach_tai_khoan, display);
                    lvDanhSachTaiKhoan.setAdapter(taiKhoanAdapter);
                    break;
                default:
                    display = (ArrayList<User>) arrayLists.stream().
                            filter(user -> (user.getHoTen().toLowerCase().contains(key.toLowerCase())||
                                    user.getDiaChi().toLowerCase().contains(key.toLowerCase())||
                                    user.getEmail().toLowerCase().contains(key.toLowerCase())||
                                    user.getSoDT().toLowerCase().contains(key.toLowerCase())||
                                    user.getMa().toLowerCase().contains(key.toLowerCase())
                            ))
                            .collect(Collectors.toList());
                    taiKhoanAdapter = new TaiKhoanAdapter(AD_DanhSachTaiKhoan.this, R.layout.custom_danh_sach_tai_khoan, display);
                    lvDanhSachTaiKhoan.setAdapter(taiKhoanAdapter);
                    break;
            }

        }
    }

}