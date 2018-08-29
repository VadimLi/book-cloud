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
@Database(entities = { Material.class }, version = AppDatabase.VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // started version with 1
    static final int VERSION = 5;

    public abstract MaterialDao getMaterialDao();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context, AppDatabase.class, "document_db")
                    .addMigrations(
                            FROM_1_TO_2,
                            FROM_2_TO_3,
                            FROM_3_TO_4,
                            FROM_4_TO_5)
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

    private static final Migration FROM_3_TO_4 = new Migration(3, 4) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("DELETE FROM material");
        }
    };

    private static final Migration FROM_4_TO_5 = new Migration(4, 5) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("DELETE FROM material");
        }
    };

//    private static final Migration FROM_5_TO_6 = new Migration(5, 6) {
//        @Override
//        public void migrate(final SupportSQLiteDatabase database) {
//            database.execSQL("CREATE TABLE IF NOT EXISTS 'folder'(" +
//                    "'id' LONG AUTO_INCREMENT," +
//                    "'name' TEXT NOT NULL," +
//                    "'id_material' LONG NOT NULL," +
//                    "'id_folder' LONG NOT NULL," +
//                    "PRIMARY KEY('id'))");
//        }
//    };
//
//    private static final Migration FROM_6_TO_7 = new Migration(6, 7) {
//        @Override
//        public void migrate(final SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE material " +
//                    "ADD COLUMN id_folder LONG;");
//        }
//    };

    public static void destroyInstance() {
        instance = null;
    }

}
