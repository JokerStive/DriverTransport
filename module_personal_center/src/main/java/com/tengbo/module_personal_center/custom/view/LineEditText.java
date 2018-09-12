package com.tengbo.module_personal_center.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tengbo.basiclibrary.utils.UiUtils;
import com.tengbo.module_personal_center.R;

import retrofit2.http.PUT;

public class LineEditText extends LinearLayout {

    private int hintTextColor;
    private float paddingLeft;
    private String text;
    private String hint;
    public EditText mEt;

    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LineEditText);
        hint = array.getString(R.styleable.LineEditText_hint);
        paddingLeft = array.getDimension(R.styleable.LineEditText_paddingLeft, 0);
        hintTextColor = array.getColor(R.styleable.LineEditText_hintTextColor, 0);
        init(context);
        array.recycle();
    }

    /*** @param context
     */
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_line_edit_text, this);
        mEt = view.findViewById(R.id.et);
        mEt.setHint(hint);
        mEt.setText(text);
        mEt.setPadding((int) paddingLeft, 0, 0, 0);

        if (hintTextColor != 0) {
            mEt.setHintTextColor(hintTextColor);
        }
    }

    public static OnFocusChangeListener onFocusAutoClearHintListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText textView = (EditText) v;
            String hint;
            if (hasFocus) {
                hint = textView.getHint().toString();
                textView.setTag(hint);
                textView.setHint("");
            } else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
            }
        }
    };

    public String getTextTrim() {
        return mEt.getText().toString().trim();
    }


    public void setText(String text) {
        mEt.setText(text);
    }

    public void setInputType(int inputType) {
        mEt.setInputType(inputType);
    }


}
