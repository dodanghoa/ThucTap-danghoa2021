package com.example.ud_giaohang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.ud_giaohang.Admin_Screen.AD_Home;
import com.example.ud_giaohang.KhachHang_Screen.KH_Home;
import com.example.ud_giaohang.NhanVien_Screen.NV_Home;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ornach.nobobutton.NoboButton;


import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    boolean res;
    NoboButton imgLogin_Google;
    EditText edtUserName, edtPassword;
    NoboButton btnLogin;
    TextView signUp, txtQuenMatKhau;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    DatabaseReference mDatabase;
    KAlertDialog kAlertDialog;
    User nguoiDungHienTai = new User();
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            PhanQuyen();
        }
    }
    private void PhanQuyen() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                nguoiDungHienTai = dataSnapshot.getValue(User.class);
                if (nguoiDungHienTai.getViTriCV().equals("Nhân viên"))
                {
                    Intent intent = new Intent(getApplicationContext(), NV_Home.class);
                    startActivity(intent);
                }
                else if (nguoiDungHienTai.getViTriCV().equals("Quản lý"))
                {
                    Intent intent = new Intent(getApplicationContext(), AD_Home.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), KH_Home.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        mDatabase.child("user").child(auth.getUid()).addValueEventListener(postListener);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
//        setRequest();
        setControl();
        setEvent();
    }

    private void setControl() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        signUp = findViewById(R.id.txtSignUp);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
    }

    private void setEvent() {
        Context context = this;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KiemTra()) {
                    kAlertDialog = new KAlertDialog(context);
                    kAlertDialog.changeAlertType(KAlertDialog.PROGRESS_TYPE);
                    kAlertDialog.setTitleText("Loading");
                    kAlertDialog.setCancelable(false);
                    kAlertDialog.showConfirmButton(false);
                    kAlertDialog.show();
                    auth.signInWithEmailAndPassword(edtUserName.getText().toString(), edtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        kAlertDialog.dismiss();
                                        //Thay đổi nếu đăng nhập thành công thì sao
                                        Toast.makeText(context, "Đăng Nhập Thành Công!", Toast.LENGTH_SHORT).show();
                                        PhanQuyen();
                                    } else {
                                        kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                        kAlertDialog.setTitleText("Đăng Nhập Không Thành Công!");
                                        kAlertDialog.setCancelable(true);
                                        kAlertDialog.showConfirmButton(false);
                                    }
                                }
                            });
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignUp.class);
                startActivity(intent);
            }
        });


        txtQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuenMatKhau.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Bạn Có Muốn Thoát?")
                .setCancelable(false)
                .setPositiveButton("Có", new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Không", new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        mDialog.show();
    }
    private boolean KiemTra() {
        boolean res = true;
        if (edtUserName.getText().toString().equals("")) {
            edtUserName.setError("Vui Lòng Nhập Email Của Bạn");
            res = false;
        }else {
            String emailAddress = edtUserName.getText().toString().trim();
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                res = true;
            } else {
                edtUserName.setError("ĐỊng dạng email không hợp lệ");
                res = false;
            }
        }
        if (edtPassword.getText().toString().equals("")) {
            edtPassword.setError("Vui Lòng Nhập Mật Khẩu");
            res = false;
        } else {
            if (edtPassword.getText().toString().length() < 6) {
                edtPassword.setError("Mật Khẩu Quá Ngắn");
                res = false;
            }
        }
        return res;
    }


}