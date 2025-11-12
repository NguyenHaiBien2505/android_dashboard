package com.he180659.dashboard1.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "danhmuc")
public class DanhMuc {
    @PrimaryKey
    private int maDanhMuc;

    private String tenDanhMuc;

    // Constructor mặc định
    public DanhMuc() {
    }

    // Constructor với tham số
    public DanhMuc(int maDanhMuc, String tenDanhMuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }

    // Getter và Setter
    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    @Override
    public String toString() {
        return tenDanhMuc;
    }
}
