package com.he180659.dashboard1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.he180659.dashboard1.converter.NumberTextWatcher;
import com.he180659.dashboard1.model.DanhMuc;
import com.he180659.dashboard1.model.Product;
import com.he180659.dashboard1.viewModel.ProductViewModel;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductEditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivProductImage;
    private TextView tvTitle;
    private TextView tvMaSanPhamLabel;
    private TextView tvMaSanPham;
    private EditText etTenSanPham;
    private EditText etMoTa;
    private EditText etGiaBan;
    private EditText etSoLuong;
    private Spinner spinnerDanhMuc;
    private Button btnChangeImage;
    private Button btnHuy;
    private Button btnLuu;

    private ProductViewModel productViewModel;
    private List<DanhMuc> danhMucList;
    private ArrayAdapter<DanhMuc> spinnerAdapter;

    private Product currentProduct;
    private boolean isEditMode = false;
    private String selectedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        // Khởi tạo ViewModel
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        initViews();
        setupClickListeners();
        loadDanhMucData();
        loadProductData();
    }

    private void initViews() {
        ivProductImage = findViewById(R.id.ivProductImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvMaSanPhamLabel = findViewById(R.id.tvMaSanPhamLabel);
        tvMaSanPham = findViewById(R.id.tvMaSanPham);
        etTenSanPham = findViewById(R.id.etTenSanPham);
        etMoTa = findViewById(R.id.etMoTa);
        etGiaBan = findViewById(R.id.etGiaBan);
        etSoLuong = findViewById(R.id.etSoLuong);
        spinnerDanhMuc = findViewById(R.id.spinnerDanhMuc);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);

        // Định dạng cho giá bán
        etGiaBan.addTextChangedListener(new NumberTextWatcher(etGiaBan));
    }

    private void setupClickListeners() {
        btnChangeImage.setOnClickListener(v -> {
            openImagePicker();
        });

        btnHuy.setOnClickListener(v -> {
            finish();
        });

        btnLuu.setOnClickListener(v -> {
            saveProduct();
        });
    }

    private void loadDanhMucData() {
        productViewModel.getAllCategories().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                danhMucList = categories;
                setupSpinner();

                // Sau khi load danh mục xong, hiển thị dữ liệu sản phẩm (nếu có)
                if (isEditMode && currentProduct != null) {
                    displayProductData();
                }
            }
        });
    }

    private void setupSpinner() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhMucList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDanhMuc.setAdapter(spinnerAdapter);
    }

    private void loadProductData() {
        if (getIntent() != null && getIntent().hasExtra("PRODUCT")) {
            // Chế độ chỉnh sửa
            isEditMode = true;
            currentProduct = (Product) getIntent().getSerializableExtra("PRODUCT");

            if (currentProduct != null) {
                tvTitle.setText("CHỈNH SỬA SẢN PHẨM");
                // Dữ liệu sản phẩm sẽ được hiển thị sau khi load danh mục xong
            }
        } else {
            // Chế độ tạo mới
            isEditMode = false;
            tvTitle.setText("THÊM SẢN PHẨM MỚI");
            tvMaSanPhamLabel.setVisibility(View.GONE);
            tvMaSanPham.setVisibility(View.GONE);
        }
    }

    private void displayProductData() {
        if (currentProduct == null) return;

        // Hiển thị mã sản phẩm
        tvMaSanPham.setText(String.valueOf(currentProduct.getMaSanPham()));

        // Hiển thị ảnh
        if (currentProduct.getHinhAnh() != null && !currentProduct.getHinhAnh().isEmpty()) {
            // Load ảnh từ URL (cần thư viện như Glide/Picasso)
            // Glide.with(this).load(currentProduct.getHinhAnh()).into(ivProductImage);
            selectedImagePath = currentProduct.getHinhAnh();
        }

        // Hiển thị thông tin
        etTenSanPham.setText(currentProduct.getTenSanPham());
        etMoTa.setText(currentProduct.getMoTa());

        // Định dạng giá bán
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        etGiaBan.setText(formatter.format(currentProduct.getGiaBan()));

        etSoLuong.setText(String.valueOf(currentProduct.getSoLuong()));

        // Chọn danh mục trong spinner
        if (danhMucList != null) {
            for (int i = 0; i < danhMucList.size(); i++) {
                if (danhMucList.get(i).getMaDanhMuc() == currentProduct.getMaDanhMuc()) {
                    spinnerDanhMuc.setSelection(i);
                    break;
                }
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                ivProductImage.setImageURI(imageUri);
                selectedImagePath = imageUri.toString();
            }
        }
    }

    private void saveProduct() {
        // Validate dữ liệu
        if (!validateData()) {
            return;
        }

        if (isEditMode) {
            updateProduct();
        } else {
            createNewProduct();
        }
    }

    private boolean validateData() {
        String tenSanPham = etTenSanPham.getText().toString().trim();
        String giaBanStr = etGiaBan.getText().toString().replaceAll("[.,]", "");
        String soLuongStr = etSoLuong.getText().toString().trim();

        if (tenSanPham.isEmpty()) {
            etTenSanPham.setError("Vui lòng nhập tên sản phẩm");
            return false;
        }

        if (giaBanStr.isEmpty()) {
            etGiaBan.setError("Vui lòng nhập giá bán");
            return false;
        }

        if (soLuongStr.isEmpty()) {
            etSoLuong.setError("Vui lòng nhập số lượng");
            return false;
        }

        DanhMuc selectedDanhMuc = (DanhMuc) spinnerDanhMuc.getSelectedItem();
        if (selectedDanhMuc == null) {
            Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void updateProduct() {
        if (currentProduct == null) return;

        // Cập nhật thông tin sản phẩm
        currentProduct.setTenSanPham(etTenSanPham.getText().toString().trim());
        currentProduct.setMoTa(etMoTa.getText().toString().trim());

        String giaBanStr = etGiaBan.getText().toString().replaceAll("[.,]", "");
        currentProduct.setGiaBan(Double.parseDouble(giaBanStr));

        currentProduct.setSoLuong(Integer.parseInt(etSoLuong.getText().toString().trim()));

        DanhMuc selectedDanhMuc = (DanhMuc) spinnerDanhMuc.getSelectedItem();
        currentProduct.setMaDanhMuc(selectedDanhMuc.getMaDanhMuc());

        currentProduct.setHinhAnh(selectedImagePath);

        // Cập nhật vào database
        productViewModel.updateProduct(currentProduct);

        Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void createNewProduct() {
        // Tạo sản phẩm mới
        Product newProduct = new Product();
        newProduct.setTenSanPham(etTenSanPham.getText().toString().trim());
        newProduct.setMoTa(etMoTa.getText().toString().trim());

        String giaBanStr = etGiaBan.getText().toString().replaceAll("[.,]", "");
        newProduct.setGiaBan(Double.parseDouble(giaBanStr));

        newProduct.setSoLuong(Integer.parseInt(etSoLuong.getText().toString().trim()));

        DanhMuc selectedDanhMuc = (DanhMuc) spinnerDanhMuc.getSelectedItem();
        newProduct.setMaDanhMuc(selectedDanhMuc.getMaDanhMuc());

        newProduct.setHinhAnh(selectedImagePath);
        newProduct.setNgayTao(new Date());

        // Thêm vào database
        productViewModel.insertProduct(newProduct);

        Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}