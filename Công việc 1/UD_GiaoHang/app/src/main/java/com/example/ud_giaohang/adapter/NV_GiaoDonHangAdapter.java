package com.example.ud_giaohang.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ud_giaohang.NhanVien_Screen.NV_ChiTietDonHang;
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
import com.google.gson.Gson;

import java.util.ArrayList;

public class NV_GiaoDonHangAdapter extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<DonHang> data;
    DatabaseReference mDatabase;
    FirebaseAuth auth;

    public NV_GiaoDonHangAdapter(@NonNull Context context, int resource, ArrayList<DonHang> data) {
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
        Button btnDiGiao = convertView.findViewById(R.id.btnXacNhanDiGiao);
        Button btnGiaoThanhCong = convertView.findViewById(R.id.btnXacNhanGiaoThanhCong);
        Button btnHuyDonHang = convertView.findViewById(R.id.btnHuyDonHang);
        LinearLayout lnLyDoHuy = convertView.findViewById(R.id.lnLyDoHuy);

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
                Intent intent = new Intent(v.getContext(), NV_ChiTietDonHang.class);
                intent.putExtra("dataDonHang",dataDonHang);
                context.startActivity(intent);
            }
        });


        btnDiGiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonHang temp = donHang;
                temp.setTrangThaiDonHang("Nhân viên đang đi giao hàng");
                data.set(position, temp);
                mDatabase.child("DonHang").child(temp.getMaDonHang()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Đang đi giao", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                notifyDataSetChanged();
            }
        });
        btnGiaoThanhCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThaiDonHang("Đã giao hàng");
                data.set(position, temp);
                mDatabase.child("DonHang").child(temp.getMaDonHang()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Đã giao hàng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                notifyDataSetChanged();
            }
        });
        btnHuyDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLyDoDiaLog(Gravity.CENTER, donHang,position);
            }
        });

        if (donHang.getTrangThaiDonHang().equals("Đã giao nhân viên đi phát")) {
            btnDiGiao.setVisibility(View.VISIBLE);
        }
        if (donHang.getTrangThaiDonHang().equals("Nhân viên đang đi giao hàng")) {
            btnGiaoThanhCong.setVisibility(View.VISIBLE);
            btnHuyDonHang.setVisibility(View.VISIBLE);
        }
        if(donHang.getTrangThaiDonHang().equals("Giao hàng không thành công")){
            tvMaDonHang.setTextColor(Color.WHITE);
            lnHeader.setBackgroundColor(Color.parseColor("#FF1100"));
            tvTrangThai.setTextColor(Color.WHITE);
            notifyDataSetChanged();
        }

        if (donHang.getTrangThaiDonHang().equals("Đã giao hàng")) {
            tvMaDonHang.setTextColor(Color.WHITE);
            lnHeader.setBackgroundColor(Color.parseColor("#FF18855C"));
            tvTrangThai.setTextColor(Color.WHITE);
            notifyDataSetChanged();
        }

        return convertView;
    }

    private void openLyDoDiaLog(int gravity, DonHang donHang,int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_huydonhang);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }
        EditText edtLyDoHuy = dialog.findViewById(R.id.edtLyDoHuy);
        Button btnGui = dialog.findViewById(R.id.btnGui);
        Button btnHuyGui = dialog.findViewById(R.id.btnHuyGui);

        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThaiDonHang("Giao hàng không thành công");
                temp.setGhiChu(edtLyDoHuy.getText().toString());
                data.set(position, temp);
                mDatabase.child("DonHang").child(temp.getMaDonHang()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                notifyDataSetChanged();
            }
        });

        btnHuyGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
