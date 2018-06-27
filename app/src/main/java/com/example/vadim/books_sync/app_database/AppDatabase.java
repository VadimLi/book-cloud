package com.example.vadim.books_sync.app_database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.model.Material;

/* singleton */
@Database(entities = { Material.class }, version = AppDatabase.VERSION)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // started version with 1
    static final int VERSION = 3;

    public abstract MaterialDao getMaterialDao();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context, AppDatabase.class, "doc_db")
                    .addMigrations(FROM_1_TO_2, FROM_2_TO_3)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    private static final Migration FROM_1_TO_2 = new Migration(1, 2) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS 'material'(" +
                    "'id' INT AUTO_INCREMENT," +
                    "'name' TEXT NOT NULL," +
                    "'format' TEXT NOT NULL," +
                    "'path' TEXT NOT NULL," +
                    "PRIMARY KEY('id'))");
        }
    };

    private static final Migration FROM_2_TO_3 = new Migration(2, 3) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE material ADD CONSTRAINT UC_Path UNIQUE (path)");
        }
    };

    public static void destroyInstance() {
        instance = null;
    }

}
