package com.epoint.epointuniversal;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.epoint.epointuniversal.pjq.R;

public class ScrollTextView extends SurfaceView implements
		SurfaceHolder.Callback {
	private final String TAG = "ScrollTextView";

	private SurfaceHolder holder;
	private Paint paint = null;// 画笔
	public boolean bStop = false; // 停止滚动

	private boolean clickEnable = false; // 可以点击
	private boolean isHorizontal = true; // 水平｜垂直
	private int speed = 3; // 滚动速度
	private String text = "sdfasd dsfsdf sdf ds f";// 文本内容
	private float textSize = 18f; // 字号
	private int textColor = Color.BLACK; // 文字颜色
	// private int times = Integer.MAX_VALUE; // 滚动次数

	private int viewWidth = 0;// 控件的长度
	private int viewHeight = 0; // 控件的高度
	private float textWidth = 0f;// 水平滚动时的文本长度
	private float textHeight = 0f; // 垂直滚动时的文本高度

	private float textX = 0f;// 文字的横坐标
	private float textY = 0f;// 文字的纵坐标
	private float viewWidth_plus_textLength = 0.0f;// 显示总长度
	// private int time = 0; // 已滚动次数
	private boolean timeEnd = false;// 是否到结束时间
	private String endTime = "";// 结束时间

	private ScheduledExecutorService scheduledExecutorService; // 执行滚动线程

	public ScrollTextView(Context context) {
		super(context);
	}

	public ScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = this.getHolder();
		holder.addCallback(this);
		paint = new Paint();
		TypedArray arr = getContext().obtainStyledAttributes(attrs,
				R.styleable.scroll);
		clickEnable = arr.getBoolean(R.styleable.scroll_clickEnable,
				clickEnable);
		isHorizontal = arr.getBoolean(R.styleable.scroll_isHorizontal,
				isHorizontal);
		speed = arr.getInteger(R.styleable.scroll_speed, speed);
		text = arr.getString(R.styleable.scroll_text);
		textColor = arr.getColor(R.styleable.scroll_textColor, textColor);
		textSize = arr.getDimension(R.styleable.scroll_textSize, textSize);
		// times = arr.getInteger(R.styleable.scroll_times, times);
		// time = times;
		paint.setColor(textColor);
		paint.setTextSize(textSize);

		/*
		 * 下面两行代码配合draw()方法中的canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
		 * 将画布填充为透明
		 */
		setZOrderOnTop(true);
		// getHolder().setFormat(PixelFormat.OPAQUE);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		setFocusable(true); // 设置焦点
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		viewWidth = MeasureSpec.getSize(widthMeasureSpec);
		viewHeight = MeasureSpec.getSize(heightMeasureSpec);
		if (isHorizontal) { // 水平滚动
			textWidth = paint.measureText(text);// measure()方法获取text的长度
			viewWidth_plus_textLength = viewWidth + textWidth;
			textY = (viewHeight) / 2 + getPaddingTop() + 10;
		} else { // 垂直滚动
			textHeight = getFontHeight(textSize) * text.length();
			viewWidth_plus_textLength = viewHeight + textHeight;
			textX = (viewWidth - textSize) / 2 + getPaddingLeft()
					- getPaddingRight();
		}
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		bStop = true;
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new ScrollThread(), 1000,
				10, TimeUnit.MILLISECONDS);

	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		bStop = true;
		scheduledExecutorService.shutdown();
		Log.d(TAG, "ScrollTextView is destroyed");
	}

	// 获取字体高度
	public int getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.ascent);
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setTimeEnd() {
		if (!"".equals(endTime) && ListHelper.timeLag(endTime) <= 0) {
			this.timeEnd = true;
		}
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setSpeed(int speed) {
		if (speed > 10 || speed < 0) {
			throw new IllegalArgumentException(
					"Speed was invalid integer, it must between 0 and 10");
		} else {
			this.speed = speed;
		}
	}

	/*
	*//**
	 * 当屏幕被触摸时调用
	 */
	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { if
	 * (!clickEnable) { return true; } switch (event.getAction()) { case
	 * MotionEvent.ACTION_DOWN: bStop = !bStop; if (!bStop && time == 0) { time
	 * = times; } break; } return true; }
	 */

	public synchronized void draw(float X, float Y) {
		Canvas canvas = holder.lockCanvas();
		canvas.drawColor(Color.BLACK);// 通过清屏把画布填充为黑色
		if (isHorizontal) { // 水平滚动
			canvas.drawText(text, X, Y, paint);
		} else { // 垂直滚动
			for (int i = 0; i < text.length(); i++) {
				canvas.drawText(text.charAt(i) + "", X, Y + (i + 1)
						* getFontHeight(textSize), paint);
			}
		}
		holder.unlockCanvasAndPost(canvas);
	}

	class ScrollThread implements Runnable {

		public void run() {
			// TODO Auto-generated method stub

			while (true) {

				// System.out.println("time=" + time);
				if (isHorizontal) {
					// 水平移动
					draw(viewWidth - textX, textY);
					textX += speed;// 速度设置：1-10
					if (textX > viewWidth_plus_textLength) {
						textX = 0;
					}
				} else {
					draw(textX, viewHeight - textY);
					textY += speed;
					if (textY > viewWidth_plus_textLength) {
						textY = 0;
					}
				}
				// setTimeEnd();
				// if (timeEnd == true && textX == 0) {
				// System.out.println("清屏");
				// getHolder().setFormat(PixelFormat.TRANSPARENT);// 设置透明
				// bStop = true;
				// timeEnd = false;
				// }
			}
		}
	}
}
