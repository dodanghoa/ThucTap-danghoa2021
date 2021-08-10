package com.example.ud_giaohang.NhanVien_Screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NV_ChiTietDonHang extends AppCompatActivity {
    ImageButton ibtnBack;
    TextView tvMaDonHang, tvTenHangHoa, tvTrangThai, tvHoTen, tvSDT, tvDiaChi, tvDiaChiGui, tvDiaChiNhan, tvTongCuoc, tvGhiChu;
    ImageView ivAnh1, ivAnh2, ivAnh3;
    DatabaseReference mDatabase;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageRef;
    DonHang donHang;
    List<String> image;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_chitietdonhang);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
        storageRef = storage.getReference().child("image");
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataDonHang = intent.getStringExtra("dataDonHang");
            Gson gson = new Gson();
            donHang = gson.fromJson(dataDonHang, DonHang.class);
            LoadInforDonHang();
        }
        setEvent();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void LoadInforDonHang() {
        tvMaDonHang.setText(donHang.getMaDonHang());
        tvTrangThai.setText(donHang.getTrangThaiDonHang());
        tvHoTen.setText(donHang.getHoTen());
        tvSDT.setText(donHang.getSoDT());
        tvDiaChi.setText(donHang.getDiaChi());
        tvTenHangHoa.setText(donHang.getTenHang());
        tvTongCuoc.setText(donHang.getTongCuoc());
        tvGhiChu.setText(donHang.getGhiChu());
        tvDiaChiGui.setText(donHang.getNguoiGui().getDiaChi());
        tvDiaChiNhan.setText(donHang.getDiaChi());
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
                    storageRef.child("DonHang_Image" + donHang.getMaDonHang() + "/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri.toString()).into(ivAnh2);
                        }
                    });
                }
                if (image.equals("anh3")) {
                    storageRef.child("DonHang_Image" + donHang.getMaDonHang() + "/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri.toString()).into(ivAnh2);
                        }
                    });
                }
            });
        }
    }

    private void setEvent() {
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setControl() {
        tvMaDonHang = findViewById(R.id.tvMaDonHang);
        tvTenHangHoa = findViewById(R.id.tv_tenhanghoa);
        tvTrangThai = findViewById(R.id.tvTrangThai);
        tvHoTen = findViewById(R.id.tv_tennguoinhan);
        tvSDT = findViewById(R.id.tv_sodienthoai);
        tvDiaChi = findViewById(R.id.tv_diachi);
        tvTongCuoc = findViewById(R.id.tv_tongcuoc);
        tvGhiChu = findViewById(R.id.tv_ghichu);
        tvDiaChiGui = findViewById(R.id.tvDiaChiGui);
        tvDiaChiNhan = findViewById(R.id.tvDiaChiNhan);
        ivAnh1 = findViewById(R.id.iv_anh1);
        ivAnh2 = findViewById(R.id.iv_anh2);
        ivAnh3 = findViewById(R.id.iv_anh3);
        ibtnBack = findViewById(R.id.ibtnBack);
    }
}