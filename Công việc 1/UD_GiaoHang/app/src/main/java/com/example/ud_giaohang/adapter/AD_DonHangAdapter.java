package com.example.ud_giaohang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ud_giaohang.Admin_Screen.AD_XemChiTietDonHang;
import com.example.ud_giaohang.KhachHang_Screen.KH_TaoDonHang;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AD_DonHangAdapter extends ArrayAdapter {
    ArrayList<User> users;
    DatabaseReference mDatabase;
    Context context;
    int resource;
    ArrayList<DonHang> donHangs;

    public AD_DonHangAdapter(@NonNull Context context, int resource, ArrayList<DonHang> donHangs) {
        super(context, resource, donHangs);
        this.context = context;
        this.resource = resource;
        this.donHangs = donHangs;
        users = new ArrayList<>();
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
        LinearLayout lnHeader = convertView.findViewById(R.id.lnHeader);
        TextView maDonHang = convertView.findViewById(R.id.tv_madonhang);
        TextView tenNguoiGui = convertView.findViewById(R.id.tv_tennguoigui);
        TextView soDienThoaiNguoiGui = convertView.findViewById(R.id.tv_sodienthoainguoigui);
        TextView tenNguoiNhan = convertView.findViewById(R.id.tv_tennguoinhan);
        TextView soDienThoai = convertView.findViewById(R.id.tv_sodienthoai);
        TextView diaChi = convertView.findViewById(R.id.tv_diachi);
        TextView tongCuoc = convertView.findViewById(R.id.tv_tongcuoc);
        TextView tenHang = convertView.findViewById(R.id.tv_tenhanghoa);
        TextView ghiChu = convertView.findViewById(R.id.tv_ghichu);
        TextView trangThai = convertView.findViewById(R.id.tv_trangthai);
        Spinner spNhanVienDiLay = convertView.findViewById(R.id.spNhanVienDiLay);
        Spinner spNhanVienDiPhat = convertView.findViewById(R.id.spNhanvienDiPhat);
        Button xacNhan = convertView.findViewById(R.id.btn_XacNhan);
        Button tuChoi = convertView.findViewById(R.id.btn_TuChoi);
        Button giaoNhanVienLay = convertView.findViewById(R.id.btn_GiaoNhanVienDiLay);
        Button btnXacNhanDiLay = convertView.findViewById(R.id.btnXacNhanDiLay);
        Button giaoNhanVienPhat = convertView.findViewById(R.id.btn_GiaoNhanVienDiPhat);
        Button btnXacNhanDiPhat = convertView.findViewById(R.id.btnXacNhanDiPhat);
        //L???y v?? set gi?? tr??? cho ????n h??ng
        DonHang donHang = donHangs.get(position);
        maDonHang.setText(donHang.getMaDonHang());
        tenNguoiGui.setText("T??n ng?????i G???i : "+donHang.getNguoiGui().getHoTen());
        soDienThoaiNguoiGui.setText("S??? ??i???n tho???i :  "+donHang.getNguoiGui().getSoDT());
        tenNguoiNhan.setText("T??n ng?????i nh???n : "+donHang.getHoTen());
        soDienThoai.setText("S??? ??i???n tho???i :  "+donHang.getSoDT());
        diaChi.setText("?????a ch??? :  "+donHang.getDiaChi());
        tongCuoc.setText("T???ng c?????c :  "+donHang.getTongCuoc());
        tenHang.setText("T??n ????n h??ng :  "+donHang.getTenHang());
        ghiChu.setText("Ghi ch?? : "+donHang.getGhiChu());
        trangThai.setText(donHang.getTrangThaiDonHang());
        //S??? Ki???n

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String data = gson.toJson(donHangs.get(position));
                Intent intent = new Intent(v.getContext(), AD_XemChiTietDonHang.class);
                intent.putExtra("dataDonHang",data);
                context.startActivity(intent);
            }
        });


        xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThaiDonHang("???? x??c nh???n");
                donHangs.set(position,temp);
                mDatabase.child("DonHang").child(temp.getMaDonHang()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(v.getContext(), "???? x??c nh???n ????n H??ng"+donHangs.size()+"position: "+position, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                notifyDataSetChanged();
            }
        });
        tuChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThaiDonHang("???? t??? ch???i");
                donHangs.set(position,temp);
                mDatabase.child("DonHang").child(temp.getMaDonHang()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(v.getContext(), "???? T??? Ch???i ????n H??ng"+donHangs.size()+"position: "+position, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                notifyDataSetChanged();
            }
        });

        giaoNhanVienLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spNhanVienDiLay.setVisibility(View.VISIBLE);
                btnXacNhanDiLay.setVisibility(View.VISIBLE);
                LoadData();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                AD_NhanVienSpinnerAdapter adapter = new AD_NhanVienSpinnerAdapter(getContext(),R.layout.custom_danh_sach_tai_khoan,users);

                                spNhanVienDiLay.setAdapter(adapter);
                            }
                        },
                        300);
            }
        });
        giaoNhanVienPhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spNhanVienDiPhat.setVisibility(View.VISIBLE);
                btnXacNhanDiPhat.setVisibility(View.VISIBLE);
                LoadData();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                AD_NhanVienSpinnerAdapter adapter = new AD_NhanVienSpinnerAdapter(getContext(),R.layout.custom_danh_sach_tai_khoan,users);
                                spNhanVienDiPhat.setAdapter(adapter);
                            }
                        },
                        300);
            }
        });

        btnXacNhanDiLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThaiDonHang("???? giao nh??n vi??n ??i l???y h??ng");
                temp.setNguoiLayHang(users.get(spNhanVienDiLay.getSelectedItemPosition()));
                donHangs.set(position,temp);
                mDatabase.child("DonHang").child(temp.getMaDonHang()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(v.getContext(), "???? giao nh??n vi??n ??i l???y h??ng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                notifyDataSetChanged();
            }
        });
        btnXacNhanDiPhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonHang temp = donHang;
                temp.setTrangThaiDonHang("???? giao nh??n vi??n ??i ph??t");
                temp.setNguoiGiaoHang(users.get(spNhanVienDiPhat.getSelectedItemPosition()));
                donHangs.set(position,temp);
                mDatabase.child("DonHang").child(temp.getMaDonHang()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(v.getContext(), "???? giao nh??n vi??n ??i ph??t h??ng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                notifyDataSetChanged();
            }
        });
        //Tr???ng th??i v?? m??u ch???

        //n???u tr???ng th??i l?? ???? t???o ????n h??ng ho???c ???? t??? ch???i ????n h??ng th?? ???n 2 n??t d?????i
        if (donHang.getTrangThaiDonHang().equals("???? t???o ????n h??ng"))
        {
            xacNhan.setVisibility(View.VISIBLE);
            tuChoi.setVisibility(View.VISIBLE);
        }
        //N???u tr???ng th??i l?? ???? x??c nh???n th?? ???n 2 n??t d?????i
        if (donHang.getTrangThaiDonHang().equals("???? x??c nh???n"))
        {
            giaoNhanVienLay.setVisibility(View.VISIBLE);
        }
        if (donHang.getTrangThaiDonHang().equals("???? l???y h??ng"))
        {

            giaoNhanVienPhat.setVisibility(View.VISIBLE);

        }
        if(donHang.getTrangThaiDonHang().equals("???? x??c nh???n"))
        {
            maDonHang.setTextColor(Color.WHITE);
            lnHeader.setBackgroundColor(Color.parseColor("#FF18855C"));
            trangThai.setTextColor(Color.WHITE);
        }
        if(donHang.getTrangThaiDonHang().equals("???? l???y h??ng"))
        {
            maDonHang.setTextColor(Color.WHITE);
            lnHeader.setBackgroundColor(Color.parseColor("#FF18855C"));
            trangThai.setTextColor(Color.WHITE);
        }
        if(donHang.getTrangThaiDonHang().equals("???? t??? ch???i")&&donHang.getTrangThaiDonHang().equals("???? h???y"))
        {
            maDonHang.setTextColor(Color.WHITE);
            lnHeader.setBackgroundColor(Color.parseColor("#FFBF4141"));
            trangThai.setTextColor(Color.WHITE);
        }
        if(donHang.getTrangThaiDonHang().equals("???? giao h??ng"))
        {
            maDonHang.setTextColor(Color.WHITE);
            lnHeader.setBackgroundColor(Color.parseColor("#FF18855C"));
            trangThai.setTextColor(Color.WHITE);
        }
        if(donHang.getTrangThaiDonHang().equals("???? giao h??ng kh??ng th??nh c??ng"))
        {
            maDonHang.setTextColor(Color.WHITE);
            lnHeader.setBackgroundColor(Color.parseColor("#FF18855C"));
            trangThai.setTextColor(Color.WHITE);
        }
        if(donHang.getTrangThaiDonHang().equals("???? giao nh??n vi??n ??i l???y h??ng"))
        {
            maDonHang.setTextColor(Color.WHITE);
            lnHeader.setBackgroundColor(Color.parseColor("#C9F39103"));
            trangThai.setTextColor(Color.WHITE);
        }
        if(donHang.getTrangThaiDonHang().equals("???? giao nh??n vi??n ??i ph??t"))
        {
            maDonHang.setTextColor(Color.WHITE);
            lnHeader.setBackgroundColor(Color.parseColor("#C9F39103"));
            trangThai.setTextColor(Color.WHITE);
        }
        return convertView;
    }
    public void LoadData() {
        DatabaseReference userChilds = mDatabase.child("user");
        userChilds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> temp = new ArrayList<>();
//                Toast.makeText(Admin_DanhSachTaiKhoan.this,snapshot.getChildrenCount()+"", Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (user.getViTriCV().equals("Nh??n vi??n"))
                    {
                        temp.add(user);
                    }
                }
                users = temp;
                Toast.makeText(context, users.size()+"", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
