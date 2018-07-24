package com.example.vadim.books_sync.presenters;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.adapter.properties_dialog.PropertiesDialog;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.services.Format;
import com.example.vadim.books_sync.viewPresenters.MaterialRowView;
import com.example.vadim.books_sync.views.MaterialViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MaterialListPresenter implements MaterialRowView {

    private final List<Material> materials = new ArrayList<>();

    private List<Material> filterMaterials = new ArrayList<>();

    private PropertiesDialog propertiesDialog;

    public void attachDialog(PropertiesDialog propertiesDialog) {
        this.propertiesDialog = propertiesDialog;
    }

    public void detachDialog() {
        propertiesDialog = null;
    }

    public void onBindViewHolder(@NonNull MaterialViewHolder materialViewHolder, int position) {
        Material material = materials.get(position);
        materialViewHolder.setNameMaterial(material.getName());
        final String pdfFormat = Format.PDF.getFormat();
        if (material.getFormat().equals(pdfFormat)) {
            materialViewHolder.setImageResource(R.mipmap.ic_pdf_foreground);
        } else {
            materialViewHolder.setImageResource(R.mipmap.ic_word_foreground);
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
        this.materials.addAll(materials);
        this.filterMaterials = materials;
    }

    public void removeAt(int position) {
        materials.remove(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openDocumentByPath(View view, int position) {
        final Material material = materials.get(position);
        final File file = new File(material.getPath());
        if (file.exists()) {
            final Intent formatIntent = new Intent(Intent.ACTION_VIEW);
            formatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = FileProvider.getUriForFile(view.getContext(),
                    view.getContext().getApplicationContext()
                            .getPackageName() +
                            ".provider", file);
            formatIntent.setData(uri);
            formatIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            view.getContext().startActivity(formatIntent);
        } else {
            Log.e("File not exists : ", file.getAbsolutePath());
        }
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

}
