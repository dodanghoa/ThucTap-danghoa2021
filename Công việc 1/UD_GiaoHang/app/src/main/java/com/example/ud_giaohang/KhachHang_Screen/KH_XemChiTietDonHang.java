package com.example.ud_giaohang.KhachHang_Screen;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.List;

public class KH_XemChiTietDonHang extends AppCompatActivity {
    User user;
    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    TextView tvTennguoigui, tvSonguoigui, tvDiachinguoigui, tvTennguoinhan, tvSonguoinhan, tvDiachinguoinhan,
            tvTenhanghoa, tvGiatrihanghoa, tvTrongluong, tvSoluong, tvTienthuho, tvNguoitracuoc, tvThoigiangiao, tvGhichu, tvCuocphi, tvTongcuoc, tvTrangthai;
    ImageView ivAnh1, ivAnh2, ivAnh3;
    ImageButton ibback;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    DonHang donHang = new DonHang();
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
        setContentView(R.layout.activity_kh_xem_chi_tiet_don_hang);
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
        if(image != null){
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

    }

    private void setEvent() {
        ibback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),KH_XemDonHang.class);
                startActivity(intent);
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
        tvNguoitracuoc = findViewById(R.id.tv_nguoitratiencuoc);
        tvThoigiangiao = findViewById(R.id.tv_thoigiangiao);
        tvGhichu = findViewById(R.id.tv_ghichu);
        tvCuocphi = findViewById(R.id.tv_cuocphi);
        tvTongcuoc = findViewById(R.id.tv_tongcuoc);
        ivAnh1 = findViewById(R.id.iv_anh1);
        ivAnh2 = findViewById(R.id.iv_anh2);
        ivAnh3 = findViewById(R.id.iv_anh3);
        tvTrangthai = findViewById(R.id.tv_trangthai);
        ibback = findViewById(R.id.ibBackKH);
    }
}