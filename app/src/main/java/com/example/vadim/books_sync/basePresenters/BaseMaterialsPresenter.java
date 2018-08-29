package com.example.vadim.books_sync.basePresenters;


import com.example.vadim.books_sync.model.Material;

import java.util.LinkedList;

public interface BaseMaterialsPresenter {
    void updateMaterials(LinkedList<Material> materials);
}
