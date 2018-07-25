package com.example.vadim.books_sync.presenters.services;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.example.vadim.books_sync.model.Material;

import java.io.File;

public class DocumentService {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openDocumentByPath(View view, Material material) {
        final File file = new File(material.getPath());
        if (file.exists()) {
            final Intent formatIntent = new Intent(Intent.ACTION_VIEW);
            formatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = FileProvider.getUriForFile(view.getContext(),
                    view.getContext().getApplicationContext()
                            .getPackageName() +
                            ".provider", file);
            formatIntent.setData(uri);
            formatIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            view.getContext().startActivity(formatIntent);
        } else {
            Log.e("File not exists : ", file.getAbsolutePath());
        }
    }

}
