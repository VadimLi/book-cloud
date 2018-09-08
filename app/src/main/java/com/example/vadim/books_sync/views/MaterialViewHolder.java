package com.example.vadim.books_sync.views;


import android.annotation.SuppressLint;
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

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.basePresenters.BaseRowPresenter;
import com.example.vadim.books_sync.presenters.MaterialListPresenter;
import com.example.vadim.books_sync.presenters.MaterialPresenter;

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

    public MaterialViewHolder(View itemView,
                              MaterialListPresenter materialListPresenter) {
        super(itemView);
        this.materialListPresenter = materialListPresenter;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        ButterKnife.bind(this, itemView);
    }

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
        final PropertiesDialogForMaterials propertiesDialogForMaterials = new PropertiesDialogForMaterials();
        final MaterialPresenter materialPresenter = materialListPresenter
                .getMaterialsPresenter().get(position);
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
    public void changeBackgroundResource(View view) {
        final int delay = 500;
        view.setBackgroundResource(R.color.colorItemFile);
        new Handler().postDelayed(() ->
                view.setBackgroundResource(Color.TRANSPARENT), delay);
    }

}
