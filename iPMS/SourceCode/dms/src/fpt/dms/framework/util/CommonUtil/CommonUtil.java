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

package fpt.dms.framework.util.CommonUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class CommonUtil {
    private static org.apache.log4j.Logger logger =
                org.apache.log4j.Logger.getLogger(CommonUtil.class);
  	
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
				//System.out.println("Unable to convert: defaulting to false");
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

    public static String correctHTMLError(String strData){
		
	    char[] specChar = new char[] {'&','"','<','>'};
        String[] strNew = new String[] {"&#38;","&#34;","&#60;","&#62;"};    
        for (int i = 0; i < 4; i++) {
            strData = correctErrorChar(strData,specChar[i],strNew[i]);
        }
        return strData;
 	}

//don't need full this function, I will check as soon as possible	
    /**
     * Insert HTML tag between begining, end of sub-text that breaked by
     * pivot string in original text e.g
     * insertHtmlTag("line1 \n line2", "\n", "(div)", "(/div)")
     * will return "(div)line1 (/div) line2"
     * @param strOrg
     * @param strPivot
     * @param strOpen
     * @param strClose
     * @return
     */
	  public static String insertHtmlTag(String strOrg,
										  String strPivot,
										  String strOpen,
										  String strClose) {
		  StringBuffer sbResult = new StringBuffer();
		  int nPvLen = strPivot.length();
		  // String values cannot be NULL
		  if ((strOrg == null) || (strPivot == null) || (strOpen == null) ||
			  (strClose == null))
		  {
			  return strOrg;
		  }
        
		  int iBegin = 0, iEnd;
		  while ((iEnd = strOrg.indexOf(strPivot,iBegin)) >- 1) {
			  sbResult.append(strOpen);
			  sbResult.append(strOrg.substring(iBegin,iEnd));
			  sbResult.append(strClose);
			  iBegin = iEnd + nPvLen;
		  }
		  iEnd = strOrg.length();
		  sbResult.append(strOrg.substring(iBegin,iEnd));
        
		  return sbResult.toString();
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
     * Delete file from real path
     * @param strFileName
     * @return
     */
    public static boolean deleteFile(String strFileName) {
        boolean bResult = false;
        if (strFileName != null) {
            try {
                logger.debug("removing file:" + strFileName);
                java.io.File attFile = new java.io.File(strFileName);
                if (attFile.delete()) {
                    bResult = true;
                }
                else {
                    logger.error("Cannot remove file:" + strFileName);
                }
            }
            catch (Exception e) {
                logger.error("Cannot remove file :"  + strFileName + ", " +
                                   e.getMessage());
            }
        }
        return bResult;
    }
    
    /*
     * Read file into byte array
     * @author trungtn (Jan-05)
     * @param strFilePath
     * @return
     * @throws java.io.IOException
    public static byte[] getFileData(String strFilePath) throws IOException {
        byte[] arrData = null;
        try {
            java.io.File file = new java.io.File(strFilePath);
            InputStream is = new FileInputStream(file);
            // Get the size of the file
            long length = file.length();
            // File is too large
            if (length > Integer.MAX_VALUE) {
                throw new IOException("File size exceeded.");
            }
            // Create the byte array to hold the data
            arrData = new byte[(int)length];

            // Begin read file
            int offset = 0;
            int numRead = 0;
            while ((offset < arrData.length) &&
                (numRead=is.read(arrData, offset, arrData.length-offset)) >= 0)
            {
                offset += numRead;
            }
            // Ensure all the bytes have been read in
            if (offset < arrData.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            // Close the input stream and return bytes
            is.close();
        }
        catch (IOException e) {
            logger.error(e);
            throw e;
        }
        return arrData;
    }
     */

    /**
     * Read file into byte array with limitted size nMaxSize
     * @author trungtn (Jan-05)
     * @param strFilePath
     * @param nMaxSize
     * @return
     * @throws IOException
     */
    public static byte[] getFileData(String strFilePath, int nMaxSize)
                                    throws IOException {
        byte[] arrData = null;
        try {
            java.io.File file = new java.io.File(strFilePath);
            InputStream is = new FileInputStream(file);
            // Get the size of the file
            long length = file.length();
            // File is too large
            if (length > nMaxSize) {
                throw new IOException("File size exceeded.");
            }
            // Create the byte array to hold the data
            arrData = new byte[(int)length];

            // Begin read file
            int offset = 0;
            int numRead = 0;
            while ((offset < arrData.length) &&
                (numRead=is.read(arrData, offset, arrData.length-offset)) >= 0)
            {
                offset += numRead;
            }
            // Ensure all the bytes have been read in
            if (offset < arrData.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            // Close the input stream and return bytes
            is.close();
        }
        catch (IOException e) {
            logger.error(e);
            throw e;
        }
        return arrData;
    }

    /**
     * Round and format a float number
     * @param f
     * @param nPrecision Number of digits after period
     * @return Round and format number with format x.xx
     */
    public static String roundFormat(float f, int nPrecision) {
        float fRound;
        float fPrecision = 1;
        String strFormat = "0.";
        for (int i = 0; i < nPrecision; i++) {
            strFormat += "#";
            fPrecision *= 10;
        }
        fRound = Math.round(f * fPrecision) / fPrecision;
        DecimalFormat fmt = new DecimalFormat(strFormat);
        return fmt.format(fRound);
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
    /*
     * Method getUnicodeHexString.
     * print the Unicode values of Java string in \\uXXXX format (for DEBUG) 
     * @param str Unicode string
     * @param escapeAscii Avoid display ASCII characters
     * @return String
    public static String getUnicodeHexString(String str, boolean escapeAscii) {
        String ostr = new String();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!escapeAscii && ((ch >= 0x0020) && (ch <= 0x007e))) {
                ostr += ch;
            }
            else {
                ostr += "\\u";
                String hex = Integer.toHexString(str.charAt(i) & 0xFFFF);
                if (hex.length() == 2)
                    ostr += "00";
                ostr += hex.toUpperCase(Locale.ENGLISH);
            }
        }
        return (ostr);
    }

    /**
     * Method getUTFString.
     * print the Unicode values of Java string in &#xxxx; format (for UTF-8 page) 
     * @param str Unicode string
     * @return String
    public static String getUTFString(String str) {
        String ostr = new String();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if ((ch >= 0x0000) && (ch <= 0x00ff)) {
                ostr += ch;
            }
            else {
                ostr += "&#" + Integer.toString(ch) + ";";
            }
        }
        return (ostr);
    }
    */
}