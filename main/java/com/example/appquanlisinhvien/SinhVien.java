package com.example.appquanlisinhvien;

import java.util.Date;

public class SinhVien {
    public int id;
    public String name;
    public String ngaySinh;
    public String lop;
    public byte[] anh;

    public SinhVien(int id, String name, String ngaySinh, String lop, byte[] anh){
        this.id = id;
        this.name = name;
        this.ngaySinh = ngaySinh;
        this.lop = lop;
        this.anh =  anh;
    }
}
