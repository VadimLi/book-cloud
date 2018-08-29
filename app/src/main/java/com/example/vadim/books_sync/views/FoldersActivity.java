package com.example.vadim.books_sync.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.widget.ImageButton;

import com.example.vadim.books_sync.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoldersActivity extends AppCompatActivity {

    @BindView(R.id.btnCreatorFolder)
    ImageButton imageButtonCreatorFolder;

    @BindView(R.id.btnFiles)
    ImageButton imageButtonFiles;

    @BindView(R.id.inputSearch)
    SearchView inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);
        ButterKnife.bind(this);

        imageButtonFiles.setOnClickListener(v -> {
            final Intent booksIntent = new Intent(this, MainActivity.class);
            setResult(RESULT_OK, booksIntent);
            finish();
        });

    }

}
