package openones.ilunarcal.andr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import openones.ilunarcal.andr.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * The Class ViewCalendar.
 */
/**
 * @author dell
 *
 */
public class ViewCalendar extends View {

    /** The image view. */
    private ImageView imageView;

    /** The Constant TAG. */
    private static final String TAG = "ViewCalendar";

    /** The Constant COLUMN. */
    private static final int COLUMN = 7;

    /** The Constant ROW. */
    private static final int ROW = 6;

    /** The lst month. */
    private String[] lstMonth = {"Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7",
            "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};

    /** The lst date. */
    private String[] lstDate = {"CN", "T2", "T3", "T4", "T5", "T6", "T7"};
    
    public ArrayList<String> getBooEvent() {
		return booEvent;
	}

	public void setBooEvent(ArrayList<String> booEvent) {
		this.booEvent = booEvent;
	}

	private String hasEvent = "*";
    private ArrayList<String> booEvent = new ArrayList<String>();

    /** The date solar. */
    private ArrayList<String> dateSolar = new ArrayList<String>();

    /** The date lunar. */
    private ArrayList<String> dateLunar = new ArrayList<String>();

    /** The width. */
    private static float width; // width of one tile

    /** The height. */
    private static float height; // height of one tile

    /** The sel x. */
    private int selX; // X index of selection

    /** The sel y. */
    private int selY; // Y index of selection

    /** The sel rect. */
    private final Rect selRect = new Rect();

    /** The url board. */
    private int urlBoard = R.drawable.a3;

    /** The board. */
    private Bitmap board = BitmapFactory.decodeResource(getResources(), urlBoard);

    /** The touch slop. */
    private int touchSlop;

    /** The year. */
    private int year;

    /** The month. */
    private int month;

    /** The c year. */
    private int cDay, cMonth, cYear;

    /** The calendar. */
    private Calendar calendar;

    /** The draw ye. */
    private Boolean drawYe;

    /** The select row. */
    private int selectRow;

    /** The select colum. */
    private int selectColum;

    /** The last motion x. */
    private float lastMotionX;

    /** The last motion y. */
    private float lastMotionY;

    /** The i lunar. */
    private ILunarCalActivity iLunar;

    /** The word red. */
    private Paint wordRed;

    /** The word lunar. */
    private Paint wordLunar;

    /** The background. */
    private Paint background;

    /** The word bold. */
    private Paint wordBold;

    /** The word blur. */
    private Paint wordBlur;

    /** The line white. */
    private Paint lineWhite;

    /** The word pup. */
    private Paint wordPup;

    /**
     * Gets the board.
     *
     * @return the board
     */
    public Bitmap getBoard() {
        return board;
    }

    /**
     * Sets the board.
     *
     * @param board the new board
     */
    public void setBoard(Bitmap board) {
        this.board = board;
    }

    /**
     * Gets the touch slop.
     *
     * @return the touch slop
     */
    public int getTouchSlop() {
        return touchSlop;
    }

    /**
     * Sets the touch slop.
     *
     * @param touchSlop the new touch slop
     */
    public void setTouchSlop(int touchSlop) {
        this.touchSlop = touchSlop;
    }

    /**
     * Instantiates a new view calendar.
     *
     * @param context the context
     */
    public ViewCalendar(Context context) {

        super(context);

        for (int i = 0; i < 42; i++) {
            dateSolar.add(i + "-" + "c");
        }

        for (int i = 0; i < 42; i++) {
            dateLunar.add(i + "-" + "c");
        }
        
        for (int i = 0; i < 42; i++) {
        	booEvent.add("0");
        	booEvent.add("1");
        }

        calendar = Calendar.getInstance(Locale.getDefault());
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        cMonth = calendar.get(Calendar.MONTH) + 1;
        cYear = calendar.get(Calendar.YEAR);

        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 5;
        setFocusable(true);
        setFocusableInTouchMode(true);
        iLunar = (ILunarCalActivity) context;
        ini();
    }

    /**
     * Gets the url board.
     *
     * @return the url board
     */
    public int getUrlBoard() {
        return urlBoard;
    }

    /**
     * Sets the url board.
     *
     * @param urlBoard the new url board
     */
    public void setUrlBoard(int urlBoard) {
        this.urlBoard = urlBoard;
    }

    /**
     * Ini.
     */
    public void ini() {
        background = new Paint();

        wordBold = new Paint();

        wordBlur = new Paint();

        lineWhite = new Paint();

        wordLunar = new Paint();

        wordRed = new Paint();

        wordPup = new Paint();
    }

    /**
     * [Explain the description for this method here].
     *
     * @param canvas
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    public void onDraw(Canvas canvas) {

        try {
            super.onDraw(canvas);
            // Set background of View
            background.setColor(getResources().getColor(R.color.puzzle_dark));
            canvas.drawBitmap(board, 0, 0, null);
            canvas.drawRect(0, 0, getWidth(), getHeight(), background);

            // Set text with Bold type
            wordBold.setColor(getResources().getColor(R.color.puzzle_light));
            wordBold.setTextSize(width / 2);

            // Set text with Blur type
            wordBlur.setColor(getResources().getColor(R.color.puzzle_dark));
            wordBlur.setTextSize(width / 2);

            // Draw line with White color
            lineWhite.setColor(getResources().getColor(R.color.puzzle_hilite));

            // Draw lunar calendar text
            wordLunar.setColor(getResources().getColor(R.color.puzzle_light));
            wordLunar.setTextSize(width / 3);

            // Draw text with Red color
            wordRed.setColor(getResources().getColor(R.color.puzzle_Red));
            wordRed.setTextSize(width / 2);

            // Draw current date
            wordPup.setColor(getResources().getColor(R.color.puzzle_Yellow));
            wordPup.setTextSize(width / 2);

            // Draw line in front lstDate
            canvas.drawLine(startX(), startY() - width, (width * COLUMN) + startX(), startY() - width, lineWhite);

            // Draw column of dateSolar
            for (int i = 0; i <= COLUMN; i++) {
                for (int j = 0; j <= ROW; j++) {
                    if (i == 0 || i == 7) {
                        canvas.drawLine((i * width) + startX(), startY() - width, (i * width) + startX(),
                                (height * ROW) + startY(), lineWhite);
                    }

                    canvas.drawLine(startX(), (j * height) + startY(), (width * COLUMN) + startX(), (j * height)
                            + startY(), lineWhite);
                }
            }

            // Draw row of dateSolar
            int o = 0;
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COLUMN; j++) {

                    canvas.drawLine((j * width) + startX(), startY(), (j * width) + startX(),
                            (height * ROW) + startY(), lineWhite);

                    canvas.drawText(lstDate[j], ((j * width) + startX() + width / 4), startY() - height / 4, wordBold);

                    // Draw DateLunar
                    String dayLunar = dateLunar.get(o).split("-")[0];

                    if (dateLunar.get(o).split("-")[1].trim().equals("c")) {
                        canvas.drawText(dayLunar, ((j * width) + startX() + width / 2), ((i) * (height)) + startY()
                                + (height * 3 / 4), wordLunar);
                    } else if (dateLunar.get(o).split("-")[1].trim().equals("p")) {
                        canvas.drawText(dayLunar, ((j * width) + startX() + width / 2), ((i) * (height)) + startY()
                                + (height * 3 / 4), wordLunar);
                    } else if (dateLunar.get(o).split("-")[1].trim().equals("n")) {
                        canvas.drawText(dayLunar, ((j * width) + startX() + width / 2), ((i) * (height)) + startY()
                                + (height * 3 / 4), wordLunar);
                    }

                    // Draw DateSolar
                    String daySolar = dateSolar.get(o).split("-")[0];

                    if (dateSolar.get(o).split("-")[1].trim().equals("c")) {
                        if (Integer.parseInt(dateSolar.get(o).split("-")[0]) == cDay
                                && Integer.parseInt(lstMonth[month].split(" ")[1].trim()) == cMonth && year == cYear) {
                            // Get day from list dateSolar
                            canvas.drawText(daySolar, ((j * width) + startX() + width / 4), ((i) * (height)) + startY()
                                    + (height / 2), wordPup);
                            canvas.drawText(hasEvent, ((j * width) + startX()), ((i) * (height)) + startY()
                                    + (height / 3), wordBold);
                        } else if (o % 7 == 0) {
                            canvas.drawText(daySolar, ((j * width) + startX() + width / 4), ((i) * (height)) + startY()
                                    + (height / 2), wordRed);
                        } else {
                            // Get day from list dateSolar
                            canvas.drawText(daySolar, ((j * width) + startX() + width / 4), ((i) * (height)) + startY()
                                    + (height / 2), wordBold);

                        }
                    } else if (dateSolar.get(o).split("-")[1].trim().equals("p")) {
                        // Get day from list dateSolar
                        canvas.drawText(daySolar, ((j * width) + startX() + width / 4), ((i) * (height)) + startY()
                                + (height / 2), wordBlur);
                    } else if (dateSolar.get(o).split("-")[1].trim().equals("n")) {
                        // Get day from list dateSolar
                        canvas.drawText(daySolar, ((j * width) + startX() + width / 4), ((i) * (height)) + startY()
                                + (height / 2), wordBlur);
                    }
                    o = o + 1;
                }
            }

            // Draw lstMonth
                wordBold.setTextSize(getWidth() / 8);
                canvas.drawText(lstMonth[month] + " - " + year, (width / 2),
                        (getHeight() - height * ROW) / 2, wordBold);
                // drawYe = true;

            Paint selected = new Paint();
            selected.setColor(getResources().getColor(R.color.puzzle_selected));
            canvas.drawRect(selRect, selected);

        } catch (Exception e) {
            Toast.makeText(getContext(), "onDraw: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    // public void select(int x, int y, int preCo, int preRo) {
    /**
     * Select.
     *
     * @param preCo the pre co
     * @param preRo the pre ro
     */
    public void select(int preCo, int preRo) {
        selX = Math.min(Math.max(preCo, 0), COLUMN - 1);

        selY = Math.min(Math.max(preRo, 0), ROW - 1);

        if (preCo == selectColum && preRo == selectRow) {
            String daySolar = dateSolar.get(selectColum + selectRow * 7);
            String day = daySolar.split("-")[0];
            String newMY;
            String newM;
            String newY;
            if (daySolar.split("-")[1].equals("p")) {
                newMY = getMYString(month - 1, year);
                newM = newMY.split("-")[0];
                newY = newMY.split("-")[1];
            } else if (daySolar.split("-")[1].equals("n")) {
                newMY = getMYString(month + 1, year);
                newM = newMY.split("-")[0];
                newY = newMY.split("-")[1];
            } else {
                newM = month + "";
                newY = year + "";
            }
            String date = day + "-" + (Integer.parseInt(newM) + 1) + "-" + newY;
            iLunar.callIntent(date);
        } else {
            getRect(selX, selY, selRect);
        }

        invalidate();
    }

    /**
     * [Explain the description for this method here].
     *
     * @param w the w
     * @param h the h
     * @param oldw the oldw
     * @param oldh the oldh
     * @see android.view.View#onSizeChanged(int, int, int, int)
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        width = (w / COLUMN);

        height = (h / ROW);

        if (width < height) {
            // width = height;
            height = width + startY();
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * Gets the rect.
     *
     * @param x the x
     * @param y the y
     * @param rect the rect
     */
    private void getRect(int x, int y, Rect rect) {
        rect.set((int) ((x * width) + (startX())), (int) ((y * height) + startY()),
                (int) ((x * width + width) + (startX())), (int) ((y * height + height) + startY()));
        selectColum = x;
        selectRow = y;
    }

    /**
     * Start x.
     *
     * @return the float
     */
    public float startX() {
        return ((getWidth() - width * COLUMN) / 2);
    }

    // lay so du
    /**
     * Start y.
     *
     * @return the float
     */
    public float startY() {
        return ((getHeight() - height * ROW));
    }

    /**
     * [Explain the description for this method here].
     *
     * @param event the event
     * @return tag
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action) {
            // MotionEvent class constant signifying a finger-down event
            case MotionEvent.ACTION_DOWN : {

                if (event.getY() > startY()) {
                    select((int) ((event.getX() - startX()) / (width)), (int) ((event.getY() - startY()) / (height)));
                }

                float f1 = event.getX();
                this.lastMotionX = f1;
                float f2 = event.getY();
                this.lastMotionY = f2;
                break;
            }
                // MotionEvent class constant signifying a finger-drag event
            case MotionEvent.ACTION_MOVE : {
                float f1 = event.getX();
                float f2 = this.lastMotionX;
                int k = (int) Math.abs(f1 - f2);
                float f3 = event.getY();
                float f4 = this.lastMotionY;
                int m = (int) Math.abs(f3 - f4);
                if ((k >= this.touchSlop) && (m > this.touchSlop)) {

                }
                break;
            }
                // MotionEvent class constant signifying a finger-up event
            case MotionEvent.ACTION_UP : {
                break;
            }
            default :
                break;

        }
        // game.calintent();
        return true;
    }

    /**
     * Gets the date solar.
     *
     * @return the date solar
     */
    public ArrayList<String> getDateSolar() {
        return dateSolar;
    }

    /**
     * Sets the date solar.
     *
     * @param dateSolar the new date solar
     */
    public void setDateSolar(ArrayList<String> dateSolar) {
        this.dateSolar = dateSolar;
    }

    /**
     * Gets the date lunar.
     *
     * @return the date lunar
     */
    public ArrayList<String> getDateLunar() {
        return dateLunar;
    }

    /**
     * Sets the date lunar.
     *
     * @param dateLunar the new date lunar
     */
    public void setDateLunar(ArrayList<String> dateLunar) {
        this.dateLunar = dateLunar;
    }

    /**
     * Gets the year.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year.
     *
     * @param year the new year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the month.
     *
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets the month.
     *
     * @param month the new month
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Gets the mY string.
     *
     * @param month the month
     * @param year the year
     * @return the mY string
     */
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
}
