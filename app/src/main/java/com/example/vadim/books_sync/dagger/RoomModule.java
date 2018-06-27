package com.example.vadim.books_sync.dagger;


import android.app.Application;

import com.example.vadim.books_sync.app_database.AppDatabase;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.utils.Finder;

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
    Finder providesFinder(MaterialDao materialDao) {
        return new Finder(materialDao);
    }

    @Provides
    @Singleton
    MaterialPresenter providesMaterialPresenter(Finder finder) {
        return new MaterialPresenter(finder);
    }

}
