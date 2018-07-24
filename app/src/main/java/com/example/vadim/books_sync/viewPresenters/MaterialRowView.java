package com.example.vadim.books_sync.viewPresenters;

import android.support.annotation.NonNull;

import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.views.MaterialViewHolder;

import java.util.List;

public interface MaterialRowView {
    void onBindViewHolder(@NonNull MaterialViewHolder materialViewHolder, int position);
    void setListContent(List<Material> materials);
    void removeAt(int position);
}
