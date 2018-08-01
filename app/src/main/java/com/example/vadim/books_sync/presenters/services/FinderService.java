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

import rx.Observable;
import rx.schedulers.Schedulers;

@SuppressLint("Registered")
public class FinderService extends Application {

    private MaterialDao materialDao;

    private LinkedList<Material> materials = new LinkedList<>();

    private List<Material> loadingMaterialList = new ArrayList<>();

    @Inject
    public FinderService(MaterialDao materialDao) {
        this.materialDao = materialDao;
    }

    public LinkedList<Material> getMaterials() {
        final File rootFile = Environment.getExternalStorageDirectory();
        Log.d("root dir : ", rootFile.getAbsolutePath());
        materials.clear();
        loadingMaterialList.clear();
        searchFiles(rootFile);
        return materials;
    }

    public void deleteMaterialFiles(List<Material> materials) {
        boolean checkMaterial;
        final Iterator<Material> materialIterator = materials.iterator();
        while (materialIterator.hasNext()) {
            checkMaterial = false;
            final Material outerMaterial = materialIterator.next();
            String outerMaterialPath = outerMaterial.getPath();
            for (Material innerMaterial : loadingMaterialList) {
                if ( outerMaterialPath
                        .equals(innerMaterial.getPath()) ) {
                    checkMaterial = true;
                }
            }
            if ( !checkMaterial ) {
                Log.d("material remove : ", outerMaterial.getName());
                materialIterator.remove();
                materialDao.deleteByPath(outerMaterialPath);
            }
        }
    }

    private void searchFiles(File root) {
        final File[] list = root.listFiles();
        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) {
                    searchFiles(f);
                } else {
                    insertData(f);
                }
            }
        } else {
            Log.e("", "list null");
        }
    }

    private Observable<Material> getMaterialData(final Material material) {
        return Observable.create(
                (Observable.OnSubscribe<Material>)subscriber -> {
            if (!subscriber.isUnsubscribed()) {
                try {
                    subscriber.onNext(insertData(new File(material.getPath())));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private Material insertData(File file) {
        final String format = getFileExtension(file);
        final String filePath = file.getAbsolutePath();
        if (checkFormat(format)) {
            final Material material = new Material();
            material.setName(file.getName());
            material.setFormat(getFileExtension(file));
            material.setPath(file.getAbsolutePath());
            if (materialDao.findByPath(file.getAbsolutePath()).isEmpty() ||
                    materialDao.findByPath(file.getAbsolutePath()) == null) {
                materialDao.insert(material);
                materials.addFirst(material);
            }
            loadingMaterialList.add(material);
            Log.i("", "File: " + filePath);
            return material;
        }
        return null;
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
