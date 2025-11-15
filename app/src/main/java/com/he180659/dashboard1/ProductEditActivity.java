package com.he180659.dashboard1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductEditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

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
    private Button btnTakePhoto;
    private Button btnHuy;
    private Button btnLuu;

    private ProductViewModel productViewModel;
    private List<DanhMuc> danhMucList;
    private ArrayAdapter<DanhMuc> spinnerAdapter;

    private Product currentProduct;
    private boolean isEditMode = false;
    private String selectedImageBase64 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

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
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);

        etGiaBan.addTextChangedListener(new NumberTextWatcher(etGiaBan));
    }

    private void setupClickListeners() {
        btnChangeImage.setOnClickListener(v -> {
            openImagePicker();
        });

        btnTakePhoto.setOnClickListener(v -> {
            takePhoto();
        });

        btnHuy.setOnClickListener(v -> {
            finish();
        });

        btnLuu.setOnClickListener(v -> {
            saveProduct();
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Không thể mở camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                // Xử lý ảnh từ thư viện
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        // Resize ảnh để tránh kích thước quá lớn
                        bitmap = resizeBitmap(bitmap, 800, 800);
                        selectedImageBase64 = Product.bitmapToBase64(bitmap);
                        ivProductImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi khi chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                // Xử lý ảnh từ camera
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        // Resize ảnh
                        imageBitmap = resizeBitmap(imageBitmap, 800, 800);
                        selectedImageBase64 = Product.bitmapToBase64(imageBitmap);
                        ivProductImage.setImageBitmap(imageBitmap);
                    }
                }
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > maxWidth || height > maxHeight) {
            float ratio = Math.min((float) maxWidth / width, (float) maxHeight / height);
            width = Math.round(width * ratio);
            height = Math.round(height * ratio);

            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        return bitmap;
    }

    private void loadDanhMucData() {
        productViewModel.getAllCategories().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                danhMucList = categories;
                setupSpinner();

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
            isEditMode = true;
            currentProduct = (Product) getIntent().getSerializableExtra("PRODUCT");

            if (currentProduct != null) {
                tvTitle.setText("CHỈNH SỬA SẢN PHẨM");
            }
        } else {
            isEditMode = false;
            tvTitle.setText("THÊM SẢN PHẨM MỚI");
            tvMaSanPhamLabel.setVisibility(View.GONE);
            tvMaSanPham.setVisibility(View.GONE);
        }
    }

    private void displayProductData() {
        if (currentProduct == null)
            return;

        tvMaSanPham.setText(String.valueOf(currentProduct.getMaSanPham()));
        etTenSanPham.setText(currentProduct.getTenSanPham());
        etMoTa.setText(currentProduct.getMoTa());

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        etGiaBan.setText(formatter.format(currentProduct.getGiaBan()));

        etSoLuong.setText(String.valueOf(currentProduct.getSoLuong()));

        // Hiển thị ảnh từ Base64
        String hinhAnh = currentProduct.getHinhAnh();
        if (hinhAnh != null && !hinhAnh.trim().isEmpty()) {
            Bitmap bitmap = Product.base64ToBitmap(hinhAnh);
            if (bitmap != null) {
                ivProductImage.setImageBitmap(bitmap);
                selectedImageBase64 = hinhAnh;
            }
        }

        if (danhMucList != null) {
            for (int i = 0; i < danhMucList.size(); i++) {
                if (danhMucList.get(i).getMaDanhMuc() == currentProduct.getMaDanhMuc()) {
                    spinnerDanhMuc.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveProduct() {
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
        if (currentProduct == null)
            return;

        currentProduct.setTenSanPham(etTenSanPham.getText().toString().trim());
        currentProduct.setMoTa(etMoTa.getText().toString().trim());

        String giaBanStr = etGiaBan.getText().toString().replaceAll("[.,]", "");
        currentProduct.setGiaBan(Double.parseDouble(giaBanStr));

        currentProduct.setSoLuong(Integer.parseInt(etSoLuong.getText().toString().trim()));

        DanhMuc selectedDanhMuc = (DanhMuc) spinnerDanhMuc.getSelectedItem();
        currentProduct.setMaDanhMuc(selectedDanhMuc.getMaDanhMuc());

        // Lưu ảnh dạng Base64 (chỉ lưu nếu không rỗng)
        // Khi chỉnh sửa, nếu không chọn ảnh mới thì giữ nguyên ảnh cũ
        if (selectedImageBase64 != null && !selectedImageBase64.trim().isEmpty()) {
            currentProduct.setHinhAnh(selectedImageBase64);
        }
        // Nếu selectedImageBase64 rỗng, giữ nguyên ảnh cũ (không thay đổi)

        productViewModel.updateProduct(currentProduct);

        Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void createNewProduct() {
        Product newProduct = new Product();
        newProduct.setTenSanPham(etTenSanPham.getText().toString().trim());
        newProduct.setMoTa(etMoTa.getText().toString().trim());

        String giaBanStr = etGiaBan.getText().toString().replaceAll("[.,]", "");
        newProduct.setGiaBan(Double.parseDouble(giaBanStr));

        newProduct.setSoLuong(Integer.parseInt(etSoLuong.getText().toString().trim()));

        DanhMuc selectedDanhMuc = (DanhMuc) spinnerDanhMuc.getSelectedItem();
        newProduct.setMaDanhMuc(selectedDanhMuc.getMaDanhMuc());

        // Lưu ảnh dạng Base64 (chỉ lưu nếu không rỗng)
        if (selectedImageBase64 != null && !selectedImageBase64.trim().isEmpty()) {
            newProduct.setHinhAnh(selectedImageBase64);
        } else {
            newProduct.setHinhAnh(null);
        }
        newProduct.setNgayTao(new Date());

        productViewModel.insertProduct(newProduct);

        Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}