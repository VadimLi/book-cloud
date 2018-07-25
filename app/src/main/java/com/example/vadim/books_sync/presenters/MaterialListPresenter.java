package com.example.vadim.books_sync.presenters;


import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.services.DocumentService;
import com.example.vadim.books_sync.presenters.services.Format;
import com.example.vadim.books_sync.viewPresenters.MaterialRowView;
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
        final Material material = materials.get(position);
        documentService.openDocumentByPath(view, material);
    }

    public void onBindMaterialRowViewAtPosition(@NonNull MaterialRowView materialRowView, int position) {
        materialRowView.setName(materials.get(position).getName());
        documentService = new DocumentService();
        final String pdfFormat = Format.PDF.getFormat();
        if (materials.get(position).getFormat().equals(pdfFormat)) {
            materialRowView.setImageResource(R.mipmap.ic_pdf_foreground);
        } else {
            materialRowView.setImageResource(R.mipmap.ic_word_foreground);
        }
    }

    public int getMaterialsSize() {
        return materials.size();
    }

    public int getFilterMaterialsSize() {
        return filterMaterials.size();
    }

    public void setListContent(List<Material> materials) {
        this.materials.clear();
        materialsPresenter.clear();
        this.materials.addAll(materials);
        for (Material material : this.materials) {
            final MaterialPresenter materialPresenter = new MaterialPresenter();
            materialPresenter.setMaterialListPresenter(this);
            materialPresenter.setMaterial(material);
            materialsPresenter.add(materialPresenter);
        }
        this.filterMaterials = materials;
    }

    public void removeAt(int position) {
        materials.remove(position);
        materialViewHolderAdapter.notifyItemRemoved(position);
        materialViewHolderAdapter.notifyItemRangeChanged(START_POSITION_OF_MATERIALS,
                materials.size());

    }

    public void addMaterialByFilter(CharSequence constraint, List<Material> filters) {
        for (Material material : filterMaterials) {
            if (material.getName().toLowerCase()
                    .contains(constraint)) {
                filters.add(material);
            }
        }
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

    public void setMaterialViewHolderAdapter(RecyclerView.Adapter<MaterialViewHolder> materialViewHolderAdapter) {
        this.materialViewHolderAdapter = materialViewHolderAdapter;
    }

}
