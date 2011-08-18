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
 
 package com.fms1.web.html;

//import java.text.MessageFormat; should be used in the future
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.fms1.common.WorkUnit;
import com.fms1.tools.CommonTools;


/**
 * Experiment: this object is made to be called directly from JSP, it returns HTML
 * If it's a control, the control name is the name of the object.
 * caching
 * @author manu
 * @date Aug 8, 2004
 * 
 * 
 */
public abstract  class Combo {
	public int status;
	/*public	String meFilter;//html name of the combo, in filter mode
	public	String meUpdate;//html name of the combo, in update mode*/
	static	final String option1="<OPTION value='";
	static final String option2="'>";
	static final String option2Selected="' selected >";
	static final String option3="</OPTION>";

	//fghpublic static String label;
	public String html="";
	public long selected=-1;
	public int mode;
	public final static int MODE_UPDATE=0;
	public final static int MODE_FILTER=1;
	/*public Combo(HttpServletRequest request,int mode){
		construct((mode==MODE_FILTER)?parse(request):parseUpdate(request),mode);
	}
	public Combo(long selectme,int mode){
		construct(selectme,mode);
	}*/
	//sets the HTML comtent. must be overriden
	//public  abstract void construct(long selectme,int mode);
	protected  String open(String name){
		return "<SELECT name='"+name+"'>";
	}
	protected  String close(){
		return "</SELECT>";
	}
	protected static void addOption(StringBuffer buf,long value,String name,long selected){
		buf.append(option1).append(value).append((value==selected)?option2Selected:option2).append(name).append(option3); 
	}
	protected static long parse(HttpServletRequest request,String name){
		
		if(request!=null){
			String requestStr=request.getParameter(name);
			if (requestStr!=null)
				return CommonTools.parseLong(requestStr);
		}
			
		return -1;
	}

}
