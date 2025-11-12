package com.he180659.dashboard1.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.he180659.dashboard1.model.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    long insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM product WHERE maSanPham = :maSanPham")
    void deleteById(int maSanPham);

    @Query("SELECT * FROM product ORDER BY ngayTao DESC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM product WHERE maSanPham = :maSanPham")
    LiveData<Product> getProductById(int maSanPham);
    
    // Tìm kiếm sản phẩm theo tên (không phân biệt hoa thường)
    @Query("SELECT * FROM product WHERE LOWER(tenSanPham) LIKE '%' || LOWER(:query) || '%' ORDER BY ngayTao DESC")
    LiveData<List<Product>> searchProducts(String query);

    @Query("SELECT * FROM product WHERE maDanhMuc = :maDanhMuc")
    LiveData<List<Product>> getProductsByCategory(int maDanhMuc);

    @Query("SELECT COUNT(*) FROM product")
    int getProductCount();
}