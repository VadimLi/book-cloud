package com.example.vadim.books_sync.app_database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.example.vadim.books_sync.dao.FolderDao;
import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.dao.MaterialFolderJoinDao;
import com.example.vadim.books_sync.model.Folder;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.model.MaterialFolderJoin;

/* singleton */
@Database(entities = { Material.class, Folder.class, MaterialFolderJoin.class},
        version = AppDatabase.VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // started version with 1
    static final int VERSION = 10;

    public abstract MaterialDao getMaterialDao();

    public abstract FolderDao getFolderDao();

    public abstract MaterialFolderJoinDao getMaterialFolderJoinDao();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context, AppDatabase.class, "documents")
                    .addMigrations(
                            FROM_1_TO_2,
                            FROM_2_TO_3,
                            FROM_3_TO_4,
                            FROM_4_TO_5,
                            FROM_5_TO_6,
                            FROM_6_TO_7,
                            FROM_7_TO_8,
                            FROM_8_TO_9,
                            FROM_9_TO_10)
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
            database.execSQL("DELETE FROM material");
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
            database.execSQL("CREATE TABLE IF NOT EXISTS 'folder'(" +
                    "'id' INT AUTO_INCREMENT," +
                    "'name' TEXT NOT NULL," +
                    "PRIMARY KEY('id'))");
        }
    };

    private static final Migration FROM_5_TO_6 = new Migration(5, 6) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS 'material_folder_join'( " +
                    "'materialId' INT, " +
                    "'folderId' INT, " +
                    "PRIMARY KEY('materialId', 'folderId'), " +
                    "FOREIGN KEY ('materialId') REFERENCES 'material' ('id') " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE, " +
                    "FOREIGN KEY ('folderId') REFERENCES 'folder' ('id') " +
                    "ON UPDATE NO ACTION ON DELETE CASCADE);");
        }
    };

    private static final Migration FROM_6_TO_7 = new Migration(6, 7) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE folder " +
                    " ADD COLUMN root INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static final Migration FROM_7_TO_8 = new Migration(7, 8) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE folder " +
                    " DROP COLUMN root");
        }
    };

    private static final Migration FROM_8_TO_9 = new Migration(8, 9) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE folder ADD CONSTRAINT UC_Name UNIQUE (name)");
        }
    };

    private static final Migration FROM_9_TO_10 = new Migration(9, VERSION) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE material ADD CONSTRAINT UC_Path UNIQUE (path)");
        }
    };

    public static void destroyInstance() {
        instance = null;
    }

}
