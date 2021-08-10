package com.example.ud_giaohang.NhanVien_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NV_DoanhThu extends AppCompatActivity {
    EditText edtTongCuocPhi, edtTongTienThuHo;
    DatabaseReference mDatabase;
    FirebaseAuth auth;
    ImageButton ibtnBack;
    long tongCuocPhi = 0;
    long tongTienThuHo = 0;
    ArrayList<DonHang> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_doanhthu);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setControl();
        loadData();
        setEvent();
    }

    private void loadData() {
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DonHang donHang = postSnapshot.getValue(DonHang.class);
                    if (donHang.getNguoiGiaoHang() != null) {
                        if (donHang.getNguoiGiaoHang().getMa().equals(auth.getUid()) &&donHang.getTrangThaiDonHang().equals("Đã giao hàng")) {
                            data.add(donHang);
                            tongCuocPhi += Long.parseLong(donHang.getCuocPhi().toString());
                            tongTienThuHo += Long.parseLong(donHang.getTienThuHo().toString());
                        }
                    }

                }
                edtTongCuocPhi.setText(String.valueOf(tongCuocPhi)+" VND");
                edtTongTienThuHo.setText(String.valueOf(tongTienThuHo)+" VND");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 200);
    }

    private void setEvent() {
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
        edtTongCuocPhi = findViewById(R.id.edtTongCuocPhi);
        edtTongTienThuHo = findViewById(R.id.edtTongPhiThuHo);
        ibtnBack = findViewById(R.id.ibBackDoanhThu);
    }
}