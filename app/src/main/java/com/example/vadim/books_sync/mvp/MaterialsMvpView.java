package com.example.vadim.books_sync.mvp;


import com.example.vadim.books_sync.model.Material;

import java.util.LinkedList;

public interface MaterialsMvpView {
    void loadMaterialFiles(LinkedList<Material> materials);
}
