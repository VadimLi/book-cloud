package com.example.vadim.books_sync.presenters;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.basePresenters.BaseRowPresenter;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.presenters.services.DocumentService;
import com.example.vadim.books_sync.views.FolderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FolderListPresenter {

    private List<Folder> filterMaterialsOrFolders = new ArrayList<>();

    private DocumentService documentService;

    private List<FolderPresenter> foldersPresenter = new ArrayList<>();

    private List<Folder> folders = new ArrayList<>();

    private RecyclerView.Adapter<FolderViewHolder> folderViewHolderAdapter;

    private final static int START_POSITION_OF_MATERIALS = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openFolderOrMaterial(View view, int position) {
        final Folder folder = folders.get(position);
       // if (folder.getName().equals())
    }

    public void onBindMaterialRowViewAtPosition(@NonNull BaseRowPresenter baseRowPresenter,
                                                int position) {
        final String nameFolder = folders.get(position).getName();
        final int rooFolder = folders.get(position).getRoot();
        baseRowPresenter.setName(nameFolder);
        if (rooFolder == 1) {
            baseRowPresenter.setImageResource(
                    R.mipmap.ic_move_to_folders_foreground);
        }
    }

    public int getMaterialsSize() {
        return folders.size();
    }

    public void setListContent(List<Folder> foldersOrMaterials) {
        foldersPresenter.clear();
        this.folders = foldersOrMaterials;
        filterMaterialsOrFolders = foldersOrMaterials;
    }

    public void removeAt(MaterialPresenter materialPresenter) {

    }

    public void updateAt(MaterialPresenter materialPresenter, String newName) {

    }

    public List<Folder> getFilterMaterialsOrFolders() {
        return filterMaterialsOrFolders;
    }

    public void setFilterMaterialsOrFolders(List<Folder> filterMaterialsOrFolders) {
        this.filterMaterialsOrFolders = filterMaterialsOrFolders;
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public List<FolderPresenter> getFoldersPresenter() {
        return foldersPresenter;
    }

    public void setFoldersPresenter(List<FolderPresenter> foldersPresenter) {
        this.foldersPresenter = foldersPresenter;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public RecyclerView.Adapter<FolderViewHolder> getFolderViewHolderAdapter() {
        return folderViewHolderAdapter;
    }

    public void setFolderViewHolderAdapter(
            RecyclerView.Adapter<FolderViewHolder> folderViewHolderAdapter) {
        this.folderViewHolderAdapter = folderViewHolderAdapter;
    }

}
