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
 
 package fpt.ncms.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import fpt.ncms.constant.NCMS;

public class SqlUtil {
    /**
     * generate date constraint for SQL statement
     * @param strDate name of date field
     * @param strFromDate from date value
     * @param strToDate to date value
     * @return String
     */
    public static String genDateConstraint(String strFieldName, String strFromDate, String strToDate) {
        StringBuffer sbTemp = new StringBuffer();
        if (strFromDate != null) {
            if (strFromDate.trim().length() > 0) {
                sbTemp.append(" AND ").append(strFieldName)
                      .append(">=TO_DATE('")
                      .append(strFromDate).append("','")
                      .append(NCMS.SQL_DATE_FORMAT_EXT)
                      .append("')");
            }
        }
        if (strToDate != null) {
            if (strToDate.trim().length() > 0) {
                // [To Date] means that end of this date e.g time is 23:59:59
                sbTemp.append(" AND ")
                      .append(strFieldName).append("<=TO_DATE('")
//                .append(strToDate).append(" 23:59:59").append("','")
//                .append(NCMS.SQL_DATE_FORMAT_FULL)
//                .append("')");
                    .append(strToDate).append(" 23:59:59").append("','DD-MON-YY HH24:MI:SS')");
            }
        }
        return sbTemp.toString();
    }

    /**
     * generate TO_DATE function for SQL statement
     * @param cal The calendar
     * @return String
     */
    public static String genSQL_To_Date(Calendar cal) {
        StringBuffer sbTemp = new StringBuffer();
        SimpleDateFormat formatter =
            new SimpleDateFormat(NCMS.DATE_FORMAT_FULL);
        if (cal != null) {
            sbTemp.append("TO_DATE('")
                  .append(formatter.format(cal.getTime())).append("','")
                  .append(NCMS.SQL_DATE_FORMAT_FULL).append("')");
            return sbTemp.toString();
        }
        else {
            return "null";
        }
    }
    
    /**
     * generate Table Name (NC, Call,...) for SQL statement
     * @param nLocation The login option
     * @return String
     */
    public static String getTableName(int nLocation) {
        switch (nLocation) {
            case 0:
                return "NC";
            case 1:
                return "Call";
            default:
                return null;
        }
    }
    
    /**
     * generate History Table Name (NCHistory, CallPQA_History,...) for SQL statement
     * @param nLocation The login option
     * @return String
     */
    public static String getHistoryTableName(int nLocation) {
        switch (nLocation) {
            case 0:
                return "NCHistory";
            case 1:
                return "Call_History";
            default:
                return null;
        }
    }
    
}
