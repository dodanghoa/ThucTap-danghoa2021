package com.example.ud_giaohang.KhachHang_Screen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.DonHang;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KH_TaoDonHang extends AppCompatActivity {
    User user;
    TextView tvTennguoidung, tv_cuocphi, tv_tongcuoc, tv_xoatatca;
    EditText et_sdt, et_tennguoinhan, et_diachi, et_giatrihang, et_cannang, et_soluong, et_thuhotien, et_tenhang;
    CheckBox ckb_thuhotien;
    ImageButton ib_anh1, ib_anh2, ib_anh3, ibBackKH;
    Button btn_taodon;
    Spinner nguoitratiencuoc, thaydoingaygiao, thaydoighichu;
    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    Uri uriImage1;
    Uri uriImage2;
    Uri uriImage3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kh_tao_don_hang);
        auth = FirebaseAuth.getInstance();
        user = new User();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
        storageRef = storage.getReference("image");
        setControl();
        et_thuhotien.setText("0");
        tv_cuocphi.setText("0");
        setEnvent();
        getUser();
    }

    //Load thông tin user vào biến user
    private void getUser() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User temp = new User();
                temp = dataSnapshot.getValue(User.class);
                user = temp;
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
                            tvTennguoidung.setText(user.getHoTen());
                        }
                    },
                    1000);
    }


    private boolean KiemTra() {
        boolean res = true;
        if (et_sdt.getText().toString().equals("")) {
            et_sdt.setError("Vui lòng nhập số điện thoại");
            res = false;
        }
        if (et_tennguoinhan.getText().toString().equals("")) {
            et_tennguoinhan.setError("Vui lòng nhập họ tên");
            res = false;
        }
        if (et_diachi.getText().toString().equals("")) {
            et_diachi.setError("Vui lòng nhập địa chỉ");
            res = false;
        }
        if (et_tenhang.getText().toString().equals("")) {
            et_tenhang.setError("Vui lòng nhập tên hàng");
            res = false;
        }
        if (et_giatrihang.getText().toString().equals("")) {
            et_giatrihang.setError("Vui lòng nhập giá trị hàng hóa");
            res = false;
        }
        if (et_cannang.getText().toString().equals("")) {
            et_cannang.setError("Vui lò ng nhập trọng lượng hàng hóa");
            res = false;
        }
        if (et_soluong.getText().toString().equals("")) {
            et_soluong.setError("Vui lòng nhập số lượng");
            res = false;
        }
        return res;
    }

    private void setEnvent() {
        btn_taodon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KiemTra()) {
                    //Lấy giá trị các trường
                    String sdt = et_sdt.getText().toString();
                    String tennguoinhan = et_tennguoinhan.getText().toString();
                    String diachi = et_diachi.getText().toString();
                    String tenhang = et_tenhang.getText().toString();
                    String giatrihang = et_giatrihang.getText().toString();
                    String trongluong = et_cannang.getText().toString();
                    String soluong = et_soluong.getText().toString();
                    String tienthuho = et_thuhotien.getText().toString();
                    String nguoitra = nguoitratiencuoc.getSelectedItem().toString();
                    String thoigiangiao = thaydoingaygiao.getSelectedItem().toString();
                    String ghichu = thaydoighichu.getSelectedItem().toString();
                    String cuocphi = tv_cuocphi.getText().toString();
                    String tongcuoc = tv_tongcuoc.getText().toString();
                    List<String> image = new ArrayList<>();
                    //truy cập firebase để lấy mã đơn hàng dựa trên sô lượng đơn hàng tồn tại trong child
                    mDatabase.child("DonHang")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // get total available quest
                                    String maDH = "DH_N00" + dataSnapshot.getChildrenCount();
                                    Date currentTime = Calendar.getInstance().getTime();
                                    if(uriImage1 != null){
                                        uploadPicture("anh1",maDH,uriImage1);
                                        image.add("anh1");
                                    }
                                    if(uriImage2 != null){
                                        uploadPicture("anh2",maDH,uriImage2);
                                        image.add("anh2");
                                    }
                                    if(uriImage3 != null){
                                        uploadPicture("anh3",maDH,uriImage3);
                                        image.add("anh3");
                                    }
                                    DonHang donHang = new DonHang(user,null,null,maDH,tennguoinhan,diachi,tenhang,nguoitra,thoigiangiao,ghichu,"Đã tạo đơn hàng",
                                            sdt,trongluong,soluong,giatrihang,tienthuho,cuocphi,tongcuoc,"",currentTime.toString(),image);
                                    mDatabase.child("DonHang").child(maDH).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                ;
                                            }
                                        }
                                    });
                                    Toast.makeText(KH_TaoDonHang.this, "Đơn hàng của bạn đã được tạo!", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                }

            }
        });

        ibBackKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),KH_Home.class);
                startActivity(intent);
            }
        });

        et_cannang.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!et_cannang.getText().toString().isEmpty()) {
                    if (Double.parseDouble(et_cannang.getText().toString()) <= 1) {
                        tv_cuocphi.setText("15000");

                    }
                    if (et_cannang.getText().toString().equals("0")) {
                        tv_cuocphi.setText("0");
                    }
                    if (Double.parseDouble(et_cannang.getText().toString()) > 1) {
                        tv_cuocphi.setText("30000");
                    }
                    if (Double.parseDouble(et_cannang.getText().toString()) >= 2.5) {
                        tv_cuocphi.setText("35000");
                    }
                    if (Double.parseDouble(et_cannang.getText().toString()) >= 5) {
                        tv_cuocphi.setText("50000");
                    }
                    if (Double.parseDouble(et_cannang.getText().toString()) >= 10) {
                        tv_cuocphi.setText("100000");
                    }
                }
            }
        });

        tv_cuocphi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tongCuoc = Integer.parseInt(tv_cuocphi.getText().toString()) + Integer.parseInt(et_thuhotien.getText().toString());
                tv_tongcuoc.setText(tongCuoc + " đ");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ckb_thuhotien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ckb_thuhotien.isChecked()) {
                    et_thuhotien.setText(et_giatrihang.getText().toString());
                    if(!et_thuhotien.getText().toString().isEmpty()&&!tv_cuocphi.getText().toString().isEmpty())
                    {
                        int tongCuoc = Integer.parseInt(tv_cuocphi.getText().toString()) + Integer.parseInt(et_thuhotien.getText().toString());
                        tv_tongcuoc.setText(tongCuoc + " đ");
                    }
                } else {
                    et_thuhotien.setText("0");
                }
            }
        });

        ib_anh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 1);
            }
        });

        ib_anh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 2);
            }
        });

        ib_anh3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 3);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage1 = data.getData();
            ib_anh1.setImageURI(uriImage1);
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage2 = data.getData();
            ib_anh2.setImageURI(uriImage2);
        }
        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage3 = data.getData();
            ib_anh3.setImageURI(uriImage3);
        }
    }


    private void uploadPicture(String nameImage, String rootPath, Uri uri) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        StorageReference mountainsRef = storageRef.child("DonHang_Image/"+ rootPath + "/" + nameImage);
        mountainsRef.putFile(uri)
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

    }

    private void setControl() {
        tvTennguoidung = findViewById(R.id.tvTennguoidung);
        tv_cuocphi = findViewById(R.id.tv_cuocphi);
        tv_tongcuoc = findViewById(R.id.tv_tongcuoc);
        tv_xoatatca = findViewById(R.id.tv_xoatatca);
        et_tenhang = findViewById(R.id.et_tenhanghoa);
        et_sdt = findViewById(R.id.et_sdt);
        et_tennguoinhan = findViewById(R.id.et_tennguoinhan);
        et_diachi = findViewById(R.id.et_diachi);
        et_giatrihang = findViewById(R.id.et_giatrihang);
        et_cannang = findViewById(R.id.et_cannang);
        et_soluong = findViewById(R.id.et_soluong);
        et_thuhotien = findViewById(R.id.et_thuhotien);
        ckb_thuhotien = findViewById(R.id.ckb_thuhotien);
        ib_anh1 = findViewById(R.id.ib_anh1);
        ib_anh2 = findViewById(R.id.ib_anh2);
        ib_anh3 = findViewById(R.id.ib_anh3);
        ibBackKH = findViewById(R.id.ibBackKH);
        btn_taodon = findViewById(R.id.btn_taodon);
        nguoitratiencuoc = findViewById(R.id.sp_nguoiguitiencuoc);
        thaydoingaygiao = findViewById(R.id.sp_thaydoingaygiao);
        thaydoighichu = findViewById(R.id.sp_thaydoighichu);
    }
}