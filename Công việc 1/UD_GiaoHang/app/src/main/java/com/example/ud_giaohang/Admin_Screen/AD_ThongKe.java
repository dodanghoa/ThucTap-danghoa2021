package com.example.ud_giaohang.Admin_Screen;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;

public class AD_ThongKe extends AppCompatActivity {
    int soLuongDongHang =0 ;
    int daTaoDonHang=0;
    int daXacNhan=0;
    int daTuChoi=0;
    int daGiaoNhanVienDiLay=0;
    int daXacNhanLayhang=0;
    int daLayHang=0;
    int daGiaoNhanVienDiPhat=0;
    int daXacNhanGiaoHang=0;
    int daGiaoHang=0;
    int tongCuocPhi=0;
    int dahuy=0;
    int giaoKhongThanhCong =0;
    int tongThuHo=0;
    private PieChart pieChart;
    FirebaseAuth auth;
    DatabaseReference mDatabase;
    ArrayList<DonHang> donHangArrayList = new ArrayList<>();
    DatabaseReference reference;

    TextView tongDon,daTaoDon, daXacNhanDon, daTuChoiDon, daGiaoNhanVienDiLayHang,
            daGiaoNhanVienDiPhatHang, nhanVienDangDiLayHang, DaLayHang, nhanVienDangDiGiaoHang,
            DaGiaoHang,txtTongTienCuoc,txtTongTienThuHo,txtdahuy,txtgiaohangkhongthangcong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_thongke);
        setConTrol();
        reference = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        LayDonHang();
        setupPieCharts();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        loadPieChart();
                    }
                },
                100);
    }



    private void LayDonHang() {
        DatabaseReference databaseReference = reference.child("DonHang");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    DonHang donHang = ds.getValue(DonHang.class);
                    int cuocPhi = Integer.parseInt((donHang.getCuocPhi()));
                    tongCuocPhi += cuocPhi;
                    int thuHo = Integer.parseInt((donHang.getTienThuHo()));
                    tongThuHo += thuHo;
                    donHangArrayList.add(donHang);
                    if(donHang.getTrangThaiDonHang().equals("Giao hàng không thành công"))
                    {
                        giaoKhongThanhCong++;
                    }
                    if (donHang.getTrangThaiDonHang().equals("Đã hủy")) {
                        dahuy++;
                    } else {
                        if(donHang.getTrangThaiDonHang().equals("Đã tạo đơn hàng"))
                        {
                            daTaoDonHang++;

                        }
                        if(donHang.getTrangThaiDonHang().equals("Đã xác nhận"))
                        {
                            daXacNhan++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("Đã từ chối"))
                        {
                            daTuChoi++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi lấy hàng"))
                        {
                            daGiaoNhanVienDiLay++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi phát"))
                        {
                            daGiaoNhanVienDiPhat++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("Nhân viên đang đi lấy hàng"))
                        {
                            daXacNhanLayhang++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("Đã lấy hàng"))
                        {
                            daLayHang++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("Nhân viên đang đi giao hàng"))
                        {
                            daXacNhanGiaoHang++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("Đã giao hàng"))
                        {
                            daGiaoHang++;
                        }
                        txtTongTienCuoc.setText((donHang.getCuocPhi()));
                    }
                }
                soLuongDongHang = donHangArrayList.size();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);

    }

    private void setupPieCharts(){
        pieChart.setHoleRadius(50);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(20);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterText("");
        pieChart.setEntryLabelTextSize(0);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterTextColor(Color.DKGRAY);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setFormLineWidth(50);
        l.setTextSize(11);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
        pieChart.animateXY(1000, 1000);
    }

    private void loadPieChart(){

        //Khởi tạo thông số
        ArrayList<PieEntry> entries = new ArrayList<>();

        if (daTaoDonHang!=0)
        {
            entries.add(new PieEntry((float) daTaoDonHang/soLuongDongHang,"Đơn hàng chờ xác nhận"));
        }

        if (daXacNhan!=0)
        {
            entries.add(new PieEntry((float)daXacNhan/soLuongDongHang,"Đơn hàng đã xác nhận"));
        }
        if (daGiaoNhanVienDiLay!=0)
        {
            entries.add(new PieEntry((float)daGiaoNhanVienDiLay/soLuongDongHang,"Đã giao nhân viên đi lấy"));
        }

        if (daXacNhanLayhang!=0)
        {
            entries.add(new PieEntry((float)daXacNhanLayhang/soLuongDongHang,"Nhân viên đang giao hàng"));
        }

        if (daTuChoi!=0)
        {
            entries.add(new PieEntry((float)daTuChoi/soLuongDongHang,"Đã từ chối"));
        }

        if (daLayHang!=0)
        {
            entries.add(new PieEntry((float)daLayHang/soLuongDongHang,"Đã lưu kho chờ vận chuyển"));
        }

        if (daGiaoNhanVienDiPhat!=0)
        {
            entries.add(new PieEntry((float)daGiaoNhanVienDiPhat/soLuongDongHang,"Đã giao nhân viên đi phát hàng"));
        }

        if (daXacNhanGiaoHang!=0)
        {
            entries.add(new PieEntry((float)daXacNhanGiaoHang/soLuongDongHang,"Nhân viên đang giao hàng "));
        }

        if (daGiaoHang!=0)
        {
            entries.add(new PieEntry((float)daGiaoHang/soLuongDongHang," Giao hàng thành công"));
        }if (giaoKhongThanhCong!=0)
        {
            entries.add(new PieEntry((float)daGiaoHang/soLuongDongHang," Giao hàng không thành công"));
        }if (dahuy!=0)
        {
            entries.add(new PieEntry((float)daGiaoHang/soLuongDongHang," Đã hủy"));
        }
        //Màu mè hoa lá hẹ
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);
        pieChart.invalidate();
        //Load data ở đây
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        txtTongTienCuoc.setText(format.format(tongCuocPhi));
        txtTongTienThuHo.setText(format.format(tongThuHo));
        tongDon.setText(soLuongDongHang+"");
        daTaoDon.setText(daTaoDonHang+"");
        daXacNhanDon.setText(daXacNhan+"");
        daTuChoiDon.setText(daTuChoi+"");
        daGiaoNhanVienDiLayHang.setText(daGiaoNhanVienDiLay+"");
        daGiaoNhanVienDiPhatHang.setText(daGiaoNhanVienDiPhat+"");
        nhanVienDangDiLayHang.setText(daGiaoNhanVienDiLay+"");
        DaLayHang.setText(daLayHang+"");
        nhanVienDangDiGiaoHang.setText(daXacNhanGiaoHang+"");
        DaGiaoHang.setText(daGiaoHang+"");
        txtdahuy.setText(dahuy+"");
        txtgiaohangkhongthangcong.setText(giaoKhongThanhCong+"");
    }
    private void setConTrol() {
        pieChart = findViewById(R.id.piechartThongKe);
        tongDon = findViewById(R.id.tongSoDon);
        daTaoDon = findViewById(R.id.daTaoDon);
        daXacNhanDon = findViewById(R.id.daXacNhan);
        daTuChoiDon = findViewById(R.id.daTuChoi);
        daGiaoNhanVienDiLayHang = findViewById(R.id.daGiaoNhanVienDiLayHang);
        daGiaoNhanVienDiPhatHang = findViewById(R.id.daGiaoNhanVienDiPhat);
        nhanVienDangDiLayHang = findViewById(R.id.nhanVienDangDiLayHang);
        txtTongTienCuoc = findViewById(R.id.txtTongTienCuoc);
        txtTongTienThuHo = findViewById(R.id.txtTongTienThuHo);
        DaLayHang = findViewById(R.id.daLayHang);
        nhanVienDangDiGiaoHang = findViewById(R.id.nhanVienDangDiGiaoHang);
        DaGiaoHang = findViewById(R.id.daGiaoHang);
        txtdahuy = findViewById(R.id.dahuy);
        txtgiaohangkhongthangcong = findViewById(R.id.giaokhongthanhcong);
    }
}