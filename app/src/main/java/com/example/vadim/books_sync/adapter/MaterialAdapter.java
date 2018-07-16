package com.example.vadim.books_sync.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.services.Format;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MyViewHolder>
        implements Filterable {

    private static final int START_POSITION_OF_MATERIALS = 0;

    private List<Material> materials = new ArrayList<>();

    private List<Material> filterMaterials = new ArrayList<>();

    private final LayoutInflater layoutInflater;

    private final Context context;

    private View view;

    public MaterialAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.material, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Material material = materials.get(position);
        holder.nameMaterial.setText(material.getName());
        final String pdfFormat = Format.PDF.getFormat();
        if (material.getFormat().equals(pdfFormat)) {
            holder.formatMaterial.setImageResource(R.mipmap.ic_pdf_foreground);
        } else {
            holder.formatMaterial.setImageResource(R.mipmap.ic_word_foreground);
        }
    }

    public void setListContent(List<Material> materials){
        this.materials = materials;
        this.filterMaterials = materials;
        notifyItemRangeChanged(START_POSITION_OF_MATERIALS, materials.size());
    }

    public void removeAt(int position) {
        materials.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(START_POSITION_OF_MATERIALS, materials.size());
    }

    @Override
    public int getItemCount() {
        return materials.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
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
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                materials = (List<Material>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.formatMaterial)
        ImageView formatMaterial;

        @BindView(R.id.nameMaterial)
        TextView nameMaterial;

        MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            final Material material = materials.get(position);
            MaterialAdapter.this.openDocumentByPath(material.getPath());
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openDocumentByPath(String path) {
        final File file = new File(path);
        if (file.exists()) {
            final Intent formatIntent = new Intent(Intent.ACTION_VIEW);
            formatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = FileProvider.getUriForFile(context,
                    context.getApplicationContext()
                            .getPackageName() +
                            ".provider", file);
            formatIntent.setData(uri);
            formatIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(formatIntent);
        } else {
            Log.e("File not exists : ", file.getAbsolutePath());
        }
    }

}
