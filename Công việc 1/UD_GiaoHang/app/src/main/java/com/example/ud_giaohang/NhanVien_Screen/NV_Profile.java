package com.example.ud_giaohang.NhanVien_Screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class NV_Profile extends AppCompatActivity {
    EditText edtMa, edtHoTen, edtEmail, edtSDT, edtDiaChi, edtNgaySinh, edtCMND;
    Button btnSua;
    ImageButton ibtnBack;
    BottomNavigationView bottomNavigationView;
    DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    CircleImageView cirAvatar;
    Uri uriImage;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, NV_Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_thongtincanhan);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
        storageRef = storage.getReference().child("image");
        setControl();
        setEvent();

        bottomNavigationView.setSelectedItemId(R.id.mn_profile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_home:
                        startActivity(new Intent(getApplicationContext(), NV_Home.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.mn_profile:
                        startActivity(new Intent(getApplicationContext(), NV_Profile.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.mn_About:
                        startActivity(new Intent(getApplicationContext(), NV_About.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.mn_Setting:
                        startActivity(new Intent(getApplicationContext(), NV_Setting.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    private void setEvent() {

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NV_SuaThongTin.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NV_Home.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
        mDatabase.child("user").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                edtMa.setText(user.getMa().toString());
                edtHoTen.setText(user.getHoTen().toString());
                edtEmail.setText(user.getEmail().toString());
                edtSDT.setText(user.getSoDT().toString());
                edtDiaChi.setText(user.getDiaChi().toString());
                edtNgaySinh.setText(user.getNgaySinh().toString());
                edtCMND.setText(user.getCMND().toString());
                storageRef.child("image").child(auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).into(cirAvatar);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void setControl() {
        bottomNavigationView = findViewById((R.id.bottom_nav));
        btnSua = findViewById(R.id.btnSua);
        ibtnBack = findViewById(R.id.ibtnBack);
        edtMa = findViewById(R.id.edtMa);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtCMND = findViewById(R.id.edtCMND);
        cirAvatar = findViewById(R.id.profile_image);
    }


}