package com.lipakov.vadim.booksDocs.presenters.states_of_file;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.lipakov.vadim.booksDocs.BuildConfig;
import com.lipakov.vadim.booksDocs.presenters.MaterialPresenter;
import com.lipakov.vadim.booksDocs.presenters.StateOfDocument;
import com.lipakov.vadim.booksDocs.views.PropertiesDialogForMaterials;

import java.io.File;

@SuppressLint("Registered")
public class SharingFile extends FileProvider implements StateOfDocument.StateOfFile {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void doStateWithFile(MaterialPresenter materialPresenter) {
        PropertiesDialogForMaterials propertiesDialogForMaterials =
                (PropertiesDialogForMaterials) materialPresenter.getPropertiesDialog();
        final File file = new File(materialPresenter.getPath());
        if ( file.exists() ) {
            final Uri uriToFile = FileProvider.getUriForFile(getContext(),
                    BuildConfig.APPLICATION_ID + ".provider", file);
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToFile);
            shareIntent.setType("text/plain");
            propertiesDialogForMaterials.startActivity(
                    Intent.createChooser(shareIntent,
                            materialPresenter.getName()));
            Log.d("TAG", "Sharing has worked");
            return;
        }
        Log.d("TAG", "Sharing has not worked");
    }

}
