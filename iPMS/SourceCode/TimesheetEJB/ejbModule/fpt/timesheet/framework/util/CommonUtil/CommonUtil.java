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
 
 /*******************************************************************************
 * File:        CommonUtil.java
 * Author:      Nguyen Thai Son - FPT Software
 * Description: common utilities
 *              This util will do following tasks:
 *              - convert an integer to a string
 - convert a string to an integer
 - convert a string to a boolean
 - convert a boolean to a string
 - cnverrt a boolean string to an integer
 - convert an integer to a boolean string
 - correct an error character
 - correct an HTML error
 - convert a string
 * Revisions:   2002.01.07 - Nguyen Thai Son - First written
 * Copyright:   Copyright (c) FPT Software. All rights reserved.
 /******************************************************************************/

package fpt.timesheet.framework.util.CommonUtil;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {

	/**
	 * @see java.lang.Object#Object()
	 */
	public CommonUtil() {
	}

	/**
	 * Method that splits a string with delimited fields
	 * into an array of field strings.
	 *
	 * @param str String with delimited fields.
	 * @param delim String that represents the delimiter.
	 *
	 * @return String[] holding all fields.
	 */
	public static String[] split(String str, String delim) {

	  StringTokenizer strtok = new StringTokenizer(str, delim);
	  String[] result = new String[strtok.countTokens()];

	  for (int i = 0; i < result.length; i++) {
		result[i] = strtok.nextToken();
	  }

	  return result;
	}

	/**
	 * Method that splits a string with delimited fields
	 * into an array of field strings.
	 *
	 * @param str String with delimited fields.
	 * @param delim char that represents the delimiter.
	 *
	 * @return String[] holding all fields.
	 */
	public static String[] split(String str, char delim) {
	  return split(str, String.valueOf(delim));
	}


	/**
	 * Method IntToStr.
	 * convert an integer to a string
	 * @param number
	 * @return String
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static String IntToStr(int number) {
		return Integer.toString(number);
	}

	/**
	 * Round and format a float number
	 * @param f
	 * @param nPrecision Number of digits after period
	 * @return Round and formated number with format x.xx
	 */
	public static String roundFormat(float f, int nPrecision) {
		float fRound;
		float fPrecision = 1;
		String strFormat = "0.";
		for (int i = 0; i < nPrecision; i++) {
			strFormat += "0";
			fPrecision *= 10;
		}
		fRound = Math.round(f * fPrecision) / fPrecision;
		DecimalFormat fmt = new DecimalFormat(strFormat);
		return fmt.format(fRound);
	}

	/**
	 * Method StrToInt.
	 * convert a string to an integer
	 * @param str
	 * @return int
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static int StrToInt(String str) {
		int nResult = 0;

		try {
			nResult = Integer.parseInt(str);
		}
		catch (NumberFormatException ex) {
			nResult = 0;
		}
		return nResult;
	}

	/**
	 * Method stringToBoolean.
	 * convert a valid string to a boolean<p>
	 *  - context is "yes", or "T": returns true<p>
	 *  - context is "no", or "F": returns false
	 * @param context
	 * @return boolean
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static boolean stringToBoolean(String context) {
		boolean booleanValue = false;
		if ((context != null) && (context.trim().length() > 0)) {
			if ((context.equalsIgnoreCase("yes")) || (context.equalsIgnoreCase("T"))) {
				booleanValue = true;
			}
			else if ((context.equalsIgnoreCase("no")) || (context.equalsIgnoreCase("F"))) {
				booleanValue = false;
			}
			else {
				booleanValue = false;
			}
		}
		return booleanValue;
	}

	/**
	 * Method booleanToString.
	 * convert a boolean to a string
	 * @param booleanValue
	 * @return String
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static String booleanToString(boolean booleanValue) {
		if (booleanValue)
			return new String("yes");
		return new String("no");
	}

	/**
	 * Method booleanStringToInt.
	 * convert convert a boolean string to an integer
	 * @param context
	 * @return int
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static int booleanStringToInt(String context) {
		int value = 0;
		if ((context != null) && (context.trim().length() > 0)) {
			if (context.equalsIgnoreCase("yes")) {
				value = 1;
			}
			else if (context.equalsIgnoreCase("no")) {
				value = 0;
			}
			else {
				value = 0;
			}
		}
		return value;
	}

	/**
	 * Method intToBooleanString.
	 * @param value
	 * @return String
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static String intToBooleanString(int value) {
		if (value == 1)
			return new String("yes");
		return new String("no");
	}

	/**
	 * Method charToBooleanString.
	 * @param context
	 * @return String
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static String charToBooleanString(String context) {
		String value = "no";
		if ((context != null) && (context.trim().length() > 0)) {
			if (context.equalsIgnoreCase("Y")) {
				value = "yes";
			}
		}
		return value;
	}

	/**
	 * Method booleanStringToChar.
	 * @param context
	 * @return String
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static String booleanStringToChar(String context) {
		String value = "N";
		if ((context != null) && (context.trim().length() > 0)) {
			if (context.equalsIgnoreCase("YES")) {
				value = "Y";
			}
			else if (context.equalsIgnoreCase("CHECKED")) {
				value = "Y";
			}
		}
		return value;
	}

	/**
	 * Method correctErrorChar.
	 * Correct an error sign
	 * @param strData data to correct
	 * @param ch special character
	 * @param strNew replaced string
	 * @return String String value is correct string
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static String correctErrorChar(String strData, char ch, String strNew) {
		if (strData == null) {
			return null;
		}
		String strResult = "";
		int iBegin = 0, iEnd;
		while ((iEnd = strData.indexOf(ch, iBegin)) > -1) {
			strResult += strData.substring(iBegin, iEnd) + strNew;
			iBegin = iEnd + 1;
		}
		iEnd = strData.length();
		strResult += strData.substring(iBegin, iEnd);
		return strResult;
	}

	/**
	 * Method correctHTMLError.
	 * Correct the HTML error
	 * @param strData data to correct
	 * @return String String value is correct string
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static String correctHTMLError(String strData) {
		char[] specChar = new char[]{'&', '"', '<', '>'};
		String[] strNew = new String[]{"&#38;", "&#34;", "&#60;", "&#62;"};
		for (int i = 0; i < 4; i++) {
			strData = correctErrorChar(strData, specChar[i], strNew[i]);
		}
		return strData;
	}

	/**
	 * Method stringConvert
	 * Convert normal string to correct Oracle SQL statement (correct ' character) 
	 * @param inString data to convert
	 * @return String
	 * @author Nguyen Thai Son
	 * @since 07 Jan 2002
	 */
	public static String stringConvert(String inString) {
		String strNew = "";
		if (inString != null) {
			String cSearch = "'";
			if (inString.indexOf(cSearch) > -1) {
				for (int nIndex = 0x00; nIndex < inString.length(); nIndex++) {
					String strFound = inString.substring(nIndex, nIndex + 1);
					if (strFound.equals(cSearch))
						strNew += strFound + strFound;
					else
						strNew += strFound;
				}
			}
			else {
				strNew = inString;
			}
		}
		return strNew;
	}

	/**
	 * Method arrayToString
	 * @param array
	 * @return
	 */
	public static String arrayToString(Object array) {
		if (array == null) {
		  return "[NULL]";
		} else {
		  Object obj = null;
		  if (array instanceof Hashtable) {
			array = ((Hashtable)array).entrySet().toArray();
		  } else if (array instanceof HashSet) {
			array = ((HashSet)array).toArray();
		  } else if (array instanceof Collection) {
		   array = ((Collection)array).toArray();
		  }
		  int length = Array.getLength(array);
		  int lastItem = length - 1;
		  StringBuffer sb = new StringBuffer("[");
		  for (int i = 0; i < length; i++) {
			obj = Array.get(array, i);
			if (obj != null) {
			  sb.append(obj);
			} else {
			  sb.append("[NULL]");
			}
			if (i < lastItem) {
			  sb.append(", ");
			}
		  }
		  sb.append("]");
		  return sb.toString();
		}
	  }        

	/**
	 * Method arrayToString
	 * Convert an array of strings to one string.
	 * Put the 'separator' string between each element.
	 * @param a
	 * @param separator
	 * @return
	 */
	public static String arrayToString(String[] a, String separator) {
		String result = "";
		if (a.length > 0) {
			result = a[0];    // start with the first element
			for (int i=1; i<a.length; i++) {
				result = result + separator + a[i];
			}
		}
		return result;
	}

	/**
	 * Method arrayToString2
	 * Convert an array of strings to one string.
	 * Put the 'separator' string between each element.
	 * @param a
	 * @param separator
	 * @return
	 */
	public static String arrayToString2(String[] a, String separator) {
		StringBuffer result = new StringBuffer();
		if (a.length > 0) {
			result.append(a[0]);
			for (int i=1; i<a.length; i++) {
				result.append(separator);
				result.append(a[i]);
			}
		}
		return result.toString();
	}

	/**
	 * Method StringtoArray
	 * @param s
	 * @param sep
	 * @return elements
	 */
	public static String[] StringtoArray( String s, String sep ) {
		// convert a String s to an Array, the elements
		// are delimited by sep
		StringBuffer buf = new StringBuffer(s);
		int arraysize = 1;
		for ( int i = 0; i < buf.length(); i++ ) {
		  if ( sep.indexOf(buf.charAt(i) ) != -1 )
			arraysize++;
		}
		String [] elements  = new String [arraysize];
		int y,z = 0;
		if ( buf.toString().indexOf(sep) != -1 ) {
		  while (  buf.length() > 0 ) {
			if ( buf.toString().indexOf(sep) != -1 ) {
			  y =  buf.toString().indexOf(sep);
			  if ( y != buf.toString().lastIndexOf(sep) ) {
				elements[z] = buf.toString().substring(0, y ); z++;
				buf.delete(0, y + 1);
			  }
			  else if ( buf.toString().lastIndexOf(sep) == y ) {
				elements[z] = buf.toString().substring(0, buf.toString().indexOf(sep));
				z++;
				buf.delete(0, buf.toString().indexOf(sep) + 1);
				elements[z] = buf.toString();z++;
				buf.delete(0, buf.length() );
			  }
			}
		  }
		}
		else {elements[0] = buf.toString(); }
		buf = null;
		return elements;
	}

	/**
	 * Method NCR2UnicodeString.
	 * Convert the NCR format (&#dddd;(decimal) OR &#xDDDD;(hexa decimal)) to Java string 
	 * @param str
	 * @return String
	 */
	public static String NCR2UnicodeString(String str) {
		String ostr = new String();
		int iTok = 0;
		int iChr = 0;
    
		while (iChr < str.length()) {
			iTok = str.indexOf("&#", iChr);
			if (iTok == -1) {
				ostr += str.substring(iChr, str.length());
				break;
			}
			ostr += str.substring(iChr, iTok);
			iChr = str.indexOf(";", iTok);
			if (iChr == -1) {
				ostr += str.substring(iTok, str.length());
				break;
			}
    
			String tok = str.substring(iTok + 2, iChr);
			try {
				int radix = 10;
				if (tok.trim().charAt(0) == 'x') {
					radix = 16;
					tok = tok.substring(1, tok.length());
				}
				// Only decode non-ASCII characters
				if (Integer.parseInt(tok, radix) > 255) {
					ostr += (char) Integer.parseInt(tok, radix);
				}
				else {
					ostr += str.substring(iTok, iChr + 1);
				}
			}
			catch (NumberFormatException exp) {
				// If this sub string is not parsed, then it may be a normal string
				// => do not covert it
				ostr += str.substring(iTok, iChr + 1);
			}
			iChr++;
		}
		return ostr;
	}

	/**
	 * Method decodeString.
	 * Convert the String from an encoding to destination encoding
	 * @param strOriginal  The original String
	 * @param strDesEncoding  Original encoding
	 * @param strDesEncoding  Destination encoding
	 * @return String
	 */
	public static String decodeString(String strOriginal,
									  String strOrgEncoding,
									  String strDesEncoding) throws Exception {
		String strResult = null;
		try {
			// Get string in original encoding and convert it
			// to destination encoding
			if (strOriginal != null) {
				strResult = new String(strOriginal.getBytes(strOrgEncoding), strDesEncoding);
				// Correct strings formed as (&#dddd;)
				strResult = NCR2UnicodeString(strResult);
			}
		}
		catch (Exception e) {
			// return original string if error occurs
			return strOriginal;
		}
		return strResult;
	}

	/**
	 * Method decodeParameter.
	 * Get request parameter and convert it to destination encoding
	 * @param request
	 * @param strParameter Parameter name
	 * @param strEncoding  Destination encoding
	 * @return String
	 */
	public static String decodeParameter(HttpServletRequest request,
										 String strParameter,
										 String strTargetEncoding) throws Exception {

		return decodeString(request.getParameter(strParameter),
							request.getCharacterEncoding(),
							strTargetEncoding);
	}

	/**
	 * Method decodeParameterValues.
	 * Get array of request parameter values and convert them
	 * to destination encoding
	 * @param request
	 * @param strParameter Parameter name
	 * @param strEncoding  Destination encoding
	 * @return String
	 */
	public static String[] decodeParameterValues(HttpServletRequest request,
												 String strParameter,
												 String strTargetEncoding) throws Exception {

		String[] arrValue = request.getParameterValues(strParameter);
		for (int i = 0; i < arrValue.length; i++) {
			arrValue[i] = decodeString(arrValue[i],
									   request.getCharacterEncoding(),
									   strTargetEncoding);
		}
		return arrValue;
	}
}