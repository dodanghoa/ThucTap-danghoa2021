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
                    if(donHang.getTrangThaiDonHang().equals("Giao h??ng kh??ng th??nh c??ng"))
                    {
                        giaoKhongThanhCong++;
                    }
                    if (donHang.getTrangThaiDonHang().equals("???? h???y")) {
                        dahuy++;
                    } else {
                        if(donHang.getTrangThaiDonHang().equals("???? t???o ????n h??ng"))
                        {
                            daTaoDonHang++;

                        }
                        if(donHang.getTrangThaiDonHang().equals("???? x??c nh???n"))
                        {
                            daXacNhan++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("???? t??? ch???i"))
                        {
                            daTuChoi++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("???? giao nh??n vi??n ??i l???y h??ng"))
                        {
                            daGiaoNhanVienDiLay++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("???? giao nh??n vi??n ??i ph??t"))
                        {
                            daGiaoNhanVienDiPhat++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("Nh??n vi??n ??ang ??i l???y h??ng"))
                        {
                            daXacNhanLayhang++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("???? l???y h??ng"))
                        {
                            daLayHang++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("Nh??n vi??n ??ang ??i giao h??ng"))
                        {
                            daXacNhanGiaoHang++;
                        }
                        if(donHang.getTrangThaiDonHang().equals("???? giao h??ng"))
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

        //Kh???i t???o th??ng s???
        ArrayList<PieEntry> entries = new ArrayList<>();

        if (daTaoDonHang!=0)
        {
            entries.add(new PieEntry((float) daTaoDonHang/soLuongDongHang,"????n h??ng ch??? x??c nh???n"));
        }

        if (daXacNhan!=0)
        {
            entries.add(new PieEntry((float)daXacNhan/soLuongDongHang,"????n h??ng ???? x??c nh???n"));
        }
        if (daGiaoNhanVienDiLay!=0)
        {
            entries.add(new PieEntry((float)daGiaoNhanVienDiLay/soLuongDongHang,"???? giao nh??n vi??n ??i l???y"));
        }

        if (daXacNhanLayhang!=0)
        {
            entries.add(new PieEntry((float)daXacNhanLayhang/soLuongDongHang,"Nh??n vi??n ??ang giao h??ng"));
        }

        if (daTuChoi!=0)
        {
            entries.add(new PieEntry((float)daTuChoi/soLuongDongHang,"???? t??? ch???i"));
        }

        if (daLayHang!=0)
        {
            entries.add(new PieEntry((float)daLayHang/soLuongDongHang,"???? l??u kho ch??? v???n chuy???n"));
        }

        if (daGiaoNhanVienDiPhat!=0)
        {
            entries.add(new PieEntry((float)daGiaoNhanVienDiPhat/soLuongDongHang,"???? giao nh??n vi??n ??i ph??t h??ng"));
        }

        if (daXacNhanGiaoHang!=0)
        {
            entries.add(new PieEntry((float)daXacNhanGiaoHang/soLuongDongHang,"Nh??n vi??n ??ang giao h??ng "));
        }

        if (daGiaoHang!=0)
        {
            entries.add(new PieEntry((float)daGiaoHang/soLuongDongHang," Giao h??ng th??nh c??ng"));
        }if (giaoKhongThanhCong!=0)
        {
            entries.add(new PieEntry((float)daGiaoHang/soLuongDongHang," Giao h??ng kh??ng th??nh c??ng"));
        }if (dahuy!=0)
        {
            entries.add(new PieEntry((float)daGiaoHang/soLuongDongHang," ???? h???y"));
        }
        //M??u m?? hoa l?? h???
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
        //Load data ??? ????y
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