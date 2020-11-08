package com.example.appquanlisinhvien;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterSinhVien extends BaseAdapter {
    Activity context;
    ArrayList<SinhVien> listSinhVien;

    public AdapterSinhVien(Activity context, ArrayList<SinhVien> listSinhVien) {
        this.context = context;
        this.listSinhVien = listSinhVien;
    }

    public AdapterSinhVien(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return listSinhVien.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.child,null,true);

        ImageView imageView = (ImageView)row.findViewById(R.id.imgAccount);
        TextView tvId = (TextView)row.findViewById(R.id.tvID);
        TextView tvName = (TextView)row.findViewById(R.id.tvName);
        TextView tvLop = (TextView)row.findViewById(R.id.tvLop);
        TextView tvNgaySinh = (TextView)row.findViewById(R.id.tvNgaySinh);
        Button btSua = (Button)row.findViewById(R.id.btSua);
        Button btXoa = (Button)row.findViewById(R.id.btXoa);

        final SinhVien sinhVien = listSinhVien.get(position);
        tvId.setText(sinhVien.id + "");
        tvName.setText(sinhVien.name + "");
        tvNgaySinh.setText(sinhVien.ngaySinh + "");
        tvLop.setText(sinhVien.lop + "");

        Bitmap bitmap = BitmapFactory.decodeByteArray(sinhVien.anh,0,sinhVien.anh.length);
        imageView.setImageBitmap(bitmap);

        btSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateActivity.class);
                intent.putExtra("ID",sinhVien.id);
                context.startActivity(intent);
            }
        });

        btXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa!");
                builder.setMessage("Bạn có chắc chắn muốn xóa ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(sinhVien.id);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return row;
    }

    private void delete(int id) {
        SQLiteDatabase database = MyDatabase.initDatabase(context,"quanlisinhviennew.db");
        database.delete("sinhvien","id = ?",new String[]{id + ""});
        listSinhVien.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM sinhvien",null);
        while(cursor.moveToNext()){
            int idSinhVien = cursor.getInt(0);
            String name = cursor.getString(1);
            String lop  = cursor.getString(2);
            String ngaySinh = cursor.getString(3);
            byte[] anh = cursor.getBlob(4);
            listSinhVien.add(new SinhVien(idSinhVien, name, ngaySinh, lop, anh));
        }
        notifyDataSetChanged();
    }
}
