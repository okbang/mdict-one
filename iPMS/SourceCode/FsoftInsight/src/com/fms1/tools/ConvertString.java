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

import java.util.StringTokenizer;

/**
 * String operations
 *
 */
public final class ConvertString  {
	public static final int adText = 1;
	public static final int adDate = 2;
	public static final int adNumber = 3;
	public static final int adSearch_ = 4;
	public static final int ad_Search_ = 5;
	public static final int adCheck = 6;
	/**
	 * author:NgaHT
	 * @param:str:String as an input 
	 * pattern:String used to replace 
	 * replace:String needed to replace
	 */
	public static String replace(final String str, final String pattern, String replace) {
		if (replace == null) {
			replace = "";
		}
		int s = 0, e = 0;
		final StringBuffer result = new StringBuffer(str.length() * 2);
		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}
	public static boolean isNumber(String param) {
		boolean result;
		if (param == null || param.equals(""))
			return false;
		param = param.replace('d', '_').replace('f', '_');
		try {
			new Double(param);
			result = true;
		}
		catch (Exception nfe) {
			result = false;
		}
		return result;
	}
	public static String toSql(final String value, final int type) {
		if (value == null)
			return null;
		String param = value;
		if ("".equals(param) && (type == adText || type == adDate)) {
			return null;
		}
		switch (type) {
			case adText :
				{
					param = replace(param, "'", "''");
					param = replace(param, "&amp;", "&");
					break;
				}
			case adSearch_ :
			case ad_Search_ :
				{
					param = replace(param, "'", "''");
					break;
				}
			case adNumber :
				{
					try {
						if (!isNumber(value) || "".equals(param))
							param = null;
						else
							param = value;
					}
					catch (NumberFormatException nfe) {
						param = null;
					}
					break;
				}
			case adDate :
				break;
		}
		return param;
	}
	/**
	 * Correct data being displayed in Excel file 
	 * @param value
	 * @return
	 */
	public static String toHtmlForExcel(String value) {
			if (value == null)
				return "";
			value = replace(value, "&", "&amp;");
			value = replace(value, "<", "&lt;");
			value = replace(value, ">", "&gt;");
			value = replace(value, "\"", "&quot;");
			value = replace(value, "\\", "&#92;");
			value = replace(value, "\'", "&#039;");
			value = replace(value, "\n\r", "\n");
			value = replace(value, "\r\n", "\n");
			value = replace(value, "\n", "<br style='mso-data-placement:same-cell;'/>");        
			return value;
		}
	/**
     * Correct data being displayed in HTML page (convert HTML tags)
     * @param value
     * @return
     */
    public static String toHtml(String value) {
		if (value == null)
			return "";
		value = replace(value, "&", "&amp;");
		value = replace(value, "<", "&lt;");
		value = replace(value, ">", "&gt;");
		value = replace(value, "\"", "&quot;");
        value = replace(value, "\\", "&#92;");
		value = replace(value, "\'", "&#039;");
		value = replace(value, "\n\r", "<BR>");
        value = replace(value, "\r\n", "<BR>");
        value = replace(value, "\n", "<BR>");        
		return value;
	}
	/**
     * Correct data being used as javascript String variable in HTML page
     * @param value
     * @return
     */
    public static String toJScript(String value) {
		if (value == null)
			return "";
		value = replace(value, "\"", "\\" + "\"");
        value = replace(value, "\'", "`");
		value = replace(value, "\n\r", "\\n");
		value = replace(value, "\r\n", "\\n");
		value = replace(value, "\r", "\n");
		value = replace(value, "\"", "'");
        value = replace(value, "\n", "\\n");
        value = replace(value, "\\", "\\\\");
		return value;
	}
	/**
	 * returns a string of type "x1 separator x2 separator  x3"
	 * usefull for building "in(..)" constraints in SQL functions
	 * 
	 */
	public static String arrayToString(int[] longArray, String separator) {
		StringBuffer constraintBuf = new StringBuffer("");
		if (longArray.length > 0) {
			int i;
			for (i = 0; i < longArray.length - 1; i++)
				constraintBuf.append(longArray[i]).append(separator);
			constraintBuf.append(longArray[i]);
		}
		return constraintBuf.toString();
	}
	public static String arrayToString(long[] longArray, String separator) {
		StringBuffer constraintBuf = new StringBuffer("");
		if (longArray.length > 0) {
			int i;
			for (i = 0; i < longArray.length - 1; i++)
				constraintBuf.append(longArray[i]).append(separator);
			constraintBuf.append(longArray[i]);
		}
		return constraintBuf.toString();
	}
	public static String arrayToString(double[] longArray, String separator) {
		StringBuffer constraintBuf = new StringBuffer("");
		if (longArray.length > 0) {
			int i;
			for (i = 0; i < longArray.length - 1; i++)
				constraintBuf.append(longArray[i]).append(separator);
			constraintBuf.append(longArray[i]);
		}
		return constraintBuf.toString();
	}
	public static String arrayToString(String[] stringArray, String separator) {
		StringBuffer constraintBuf = new StringBuffer("");
		if (stringArray.length > 0) {
			int i;
			for (i = 0; i < stringArray.length - 1; i++)
				constraintBuf.append(stringArray[i]).append(separator);
			constraintBuf.append(stringArray[i]);
		}
		return constraintBuf.toString();
	}
	public static int [] arrayToArray(String[] stringArray) {
		int  [] retVal=null;
		if (stringArray!=null){
			retVal=new int [stringArray.length];
			for (int i =0;i<stringArray.length;i++){
				retVal[i]=Integer.parseInt(stringArray[i]);					
			}
		}
		return retVal;
	}
	/**
	 * removes parameter from query string
	 * 
	 */
	public static String removeFromQueryString(String queryString, String removeMe) {
		if (queryString==null)
			return null;
		StringBuffer retVal=new StringBuffer();
		StringTokenizer tokenizer = new StringTokenizer(queryString,"&");
		String token;
		while (tokenizer.hasMoreElements()){
			token=tokenizer.nextToken();
			if (token.indexOf(removeMe)<0)
				retVal.append("&"+token);
		}
		return retVal.toString();
	}
	public static String trunc(String string,int maxSize) {
		if (string!=null && string.length() > maxSize)
			string = string.substring(0, maxSize-4) + "...";
		return string;
	}
	public static String firstWord(String string) {
		return string.substring(0,string.indexOf(" "));
	}
	/**
	 * Remove a character from a String which has position is at iIndex
	 * @param strValue
	 * @param iIndex
	 * @return removed String
	 */
	public static String removeChar(String strValue, int iIndex){
		StringBuffer strBuff = new StringBuffer(strValue.length()-1);
		strBuff.append(strValue.substring(0, iIndex)).append(strValue.substring(iIndex + 1));
		return strBuff.toString();
	}

	// Add by LanDD
	public static String breakString(String str, int wrapLength) {
		return wrap(str, wrapLength, null, true);
	}    

	public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
		if (str == null) {
			return null;
		}
		if (newLineStr == null) {
			newLineStr = "\n";
		}
		if (wrapLength < 1) {
			wrapLength = 1;
		}
		int inputLineLength = str.length();
		int offset = 0;
		StringBuffer wrappedLine = new StringBuffer(inputLineLength + 32);
    
		while ((inputLineLength - offset) > wrapLength) {
			if (str.charAt(offset) == ' ') {
				offset++;
				continue;
			}
			int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

			if (spaceToWrapAt >= offset) {
				// normal case
				wrappedLine.append(str.substring(offset, spaceToWrapAt));
				wrappedLine.append(newLineStr);
				offset = spaceToWrapAt + 1;
            
			} else {
				// really long word or URL
				if (wrapLongWords) {
					// wrap really long word one line at a time
					wrappedLine.append(str.substring(offset, wrapLength + offset));
					wrappedLine.append(newLineStr);
					offset += wrapLength;
				} else {
					// do not wrap really long word, just extend beyond limit
					spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
					if (spaceToWrapAt >= 0) {
						wrappedLine.append(str.substring(offset, spaceToWrapAt));
						wrappedLine.append(newLineStr);
						offset = spaceToWrapAt + 1;
					} else {
						wrappedLine.append(str.substring(offset));
						offset = inputLineLength;
					}
				}
			}
		}

		// Whatever is left in line is short enough to just pass through
		wrappedLine.append(str.substring(offset));

		return wrappedLine.toString();
	} 
	

	/**
	 * remove all space in the begining, end of String and
	 * replace many consecutive spaces between words with a space
	 * @param strValue
	 * @return standardized String.
	 */
	public static String toStandardizeString(String strValue){
		if (strValue == null || "".equals(strValue)){
			return "";
		}
		String strResult = strValue;
		while (" ".equals(strResult.substring(0,1))){
			strResult = removeChar(strResult, 0);
		}
		for (int i = 0; i < strResult.length()-1; i++){
			if (" ".equals(strResult.substring(i, i+1)) && " ".equals(strResult.substring(i+1,i+2))){
				strResult = removeChar(strResult, i);
				i--;
			}
		}
		return strResult;
	}
	public static String toStandardName(String strName){
		if (strName == null)
			return null;
		String result = strName;
		if ("".equals(result)) {
			return null;
		}
		result = replace(result, "'", "");
		result = replace(result, "\"", "");
		return result;
	}
}