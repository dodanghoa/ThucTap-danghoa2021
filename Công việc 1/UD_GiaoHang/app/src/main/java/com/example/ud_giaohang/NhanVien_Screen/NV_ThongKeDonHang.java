package com.example.ud_giaohang.NhanVien_Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NV_ThongKeDonHang extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseAuth auth;
    DonHang donHang;
    PieChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_thongkedonhang);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setControl();
        setEvent();
        
    }

    private void setEvent() {
        List<PieEntry> data = new ArrayList<>();

    }

    private void setControl() {
        chart = findViewById(R.id.chart);
    }
}