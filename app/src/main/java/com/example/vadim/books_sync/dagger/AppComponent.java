package com.example.vadim.books_sync.dagger;

import android.app.Application;

import com.example.vadim.books_sync.app_database.AppDatabase;
import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.dao.MaterialFolderJoinDao;
import com.example.vadim.books_sync.presenters.MaterialsUpdaterPresenter;
import com.example.vadim.books_sync.presenters.services.FinderService;
import com.example.vadim.books_sync.views.AddFolderDialog;
import com.example.vadim.books_sync.views.FoldersActivity;
import com.example.vadim.books_sync.views.MainActivity;
import com.example.vadim.books_sync.views.MaterialsOfFolderActivity;
import com.example.vadim.books_sync.views.PropertiesDialogForFolders;
import com.example.vadim.books_sync.views.PropertiesDialogForMaterials;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {

    void injectMainActivity(MainActivity activity);

    void injectFoldersActivity(FoldersActivity activity);

    void injectMaterialsOfFolderActivity(MaterialsOfFolderActivity activity);

    void injectDialogFragmentForMaterials(
            PropertiesDialogForMaterials propertiesDialogForMaterials);

    void injectDialogFragmentForFolders(
            PropertiesDialogForFolders propertiesDialogForMaterials);

    void injectAddFolderDialog(AddFolderDialog addFolderDialog);

    MaterialDao materialDao();

    FolderDao folderDao();

    MaterialFolderJoinDao materialFolderJoinDao();

    AppDatabase appDatabase();

    Application application();

    FinderService getFinderService();

    MaterialsUpdaterPresenter getMaterialsUpdaterPresenter();

}
