package com.lipakov.vadim.booksDocs.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.arch.persistence.room.Transaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.lipakov.vadim.booksDocs.R;
import com.lipakov.vadim.booksDocs.adapter.MaterialsRecyclerAdapter;
import com.lipakov.vadim.booksDocs.basePresenters.BaseMaterialsPresenter;
import com.lipakov.vadim.booksDocs.dagger.AppModule;
import com.lipakov.vadim.booksDocs.dagger.DaggerAppComponent;
import com.lipakov.vadim.booksDocs.dagger.RoomModule;
import com.lipakov.vadim.booksDocs.dao.FolderDao;
import com.lipakov.vadim.booksDocs.dao.MaterialDao;
import com.lipakov.vadim.booksDocs.dao.MaterialFolderJoinDao;
import com.lipakov.vadim.booksDocs.model.Folder;
import com.lipakov.vadim.booksDocs.model.Material;
import com.lipakov.vadim.booksDocs.model.MaterialFolderJoin;
import com.lipakov.vadim.booksDocs.presenters.MaterialsUpdaterPresenter;
import com.lipakov.vadim.booksDocs.presenters.services.Formats;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ActivityView {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    @BindView(R.id.material_list)
    RecyclerView recyclerView;

    @BindView(R.id.searchMaterials)
    SearchView searchMaterials;

    @BindView(R.id.progressBarLoadMaterials)
    ProgressBar progressBarLoadMaterials;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

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
        ButterKnife.bind(this);
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .injectMainActivity(this);
        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        final List<Material> materials = materialDao.findAll();
        final LinkedList<Material> materialLinkedList =
                convertToLinkedMaterialList(materials);
        setProgressBarLoadMaterials(materialLinkedList);
        createAdapter();
        addOnClickListenerForButtonFolders();
        addQueryTextListener();
        addRefresherListener(materialLinkedList);
    }

    private void addOnClickListenerForButtonFolders() {
        final View actionView = getCustomActionBar();
        final ImageButton btnFolders =
                actionView.findViewById(R.id.btnFolders);
        btnFolders.setOnClickListener(v -> {
            final Intent foldersIntent =
                    new Intent(this, FoldersActivity.class);
            materialsUpdaterPresenter.getUpdaterAdapter().interrupt();
            swipeContainer.setRefreshing(false);
            startActivityForResult(foldersIntent, 1);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addRefresherListener(LinkedList<Material> materialLinkedList) {
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
        for (final Material material : materials) {
            newMaterialList.addFirst(material);
        }
        return newMaterialList;
    }

    @Override
    public void addQueryTextListener() {
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
    }

    @Override
    public void createAdapter() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        materialsRecyclerAdapter = new MaterialsRecyclerAdapter(this);
        recyclerView.setAdapter(materialsRecyclerAdapter);
    }

    @Override
    public View getCustomActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.main_action_bar_layout);
        return actionBar.getCustomView();
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
        final List<String> materialsOfFormats = materialDao.findDistinctFormats();
        for (final String format : materialsOfFormats) {
            final List<Folder> folders = folderDao.findByName(format);
            if ( folders.isEmpty() ) {
                final Folder folder = new Folder();
                folder.setName(format);
                final long folderId = folderDao.insert(folder);
                folder.setId(folderId);
            }
        }
        deleteFoldersWithFormats(materialsOfFormats);
        addMaterialsToFolder();
    }

    private void deleteFoldersWithFormats(final List<String> materialsOfFormats) {
        final List<Folder> folders = folderDao.findAll();
        for (final Folder folder : folders) {
            final String folderName = folder.getName();
            if ( !Formats.notCheckNameOfFormat(folderName) &&
                    !materialsOfFormats.contains(folderName) ) {
                folderDao.deleteByName(folderName);
            }
        }
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

