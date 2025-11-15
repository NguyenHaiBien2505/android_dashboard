package com.he180659.dashboard1.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;
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

    // Phương thức chuyển Bitmap sang Base64
    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) return "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Phương thức chuyển Base64 sang Bitmap
    public static Bitmap base64ToBitmap(String base64String) {
        if (base64String == null || base64String.isEmpty()) return null;

        try {
            // Loại bỏ data URI prefix nếu có (ví dụ: "data:image/png;base64,")
            String cleanBase64 = base64String;
            if (base64String.contains(",")) {
                cleanBase64 = base64String.substring(base64String.indexOf(",") + 1);
            }
            
            // Loại bỏ khoảng trắng và xuống dòng
            cleanBase64 = cleanBase64.replaceAll("\\s+", "");
            
            byte[] decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT);
            if (decodedBytes == null || decodedBytes.length == 0) {
                return null;
            }
            
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức kiểm tra xem chuỗi có phải là Base64 hợp lệ không
    public static boolean isValidBase64(String base64String) {
        if (base64String == null || base64String.isEmpty() || base64String.trim().isEmpty()) {
            return false;
        }

        try {
            // Loại bỏ data URI prefix nếu có
            String cleanBase64 = base64String;
            if (base64String.contains(",")) {
                cleanBase64 = base64String.substring(base64String.indexOf(",") + 1);
            }
            
            // Loại bỏ khoảng trắng và xuống dòng
            cleanBase64 = cleanBase64.replaceAll("\\s+", "");
            
            if (cleanBase64.isEmpty()) {
                return false;
            }
            
            // Thử decode
            byte[] decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT);
            
            // Kiểm tra xem có decode được không và có tạo được bitmap không
            if (decodedBytes == null || decodedBytes.length == 0) {
                return false;
            }
            
            // Thử tạo bitmap để đảm bảo dữ liệu hợp lệ
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            return bitmap != null;
        } catch (Exception e) {
            return false;
        }
    }
}