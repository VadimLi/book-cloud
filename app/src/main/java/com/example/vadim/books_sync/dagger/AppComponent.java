package com.example.vadim.books_sync.dagger;

import android.app.Application;

import com.example.vadim.books_sync.app_database.AppDatabase;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.presenters.MaterialsUpdaterPresenter;
import com.example.vadim.books_sync.presenters.services.FinderService;
import com.example.vadim.books_sync.views.MainActivity;
import com.example.vadim.books_sync.views.PropertiesDialog;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {

    void injectMainActivity(MainActivity activity);

    void injectDialogFragment(PropertiesDialog propertiesDialog);

    MaterialDao materialDao();

    AppDatabase appDatabase();

    Application application();

    FinderService getFinderService();

    MaterialsUpdaterPresenter getMaterialsUpdaterPresenter();

}
