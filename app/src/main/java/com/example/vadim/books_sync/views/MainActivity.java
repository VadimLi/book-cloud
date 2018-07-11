package com.example.vadim.books_sync.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.adapter.MaterialAdapter;
import com.example.vadim.books_sync.dagger.AppModule;
import com.example.vadim.books_sync.dagger.DaggerAppComponent;
import com.example.vadim.books_sync.dagger.RoomModule;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.mvp.MaterialsMvpView;
import com.example.vadim.books_sync.presenters.MaterialPresenter;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MaterialsMvpView {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    @Inject
    MaterialDao materialDao;

    @Inject
    MaterialPresenter materialPresenter;

    private RecyclerView recyclerView;

    private MaterialAdapter materialAdapter;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_WRITE_EXTERNAL_STORAGE);

        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);
        materialPresenter.attachView(this);

        recyclerView = findViewById(R.id.material_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        materialAdapter = new MaterialAdapter(this);
        final List<Material> materials = materialDao.findAll();
        materialAdapter.setListContent(materials);
        recyclerView.setAdapter(materialAdapter);

        final ImageButton syncButton = findViewById(R.id.btnSync);
        final LinkedList<Material> newMaterialList = new LinkedList<>();
        materials.forEach(newMaterialList::addLast);
        syncButton.setOnClickListener(e ->
                materialPresenter.addMaterialFiles(newMaterialList));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void addMaterialFiles(LinkedList<Material> materials) {
        materialAdapter.setListContent(materials);
        recyclerView.setAdapter(materialAdapter);
        materialAdapter.notifyDataSetChanged();
    }

}

