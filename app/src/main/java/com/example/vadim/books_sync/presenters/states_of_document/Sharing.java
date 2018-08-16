package com.example.vadim.books_sync.presenters.states_of_document;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.vadim.books_sync.presenters.MaterialPresenter;
import com.example.vadim.books_sync.views.PropertiesDialog;

import java.io.File;

public class Sharing extends State {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void doState(MaterialPresenter materialPresenter) {
        PropertiesDialog propertiesDialog =
                (PropertiesDialog) materialPresenter.getPropertiesDialog();
        final File file = new File(materialPresenter.getPath());
        if ( file.exists() ) {
            final Uri uriToFile = Uri.fromFile(file);
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToFile);
            shareIntent.setType("text/plain");
            propertiesDialog.startActivity(
                    Intent.createChooser(shareIntent,
                            materialPresenter.getName()));
        }
    }

}
