package com.example.cinemamanager.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemamanager.databinding.ItemCategoryBinding;
import com.example.cinemamanager.model.Category;
import com.example.cinemamanager.util.GlideUtils;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> mListCategory;
    private final IManagerCategoryListener iManagerCategoryListener;

    public interface IManagerCategoryListener {
        void clickItemCategory(Category category);
    }

    public CategoryAdapter(List<Category> mListCategory, IManagerCategoryListener iManagerCategoryListener) {
        this.mListCategory = mListCategory;
        this.iManagerCategoryListener = iManagerCategoryListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if (category != null) {
            GlideUtils.loadUrl(category.getImage(), holder.binding.imgCategory);
            holder.binding.tvCategoryName.setText(category.getName());
            holder.binding.layoutItem.setOnClickListener(v -> iManagerCategoryListener.clickItemCategory(category));
        }
    }

    @Override
    public int getItemCount() {
        return mListCategory == null ? 0 : mListCategory.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryBinding binding;

        public CategoryViewHolder(@NonNull ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}