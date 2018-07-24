package com.example.vadim.books_sync.adapter.properties_dialog;


import android.annotation.SuppressLint;
import android.app.DialogFragment;

@SuppressLint("ValidFragment")
public class PropertiesDialog extends DialogFragment {

//    @BindView(R.id.fileName)
//    TextView fileNameTextView;
//
//    @BindView(R.id.rename)
//    ImageButton renameImageButton;
//
//    @BindView(R.id.addToFolder)
//    ImageButton addToFolderImageButton;
//
//    @BindView(R.id.trash)
//    ImageButton trashImageButton;
//
//    @BindView(R.id.share)
//    ImageButton shareImageButton;
//
//    private MaterialDao materialDao;
//
//    private MaterialListPresenter materialPresenter;
//
//    @Inject
//    public PropertiesDialog(MaterialDao materialDao) {
//        this.materialDao = materialDao;
//    }
//
//    public void setMaterialPresenter(MaterialListPresenter materialPresenter) {
//        this.materialPresenter = materialPresenter;
//    }
//
//    @SuppressLint("InflateParams")
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View viewProperties = inflater.inflate(R.layout.dialog_properties, null);
//        ButterKnife.bind(this, viewProperties);
//
//        final Material material = materialPresenter.getMaterial();
//        fileNameTextView.setText(material.getName());
//
//        renameImageButton.setOnClickListener(v -> Log.d("state ", "rename"));
//
//        addToFolderImageButton.setOnClickListener(v -> Log.d("state ", "add to folder"));
//
//        trashImageButton.setOnClickListener(v -> {
//            File file = new File(material.getPath());
//            if (file.exists()) {
//                file.delete();
//                materialPresenter.removeMaterial();
//                materialDao.deleteById(material.getId());
//            }
//        });
//
//        shareImageButton.setOnClickListener(v -> {
//            File file = new File(material.getPath());
//            if (file.exists()) {
//                final Uri uriToFile = Uri.fromFile(file);
//                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToFile);
//                shareIntent.setType("text/plain");
//                startActivity(Intent.createChooser(shareIntent, material.getName()));
//            }
//        });
//
//        return viewProperties;
//    }

}
