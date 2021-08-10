package com.example.ud_giaohang.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ud_giaohang.Admin_Screen.AD_XemChiTietDonHang;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AD_RecycleVeiwDonHang extends RecyclerView.Adapter<AD_RecycleVeiwDonHang.DonHangVeiwHolder> {
    ArrayList<DonHang> donHangs;
    private FirebaseStorage storage;
    Context context;
    private StorageReference storageRef;

    public void setData(ArrayList<DonHang> data) {
        this.donHangs = data;
        notifyDataSetChanged();
    }
    public   AD_RecycleVeiwDonHang(Context context)
    {
        this.context = context;
    }
    @NonNull

    @Override
    public DonHangVeiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_donhang_search_admin, parent, false);
        return new DonHangVeiwHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AD_RecycleVeiwDonHang.DonHangVeiwHolder holder, int position) {
        DonHang donHang = donHangs.get(position);
        holder.maDonHang.setText(donHang.getMaDonHang());
        holder.tenNguoiGui.setText("Tên người Gửi : "+donHang.getNguoiGui().getHoTen());
        holder.soDienThoaiNguoiGui.setText("Số điện thoại :  "+donHang.getNguoiGui().getSoDT());
        holder.tenNguoiNhan.setText("Tên người nhận : "+donHang.getHoTen());
        holder.soDienThoai.setText("Số điện thoại :  "+donHang.getSoDT());
        holder.diaChi.setText("Địa chỉ :  "+donHang.getDiaChi());
        holder.tongCuoc.setText("Tổng cước :  "+donHang.getTongCuoc());
        holder.tenHang.setText("Tên đơn hàng :  "+donHang.getTenHang());
        holder.ghiChu.setText("Ghi chú : "+donHang.getGhiChu());
        holder.trangThai.setText(donHang.getTrangThaiDonHang());
        holder.btnXemChiTietDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String data = gson.toJson(donHangs.get(position));
                Intent intent = new Intent(context, AD_XemChiTietDonHang.class);
                intent.putExtra("dataDonHang",data);
                context.startActivity(intent);
            }
        });}

    @Override
    public int getItemCount() {
        return donHangs.size();
    }

    public class DonHangVeiwHolder extends RecyclerView.ViewHolder {
        LinearLayout lnHeader;
        TextView maDonHang;
        TextView tenNguoiGui, soDienThoaiNguoiGui ,tenNguoiNhan ,soDienThoai ,diaChi ,tongCuoc ,tenHang,ghiChu,trangThai;
        Button btnXemChiTietDonHang;
        CircleImageView image;
        public DonHangVeiwHolder(@NonNull View itemView) {
            super(itemView);
             lnHeader = itemView.findViewById(R.id.lnHeader);
             maDonHang = itemView.findViewById(R.id.tv_madonhang);
             tenNguoiGui = itemView.findViewById(R.id.tv_tennguoigui);
             soDienThoaiNguoiGui = itemView.findViewById(R.id.tv_sodienthoainguoigui);
             tenNguoiNhan = itemView.findViewById(R.id.tv_tennguoinhan);
             soDienThoai = itemView.findViewById(R.id.tv_sodienthoai);
             diaChi = itemView.findViewById(R.id.tv_diachi);
             tongCuoc = itemView.findViewById(R.id.tv_tongcuoc);
             tenHang = itemView.findViewById(R.id.tv_tenhanghoa);
             ghiChu = itemView.findViewById(R.id.tv_ghichu);
             trangThai = itemView.findViewById(R.id.tv_trangthai);
             btnXemChiTietDonHang = itemView.findViewById(R.id.btn_xemchitiet);
        }
    }


}
