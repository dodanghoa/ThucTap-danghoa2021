package com.example.ud_giaohang.KhachHang_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class KH_ThongKe extends AppCompatActivity {
    int soLuongDongHang =0 ;
    int daTaoDonHang=0;
    int choLayHang=0;
    int daTuChoi=0;
    int daLayHang=0;
    int daGiaoHang=0;
    int giaoThanhCong=0;
    int tongCuocPhi=0;
    int tongThuHo=0;
    int daHuy=0;
    private PieChart pieChart;
    ArrayList<DonHang> donHangs = new ArrayList<>();
    DatabaseReference mDatabase;
    DonHang donHang;
    private FirebaseAuth auth;
    TextView tvTongDon, tvTongTienThuHo, tvTongCuoc, tvdataodon, tvtuchoidon, tvcholay, tvdalay, tvdanggiao, tvgiaothanhcong, tvdahuy;
    ImageButton ibback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        donHang = new DonHang();
        setContentView(R.layout.activity_kh_thong_ke);
        setConTrol();
        LayDonHang();
        setupPieCharts();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        loadPieChart();
                    }
                },
                500);

        ibback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),KH_Home.class);
                startActivity(intent);
            }
        });
    }

    private void LayDonHang() {
        DatabaseReference reference = mDatabase.child("DonHang");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    DonHang donHang = ds.getValue(DonHang.class);
                    if(donHang.getNguoiGui().getMa().equals(auth.getUid())){
                        if(!donHang.getTrangThaiDonHang().equals("Đã hủy") ){
                            int cuocPhi = Integer.parseInt((donHang.getCuocPhi()));
                            tongCuocPhi +=cuocPhi;
                            int thuHo = Integer.parseInt((donHang.getTienThuHo()));
                            tongThuHo +=thuHo;
                            donHangs.add(donHang);
                            if(donHang.getTrangThaiDonHang().equals("Đã tạo đơn hàng"))
                            {
                                daTaoDonHang++;
                            }
                            if(donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi lấy hàng"))
                            {
                                choLayHang++;
                            }
                            if(donHang.getTrangThaiDonHang().equals("Đã từ chối"))
                            {
                                daTuChoi++;
                            }
                            if(donHang.getTrangThaiDonHang().equals("Đã lấy hàng"))
                            {
                                daLayHang++;
                            }
                            if(donHang.getTrangThaiDonHang().equals("Nhân viên đang đi giao hàng"))
                            {
                                daGiaoHang++;
                            }
                            if(donHang.getTrangThaiDonHang().equals("Đã giao hàng"))
                            {
                                giaoThanhCong++;
                            }
                        }
                        tvTongCuoc.setText((donHang.getCuocPhi()));
                    }
                    if(donHang.getTrangThaiDonHang().equals("Đã hủy"))
                    {
                        daHuy++;
                    }
                    soLuongDongHang = donHangs.size();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(valueEventListener);
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
        l.setTextSize(10);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
        pieChart.animateXY(2000, 5000);
    }

    private void loadPieChart(){
        //Khởi tạo thông số
        ArrayList<PieEntry> entries = new ArrayList<>();

        if (daTaoDonHang!=0)
        {
            entries.add(new PieEntry((float) daTaoDonHang/soLuongDongHang,"Đã tạo đơn hàng"));
        }
        if (choLayHang!=0)
        {
            entries.add(new PieEntry((float)choLayHang/soLuongDongHang,"Chờ lấy hàng"));
        }
        if (daTuChoi!=0)
        {
            entries.add(new PieEntry((float) daTuChoi/soLuongDongHang,"Đã từ chối hàng"));
        }
        if (daLayHang!=0)
        {
            entries.add(new PieEntry((float)daLayHang/soLuongDongHang,"Đã lấy hàng"));
        }

        if (daGiaoHang!=0)
        {
            entries.add(new PieEntry((float)daGiaoHang/soLuongDongHang,"Đã giao hàng"));
        }

        if (giaoThanhCong!=0)
        {
            entries.add(new PieEntry((float)giaoThanhCong/soLuongDongHang,"Giao thành công"));
        }

        if (daHuy!=0)
        {
            entries.add(new PieEntry((float)daHuy/soLuongDongHang,"Đã hủy"));
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
        tvTongCuoc.setText(format.format(tongCuocPhi));
        tvTongTienThuHo.setText(format.format(tongThuHo));
        tvTongDon.setText(soLuongDongHang+"");
        tvdataodon.setText(daTaoDonHang+"");
        tvtuchoidon.setText(daTuChoi+"");
        tvcholay.setText(choLayHang+"");
        tvdalay.setText(daLayHang+"");
        tvdanggiao.setText(daGiaoHang+"");
        tvgiaothanhcong.setText(giaoThanhCong+"");
        tvdahuy.setText(daHuy+"");
    }

    private void setConTrol() {
        pieChart = findViewById(R.id.piechartThongKe);
        tvTongDon = findViewById(R.id.tv_tongSoDon);
        tvTongCuoc = findViewById(R.id.tv_TongTienCuoc);
        tvTongTienThuHo = findViewById(R.id.tv_tongTienThuHo);
        tvdataodon = findViewById(R.id.tv_daTaoDon);
        tvcholay = findViewById(R.id.tv_dangChoLay);
        tvdalay = findViewById(R.id.tv_daLay);
        tvdanggiao = findViewById(R.id.tv_dangGiao);
        tvgiaothanhcong = findViewById(R.id.tv_giaoThanhCong);
        tvtuchoidon = findViewById(R.id.tv_tuchoi);
        ibback = findViewById(R.id.ibBackKH);
        tvdahuy = findViewById(R.id.tv_dahuy);
    }
}