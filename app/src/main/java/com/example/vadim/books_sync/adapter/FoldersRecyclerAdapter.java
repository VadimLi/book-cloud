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
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.presenters.FolderListPresenter;
import com.example.vadim.books_sync.presenters.MaterialListPresenter;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.views.FolderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FoldersRecyclerAdapter extends RecyclerView.Adapter<FolderViewHolder>
        implements Filterable {

    private LayoutInflater layoutInflater;

    private FolderListPresenter folderListPresenter;

    private MaterialPresenter materialPresenter;

    public MaterialPresenter getMaterialPresenter() {
        return materialPresenter;
    }

    public void setMaterialPresenter(MaterialPresenter materialPresenter) {
        this.materialPresenter = materialPresenter;
    }

    public FolderListPresenter getFolderListPresenter() {
        return folderListPresenter;
    }

    public void setFolderListPresenter(FolderListPresenter folderListPresenter) {
        this.folderListPresenter = folderListPresenter;
    }

    public FoldersRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        folderListPresenter = new FolderListPresenter();
        folderListPresenter.setMaterialViewHolderAdapter(this);
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(R.layout.file_system, parent, false);
        return new FolderViewHolder(view, folderListPresenter, materialPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        folderListPresenter.onBindMaterialRowViewAtPosition(holder, position);
    }

    public void setListContent(List<Folder> folders) {
        folderListPresenter.setListContent(folders);
        notifyItemRangeChanged(MaterialListPresenter.START_POSITION_OF_MATERIALS,
                folders.size());
    }

    @Override
    public int getItemCount() {
        return folderListPresenter.getFoldersSize();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults results = new FilterResults();
                final List<Folder> filterFolders = folderListPresenter.getFilterFolders();
                if (constraint != null && constraint.length() > 0) {
                    constraint = constraint.toString().toLowerCase();
                    final List<Folder> filters = new ArrayList<>();
                    for (Folder folder : filterFolders) {
                        if (folder.getName().toLowerCase()
                                .contains(constraint)) {
                            filters.add(folder);
                        }
                    }
                    results.count = filters.size();
                    results.values = filters;
                } else {
                    results.count = filterFolders.size();
                    results.values = filterFolders;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence,
                                          FilterResults filterResults) {
                folderListPresenter.setFolders((List<Folder>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

}
