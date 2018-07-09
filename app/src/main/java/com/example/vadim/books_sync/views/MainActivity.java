package com.example.vadim.books_sync.views;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.dagger.AppModule;
import com.example.vadim.books_sync.dagger.DaggerAppComponent;
import com.example.vadim.books_sync.dagger.RoomModule;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.databinding.ActivityMainBinding;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.views.adapter.ActionCallback;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    @Inject
    MaterialDao materialDao;

    @Inject
    MaterialPresenter materialPresenter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_WRITE_EXTERNAL_STORAGE);

        final ActivityMainBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);

        materialPresenter.attachView(this);
        showListView(binding);
        final ImageButton syncButton = findViewById(R.id.btnSync);
        syncButton.setOnClickListener(e -> materialPresenter.addMaterialFiles(binding));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showListView(ActivityMainBinding binding) {
        List<Material> materials = materialDao.findAll();
        final ActionCallback actionCallback = material -> {
            String path = material.getPath();
            openDocumentByPath(path);
        };
        final MaterialAdapter materialAdapter =
                new MaterialAdapter(actionCallback);
        binding.materialList.setAdapter(materialAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openDocumentByPath(String path) {
        final File file = new File(path);
        if (file.exists()) {
            final Intent formatIntent = new Intent(Intent.ACTION_VIEW);
            formatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = FileProvider.getUriForFile(this,
                    getApplicationContext()
                            .getPackageName() +
                            ".provider", file);
            formatIntent.setData(uri);
            formatIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(formatIntent);
        } else {
            Log.e("File not exists : ", file.getAbsolutePath());
        }
    }

}

