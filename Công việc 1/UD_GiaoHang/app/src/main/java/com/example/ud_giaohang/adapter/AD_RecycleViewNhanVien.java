package com.example.ud_giaohang.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class AD_RecycleViewNhanVien extends RecyclerView.Adapter<AD_RecycleViewNhanVien.UserVeiwHolder>{
    ArrayList<User> users;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    Context context ;
    public  void  setData( ArrayList<User>data)
    {
        this.users = data;
        notifyDataSetChanged();
    }
    public AD_RecycleViewNhanVien(Context con) {
        this.context = con;

    }
    @NonNull

    @Override
    public UserVeiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_admin_item_search_taikhoan,parent,false);
        return new UserVeiwHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  AD_RecycleViewNhanVien.UserVeiwHolder holder, int position) {
        User  user = users.get(position);
        if (user == null)
        {
            return;
        }
        holder.txtIDTaiKhoan.setText(user.getMa());
        holder.txtTenNguoiDung.setText(user.getHoTen());
        holder.txtLoaiTaiKhoan.setText(user.getViTriCV());
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
        storageRef = storage.getReference().child("image");
        storageRef.child(user.getMa()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(uri.toString().isEmpty())
                {
                    Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").
                            into(holder.image);
                }
                else {
                    Picasso.get().load(uri.toString()).into(holder.image);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String data = gson.toJson(users.get(position));
                Intent intent = new Intent(context, AD_ThongTinTaiKhoan.class);
                intent.putExtra("dataUser",data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class  UserVeiwHolder extends RecyclerView.ViewHolder{
        TextView txtIDTaiKhoan,txtTenNguoiDung,txtLoaiTaiKhoan;
        CircleImageView image;
        public UserVeiwHolder(@NonNull View itemView) {
            super(itemView);
             txtIDTaiKhoan = itemView.findViewById(R.id.txtIDTaiKhoan);
             txtTenNguoiDung = itemView.findViewById(R.id.txtTenNguoiDung);
             txtLoaiTaiKhoan = itemView.findViewById(R.id.txtLoaiTaiKhoan);
             image = itemView.findViewById(R.id.avatar);
        }
    }
}
