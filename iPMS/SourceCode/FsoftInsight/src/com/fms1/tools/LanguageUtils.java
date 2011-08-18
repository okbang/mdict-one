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
 
 /*
 * Created on 2005/10/11
 *
 */
package com.fms1.tools;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class used to load message from property file
 */
public class LanguageUtils {
	static String LANG_BASE_NAME = "res.language";
	static ResourceBundle resBundle_JAPANESE = null;
	static ResourceBundle resBundle_ENGLISH = null;
	static String[] specialString = { "&nbsp;","&nbsp", ":", "/", "#", "@",
			"&", "%", ",", "<", ">", "-", "\\(", "\\)", "\\*", "\\[", "\\]",
			"!", "\\r\\n", "(",")" };


	public static ResourceBundle getBundle(int type){
		switch (type){
			case LanguageChoose.ENGLISH : if (resBundle_ENGLISH == null) resBundle_ENGLISH = ResourceBundle.getBundle(LANG_BASE_NAME,Locale.ENGLISH);
						return resBundle_ENGLISH;
			case LanguageChoose.JAPANESE : if (resBundle_JAPANESE == null) resBundle_JAPANESE = ResourceBundle.getBundle(LANG_BASE_NAME,Locale.JAPANESE);
						return resBundle_JAPANESE;
			default : if (resBundle_ENGLISH == null) resBundle_ENGLISH = ResourceBundle.getBundle(LANG_BASE_NAME,Locale.ENGLISH);
						return resBundle_ENGLISH;
		}
	}

	public static String getMessage(int type, String aKey){
		String res = "";
		try {
			res = getBundle(type).getString(makeActualKey(aKey.trim()));
		}
		catch (MissingResourceException e) {
			res = aKey;
		}
		finally{
			if ("".equals(res) || res == null){
				res = aKey;
			}
			return res;
		}
	}
	
	public static String makeActualKey(String aKey){
		
		StringBuffer res = null;
		res = new StringBuffer(aKey.trim());
		int index =-1;
		int i=0;
		for (i=0; i< specialString.length; i++){
			index = res.toString().indexOf(specialString[i]);
			while (index >=0 ){
				res = res.replace(index,index + specialString[i].length(),"");
				index = res.toString().indexOf(specialString[i]);
			}
		}
		res = new StringBuffer(res.toString().trim());
		index = res.toString().indexOf("  ");
		while (index >=0){
			res = res.replace(index,index+2," ");
			index = res.toString().indexOf("  ");
		}

		index = res.toString().indexOf(" ");
		while (index >=0){
			res = res.replace(index,index+1,"__");
			index = res.toString().indexOf(" ");
		}
		
		return res.toString();
	}

	public static String paramText(int type,String[] args){
		StringBuffer result;
		String temp = null;
		if (args == null || args.length < 1){
			return null;
		}

		String []value = new String[args.length];
		temp = getMessage(type,args[0]);
		if ("".equals(temp)){
			value[0] = args[0];
		}else{
			value[0] = temp;
		}
		for (int i=1; i < args.length; i++){
			value[i] = args[i];
		}
		if (args.length < 2){
			return  value[0];
		}

		result = new StringBuffer(value[0]);
		temp = value[0];
		int index1 =0;
		int index2 = 0;
		for (int i=1; i< value.length; i++){
			index1 = result.toString().indexOf("~PARAM" + i + "_");
			if (index1 >=0){
				index2 = result.toString().indexOf("~", index1 +7)+1;
				if (index2 > 7 ){
					result = result.replace(index1, index2, value[i]);
				}
			}
		}
		return result.toString();
	}
}
