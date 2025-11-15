package com.he180659.dashboard1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.he180659.dashboard1.adapter.ProductAdapter;
import com.he180659.dashboard1.model.DanhMuc;
import com.he180659.dashboard1.model.Product;
import com.he180659.dashboard1.utils.ResourceToBase64;
import com.he180659.dashboard1.viewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductManagementActivity extends AppCompatActivity implements ProductAdapter.OnProductActionListener {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<DanhMuc> danhMucList;
    private EditText etSearch;
    private ImageButton btnSearch, btnAddProduct;
    private ImageButton btnBack;

    private ProductViewModel productViewModel;
    private boolean isSearching = false;
    private String currentSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        // Khởi tạo ViewModel
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        observeData();
        insertSampleData();
    }

    private void initViews() {
        rvProducts = findViewById(R.id.rvProducts);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        danhMucList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, danhMucList, this);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(productAdapter);
    }

    private void setupClickListeners() {
        btnAddProduct.setOnClickListener(v -> {
            addNewProduct();
        });

        btnSearch.setOnClickListener(v -> {
            performSearch();
        });

        btnBack.setOnClickListener(v -> {
            finish(); // Quay lại MainActivity (Dashboard)
        });

        // Xử lý sự kiện khi nhấn Enter trong ô tìm kiếm
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            performSearch();
            return true;
        });
    }

    private void observeData() {
        // Quan sát danh sách sản phẩm từ database
        productViewModel.getAllProducts().observe(this, products -> {
            if (!isSearching) {
                Log.d("ProductManagement", "Products updated: " + (products != null ? products.size() : 0));
                updateProductList(products);
            }
        });

        // Quan sát danh sách danh mục từ database
        productViewModel.getAllCategories().observe(this, categories -> {
            Log.d("ProductManagement", "Categories updated: " + (categories != null ? categories.size() : 0));
            danhMucList.clear();
            if (categories != null) {
                danhMucList.addAll(categories);
            }
            productAdapter.updateCategoryList(danhMucList);
        });
    }

    private void performSearch() {
        String query = etSearch.getText().toString().trim();
        currentSearchQuery = query;

        if (query.isEmpty()) {
            // Nếu query rỗng, hiển thị tất cả sản phẩm
            isSearching = false;
            productViewModel.getAllProducts().observe(this, this::updateProductList);
            showToast("Hiển thị tất cả sản phẩm");
        } else {
            // Thực hiện tìm kiếm
            isSearching = true;
            productViewModel.searchProducts(query).observe(this, products -> {
                Log.d("ProductManagement", "Search results for '" + query + "': " + (products != null ? products.size() : 0));
                updateProductList(products);
                if (products != null && products.isEmpty()) {
                    showToast("Không tìm thấy sản phẩm nào với từ khóa: " + query);
                } else {
                    showToast("Tìm thấy " + (products != null ? products.size() : 0) + " sản phẩm");
                }
            });
        }
    }

    private void updateProductList(List<Product> products) {
        productList.clear();
        if (products != null) {
            productList.addAll(products);
        }
        productAdapter.notifyDataSetChanged();
    }

    private void insertSampleData() {
        // Chèn dữ liệu mẫu nếu database trống
        new Thread(() -> {
            try {
                Thread.sleep(500);

                if (productViewModel.getAllCategories().getValue() == null ||
                        productViewModel.getAllCategories().getValue().isEmpty()) {

                    Log.d("ProductManagement", "Inserting sample data...");

                    // Chèn danh mục
                    List<DanhMuc> sampleCategories = new ArrayList<>();
                    sampleCategories.add(new DanhMuc(1, "Điện thoại"));
                    sampleCategories.add(new DanhMuc(2, "Laptop"));
                    sampleCategories.add(new DanhMuc(3, "Phụ kiện"));
                    sampleCategories.add(new DanhMuc(4, "Đồng hồ thông minh"));

                    for (DanhMuc category : sampleCategories) {
                        productViewModel.insertCategory(category);
                    }

                    Thread.sleep(500);

                    // Chèn sản phẩm với ảnh THẬT từ drawable
                    List<Product> sampleProducts = new ArrayList<>();

                    sampleProducts.add(new Product(
                            ResourceToBase64.resourceToBase64(this, R.drawable.anhdt),
                            "iPhone 14 Pro",
                            "Điện thoại flagship của Apple với camera 48MP và chip A16 Bionic",
                            27990000, 50, 1, new Date()
                    ));

                    sampleProducts.add(new Product(
                            ResourceToBase64.resourceToBase64(this, R.drawable.anhdt),
                            "iPhone 13",
                            "Điện thoại Apple với chip A15 Bionic và thời lượng pin ấn tượng",
                            19990000, 30, 1, new Date()
                    ));

                    sampleProducts.add(new Product(
                            ResourceToBase64.resourceToBase64(this, R.drawable.anhdt),
                            "Samsung Galaxy S23",
                            "Điện thoại Android cao cấp với camera 50MP và chip Snapdragon 8 Gen 2",
                            21990000, 30, 1, new Date()
                    ));

                    sampleProducts.add(new Product(
                            ResourceToBase64.resourceToBase64(this, R.drawable.anhdt),
                            "MacBook Air M2",
                            "Laptop siêu mỏng nhẹ với chip Apple M2, màn hình Liquid Retina",
                            32990000, 20, 2, new Date()
                    ));

                    sampleProducts.add(new Product(
                            ResourceToBase64.resourceToBase64(this, R.drawable.anhdt),
                            "Dell XPS 13",
                            "Laptop cao cấp Dell với thiết kế viền màn hình InfinityEdge",
                            28990000, 15, 2, new Date()
                    ));

                    sampleProducts.add(new Product(
                            ResourceToBase64.resourceToBase64(this, R.drawable.anhdt),
                            "Tai nghe AirPods Pro",
                            "Tai nghe không dây Apple với chống ồn chủ động và tính năng Spatial Audio",
                            5990000, 100, 3, new Date()
                    ));

                    sampleProducts.add(new Product(
                            ResourceToBase64.resourceToBase64(this, R.drawable.anhdt),
                            "Apple Watch Series 8",
                            "Đồng hồ thông minh Apple với tính năng đo nhiệt độ và cảm biến va chạm",
                            11990000, 40, 4, new Date()
                    ));

                    for (Product product : sampleProducts) {
                        productViewModel.insertProduct(product);
                    }

                    Log.d("ProductManagement", "Sample data inserted successfully");
                }
            } catch (Exception e) {
                Log.e("ProductManagement", "Error inserting sample data", e);
            }
        }).start();
    }

    private void addNewProduct() {
        // Mở màn hình thêm sản phẩm mới
        Intent intent = new Intent(ProductManagementActivity.this, ProductEditActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteProduct(Product product) {
        showDeleteConfirmationDialog(product);
    }

    @Override
    public void onEditProduct(Product product) {
        // Mở màn hình chỉnh sửa sản phẩm
        Intent intent = new Intent(ProductManagementActivity.this, ProductEditActivity.class);
        intent.putExtra("PRODUCT", product);
        startActivity(intent);
    }

    @Override
    public void onViewProductInfo(Product product) {
        // Mở màn hình chi tiết sản phẩm
        Intent intent = new Intent(ProductManagementActivity.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT", product);
        startActivity(intent);
    }

    private void showDeleteConfirmationDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm \"" + product.getTenSanPham() + "\" không?");

        builder.setPositiveButton("Xóa", (dialog, which) -> {
            // Thực hiện xóa sản phẩm từ database
            productViewModel.deleteProduct(product);
            showToast("Đã xóa sản phẩm thành công");
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Tuỳ chỉnh màu sắc cho nút
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    // Phương thức hiển thị thông báo
    private void showToast(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (isSearching && !currentSearchQuery.isEmpty()) {
            // Nếu đang trong chế độ tìm kiếm, bấm back sẽ quay lại hiển thị tất cả
            etSearch.setText("");
            isSearching = false;
            productViewModel.getAllProducts().observe(this, this::updateProductList);
            showToast("Hiển thị tất cả sản phẩm");
        } else {
            super.onBackPressed();
        }
    }
}