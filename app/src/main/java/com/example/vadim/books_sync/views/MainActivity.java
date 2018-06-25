package com.example.vadim.books_sync.views;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.views.adapter.MaterialAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    private MaterialPresenter materialPresenter;

    private final static ArrayList<Material> outputMaterials = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_WRITE_EXTERNAL_STORAGE);

        materialPresenter = new MaterialPresenter(this);

        final Button syncButton = findViewById(R.id.btnSync);
        syncButton.setOnClickListener(e -> {
            showListView();
        });
    }

    private void showListView() {
        materialPresenter.addMaterialFiles();
        final MaterialAdapter materialAdapter = new MaterialAdapter(this, outputMaterials);
        Log.d("materials - ", outputMaterials.toString());

        ListView lvMain = findViewById(R.id.lvMain);
        lvMain.setAdapter(materialAdapter);
    }

    public void fillData(List<Material> inputMaterials) {
        for (Material material : inputMaterials) {
            Material newMaterial = new Material();
            newMaterial.setName(material.getName());
            newMaterial.setFormat(material.getFormat());
            newMaterial.setPath(material.getPath());
            outputMaterials.add(newMaterial);
        }
    }

}
