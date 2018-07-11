package com.example.vadim.books_sync.presenters.services;


import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.model.Material;

import java.io.File;
import java.util.LinkedList;

import javax.inject.Inject;

@SuppressLint("Registered")
public class FinderService extends Application {

    private MaterialDao materialDao;

    private LinkedList<Material> materials = new LinkedList<>();

    @Inject
    public FinderService(MaterialDao materialDao) {
        this.materialDao = materialDao;
    }

    public LinkedList<Material> getMaterials() {
        final File rootFile = Environment.getExternalStorageDirectory();
        Log.d("root dir : ", rootFile.getAbsolutePath());
        materials.clear();
        searchFiles(rootFile);
        return materials;
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
                materials.addFirst(material);
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
