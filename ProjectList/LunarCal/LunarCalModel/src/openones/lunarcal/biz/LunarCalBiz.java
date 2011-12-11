/**
 * Licensed to Open-Ones Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Open-Ones Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package openones.lunarcal.biz;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * This class provides main processing of calendar events.
 * @author Thach Le
 *
 */
public class LunarCalBiz {
    /** logger. */
    static final Logger LOG = Logger.getLogger("LunarCalBiz");

    /** given month of the calendar. */
    private int month;

    /** given year of the calendar. */
    private int year;

    /**
     * Create an instance of Lunar Calendar with given month/year.
     * @param month start by 0. Ex: Jan = 0; Feb = 1.
     * @param year year of the calendar
     */
    public LunarCalBiz(int month, int year) {
        this.month = month;
        this.year = year;
    }

    /**
     * Get the day of week with given members: month and year.
     * @return the day of week of the first day of month/year
     */
    public int getFirstDayOfWeek() {
        // Get "Day of Week" of the month

        // Step 1: Build a calendar instance of date 1/month/year
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
        int date = 1;
        cal.set(year, month, date); // set date 1/month/year

        // Step 2: Get the day of week of the month/year
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        return dayOfWeek;
    }
}
