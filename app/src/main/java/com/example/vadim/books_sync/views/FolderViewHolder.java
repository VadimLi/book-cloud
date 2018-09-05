package com.example.vadim.books_sync.views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.basePresenters.BaseRowPresenter;
import com.example.vadim.books_sync.presenters.FolderListPresenter;

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

    public FolderViewHolder(View itemView,
                            FolderListPresenter folderListPresenter) {
        super(itemView);
        this.folderListPresenter = folderListPresenter;
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
        folderListPresenter.openFolderOrMaterial(view, position);
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    public boolean onLongClick(View view) {
        changeBackgroundResource(view);
        return true;
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
