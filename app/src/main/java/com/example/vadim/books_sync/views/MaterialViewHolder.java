package com.example.vadim.books_sync.views;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.viewPresenters.MaterialRowView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaterialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    @BindView(R.id.formatMaterial)
    ImageView formatMaterial;

    @BindView(R.id.nameMaterial)
    TextView nameMaterial;

    private MaterialRowView materialRowView;

    @Inject
    public MaterialViewHolder(View itemView,
                              MaterialRowView materialRowView) {
        super(itemView);
        this.materialRowView = materialRowView;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
    }

    @Override
    public boolean onLongClick(View view) {
//        int position = getAdapterPosition();
//        final FragmentManager fragmentManager = ((Activity) view.getContext()).getFragmentManager();
//        final PropertiesDialog propertiesDialog = new PropertiesDialog();
//        propertiesDialog.show(fragmentManager, "properties dialog");
        return true;
    }

//    private Material findMaterialByPosition(int position) {
////        return materialListPresenter.getMaterials().get(position);
//    }

    public void setNameMaterial(String name) {
        nameMaterial.setText(name);
    }

    public void setImageResource(int resourceId) {
        formatMaterial.setImageResource(resourceId);
    }

}
