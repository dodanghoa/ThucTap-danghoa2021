package com.example.ud_giaohang.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ud_giaohang.Admin_Screen.AD_ThongTinTaiKhoan;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaiKhoanAdapter extends ArrayAdapter {
    private FirebaseStorage storage;
    private StorageReference storageRef;
    Context context; int resource;  ArrayList<User> arrayLists;
    public TaiKhoanAdapter(@NonNull Context context, int resource, @NonNull   ArrayList<User> arrayLists) {
        super(context, resource, arrayLists);
        this.context = context;
        this.resource = resource;
        this.arrayLists = arrayLists;
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource,null);
        TextView txtIDTaiKhoan = view.findViewById(R.id.txtIDTaiKhoan);
        TextView txtTenNguoiDung = view.findViewById(R.id.txtTenNguoiDung);
        TextView txtLoaiTaiKhoan = view.findViewById(R.id.txtLoaiTaiKhoan);
        CircleImageView image = view.findViewById(R.id.avatar);
        LinearLayout lnItem = view.findViewById(R.id.lnItem);
        User user = arrayLists.get(position);
        txtIDTaiKhoan.setText("ID: "+ user.getMa().toString());
        txtTenNguoiDung.setText("Họ và tên:"+ user.getHoTen().toString());
        txtLoaiTaiKhoan.setText( user.getViTriCV().toString());
        storageRef = storage.getReference().child("image");
        storageRef.child(user.getMa()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(uri.toString().isEmpty())
                {
                    Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").
                            into(image);
                }
                else {
                    Picasso.get().load(uri.toString()).into(image);
                }
            }
        });
        return view;
    }
}
