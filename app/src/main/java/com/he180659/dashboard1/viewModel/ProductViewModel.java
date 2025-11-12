package com.he180659.dashboard1.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.he180659.dashboard1.model.DanhMuc;
import com.he180659.dashboard1.model.Product;
import com.he180659.dashboard1.repository.ProductRepository;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository repository;
    private LiveData<List<Product>> allProducts;
    private LiveData<List<DanhMuc>> allCategories;

    public ProductViewModel(Application application) {
        super(application);
        repository = new ProductRepository(application);
        allProducts = repository.getAllProducts();
        allCategories = repository.getAllCategories();
    }

    // Product methods
    public void insertProduct(Product product) {
        repository.insertProduct(product);
    }

    public void updateProduct(Product product) {
        repository.updateProduct(product);
    }

    public void deleteProduct(Product product) {
        repository.deleteProduct(product);
    }

    public void deleteProductById(int maSanPham) {
        repository.deleteProductById(maSanPham);
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<Product> getProductById(int maSanPham) {
        return repository.getProductById(maSanPham);
    }

    public LiveData<List<Product>> searchProducts(String query) {
        return repository.searchProducts(query);
    }



    // Category methods
    public void insertCategory(DanhMuc danhMuc) {
        repository.insertCategory(danhMuc);
    }

    public void updateCategory(DanhMuc danhMuc) {
        repository.updateCategory(danhMuc);
    }

    public void deleteCategory(DanhMuc danhMuc) {
        repository.deleteCategory(danhMuc);
    }

    public LiveData<List<DanhMuc>> getAllCategories() {
        return allCategories;
    }

    public LiveData<DanhMuc> getCategoryById(int maDanhMuc) {
        return repository.getCategoryById(maDanhMuc);
    }
}