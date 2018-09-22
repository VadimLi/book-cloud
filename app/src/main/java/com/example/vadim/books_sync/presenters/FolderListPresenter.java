package com.example.vadim.books_sync.presenters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.basePresenters.BaseRowPresenter;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.views.FolderViewHolder;
import com.example.vadim.books_sync.views.MaterialsOfFolderActivity;

import java.util.ArrayList;
import java.util.List;

public class FolderListPresenter {

    private List<Folder> filterFolders = new ArrayList<>();

    private List<FolderPresenter> foldersPresenter = new ArrayList<>();

    private List<Folder> folders = new ArrayList<>();

    private RecyclerView.Adapter<FolderViewHolder> folderViewHolderAdapter;

    private final static int START_POSITION_OF_FOLDERS = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openFolder(View view, int position) {
        final Folder folder = folders.get(position);
        final Intent foldersIntent =
                new Intent(view.getContext(), MaterialsOfFolderActivity.class);
        foldersIntent.putExtra("folder", folder);
        ((Activity) view.getContext()).startActivityForResult(foldersIntent, 1);
    }

    public void onBindMaterialRowViewAtPosition(@NonNull BaseRowPresenter baseRowPresenter,
                                                int position) {
        final String nameFolder = folders.get(position).getName();
        baseRowPresenter.setName(nameFolder);
        baseRowPresenter.setImageResource(
                R.mipmap.ic_move_to_folders_foreground);
    }

    public int getFoldersSize() {
        return folders.size();
    }

    public void setListContent(List<Folder> folders) {
        foldersPresenter.clear();
        this.folders = folders;
        filterFolders = folders;
        addToMaterialPresenters();
    }

    public void updateAt(FolderPresenter folderPresenter, String newName) {
        int position = folderPresenter.getFolderPosition();
        final Folder folder = folderPresenter.getFolder();
        folder.setName(newName);
        folders.set(position, folder);
        foldersPresenter.set(position, folderPresenter);
        folderViewHolderAdapter.notifyDataSetChanged();
        folderViewHolderAdapter.notifyItemMoved(
                START_POSITION_OF_FOLDERS, folders.size());
    }

    public void removeAt(FolderPresenter folderPresenter) {
        int position = folderPresenter.getFolderPosition();
        foldersPresenter.remove(position);
        folders.remove(position);
        folderViewHolderAdapter.notifyDataSetChanged();
        folderViewHolderAdapter.notifyItemMoved(
                START_POSITION_OF_FOLDERS, folders.size());
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<Folder> getFilterFolders() {
        return filterFolders;
    }

    public void setMaterialViewHolderAdapter(
            RecyclerView.Adapter<FolderViewHolder> folderViewHolderAdapter) {
        this.folderViewHolderAdapter = folderViewHolderAdapter;
    }

    public List<FolderPresenter> getFoldersPresenter() {
        return foldersPresenter;
    }

    private void addToMaterialPresenters() {
        for (Folder folder : this.folders) {
            final FolderPresenter folderPresenter = new FolderPresenter();
            folderPresenter.setFolderListPresenter(this);
            folderPresenter.setFolder(folder);
            foldersPresenter.add(folderPresenter);
        }
    }

    public void addNewFolder(final FolderPresenter folderPresenter) {
        this.folders.add(folderPresenter.getFolder());
        this.foldersPresenter.add(folderPresenter);
        folderViewHolderAdapter.notifyDataSetChanged();
        folderViewHolderAdapter.notifyItemMoved(
                START_POSITION_OF_FOLDERS, folders.size());
    }



}
