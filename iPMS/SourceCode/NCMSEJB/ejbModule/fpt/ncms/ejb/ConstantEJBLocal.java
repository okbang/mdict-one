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
 
 package fpt.ncms.ejb;
/**
 * Local interface for Enterprise Bean: ConstantEJB
 */
public interface ConstantEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * queryConstantByID
	 */
	public java.util.ArrayList queryConstantByID(
		java.lang.String strConstantID)
		throws java.sql.SQLException;
	/**
	 * queryConstantByType
	 */
	public java.util.ArrayList queryConstantByType(
		java.lang.String strConstantType,
		int nUsage)
		throws java.sql.SQLException;
	/**
	 * queryConstant
	 */
	public java.util.ArrayList queryConstant(
		java.lang.String strConstantType,
		int nUsage,
		int nFromRow,
		int nToRow)
		throws java.sql.SQLException;
	/**
	 * queryConstantType
	 */
	public java.util.ArrayList queryConstantType(
		int nUsage,
		boolean isShowAll,
		java.lang.String strExcludeConstants)
		throws java.sql.SQLException;
	/**
	 * getNumByType
	 */
	public int getNumByType(
		java.lang.String strConstantType,
		int nUsage,
		boolean isShowAll)
		throws java.sql.SQLException;
	/**
	 * deleteConstant
	 */
	public int deleteConstant(java.lang.String strConstantID)
		throws java.sql.SQLException;
	/**
	 * insertConstant
	 */
	public int insertConstant(
		java.lang.String strConstantType,
		java.lang.String strDescription,
		int nUsage)
		throws java.sql.SQLException;
	/**
	 * updateConstant
	 */
	public int updateConstant(
		java.lang.String strDescription,
		java.lang.String strConstantID,
		int nUsage)
		throws java.sql.SQLException;
}
