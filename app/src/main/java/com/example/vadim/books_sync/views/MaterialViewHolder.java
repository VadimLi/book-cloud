package com.example.vadim.books_sync.views;


import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.adapter.properties_dialog.PropertiesDialog;
import com.example.vadim.books_sync.presenters.MaterialListPresenter;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.viewPresenters.MaterialRowView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaterialViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener, MaterialRowView {

    @BindView(R.id.formatMaterial)
    ImageView formatMaterial;

    @BindView(R.id.nameMaterial)
    TextView nameMaterial;

    private MaterialListPresenter materialListPresenter;

    @Inject
    public MaterialViewHolder(View itemView, MaterialListPresenter materialListPresenter) {
        super(itemView);
        this.materialListPresenter = materialListPresenter;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        final int position = getAdapterPosition();
        materialListPresenter.openDocumentByPath(view, position);
    }

    @Override
    public boolean onLongClick(View view) {
        final int position = getAdapterPosition();
        final PropertiesDialog propertiesDialog = new PropertiesDialog();
        final MaterialPresenter materialPresenter = materialListPresenter
                .getMaterialsPresenter().get(position);
        materialPresenter.setMaterialPosition(position);

        propertiesDialog.setMaterialPresenter(materialPresenter);
        final FragmentManager fragmentManager =
                ((Activity) view.getContext()).getFragmentManager();
        propertiesDialog.show(fragmentManager, "properties dialog");
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

}
