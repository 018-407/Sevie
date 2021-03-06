package com.android.library.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.android.library.R;
import com.android.library.Utils.Cache;

public class CustomButton extends android.support.v7.widget.AppCompatButton {
	public CustomButton(Context context) {
		this(context, null);
	}

	public CustomButton(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.buttonStyle);
	}

	public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
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