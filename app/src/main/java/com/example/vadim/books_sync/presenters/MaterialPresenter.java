package com.example.vadim.books_sync.presenters;


import com.example.vadim.books_sync.model.Material;

public class MaterialPresenter {

    private MaterialListPresenter materialListPresenter;

    private Material material;

    private int materialPosition;

    public MaterialListPresenter getMaterialListPresenter() {
        return materialListPresenter;
    }

    public void setMaterialListPresenter(MaterialListPresenter materialListPresenter) {
        this.materialListPresenter = materialListPresenter;
    }

    public int getMaterialPosition() {
        return materialPosition;
    }

    public void setMaterialPosition(int materialPosition) {
        this.materialPosition = materialPosition;
    }

    public void removeDocument() {
        materialListPresenter.removeAt(this);
    }

    public void update(String newName) {
        materialListPresenter.updateAt(this, newName);
    }

    public long getId() {
        return material.getId();
    }

    public String getFormat() {
        return material.getFormat();
    }

    public String getName() {
        return material.getName();
    }

    public String getPath() {
        return material.getPath();
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
