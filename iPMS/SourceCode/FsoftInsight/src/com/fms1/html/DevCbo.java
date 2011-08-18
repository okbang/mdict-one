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
 
 package com.fms1.html;

//import java.text.MessageFormat; should be used in the future
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import com.fms1.common.Assignments;
import com.fms1.common.UserHelper;
import com.fms1.infoclass.ResponsibilityInfo;
import com.fms1.infoclass.UserInfo;
import com.fms1.web.html.Combo;
/**
 * Developper list combo
 * @author manu
 */
public class DevCbo extends Combo{
	public static final String meUpdate="devUpdate";
	public static final String label="User";
	public DevCbo(HttpServletRequest request){
		construct(parse(request));
	}
	public DevCbo(long selectme){
		construct(selectme);
	}
	public DevCbo(long selectme,String groupname){
		construct(selectme,groupname);
	}
	void construct(long selectme){
		construct(selectme,null);
	}
		
	void construct(long selectme, String groupname){
		selected=selectme;
		StringBuffer retval= new StringBuffer(open(meUpdate));
		Vector list;
		if (groupname==null)
			list=UserHelper.getAllUsers();
		else
			list=UserHelper.getUsersByGroup(groupname);
			
		UserInfo inf;
		for (int i=0;i<list.size();i++){
			inf=(UserInfo)list.elementAt(i);
			addOption(retval,inf.developerID,inf.account,selected);
	
		}
		html= retval.append(close()).toString();
	}
	public  static long parse(HttpServletRequest request){
		return parse(request,meUpdate);

	}
}
