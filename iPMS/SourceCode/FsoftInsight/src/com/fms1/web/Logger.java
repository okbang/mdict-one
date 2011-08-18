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
 
 package com.fms1.web;
import java.io.*;
import java.util.*;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fms1.tools.CommonTools;
import com.fms1.infoclass.UserInfo;

/**
 * logging utility
 * @author manu
 * @date Feb 17, 2004
 */
public class Logger {
	//action and ids come from constant Class
	private static String[] actions;
	private static int[] actionIDs;
	private static int actionSize;
	private static String dir;
	private static int currentDay=-1;
	private static final Calendar calendar=new GregorianCalendar();	
	public static final void init() {
		try {
			if (actions == null) {
				dir=Parameters.logRoot;
				//first call:cache the actions and actionIDs
				Field[] fld = Constants.class.getFields();
				actionSize = fld.length;
				actions=new String [actionSize];
				actionIDs=new int [actionSize];
				Workaround ct= new Workaround();
				for (int i = 0; i < actionSize; i++) {
					actions[i]=fld[i].getName();
					actionIDs[i]=fld[i].getInt(ct);
				}
			}
		}
		catch (Exception e) {
			System.err.println("Constants object must include int values only");
			e.printStackTrace();
		}
	}
	public static final void logMe(String vars) {
		init();
		calendar.setTime(new Date());
		int day=calendar.get(Calendar.DAY_OF_WEEK);
		try{
			
			String fileName="Insightlog"+day+".csv";
			File file= new File(dir+fileName);
			RandomAccessFile logFile= new RandomAccessFile(file,"rw");
			//we use a pool of 7 log files, must clean prev week's file
			if (day!=currentDay)
				logFile.setLength(0);
			currentDay=day;
			//move to the end of file or 
			long size=logFile.length();
			//move to end of file
			if (size>0)
				logFile.seek(size-1);
			else
				logFile.seek(0);
			logFile.writeBytes(vars);
			logFile.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * csv format
	 */
	public static final void logRequest(HttpServletRequest request){
		Date now=new Date(); 
		HttpSession session=request.getSession();
		String strRq=request.getParameter("reqType");
		String url=request.getRequestURI();
		String strAccount = "";
		final int reqType = CommonTools.parseInt(strRq);
		UserInfo userInfo = (UserInfo)session.getAttribute("UserLoginInfo");
		if (userInfo != null){
			strAccount = userInfo.account;
		}
		logMe(request.getRemoteAddr()+ "," + strAccount + "," + getAction(reqType) + "," + CommonTools.dateFormat(now) + "," + url + ",\"" + CommonTools.timeFormat(now) + getParams(request) + "\"\n\r");
	}
	/**
	 * 
	 * bug if 2 char to be replaced are following each other
	 */
	private static String getParams(HttpServletRequest request) {
		StringBuffer buff=new StringBuffer();
		try{
			Enumeration e=request.getParameterNames();
			String separator="\",\"";
			String x=": ";
			String param,paramVal;
			while(e.hasMoreElements()){
				param=(String)e.nextElement();
				paramVal=CommonTools.stringReplace(request.getParameter(param),"\"","\"\"");
				buff.append(separator).append(param).append(x).append(paramVal);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			return buff.toString();
		}
		
		 
	}
	private static final String getAction(int reqType) {
		for (int i=0;i<actionSize;i++){
			if (actionIDs[i]==reqType)
				return actions[i];
		}
		return "undefined";
		
	}
	//as it says, we need to implement Constants in order to get the values of reqTypes
	private static class Workaround implements Constants{
	}
	
}
