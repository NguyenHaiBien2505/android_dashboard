package com.he180659.dashboard1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvQuanLySanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iconUser = findViewById(R.id.iconUser);
        tvQuanLySanPham = findViewById(R.id.tvQuanLySanPham);

        // Xử lý click icon user
        iconUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserPopupMenu(v);
            }
        });

        // Xử lý click Quản lý sản phẩm
        tvQuanLySanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductManagement();
            }
        });
    }

    private void showUserPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_profile) {
                    Toast.makeText(MainActivity.this, "Mở thông tin tài khoản", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.menu_update) {
                    Toast.makeText(MainActivity.this, "Cập nhật thông tin", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.menu_logout) {
                    Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void openProductManagement() {
        // Mở màn hình quản lý sản phẩm
        Intent intent = new Intent(MainActivity.this, ProductManagementActivity.class);
        startActivity(intent);
    }
}