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
 
 package fpt.dms.bean.DefectManagement;
public class QueryAddBean
{
	String[] FieldName = null;
	String[] NotOpe = null;
	String[] Criteria = null;
	String[] Value = null;
	String[] Logical = null;
	String[] Group = null;
	String scope = "";
	String Name = "";
	public String[] getCriteria()
	{
		return Criteria;
	}
	public String[] getFieldName()
	{
		return FieldName;
	}
	public String[] getGroup()
	{
		return Group;
	}
	public String[] getLogical()
	{
		return Logical;
	}
	public String getName()
	{
		return Name;
	}
	public String[] getNotOpe()
	{
		return NotOpe;
	}
	public String[] getValue()
	{
		return Value;
	}
	public String getScope()
	{
		return scope;
	}
	public void setValue(String[] Value)
	{
		this.Value = Value;
	}
	public void setScope(String scope)
	{
		this.scope = scope;
	}
	public void setNotOpe(String[] NotOpe)
	{
		this.NotOpe = NotOpe;
	}
	public void setName(String Name)
	{
		this.Name = Name;
	}
	public void setLogical(String[] Logical)
	{
		this.Logical = Logical;
	}
	public void setGroup(String[] Group)
	{
		this.Group = Group;
	}
	public void setFieldName(String[] FieldName)
	{
		this.FieldName = FieldName;
	}
	public void setCriteria(String[] Criteria)
	{
		this.Criteria = Criteria;
	}
}