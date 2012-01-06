/*
 *
 */
package openones.ilunarcal.andr;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import oog.ical.lunar.SolarToLunar;

/**
 * The Class GetCalendar. Convert Solar day to Lunar day and return as an arraylist
 */

public class GetCalendar {

    /** The current day of month. */
    private  int currentDayOfMonth;

    /** The day offset. */
    private static final int DAY_OFFSET = 1;

    /** The days in month. */
    private  int daysInMonth;

    /** The days of month. */

    private final  int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * Gets the number of days of month.
     *
     * @param i the i
     * @return the number of days of month
     */
    private  int getNumberOfDaysOfMonth(int i) {
        return daysOfMonth[i];
    }

    //Get current day of the month
    /**
     * Gets the current day of month.
     *
     * @return the current day of month
     */
    public  int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    //Create an array to store Solar Day
    /**
     * Gets the solar day.
     *
     * @param mm the mm
     * @param yyyy the yyyy
     * @return the solar day as an arraylist
     */
    public  ArrayList<String> getSolar(int mm, int yyyy) {
        ArrayList<String> listSolar = new ArrayList<String>();
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;
        int currentMonth = mm;
        // String currentMonthName = getMonthAsString(currentMonth);
        daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        // Only get day, month, year follow default time zone
        GregorianCalendar cal = new GregorianCalendar(yyyy, currentMonth, 1);

        // if current month is December
        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = yyyy;
            nextYear = yyyy + 1;
        }
        // if current month is January
        else if (currentMonth == 0) {
            prevMonth = 11;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
            prevYear = yyyy - 1;
            nextYear = yyyy;
        }
        // None of the above cases
        else {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = currentMonth + 1;
            prevYear = yyyy;
            nextYear = yyyy;
        }
        /**
         * Gets the current day of month.
         *
         * @return the current day of month
         */
        int currentWeekDay = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;

        if (currentWeekDay != 0) {
            trailingSpaces = currentWeekDay;
        } else {
            trailingSpaces = 7;
        }

        if (cal.isLeapYear(cal.get(java.util.Calendar.YEAR)) && mm == 1) {
            ++daysInMonth;
        }

        // Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {
            listSolar.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-p");
        }

        // Current Month days
        for (int i = 1; i <= daysInMonth; i++) {
            if (i == getCurrentDayOfMonth()) {
                listSolar.add(String.valueOf(i) + "-c");
            } else {
                listSolar.add(String.valueOf(i) + "-c");
            }
         }

        int size = listSolar.size();
        // Leading Month days
        for (int i = 0; i < 42 - size; i++) {
            listSolar.add(String.valueOf(i + 1) + "-n");
        }
        return listSolar;
    }
    
    //Get Solar Calendar and convert it into Lunar Calendar
    /**
     * Gets the lunar.
     *
     * @param mm the mm
     * @param yyyy the yyyy
     * @return the lunar
     */
    public  ArrayList<String> getLunar(int mm, int yyyy) {
        ArrayList<String> listLunar = new ArrayList<String>();
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;
        int currentMonth = mm;
        daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        // Only get day, month, year follow default time zone
        GregorianCalendar cal = new GregorianCalendar(yyyy, currentMonth, 1);

        // if current month is December
        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = yyyy;
            nextYear = yyyy + 1;
        } else if (currentMonth == 0) {   // if current month is January
            prevMonth = 11;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
            prevYear = yyyy - 1;
            nextYear = yyyy;
        } else {  // None of the above cases
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = currentMonth + 1;
            prevYear = yyyy;
            nextYear = yyyy;
        }

        // Compute how many days of the previous month before the first day of current month
        int currentWeekDay = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;

        //If current year is leap year then February will has 29 day
        if (cal.isLeapYear(cal.get(java.util.Calendar.YEAR)) && mm == 1) {
            ++daysInMonth;
        }

        // Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {
            int value = Integer.parseInt((String.valueOf((daysInPrevMonth
                    - trailingSpaces + DAY_OFFSET) + i)));
            listLunar.add(String.valueOf(SolarToLunar.convertSolar2Lunar(value, prevMonth + 1, prevYear, 7)[0]) + "-p");
        }

        // Current Month days
        for (int i = 1; i <= daysInMonth; i++) {
                int value = Integer.parseInt((String.valueOf(i)));
                listLunar.add(String.valueOf(SolarToLunar.convertSolar2Lunar(value, mm + 1, yyyy, 7)[0]) + "-c");
            }
        int size = listLunar.size();

        // Leading Month days
        for (int i = 0; i < 42 - size; i++) {
            int value = Integer.parseInt((String.valueOf(i+1)));
            listLunar.add(String.valueOf(SolarToLunar.convertSolar2Lunar(value, nextMonth + 1, nextYear, 7)[0]) + "-n");
        }
        return listLunar;
    }

    // Convert day by Library.
    /**
     * Convert2 solar.
     *
     * @param day the day
     * @param month the month
     * @param year the year
     * @return the string
     */
    public  String convert2Solar(String day, String month, String year) {
        int daySolar = Integer.parseInt(day);
        int lengthOfMonth = month.length();
        int monthSolar = Integer.parseInt((month.substring(lengthOfMonth - 2))
                .trim());
        int yearSolar = Integer.parseInt(year);
        String dayLunar;
        int[] lunar = SolarToLunar.convertSolar2Lunar(daySolar, monthSolar,
                yearSolar, 7);
        dayLunar = String.valueOf(lunar[0]);
        return dayLunar;
    }
}
