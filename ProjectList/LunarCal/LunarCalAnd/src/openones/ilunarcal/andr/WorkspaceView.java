/**
 * Copyright 2010 Eric Taix (eric.taix@gmail.com) Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package openones.ilunarcal.andr;

import java.sql.Date;

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
public class WorkspaceView extends ViewGroup {

    /** The Constant INVALID_SCREEN. */
    private static final int INVALID_SCREEN = -1;

    /** The Constant SNAP_VELOCITY. */
    private static final int SNAP_VELOCITY = 1000;

    /** The default screen. */
    private int defaultScreen;

    /** The current screen. */
    private int currentScreen;

    /** The next screen. */
    private int nextScreen = INVALID_SCREEN;

    /** The new screen. */
    private int newScreen = 1;

    /** The wallpaper. */
    private Bitmap wallpaper;

    /** The paint. */
    private Paint paint;

    /** The wallpaper width. */
    private int wallpaperWidth;

    /** The wallpaper height. */
    private int wallpaperHeight;

    /** The wallpaper offset. */
    private float wallpaperOffset;

    /** The wallpaper loaded. */
    private boolean wallpaperLoaded;

    /** The first wallpaper layout. */
    private boolean firstWallpaperLayout = true;

    /** The lock. */
    private static boolean lock;

    // The scroller which scroll each view
    /** The scroller. */
    private Scroller scroller;
    // A tracker which to calculate the velocity of a mouvement
    /** The m velocity tracker. */
    private VelocityTracker mVelocityTracker;

    // Tha last known values of X and Y
    /** The last motion x. */
    private float lastMotionX;

    /** The last motion y. */
    private float lastMotionY;

    /** The Constant TOUCH_STATE_REST. */
    static final int TOUCH_STATE_REST = 0;

    /** The Constant TOUCH_STATE_SCROLLING. */
    static final int TOUCH_STATE_SCROLLING = 1;

    // The current touch state
    /** The touch state. */
    private int touchState = TOUCH_STATE_REST;
    // The minimal distance of a touch slop
    /** The touch slop. */
    private int touchSlop;

    // An internal flag to reset long press when user is scrolling
    /** The allow long press. */
    private boolean allowLongPress;

    /** The m scroll y. */
    private int mScrollY;

    /** The m scroll x. */
    private int mScrollX;

    /** The rotate first view. */
    private boolean rotateFirstView = false;

    /** The rotate last view. */
    private boolean rotateLastView = false;

    /** The listener. */
    private OnScreenSelectedListener listener;

    /** The m first layout. */
    private boolean mFirstLayout = true;

    /** The on date changed listener. */
    private OnDateChangedListener onDateChangedListener;

    /**
     * Used to inflate the Workspace from XML.
     *
     * @param context
     *            The application's context.
     * @param attrs
     *            The attribtues set containing the Workspace's customization
     *            values.
     */
    public WorkspaceView(Context context, AttributeSet attrs) {
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
    public WorkspaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        defaultScreen = 0;
        initWorkspace();
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
     * scrolling in pixels slop<br/>.
     *
     * @param touchSlopP the new touch slop
     */
    public void setTouchSlop(int touchSlopP) {
        touchSlop = touchSlopP;
    }

    /**
     * Set the background's wallpaper.
     *
     * @param bitmap the bitmap
     */
    public void loadWallpaper(Bitmap bitmap) {
        wallpaper = bitmap;
        wallpaperLoaded = true;
        requestLayout();
        //invalidate();
    }

    /**
     * Checks if is default screen showing.
     *
     * @return true, if is default screen showing
     */
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
     * @param currentScreen the new current screen
     */
    void setCurrentScreen(int currentScreen) {
        currentScreen = Math.max(0, Math.min(currentScreen, getChildCount()));
        scrollTo(currentScreen * getWidth(), 0);
        //invalidate();
    }

    /**
     * Show default screen.
     */
    void showDefaultScreen() {
        setCurrentScreen(defaultScreen);
    }

    /**
     * Rotate first view.
     */
    public void rotateFirstView() {
        this.rotateFirstView = true;
        requestLayout();
    }

    /**
     * Rotate last view.
     */
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

    /**
     * [Explain the description for this method here].
     * @see android.view.View#computeScroll()
     */
    @Override
    public void computeScroll() {

        if (scroller.computeScrollOffset()) {
            int i = scroller.getCurrX();
            this.mScrollX = i;
            int j = this.mScrollX;
            scrollTo(j, 0);
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
     *
     * @param canvas the canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if ((touchState != 1) && (this.nextScreen == -1)) {
            int j = this.currentScreen;
            View localView1 = getChildAt(j);
            boolean bool2 = drawChild(canvas, localView1, 0);
            int k = this.currentScreen;
            int m = getWidth();
            int n = k * m;
            this.mScrollX = n;
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
                if (i8 >= i7) {
                    return;
                }
                View localView4 = getChildAt(i8);
                boolean bool4 = drawChild(canvas, localView4, l2);
                i8 += 1;
            }
        }
    }

    /**
     * Measure the workspace AND also children.
     *
     * @param widthMeasureSpec the width measure spec
     * @param heightMeasureSpec the height measure spec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.rotateLastView) {
            rotateLastViewInLayout();
        }
        if (this.rotateFirstView) {
            rotateFirstViewInLayout();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int i = View.MeasureSpec.getSize(widthMeasureSpec);
        int j = getChildCount();
        int k = 0;
        while (true) {
            if (k >= j) {
                if (!this.mFirstLayout) {
                    return;
                }
                setHorizontalScrollBarEnabled(false);
                int m = this.currentScreen * i;
                scrollTo(m, 0);
                setHorizontalScrollBarEnabled(true);
                this.mFirstLayout = false;
                return;
            }
            getChildAt(k).measure(widthMeasureSpec, heightMeasureSpec);
            k += 1;
        }
    }

    /**
     * Overrided method to layout child.
     *
     * @param changed the changed
     * @param left the left
     * @param top the top
     * @param right the right
     * @param bottom the bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        int childLeft = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
                child.layout(childLeft, 0, childLeft + childWidth,
                        child.getMeasuredHeight());
                childLeft += childWidth;
            }
        }

    }

    /**
     * This method JUST determines whether we want to intercept the motion. If
     * we return true, onTouchEvent will be called and we do the actual
     * scrolling there.
     *
     * @param ev the ev
     * @return true, if successful
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

            if (xMoved) {
                touchState = TOUCH_STATE_SCROLLING;
            } else {
                touchState = TOUCH_STATE_REST;
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
            default :
                break;
        }

        /*
         * The only time we want to intercept motion events is if we are in the
         * drag mode.
         */
        return touchState != TOUCH_STATE_REST;
    }

    /**
     * Track the touch event.
     *
     * @param ev the ev
     * @return true, if successful
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        final int action = ev.getAction();
        final float x = ev.getX();

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

            break;
        case MotionEvent.ACTION_MOVE:

            if (touchState == TOUCH_STATE_SCROLLING) {
                // Scroll to follow the motion event
                final int deltaX = (int) (lastMotionX - x);
                lastMotionX = x;

                if (deltaX < 0) {
                    if (getScrollX() > 0) {

                        scrollBy(Math.max(-getScrollX(), deltaX), 0);
                    }
                } else if (deltaX > 0) {

                    final int availableToScroll = getChildAt(
                            getChildCount() - 1).getRight()
                            - getScrollX() - getWidth();

                    if (availableToScroll > 0) {
                        scrollBy(Math.min(availableToScroll, deltaX), 0);
                    }
                }
            }
            break;
        case MotionEvent.ACTION_UP:

            if (touchState == TOUCH_STATE_SCROLLING) {
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();
                if (velocityX > SNAP_VELOCITY && currentScreen > 0) {
                    // Fling hard enough to move left
                    scrollToScreen(currentScreen - 1);
                } else if (velocityX < -SNAP_VELOCITY
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
            default :
                break;
        }

        return true;
    }

    /**
     * Scroll to the appropriated screen depending of the current position.
     */
    private void snapToDestination() {
        final int screenWidth = getWidth();
        final int whichScreen = (getScrollX() + (screenWidth / 2))
                / screenWidth;

        scrollToScreen(whichScreen);
    }

    /**
     * Scroll to a specific screen.
     *
     * @param whichScreen the which screen
     */
    public void scrollToScreen(int whichScreen) {

        boolean changingScreens = whichScreen != currentScreen;

        nextScreen = whichScreen;

        View focusedChild = getFocusedChild();
        if (focusedChild != null && changingScreens
                && focusedChild == getChildAt(currentScreen)) {
            focusedChild.clearFocus();
        }

        final int newX = whichScreen * getWidth();
        final int delta = newX - getScrollX();

        scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
        invalidate();
    }

    /**
     * Return the parceable instance to be saved.
     *
     * @return the parcelable
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        final SavedState state = new SavedState(super.onSaveInstanceState());
        state.currentScreen = currentScreen;
        return state;
    }

    /**
     * Restore the previous saved current screen.
     *
     * @param state the state
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
     * Scroll to the left right screen.
     */
    public void scrollLeft() {
        if (nextScreen == INVALID_SCREEN && currentScreen > 0
                && scroller.isFinished()) {
            scrollToScreen(currentScreen - 1);
        }
    }

    /**
     * Scroll to the next right screen.
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
     * @param v the v
     * @return the screen for view
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
     * could not be found.
     *
     * @param tag the tag
     * @return the view for tag
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
     * Allow long press.
     *
     * @return True is long presses are still allowed for the current touch
     */
    public boolean allowLongPress() {
        return allowLongPress;
    }

    /**
     * Move to the default screen.
     */
    public void moveToDefaultScreen() {
        scrollToScreen(defaultScreen);
        getChildAt(defaultScreen).requestFocus();
    }

    // ========================= INNER CLASSES ==============================

    /**
     * A SavedState which save and load the current screen.
     */
    public static class SavedState extends BaseSavedState {

        /** The current screen. */
        private int currentScreen = -1;

        /**
         * Internal constructor.
         *
         * @param superState the super state
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Private constructor.
         *
         * @param in the in
         */
        private SavedState(Parcel in) {
            super(in);
            currentScreen = in.readInt();
        }

        /**
         * Save the current screen.
         *
         * @param out the out
         * @param flags the flags
         */
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentScreen);
        }

        /** Return a Parcelable creator. */
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
     * Return a centered Bitmap.
     *
     * @param bitmap the bitmap
     * @param width the width
     * @param height the height
     * @param context the context
     * @return the bitmap
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

    /**
     * Rotate first view in layout.
     */
    private void rotateFirstViewInLayout() {
        Log.i("roate", "first");
        View localView = getChildAt(0);
        super.removeViewInLayout(localView);
        int i = this.currentScreen - 1;
        this.currentScreen = i;
        this.nextScreen = -1;
        int j = this.currentScreen;
        int k = getWidth();
        int m = j * k;
        this.mScrollX = m;
        int n = this.mScrollX;
        scrollTo(n, 0);
        this.rotateFirstView = false;
        int i1 = getChildCount();
        ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(
                -1, -1);
        boolean bool = super.addViewInLayout(localView, i1, localLayoutParams);
    }

    /**
     * Rotate last view in layout.
     */
    private void rotateLastViewInLayout() {
        Log.i("roate", "last");
        int i = getChildCount() - 1;
        View localView = getChildAt(i);
        int j = getChildCount() - 1;
        removeViewAt(j);
        int k = this.currentScreen + 1;
        this.currentScreen = k;
        this.nextScreen = -1;
        int m = this.currentScreen;
        int n = getWidth();
        int i1 = m * n;
        this.mScrollX = i1;
        int i2 = this.mScrollX;
        scrollTo(i2, 0);
        this.rotateLastView = false;
        ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(
                -1, -1);
        boolean bool = super.addViewInLayout(localView, 0, localLayoutParams);
    }

    /**
     * Show screen.
     *
     * @param paramInt the param int
     */
    public void showScreen(int paramInt) {
        currentScreen = paramInt;
        nextScreen = -1;
        int i = getWidth() * paramInt;
        mScrollX = i;
        int j = mScrollX;
        scrollTo(j, 0);
        postInvalidate();
    }

    /**
     * Fire screen selected event.
     *
     * @param paramInt the param int
     */
    private void fireScreenSelectedEvent(int paramInt) {
        if (this.listener == null) {
            return;
        }
        this.listener.onSelected(paramInt);
    }

    /**
     * Gets the on item selected listener.
     *
     * @return the on item selected listener
     */
    public OnScreenSelectedListener getOnItemSelectedListener() {
        return this.listener;
    }

    /**
     * The listener interface for receiving onScreenSelected events.
     * The class that is interested in processing a onScreenSelected
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's .When
     * the onScreenSelected event occurs, that object's appropriate
     * method is invoked.
     *
     * @see OnScreenSelectedEvent
     */
    public static abstract interface OnScreenSelectedListener {
        /**
         * On selected.
         *
         * @param paramInt the param int
         */
        public abstract void onSelected(int paramInt);
    }

    /**
     * Sets the on screen selected listener.
     *
     * @param paramOnScreenSelectedListener the new on screen selected listener
     */
    public void setOnScreenSelectedListener(
            OnScreenSelectedListener paramOnScreenSelectedListener) {
        this.listener = paramOnScreenSelectedListener;
    }

    /**
     * Sets the on date changed listener.
     *
     * @param paramOnDateChangedListener the new on date changed listener
     */
    public void setOnDateChangedListener(
            OnDateChangedListener paramOnDateChangedListener) {
        this.onDateChangedListener = paramOnDateChangedListener;
    }

    /**
     * The listener interface for receiving onDateChanged events.
     * The class that is interested in processing a onDateChanged
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's. When
     * the onDateChanged event occurs, that object's appropriate
     * method is invoked.
     *
     * @see OnDateChangedEvent
     */
    public static abstract interface OnDateChangedListener {
        /**
         * On date changed.
         *
         * @param paramDate the param date
         */
        public abstract void onDateChanged(Date paramDate);
    }

}
