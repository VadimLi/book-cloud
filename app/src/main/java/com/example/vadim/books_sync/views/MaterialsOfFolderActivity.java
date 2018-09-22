package com.example.vadim.books_sync.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.vadim.books_sync.R;
import com.example.vadim.books_sync.adapter.MaterialsRecyclerAdapter;
import com.example.vadim.books_sync.dagger.AppModule;
import com.example.vadim.books_sync.dagger.DaggerAppComponent;
import com.example.vadim.books_sync.dagger.RoomModule;
import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.dao.MaterialFolderJoinDao;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.MaterialsUpdaterPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaterialsOfFolderActivity extends AppCompatActivity implements ActivityView {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    @BindView(R.id.material_list_of_folder)
    RecyclerView recyclerView;

    @BindView(R.id.searchFiles)
    SearchView searchMaterials;

    @BindView(R.id.btnFilesOfFolder)
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

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials_of_folder);

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
                .injectMaterialsOfFolderActivity(this);
        createAdapter();
        final Bundle folderBundle = getIntent().getExtras();
        if (folderBundle != null) {
            final Folder folder = folderBundle.getParcelable("folder");
            final List<Material> materials = materialFolderJoinDao
                    .findMaterialsForFolders(folder.getId());
            materialsRecyclerAdapter.setListContent(materials);
            recyclerView.setAdapter(materialsRecyclerAdapter);
        }
        addQueryTextListener();
        moveFolders.setOnClickListener(v -> {
            final Intent booksIntent = new Intent(this, FoldersActivity.class);
            setResult(RESULT_OK, booksIntent);
            finish();
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("state ", "stop activity");
        materialsUpdaterPresenter.detachView();
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

}