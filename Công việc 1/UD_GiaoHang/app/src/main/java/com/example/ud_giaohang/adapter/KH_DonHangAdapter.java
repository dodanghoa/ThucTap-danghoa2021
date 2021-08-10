package com.example.ud_giaohang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.ud_giaohang.KhachHang_Screen.KH_XemChiTietDonHang;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class KH_DonHangAdapter extends ArrayAdapter {
    private FirebaseStorage storage;
    private StorageReference storageRef;
    Context context;
    int resource;
    ArrayList<DonHang> donHangs;
    DatabaseReference mDatabase;
    public KH_DonHangAdapter(@NonNull Context context, int resource, ArrayList<DonHang> donHangs) {
        super(context, resource, donHangs);
        this.context = context;
        this.resource = resource;
        this.donHangs = donHangs;
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    @Override
    public int getCount() {
        return donHangs.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);


        TextView maDonHang = convertView.findViewById(R.id.tv_madonhang);
        TextView tenNguoiNhan = convertView.findViewById(R.id.tv_tennguoinhan);
        TextView soDienThoai = convertView.findViewById(R.id.tv_sodienthoai);
        TextView diaChi = convertView.findViewById(R.id.tv_diachi);
        TextView tongCuoc = convertView.findViewById(R.id.tv_tongcuoc);
        TextView tenHang = convertView.findViewById(R.id.tv_tenhanghoa);
        TextView ghiChu = convertView.findViewById(R.id.tv_ghichu);
        TextView trangThai = convertView.findViewById(R.id.tv_trangthai);
        Button xemChiTiet = convertView.findViewById(R.id.btn_xemchitiet);
        Button huy = convertView.findViewById(R.id.btn_huy);


        DonHang donHang = donHangs.get(position);
        maDonHang.setText(donHang.getMaDonHang());
        tenNguoiNhan.setText(donHang.getHoTen());
        soDienThoai.setText(donHang.getSoDT());
        diaChi.setText(donHang.getDiaChi());
        tongCuoc.setText(donHang.getTongCuoc());
        tenHang.setText(donHang.getTenHang());
        ghiChu.setText(donHang.getGhiChu());
        trangThai.setText(donHang.getTrangThaiDonHang());

        //nếu trạng thái là đã tạo đơn hàng mới hiện nút hủy
        if (donHang.getTrangThaiDonHang().equals("Đã tạo đơn hàng"))
        {
            huy.setVisibility(View.VISIBLE);
        }
        xemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String data = gson.toJson(donHangs.get(position));
                Intent intent = new Intent(v.getContext(),KH_XemChiTietDonHang.class);
                intent.putExtra("dataDonHang",data);
                context.startActivity(intent);
            }
        });

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donHang.setTrangThaiDonHang("Đã hủy");
                mDatabase.child("DonHang").child(donHang.getMaDonHang()).setValue(donHang);
                notifyDataSetChanged();
            }
        });


        return convertView;
    }
}
