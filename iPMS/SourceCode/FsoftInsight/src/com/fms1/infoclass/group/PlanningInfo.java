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
 
 package com.fms1.infoclass.group;
import java.util.Vector;
import com.fms1.infoclass.*;
import com.fms1.common.*;
/**
 * @author manu
 */
public class PlanningInfo {
	
	public long planningID;
	public int year;
	public long workUnit;
	public java.sql.Date lastUpdate;
	public Vector rows=new Vector();
	public float version;
	public boolean isGroup=false;
	public String planType;
	public class Row {
		public Row(int pMetricConstant,long pGroupID,String pGroupName,String pcolFormula,String prowFormula,String pyearFormula,String pinputConstraint){
			MetricDescInfo minfo= Metrics.getMetricDesc(pMetricConstant);
			metricConstant=pMetricConstant;
			strMetricID=minfo.metricID;
			unit=minfo.unit;
			groupID=pGroupID;
			groupName=pGroupName;
			metricName=minfo.metricName;
			colFormula=pcolFormula;
			rowFormula=prowFormula;
			yearFormula=pyearFormula;
			inputConstraint=pinputConstraint;
			for (int i=0;i<12;i++)values[i]=Double.NaN;
		}
		public Row(String sectionName){
			metricName=sectionName;
			groupID=-2;
		}
		public String strMetricID;
		public String unit;
		public String metricName;
		public int metricConstant;
		public double [] values= new double [12];
		public String assumption;
		public String groupName;
		public double yearTotal=Double.NaN;
		public String colFormula;
		public String rowFormula;
		public String yearFormula;
		public String inputConstraint;
		public long groupID=-1;//-1 is id if plan for the group
	}
}
