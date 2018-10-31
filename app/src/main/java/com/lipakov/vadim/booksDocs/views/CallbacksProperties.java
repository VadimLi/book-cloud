package com.lipakov.vadim.booksDocs.views;

import android.view.View;

public interface CallbacksProperties {
    View.OnClickListener onClickShare();
    View.OnClickListener onClickRemove();
    View.OnLongClickListener onLongClickRemove();
    View.OnClickListener onClickRename();
    View.OnClickListener onClickAddToFile();
    interface CallbacksEditor {
        View.OnClickListener onClickCancel();
        View.OnClickListener onClickApply();
        View.OnClickListener onClickEditText();
    }
}
