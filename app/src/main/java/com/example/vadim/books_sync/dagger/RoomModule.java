package com.example.vadim.books_sync.dagger;


import android.app.Application;

import com.example.vadim.books_sync.app_database.AppDatabase;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.services.FinderService;

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

    @Provides
    @Singleton
    FinderService providesFinderService(MaterialDao materialDao) {
        return new FinderService(materialDao);
    }

    @Provides
    @Singleton
    MaterialPresenter providesMaterialPresenter(FinderService finderService) {
        return new MaterialPresenter(finderService);
    }

}
