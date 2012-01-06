/**
 * 
 */
package openones.ilunarcal.andr;

import java.util.Calendar;

import openones.ilunarcal.andr.R;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;

/**
 * @author mastertan
 * 
 */
public class DetailForm extends Activity implements OnClickListener {
	private EventHelper helper = null;

	private EditText edt_EventName;
	private Spinner edt_EventType;
	private EditText edt_Description;
	private EditText edt_StartDate;
	private EditText edt_StartTime;
	private EditText edt_EndDate;
	private EditText edt_EndTime;
	private EditText edt_Location;
	private RadioButton rb_Solar;
	private RadioButton rb_Lunar;
	private Spinner edt_Repeat;
	private Spinner edt_Remind;
	static final int TIME_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	static final int REPEAT_DIALOG_ID = 2;
	static final int REMIND_DIALOG_ID = 3;

	private int mHour;
	private int mMinute;
	private int timePicker = 0;
	private int datePicker = 0;

	private int mYear;
	private int mMonth;
	private int mDay;

	private int tmpYear_Start;
	private int tmpMonth_Start;
	private int tmpDay_Start;

	private int tmpYear_End;
	private int tmpMonth_End;
	private int tmpDay_End;

	private Button save = null;
	private Button cancel = null;
	private int typeSel = 0;
	private int repeatSel = 0;
	private int remindSel = 0;
	private int posType = 0;
	private int posRepeat = 0;
	private int posRemind = 0;

	private String[] dayPicker = null;
	private String[] endDayPicker = null;

	String curClickDate = null;
	ILunarCalActivity day2 = new ILunarCalActivity();
	String eventId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);

		helper = new EventHelper(this);

		edt_EventName = (EditText) findViewById(R.id.event_name);
		edt_EventType = (Spinner) findViewById(R.id.spinEventType);
		edt_Description = (EditText) findViewById(R.id.description);
		edt_Location = (EditText) findViewById(R.id.editLocation);
		edt_StartDate = (EditText) findViewById(R.id.editStartDay);
		edt_StartDate.setOnClickListener(this);
		edt_EndDate = (EditText) findViewById(R.id.editEndDay);
		edt_EndDate.setOnClickListener(this);
		edt_StartTime = (EditText) findViewById(R.id.editStartTime);
		edt_StartTime.setOnClickListener(this);
		edt_EndTime = (EditText) findViewById(R.id.editEndTime);
		edt_EndTime.setOnClickListener(this);
		edt_Repeat = (Spinner) findViewById(R.id.spinRepeat);
		edt_Remind = (Spinner) findViewById(R.id.spinRemind);
		rb_Solar = (RadioButton) findViewById(R.id.rbtnSolarDate);
		rb_Lunar = (RadioButton) findViewById(R.id.rbtnLunarDate);

		curClickDate = getIntent().getStringExtra("Date");
		// abc = 1 + "-" + 2 + "-" + 2011;
		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);

		save.setOnClickListener(onSave);
		cancel.setOnClickListener(onCancel);

		eventId = getIntent().getStringExtra(ListEvent.ID_EXTRA);

		if (eventId != null) {
			// if (curClickDate != null) {
			save.setText("Cập nhật");
			load();
		}

		ArrayAdapter<CharSequence> adapterEventType = ArrayAdapter
				.createFromResource(this, R.array.eventTypes,
						android.R.layout.simple_spinner_item);
		adapterEventType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edt_EventType.setAdapter(adapterEventType);
		edt_EventType.setSelection(typeSel);
		edt_EventType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				posType = position;
				if (posType == 0 || posType == 3){
					edt_Repeat.setSelection(0);
					edt_Remind.setSelection(0);
				}
				if (posType == 1) {
					edt_Repeat.setSelection(3);
					edt_Remind.setSelection(3);
					rb_Solar.setChecked(true);
					rb_Lunar.setChecked(false);
				}
				if (posType == 2) {
					edt_Repeat.setSelection(3);
					edt_Remind.setSelection(3);
					rb_Lunar.setChecked(true);
					rb_Solar.setChecked(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				posType = 0;
			}
		});

		ArrayAdapter<CharSequence> adapterRepeat = ArrayAdapter
				.createFromResource(this, R.array.repeat,
						android.R.layout.simple_spinner_item);
		adapterRepeat
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edt_Repeat.setAdapter(adapterRepeat);
		edt_Repeat.setSelection(repeatSel);
		edt_Repeat.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				posRepeat = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				posRepeat = 0;
			}
		});

		ArrayAdapter<CharSequence> adapterRemind = ArrayAdapter
				.createFromResource(this, R.array.remind,
						android.R.layout.simple_spinner_item);
		adapterRemind
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edt_Remind.setAdapter(adapterRemind);
		edt_Remind.setSelection(remindSel);
		edt_Remind.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				posRemind = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				posRemind = 0;
			}
		});

		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		updateDisplayTime();

		if (curClickDate != null) {
			dayPicker = curClickDate.split("-");
		} else {
			Cursor cus = helper.getEvent(eventId);
			cus.moveToFirst();
			String day = helper.getStartDay(cus);
			dayPicker = day.split("-");
		}
		mDay = Integer.parseInt(dayPicker[0]);
		mMonth = Integer.parseInt(dayPicker[1]);
		mYear = Integer.parseInt(dayPicker[2]);

		updateDisplayDate();

	}

	private void updateDisplayDate() {
		// TODO Auto-generated method stub
		if (datePicker == 0) {
			edt_StartDate.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(mDay).append("-").append(mMonth).append("-")
					.append(mYear).append(""));
			edt_EndDate.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(mDay).append("-").append(mMonth).append("-")
					.append(mYear).append(""));
		}
		if (datePicker == 1) {
			edt_StartDate.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(mDay).append("-").append(mMonth + 1).append("-")
					.append(mYear).append(""));
		}
		if (datePicker == 2) {
			edt_EndDate.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(mDay).append("-").append(mMonth + 1).append("-")
					.append(mYear).append(""));
		}
	}

	private void updateDisplayTime() {
		// TODO Auto-generated method stub
		if (timePicker == 0) {
			edt_StartTime.setText(new StringBuilder().append(pad(mHour))
					.append(":").append(pad(mMinute)));
			edt_EndTime.setText(new StringBuilder().append(pad(mHour))
					.append(":").append(pad(mMinute)));
		}
		if (timePicker == 1) {
			edt_StartTime.setText(new StringBuilder().append(pad(mHour))
					.append(":").append(pad(mMinute)));
		}
		if (timePicker == 2) {
			edt_EndTime.setText(new StringBuilder().append(pad(mHour))
					.append(":").append(pad(mMinute)));
		}
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		helper.close();
	}

	private void load() {
		Cursor c = helper.getEvent(eventId);
		// Cursor c = helper.getEvent(curClickDate);

		c.moveToFirst();
		edt_EventName.setText(helper.getEventName(c));
		edt_Description.setText(helper.getDescription(c));
		if (helper.getType(c).equals("Ngày Giỗ")) {
			typeSel = 0;
		} else if (helper.getType(c).equals("Ngày Sinh Nhật")) {
			typeSel = 1;
		} else if (helper.getType(c).equals("Tiệc")) {
			typeSel = 2;
		} else if (helper.getType(c).equals("Khác")) {
			typeSel = 3;
		}
		edt_Location.setText(helper.getLocation(c));
		edt_StartDate.setText(helper.getStartDay(c));
		edt_EndDate.setText(helper.getEndDay(c));
		edt_StartTime.setText(helper.getStartTime(c));
		edt_EndTime.setText(helper.getEndTime(c));
		if (helper.getRepeat(c) == "Không lặp lại") {
			repeatSel = 0;
		} else if (helper.getRepeat(c).equals("Theo ngày")) {
			repeatSel = 1;
		} else if (helper.getRepeat(c).equals("Theo tuần")) {
			repeatSel = 2;
		} else if (helper.getRepeat(c).equals("Theo tháng")) {
			repeatSel = 3;
		} else if (helper.getRepeat(c).equals("Theo năm")) {
			repeatSel = 4;
		}
		String a = helper.getRemind(c);
		String a1 = a;
		if (helper.getRemind(c).equals("Không nhắc nhở")) {
			remindSel = 0;
		} else if (helper.getRemind(c).equals("Đúng thời gian")) {
			remindSel = 1;
		} else if (helper.getRemind(c).equals("Trước 30 phút")) {
			remindSel = 2;
		} else if (helper.getRemind(c).equals("Trước 1 giờ")) {
			remindSel = 3;
		} else if (helper.getRemind(c).equals("Trước 1 ngày")) {
			remindSel = 4;
		} else if (helper.getRemind(c).equals("Tùy chỉnh")) {
			remindSel = 5;
		} else {
			remindSel = 0;
		}
		c.close();
	}

	private View.OnClickListener onSave = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String type = "0";
			String dmy = mDay + "-" + mMonth + "-" + mYear;
			if (eventId == null) {
				// if (curClickDate == null) {
				if (rb_Lunar.isChecked()) {
					type = "0";
				} else
					type = "1";
				helper.insert(edt_EventName.getText().toString(),
						edt_Description.getText().toString(), dmy,
						edt_EventType.getItemAtPosition(posType).toString(),
						edt_Location.getText().toString(), edt_StartDate
								.getText().toString(), edt_EndDate.getText()
								.toString(),
						edt_StartTime.getText().toString(), edt_EndTime
								.getText().toString(), edt_Repeat
								.getItemAtPosition(posRepeat).toString(),
						edt_Remind.getItemAtPosition(posRemind).toString(),
						type);
			} else {
				helper.update(
						eventId,
						edt_EventName.getText().toString(),
						// helper.update(curClickDate,
						// edt_EventName.getText().toString(),
						edt_Description.getText().toString(), dmy,
						edt_EventType.getItemAtPosition(posType).toString(),
						edt_Location.getText().toString(), edt_StartDate
								.getText().toString(), edt_EndDate.getText()
								.toString(),
						edt_StartTime.getText().toString(), edt_EndTime
								.getText().toString(), edt_Repeat
								.getItemAtPosition(posRepeat).toString(),
						edt_Remind.getItemAtPosition(posRemind).toString(),
						type);
			}
			finish();
			Intent intent = new Intent(getApplicationContext(), ListEvent.class);
			// if (curClickDate != null)
			// intent.putExtra("Date", curClickDate);
			// else{
			// ListEvent.setSelectAll(true);
			// }
			startActivity(intent);
		}
	};

	private View.OnClickListener onCancel = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == edt_StartDate) {
			datePicker = 1;
			showDialog(DATE_DIALOG_ID);
		}

		if (v == edt_EndDate) {
			datePicker = 2;
			showDialog(DATE_DIALOG_ID);
		}

		if (v == edt_StartTime) {
			timePicker = 1;
			showDialog(TIME_DIALOG_ID);
		}

		if (v == edt_EndTime) {
			timePicker = 2;
			showDialog(TIME_DIALOG_ID);
		}
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplayDate();
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplayTime();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear,
					mMonth - 1, mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		}
		return null;
	}
}
