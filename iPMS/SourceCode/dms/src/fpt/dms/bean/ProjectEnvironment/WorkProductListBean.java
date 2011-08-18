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
 
 package fpt.dms.bean.ProjectEnvironment;

import fpt.dms.framework.util.StringUtil.StringMatrix;

public class WorkProductListBean
{
	int nUpdateWorkProductSize;
	int nProjectID;
	private StringMatrix WorkProductSizeList = null;
	private String[] selectedWorkProductSize = null;
	private StringMatrix AssignToList = null;

	private String strPlannedDefect = "";
	private String strReplannedDefect = "";
	//////////////////////////////////////////////////////////////////////
	public StringMatrix getWorkProductSizeList()
	{
		return WorkProductSizeList;
	}
	public void setWorkProductSizeList(StringMatrix WorkProductSizeList)
	{
		this.WorkProductSizeList = WorkProductSizeList;
	}
	public void setSelectedWorkProductSize(String[] selectedWorkProductSize)
	{
		this.selectedWorkProductSize = selectedWorkProductSize;
	}
	public String[] getSelectedWorkProductSize()
	{
		return selectedWorkProductSize;
	}
	public int getProjectID()
	{
		return nProjectID;
	}
	public void setProjectID(int inData)
	{
		nProjectID = inData;
	}
	public int getUpdateWorkProductSize()
	{
		return nUpdateWorkProductSize;
	}
	public void setUpdateWorkProductSize(int inData)
	{
		nUpdateWorkProductSize = inData;
	}
	public StringMatrix getListing()
	{
		return AssignToList;
	}
	public void setListing(StringMatrix AssignToList)
	{
		this.AssignToList = AssignToList;
	}
	
	String strClientMessage = "";
	public String getClientMessage()
	{
		return this.strClientMessage;
	}
	public void setClientMessage(String data)
	{
		this.strClientMessage = data;
	}
	/////////////
	// WorkProduct Combo:
	private StringMatrix smxComboWorkProduct;
	public StringMatrix getComboWorkProduct()
	{
		return smxComboWorkProduct;
	}
	public void setComboWorkProduct(StringMatrix inCombo)
	{
		smxComboWorkProduct = inCombo;
	}
	//UnitCombo
	private StringMatrix smxComboUnit;
	public StringMatrix getComboUnit()
	{
		return smxComboUnit;
	}
	public void setComboUnit(StringMatrix inCombo)
	{
		smxComboUnit = inCombo;
	}
	public String getPlannedDefect()
	{
		return this.strPlannedDefect;
	}
	public void setPlannedDefect(String data)
	{
		this.strPlannedDefect = data;
	}
	public String getReplannedDefect()
	{
		return this.strReplannedDefect;
	}
	public void setReplannedDefect(String data)
	{
		this.strReplannedDefect = data;
	}
}