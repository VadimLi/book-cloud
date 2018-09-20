package com.example.vadim.books_sync.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.ImageButton;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.adapter.FoldersRecyclerAdapter;
import com.example.vadim.books_sync.dagger.AppModule;
import com.example.vadim.books_sync.dagger.DaggerAppComponent;
import com.example.vadim.books_sync.dagger.RoomModule;
import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.presenters.FolderPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoldersActivity extends AppCompatActivity implements ActivityView {

    @BindView(R.id.btnCreatorFolder)
    ImageButton imageButtonCreatorFolder;

    @BindView(R.id.btnFiles)
    ImageButton imageButtonFiles;

    @BindView(R.id.searchFolders)
    SearchView searchFolders;

    @BindView(R.id.folder_list)
    RecyclerView recyclerView;

    @Inject
    FolderDao folderDao;

    @Inject
    MaterialDao materialDao;

    private FoldersRecyclerAdapter foldersRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);
        ButterKnife.bind(this);

        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .injectSelectorFolderActivity(this);

        createFolderAdapter();
        final List<Folder> folders = folderDao.findAll();
        foldersRecyclerAdapter.setListContent(folders);
        recyclerView.setAdapter(foldersRecyclerAdapter);

        imageButtonFiles.setOnClickListener(v -> {
            final Intent booksIntent = new Intent(this, MainActivity.class);
            setResult(RESULT_OK, booksIntent);
            finish();
        });

        imageButtonCreatorFolder.setOnClickListener(v -> {
            final AddFolderDialog addFolderDialog =
                    new AddFolderDialog();
            final FolderPresenter folderPresenter = new FolderPresenter();
            addFolderDialog.setFolderPresenter(folderPresenter);
            folderPresenter.setFolderListPresenter(foldersRecyclerAdapter.getFolderListPresenter());
            final FragmentManager fragmentManager = getSupportFragmentManager();
            addFolderDialog.show(fragmentManager, "dialog for adding folder");
        });

    }

    private void createFolderAdapter() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        foldersRecyclerAdapter = new FoldersRecyclerAdapter(this);
        recyclerView.setAdapter(foldersRecyclerAdapter);
    }

    @Override
    public void addQueryTextListener() {
        searchFolders.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                foldersRecyclerAdapter.getFilter().filter(newText);
                return false;
            }

        });
    }

}
