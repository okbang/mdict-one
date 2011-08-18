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
 
 package fpt.dashboard.bean.ResourceManagement;

import java.util.Vector;

public class ResourceWeekBean
{
	int nDoViewDetailAssignment;
	Vector BusyDevID = null;
	Vector BusyDevName = null;
	Vector FreeDevID = null;
	Vector FreeDevName = null;
	String month;
	String year;
	String rang;
	String condi;
	Vector BusyTable = null;
	Vector Tooltip = null;
	String[] arrGroup = null;
	public int getDoViewDetailAssignment()
	{
		return nDoViewDetailAssignment;
	}
	public void setDoViewDetailAssignment(int inData)
	{
		nDoViewDetailAssignment = inData;
	}
	public Vector getBusyDevID()
	{
		return BusyDevID;
	}
	public void setBusyDevID(Vector BusyDevID)
	{
		this.BusyDevID = BusyDevID;
	}
	public void setBusyDevName(Vector BusyDevName)
	{
		this.BusyDevName = BusyDevName;
	}
	public void setBusyTable(Vector BusyTable)
	{
		this.BusyTable = BusyTable;
	}
	public Vector getBusyTable()
	{
		return BusyTable;
	}
	public Vector getBusyDevName()
	{
		return BusyDevName;
	}
	public Vector getFreeDevID()
	{
		return FreeDevID;
	}
	public void setFreeDevID(Vector FreeDevID)
	{
		this.FreeDevID = FreeDevID;
	}
	public void setFreeDevName(Vector FreeDevName)
	{
		this.FreeDevName = FreeDevName;
	}
	public Vector getFreeDevName()
	{
		return FreeDevName;
	}
	public String getMonth()
	{
		return month;
	}
	public void setMonth(String month)
	{
		this.month = month;
	}
	public void setTooltip(Vector Tooltip)
	{
		this.Tooltip = Tooltip;
	}
	public Vector getTooltip()
	{
		return Tooltip;
	}
	public String getYear()
	{
		return year;
	}
	public void setYear(String year)
	{
		this.year = year;
	}
	public String[] getArrGroup()
	{
		return arrGroup;
	}
	public void setArrGroup(String[] arrGroup)
	{
		this.arrGroup = arrGroup;
	}
	public String getRang()
	{
		return rang;
	}
	public void setRang(String rang)
	{
		this.rang = rang;
	}
	public String getCondi()
	{
		return condi;
	}
	public void setCondi(String condi)
	{
		this.condi = condi;
	}
}