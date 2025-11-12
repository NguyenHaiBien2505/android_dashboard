package com.he180659.dashboard1.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.he180659.dashboard1.converter.DateConverter;
import com.he180659.dashboard1.dao.DanhMucDao;
import com.he180659.dashboard1.dao.ProductDao;
import com.he180659.dashboard1.model.DanhMuc;
import com.he180659.dashboard1.model.Product;


@Database(
        entities = {Product.class, DanhMuc.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProductDao productDao();
    public abstract DanhMucDao danhMucDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "product_management.db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}