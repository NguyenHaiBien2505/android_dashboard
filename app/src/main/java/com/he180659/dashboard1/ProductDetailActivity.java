package com.he180659.dashboard1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.he180659.dashboard1.model.Product;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvMaSanPham;
    private TextView tvTenSanPham;
    private TextView tvMoTa;
    private TextView tvGiaBan;
    private TextView tvSoLuong;
    private TextView tvDanhMuc;
    private TextView tvNgayTao;
    private Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();
        setupClickListeners();
        loadProductData();
    }

    private void initViews() {
        ivProductImage = findViewById(R.id.ivProductImage);
        tvMaSanPham = findViewById(R.id.tvMaSanPham);
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvMoTa = findViewById(R.id.tvMoTa);
        tvGiaBan = findViewById(R.id.tvGiaBan);
        tvSoLuong = findViewById(R.id.tvSoLuong);
        tvDanhMuc = findViewById(R.id.tvDanhMuc);
        tvNgayTao = findViewById(R.id.tvNgayTao);
        btnOK = findViewById(R.id.btnOK);
    }

    private void setupClickListeners() {
        btnOK.setOnClickListener(v -> {
            finish(); // Đóng activity khi bấm OK
        });
    }

    private void loadProductData() {
        // Lấy dữ liệu sản phẩm từ Intent
        if (getIntent() != null && getIntent().hasExtra("PRODUCT")) {
            Product product = (Product) getIntent().getSerializableExtra("PRODUCT");
            if (product != null) {
                displayProductData(product);
            }
        }
    }

    private void displayProductData(Product product) {
        // Hiển thị ảnh từ Base64
        String hinhAnh = product.getHinhAnh();
        if (hinhAnh != null && !hinhAnh.trim().isEmpty()) {
            Bitmap bitmap = Product.base64ToBitmap(hinhAnh);
            if (bitmap != null) {
                ivProductImage.setImageBitmap(bitmap);
            } else {
                ivProductImage.setImageResource(R.drawable.placeholder_image);
            }
        } else {
            ivProductImage.setImageResource(R.drawable.placeholder_image);
        }

        // Hiển thị thông tin sản phẩm
        tvMaSanPham.setText(String.valueOf(product.getMaSanPham()));
        tvTenSanPham.setText(product.getTenSanPham());
        tvMoTa.setText(product.getMoTa());
        tvGiaBan.setText(product.getFormattedPrice());
        tvSoLuong.setText(String.valueOf(product.getSoLuong()));

        // Hiển thị danh mục (cần lấy từ danh sách danh mục)
        tvDanhMuc.setText(getTenDanhMuc(product.getMaDanhMuc()));

        // Hiển thị ngày tạo
        if (product.getNgayTao() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvNgayTao.setText(dateFormat.format(product.getNgayTao()));
        } else {
            tvNgayTao.setText("Chưa có thông tin");
        }
    }

    private String getTenDanhMuc(int maDanhMuc) {
        // Lấy tên danh mục từ mã danh mục
        // Trong thực tế, bạn có thể lấy từ database hoặc truyền qua Intent
        switch (maDanhMuc) {
            case 1: return "Điện thoại";
            case 2: return "Laptop";
            case 3: return "Phụ kiện";
            case 4: return "Đồng hồ thông minh";
            case 5: return "Máy tính bảng";
            default: return "Chưa phân loại";
        }
    }
}
