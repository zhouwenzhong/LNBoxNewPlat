package com.lianyao.ftfbox.domain.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

/**
 * Created by zhouwz on 16/6/11.
 */
public class DyneImageView extends ImageView {
    public DyneImageView(Context context) {
        super(context);
    }

    public DyneImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DyneImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);

    }
}
