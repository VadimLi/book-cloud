package com.example.vadim.books_sync.views;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.dagger.AppModule;
import com.example.vadim.books_sync.dagger.DaggerAppComponent;
import com.example.vadim.books_sync.dagger.RoomModule;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.views.adapter.MaterialAdapter;

import java.util.List;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    @Inject
    MaterialPresenter materialPresenter;

    @Inject
    MaterialDao materialDao;

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

        showListView();
        final Button syncButton = findViewById(R.id.btnSync);
        syncButton.setOnClickListener(e -> materialPresenter.addMaterialFiles());
    }

    public void showListView() {
        final List<Material> materialList = materialDao.findAll();
        final MaterialAdapter materialAdapter = new MaterialAdapter(this, materialList);

        ListView lvMain = findViewById(R.id.lvMain);
        lvMain.setAdapter(materialAdapter);
    }

}
