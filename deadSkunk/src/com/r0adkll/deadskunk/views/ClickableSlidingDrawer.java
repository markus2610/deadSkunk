package com.r0adkll.deadskunk.views;

import com.r0adkll.deadskunk.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class ClickableSlidingDrawer extends SlidingDrawer
{
	private static final String TAG_CLICK_INTERCEPTED = "click_intercepted";

	private ViewGroup mHandleLayout;
	private final Rect mHitRect = new Rect();
	private int mTopOffset;
	private boolean mVertical;

	public ClickableSlidingDrawer(Context context, AttributeSet attrs)
	{
		super(context, attrs, 0);
		
	}

	public ClickableSlidingDrawer(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingDrawer, defStyle, 0);
        int orientation = a.getInt(R.styleable.SlidingDrawer_orientation, ORIENTATION_VERTICAL);
        mTopOffset = (int) a.getDimension(R.styleable.SlidingDrawer_topOffset, 0.0f);
        mVertical = orientation == ORIENTATION_VERTICAL;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);

		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);

		if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED) {
			throw new RuntimeException("SlidingDrawer cannot have UNSPECIFIED dimensions");
		}

		final View handle = getHandle();
		final View content = getContent();
		measureChild(handle, widthMeasureSpec, heightMeasureSpec);


		if (mVertical) {
            int height = heightSpecSize - handle.getMeasuredHeight() - mTopOffset;
            content.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, heightSpecMode));
            heightSpecSize = handle.getMeasuredHeight() + mTopOffset + content.getMeasuredHeight();
            widthSpecSize = content.getMeasuredWidth();
            if (handle.getMeasuredWidth() > widthSpecSize) widthSpecSize = handle.getMeasuredWidth();
        }
        else {
            int width = widthSpecSize - handle.getMeasuredWidth() - mTopOffset;
            getContent().measure(MeasureSpec.makeMeasureSpec(width, widthSpecMode), heightMeasureSpec);
            widthSpecSize = handle.getMeasuredWidth() + mTopOffset + content.getMeasuredWidth();
            heightSpecSize = content.getMeasuredHeight();
            if (handle.getMeasuredHeight() > heightSpecSize) heightSpecSize = handle.getMeasuredHeight();
        }

		setMeasuredDimension(widthSpecSize, heightSpecSize);
	}


	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();

		View handle = getHandle();

		if (handle instanceof ViewGroup)
		{
			mHandleLayout = (ViewGroup) handle;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		if (mHandleLayout != null)
		{
			int clickX = (int) (event.getX() - mHandleLayout.getLeft());
			int clickY = (int) (event.getY() - mHandleLayout.getTop());

			if (isAnyClickableChildHit(mHandleLayout, clickX, clickY))
			{
				return false;
			}
		}
		return super.onInterceptTouchEvent(event);
	}

	private boolean isAnyClickableChildHit(ViewGroup viewGroup, int clickX, int clickY)
	{
		for (int i = 0; i < viewGroup.getChildCount(); i++)
		{
			View childView = viewGroup.getChildAt(i);

			if (TAG_CLICK_INTERCEPTED.equals(childView.getTag()))
			{
				childView.getHitRect(mHitRect);

				if (mHitRect.contains(clickX, clickY))
				{
					return true;
				}
			}

			if (childView instanceof ViewGroup && isAnyClickableChildHit((ViewGroup) childView, clickX, clickY))
			{
				return true;
			}
		}
		return false;
	}
}
