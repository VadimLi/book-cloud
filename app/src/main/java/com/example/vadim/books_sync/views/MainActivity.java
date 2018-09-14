package com.example.vadim.books_sync.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.arch.persistence.room.Transaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.adapter.MaterialsRecyclerAdapter;
import com.example.vadim.books_sync.basePresenters.BaseMaterialsPresenter;
import com.example.vadim.books_sync.dagger.AppModule;
import com.example.vadim.books_sync.dagger.DaggerAppComponent;
import com.example.vadim.books_sync.dagger.RoomModule;
import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.dao.MaterialFolderJoinDao;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.model.MaterialFolderJoin;
import com.example.vadim.books_sync.presenters.MaterialsUpdaterPresenter;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    @BindView(R.id.material_list)
    RecyclerView recyclerView;

    @BindView(R.id.searchMaterials)
    SearchView searchMaterials;

    @BindView(R.id.progressBarLoadMaterials)
    ProgressBar progressBarLoadMaterials;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.btnFolders)
    ImageButton moveFolders;

    @Inject
    MaterialDao materialDao;

    @Inject
    FolderDao folderDao;

    @Inject
    MaterialFolderJoinDao materialFolderJoinDao;

    @Inject
    MaterialsUpdaterPresenter materialsUpdaterPresenter;

    private MaterialsRecyclerAdapter materialsRecyclerAdapter;

    private Bundle savedInstanceState;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(this.savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_WRITE_EXTERNAL_STORAGE);

        ButterKnife.bind(this);
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .injectMainActivity(this);

        createMaterialAdapter();
        final List<Material> materials = materialDao.findAll();
        final LinkedList<Material> materialLinkedList =
                convertToLinkedMaterialList(materials);
        
        setProgressBarLoadMaterials(materialLinkedList);
        moveFolders.setOnClickListener(v -> {
            final Intent foldersIntent =
                    new Intent(this, FoldersActivity.class);
            materialsUpdaterPresenter.getUpdaterAdapter().interrupt();
            swipeContainer.setRefreshing(false);
            startActivityForResult(foldersIntent, 1);
        });

        searchMaterials.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                materialsRecyclerAdapter.getFilter().filter(newText);
                return false;
            }

        });

        swipeContainer.setOnRefreshListener(() -> {
            if (progressBarLoadMaterials.getVisibility() == View.GONE) {
                materialsUpdaterPresenter.attachView(
                        new SwipeContainerRefresherBaseMaterials());
                materialsUpdaterPresenter.updateMaterials(materialLinkedList);
            } else {
                final int delay = 300;
                swipeContainer.postDelayed(() ->
                        swipeContainer.setRefreshing(false), delay);
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setProgressBarLoadMaterials(LinkedList<Material> materialLinkedList) {
        final ProgressBarRefresherBaseMaterials progressBarRefresherBaseMaterials =
                new ProgressBarRefresherBaseMaterials();
        if (this.savedInstanceState == null) {
            materialsUpdaterPresenter.attachView(progressBarRefresherBaseMaterials);
            materialsUpdaterPresenter.updateMaterials(materialLinkedList);
        } else {
            progressBarLoadMaterials.setVisibility(View.INVISIBLE);
            progressBarRefresherBaseMaterials.updateMaterials(materialLinkedList);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                setProgressBarLoadMaterials(
                        convertToLinkedMaterialList(materialDao.findAll()));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("state ", "stop activity");
        materialsUpdaterPresenter.detachView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private LinkedList<Material> convertToLinkedMaterialList(
            List<Material> materials) {
        final LinkedList<Material> newMaterialList = new LinkedList<>();
        materials.forEach(newMaterialList::addLast);
        return newMaterialList;
    }

    private void createMaterialAdapter() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        materialsRecyclerAdapter = new MaterialsRecyclerAdapter(this);
        recyclerView.setAdapter(materialsRecyclerAdapter);
    }

    public class ProgressBarRefresherBaseMaterials implements BaseMaterialsPresenter {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void updateMaterials(LinkedList<Material> materials) {
            final int minDelay = 1000;
            progressBarLoadMaterials.postDelayed(() -> {
                materialsRecyclerAdapter.setListContent(materials);
                recyclerView.setAdapter(materialsRecyclerAdapter);
                progressBarLoadMaterials.setVisibility(View.GONE);
                addFormatsFiles();
            }, minDelay);
        }

    }

    public class SwipeContainerRefresherBaseMaterials implements BaseMaterialsPresenter {

        @Override
        public void updateMaterials(LinkedList<Material> materials) {
            swipeContainer.post(() -> {
                materialsRecyclerAdapter.setListContent(materials);
                recyclerView.setAdapter(materialsRecyclerAdapter);
                swipeContainer.setRefreshing(false);
                addFormatsFiles();
            });
        }

    }

    @Transaction
    private void addFormatsFiles() {
        final List<String> materialFormats = materialDao.findDistinctFormats();
        for (final String format : materialFormats) {
            final List<Folder> folders = folderDao.findByName(format);
            if ( folders.isEmpty() ) {
                final Folder folder = new Folder();
                folder.setName(format);
                final long folderId = folderDao.insert(folder);
                folder.setId(folderId);
            }
        }
        addMaterialsToFolder();
    }

    private void addMaterialsToFolder() {
        final List<Folder> folders = folderDao.findAll();
        for (final Folder folder : folders) {
            for (final Material material : materialDao.findByFormat(folder.getName())) {
                final MaterialFolderJoin materialFolderJoin = new MaterialFolderJoin();
                materialFolderJoin.setMaterialId(material.getId());
                materialFolderJoin.setFolderId(folder.getId());
                materialFolderJoinDao.insert(materialFolderJoin);
            }
        }
    }

}

