package com.example.vadim.books_sync;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File root = Environment.getRootDirectory();
        String rootPath = root.getPath();

        final Button syncButton = findViewById(R.id.btnSync);
        syncButton.setOnClickListener(e -> {
            searchFilesPDf(root);
        });
    }

    private void searchFilesPDf(File root) {

        File[] list = root.listFiles();

        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) {
                    Log.d("", "Dir: " + f.getAbsoluteFile());
                    searchFilesPDf(f);
                }
                else {
                    String format = getFileExtension(f);
                 //   if (format.equals("pdf")) {
                    Log.d("", "File: " + f.getAbsoluteFile());
                 //   }
                }
            }
        } else {
            Log.e("", "list null");
        }
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

}
