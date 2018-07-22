package com.example.vadim.books_sync.dagger;

import android.app.Application;

import com.example.vadim.books_sync.adapter.MaterialAdapter;
import com.example.vadim.books_sync.adapter.properties_dialog.PropertiesDialog;
import com.example.vadim.books_sync.app_database.AppDatabase;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.presenters.services.FinderService;
import com.example.vadim.books_sync.views.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);

    MaterialDao materialDao();

    AppDatabase appDatabase();

    Application application();

    FinderService getFinderService();

    MaterialPresenter getMaterialPresenter();

    PropertiesDialog getPropertiesDialog();

    MaterialAdapter getMaterialAdapter();

}
