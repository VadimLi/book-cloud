package com.lipakov.vadim.booksDocs.dagger;

import android.app.Application;

import com.lipakov.vadim.booksDocs.app_database.AppDatabase;
import com.lipakov.vadim.booksDocs.dao.FolderDao;
import com.lipakov.vadim.booksDocs.dao.MaterialDao;
import com.lipakov.vadim.booksDocs.dao.MaterialFolderJoinDao;
import com.lipakov.vadim.booksDocs.presenters.MaterialPresenter;
import com.lipakov.vadim.booksDocs.presenters.MaterialsUpdaterPresenter;
import com.lipakov.vadim.booksDocs.presenters.services.FinderService;
import com.lipakov.vadim.booksDocs.views.AddingFolderDialog;
import com.lipakov.vadim.booksDocs.views.FoldersActivity;
import com.lipakov.vadim.booksDocs.views.MainActivity;
import com.lipakov.vadim.booksDocs.views.MaterialsOfFolderActivity;
import com.lipakov.vadim.booksDocs.views.PropertiesDialogForFolders;
import com.lipakov.vadim.booksDocs.views.PropertiesDialogForMaterials;
import com.lipakov.vadim.booksDocs.views.SelectorFolderActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {

    void injectMainActivity(MainActivity activity);

    void injectSelectorFolderActivity(FoldersActivity activity);

    void injectMaterialsOfFolderActivity(MaterialsOfFolderActivity activity);

    void injectDialogFragmentForMaterials(
            PropertiesDialogForMaterials propertiesDialogForMaterials);

    void injectDialogFragmentForFolders(
            PropertiesDialogForFolders propertiesDialogForMaterials);

    void injectAddFolderDialog(AddingFolderDialog addingFolderDialog);

    void injectSelectorFolderActivity(SelectorFolderActivity selectorFolderActivity);

    MaterialDao materialDao();

    FolderDao folderDao();

    MaterialFolderJoinDao materialFolderJoinDao();

    AppDatabase appDatabase();

    Application application();

    FinderService getFinderService();

    MaterialsUpdaterPresenter getMaterialsUpdaterPresenter();

}
