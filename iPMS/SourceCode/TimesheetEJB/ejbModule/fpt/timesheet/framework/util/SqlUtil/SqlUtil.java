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
 
 package fpt.timesheet.framework.util.SqlUtil;

import fpt.timesheet.constant.Timesheet;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlUtil {
    // Processes
	public final static int PROCESS_CONTRACT_MAN 		= 1;
    public final static int PROCESS_REQUIREMENT 		= 2;
    public final static int PROCESS_DESIGN 				= 3;
    public final static int PROCESS_CODING 				= 4;
    public final static int PROCESS_DEPLOYMENT 			= 5;
    public final static int PROCESS_CUSTOMER_SUPPORT 	= 6;
    public final static int PROCESS_TEST 				= 7;
    public final static int PROCESS_CONFIGURATION_MAN 	= 8;
    public final static int PROCESS_PROJECT_MAN 		= 9;
    public final static int PROCESS_QUALITY_PLANNING 	= 10;
    public final static int PROCESS_SUBCONTRACT_MAN 	= 11;
    public final static int PROCESS_QUALITY_CONTROL 	= 12;
    public final static int PROCESS_INTERNAL_AUDIT 		= 13;
    public final static int PROCESS_CORRECTION 			= 14;
    public final static int PROCESS_MANAGEMENT_REVIEW 	= 15;
    public final static int PROCESS_RECRUITMENT 		= 16;
    public final static int PROCESS_TRAINING 			= 17;
    public final static int PROCESS_DOCUMENT_CONTROL 	= 18;
    public final static int PROCESS_IS_MANAGEMENT 		= 19;
    public final static int PROCESS_STAFF_MAN 			= 20;
    public final static int PROCESS_COLLABORATOR_MAN 	= 21;
    public final static int PROCESS_STUDENT_MANAGEMENT 	= 22;
    public final static int PROCESS_ADMINISTRATION 		= 23;
    public final static int PROCESS_FACILITY_MAN 		= 24;
    public final static int PROCESS_RETIREMENT 			= 25;
    public final static int PROCESS_OTHER 				= 26;
    public final static int PROCESS_TECHNOLOGY_MAN 		= 27;
	public final static int PROCESS_PREVENTION 			= 28;
    public final static int PROCESS_PROCESS_IMPROVEMENT = 29;
    
    // Type of work
	public final static int TOW_STUDY 		= 1;
    public final static int TOW_CREATE 		= 2;
	public final static int TOW_REVIEW 		= 3;
	public final static int TOW_TEST 		= 4;
    public final static int TOW_CORRECT 	= 5;
    public final static int TOW_TRANSLATION = 6;
    
    // KPA - not completed list, for mapping between processes and KPA
    public final static int KPA_RM = 1;
    public final static int KPA_PT = 3;
    public final static int KPA_QA = 5;
    public final static int KPA_CM = 6;
    public final static int KPA_PE = 11;
    public final static int KPA_DP = 16;

	/**
	 * Method genStringSplit
	 * @param arrProjectID
	 * @return String
	 */
	public static String genStringSplit(String arrProjectID) {
		StringBuffer strFilterSQL = new StringBuffer();
		int length =  arrProjectID.length() - 1;
		int pos =(int)(length /2);
		int k;
		for(k = 0; k<length-pos; k ++) {
			if(arrProjectID.substring(pos+k,k+pos+1).equals(",")){
				break;
			}
		}
		pos +=k+1;
		String strProjectList1 = arrProjectID.substring(0,pos-1);
		String strProjectList2 = arrProjectID.substring(pos,length+1);

		strFilterSQL.append(" AND (T.project_id IN (" + strProjectList1 + ") OR T.project_id IN (" + strProjectList2 + ")) ");			
		return strFilterSQL.toString();
	}

    /**
     * generate date constraint for SQL statement
     * @param strDate name of date field
     * @param strFromDate from date value
     * @param strToDate to date value
     * @return String
     */
    public static String genDateConstraint(String strDate, String strFromDate, String strToDate) {
        StringBuffer sbTemp = new StringBuffer();
        if (strFromDate != null) {
            if (strFromDate.trim().length() > 0) {
                sbTemp.append(" AND ").append(strDate).append(">=TO_DATE('");
                sbTemp.append(strFromDate).append("','").append(Timesheet.SQL_DATE_FORMAT);
                sbTemp.append("')");
            }
        }
        if (strToDate != null) {
            if (strToDate.trim().length() > 0) {
                sbTemp.append(" AND ").append(strDate).append("<=TO_DATE('");
                sbTemp.append(strToDate).append("','").append(Timesheet.SQL_DATE_FORMAT);
                sbTemp.append("')");
            }
        }
        return sbTemp.toString();
    }
    
    
	/**
	 * Method genBetweenStm
	 * @param strValue
	 * @param strFromDate
	 * @param strToDate
	 * @return p.actual_finish_date BETWEEN To_Date ('01/10/2005', 'dd/mm/yy') AND To_Date ('12/10/2005', 'dd/mm/yy')
	 */
	public static String genBetweenStm(String strValue, String strFromDate, String strToDate) {
		StringBuffer sbTemp = new StringBuffer();
		if (strFromDate != null) {
			if (strFromDate.trim().length() > 0) {
				sbTemp.append(strValue).append(" BETWEEN ").append("TO_DATE('");
				sbTemp.append(strFromDate).append("','").append(Timesheet.SQL_DATE_FORMAT);
				sbTemp.append("')");
			}
		}
		if (strToDate != null) {
			if (strToDate.trim().length() > 0) {
				sbTemp.append(" AND ").append("TO_DATE('");
				sbTemp.append(strToDate).append("','").append(Timesheet.SQL_DATE_FORMAT);
				sbTemp.append("')");
			}
		}
		return sbTemp.toString();
	}

    /**
     * Method genTranslation.
     * @param strTOW Column name of Type Of Work
     * @return String SQL String
     */
    public static String genTranslation(String strTOW) {
        StringBuffer sbTemp = new StringBuffer();
        sbTemp.append(" AND ");
        sbTemp.append(strTOW);
        sbTemp.append("=");
        sbTemp.append(TOW_TRANSLATION);
        return sbTemp.toString();
    }

    /**
     * Method genManagement.
     * @param strTOW Column name of Type Of Work
     * @param strProcess Column name of Process
     * @return String SQL String
     */
    public static String genManagement(String strTOW, String strProcess) {
        StringBuffer sbTemp = new StringBuffer();
        /*
        sbTemp.append(" AND ");
        sbTemp.append(strTOW);
        sbTemp.append("<>");
        sbTemp.append(TOW_TRANSLATION);
        */
        sbTemp.append(" AND ");
        sbTemp.append(strProcess);
        sbTemp.append(" IN(");
        sbTemp.append(PROCESS_CONTRACT_MAN).append(",");
        sbTemp.append(PROCESS_PROJECT_MAN).append(",");
        sbTemp.append(PROCESS_SUBCONTRACT_MAN).append(",");
        sbTemp.append(PROCESS_MANAGEMENT_REVIEW).append(",");
        sbTemp.append(PROCESS_RECRUITMENT).append(",");
        sbTemp.append(PROCESS_STAFF_MAN).append(",");
        sbTemp.append(PROCESS_COLLABORATOR_MAN).append(",");
        sbTemp.append(PROCESS_STUDENT_MANAGEMENT).append(",");
        sbTemp.append(PROCESS_RETIREMENT).append(",");
        sbTemp.append(PROCESS_IS_MANAGEMENT).append(",");
        sbTemp.append(PROCESS_ADMINISTRATION).append(",");
        sbTemp.append(PROCESS_FACILITY_MAN).append(",");
        sbTemp.append(PROCESS_TECHNOLOGY_MAN);
        sbTemp.append(")");

        return sbTemp.toString();
    }

    /**
     * Method genQuality.
     * @param strTOW Column name of Type Of Work
     * @param strProcess Column name of Process
     * @return String SQL String
     */
    public static String genQuality(String strTOW, String strProcess) {
        StringBuffer sbTemp = new StringBuffer();
        /*
        sbTemp.append(" AND ");
        sbTemp.append(strTOW);
        sbTemp.append("<>");
        sbTemp.append(TOW_TRANSLATION);
        */
        sbTemp.append(" AND (");
        sbTemp.append(strProcess);
        sbTemp.append(" IN(");
        sbTemp.append(PROCESS_TEST).append(",");
        sbTemp.append(PROCESS_QUALITY_PLANNING).append(",");
        sbTemp.append(PROCESS_QUALITY_CONTROL).append(",");
        sbTemp.append(PROCESS_INTERNAL_AUDIT).append(",");
        sbTemp.append(PROCESS_CORRECTION).append(",");
        sbTemp.append(PROCESS_PREVENTION).append(",");
        sbTemp.append(PROCESS_TRAINING).append(",");
        sbTemp.append(PROCESS_DOCUMENT_CONTROL).append(",");
        sbTemp.append(PROCESS_PROCESS_IMPROVEMENT);
        sbTemp.append(") OR (");

        sbTemp.append(strTOW);
        sbTemp.append(" IN(");
        sbTemp.append(TOW_CORRECT).append(",");
        sbTemp.append(TOW_TEST).append(",");
        sbTemp.append(TOW_REVIEW);
        sbTemp.append(") AND ");
        sbTemp.append(strProcess);
        sbTemp.append(" IN(");
        sbTemp.append(PROCESS_REQUIREMENT).append(",");
        sbTemp.append(PROCESS_DESIGN).append(",");
        sbTemp.append(PROCESS_CODING).append(",");
        sbTemp.append(PROCESS_DEPLOYMENT).append(",");
        sbTemp.append(PROCESS_CUSTOMER_SUPPORT).append(",");
        sbTemp.append(PROCESS_CONFIGURATION_MAN).append(",");
        sbTemp.append(PROCESS_OTHER);
        sbTemp.append(")");
        sbTemp.append(")");

        sbTemp.append(")");

        return sbTemp.toString();
    }

    /**
     * Method genCorrection.
     * @param strTOW Column name of Type Of Work
     * @param strProcess Column name of Process
     * @return String SQL String
     */
    public static String genCorrection(String strTOW, String strProcess) {
        StringBuffer sbTemp = new StringBuffer();
        /*
        sbTemp.append(" AND ");
        sbTemp.append(strTOW);
        sbTemp.append("<>");
        sbTemp.append(TOW_TRANSLATION);
        */
        sbTemp.append(" AND (");
        sbTemp.append(strProcess);
        sbTemp.append("=");
        sbTemp.append(PROCESS_CORRECTION);
        sbTemp.append(" OR (");

        sbTemp.append(strTOW);
        sbTemp.append("=");
        sbTemp.append(TOW_CORRECT);
        sbTemp.append(" AND ");
        sbTemp.append(strProcess);
        sbTemp.append(" IN(");
        sbTemp.append(PROCESS_REQUIREMENT).append(",");
        sbTemp.append(PROCESS_DESIGN).append(",");
        sbTemp.append(PROCESS_CODING).append(",");
        sbTemp.append(PROCESS_DEPLOYMENT).append(",");
        sbTemp.append(PROCESS_CUSTOMER_SUPPORT).append(",");
        sbTemp.append(PROCESS_TEST).append(",");
        sbTemp.append(PROCESS_CONFIGURATION_MAN).append(",");
        sbTemp.append(PROCESS_OTHER);
        sbTemp.append(")");
        sbTemp.append(")");

        sbTemp.append(")");

        return sbTemp.toString();
    }
    
    /**
     * Map from a Process to a KPA
     * @param nProcessID
     * @return int KPA_ID
     */
    public static int mapProcessKPA(int nProcessID) {
        switch (nProcessID) {
            case PROCESS_CONTRACT_MAN:
            case PROCESS_REQUIREMENT:
                return KPA_RM;
            
            case PROCESS_PROJECT_MAN:
                return KPA_PT;

            case PROCESS_QUALITY_CONTROL:
            case PROCESS_INTERNAL_AUDIT:
            case PROCESS_CORRECTION:
                return KPA_QA;

            case PROCESS_CONFIGURATION_MAN:
                return KPA_CM;

            case PROCESS_DESIGN:
            case PROCESS_CODING:
            case PROCESS_DEPLOYMENT:
            case PROCESS_CUSTOMER_SUPPORT:
            case PROCESS_TEST:
            case PROCESS_TRAINING:
                return KPA_PE;

            case PROCESS_PREVENTION:
                return KPA_DP;

            default:    // Others
                return KPA_PT;
        }
    }
}
