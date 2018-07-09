package com.example.vadim.books_sync.presenters.utils;


import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.model.Material;
import com.example.vadim.books_sync.presenters.Format;

import java.io.File;

import javax.inject.Inject;

public class Finder extends Application {

    private MaterialDao materialDao;

    @Inject
    public Finder(MaterialDao materialDao) {
        this.materialDao = materialDao;
    }

    public Finder() {}

    public void findFiles() {
        File rootFile = Environment.getExternalStorageDirectory();
        Log.d("root dir : ", rootFile.getAbsolutePath());
        searchFiles(rootFile);
    }

    private void searchFiles(File root) {
        final File[] list = root.listFiles();
        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) {
                    Log.d("", "Dir: " + f.getAbsoluteFile());
                    searchFiles(f);
                }
                else {
                    insertData(f);
                }
            }
        } else {
            Log.e("", "list null");
        }
    }

    private void insertData(File file) {
        final String format = getFileExtension(file);
        final String filePath = file.getAbsolutePath();
        if (checkFormat(format)) {
            if (materialDao.findAllByPath(file.getAbsolutePath()).isEmpty() ||
                    materialDao.findAllByPath(file.getAbsolutePath()) == null) {
                final Material material = new Material();
                material.setName(file.getName());
                material.setFormat(getFileExtension(file));
                material.setPath(file.getAbsolutePath());
                materialDao.insert(material);
            }
            Log.i("", "File: " + filePath);
        }
    }

    private boolean checkFormat(String format) {
        for (Format f : Format.values()) {
            if (format.equals(f.getFormat())) {
                return true;
            }
        }
        return false;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

}
