package com.lipakov.vadim.booksDocs.basePresenters;


import com.lipakov.vadim.booksDocs.model.Material;

import java.util.LinkedList;

public interface BaseMaterialsPresenter {
    void updateMaterials(LinkedList<Material> materials);
}
