/*
 * Copyright (c) 2009, FPT Software JSC
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *	- Neither the name of the FPT Software JSC nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 package com.fms1.tools;

import java.sql.Date;
import java.util.Calendar;
//Note calendar counts monthes from 0 to 11 but we use from 1 to 12
/**
 * Usefull tool to perform date operations
 * @author phuongnt
 *
 */
public class ReportMonth {
	private int month;
	private int year;
	
	public ReportMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		month = cal.get(Calendar.MONTH)+1;
		year = cal.get(Calendar.YEAR);
	}
	
	public ReportMonth(int month, int year) {
		this.month = month;
		this.year = year;
	}
	
	public ReportMonth(ReportMonth rm) {
		this.month = rm.getMonth();
		this.year = rm.getYear();
	}
	public ReportMonth() {
		Calendar cal =Calendar.getInstance();
		month = cal.get(Calendar.MONTH)+1;
		year = cal.get(Calendar.YEAR);
	}
	
	public void moveToNextMonth() {
		if (month == 12) {
			month = 1;
			year++;
		}
		else {
			month++;
		}
	}
	
	public void moveToPreviousMonth() {
		if (month == 1) {
			month = 12;
			year--;
		}
		else {
			month--;
		}
	}
	public void moveToPreviousMonth(int nmonth) {
		for (int i=0;i<nmonth;i++)
			moveToPreviousMonth();
	}
	
	public ReportMonth getNextMonth() {
		ReportMonth tmp = new ReportMonth(this);
		tmp.moveToNextMonth();
		return tmp;
	}
	
	public ReportMonth getPreviousMonth() {
		ReportMonth tmp = new ReportMonth(this);
		tmp.moveToPreviousMonth();
		return tmp;
	}
	public int getMonth() {
		return month;
	}
	public String getMonthLabel() {
		return CommonTools.getMonth(month);
	}
	public int getYear() {
		return year;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public void setYear(int year) {
		this.year = year;
	}

	public Date getFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH,month-1);
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(cal.getTime().getTime());
	}

	public Date getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH,month-1);
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return new java.sql.Date(cal.getTime().getTime());
	}




}
