package com.example.ud_giaohang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ornach.nobobutton.NoboButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;


public class SignUp extends AppCompatActivity {
    private FirebaseAuth auth;
    TextView txtLogin;
    EditText edtUserName, edtPassword, edtAddress, edtEmail, edtRePassword, edtPhone,edtBirthDay,edtIDCard;
    NoboButton btnSignUp;
    KAlertDialog kAlertDialog;
    LinearLayout lnSignUp;
    DatabaseReference mDatabase;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        mDatabase  = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        setControl();
        setEvent();
    }
    private void setEvent() {
        Context context = this;
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KiemTra()) {
                    kAlertDialog = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE);
                    kAlertDialog.changeAlertType(KAlertDialog.PROGRESS_TYPE);
                    kAlertDialog.setTitleText("Loading");
                    kAlertDialog.setCancelable(false);
                    kAlertDialog.show();
                    auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String ID = task.getResult().getUser().getUid();
                                        String name = edtUserName.getText().toString();
                                        String email = edtEmail.getText().toString();
                                        String address = edtAddress.getText().toString();
                                        String phone = edtPhone.getText().toString();
                                        String birthDay  = edtBirthDay.getText().toString();
                                        String idCard  = edtIDCard.getText().toString();
                                        String matKhau  = edtPassword.getText().toString();
                                        writeNewUser(ID,name,email,phone,address,"Kh??ch h??ng",idCard,birthDay,matKhau);
                                        kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        kAlertDialog.setTitleText("????ng K?? Th??nh C??ng!");
                                        kAlertDialog.setCancelable(true);
                                        kAlertDialog.showConfirmButton(false);
                                    } else {
                                        kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                        kAlertDialog.setTitleText("????ng K?? Kh??ng Th??nh C??ng!");
                                        kAlertDialog.setCancelable(true);
                                        kAlertDialog.showConfirmButton(false);
                                    }
                                }
                            });
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date = +dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        edtBirthDay.setText(date);
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
    }
    private boolean KiemTra() {
        boolean res = true;
        if (edtUserName.getText().toString().equals("")) {
            edtUserName.setError("Vui L??ng Nh???p T??n C???a B???n");
            res = false;
        }

        if (edtEmail.getText().toString().equals("")) {
            edtEmail.setError("Vui L??ng Nh???p Email");
            res = false;
        }else {
            String emailAddress = edtEmail.getText().toString().trim();
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                res = true;
            } else {
                edtEmail.setError("?????ng d???ng email kh??ng h???p l???");
                res = false;
            }
        }

        if (edtAddress.getText().toString().equals("")) {
            edtAddress.setError("Vui L??ng Nh???p ?????a Ch???");
            res = false;
        }

        if (edtPassword.getText().toString().equals("")) {
            edtPassword.setError("Vui L??ng Nh???p M???t Kh???u");
            res = false;
        } else {
            if (edtPassword.getText().toString().length() < 6) {
                edtPassword.setError("M???t Kh???u Qu?? Ng???n");
                res = false;
            }
        }

        if (edtRePassword.getText().toString().equals("")) {
            edtRePassword.setError("Vui L??ng Nh???p M???t Kh???u");
        }else {
            if (!edtRePassword.getText().toString().equals(edtPassword.getText().toString())) {
                edtRePassword.setError("M???t Kh???u Kh??ng Tr??ng Kh???p");
                res = false;
            }
        }

        if (edtPhone.getText().toString().equals("")) {
            edtPhone.setError("Vui L??ng Nh???p S??? ??i???n Tho???i");
        }
        return res;
    }
    public void writeNewUser(String ma, String hoTen, String email, String soDT, String diaChi, String viTriCV, String CMND, String ngaySinh, String matKhau ){
        User user =new User(ma,hoTen,email,soDT,diaChi,viTriCV,CMND,ngaySinh,matKhau);
        mDatabase.child("user").child(ma).setValue(user);
    }
    private void setControl() {
        edtUserName = findViewById(R.id.edtUserName_SignUp);
        edtPassword = findViewById(R.id.edtPassword_SignUp);
        edtEmail = findViewById(R.id.edtEmail_SignUp);
        edtPhone = findViewById(R.id.edt_Phone);
        edtRePassword = findViewById(R.id.edtRePassword);
        edtAddress = findViewById(R.id.edtAddress_SignUp);
        edtBirthDay = findViewById(R.id.edtBirth);
        edtIDCard = findViewById(R.id.edtIDCard);
        btnSignUp = findViewById(R.id.btnSignUp);
        lnSignUp = findViewById(R.id.lnSignUp);
        txtLogin = findViewById(R.id.txtLogin);
    }
}