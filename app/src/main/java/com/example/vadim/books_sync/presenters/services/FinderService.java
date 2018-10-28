package com.example.vadim.books_sync.presenters.services;


import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.vadim.books_sync.dao.MaterialDao;
import com.example.vadim.books_sync.model.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;


@SuppressLint("Registered")
public class FinderService extends Application {

    private MaterialDao materialDao;

    private LinkedList<Material> materials = new LinkedList<>();

    private List<String> fileSystemMaterialPathList = new ArrayList<>();

    @Inject
    public FinderService(MaterialDao materialDao) {
        this.materialDao = materialDao;
    }

    public LinkedList<Material> getMaterials() {
        final File rootFile = Environment.getExternalStorageDirectory();
        Log.d("root dir : ", rootFile.getAbsolutePath());
        materials.clear();
        fileSystemMaterialPathList.clear();
        searchFiles(rootFile);
        return materials;
    }

    public void deleteMaterialFiles(final List<Material> materials) {
        final Iterator<Material> materialIterator = materials.iterator();
        while (materialIterator.hasNext()) {
            final Material material = materialIterator.next();
            if ( !fileSystemMaterialPathList.contains(material.getPath()) ) {
                materialIterator.remove();
                materialDao.deleteByPath(material.getPath());
            }
        }
    }

    private void searchFiles(File root) {
        final File[] list = root.listFiles();
        if (list != null) {
            for (File f : list) {
                if (f.isDirectory())
                    searchFiles(f);
                else insertData(f);
            }
        } else {
            Log.e("", "list null");
        }
    }

    private void insertData(File file) {
        final String format = getFileExtension(file);
        final String filePath = file.getAbsolutePath();
        if (checkFormat(format)) {
            final Material material = new Material();
            material.setName(file.getName());
            material.setFormat(getFileExtension(file));
            material.setPath(file.getAbsolutePath());
            if (materialDao.findByPath(file.getAbsolutePath()).isEmpty() ||
                    materialDao.findByPath(file.getAbsolutePath()) == null) {
                final long materialId = materialDao.insert(material);
                material.setId(materialId);
                materials.addFirst(material);
            }
            fileSystemMaterialPathList.add(material.getPath());
            Log.i("", "File: " + filePath);
        }
    }

    private boolean checkFormat(String inputFormat) {
        for (Formats.Format format : Formats.Format.values()) {
            if (inputFormat.equals(format.getFormat())) {
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