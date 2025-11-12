package com.he180659.dashboard1.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(
        tableName = "product",
        foreignKeys = @ForeignKey(
                entity = DanhMuc.class,
                parentColumns = "maDanhMuc",
                childColumns = "maDanhMuc",
                onDelete = ForeignKey.SET_NULL
        )
)
public class Product  implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int maSanPham;

    private String hinhAnh;

    private String tenSanPham;

    private String moTa;

    private double giaBan;

    private int soLuong;

    @ColumnInfo(index = true)
    private int maDanhMuc;

    private Date ngayTao;

    // Constructor mặc định
    public Product() {
    }

    // Constructor đầy đủ tham số
    public Product(String hinhAnh, String tenSanPham, String moTa,
                   double giaBan, int soLuong, int maDanhMuc, Date ngayTao) {
        this.hinhAnh = hinhAnh;
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.maDanhMuc = maDanhMuc;
        this.ngayTao = ngayTao;
    }

    // Getter và Setter
    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    // Phương thức format giá bán
    public String getFormattedPrice() {
        return String.format("%,.0f VNĐ", giaBan);
    }

    // Phương thức hiển thị thông tin cơ bản
    public String getBasicInfo() {
        return tenSanPham + " - " + getFormattedPrice();
    }
}