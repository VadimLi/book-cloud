package com.lipakov.vadim.booksDocs.dagger;


import android.app.Application;

import com.lipakov.vadim.booksDocs.app_database.AppDatabase;
import com.lipakov.vadim.booksDocs.dao.FolderDao;
import com.lipakov.vadim.booksDocs.dao.MaterialDao;
import com.lipakov.vadim.booksDocs.dao.MaterialFolderJoinDao;
import com.lipakov.vadim.booksDocs.model.MaterialFolderJoin;
import com.lipakov.vadim.booksDocs.presenters.MaterialsUpdaterPresenter;
import com.lipakov.vadim.booksDocs.presenters.services.FinderService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private AppDatabase appDatabase;

    public RoomModule(Application mApplication) {
        appDatabase = AppDatabase.getInMemoryDatabase(mApplication);
    }

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase() {
        return appDatabase;
    }

    @Singleton
    @Provides
    MaterialDao providesMaterialDao(AppDatabase appDatabase) {
        return appDatabase.getMaterialDao();
    }

    @Singleton
    @Provides
    FolderDao providesFolderDao(AppDatabase appDatabase) {
        return appDatabase.getFolderDao();
    }

    @Singleton
    @Provides
    MaterialFolderJoinDao providesMaterialFolderJoinDao(AppDatabase appDatabase) {
        return appDatabase.getMaterialFolderJoinDao();
    }

    @Provides
    @Singleton
    FinderService providesFinderService(MaterialDao materialDao) {
        return new FinderService(materialDao);
    }

    @Provides
    @Singleton
    MaterialsUpdaterPresenter providesMaterialsUpdaterPresenter(FinderService finderService) {
        return new MaterialsUpdaterPresenter(finderService);
    }

}
