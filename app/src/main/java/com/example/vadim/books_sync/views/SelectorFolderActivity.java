package com.example.vadim.books_sync.views;

import android.annotation.SuppressLint;
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
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.FolderPresenter;
import com.example.vadim.books_sync.presenters.MaterialPresenter;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("Registered")
public class SelectorFolderActivity extends AppCompatActivity
        implements ActivityView, ActivityView.ActivityViewForFolders {

    @BindView(R.id.searchFolders)
    SearchView searchFolders;

    @BindView(R.id.btnPropertiesForFile)
    ImageButton btnPropertiesForFile;

    @BindView(R.id.btnCreatorFolder)
    ImageButton imageButtonCreatorFolder;

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
        createAdapter();
        final List<Folder> folders = folderDao.findAllWithoutFormats();
        final Bundle materialBundle = (getIntent()).getExtras();
        if (materialBundle != null) {
            final Material addingMaterial = materialBundle.getParcelable("material");
            final MaterialPresenter materialPresenter = new MaterialPresenter();
            materialPresenter.setMaterial(addingMaterial);
            foldersRecyclerAdapter.setMaterialPresenter(materialPresenter);
            foldersRecyclerAdapter.setListContent(folders);
            recyclerView.setAdapter(foldersRecyclerAdapter);
        }
        btnPropertiesForFile.setOnClickListener(v -> {
            final Intent booksIntent = new Intent(this,
                    PropertiesDialogForMaterials.class);
            setResult(RESULT_OK, booksIntent);
            finish();
        });
        addClickListenerForCreatorFolder();
        addQueryTextListener();
    }

    @Override
    public void addClickListenerForCreatorFolder() {
        imageButtonCreatorFolder.setOnClickListener(v -> {
            final AddingFolderDialog addingFolderDialog =
                    new AddingFolderDialog();
            final FolderPresenter folderPresenter = new FolderPresenter();
            addingFolderDialog.setFolderPresenter(folderPresenter);
            folderPresenter.setFolderListPresenter(foldersRecyclerAdapter.getFolderListPresenter());
            final FragmentManager fragmentManager = getSupportFragmentManager();
            addingFolderDialog.show(fragmentManager, "dialog for adding folder");
        });
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

    @Override
    public void createAdapter() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        foldersRecyclerAdapter = new FoldersRecyclerAdapter(this);
        recyclerView.setAdapter(foldersRecyclerAdapter);
    }

}
