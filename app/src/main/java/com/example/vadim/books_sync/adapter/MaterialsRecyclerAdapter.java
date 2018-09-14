package com.example.vadim.books_sync.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.MaterialListPresenter;
import com.example.vadim.books_sync.views.MaterialViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MaterialsRecyclerAdapter extends RecyclerView.Adapter<MaterialViewHolder>
        implements Filterable {

    private LayoutInflater layoutInflater;

    private final MaterialListPresenter materialListPresenter;

    public MaterialsRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        materialListPresenter = new MaterialListPresenter();
        materialListPresenter.setMaterialViewHolderAdapter(this);
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(R.layout.material, parent, false);
        return new MaterialViewHolder(view, materialListPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        materialListPresenter.onBindMaterialRowViewAtPosition(holder, position);
    }

    public void setListContent(List<Material> materials) {
        materialListPresenter.setListContent(materials);
        notifyItemRangeChanged(MaterialListPresenter.START_POSITION_OF_MATERIALS,
                materials.size());
    }

    @Override
    public int getItemCount() {
        return materialListPresenter.getMaterialsSize();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults results = new FilterResults();
                final List<Material> filterMaterials = materialListPresenter.getFilterMaterials();
                if (constraint != null && constraint.length() > 0) {
                    constraint = constraint.toString().toLowerCase();
                    final List<Material> filters = new ArrayList<>();
                    for (Material material : filterMaterials) {
                        if (material.getName().toLowerCase()
                                .contains(constraint)) {
                            filters.add(material);
                        }
                    }
                    results.count = filters.size();
                    results.values = filters;
                } else {
                    results.count = filterMaterials.size();
                    results.values = filterMaterials;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence,
                                          FilterResults filterResults) {
                materialListPresenter.setMaterials((List<Material>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

}