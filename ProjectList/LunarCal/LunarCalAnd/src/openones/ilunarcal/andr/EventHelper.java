/**
 *
 */
package openones.ilunarcal.andr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import oog.ical.core.Day;
import oog.ical.lunar.SolarToLunar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The Class EventHelper.
 * 
 * @author mastertan
 */
public class EventHelper extends SQLiteOpenHelper {
	/** The Constant DATABASE_NAME. */
	private static final String DATABASE_NAME = "lunarcal.db";

	/** The Constant SCHEMA_VERSION. */
	private static final int SCHEMA_VERSION = 1;

	/**
	 * Instantiates a new event helper.
	 * 
	 * @param conext
	 *            the conext
	 */
	public EventHelper(Context conext) {
		super(conext, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	/**
	 * [Explain the description for this method here].
	 * 
	 * @param db
	 *            the db
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE event (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "eventName TEXT, description TEXT, "
				+ "dayCreate TEXT, type TEXT, location TEXT, "
				+ "startDay TEXT, endDay TEXT, startTime TEXT, "
				+ "endTime TEXT, repeat TEXT, remind TEXT, typeCalendar TEXT);");
	}

	/**
	 * [Explain the description for this method here].
	 * 
	 * @param db
	 *            the db
	 * @param oldVersion
	 *            the old version
	 * @param newVersion
	 *            the new version
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      int, int)
	 */

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * Insert.
	 * 
	 * @param eventName
	 *            the event name
	 * @param description
	 *            the description
	 * @param dayCreate
	 *            the day create
	 * @param type
	 *            the type
	 * @param location
	 *            the location
	 * @param startDay
	 *            the start day
	 * @param endDay
	 *            the end day
	 * @param startTime
	 *            the start time
	 * @param endTime
	 *            the end time
	 * @param repeat
	 *            the repeat
	 * @param remind
	 *            the remind
	 */

	public void insert(String eventName, String description, String dayCreate,
			String type, String location, String startDay, String endDay,
			String startTime, String endTime, String repeat, String remind,
			String typeCalendar) {
		ContentValues cv = new ContentValues();

		cv.put("eventName", eventName);
		cv.put("description", description);
		cv.put("dayCreate", dayCreate);
		cv.put("type", type);
		cv.put("location", location);
		cv.put("startDay", startDay);
		cv.put("endDay", endDay);
		cv.put("startTime", startTime);
		cv.put("endTime", endTime);
		cv.put("repeat", repeat);
		cv.put("remind", remind);
		cv.put("typeCalendar", typeCalendar);

		getWritableDatabase().insert("event", "eventName", cv);
	}

	/**
	 * Update.
	 * 
	 * @param id
	 *            the id
	 * @param eventName
	 *            the event name
	 * @param description
	 *            the description
	 * @param dayCreate
	 *            the day create
	 * @param type
	 *            the type
	 * @param location
	 *            the location
	 * @param startDay
	 *            the start day
	 * @param endDay
	 *            the end day
	 * @param startTime
	 *            the start time
	 * @param endTime
	 *            the end time
	 * @param repeat
	 *            the repeat
	 * @param remind
	 *            the remind
	 * @param typeCalendar
	 *            the typeCalendar
	 */
	public void update(String id, String eventName, String description,
			String dayCreate, String type, String location, String startDay,
			String endDay, String startTime, String endTime, String repeat,
			String remind, String typeCalendar) {
		ContentValues cv = new ContentValues();
		String[] args = { id };

		cv.put("dayCreate", dayCreate);
		cv.put("eventName", eventName);
		cv.put("description", description);
		cv.put("type", type);
		cv.put("location", location);
		cv.put("startDay", startDay);
		cv.put("endDay", endDay);
		cv.put("startTime", startTime);
		cv.put("endTime", endTime);
		cv.put("repeat", repeat);
		cv.put("remind", remind);
		cv.put("typeCalendar", typeCalendar);

		getWritableDatabase().update("event", cv, "_ID=?", args);
	}

	/**
	 * Delete.
	 * 
	 * @param id
	 *            the id
	 */
	public void delete(String id) {
		String[] args = { id };

		getWritableDatabase().delete("event", "_ID=?", args);
	}

	/**
	 * Select all.
	 * 
	 * @return the cursor
	 */

	public Cursor selectAll() {
		return (getReadableDatabase().rawQuery("SELECT * FROM event", null));
	}

	/**
	 * Delete all.
	 */
	public void deleteAll() {
		getWritableDatabase().delete("event", null, null);
	}

	/**
	 * Gets the all.
	 * 
	 * @param dayCreate
	 *            the day create
	 * @return the all
	 */
	public Cursor getAll(String dayCreate) {

		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, eventName, description, type, location, startDay, endDay, "
								+ "startTime, endTime, repeat, remind, typeCalendar FROM event WHERE dayCreate ='"
								+ dayCreate + "' ORDER BY eventName", null));
	}

	/**
	 * Gets the event.
	 * 
	 * @param eventId
	 *            the event id
	 * @return the event
	 */
	public Cursor getEvent(String eventId) {
		String[] args = { eventId };
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, eventName, description, dayCreate, type, "
								+ "location, startDay, endDay, startTime, endTime, repeat, remind, typeCalendar FROM event WHERE _ID=?",
						args));
	}

	public ArrayList<String> getEventbyMonth(int month, int year) {
		Cursor cursor = selectAll();
		cursor.moveToFirst();
		GetCalendar listCal = new GetCalendar();
		ArrayList<String> lunarList = new ArrayList<String>();
		lunarList = listCal.getLunar(month, year);
		ArrayList<String> solarList = new ArrayList<String>();
		solarList = listCal.getSolar(month, year);
		ArrayList<String> listEvent = new ArrayList<String>();
		for (int i = 0; i < 42; i++) {
			String[] lunar = lunarList.get(i).split("-");
			String[] solar = lunarList.get(i).split("-");
			while (cursor.isLast()) {
				String date = getStartDay(cursor);
				String[] eventSolar = date.split("-");
				String[] eventLunar = SolarToLunar(date).split("-");
				String typeCalendar = getTypeCalendar(cursor);
				String type = getRepeat(cursor);
				// no repeat
				if (type.equals("0")) {

				}
				// repeat by week
				else if (type.equals("1")) {
					Calendar calendar = new GregorianCalendar();
					calendar.set(Integer.parseInt(eventSolar[0]),
							Integer.parseInt(eventSolar[1]),
							Integer.parseInt(eventSolar[2]));
					int dayOfWeekEvent = calendar.get(Calendar.DAY_OF_WEEK);
					calendar.set(Integer.parseInt(solar[0]),
							Integer.parseInt(solar[1]),
							Integer.parseInt(solar[2]));
					int dayOfWeekSolar = calendar.get(Calendar.DAY_OF_WEEK);
					if (dayOfWeekEvent == dayOfWeekSolar)
						listEvent.add("1");
					else
						listEvent.add("0");
				}
				// repeat by month
				else if (type.equals("2")) {
					if (typeCalendar.equals("0")) {
						if (lunar[0].equals(eventLunar[0])) {
							listEvent.add("1");
						} else
							listEvent.add("0");
					} else {
						if (solar[0].equals(eventSolar[0])) {
							listEvent.add("1");
						} else
							listEvent.add("0");
					}
				}
				// repeat by year
				else if (type.equals("3")) {
					if (typeCalendar.equals("0")) {
						if (lunar[0].equals(eventLunar[0])
								&& lunar[1].equals(eventLunar[1])) {
							listEvent.add("1");
						} else
							listEvent.add("0");
					} else {
						if (solar[0].equals(eventSolar[0])
								&& solar[1].equals(eventSolar[1])) {
							listEvent.add("1");
						} else
							listEvent.add("0");
					}
				} else {
					listEvent.add("0");
				}
			}
			cursor.moveToNext();
		}
		return listEvent;
	}

	private String SolarToLunar(String solar) {
		String[] date = solar.split("-");
		return String.valueOf(SolarToLunar.convertSolar2Lunar(
				Integer.parseInt(date[0]), Integer.parseInt(date[1]) + 1,
				Integer.parseInt(date[2]), 7));
	}

	private String LunarToSolar(String lunar) {
		String[] date = lunar.split("-");
		int leap = 0;
		if (Integer.parseInt(date[2]) % 4 == 0) {
			leap = 1;
		}
		return String.valueOf(SolarToLunar.convertLunar2Solar(
				Integer.parseInt(date[0]), Integer.parseInt(date[1]),
				Integer.parseInt(date[2]), leap, 7));
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
	 * Gets the ID of event.
	 * 
	 * @param c
	 *            the c
	 * @return the event id
	 */
	public int getEventId(Cursor c) {
		return (c.getInt(0));
	}

	/**
	 * Gets the name of event.
	 * 
	 * @param c
	 *            the c
	 * @return the event name
	 */
	public String getEventName(Cursor c) {
		return (c.getString(1));
	}

	/**
	 * Gets the description about event.
	 * 
	 * @param c
	 *            the c
	 * @return the description
	 */
	public String getDescription(Cursor c) {
		return (c.getString(2));
	}

	/**
	 * Gets the day create.
	 * 
	 * @param c
	 *            the c
	 * @return the day create
	 */
	public String getDayCreate(Cursor c) {
		return (c.getString(3));
	}

	/**
	 * Gets the type of event.
	 * 
	 * @param c
	 *            the c
	 * @return the type
	 */
	public String getType(Cursor c) {
		return (c.getString(4));
	}

	/**
	 * Gets the location where hold the event.
	 * 
	 * @param c
	 *            the c
	 * @return the location
	 */
	public String getLocation(Cursor c) {
		return (c.getString(5));
	}

	/**
	 * Gets the start day of event.
	 * 
	 * @param c
	 *            the c
	 * @return the start day
	 */
	public String getStartDay(Cursor c) {
		return (c.getString(6));
	}

	/**
	 * Gets the end day of event.
	 * 
	 * @param c
	 *            the c
	 * @return the end day
	 */
	public String getEndDay(Cursor c) {
		return (c.getString(7));
	}

	/**
	 * Gets the start time of event.
	 * 
	 * @param c
	 *            the c
	 * @return the start time
	 */
	public String getStartTime(Cursor c) {
		return (c.getString(8));
	}

	/**
	 * Gets the end time of event.
	 * 
	 * @param c
	 *            the c
	 * @return the end time
	 */
	public String getEndTime(Cursor c) {
		return (c.getString(9));
	}

	/**
	 * Gets the repeat type.
	 * 
	 * @param c
	 *            the c
	 * @return the repeat
	 */
	public String getRepeat(Cursor c) {
		return (c.getString(10));
	}

	/**
	 * Gets the remind type.
	 * 
	 * @param c
	 *            the c
	 * @return the remind
	 */
	public String getRemind(Cursor c) {
		return (c.getString(11));
	}

	/**
	 * Gets the type of Calendar.
	 * 
	 * @param c
	 *            the c
	 * @return the type calendar
	 */
	public String getTypeCalendar(Cursor c) {
		return (c.getString(12));
	}
}
