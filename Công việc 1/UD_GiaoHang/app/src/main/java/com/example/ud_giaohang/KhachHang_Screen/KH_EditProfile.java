package com.example.ud_giaohang.KhachHang_Screen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.kalert.KAlertDialog;
import com.example.ud_giaohang.Admin_Screen.AD_Profile;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class KH_EditProfile extends AppCompatActivity {

    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri uriImage ;

    EditText etTen, etSo, etDiaChi, etEmail, etNgaySinh, etCMND;
    CircleImageView civImage;
    Button btnCapNhat;
    User nguoiDungHienTai = new User();
    ImageButton ibback;
    KAlertDialog kAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
        storageRef = storage.getReference().child("image");
        mDatabase  = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setContentView(R.layout.activity_kh_edit_profile);
        setControl();
        LoadUser();
        setEvent();
    }

    private void setEvent() {
        civImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                civImage.setOnClickListener(new View.OnClickListener() {
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

        etNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date = +dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        etNgaySinh.setText(date);
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

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kAlertDialog = new KAlertDialog(KH_EditProfile.this);
                kAlertDialog.changeAlertType(KAlertDialog.PROGRESS_TYPE);
                kAlertDialog.setTitleText("Loading");
                kAlertDialog.setCancelable(false);
                kAlertDialog.showConfirmButton(false);
                kAlertDialog.show();
                if(KiemTra())
                {
                    nguoiDungHienTai.setHoTen(etTen.getText().toString());
                    nguoiDungHienTai.setCMND(etSo.getText().toString());
                    nguoiDungHienTai.setDiaChi(etDiaChi.getText().toString());
                    nguoiDungHienTai.setNgaySinh(etEmail.getText().toString());
                    nguoiDungHienTai.setSoDT(etNgaySinh.getText().toString());
                    nguoiDungHienTai.setSoDT(etCMND.getText().toString());
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

        ibback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),KH_Profile.class);
                startActivity(intent);
            }
        });

    }

    private boolean KiemTra() {
        boolean res = true;
        if (etTen.getText().toString().equals("")) {
            etTen.setError("Vui lòng nhập tên");
            res = false;
        }

        if (etEmail.getText().toString().equals("")) {
            etEmail.setError("Vui lòng nhập email");
            res = false;
        }else {
            String emailAddress = etEmail.getText().toString().trim();
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                res = true;
            } else {
                etEmail.setError("ĐỊng dạng email không hợp lệ");
                res = false;
            }
        }

        if (etSo.getText().toString().equals("")) {
            etSo.setError("Vui lòng nhập số điện thoại");
            res = false;
        }
        if (etDiaChi.getText().toString().equals("")) {
            etDiaChi.setError("Vui lòng nhập địa chỉ");
        }
        if (etNgaySinh.getText().toString().equals("")) {
            etNgaySinh.setError("Vui lòng chọn ngày sinh");
        }
        if (etCMND.getText().toString().equals("")) {
            etCMND.setError("Vui lòng nhập CMND");
        }
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            if (uriImage!=null)
            {   civImage.setImageURI(uriImage);
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
                etTen.setText( nguoiDungHienTai.getHoTen().toString());
                etSo.setText( nguoiDungHienTai.getSoDT().toString());
                etDiaChi.setText( nguoiDungHienTai.getDiaChi().toString());
                etEmail.setText( nguoiDungHienTai.getEmail().toString());
                etNgaySinh.setText( nguoiDungHienTai.getNgaySinh().toString());
                etCMND.setText( nguoiDungHienTai.getCMND().toString());
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
        etTen = findViewById(R.id.et_tennguoidung);
        etSo = findViewById(R.id.et_sdt);
        etDiaChi = findViewById(R.id.et_diachi);
        etEmail = findViewById(R.id.et_email);
        etNgaySinh = findViewById(R.id.et_ngaysinh);
        etCMND = findViewById(R.id.et_cmnd);
        btnCapNhat = findViewById(R.id.btn_capnhattk);
        civImage = findViewById(R.id.profile_image);
        ibback = findViewById(R.id.ib_backprofile);
    }
}
