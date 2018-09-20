package com.example.vadim.books_sync.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.adapter.FoldersRecyclerAdapter;
import com.example.vadim.books_sync.dagger.AppModule;
import com.example.vadim.books_sync.dagger.DaggerAppComponent;
import com.example.vadim.books_sync.dagger.RoomModule;
import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.model.Folder;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("Registered")
public class SelectorFolderActivity extends AppCompatActivity {

    @BindView(R.id.btnPropertiesForFile)
    ImageButton btnPropertiesForFile;

    @BindView(R.id.btnCreatorFolder)
    ImageButton btnCreatorFolder;

    @BindView(R.id.folder_list)
    RecyclerView recyclerView;

    @Inject
    FolderDao folderDao;

    private FoldersRecyclerAdapter foldersRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_folder);
        ButterKnife.bind(this);
        DaggerAppComponent.builder()
                .appModule(new AppModule(
                        Objects.requireNonNull(this)
                                .getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .injectSelectorFolderActivity(this);

        createFolderAdapter();
        final List<Folder> folders = folderDao.findAllWithoutFormats();
        foldersRecyclerAdapter.setListContent(folders);
        recyclerView.setAdapter(foldersRecyclerAdapter);

        btnPropertiesForFile.setOnClickListener(v -> {
            final Intent booksIntent = new Intent(this,
                    PropertiesDialogForMaterials.class);
            setResult(RESULT_OK, booksIntent);
            finish();
        });

    }

    private void createFolderAdapter() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        foldersRecyclerAdapter = new FoldersRecyclerAdapter(this);
        recyclerView.setAdapter(foldersRecyclerAdapter);
    }

}
