package com.android.library.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.android.library.R;
import com.android.library.Utils.Cache;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
	public CustomTextView(Context context) {
		this(context, null);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.textViewStyle);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if(isInEditMode()) {
			return;
		}
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.app);
		if(typedArray != null) {
			String fontFamily = typedArray.getString(R.styleable.app_typeface);
			if(fontFamily != null && !fontFamily.isEmpty()) {
				setTypeface(Cache.getTypeface(context.getAssets(), fontFamily));
			}
		}
	}
}