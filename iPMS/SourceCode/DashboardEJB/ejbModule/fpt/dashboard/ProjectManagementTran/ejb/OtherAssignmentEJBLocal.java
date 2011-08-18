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
 
 package fpt.dashboard.ProjectManagementTran.ejb;
/**
 * Local interface for Enterprise Bean: OtherAssignmentEJB
 */
public interface OtherAssignmentEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * addAssignment
	 */
	public void addAssignment(
		int Developer_ID,
		java.lang.String From_date,
		java.lang.String To_date,
		int Type,
		java.lang.String Description,
		java.lang.String usage)
		throws java.sql.SQLException;
	/**
	 * getDeveloper
	 */
	public java.util.Collection getDeveloper() throws java.sql.SQLException;
	/**
	 * getList
	 */
	public java.util.Collection getList() throws java.sql.SQLException;
	/**
	 * delete
	 */
	public void delete(java.lang.String so) throws java.sql.SQLException;
	/**
	 * getGroup
	 */
	public java.util.Collection getGroup() throws java.sql.SQLException;
	/**
	 * getListnew
	 */
	public java.util.Collection getListnew(
		java.lang.String From_date,
		java.lang.String To_date,
		java.lang.String Group)
		throws java.sql.SQLException;
	/**
	 * getOffTime
	 */
	public java.util.Collection getOffTime(java.lang.String strID)
		throws java.sql.SQLException;
}
