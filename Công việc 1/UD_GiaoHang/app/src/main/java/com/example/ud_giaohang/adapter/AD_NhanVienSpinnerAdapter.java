package com.example.ud_giaohang.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ud_giaohang.Admin_Screen.AD_DanhSachTaiKhoan;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AD_NhanVienSpinnerAdapter extends ArrayAdapter {
     Context context;
     int resource;
    DatabaseReference mDatabase;
    ArrayList<User> users;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    public AD_NhanVienSpinnerAdapter(@NonNull Context context, int resource,@NonNull ArrayList<User> users) {
        super(context, resource, users);
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        this.context = context;
        this.resource = resource;
        this.users = users;
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initVeiw(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable  View convertView, @NonNull  ViewGroup parent) {
        return initVeiw(position, convertView, parent);
    }
    private View initVeiw(int position,   View convertView,   ViewGroup parent)
    {
        convertView = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView txtIDTaiKhoan = convertView.findViewById(R.id.txtIDTaiKhoan);
        TextView txtTenNguoiDung = convertView.findViewById(R.id.txtTenNguoiDung);
        TextView txtLoaiTaiKhoan = convertView.findViewById(R.id.txtLoaiTaiKhoan);
        CircleImageView image = convertView.findViewById(R.id.avatar);
        User user = users.get(position);
        txtIDTaiKhoan.setText("ID: "+ user.getMa().toString());
        txtTenNguoiDung.setText("Họ và tên:"+ user.getHoTen().toString());
        txtLoaiTaiKhoan.setText("Họ và tên:"+ user.getViTriCV().toString());
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
        return convertView;
    }

}
