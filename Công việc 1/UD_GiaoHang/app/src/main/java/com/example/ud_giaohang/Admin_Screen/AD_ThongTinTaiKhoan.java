package com.example.ud_giaohang.Admin_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class AD_ThongTinTaiKhoan extends AppCompatActivity {
    FileInputStream serviceAcount;

    KAlertDialog kAlertDialog;
    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    User user;
    EditText edtHoTen, edtEmail, edtSDT, edtNgaySinh, edtCMND, edtDiaChi;
    Spinner spLoaiTK;
    Button btnLuu;
    User nguoiDungHienTai = new User();

    public AD_ThongTinTaiKhoan() throws FileNotFoundException {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setContentView(R.layout.activity_admin_thong_tin_tai_khoan);
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataUser = intent.getStringExtra("dataUser");
            Gson gson = new Gson();
            user = gson.fromJson(dataUser, User.class);
            LoadInfoUser();
        } else {
            GetData();
        }
        setEvent();
    }

    private void LoadInfoUser() {
        edtHoTen.setText(user.getHoTen());
        edtCMND.setText(user.getCMND());
        edtEmail.setText(user.getEmail());
        edtDiaChi.setText(user.getDiaChi());
        edtNgaySinh.setText(user.getNgaySinh());
        edtSDT.setText(user.getSoDT());
        if (user.getViTriCV().equals("Nhân viên")) {
            spLoaiTK.setSelection(0);
        } else if (user.getViTriCV().equals("Quản lý")) {
            spLoaiTK.setSelection(1);
        } else {
            spLoaiTK.setSelection(2);
        }
    }

    private void setEvent() {
        Context context = this;
        edtNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date = +dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        edtNgaySinh.setText(date);
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


        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kAlertDialog = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE);
                kAlertDialog.changeAlertType(KAlertDialog.PROGRESS_TYPE);
                kAlertDialog.setTitleText("Loading");
                kAlertDialog.setCancelable(false);
                kAlertDialog.show();
                if (KiemTra()) {
                    if (user == null) {
                        GetData();
                        auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), "123456")
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String ID = task.getResult().getUser().getUid();
                                            String name = edtHoTen.getText().toString();
                                            String email = edtEmail.getText().toString();
                                            String address = edtDiaChi.getText().toString();
                                            String phone = edtSDT.getText().toString();
                                            String birthDay = edtNgaySinh.getText().toString();
                                            String idCard = edtCMND.getText().toString();
                                            String viTri = spLoaiTK.getSelectedItem().toString();
                                            String matKhau = "123456";
                                            writeNewUser(ID, name, email, phone, address, "Khách hàng", idCard, birthDay, matKhau);
                                            kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                            kAlertDialog.setTitleText("Đăng Kí Thành Công!");
                                            kAlertDialog.setCancelable(true);
                                            kAlertDialog.showConfirmButton(false);

                                        } else {
                                            kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                            kAlertDialog.setTitleText("Đăng Kí Không Thành Công!");
                                            kAlertDialog.setCancelable(true);
                                            kAlertDialog.showConfirmButton(false);
                                        }
                                    }
                                });
                        auth.signOut();
                        auth.signInWithEmailAndPassword(nguoiDungHienTai.getEmail(), nguoiDungHienTai.getMatKhau());
                    } else {
                        String name = edtHoTen.getText().toString();
                        String address = edtDiaChi.getText().toString();
                        String phone = edtSDT.getText().toString();
                        String birthDay = edtNgaySinh.getText().toString();
                        String idCard = edtCMND.getText().toString();
                        String viTri = spLoaiTK.getSelectedItem().toString();
                        writeNewUser(user.getMa(), name, user.getEmail(), phone, address, viTri, idCard, birthDay, user.getMatKhau());
                        kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                        kAlertDialog.setTitleText("Lưu thông tin thành công!");
                        kAlertDialog.setCancelable(true);
                        kAlertDialog.showConfirmButton(false);
                    }
                } else {
                    kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                    kAlertDialog.setTitleText("Lưu thông tin không thành công!");
                    kAlertDialog.setCancelable(true);
                    kAlertDialog.showConfirmButton(false);
                }
            }
        });
    }

    private void GetData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                nguoiDungHienTai = dataSnapshot.getValue(User.class);
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
        mDatabase.child("user").child(auth.getUid()).addValueEventListener(postListener);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                    }
                },
                1000);
    }


    public void writeNewUser(String ma, String hoTen, String email, String soDT, String diaChi, String viTriCV, String CMND, String ngaySinh, String matKhau) {
        User user = new User(ma, hoTen, email, soDT, diaChi, viTriCV, CMND, ngaySinh, matKhau);
        mDatabase.child("user").child(ma).setValue(user);
    }

    private void setControl() {
        edtHoTen = findViewById(R.id.edtHoVaTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtSDT = findViewById(R.id.edtSoDienThoai);
        edtCMND = findViewById(R.id.edtCMND);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        spLoaiTK = findViewById(R.id.spLoaiTaiKhoan);
        btnLuu = findViewById(R.id.btnLuuThongTin);
    }

    private boolean KiemTra() {
        boolean res = true;
        if (edtHoTen.getText().toString().equals("")) {
            edtHoTen.setError("Vui Lòng Nhập Tên Của Bạn");
            res = false;
        }

        if (edtEmail.getText().toString().equals("")) {
            edtEmail.setError("Vui Lòng Nhập Email");
            res = false;
        } else {
            String emailAddress = edtEmail.getText().toString().trim();
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                res = true;
            } else {
                edtEmail.setError("ĐỊng dạng email không hợp lệ");
                res = false;
            }
        }
        if (edtDiaChi.getText().toString().equals("")) {
            edtDiaChi.setError("Vui Lòng Nhập Địa Chỉ");
            res = false;
        }
        if (edtSDT.getText().toString().equals("")) {
            edtSDT.setError("Vui Lòng Nhập Số Điện Thoại");
            res = false;
        }
        if (edtNgaySinh.getText().toString().equals("")) {
            edtNgaySinh.setError("Vui Lòng Chọn Ngày Sinh");
            res = false;
        }
        if (edtCMND.getText().toString().equals("")) {
            edtCMND.setError("Vui Lòng Nhập Số CMND");
            res = false;
        }
        return res;
    }
}