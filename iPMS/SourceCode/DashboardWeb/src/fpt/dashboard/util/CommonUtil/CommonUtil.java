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

package fpt.dashboard.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class CommonUtil
{

	public CommonUtil()
	{
	}


  	/**************************************************************************
	Function: public static String IntToStr(int number)
	Description: convert an integer to a string
	Parameters:
		- number[in]: an integer
	Return: String
	Author: Nguyen Thai Son
	Created date: 07 Jan 2002
	**************************************************************************/
	public static String IntToStr(int number)
	{
		return Integer.toString(number);
	}


	/**************************************************************************
	Function: public static int StrToInt(String str)
	Description: convert a string to an integer
	Parameters:
		- str[in]: a string
	Return: int
	Author: Nguyen Thai Son
	Created date: 07 Jan 2002
	**************************************************************************/

	public static int StrToInt(String str)
	{
		Integer in = new Integer(str);
		return in.intValue();
	}


	/**************************************************************************
	Function: public static boolean stringToBoolean(String context)
	Description: convert a valid string to a boolean
		- context is "yes", or "T": returns true
		- context is "no", or "F": returns false
	Parameters:
		- context[in]: a string
	Return: boolean
	Author: Nguyen Thai Son
	Created date: 07 Jan 2002
	**************************************************************************/

	public static boolean stringToBoolean(String context)
	{
		boolean booleanValue = false;
		if((context!=null) && (context.trim().length() > 0))
		{
			if((context.equalsIgnoreCase("yes")) || (context.equalsIgnoreCase("T")))
			{
				booleanValue = true;
			}
			else if((context.equalsIgnoreCase("no")) || (context.equalsIgnoreCase("F")))
			{
				booleanValue = false;
			}
			else
			{
				System.out.println("Unable to convert: defaulting to false");
				booleanValue = false;
			}
		}
		return booleanValue;
	}


	/**************************************************************************
	Function: public static String booleanToString(boolean booleanValue)
	Description: convert a boolean to a string
	Parameters:
		- context[in]: a boolean
	Return: String
	Author: Nguyen Thai Son
	Created date: 07 Jan 2002
	**************************************************************************/

	public static String booleanToString(boolean booleanValue)
	{
		if(booleanValue) return new String("yes");
		return new String("no");
	}

  	/**************************************************************************
	Function: public static int booleanStringToInt(String context)
	Description: convert a boolean string to an integer
	Parameters:
		- context[in]: a boolean string
	Return: int
	Author: Nguyen Thai Son
	Created date: 07 Jan 2002
	**************************************************************************/

  	public static int booleanStringToInt(String context)
  	{
    	int value = 0;
    	if ((context != null) && (context.trim().length() > 0))
    	{
      		if (context.equalsIgnoreCase("yes"))
      		{
        		value=1;
      		}
      		else if (context.equalsIgnoreCase("no"))
      		{
        		value=0;
      		}
      		else
      		{
        		value=0;
      		}
    	}
    	return value;
  	}

  	public static String intToBooleanString(int value)
  	{
    	if (value==1) return new String("yes");
    	return new String("no");
	}

  	public static String charToBooleanString(String context)
  	{
    	String value = "no";
    	if ((context != null) && (context.trim().length() > 0))
    	{
      		if (context.equalsIgnoreCase("Y"))
      		{
        		value="yes";
      		}
    	}
    	return value;
  	}


  	public static String booleanStringToChar(String context)
  	{
    	String value = "N";
    	if ((context != null) && (context.trim().length() > 0))
    	{
      		if (context.equalsIgnoreCase("YES"))
      		{
        		value="Y";
      		}
      		else if (context.equalsIgnoreCase("CHECKED"))
      		{
        		value="Y";
      		}
    	}
    	return value;
  	}


    /**************************************************************************
	Function: public static String correctErrorChar(String strData,char ch,String strNew)
	Description: Correct an error sign
	Parameters:
		- strData[in]: data to correct
		- ch[in]: special character
		- strNew: replaced string
	Return: String value is correct string
	Author: Nguyen Thai Son
	Created date: 07 Jan 2002
	**************************************************************************/

    public static String correctErrorChar(String strData,char ch,String strNew)
    {
        if (strData == null)
        {
            return null;
        }
        String strResult = "";
        int iBegin = 0, iEnd;
        while ((iEnd = strData.indexOf(ch,iBegin))>-1)
        {
            strResult += strData.substring(iBegin,iEnd)+strNew;
            iBegin = iEnd+1;
        }
        iEnd = strData.length();
        strResult += strData.substring(iBegin,iEnd);
        return strResult;
    }


  	/**************************************************************************
	Function: public static String correctHTMLError(String strData)
	Description: Correct the HTML error
	Parameters:
		- strData[in]: data to correct
	Return: String value is correct string
	Author: Nguyen Thai Son
	Created date: 07 Jan 2002
	**************************************************************************/

  	public static String correctHTMLError(String strData)
    {
        char[] specChar = new char[] {'&','"','<','>'};
        String[] strNew = new String[] {"&#38;","&#34;","&#60;","&#62;"};
        for (int i=0;i<4;i++)
        {
            strData = correctErrorChar(strData,specChar[i],strNew[i]);
        }
        return strData;
    }


   /**************************************************************************
	public static String stringConvert(String inString)
	Description:
	Parameters:
		- inString[in]: data to convert
	Return: String
	Author:
	Created date: 2001
	**************************************************************************/

   	public static String stringConvert(String inString)
  	{
    	String strNew = "";
    	if (inString!=null)
    	{
			String strSearch = null;
			String cSearch = null;

			cSearch = "'";
			strNew = "";
			strSearch = inString;
			if (strSearch.indexOf(cSearch)>-1)
			{
				for (int nIndex=0x00; nIndex<strSearch.length(); nIndex++)
				{
					String strFound = strSearch.substring(nIndex, nIndex+1);
					if (strFound.equals(cSearch)) strNew += strFound + strFound;
					else strNew += strFound;
				}
			}
			else
			{
				strNew = strSearch;
			}
		}

    	return strNew;
  	}
    
    /**
     * Get parameter from HttpServletRequest or recover session attribute
     * <br>- If request parameter is not null then return request paramerter
     * and store the parameter into session variable
     * <br>- Else if session attribute is not null then recover session attribute, return session attribute
     * @param request
     * @param strRequestParam
     * @param strSessionAttribute
     * @return
     */
    public static String getParameter(HttpServletRequest request,
            String strRequestParam, String strSessionAttribute) {
        String strRet = null;
        if ((request != null) && (strRequestParam != null) && (strSessionAttribute != null)) {
            HttpSession session = request.getSession();
            if (request.getParameter(strRequestParam) != null) {
                strRet = request.getParameter(strRequestParam);
                session.setAttribute(strSessionAttribute, strRet);
            }
            else if (session.getAttribute(strSessionAttribute) != null) {
                strRet = (String) session.getAttribute(strSessionAttribute);
            }
        }
        return strRet;
    }
    /*
     * Method NCR2UnicodeString.
     * Convert the NCR format (&#dddd;(decimal) OR &#xDDDD;(hexa decimal)) to Java string 
     * @param str
     * @return String
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
    public static String decodeString(String strOriginal,
                                      String strOrgEncoding,
                                      String strDesEncoding)
                                       throws Exception
    {
        String strResult = null;
        try {
            // Get string in original encoding and convert it
            // to destination encoding
            if (strOriginal != null) {
                strResult = new String(
                        strOriginal.getBytes(strOrgEncoding), strDesEncoding);
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
    public static String decodeParameter(HttpServletRequest request,
                                         String strParameter,
                                         String strTargetEncoding)
                                         throws Exception
    {
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
    public static String[] decodeParameterValues(HttpServletRequest request,
                                                 String strParameter,
                                                 String strTargetEncoding)
                                                 throws Exception
    {
        String[] arrValue = request.getParameterValues(strParameter);
        for (int i = 0; i < arrValue.length; i++) {
            arrValue[i] = decodeString(arrValue[i],
                                       request.getCharacterEncoding(),
                                       strTargetEncoding);
        }
        return arrValue;
    }
     */
}