/**
 * Copyright 2010 Eric Taix (eric.taix@gmail.com) Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package openones.ilunarcal.andr;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Scroller;

/**
 * The workspace is a wide area with a infinite number of screens. Each screen
 * contains a view. A workspace is meant to be used with a fixed width only.<br/>
 * <br/>
 * This code has been done by using com.android.launcher.Workspace.java
 */
public class VerticalView extends ViewGroup {
	private static final int INVALID_SCREEN = -1;
	public static boolean VERTICAL = false;

	// The velocity at which a fling gesture will cause us to snap to the next
	// screen
	private static final int SNAP_VELOCITY = 500;

	// the default screen index
	private int defaultScreen;
	// The current screen index
	private int currentScreen;
	// The next screen index
	private int nextScreen = INVALID_SCREEN;
	// The newScreen
	private int newScreen = 1;
	// Wallpaper properties
	private Bitmap wallpaper;
	private Paint paint;
	private int wallpaperWidth;
	private int wallpaperHeight;
	private float wallpaperOffset;
	private boolean wallpaperLoaded;
	private boolean firstWallpaperLayout = true;
	public static boolean lock;

	// The scroller which scroll each view
	private Scroller scroller;
	// A tracker which to calculate the velocity of a mouvement
	private VelocityTracker mVelocityTracker;

	// Tha last known values of X and Y
	private float lastMotionX;
	private float lastMotionY;

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;

	// The current touch state
	private int touchState = TOUCH_STATE_REST;
	// The minimal distance of a touch slop
	private int touchSlop;

	// An internal flag to reset long press when user is scrolling
	private boolean allowLongPress;

	private int mScrollY;

	private int mScrollX;

	private boolean rotateFirstView = false;

	private boolean rotateLastView = false;

	private OnScreenSelectedListener listener;

	private boolean mFirstLayout = true;

	OnDateChangedListener onDateChangedListener;
	GetCalendar calendarConfig = new GetCalendar();

	private int curMonth;


	private int curYear;

	public boolean addComplete = true;

	/**
	 * Used to inflate the Workspace from XML.
	 * 
	 * @param context
	 *            The application's context.
	 * @param attrs
	 *            The attribtues set containing the Workspace's customization
	 *            values.
	 */
	public VerticalView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Used to inflate the Workspace from XML.
	 * 
	 * @param context
	 *            The application's context.
	 * @param attrs
	 *            The attribtues set containing the Workspace's customization
	 *            values.
	 * @param defStyle
	 *            Unused.
	 */
	public VerticalView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		defaultScreen = 0;
		initWorkspace();
		// showScreen(1);
	}

	public VerticalView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		initWorkspace();
		listener = new OnScreenSelectedListener() {

			@Override
			public void onSelected(int paramInt) {
				// TODO Auto-generated method stub
				prepareOtherViews(paramInt);
			}
		};
	}

	/**
	 * Initializes various states for this workspace.
	 */
	private void initWorkspace() {
		scroller = new Scroller(getContext());
		currentScreen = defaultScreen;

		paint = new Paint();
		paint.setDither(false);
		touchSlop = ViewConfiguration.getTouchSlop();
	}

	/**
	 * Set a new distance that a touch can wander before we think the user is
	 * scrolling in pixels slop<br/>
	 * 
	 * @param touchSlopP
	 */
	public void setTouchSlop(int touchSlopP) {
		touchSlop = touchSlopP;
	}

	/**
	 * Set the background's wallpaper.
	 */
	public void loadWallpaper(Bitmap bitmap) {
		wallpaper = bitmap;
		wallpaperLoaded = true;
		requestLayout();
		// invalidate();
	}

	boolean isDefaultScreenShowing() {
		return currentScreen == defaultScreen;
	}

	/**
	 * Returns the index of the currently displayed screen.
	 * 
	 * @return The index of the currently displayed screen.
	 */
	int getCurrentScreen() {
		return currentScreen;
	}

	/**
	 * Sets the current screen.
	 * 
	 * @param currentScreen
	 */
	void setCurrentScreen(int currentScreen) {
		currentScreen = Math.max(0, Math.min(currentScreen, getChildCount()));
		scrollTo(0, currentScreen * getHeight());
		// invalidate();
	}

	/**
	 * Shows the default screen (defined by the firstScreen attribute in XML.)
	 */
	void showDefaultScreen() {
		setCurrentScreen(defaultScreen);
	}

	public void rotateFirstView() {
		this.rotateFirstView = true;
		requestLayout();
	}

	public void rotateLastView() {
		this.rotateLastView = true;
		requestLayout();
	}

	/**
	 * Registers the specified listener on each screen contained in this
	 * workspace.
	 * 
	 * @param l
	 *            The listener used to respond to long clicks.
	 */
	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).setOnLongClickListener(l);
		}
	}

	@Override
	public void computeScroll() {

		if (scroller.computeScrollOffset()) {
			int i = scroller.getCurrY();
			this.mScrollY = i;
			int j = this.mScrollY;
			scrollTo(0, j);
			postInvalidate();
			return;
		}
		if (nextScreen == INVALID_SCREEN) {
			return;
		}
		int k = this.nextScreen;
		int m = getChildCount() - 1;
		int n = Math.min(k, m);
		int i1 = Math.max(0, n);
		this.currentScreen = i1;
		int i2 = this.currentScreen;
		fireScreenSelectedEvent(currentScreen);
		nextScreen = -1;

	}

	/**
	 * ViewGroup.dispatchDraw() supports many features we don't need: clip to
	 * padding, layout animation, animation listener, disappearing children,
	 * etc. The following implementation attempts to fast-track the drawing
	 * dispatch by drawing only what we know needs to be drawn.
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if ((touchState != 1) && (this.nextScreen == -1)) {
			int j = this.currentScreen;
			View localView1 = getChildAt(j);
			boolean bool2 = drawChild(canvas, localView1, 0);
			int k = this.currentScreen;
			int m = getHeight();
			int n = k * m;
			this.mScrollY = n;
		} else {
			long l2 = getDrawingTime();
			if (this.nextScreen >= 0) {
				int i1 = this.nextScreen;
				int i2 = getChildCount();
				if (i1 < i2) {
					int i3 = this.currentScreen;
					int i4 = this.nextScreen;
					if (Math.abs(i3 - i4) == 1) {
						int i5 = this.currentScreen;
						View localView2 = getChildAt(i5);
						boolean bool2 = drawChild(canvas, localView2, l2);
						int i6 = this.nextScreen;
						View localView3 = getChildAt(i6);
						boolean bool3 = drawChild(canvas, localView3, l2);
						return;
					}
				}
			}
			int i7 = getChildCount();
			int i8 = 0;
			while (true) {
				if (i8 >= i7)
					return;
				View localView4 = getChildAt(i8);
				boolean bool4 = drawChild(canvas, localView4, l2);
				i8 += 1;
			}
		}
	}

	/**
	 * Measure the workspace AND also children
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (this.rotateLastView)
			rotateLastViewInLayout();
		if (this.rotateFirstView)
			rotateFirstViewInLayout();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int i = View.MeasureSpec.getSize(heightMeasureSpec);
		int j = getChildCount();
		int k = 0;
		while (true) {
			if (k >= j) {
				if (!this.mFirstLayout)
					return;
				// setHorizontalScrollBarEnabled(false);
				setHorizontalScrollBarEnabled(false);
				int m = this.currentScreen * i;
				scrollTo(0, m);
				setHorizontalScrollBarEnabled(true);
				this.mFirstLayout = false;
				return;
			}
			getChildAt(k).measure(widthMeasureSpec, heightMeasureSpec);
			k += 1;
		}
	}

	/**
	 * Overrided method to layout child
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		int childLeft = 0;
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childHeight = child.getMeasuredHeight();
				child.layout(0, childLeft, child.getMeasuredWidth(), childLeft
						+ childHeight);
				childLeft += childHeight;
			}
		}

	}

	/**
	 * This method JUST determines whether we want to intercept the motion. If
	 * we return true, onTouchEvent will be called and we do the actual
	 * scrolling there.
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		/*
		 * Shortcut the most recurring case: the user is in the dragging state
		 * and he is moving his finger. We want to intercept this motion.
		 */
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (touchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			/*
			 * Locally do absolute value. mLastMotionX is set to the y value of
			 * the down event.
			 */
			final int xDiff = (int) Math.abs(x - lastMotionX);
			final int yDiff = (int) Math.abs(y - lastMotionY);
			boolean xMoved = xDiff > touchSlop;
			boolean yMoved = yDiff > touchSlop;

			if (yMoved) {
				touchState = TOUCH_STATE_SCROLLING;
				getParent().requestDisallowInterceptTouchEvent(true);
			} else {
				touchState = TOUCH_STATE_REST;
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;

		case MotionEvent.ACTION_DOWN:
			// Remember location of down touch
			lastMotionX = x;
			lastMotionY = y;
			allowLongPress = true;

			/*
			 * If being flinged and user touches the screen, initiate drag;
			 * otherwise don't. mScroller.isFinished should be false when being
			 * flinged.
			 */
			touchState = scroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			// setCurrentScreen(1);
			touchState = TOUCH_STATE_REST;
			break;
		}

		/*
		 * The only time we want to intercept motion events is if we are in the
		 * drag mode.
		 */
		return touchState != TOUCH_STATE_REST;
	}

	/**
	 * Track the touch event
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.i("Verti", "OnT");
		if (addComplete == false)
			return false;
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}

			// Remember where the motion event started
			lastMotionX = x;
			lastMotionY = y;

			break;
		case MotionEvent.ACTION_MOVE:

			if (touchState == TOUCH_STATE_SCROLLING) {
				// Scroll to follow the motion event
				final int deltaY = (int) (lastMotionY - y);
				lastMotionY = y;

				if (deltaY < 0) {
					if (mScrollY >= 0) {

						scrollBy(0, Math.max(-mScrollY, deltaY));
						mScrollY = getScrollY();
					}
				} else if (deltaY > 0) {

					final int availableToScroll = getChildAt(
							getChildCount() - 1).getBottom()
							- mScrollY - getHeight();

					scrollBy(0, Math.min(availableToScroll, deltaY));
					mScrollY = getScrollY();

				}
			}
			break;
		case MotionEvent.ACTION_UP:

			if (touchState == TOUCH_STATE_SCROLLING) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int velocityY = (int) velocityTracker.getYVelocity();
				if (velocityY > SNAP_VELOCITY && currentScreen > 0) {
					// Fling hard enough to move left
					scrollToScreen(currentScreen - 1);
				} else if (velocityY < -SNAP_VELOCITY
						&& currentScreen < getChildCount() - 1) {
					// Fling hard enough to move right
					scrollToScreen(currentScreen + 1);
				} else {
					snapToDestination();
				}
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
			}
			touchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			touchState = TOUCH_STATE_REST;
		}

		return true;
	}

	/**
	 * Scroll to the appropriated screen depending of the current position
	 */
	private void snapToDestination() {
		final int screenWidth = getHeight();
		final int whichScreen = (getScrollY() + (screenWidth / 2))
				/ screenWidth;

		scrollToScreen(whichScreen);
	}

	/**
	 * Scroll to a specific screen
	 * 
	 * @param whichScreen
	 */
	public void scrollToScreen(int whichScreen) {

		if (!this.scroller.isFinished())
			return;
		boolean changingScreens = whichScreen != currentScreen;

		nextScreen = whichScreen;

		View focusedChild = getFocusedChild();
		if (focusedChild != null && changingScreens
				&& focusedChild == getChildAt(currentScreen)) {
			focusedChild.clearFocus();
		}
		if (!this.scroller.isFinished())
			this.scroller.abortAnimation();
		final int newY = whichScreen * getHeight();
		final int delta = newY - getScrollY();

		scroller.startScroll(0, getScrollY(), 0, delta, Math.abs(delta) * 2);
		invalidate();
	}

	/**
	 * Return the parceable instance to be saved
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		final SavedState state = new SavedState(super.onSaveInstanceState());
		state.currentScreen = currentScreen;
		return state;
	}

	/**
	 * Restore the previous saved current screen
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		if (savedState.currentScreen != -1) {
			currentScreen = savedState.currentScreen;
		}
	}

	/**
	 * Scroll to the left right screen
	 */
	public void scrollLeft() {
		if (nextScreen == INVALID_SCREEN && currentScreen > 0
				&& scroller.isFinished()) {
			scrollToScreen(currentScreen - 1);
		}
	}

	/**
	 * Scroll to the next right screen
	 */
	public void scrollRight() {
		if (nextScreen == INVALID_SCREEN && currentScreen < getChildCount() - 1
				&& scroller.isFinished()) {
			scrollToScreen(currentScreen + 1);
		}
	}

	/**
	 * Return the screen's index where a view has been added to.
	 * 
	 * @param v
	 * @return
	 */
	public int getScreenForView(View v) {
		int result = -1;
		if (v != null) {
			ViewParent vp = v.getParent();
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				if (vp == getChildAt(i)) {
					return i;
				}
			}
		}
		return result;
	}

	/**
	 * Return a view instance according to the tag parameter or null if the view
	 * could not be found
	 * 
	 * @param tag
	 * @return
	 */
	public View getViewForTag(Object tag) {
		int screenCount = getChildCount();
		for (int screen = 0; screen < screenCount; screen++) {
			View child = getChildAt(screen);
			if (child.getTag() == tag) {
				return child;
			}
		}
		return null;
	}

	/**
	 * @return True is long presses are still allowed for the current touch
	 */
	public boolean allowLongPress() {
		return allowLongPress;
	}

	/**
	 * Move to the default screen
	 */
	public void moveToDefaultScreen() {
		scrollToScreen(defaultScreen);
		getChildAt(defaultScreen).requestFocus();
	}

	// ========================= INNER CLASSES ==============================

	/**
	 * A SavedState which save and load the current screen
	 */
	public static class SavedState extends BaseSavedState {
		int currentScreen = -1;

		/**
		 * Internal constructor
		 * 
		 * @param superState
		 */
		SavedState(Parcelable superState) {
			super(superState);
		}

		/**
		 * Private constructor
		 * 
		 * @param in
		 */
		private SavedState(Parcel in) {
			super(in);
			currentScreen = in.readInt();
		}

		/**
		 * Save the current screen
		 */
		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(currentScreen);
		}

		/**
		 * Return a Parcelable creator
		 */
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	// ======================== UTILITIES METHODS ==========================

	/**
	 * Return a centered Bitmap
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @param context
	 * @return
	 */
	static Bitmap centerToFit(Bitmap bitmap, int width, int height,
			Context context) {
		final int bitmapWidth = bitmap.getWidth();
		final int bitmapHeight = bitmap.getHeight();

		if (bitmapWidth < width || bitmapHeight < height) {
			// Normally should get the window_background color of the context
			int color = Integer.valueOf("FF191919", 16);
			Bitmap centered = Bitmap.createBitmap(bitmapWidth < width ? width
					: bitmapWidth, bitmapHeight < height ? height
					: bitmapHeight, Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(centered);
			canvas.drawColor(color);
			canvas.drawBitmap(bitmap, (width - bitmapWidth) / 2.0f,
					(height - bitmapHeight) / 2.0f, null);
			bitmap = centered;
		}
		return bitmap;
	}

	private void rotateFirstViewInLayout() {
		addComplete = false;
		Log.i("roate", "first");
		View localView = getChildAt(0);
		super.removeViewInLayout(localView);
		this.nextScreen = -1;
		int i = this.currentScreen - 1;
		this.currentScreen = i;
		int j = this.currentScreen;
		int k = getHeight();
		int m = j * k;
		this.mScrollY = m;
		int n = this.mScrollY;
		scrollTo(0, n);
		this.rotateFirstView = false;
		int i1 = getChildCount();
		ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(
				-1, -1);
		boolean bool = super.addViewInLayout(localView, i1, localLayoutParams);
		addComplete = bool;
	}

	private void rotateLastViewInLayout() {
		addComplete = false;
		Log.i("roate", "last");
		int i = getChildCount() - 1;
		View localView = getChildAt(i);
		int j = getChildCount() - 1;
		removeViewAt(j);
		this.nextScreen = -1;
		int k = this.currentScreen + 1;
		this.currentScreen = k;
		int m = this.currentScreen;
		int n = getHeight();
		int i1 = m * n;
		this.mScrollY = i1;
		int i2 = this.mScrollY;
		scrollTo(0, i2);
		this.rotateLastView = false;
		ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(
				-1, -1);
		boolean bool = super.addViewInLayout(localView, 0, localLayoutParams);
		addComplete = bool;
	}

	public void showScreen(int paramInt) {
		currentScreen = paramInt;
		nextScreen = -1;
		int i = getHeight() * paramInt;
		mScrollY = i;
		int j = mScrollY;
		scrollTo(0, j);
		postInvalidate();
	}

	private void fireScreenSelectedEvent(int paramInt) {
		if (this.listener == null)
			return;
		this.listener.onSelected(paramInt);
	}

	public OnScreenSelectedListener getOnItemSelectedListener() {
		return this.listener;
	}

	public static abstract interface OnScreenSelectedListener {
		public abstract void onSelected(int paramInt);
	}

	public void setOnScreenSelectedListener(
			OnScreenSelectedListener paramOnScreenSelectedListener) {
		this.listener = paramOnScreenSelectedListener;
	}

	public static abstract interface OnDateChangedListener {
		public abstract void onDateChanged(int month,int year);
	}
	public void setOnDateChangedListener(
			OnDateChangedListener paramOnDateChangedListener) {
		this.onDateChangedListener = paramOnDateChangedListener;
	}

	protected void prepareOtherViews(int paramInt) {
		ViewCalendar localView = (ViewCalendar) getChildAt(paramInt);
		int cMonth = localView.getMonth();
		int cYear = localView.getYear();
		if (paramInt == 0) {
			ViewCalendar View2 = (ViewCalendar) getChildAt(2);
			ViewCalendar View0 = (ViewCalendar) getChildAt(0);
			// String strMY = getMYString(curMonth - 1, curYear);
			int newM = View0.getMonth();
			int newY = View0.getYear();
			String strMY = getMYString(newM, newY - 1);
			newM = Integer.parseInt(strMY.split("-")[0]);
			newY = Integer.parseInt(strMY.split("-")[1]);
			curMonth = newM;
			curYear = newY;
			ArrayList<String> list0Solar = calendarConfig.getSolar(newM, newY);
			ArrayList<String> list2Lunar = calendarConfig.getLunar(newM, newY);
			View2.setDateSolar(list0Solar);
			View2.setDateLunar(list2Lunar);
			View2.setMonth(newM);
			View2.setYear(newY);
			View2.invalidate();
			rotateLastView();
			// View0.invalidate();

			// if (BackgroundManager.haveImage)
			// View2.setBoard(BackgroundManager.getRadomBitmap());
		}
		
		if (paramInt == 2) {
			ViewCalendar View0 = (ViewCalendar) getChildAt(0);
			ViewCalendar View2 = (ViewCalendar) getChildAt(2);
			int newM = View2.getMonth();
			int newY = View2.getYear();
			String strMY = getMYString(newM, newY + 1);
			newM = Integer.parseInt(strMY.split("-")[0]);
			newY = Integer.parseInt(strMY.split("-")[1]);
			curMonth = newM;
			curYear = newY;
			ArrayList<String> list0Solar = calendarConfig.getSolar(newM, newY);
			ArrayList<String> list0Lunar = calendarConfig.getLunar(newM, newY);
			View0.setDateSolar(list0Solar);
			View0.setDateLunar(list0Lunar);
			View0.setMonth(newM);
			View0.setYear(newY);
			View0.invalidate();
			rotateFirstView();
		}
		// View2.invalidate();
		// View0.setDateLunar(list0Lunar);

		// if (BackgroundManager.haveImage)
		// View0.setBoard(BackgroundManager.getRadomBitmap());
		if (this.onDateChangedListener == null)
			return;
		this.onDateChangedListener.onDateChanged(cMonth, cYear);
	}

	String getMYString(int month, int year) {
		if (month == -1) {
			month = 11;
			year = year - 1;
		}
		if (month == 12) {
			month = 0;
			year = year + 1;
		}
		return month + "-" + year;
	}

	public void setDate(int month, int year) {
		// TODO Auto-generated method stub
		int i = getChildCount();
		int j = 0;
		curMonth = month;
		curYear = year;
		while (true) {

			int k = 3 - i;
			if (j >= k) {

				ViewCalendar View1 = (ViewCalendar) getChildAt(1);

				ArrayList<String> list1Solar = calendarConfig.getSolar(month,
						year);
				ArrayList<String> list1Lunar = calendarConfig.getLunar(month,
						year);
				Log.i("firstSize1", list1Lunar.size() + "");
				View1.setDateSolar(list1Solar);
				View1.setDateLunar(list1Lunar);
				View1.setMonth(month);
				View1.setYear(year);
				View1.invalidate();
				// View1.setDate(curMonth, curYear);

				ViewCalendar View0 = (ViewCalendar) getChildAt(0);
				String strMY = getMYString(month, year - 1);
				int newM = Integer.parseInt(strMY.split("-")[0]);
				int newY = Integer.parseInt(strMY.split("-")[1]);
				ArrayList<String> list0Solar = calendarConfig.getSolar(newM,
						newY);
				ArrayList<String> list0Lunar = calendarConfig.getLunar(newM,
						newY);
				View0.setDateSolar(list0Solar);
				View0.setDateLunar(list0Lunar);
				View0.setMonth(newM);
				View0.setYear(newY);
				View0.invalidate();
				// View0.setDate(newM, newY);

				ViewCalendar View2 = (ViewCalendar) getChildAt(2);
				strMY = getMYString(month, year + 1);
				newM = Integer.parseInt(strMY.split("-")[0]);
				newY = Integer.parseInt(strMY.split("-")[1]);
				ArrayList<String> list2Solar = calendarConfig.getSolar(newM,
						newY);
				ArrayList<String> list2Lunar = calendarConfig.getLunar(newM,
						newY);
				View2.setDateSolar(list2Solar);
				View2.setDateLunar(list2Lunar);
				View2.setMonth(newM);
				View2.setYear(newY);
				View2.invalidate();
				// View2.setDate(newM, newY);
				//setOnScreenSelectedListener(null);
				showScreen(1);
				//setOnScreenSelectedListener(listener);
				return;
			}
			ViewCalendar newView = new ViewCalendar(getContext());
			addView(newView);
			j += 1;
		}

	}

	public int getCurMonth() {
		return curMonth;
	}

	public void setCurMonth(int curMonth) {
		this.curMonth = curMonth;
	}

	public int getCurYear() {
		return curYear;
	}

	public void setCurYear(int curYear) {
		this.curYear = curYear;
	}

}
