package com.he180659.dashboard1.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.he180659.dashboard1.model.DanhMuc;

import java.util.List;

@Dao
public interface DanhMucDao {

    @Insert
    void insert(DanhMuc danhMuc);

    @Update
    void update(DanhMuc danhMuc);

    @Delete
    void delete(DanhMuc danhMuc);

    @Query("SELECT * FROM danhmuc ORDER BY tenDanhMuc")
    LiveData<List<DanhMuc>> getAllCategories();

    @Query("SELECT * FROM danhmuc WHERE maDanhMuc = :maDanhMuc")
    LiveData<DanhMuc> getCategoryById(int maDanhMuc);

    @Query("SELECT COUNT(*) FROM danhmuc")
    int getCategoryCount();
}