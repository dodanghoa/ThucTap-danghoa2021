package com.example.ud_giaohang.Admin_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AD_Profile extends AppCompatActivity {
    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri uriImage ;
    CircleImageView civAvatar;
    CircleImageView editAvatar;
    EditText et_hoten,et_sdt,et_email,et_diachi,et_ngaysinh,et_cmnd;
    Button btn_capnhattk;
    BottomNavigationView bottomNavigationView ;
    User nguoiDungHienTai = new User();
    KAlertDialog kAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
        storageRef = storage.getReference().child("image");
        mDatabase  = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
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
                        startActivity(new Intent(getApplicationContext(), AD_Home.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_profile:
                        startActivity(new Intent(getApplicationContext(), AD_Profile.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_Search:
                        startActivity(new Intent(getApplicationContext(), AD_Search.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.mn_Setting:
                        startActivity(new Intent(getApplicationContext(), AD_Setting.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,AD_Home.class);
        startActivity(intent);
        finish();
    }

    private void setEvent() {
        editAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 2);
                    }
                });
            }
        });
        et_ngaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date = +dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        et_ngaysinh.setText(date);
                    }
                };
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        dateSetListener,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");
            }
        });
        btn_capnhattk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kAlertDialog = new KAlertDialog(AD_Profile.this);
                kAlertDialog.changeAlertType(KAlertDialog.PROGRESS_TYPE);
                kAlertDialog.setTitleText("Loading");
                kAlertDialog.setCancelable(false);
                kAlertDialog.showConfirmButton(false);
                kAlertDialog.show();
                if(KiemTra())
                {
                    nguoiDungHienTai.setHoTen(et_hoten.getText().toString());
                    nguoiDungHienTai.setCMND(et_cmnd.getText().toString());
                    nguoiDungHienTai.setDiaChi(et_diachi.getText().toString());
                    nguoiDungHienTai.setNgaySinh(et_ngaysinh.getText().toString());
                    nguoiDungHienTai.setSoDT(et_sdt.getText().toString());
                    mDatabase.child("user").child(auth.getUid()).setValue(nguoiDungHienTai).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                kAlertDialog.setTitleText("Thông tin đã được cập nhật");
                                kAlertDialog.setCancelable(true);
                                kAlertDialog.showConfirmButton(false);
                                kAlertDialog.show();
                            }
                        }
                    });
                }
            }
        });

    }
    private boolean KiemTra() {
        boolean res = true;
        if (et_hoten.getText().toString().equals("")) {
            et_hoten.setError("Vui Lòng Nhập Tên Của Bạn");
            res = false;
        }

        if (et_email.getText().toString().equals("")) {
            et_email.setError("Vui Lòng Nhập Email");
            res = false;
        }else {
            String emailAddress = et_email.getText().toString().trim();
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                res = true;
            } else {
                et_email.setError("ĐỊng dạng email không hợp lệ");
                res = false;
            }
        }

        if (et_diachi.getText().toString().equals("")) {
            et_diachi.setError("Vui Lòng Nhập Địa Chỉ");
            res = false;
        }
        if (et_sdt.getText().toString().equals("")) {
            et_sdt.setError("Vui Lòng Nhập Số Điện Thoại");
        }
        return res;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            if (uriImage!=null)
            {   civAvatar.setImageURI(uriImage);
                uploadPicture();
            }
        }
    }
    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();
        final String key = auth.getUid().toString();
        StorageReference mountainsRef = storageRef.child(key);
        mountainsRef.putFile(uriImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
        LoadUser();
    }


    private void LoadUser() {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    nguoiDungHienTai = dataSnapshot.getValue(User.class);
                    et_hoten.setText( nguoiDungHienTai.getHoTen().toString());
                    et_diachi.setText( nguoiDungHienTai.getDiaChi().toString());
                    et_cmnd.setText( nguoiDungHienTai.getCMND().toString());
                    et_ngaysinh.setText( nguoiDungHienTai.getNgaySinh().toString());
                    et_sdt.setText( nguoiDungHienTai.getSoDT().toString());
                    et_email.setText( nguoiDungHienTai.getEmail().toString());
                    // ..
                    storageRef.child(auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri.toString()).into(civAvatar);
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
        et_hoten = findViewById(R.id.et_hoten);
        et_cmnd = findViewById(R.id.et_cmnd);
        et_email = findViewById(R.id.et_email);
        et_sdt = findViewById(R.id.et_sdt);
        et_ngaysinh = findViewById(R.id.et_ngaysinh);
        et_cmnd = findViewById(R.id.et_cmnd);
        et_diachi = findViewById(R.id.et_diachi);
        btn_capnhattk = findViewById(R.id.btn_capnhattk);
        editAvatar = findViewById(R.id.iv_camera);
        civAvatar = findViewById(R.id.profilePic);

    }
}