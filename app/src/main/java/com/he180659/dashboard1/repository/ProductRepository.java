package com.he180659.dashboard1.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.he180659.dashboard1.dao.DanhMucDao;
import com.he180659.dashboard1.dao.ProductDao;
import com.he180659.dashboard1.database.AppDatabase;
import com.he180659.dashboard1.model.DanhMuc;
import com.he180659.dashboard1.model.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepository {

    private ProductDao productDao;
    private DanhMucDao danhMucDao;
    private LiveData<List<Product>> allProducts;
    private LiveData<List<DanhMuc>> allCategories;

    private ExecutorService executorService;

    public ProductRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        productDao = database.productDao();
        danhMucDao = database.danhMucDao();
        allProducts = productDao.getAllProducts();
        allCategories = danhMucDao.getAllCategories();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Product operations
    public void insertProduct(Product product) {
        executorService.execute(() -> {
            try {
                long id = productDao.insert(product);
                // Cập nhật mã sản phẩm với ID được tạo tự động
                product.setMaSanPham((int) id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateProduct(Product product) {
        executorService.execute(() -> {
            try {
                productDao.update(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteProduct(Product product) {
        executorService.execute(() -> {
            try {
                productDao.delete(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteProductById(int maSanPham) {
        executorService.execute(() -> {
            try {
                productDao.deleteById(maSanPham);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<Product> getProductById(int maSanPham) {
        return productDao.getProductById(maSanPham);
    }

    // Phương thức tìm kiếm sản phẩm
    public LiveData<List<Product>> searchProducts(String query) {
        return productDao.searchProducts(query);
    }

    // Category operations
    public void insertCategory(DanhMuc danhMuc) {
        executorService.execute(() -> {
            try {
                danhMucDao.insert(danhMuc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateCategory(DanhMuc danhMuc) {
        executorService.execute(() -> {
            try {
                danhMucDao.update(danhMuc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteCategory(DanhMuc danhMuc) {
        executorService.execute(() -> {
            try {
                danhMucDao.delete(danhMuc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LiveData<List<DanhMuc>> getAllCategories() {
        return allCategories;
    }

    public LiveData<DanhMuc> getCategoryById(int maDanhMuc) {
        return danhMucDao.getCategoryById(maDanhMuc);
    }
}