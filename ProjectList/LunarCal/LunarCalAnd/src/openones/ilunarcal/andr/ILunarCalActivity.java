/**
 *
 */
package openones.ilunarcal.andr;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import openones.ilunarcal.andr.R;
import openones.ilunarcal.andr.VerticalView.OnDateChangedListener;
import openones.ilunarcal.andr.WorkspaceView.OnScreenSelectedListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The Class ILunarCalActivity.
 */
public class ILunarCalActivity extends Activity {

	/** The work. */
	private WorkspaceView work;

	/** The screen listener. */
	private WorkspaceView.OnScreenSelectedListener screenListener;

	/** The calendar config. */
	private GetCalendar calendarConfig = new GetCalendar();

	/** The current day. */
	private Date currentDay;

	/** The cur month. */
	private int curMonth;

	/** The cur year. */
	private int curYear;

	/** The year. */
	private int day, month, year;

	/** The calendar. */
	private Calendar calendar;

	/** The helper. */
	private EventHelper helper = null;

	private OnDateChangedListener onDateChange;

	/** The day create. */
	private static String dayCreate = null;

	/** The Constant DELETE_ALL_DIALOG. */
	private static final int DELETE_ALL_DIALOG = 1;

	/** The Constant DELETED. */
	private static final int DELETED = 2;

	/** The Constant SELECT_ALL. */
	private static final int SELECT_ALL = 3;

	/** The Constant SYNCHRONIZE. */
	private static final int SYNCHRONIZE = 4;

	/** The Constant MONTH_BUTTON. */
	private static final int MONTH_BUTTON = 5;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		helper = new EventHelper(this);

		work = new WorkspaceView(this, null);

		work.setTouchSlop(32);

		// new BackgroundManager();
		Calendar cal = Calendar.getInstance();
		Date date = new Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
		curMonth = date.getMonth();
		curYear = date.getYear();

		calendar = Calendar.getInstance(Locale.getDefault());
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);

		OnScreenSelectedListener screenselect = new WorkspaceView.OnScreenSelectedListener() {

			@Override
			public void onSelected(int paramInt) {
				// TODO Auto-generated method stub
				ILunarCalActivity.this.prepareOtherViews(paramInt);
			}
		};
		this.screenListener = screenselect;
		openones.ilunarcal.andr.VerticalView.OnDateChangedListener onDateChange = new VerticalView.OnDateChangedListener() {

			@Override
			public void onDateChanged(int month, int year) {
				// TODO Auto-generated method stub
				VerticalView view0 = (VerticalView) work.getChildAt(0);
				VerticalView view2 = (VerticalView) work.getChildAt(2);
				VerticalView view1 = (VerticalView) work.getChildAt(1);
				view1.setDate(month, year);
				int newM;
				int newY;
				String strMY = getMYString(month - 1, year);
				newM = Integer.parseInt(strMY.split("-")[0]);
				newY = Integer.parseInt(strMY.split("-")[1]);
				view0.setDate(newM, newY);
				strMY = getMYString(month + 1, year);
				newM = Integer.parseInt(strMY.split("-")[0]);
				newY = Integer.parseInt(strMY.split("-")[1]);
				view2.setDate(newM, newY);
			}
		};
		this.onDateChange = onDateChange;
		currentDay = Calendar.getInstance().getTime();
		work.setOnScreenSelectedListener(screenselect);
		showDate();
		// Add views to the workspace view
		setContentView(work);

	}

	/**
	 * Call intent.
	 * 
	 * @param value
	 *            the value
	 */
	public void callIntent(String value) {
		Intent intent = new Intent(ILunarCalActivity.this, DetailForm.class);
		intent.putExtra("Date", value);
		ILunarCalActivity.this.startActivity(intent);
	}

	/**
	 * Prepare other views.
	 * 
	 * @param paramInt
	 *            the param int
	 */
	protected void prepareOtherViews(int paramInt) {
		if (paramInt == 0) {
			VerticalView view2 = (VerticalView) this.work.getChildAt(2);
			VerticalView view1 = (VerticalView) this.work.getChildAt(1);
			VerticalView view0 = (VerticalView) this.work.getChildAt(0);
			int newM = view0.getCurMonth();
			int newY = view0.getCurYear();
			String strMY = getMYString(newM - 1, newY);
			newM = Integer.parseInt(strMY.split("-")[0]);
			newY = Integer.parseInt(strMY.split("-")[1]);
			curMonth = newM;
			curYear = newY;

			view2.setDate(newM, newY);
			this.work.rotateLastView();

			return;
		}
		if (paramInt != 2) {
			return;
		}
		VerticalView view0 = (VerticalView) this.work.getChildAt(0);
		VerticalView view1 = (VerticalView) this.work.getChildAt(1);
		VerticalView view2 = (VerticalView) this.work.getChildAt(2);
		int newM = view2.getCurMonth();
		int newY = view2.getCurYear();
		String strMY = getMYString(newM + 1, newY);
		newM = Integer.parseInt(strMY.split("-")[0]);
		newY = Integer.parseInt(strMY.split("-")[1]);
		curMonth = newM;
		curYear = newY;
		view0.setDate(newM, newY);
		this.work.rotateFirstView();

	}

	/**
	 * Show date.
	 */
	private void showDate() {
		int i = this.work.getChildCount();
		int j = 0;
		while (true) {

			int k = 3 - i;
			if (j >= k) {

				VerticalView view1 = (VerticalView) this.work.getChildAt(1);
				view1.setDate(curMonth, curYear);

				VerticalView view0 = (VerticalView) this.work.getChildAt(0);
				String strMY = getMYString(curMonth - 1, curYear);
				int newM = Integer.parseInt(strMY.split("-")[0]);
				int newY = Integer.parseInt(strMY.split("-")[1]);
				view0.setDate(newM, newY);

				VerticalView view2 = (VerticalView) this.work.getChildAt(2);
				strMY = getMYString(curMonth + 1, curYear);
				newM = Integer.parseInt(strMY.split("-")[0]);
				newY = Integer.parseInt(strMY.split("-")[1]);
				view2.setDate(newM, newY);
				this.work.showScreen(1);
				return;
			}
			VerticalView newView = new VerticalView(this);
			newView.setOnDateChangedListener(onDateChange);
			this.work.addView(newView);
			j += 1;
		}
	}

	/**
	 * Gets the mY string.
	 * 
	 * @param month
	 *            the month
	 * @param year
	 *            the year
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

	/**
	 * [Explain the description for this method here].
	 * 
	 * @param menu
	 *            the menu
	 * @return true, if successful
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 0, "Tạo sự kiện").setIcon(R.drawable.add);
		menu.add(0, 2, 0, "Xóa sự kiện").setIcon(R.drawable.delete);
		menu.add(0, 3, 0, "Xem sự kiện").setIcon(R.drawable.view);
		menu.add(0, 4, 0, "Đồng bộ hóa").setIcon(R.drawable.synchronize);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * [Explain the description for this method here].
	 * 
	 * @param item
	 *            the item
	 * @return true, if successful
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case 1:
			String date = day + "-" + month + "-" + year;
			callIntent(date);
			return true;
		case 2:
			showDialog(DELETE_ALL_DIALOG);
			return true;
		case 3:
			ListEvent list = new ListEvent();
			list.setSelectAll(true);
			Intent intent1 = new Intent(ILunarCalActivity.this, ListEvent.class);
			startActivity(intent1);
			return true;
		case 4:
			// showDialog(SYNCHRONIZE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Gets the day create.
	 * 
	 * @return the dayCreate
	 */
	public String getDayCreate() {
		return dayCreate;
	}

	/**
	 * Sets the day create.
	 * 
	 * @param dayCreate
	 *            the dayCreate to set
	 */
	public void setDayCreate(String dayCreate) {
		this.dayCreate = dayCreate;
	}

	/**
	 * [Explain the description for this method here].
	 * 
	 * @param id
	 *            the id
	 * @return the dialog
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DELETE_ALL_DIALOG:
			return new AlertDialog.Builder(ILunarCalActivity.this)
					.setIcon(R.drawable.alert_dialog_icon)
					.setTitle("Xóa tất cả sự kiện ?")
					.setMessage(
							"Tất cả các sự kiện sẽ bị xóa vĩnh viễn, bạn chắc chắn chưa ?")
					.setPositiveButton("Đồng ý",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									/* User clicked OK so do some stuff */
									helper.deleteAll();
									showDialog(DELETED);
								}
							})
					.setNegativeButton("Hủy",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									/* User clicked Cancel so do some stuff */
								}
							}).create();
		case DELETED:
			 return new
			 AlertDialog.Builder(ILunarCalActivity.this).setIcon(
			 R.drawable.deleted).setTitle(
			 "Xóa thành công!")
			 .setPositiveButton("OK",
			 new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog,
			 int whichButton) {
			 /* User clicked OK so do some stuff */
			 }
			 }).create();
		case SYNCHRONIZE:
			// return new
			// AlertDialog.Builder(BackupILunarCalActivity.this).setIcon(
			// R.drawable.sad).setTitle(
			// "Em xin lá»—i")
			// .setPositiveButton("OK",
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog,
			// int whichButton) {
			// /* User clicked OK so do some stuff */
			// }
			// }).create();
		case MONTH_BUTTON:
			// return new
			// AlertDialog.Builder(BackupILunarCalActivity.this).setIcon(
			// R.drawable.month_button).setTitle(
			// "Cá»© tá»« tá»«")
			// .setPositiveButton("OK",
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog,
			// int whichButton) {
			// /* User clicked OK so do some stuff */
			// }
			// }).create();
		default:
			break;
		}
		return null;
	}

	/**
	 * [Explain the description for this method here].
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
