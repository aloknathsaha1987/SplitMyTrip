package com.aloknath.splitmytrip.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ALOKNATH on 3/14/2015.
 */
public class EditTextNoKeyBoard extends EditText {

    public EditTextNoKeyBoard(Context context) {
        super(context);
    }


    public EditTextNoKeyBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public EditTextNoKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        // TODO Auto-generated method stub
        return false;
    }
}