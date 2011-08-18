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

package fpt.ncms.util.CommonUtil;

public final class CommonUtil {

    public CommonUtil() {
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
    public static String IntToStr(int number) {
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

    public static int StrToInt(String str) {
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

    public static String booleanToString(boolean booleanValue) {
        if (booleanValue)
            return new String("yes");
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

    public static String intToBooleanString(int value) {
        if (value == 1)
            return new String("yes");
        return new String("no");
    }

    public static String charToBooleanString(String context) {
        String value = "no";
        if ((context != null) && (context.trim().length() > 0)) {
            if (context.equalsIgnoreCase("Y")) {
                value = "yes";
            }
        }
        return value;
    }

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

    /**************************************************************************
     Function: public static String correctHTMLError(String strData)
     Description: Correct the HTML error
     Parameters:
     - strData[in]: data to correct
     Return: String value is correct string
     Author: Nguyen Thai Son
     Created date: 07 Jan 2002
     **************************************************************************/

    public static String correctHTMLError(String strData) {
        char[] specChar = new char[]{'&', '"', '<', '>'};
        String[] strNew = new String[]{"&#38;", "&#34;", "&#60;", "&#62;"};
        for (int i = 0; i < 4; i++) {
            strData = correctErrorChar(strData, specChar[i], strNew[i]);
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

    public static String stringConvert(String inString) {
        String strNew = "";
        if (inString != null) {
            String strSearch = null;
            String cSearch = null;

            cSearch = "'";
            strNew = "";
            strSearch = inString;
            if (strSearch.indexOf(cSearch) > -1) {
                for (int nIndex = 0x00; nIndex < strSearch.length(); nIndex++) {
                    String strFound = strSearch.substring(nIndex, nIndex + 1);
                    if (strFound.equals(cSearch))
                        strNew += strFound + strFound;
                    else
                        strNew += strFound;
                }
            }
            else {
                strNew = strSearch;
            }
        }

        return strNew;
    }
}