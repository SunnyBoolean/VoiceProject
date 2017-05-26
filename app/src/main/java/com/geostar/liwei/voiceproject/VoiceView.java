package com.geostar.liwei.voiceproject;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liwei on 2017/5/23. 音量调节控件
 */

public class VoiceView extends View {
	/**
	 * 最大音量
	 */
	private int mMaxCount = 30;
	// 每一个音量条宽度
	private int mVoiceBarWidth = 13;

	// 每个柱状条之间的距离
	private int mVoiceBarDistance = 15;
	// 背景音量条的高度
	private int mBgVoiceBarHeight = 90;
	// 音量的高度
	private int mFgVoiceBarHeight = 150;
	// 背景音量条颜色
	private int mBgVoiceColor;
	// 音量条颜色
	private int mVoiceColor;
	// 背景画笔
	private Paint mBgPaint;
	// 音量画笔
	private Paint mPaint;
	//绘制音量条X坐标偏移量
	private final int mXOffset = 15;

	public VoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initArrt(context, attrs, defStyleAttr);
	}

	public VoiceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initArrt(context, attrs, 0);
	}

	public VoiceView(Context context) {
		super(context);
		
	}

	private void initArrt(Context context, AttributeSet attrs, int defStyle) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.VoiceView, defStyle, 0);
		mMaxCount = a.getInt(R.styleable.VoiceView_maxVoice, 30);// 最大音量
		mVoiceBarWidth = (int) a.getDimension(
				R.styleable.VoiceView_voiceBarWidth, (float) 15);// 音量条的宽度
		mVoiceBarDistance = (int) a.getDimension(
				R.styleable.VoiceView_voiceBarDistance, (float) 15.0);// 音量条之间的距离
		mBgVoiceBarHeight = (int) a.getDimension(
				R.styleable.VoiceView_bgVoiceHeight, (float) 90.0);// 背景音乐条的高度
		mFgVoiceBarHeight = (int) a.getDimension(
				R.styleable.VoiceView_fgVoiceHeight, (float) 150.0);// 音乐条的高度
		mBgVoiceColor = a.getColor(R.styleable.VoiceView_bgVoiceColor,
				Color.parseColor("#FF78909C"));// 背景颜色
		mVoiceColor = a.getColor(R.styleable.VoiceView_fgVoiceColor,
				Color.parseColor("#FF03A9F4"));// 颜色
		mVoiceCount = a.getInt(R.styleable.VoiceView_defaultVoice, 15);
		a.recycle();
		initPaint(context);
	}

	int startY = 0;

	/**
	 * 初始化画笔
	 * 
	 * @param context
	 */
	private void initPaint(Context context) {
		mBgPaint = new Paint();
		mBgPaint.setStrokeWidth(mVoiceBarWidth);// 画笔宽度
		mBgPaint.setStyle(Style.FILL_AND_STROKE);
		mBgPaint.setAntiAlias(false);
		mBgPaint.setColor(mBgVoiceColor);

		mPaint = new Paint();
		mPaint.setAntiAlias(false);
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setStrokeWidth(mVoiceBarWidth);
		mPaint.setColor(mVoiceColor);

		startY = (mFgVoiceBarHeight - mBgVoiceBarHeight) / 2;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			int desired = (int) (getPaddingLeft() + mMaxCount*(mVoiceBarDistance+mVoiceBarWidth) + getPaddingRight())+mXOffset;
			width = desired;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			int desired = (int) (getPaddingTop() + mFgVoiceBarHeight + getPaddingBottom());
			height = desired;
		}

		setMeasuredDimension(width, height);
	}

	int mVoiceCount = 10;

	public void addVoice() {
		mVoiceCount++;
		invalidate();
	}

	public void rediceVoice() {
		mVoiceCount--;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < mMaxCount; i++) {
			int startx = i * mVoiceBarWidth + i * mVoiceBarDistance + mXOffset;
			canvas.drawLine(startx, startY, startx, mBgVoiceBarHeight+startY,
					mBgPaint);
		}
		for (int i = 0; i < mVoiceCount; i++) {
			int startx = i * mVoiceBarWidth + i * mVoiceBarDistance + mXOffset;

			canvas.drawLine(startx, 0, startx, mFgVoiceBarHeight, mPaint);
		}
	}

	//
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {

		} else if (action == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();// 手指触摸的位置
			int voice = caculate(x);
			mVoiceCount = voice;
			invalidate();
			if (mVoiceChangeListener != null) {
				mVoiceChangeListener.onVoiceChange(mVoiceCount);
			}

		} else if (action == MotionEvent.ACTION_MOVE) {
			int x = (int) event.getX();
			int voice = caculate(x);
			mVoiceCount = voice;
			invalidate();
			if (mVoiceChangeListener != null) {
				mVoiceChangeListener.onVoiceChange(mVoiceCount);
			}

		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return super.onTouchEvent(event);
	}

	/**
	 * 计算位置
	 * 
	 * @param touchX
	 * @return
	 */
	private int caculate(int touchX) {
		// i*(mBarWidth+mDistance)=touchX;
		int result = (touchX - mXOffset) / (mVoiceBarWidth + mVoiceBarDistance);
		if (result > mMaxCount) {
			result = mMaxCount;
		}
		return result;
	}

	private OnVoicceChangeListener mVoiceChangeListener;

	public void setOnVoiceChangeListener(OnVoicceChangeListener listener) {
		this.mVoiceChangeListener = listener;
	}

	public interface OnVoicceChangeListener {
		public void onVoiceChange(int position);
	}
}
