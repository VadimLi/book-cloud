package com.example.vadim.books_sync.presenters;


import android.view.View;

import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.states_of_document.AbstractStateProperties;
import com.example.vadim.books_sync.views.PropertiesDialog;

public class MaterialPresenter implements StateOwnerProperties {

    private MaterialListPresenter materialListPresenter;

    private Material material;

    private int materialPosition;

    private AbstractStateProperties abstractPropertiesState;

    private PropertiesDialog propertiesDialog;

    public void attachDialog(PropertiesDialog propertiesDialog) {
        this.propertiesDialog = propertiesDialog;
    }

    public PropertiesDialog getPropertiesDialog() {
        return propertiesDialog;
    }

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

    public void setAbstractPropertiesState(AbstractStateProperties abstractPropertiesState) {
        this.abstractPropertiesState=abstractPropertiesState;
    }

    public AbstractStateProperties getAbstractPropertiesState() {
        return abstractPropertiesState;
    }

    @Override
    public void removeDocument() {
        abstractPropertiesState.doState(this);
    }

    @Override
    public void renameDocument() {
        abstractPropertiesState.doState(this);
    }

    @Override
    public void shareDocument() {
        abstractPropertiesState.doState(this);
    }

    @Override
    public void addToFolderDocument() {
        abstractPropertiesState.doState(this);
    }

}
