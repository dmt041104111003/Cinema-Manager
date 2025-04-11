package com.example.cinemamanager.adapter.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemamanager.databinding.ItemCategoryAdminBinding;
import com.example.cinemamanager.model.Category;
import com.example.cinemamanager.util.GlideUtils;

import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.CategoryViewHolder> {

    private final List<Category> mListCategory;
    private final IManagerCategoryListener iManagerCategoryListener;

    public interface IManagerCategoryListener {
        void editCategory(Category category);

        void deleteCategory(Category category);
    }

    public AdminCategoryAdapter(List<Category> mListCategory, IManagerCategoryListener iManagerCategoryListener) {
        this.mListCategory = mListCategory;
        this.iManagerCategoryListener = iManagerCategoryListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryAdminBinding binding = ItemCategoryAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if (category != null) {
            bindCategory(holder, category);
        }
    }

    private void bindCategory(@NonNull CategoryViewHolder holder, @NonNull Category category) {
        GlideUtils.loadUrl(category.getImage(), holder.binding.imgCategory);
        holder.binding.tvCategoryName.setText(category.getName());
        holder.binding.imgEdit.setOnClickListener(v -> iManagerCategoryListener.editCategory(category));
        holder.binding.imgDelete.setOnClickListener(v -> iManagerCategoryListener.deleteCategory(category));
    }

    @Override
    public int getItemCount() {
        return (mListCategory == null || mListCategory.isEmpty()) ? 0 : mListCategory.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryAdminBinding binding;

        public CategoryViewHolder(@NonNull ItemCategoryAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}