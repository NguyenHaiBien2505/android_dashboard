package com.he180659.dashboard1.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.he180659.dashboard1.R;
import com.he180659.dashboard1.model.DanhMuc;
import com.he180659.dashboard1.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private List<DanhMuc> danhMucList;
    private OnProductActionListener listener;

    public interface OnProductActionListener {
        void onDeleteProduct(Product product);
        void onEditProduct(Product product);
        void onViewProductInfo(Product product);
    }

    public ProductAdapter(List<Product> productList, List<DanhMuc> danhMucList, OnProductActionListener listener) {
        this.productList = productList;
        this.danhMucList = danhMucList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateData(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    public void updateCategoryList(List<DanhMuc> newDanhMucList) {
        this.danhMucList.clear();
        this.danhMucList.addAll(newDanhMucList);
        notifyDataSetChanged();
    }

    // Phương thức tìm tên danh mục theo mã danh mục
    private String getTenDanhMuc(int maDanhMuc) {
        for (DanhMuc danhMuc : danhMucList) {
            if (danhMuc.getMaDanhMuc() == maDanhMuc) {
                return danhMuc.getTenDanhMuc();
            }
        }
        return "Chưa phân loại";
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvProductQuantity;
        private TextView tvProductCategory;
        private ImageButton btnDelete, btnEdit, btnInfo;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnInfo = itemView.findViewById(R.id.btnInfo);

            // Thiết lập sự kiện click
            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteProduct(productList.get(position));
                }
            });

            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onEditProduct(productList.get(position));
                }
            });

            btnInfo.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onViewProductInfo(productList.get(position));
                }
            });

            // Click vào item để xem thông tin
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onViewProductInfo(productList.get(position));
                }
            });
        }

        public void bind(Product product) {
            tvProductName.setText(product.getTenSanPham());
            tvProductPrice.setText(product.getFormattedPrice());
            tvProductQuantity.setText("Số lượng: " + product.getSoLuong());
            tvProductCategory.setText(getTenDanhMuc(product.getMaDanhMuc()));

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
        }
    }
}