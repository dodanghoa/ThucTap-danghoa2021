package com.example.ud_giaohang.KhachHang_Screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ud_giaohang.Admin_Screen.AD_Home;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class KH_Profile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView ;
    TextView tvsua, tvTen, tvSo, tvDiaChi, tvEmail, tvNgaySinh, tvCMND;
    CircleImageView civImage;

    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri uriImage ;
    User user;
    User nguoiDungHienTai = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
        storageRef = storage.getReference().child("image");
        setContentView(R.layout.activity_kh_profile);
        setControl();
        LoadUser();
        setEvent();
        bottomNavigationView.setSelectedItemId(R.id.mn_profile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.mn_home:
                        startActivity(new Intent(getApplicationContext(), KH_Home.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_profile:
                        startActivity(new Intent(getApplicationContext(), KH_Profile.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_About:
                        startActivity(new Intent(getApplicationContext(), KH_About.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_Setting:
                        startActivity(new Intent(getApplicationContext(), KH_Setting.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });

    }

    private void setEvent() {
        tvsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),KH_EditProfile.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, KH_Home.class);
        startActivity(intent);
        finish();
    }
    private void LoadUser() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                nguoiDungHienTai = dataSnapshot.getValue(User.class);
                tvTen.setText( nguoiDungHienTai.getHoTen().toString());
                tvSo.setText( nguoiDungHienTai.getSoDT().toString());
                tvDiaChi.setText( nguoiDungHienTai.getDiaChi().toString());
                tvEmail.setText( nguoiDungHienTai.getEmail().toString());
                tvNgaySinh.setText( nguoiDungHienTai.getNgaySinh().toString());
                tvCMND.setText( nguoiDungHienTai.getCMND().toString());
                // ..
                storageRef.child(auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).into(civImage);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        mDatabase.child("user").child(auth.getUid()).addValueEventListener(postListener);
    }

    private void setControl() {

        bottomNavigationView = findViewById((R.id.bottom_nav));
        tvsua = findViewById(R.id.tv_sua);
        tvTen = findViewById(R.id.tv_tennguoidung);
        tvSo = findViewById(R.id.tv_sodienthoai);
        tvDiaChi = findViewById(R.id.tv_diachi);
        tvEmail = findViewById(R.id.tv_email);
        tvNgaySinh = findViewById(R.id.tv_ngaysinh);
        tvCMND = findViewById(R.id.tv_cmnd);
        civImage = findViewById(R.id.profile_image);
    }
}