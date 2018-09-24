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
import com.example.vadim.books_sync.presenters.FolderListPresenter;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.MaterialPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FolderViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener,
        BaseRowPresenter {

    @BindView(R.id.formatMaterialOrFolder)
    ImageView formatMaterialOrFolder;

    @BindView(R.id.nameMaterialOrFolder)
    TextView nameMaterialOrFolder;

    private final FolderListPresenter folderListPresenter;

    private final MaterialPresenter materialPresenter;

    public FolderViewHolder(final View itemView,
                            final FolderListPresenter folderListPresenter,
                            final MaterialPresenter materialPresenter) {
        super(itemView);
        this.folderListPresenter = folderListPresenter;
        this.materialPresenter = materialPresenter;
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
        if (view.getContext() instanceof FoldersActivity) {
            folderListPresenter.openFolder(view, position);
        } else {
            final FolderPresenter folderPresenter = folderListPresenter
                    .getFoldersPresenter().get(position);
            folderPresenter.setMaterialPresenter(materialPresenter);
            final PropertiesDialogForFolders propertiesDialogForFolders =
                    new PropertiesDialogForFolders();
            propertiesDialogForFolders.setFolderPresenter(folderPresenter);
            final FragmentManager fragmentManager =
                    ((FragmentActivity) view.getContext()).getSupportFragmentManager();
            propertiesDialogForFolders.show(fragmentManager,
                    "properties dialog for folder");
        }
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    public boolean onLongClick(View view) {
        if (view.getContext() instanceof FoldersActivity) {
            final int position = getAdapterPosition();
            final FolderPresenter folderPresenter = folderListPresenter
                    .getFoldersPresenter().get(position);
            final PropertiesDialogForFolders propertiesDialogForFolders =
                    new PropertiesDialogForFolders();
            folderPresenter.setFolderPosition(position);
            folderPresenter.setFolderListPresenter(folderListPresenter);
            propertiesDialogForFolders.setFolderPresenter(folderPresenter);
            changeBackgroundResource(view);
            final List<String> formatList =
                    ((FoldersActivity) view.getContext()).materialDao.
                            findDistinctNameByFormat(folderPresenter.getName());
            if ( formatList.isEmpty() ) {
                final FragmentManager fragmentManager =
                        ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                propertiesDialogForFolders.show(fragmentManager,
                        "properties dialog for folder");
            }
        }
        return false;
    }

    @Override
    public void setName(String name) {
        nameMaterialOrFolder.setText(name);
    }

    @Override
    public void setImageResource(int resourceId) {
        formatMaterialOrFolder.setImageResource(resourceId);
    }

    @SuppressLint("ResourceType")
    private void changeBackgroundResource(View view) {
        final int delay = 500;
        view.setBackgroundResource(R.color.colorItemFile);
        new Handler().postDelayed(() ->
                view.setBackgroundResource(Color.TRANSPARENT), delay);
    }

}
