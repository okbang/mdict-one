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

import com.fms1.common.WorkUnit;
import com.fms1.infoclass.WorkUnitInfo;
import com.fms1.web.html.Combo;

/**
 * groups and projects list combo
 * @author manu
 * @date Aug 8, 2004
 * 
 */
public class WUCombo extends Combo{
	public static final String meFilter="WUCombo";
	public static final String meUpdate="WUComboUpdate";
	public static final String label="Project/group";
	public WUCombo(HttpServletRequest request,int mode){
		this.mode=mode;
		construct(parse(request,mode),mode);
	}
	public WUCombo(long selectme,int mode){
		construct(selectme,mode);
	}
	void construct(long selectme,int mode){
		selected=selectme;
		//String link="<A href='"+Constants.HTML_WU_COMBO+"'></A>";
		StringBuffer retval= new StringBuffer(open((mode==MODE_FILTER)?meFilter:meUpdate));
		addOption(retval,-1,(mode==MODE_FILTER)?"all":"General",selected);
		Vector wulist=WorkUnit.getWUList(null);
		WorkUnitInfo inf;
		for (int i=0;i<wulist.size();i++){
			inf=(WorkUnitInfo)wulist.elementAt(i);
			if (inf.type==WorkUnitInfo.TYPE_GROUP && inf.workUnitID!=0) 
				addOption(retval,inf.workUnitID,inf.workUnitName,selected);
	
		}
		for (int i=0;i<wulist.size();i++){
			inf=(WorkUnitInfo)wulist.elementAt(i);
			if (inf.type==WorkUnitInfo.TYPE_PROJECT && inf.workUnitID!=0) 
				addOption(retval,inf.workUnitID,inf.workUnitName,selected);
	
		}
		html= retval.append(close()).toString();
	}
	public  static long parse(HttpServletRequest request,int mode){
		return parse(request,(mode==MODE_FILTER)?meFilter:meUpdate);

	}
}
