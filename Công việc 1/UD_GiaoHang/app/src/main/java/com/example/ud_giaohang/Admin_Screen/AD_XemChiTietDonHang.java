package com.example.ud_giaohang.Admin_Screen;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.adapter.AD_NhanVienSpinnerAdapter;
import com.example.ud_giaohang.model.DonHang;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AD_XemChiTietDonHang extends AppCompatActivity {
    User user;
    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    TextView tvTennguoigui, tvSonguoigui, tvDiachinguoigui, tvTennguoinhan, tvSonguoinhan, tvDiachinguoinhan,
            tvTenhanghoa, tvGiatrihanghoa, tvTrongluong, tvSoluong, tvTienthuho, tvNguoitracuoc, tvThoigiangiao,
            tvGhichu, tvCuocphi, tvTongcuoc, tvTrangthai,tvMaDonHang;
    ImageView ivAnh1, ivAnh2, ivAnh3;
    Spinner spNhanVienDiLay, spNhanVienDiPhat;
    Button xacNhan, tuChoi, giaoNhanVienLay, btnXacNhanDiLay, giaoNhanVienPhat, btnXacNhanDiPhat;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    DonHang donHang = new DonHang();
    ArrayList<User> users;
    List<String> image;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = new User();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
        storageRef = storage.getReference("image");
        setContentView(R.layout.activity_ad_xem_chi_tiet_don_hang);
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataDonHang = intent.getStringExtra("dataDonHang");
            Gson gson = new Gson();
            donHang = gson.fromJson(dataDonHang, DonHang.class);
            LoadInfoDonHang();
        }
        setEvent();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void LoadInfoDonHang() {
        tvTennguoinhan.setText(donHang.getHoTen());
        tvMaDonHang.setText(donHang.getMaDonHang());
        tvSonguoinhan.setText(donHang.getSoDT());
        tvDiachinguoinhan.setText(donHang.getDiaChi());
        tvTenhanghoa.setText(donHang.getTenHang());
        tvGiatrihanghoa.setText(donHang.getTienHang());
        tvTrongluong.setText(donHang.getTrongLuong());
        tvSoluong.setText(donHang.getSoLuong());
        tvTienthuho.setText(donHang.getTienThuHo());
        tvNguoitracuoc.setText(donHang.getNguoiTra());
        tvThoigiangiao.setText(donHang.getThoiGianGiao());
        tvGhichu.setText(donHang.getGhiChu());
        tvCuocphi.setText(donHang.getCuocPhi());
        tvTongcuoc.setText(donHang.getTongCuoc());
        tvTennguoigui.setText(donHang.getNguoiGui().getHoTen());
        tvSonguoigui.setText(donHang.getNguoiGui().getSoDT());
        tvDiachinguoigui.setText(donHang.getNguoiGui().getDiaChi());
        tvTrangthai.setText(donHang.getTrangThaiDonHang());
        image = donHang.getImage();
        if (image != null) {
            image.forEach(image -> {
                if (image.equals("anh1")) {
                    storageRef.child("DonHang_Image/" + donHang.getMaDonHang() + "/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.get().load(uri.toString()).into(ivAnh1);
                        }
                    });
                }
                if (image.equals("anh2")) {
                    storageRef.child("DonHang_Image/" + donHang.getMaDonHang() + "/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.get().load(uri.toString()).into(ivAnh2);

                        }
                    });
                }
                if (image.equals("anh3")) {
                    storageRef.child("DonHang_Image/" + donHang.getMaDonHang() + "/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.get().load(uri.toString()).into(ivAnh3);
                        }
                    });
                }
            });
        }
        if (donHang.getTrangThaiDonHang().equals("Đã tạo đơn hàng"))
        {
            xacNhan.setVisibility(View.VISIBLE);
            tuChoi.setVisibility(View.VISIBLE);
        }
        //Nếu trạng thái là đã xác nhận thì ẩn 2 nút dưới
        if (donHang.getTrangThaiDonHang().equals("Đã xác nhận"))
        {
            giaoNhanVienLay.setVisibility(View.VISIBLE);
        }
        if (donHang.getTrangThaiDonHang().equals("Đã lấy hàng"))
        {

            giaoNhanVienPhat.setVisibility(View.VISIBLE);

        }
        if(donHang.getTrangThaiDonHang().equals("Đã xác nhận"))
        {
            tvTrangthai.setTextColor(Color.WHITE);
            tvTrangthai.setBackgroundColor(Color.parseColor("#FF18855C"));
        }
        if(donHang.getTrangThaiDonHang().equals("Đã lấy hàng"))
        {
            tvTrangthai.setTextColor(Color.WHITE);
            tvTrangthai.setBackgroundColor(Color.parseColor("#FF18855C"));

        }
        if(donHang.getTrangThaiDonHang().equals("Đã từ chối"))
        {
            tvTrangthai.setTextColor(Color.WHITE);
            tvTrangthai.setBackgroundColor(Color.parseColor("#FFBF4141"));

        }
        if(donHang.getTrangThaiDonHang().equals("Đã giao hàng"))
        {
            tvTrangthai.setTextColor(Color.WHITE);
            tvTrangthai.setBackgroundColor(Color.parseColor("#FF18855C"));

        }
        if(donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi lấy hàng"))
        {
            tvTrangthai.setTextColor(Color.WHITE);
            tvTrangthai.setBackgroundColor(Color.parseColor("#C9F39103"));

        }
        if(donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi phát"))
        {
            tvTrangthai.setTextColor(Color.WHITE);
            tvTrangthai.setBackgroundColor(Color.parseColor("#C9F39103"));
        }

    }

    private void setEvent() {
        xacNhan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                donHang.setTrangThaiDonHang("Đã xác nhận");
                mDatabase.child("DonHang").child(donHang.getMaDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(v.getContext(), "Đã xác nhận ĐƠn Hàng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                xacNhan.setVisibility(View.GONE);
                tuChoi.setVisibility(View.GONE);
                LoadInfoDonHang();
            }
        });
        tuChoi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                donHang.setTrangThaiDonHang("Đã từ chối");
                mDatabase.child("DonHang").child(donHang.getMaDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(v.getContext(), "Đã Từ Chối ĐƠn Hàng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                LoadInfoDonHang();
            }
        });

        giaoNhanVienLay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                spNhanVienDiLay.setVisibility(View.VISIBLE);
                btnXacNhanDiLay.setVisibility(View.VISIBLE);
                LoadUser();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                AD_NhanVienSpinnerAdapter adapter = new AD_NhanVienSpinnerAdapter(AD_XemChiTietDonHang.this,R.layout.custom_danh_sach_tai_khoan,users);

                                spNhanVienDiLay.setAdapter(adapter);
                            }
                        },
                        100);

            }
        });
        giaoNhanVienPhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spNhanVienDiPhat.setVisibility(View.VISIBLE);
                btnXacNhanDiPhat.setVisibility(View.VISIBLE);
                LoadUser();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                AD_NhanVienSpinnerAdapter adapter = new AD_NhanVienSpinnerAdapter(AD_XemChiTietDonHang.this,R.layout.custom_danh_sach_tai_khoan,users);
                                spNhanVienDiPhat.setAdapter(adapter);
                            }
                        },
                        100);

            }
        });

        btnXacNhanDiLay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                donHang.setTrangThaiDonHang("Đã giao nhân viên đi lấy hàng");
                donHang.setNguoiLayHang(users.get(spNhanVienDiLay.getSelectedItemPosition()));
                mDatabase.child("DonHang").child(donHang.getMaDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(v.getContext(), "Đã giao nhân viên đi lấy hàng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                LoadInfoDonHang();
                spNhanVienDiLay.setVisibility(View.GONE);
                btnXacNhanDiLay.setVisibility(View.GONE);
                giaoNhanVienLay.setVisibility(View.GONE);
            }
        });
        btnXacNhanDiPhat.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                donHang.setTrangThaiDonHang("Đã giao nhân viên đi phát");
                donHang.setNguoiGiaoHang(users.get(spNhanVienDiPhat.getSelectedItemPosition()));
                mDatabase.child("DonHang").child(donHang.getMaDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(v.getContext(), "Đã giao nhân viên đi phát hàng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                LoadInfoDonHang();
                spNhanVienDiPhat.setVisibility(View.GONE);
                btnXacNhanDiPhat.setVisibility(View.GONE);
                giaoNhanVienPhat.setVisibility(View.GONE);
            }
        });
    }
    public void LoadUser() {
        DatabaseReference userChilds = mDatabase.child("user");
        userChilds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> temp = new ArrayList<>();
//                Toast.makeText(Admin_DanhSachTaiKhoan.this,snapshot.getChildrenCount()+"", Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (user.getViTriCV().equals("Nhân viên"))
                    {
                        temp.add(user);
                    }
                }
                users = temp;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setControl() {
        tvTenhanghoa = findViewById(R.id.tv_tenhanghoa);
        tvTennguoigui = findViewById(R.id.tv_tennguoigui);
        tvTennguoinhan = findViewById(R.id.tv_tennguoinhan);
        tvSonguoigui = findViewById(R.id.tv_songuoigui);
        tvSonguoinhan = findViewById(R.id.tv_sodienthoai);
        tvDiachinguoigui = findViewById(R.id.tv_diachinguoigui);
        tvDiachinguoinhan = findViewById(R.id.tv_diachinguoinhan);
        tvGiatrihanghoa = findViewById(R.id.tv_giatrihanghoa);
        tvTrongluong = findViewById(R.id.tv_sotrongluong);
        tvSoluong = findViewById(R.id.tv_soluong);
        tvTienthuho = findViewById(R.id.tv_tienthuho);
        tvMaDonHang = findViewById(R.id.txtMaDonHang);
        tvNguoitracuoc = findViewById(R.id.tv_nguoitratiencuoc);
        tvThoigiangiao = findViewById(R.id.tv_thoigiangiao);
        tvGhichu = findViewById(R.id.tv_ghichu);
        tvCuocphi = findViewById(R.id.tv_cuocphi);
        tvTongcuoc = findViewById(R.id.tv_tongcuoc);
        ivAnh1 = findViewById(R.id.iv_anh1);
        ivAnh2 = findViewById(R.id.iv_anh2);
        ivAnh3 = findViewById(R.id.iv_anh3);
        tvTrangthai = findViewById(R.id.tv_trangthai);
        spNhanVienDiLay = findViewById(R.id.spNhanVienDiLay);
        spNhanVienDiPhat = findViewById(R.id.spNhanvienDiPhat);
        xacNhan = findViewById(R.id.btn_XacNhan);
        tuChoi = findViewById(R.id.btn_TuChoi);
        giaoNhanVienLay = findViewById(R.id.btn_GiaoNhanVienDiLay);
        btnXacNhanDiLay = findViewById(R.id.btnXacNhanDiLay);
        giaoNhanVienPhat = findViewById(R.id.btn_GiaoNhanVienDiPhat);
        btnXacNhanDiPhat = findViewById(R.id.btnXacNhanDiPhat);
    }
}