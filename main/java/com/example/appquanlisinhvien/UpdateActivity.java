package com.example.appquanlisinhvien;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {

    final String DATABASE = "quanlisinhviennew.db";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    int id = -1;

    Button btChonHinh, btChupHinh, btLuu, btHuy;
    EditText edtName, edtLop, edtNgaySinh;
    ImageView imgUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        addControls();
        initUI();
        addEvents();
    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",-1);
        SQLiteDatabase database = MyDatabase.initDatabase(this,DATABASE);
        Cursor cursor = database.rawQuery("SELECT * FROM sinhvien where id = ?",new String[]{id + ""});
        cursor.moveToFirst();
        String name = cursor.getString(1);
        String ngaySinh = cursor.getString(2);
        String lop = cursor.getString(3);
        byte[] anh = cursor.getBlob(4);

        Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);
        imgUpdate.setImageBitmap(bitmap);
        edtName.setText(name);
        edtLop.setText(lop);
        edtNgaySinh.setText(ngaySinh);
    }

    private void addControls(){
        btChonHinh = (Button)findViewById(R.id.btChonHinh);
        btChupHinh = (Button)findViewById(R.id.btChupHinh);
        btLuu = (Button)findViewById(R.id.btLuu);
        btHuy = (Button)findViewById(R.id.btHuy);
        edtName = (EditText) findViewById(R.id.edtName);
        edtLop = (EditText) findViewById(R.id.edtLop);
        edtNgaySinh = (EditText) findViewById(R.id.edtNgaySinh);
        imgUpdate = (ImageView)findViewById(R.id.imgUpdate);
    }

    private void addEvents(){
        btChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        btChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        btLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSinhVien();
            }
        });
        btHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CHOOSE_PHOTO);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void  onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgUpdate.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgUpdate.setImageBitmap(bitmap);
            }
        }
    }

    private void updateSinhVien(){
        String name = edtName.getText().toString();
        String lop = edtLop.getText().toString();
        String ngaySinh = edtNgaySinh.getText().toString();
        byte[] anh = getByteArrayFromImageView(imgUpdate);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("lop",lop);
        contentValues.put("ngaysinh",ngaySinh);
        contentValues.put("anh",anh);

        SQLiteDatabase database = MyDatabase.initDatabase(this,"quanlisinhviennew.db");
        database.update("sinhvien",contentValues,"id = ?", new String[] {id + ""});
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void cancel(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private byte[] getByteArrayFromImageView(ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable)imageView.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] anh = stream.toByteArray();
        return anh;
    }
}