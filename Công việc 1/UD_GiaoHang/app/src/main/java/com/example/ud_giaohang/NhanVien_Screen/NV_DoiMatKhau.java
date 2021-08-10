package com.example.ud_giaohang.NhanVien_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.developer.kalert.KAlertDialog;
import com.example.ud_giaohang.KhachHang_Screen.KH_Setting;
import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NV_DoiMatKhau extends AppCompatActivity {
    Button btnLuu;
    User nguoiDungHienTai = new User();
    EditText edtTentk,edtMKHientai,edtMKMoi,edtNhapLaimk;
    FirebaseAuth auth;//Bien ket noi authecation
    KAlertDialog kAlertDialog;//Bien dialog
    DatabaseReference mDatabase;
    ImageButton ibback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_doi_mat_khau);
        auth = FirebaseAuth.getInstance();//khoi tao ket noi
        mDatabase  = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setControl();
        LoadData();
        LoadUser();
        setEvent();
    }
    private void LoadData() {
        edtTentk.setText(auth.getCurrentUser().getEmail());//Lay data tk hien tai
    }

    private void setEvent() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KiemTra())
                {
                    ChangePass();
                }
            }
        });

        ibback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), KH_Setting.class);
                startActivity(intent);
            }
        });
    }

    public void ChangePass(){
        kAlertDialog = new KAlertDialog(this );
        kAlertDialog.changeAlertType(KAlertDialog.PROGRESS_TYPE);
        kAlertDialog.setTitleText("Loading");
        kAlertDialog.setCancelable(false);
        kAlertDialog.showConfirmButton(false);
        kAlertDialog.showCancelButton(false);
        kAlertDialog.show();
        FirebaseUser user = auth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(edtTentk.getText().toString(),edtMKHientai.getText().toString());
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(edtMKMoi.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        kAlertDialog.setTitleText("Đổi mật khẩu thành công");
                                        kAlertDialog.setCancelable(true);
                                        kAlertDialog.showConfirmButton(false);
                                        nguoiDungHienTai.setMatKhau(edtMKMoi.getText().toString());
                                        mDatabase.child("user").child(auth.getUid()).setValue(nguoiDungHienTai);
                                    } else {
                                        kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                        kAlertDialog.setTitleText("Đổi mật khẩu Không thành công");
                                        kAlertDialog.setCancelable(true);
                                        kAlertDialog.showConfirmButton(false);
                                    }
                                }
                            });
                        } else {
                            kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                            kAlertDialog.setTitleText("Sai mât khẩu");
                            kAlertDialog.setCancelable(true);
                            kAlertDialog.showConfirmButton(false);
                        }
                    }
                });
    }

    private void LoadUser() {
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
    }

    public boolean KiemTra(){
        boolean res = true;
        if(edtTentk.getText().toString().equals("")){
            edtTentk.setError("Vui Lòng Nhập Tên Tài Khoản Của Bạn");
            res = false;
        }if(edtMKHientai.getText().toString().equals("")){
            edtMKHientai.setError("Vui Lòng Mật Khẩu Của Bạn");
            res = false;
        }if(edtMKMoi.getText().toString().equals("")){
            edtMKMoi.setError("Vui Lòng Nhập Mật Khẩu Mà Bạn Muốn Đổi");
            res = false;
        }if(edtNhapLaimk.getText().toString().equals("")){
            edtMKMoi.setError("Vui Lòng Nhập Nhập Lại Mật Khẩu Mới Của Bạn");
            res = false;
        }else {
            if (!edtNhapLaimk.getText().toString().equals(edtMKMoi.getText().toString())){
                edtNhapLaimk.setError("Mật Khẩu Không Trùng Khớp");
            }
        }
        return res;
    }

    private void setControl() {
        btnLuu = findViewById(R.id.btnLuu);
        edtTentk = findViewById(R.id.tentk);
        edtMKHientai = findViewById(R.id.mkhientai);
        edtMKMoi = findViewById(R.id.mkmoi);
        edtNhapLaimk = findViewById(R.id.nhaplaimk);
        ibback = findViewById(R.id.ibBackKH);
    }
}