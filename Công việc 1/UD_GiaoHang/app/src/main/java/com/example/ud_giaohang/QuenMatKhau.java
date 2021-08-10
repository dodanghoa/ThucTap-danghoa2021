package com.example.ud_giaohang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ornach.nobobutton.NoboButton;

public class QuenMatKhau extends AppCompatActivity {

    EditText edtEmail;
    NoboButton btnRecovery;
    KAlertDialog kAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        setControl();
        setEvent();
    }

    private void setEvent() {
        Context context= this;
        btnRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KiemTra())
                {
                    kAlertDialog = new KAlertDialog(context,KAlertDialog.PROGRESS_TYPE);
                    kAlertDialog.setTitleText("Đang Gửi ...");
                    kAlertDialog.show();
                    kAlertDialog.setCancelable(false);
                    kAlertDialog.showConfirmButton(false);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(edtEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        kAlertDialog.setTitleText("Đã gửi Email !");
                                        kAlertDialog.setConfirmText("Vui lòng truy cập email và thay đổi mật khẩu!");
                                        kAlertDialog.setCancelable(true);
                                        kAlertDialog.showConfirmButton(false);
                                    }
                                    else {
                                        kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                        kAlertDialog.setTitleText(" Email chưa đăng ký !");
                                        kAlertDialog.setCancelable(true);
                                        kAlertDialog.showConfirmButton(false);
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean KiemTra() {
        boolean res = true;
        if (edtEmail.getText().toString()=="")
        {
            res = false;
        }else {
            String emailAddress = edtEmail.getText().toString().trim();
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                res = true;
            } else {
                edtEmail.setError("ĐỊng dạng email không hợp lệ");
                res = false;
            }
        }
        return res;
    }

    private void setControl() {
        edtEmail = findViewById(R.id.edtEmail);
        btnRecovery = findViewById(R.id.btnRecovery);
    }
}