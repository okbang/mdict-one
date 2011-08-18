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
 
 package fpt.dashboard.framework.util;

public class SqlUtil {
	private final static String SQL_DATE_FORMAT = "dd/mm/yy";
	/**
	 * generate date constraint for SQL statement
	 * @param strDate name of date field
	 * @param strFromDate from date value
	 * @param strToDate to date value
	 * @return String
	 */
	public static String genDateConstraint(
		String strFieldName,
		String strFromDate,
		String strToDate) {
		StringBuffer sbTemp = new StringBuffer();
		if (strFromDate != null) {
			if (strFromDate.trim().length() > 0) {
				sbTemp
					.append(" AND ")
					.append(strFieldName)
					.append(">=TO_DATE('")
					.append(strFromDate)
					.append("','")
					.append(SQL_DATE_FORMAT)
					.append("')");
			}
		}
		if (strToDate != null) {
			if (strToDate.trim().length() > 0) {
				sbTemp
					.append(" AND ")
					.append(strFieldName)
					.append("<=TO_DATE('")
					.append(strToDate)
					.append("','")
					.append(SQL_DATE_FORMAT)
					.append("')");
			}
		}
		return sbTemp.toString();
	}

	/**
	 * Set begining position of scrollable ResultSet
	 * @param rs  The ResultSet
	 * @param nPage Page number <BR>
	 * (< 0): Do not set begin position <BR>
	 * 0    : jump to page 1
	 * > 0  : jump to this page
	 * @param nPageSize
	 * @return int : Number of records of this ResultSet
	 */
	public static int setFetchPosition(
		java.sql.ResultSet rs,
		int nPage,
		int nPageSize) {
		int nResult = 0;
		try {
			int nActualPage = nPage;
			if (nPage <= 0) {
				nActualPage = 1;
			}
			rs.last(); // Go to last record
			int nCount = rs.getRow(); // =>Number of rows returned
			int nTotalPage = ((nCount + nPageSize - 1) / nPageSize);
			// Total pages
			if (nPage > nTotalPage) {
				// Input page number is larger than total pages
				nActualPage = 1;
			}
			int nStart = (nActualPage - 1) * nPageSize;
			rs.first();
			rs.relative(nStart - 1); //Begin fetch position of records

			nResult = nCount;
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nResult;
	}
    
    /**
     *  0:Developer<BR>
        1:Tester<BR>
        2:Project Leader<BR>
        3:Project Director<BR>
        4:Graphic Designer<BR>
        5:External<BR>
        6:Onsite Coordinator<BR>
        7:SQA<BR>
        8:PQA<BR>
        9:Communicator<BR>      
        role for converting: Developer = {Developer,Communicator}<BR>
                             Project Leader = {Project Leader,Project Director}<BR>
                             Tester/SQA = {Tester,SQA,PQA}<BR>
     * @param response
     * @return
     */
    public final static String getDefectPermission(long response) {
        // developer
        if (response == 0 || response == 9 || response == 4 || response == 6) {
            return "1000000000";
        }
        // project leader
        else if (response == 2 || response == 3) {
            return "1110000000";
        }
		else if (response == 5) {
			return "0000001000";
		}
        // tester/sqa;
        else if (response == 1 || response == 7 || response == 8) {
            return "1100000000";
        }
        else {
            return "0000000000";
        }
    }
}
