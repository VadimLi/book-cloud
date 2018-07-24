package com.example.vadim.books_sync.viewPresenters;


import com.example.vadim.books_sync.model.Material;

import java.util.LinkedList;

public interface MaterialsView {
    void updateMaterials(LinkedList<Material> materials);
}
