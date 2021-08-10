package com.example.ud_giaohang.NhanVien_Screen;

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

import com.example.ud_giaohang.R;
import com.example.ud_giaohang.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class NV_SuaThongTin extends AppCompatActivity {
    Button btnSua;
    ImageButton ibtnBack;
    EditText edtMa, edtHoTen, edtEmail, edtSDT, edtDiaChi, edtNgaySinh, edtCMND;
    CircleImageView civAvatar;
    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri uriImage;
    User currentUser = new User() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_suathongtin);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://ud-giaohang-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        storage = FirebaseStorage.getInstance("gs://ud-giaohang.appspot.com");
        storageRef = storage.getReference("image");
        setControl();
        setEvent();

    }

    private void setEvent() {
        //Lay du lieu thong tin user tu firebase xuong
        mDatabase.child("user").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                currentUser = user;
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
                        Picasso.get().load(uri.toString()).into(civAvatar);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        //Tro lai truoc
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Sua du lieu
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = edtMa.getText().toString();
                String hoTen = edtHoTen.getText().toString();
                String email = edtEmail.getText().toString();
                String sdt = edtSDT.getText().toString();
                String diaChi = edtDiaChi.getText().toString();
                String cmnd = edtCMND.getText().toString();
                String ngaySinh = edtNgaySinh.getText().toString();
                User user = new User(ma, hoTen, email, sdt, diaChi, "Nhân viên", cmnd, ngaySinh,currentUser.getMatKhau());
                //Dua du lieu len firebase
                mDatabase.child("user").child(auth.getUid()).setValue(user);
                finish();
            }
        });
        //Click chon image
        civAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            civAvatar.setImageURI(uriImage);
            uploadPicture();
        }
    }

    //Ham luu image len firestore
    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String key = auth.getUid().toString();
        StorageReference mountainsRef = storageRef.child("image/" + key);
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

    }


    private void setControl() {
        //Anh xa
        btnSua = findViewById(R.id.btnSua);
        ibtnBack = findViewById(R.id.ibtnBack);
        edtMa = findViewById(R.id.edtMa);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtCMND = findViewById(R.id.edtCMND);
        civAvatar = findViewById(R.id.profile_image);
    }
}