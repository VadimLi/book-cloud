package com.example.vadim.books_sync.presenters;


import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.basePresenters.BaseRowPresenter;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.services.DocumentService;
import com.example.vadim.books_sync.presenters.services.Formats;
import com.example.vadim.books_sync.views.MaterialViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MaterialListPresenter {

    private List<Material> filterMaterials = new ArrayList<>();

    private DocumentService documentService;

    private List<MaterialPresenter> materialsPresenter = new ArrayList<>();

    private List<Material> materials = new ArrayList<>();

    private RecyclerView.Adapter<MaterialViewHolder> materialViewHolderAdapter;

    public final static int START_POSITION_OF_MATERIALS = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openDocumentByPath(View view, int position) {
        changeFirstElementWithPosition(position);
        documentService.openDocumentByPath(view,
                materialsPresenter.get(position).getMaterial());
    }

    private void changeFirstElementWithPosition(int position) {
        final MaterialPresenter materialPresenter = materialsPresenter.get(position);
        final Material material = materials.get(position);

        materials.set(position,
                materials.get(START_POSITION_OF_MATERIALS));
        materials.set(START_POSITION_OF_MATERIALS, material);

        materialsPresenter.set(position,
                materialsPresenter.get(START_POSITION_OF_MATERIALS));
        materialsPresenter.set(START_POSITION_OF_MATERIALS, materialPresenter);

        materialsPresenter.get(position).setMaterialPosition(
                materialsPresenter.get(START_POSITION_OF_MATERIALS).getMaterialPosition());
        materialsPresenter.get(START_POSITION_OF_MATERIALS)
                .setMaterialPosition(materialPresenter.getMaterialPosition());

        materialsPresenter.get(position).setMaterial(
                materialsPresenter.get(START_POSITION_OF_MATERIALS).getMaterial());
        materialsPresenter.get(START_POSITION_OF_MATERIALS)
                .setMaterial(materialPresenter.getMaterial());
    }

    public void onBindMaterialRowViewAtPosition(@NonNull BaseRowPresenter baseRowPresenter,
                                                int position) {
        baseRowPresenter.setName(materials.get(position).getName());
        documentService = new DocumentService();

        final String format = materials.get(position).getFormat();
        switch (format) {
            case Formats.PDF:
                baseRowPresenter.setImageResource(R.mipmap.ic_pdf_foreground);
                break;
            case Formats.DOCX : {
                baseRowPresenter.setImageResource(R.mipmap.ic_word_foreground);
                break;
            }
        }
    }

    public int getMaterialsSize() {
        return materials.size();
    }

    public void setListContent(List<Material> materials) {
        materialsPresenter.clear();
        this.materials = materials;
        filterMaterials = materials;
        addToMaterialPresenters();
    }

    public void removeAt(MaterialPresenter materialPresenter) {
        int position = materialPresenter.getMaterialPosition();
        materialsPresenter.remove(position);
        materials.remove(position);
        materialViewHolderAdapter.notifyDataSetChanged();
        materialViewHolderAdapter.notifyItemMoved(
                START_POSITION_OF_MATERIALS, materials.size());
    }

    public void updateAt(MaterialPresenter materialPresenter, String newName) {
        int position = materialPresenter.getMaterialPosition();
        final Material material = materialPresenter.getMaterial();
        material.setName(newName);
        materials.set(position, material);
        materialsPresenter.set(position, materialPresenter);
        materialViewHolderAdapter.notifyDataSetChanged();
        materialViewHolderAdapter.notifyItemMoved(
                START_POSITION_OF_MATERIALS, materials.size());
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public List<Material> getFilterMaterials() {
        return filterMaterials;
    }

    public List<MaterialPresenter> getMaterialsPresenter() {
        return materialsPresenter;
    }

    public void setMaterialViewHolderAdapter(
            RecyclerView.Adapter<MaterialViewHolder> materialViewHolderAdapter) {
        this.materialViewHolderAdapter = materialViewHolderAdapter;
    }

    public RecyclerView.Adapter<MaterialViewHolder> getMaterialViewHolderAdapter() {
        return materialViewHolderAdapter;
    }

    private void addToMaterialPresenters() {
        for (Material material : this.materials) {
            final MaterialPresenter materialPresenter = new MaterialPresenter();
            materialPresenter.setMaterialListPresenter(this);
            materialPresenter.setMaterial(material);
            materialsPresenter.add(materialPresenter);
        }
    }

}
