package com.example.vadim.books_sync.adapter.properties_dialog;


import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.model.Material;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class PropertiesDialog extends DialogFragment {

    @BindView(R.id.fileName)
    TextView fileNameTextView;

    @BindView(R.id.rename)
    ImageButton renameImageButton;

    @BindView(R.id.addToFolder)
    ImageButton addToFolderImageButton;

    @BindView(R.id.trash)
    ImageButton trashImageButton;

    @BindView(R.id.share)
    ImageButton shareImageButton;

    private MaterialDao materialDao;

    private Material material;

    @Inject
    public PropertiesDialog(MaterialDao materialDao) {
        this.materialDao = materialDao;
    }

    @SuppressLint("InflateParams")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewProperties = inflater.inflate(R.layout.dialog_properties, null);
        ButterKnife.bind(this, viewProperties);
        fileNameTextView.setText(material.getName());

        renameImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("state ", "rename");
            }
        });

        addToFolderImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("state ", "add to folder");
            }
        });

        trashImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("state ", "trash");
            }
        });

        shareImageButton.setOnClickListener(v -> {
            File file = new File(material.getPath());
            if (file.exists()) {
                final Uri uriToFile = Uri.fromFile(file);
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToFile);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, material.getName()));
            }
        });

        return viewProperties;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

}
