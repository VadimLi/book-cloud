package com.example.vadim.books_sync.views;


import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.databinding.MaterialBinding;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.views.adapter.ActionCallback;
import com.example.vadim.books_sync.views.adapter.DataBoundAdapter;
import com.example.vadim.books_sync.views.adapter.DataBoundViewHolder;

import java.util.List;

public class MaterialAdapter extends DataBoundAdapter<MaterialBinding> {

    private final ActionCallback actionCallback;

    private MaterialPresenter materialPresenter;

    public MaterialAdapter(final ActionCallback actionCallback) {
        super(R.layout.material);
        this.actionCallback = actionCallback;
        materialPresenter = new MaterialPresenter();
    }

    @Override
    protected void bindItem(DataBoundViewHolder<MaterialBinding> holder,
                            int position,
                            List<Object> payloads) {
        holder.getBinding().setMaterialPresenter(materialPresenter);
        holder.getBinding().setCallback(actionCallback);
    }

    @Override
    public int getItemCount() {
        return materialPresenter.getMaterials().size();
    }

}
