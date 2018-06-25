package com.example.vadim.books_sync.presenters.utils;


import android.os.Environment;
import android.util.Log;

import com.example.vadim.books_sync.model.Material;

import java.io.File;
import java.util.List;

public class Finder {

    private final List<Material> materials;

    public Finder(final List<Material> materials) {
        this.materials = materials;
    }

    public List<Material> findFiles() {
        File rootFile = Environment.getExternalStorageDirectory();
        Log.d("root dir : ", rootFile.getAbsolutePath());
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
                    final String format = getFileExtension(f);
                    final String filePath = f.getAbsolutePath();
                    if (format.equals("pdf") || format.equals("docx")) {
                        final Material material = new Material();
                        material.setName(f.getName());
                        material.setFormat(format);
                        material.setPath(filePath);
                        materials.add(material);
                        Log.i("", "File: " + filePath);
                    }
                }
            }
        } else {
            Log.e("", "list null");
        }
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

}
