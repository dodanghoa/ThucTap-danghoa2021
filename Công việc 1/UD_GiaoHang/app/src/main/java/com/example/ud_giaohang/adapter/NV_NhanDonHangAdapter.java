package com.example.ud_giaohang.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ud_giaohang.NhanVien_Screen.NV_ChiTietDonHang;
import com.example.ud_giaohang.NhanVien_Screen.NV_Home;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NV_NhanDonHangAdapter extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<DonHang> data;
    DatabaseReference mDatabase;
    FirebaseAuth auth;

    public NV_NhanDonHangAdapter(@NonNull Context context, int resource, ArrayList<DonHang> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        LinearLayout lnHeader = convertView.findViewById(R.id.lnHeader);
        TextView tvMaDonHang = convertView.findViewById(R.id.tvMaDonHang);
        TextView tvTrangThai = convertView.findViewById(R.id.tvTrangThai);
        TextView tvCuocPhi = convertView.findViewById(R.id.tvTienCuoc);
        TextView tvTienThuHo = convertView.findViewById(R.id.tvTienThuHo);
        TextView tvDiaChiGui = convertView.findViewById(R.id.tvDiaChiGui);
        TextView tvDiaChiNhan = convertView.findViewById(R.id.tvDiaChiNhan);
        ImageView ivLogo = convertView.findViewById(R.id.ivLogo);
        Button btnXacNhanDiLay = convertView.findViewById(R.id.btnXacNhanDiLay);
        Button btnXacNhanDaLay = convertView.findViewById(R.id.btnDaLayHang);


        DonHang donHang = data.get(position);
        tvMaDonHang.setText(donHang.getMaDonHang());
        tvTrangThai.setText(donHang.getTrangThaiDonHang());
        tvCuocPhi.setText(donHang.getCuocPhi());
        tvTienThuHo.setText(donHang.getTienThuHo());
        tvDiaChiGui.setText(donHang.getNguoiGui().getDiaChi());
        tvDiaChiNhan.setText(donHang.getDiaChi());
        ivLogo.setImageResource(R.drawable.logo);



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String dataDonHang = gson.toJson(data.get(position));
                Intent intent = new Intent(v.getContext(),NV_ChiTietDonHang.class);
                intent.putExtra("dataDonHang",dataDonHang);
                context.startActivity(intent);
            }
        });




        btnXacNhanDiLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThaiDonHang("Nhân viên đang đi lấy hàng");
                data.set(position,temp);
                mDatabase.child("DonHang").child(temp.getMaDonHang()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(v.getContext(), "Đã lấy hàng thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                notifyDataSetChanged();
            }
        });
        btnXacNhanDaLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThaiDonHang("Đã lấy hàng");
                data.set(position,temp);
                mDatabase.child("DonHang").child(temp.getMaDonHang()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(v.getContext(), "Đã lấy hàng thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                notifyDataSetChanged();
            }
        });
        if(donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi lấy hàng")){
            btnXacNhanDiLay.setVisibility(View.VISIBLE);
        }
        if(donHang.getTrangThaiDonHang().equals("Nhân viên đang đi lấy hàng")){
            btnXacNhanDaLay.setVisibility(View.VISIBLE);
        }
        if(donHang.getTrangThaiDonHang().equals("Đã lấy hàng")){
            tvMaDonHang.setTextColor(Color.WHITE);
            lnHeader.setBackgroundColor(Color.parseColor("#FF18855C"));
            tvTrangThai.setTextColor(Color.WHITE);
            notifyDataSetChanged();
        }
        return convertView;
    }
}
