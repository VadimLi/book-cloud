package com.example.vadim.books_sync.presenters.states_of_document;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.example.vadim.books_sync.presenters.MaterialPresenter;

import java.io.File;

public class Sharing extends AbstractStateProperties {

    @Override
    public void doState(MaterialPresenter materialPresenter) {
        final File file = new File(materialPresenter.getPath());
        if ( file.exists() ) {
            final Uri uriToFile = Uri.fromFile(file);
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToFile);
            shareIntent.setType("text/plain");
            materialPresenter
                    .getPropertiesDialog()
                    .startActivity(Intent.createChooser(shareIntent,
                            materialPresenter.getName()));
        }
    }

}
