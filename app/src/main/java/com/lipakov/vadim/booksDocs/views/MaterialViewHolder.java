package com.lipakov.vadim.booksDocs.views;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lipakov.vadim.booksDocs.R;
import com.lipakov.vadim.booksDocs.basePresenters.BaseRowPresenter;
import com.lipakov.vadim.booksDocs.presenters.FolderPresenter;
import com.lipakov.vadim.booksDocs.presenters.MaterialListPresenter;
import com.lipakov.vadim.booksDocs.presenters.MaterialPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaterialViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener,
        BaseRowPresenter {

    @BindView(R.id.formatMaterial)
    ImageView formatMaterial;

    @BindView(R.id.nameMaterial)
    TextView nameMaterial;

    private final MaterialListPresenter materialListPresenter;

    private final FolderPresenter folderPresenter;

    public MaterialViewHolder(final View itemView,
                              final MaterialListPresenter materialListPresenter,
                              final FolderPresenter folderPresenter) {
        super(itemView);
        this.materialListPresenter = materialListPresenter;
        this.folderPresenter = folderPresenter;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        changeBackgroundResource(view);
        view.setBackgroundResource(R.color.colorItemFile);
        new Handler().postDelayed(() ->
                view.setBackgroundResource(Color.TRANSPARENT), 500);
        final int position = getAdapterPosition();
        materialListPresenter.openDocumentByPath(view, position);
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    public boolean onLongClick(View view) {
        changeBackgroundResource(view);
        final int position = getAdapterPosition();
        final MaterialPresenter materialPresenter = materialListPresenter
                .getMaterialsPresenter().get(position);
        final PropertiesDialogForMaterials propertiesDialogForMaterials =
                new PropertiesDialogForMaterials();
        materialPresenter.setFolderPresenter(folderPresenter);
        materialPresenter.setMaterialPosition(position);
        materialPresenter.setMaterialListPresenter(materialListPresenter);
        propertiesDialogForMaterials.setMaterialPresenter(materialPresenter);

        final FragmentManager fragmentManager =
                ((FragmentActivity) view.getContext()).getSupportFragmentManager();
        propertiesDialogForMaterials.show(fragmentManager, "properties dialog for files");
        return true;
    }

    @Override
    public void setName(String name) {
        nameMaterial.setText(name);
    }

    @Override
    public void setImageResource(int resourceId) {
        formatMaterial.setImageResource(resourceId);
    }

    @SuppressLint("ResourceType")
    private void changeBackgroundResource(View view) {
        final int delay = 500;
        view.setBackgroundResource(R.color.colorItemFile);
        new Handler().postDelayed(() ->
                view.setBackgroundResource(Color.TRANSPARENT), delay);
    }

}
