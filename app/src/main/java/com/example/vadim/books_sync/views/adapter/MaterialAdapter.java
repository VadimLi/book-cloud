package com.example.vadim.books_sync.views.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.model.Material;

import java.util.List;

public class MaterialAdapter extends BaseAdapter {

    private List<Material> materials;

    private final LayoutInflater layoutInflater;

    public MaterialAdapter(final Context context,
                           final List<Material> materials) {
        this.materials = materials;
        layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return materials.size();
    }

    @Override
    public Object getItem(int position) {
        return materials.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.material, parent, false);
        }
        Material material = getMaterial(position);
        ((TextView) view.findViewById(R.id.nameMaterial)).setText(material.getName());
        ((TextView) view.findViewById(R.id.formatMaterial)).setText(material.getFormat());

        return view;
    }

    private Material getMaterial(int position) {
        return (Material) getItem(position);
    }

}


