package com.dw.andro.dictionary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dvayweb on 24/02/16.
 */
public class BackgroundItemDecoration extends RecyclerView.ItemDecoration {

    private final int mOddBackground;
    private final int mEvenBackground;
    private Drawable mDivider;

    public BackgroundItemDecoration(@DrawableRes int oddBackground, @DrawableRes int evenBackground, Context context) {
        mOddBackground = oddBackground;
        mEvenBackground = evenBackground;
        mDivider = context.getResources().getDrawable(R.drawable.line_divider);
    }

//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//        int position = parent.getChildAdapterPosition(view);
//        view.setBackgroundResource(position % 2 == 0 ? mEvenBackground : mOddBackground);
//    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
