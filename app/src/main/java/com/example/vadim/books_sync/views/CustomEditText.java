package com.example.vadim.books_sync.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.vadim.books_sync.R;

import io.reactivex.subjects.PublishSubject;

@SuppressLint("AppCompatCustomView")
public class CustomEditText extends EditText {

    //The image we are going to use for the Clear button
    private Drawable imgCloseButton;

    private final static PublishSubject<String> PUBLISH_SUBJECT = PublishSubject.create();;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    void init() {
        imgCloseButton =
                getResources().getDrawable(R.mipmap.ic_clear_text_foreground);
        // Set bounds of the Clear button so it will look ok
        imgCloseButton.setBounds(0, 0,
                imgCloseButton.getIntrinsicWidth() / 3,
                imgCloseButton.getIntrinsicHeight() / 3);

        // There may be initial text in the field, so we may need to display the  button
        handleClearButton();
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CustomEditText.this.handleClearButton();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                PUBLISH_SUBJECT.onNext(arg0.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        setOnTouchListener((v, event) -> {
            if (getCompoundDrawables()[2] == null)
                return false;
            if (event.getAction() != MotionEvent.ACTION_UP)
                return false;
            if ((event.getX() > getWidth()  -
                    getPaddingRight() - imgCloseButton.getIntrinsicWidth())) {
                setText("");
                CustomEditText.this.handleClearButton();
            }
            return false;
        });

    }

    public void handleClearButton() {
        if (this.getText().toString().equals("")) {
            setVisibleCloseButton(false);
        } else {
            setVisibleCloseButton(true);
        }
    }

    public void setVisibleCloseButton(boolean visible) {
        if (visible) {
            this.setCompoundDrawables(
                    this.getCompoundDrawables()[0],
                    this.getCompoundDrawables()[1],
                    imgCloseButton, this.getCompoundDrawables()[3]);
            this.setBackgroundColor(1);
        } else {
            this.setCompoundDrawables(
                    this.getCompoundDrawables()[0],
                    this.getCompoundDrawables()[1],
                    null, this.getCompoundDrawables()[3]);
        }
    }

    public static PublishSubject<String> getPublishSubject() {
        return PUBLISH_SUBJECT;
    }

}
