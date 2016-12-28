package com.my.targetDemoApp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MidrollSeekbar extends View
{
	private static final int SEEKBAR_COLOR = 0xFF168DE2;
	private static final int POINT_COLOR = 0xFFFFA930;

	Paint line = new Paint();
	ArrayList<Paint> midrollPaints = new ArrayList<>();
	private int max;
	private int progress;
	private int height;
	private float[] midrolls;

	public void setHeight(int height)
	{
		this.height = height;
		//noinspection SuspiciousNameCombination
		line.setStrokeWidth(height);
	}

	public void setMax(int max)
	{
		this.max = max;
	}

	public void setProgress(int progress)
	{
		this.progress = progress;
		invalidate();
	}

	public void setupMidrolls(float[] midrollPoints)
	{
		this.midrolls = midrollPoints;
		midrollPaints.clear();
		for (float midrollPoint : midrollPoints)
		{
			Paint p = new Paint();
			p.setColor(POINT_COLOR);
			p.setStrokeWidth(20);
			p.setStyle(Paint.Style.STROKE);
			p.setStrokeJoin(Paint.Join.ROUND);
			midrollPaints.add(p);
		}
	}

	public MidrollSeekbar(Context context)
	{
		this(context, null);
	}

	public MidrollSeekbar(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public MidrollSeekbar(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		height = 20;
		setBackgroundColor(Color.LTGRAY);
		line.setColor(SEEKBAR_COLOR);
		line.setStrokeWidth(20);
		line.setStyle(Paint.Style.STROKE);
		line.setStrokeJoin(Paint.Join.ROUND);

		setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (getWidth() != 0 && getHeight() != 0 && max != 0 && progress != 0)
		{
			float progressWidth = ((float) getWidth() / max) * progress;
			canvas.drawLine(0, getHeight() / 2, progressWidth, getHeight() / 2, line);
			for (int i = 0; i < midrollPaints.size(); i++)
			{
				Paint midrollPaint = midrollPaints.get(i);

				canvas.drawLine(((float) getWidth() / max) * midrolls[i] * 1000,
						getHeight() / 2,
						((float) getWidth() / max) * midrolls[i] * 1000 + 20,
						getHeight() / 2,
						midrollPaint);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = MeasureSpec.getSize(widthMeasureSpec);

		widthMeasureSpec = MeasureSpec.makeMeasureSpec(width - getPaddingRight() -
				getPaddingLeft(),
				MeasureSpec.getMode(widthMeasureSpec));

		setMeasuredDimension(widthMeasureSpec,
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
	}
}
