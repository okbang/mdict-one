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
 
 /*
 * @(#)Timesheet.java 18-April-03
 */

package fpt.timesheet.constant;

/**
 * Class Timesheet
 * Description: Constants used in Timesheet package.
 * @version 1.0 18-April-03
 * @author
 */
public class Timesheet {
    /** JNDI database name */
	public static final String DB_NAME = "java:jdbc/TimesheetDB";

	public static final int PQA_ROLE = 8;
	public static final int USER_QUIT = 4;

    /** total number of timesheet records per page */
    public static final int MAX_RECORDS = 50;

    /** timesheet status */
    public static final int ALL_STATUS 					= 0;
    public static final int UNAPPROVED_STATUS 			= 1;
    public static final int APPROVED_BY_LEADER_STATUS 	= 2;
    public static final int APPROVED_BY_OTHER_STATUS 	= 3;
    public static final int APPROVED_BY_QA_STATUS 		= 4;
    public static final int REJECTED_STATUS 			= 5;

    /** project status */
    public static final int PROJECT_STATUS_NULL 		= -2;
    public static final int PROJECT_STATUS_ALL 			= -1;
    public static final int PROJECT_STATUS_ONGOING 		= 0;
    public static final int PROJECT_STATUS_CLOSED 		= 1;
    public static final int PROJECT_STATUS_CANCELLED 	= 2;
    public static final int PROJECT_STATUS_TENTATIVE 	= 3;
    public static final int PROJECT_STATUS_RUNNING 		= 4; //Ongoing and Tentative projects

	//HanhTN add status tracking -- 10/08/2006
	//Ongoing and Closed project
	public static final int PROJECT_STATUS_ONGOING_CLOSED 	= 5;
	public static final int PROJECT_STATUS_ONGOING_TENTATIVE_CLOSED = 6;

    public static final String PROJECT_STATUS_ALL_STR 		= "All status";
    public static final String PROJECT_STATUS_ONGOING_STR 	= "On-going";
    public static final String PROJECT_STATUS_CLOSED_STR 	= "Closed";
    public static final String PROJECT_STATUS_CANCELLED_STR = "Cancelled";
    public static final String PROJECT_STATUS_TENTATIVE_STR = "Tentative";

    /** project type */
    public static final int PROJECTTYPE_EXTERNAL	= 0;
    public static final int PROJECTTYPE_INTERNAL 	= 8;
    public static final int PROJECTTYPE_PUBLIC 		= 9;
    public static final int PROJECTTYPE_ALL 		= 10;

    /** date format used in Timesheet */
    public static final String DATE_FORMAT 			= "MM/DD/YY";
    public static final String SQL_DATE_FORMAT 		= "MM/DD/YY";

    public static final String STATUS_UNAPPROVED 	= "unapproved";
    public static final String STATUS_PL 			= "PL";
    public static final String STATUS_GL 			= "GL";
    public static final String STATUS_MISC 			= "misc";
    public static final String STATUS_QA 			= "QA";
    public static final String STATUS_REJECTED 		= "rejected";
    public static final String STATUS_UNKNOWN 		= "unknown";

    // Report Type Summary
    public static final int REPORTTYPE_SUMMARY 			= 0;
    public static final int REPORTTYPE_PROCESS_TOW 		= 1;
    public static final int REPORTTYPE_PRODUCT_TOW 		= 2;
    public static final int REPORTTYPE_KPA_TOW 			= 3;
    public static final int REPORTTYPE_ACCOUNT_DATE 	= 4;

    public static final int REPORTTYPE_PROJECT_SUMMARY  = 5;
    public static final int REPORTTYPE_PROJECT_PROCESS  = 6;
    public static final int REPORTTYPE_PROJECT_PRODUCT  = 7;
    public static final int REPORTTYPE_PROJECT_KPA 		= 8;
    public static final int REPORTTYPE_PROJECT_TOW 		= 9;

	//Report type Lack Timesheet
	public static final int REPORTTYPE_LACKTS_GROUP		= 0;
	public static final int REPORTTYPE_LACKTS_PROJECT	= 1;
	public static final int REPORTTYPE_UNAPPROVED_PM 	= 2;
	public static final int REPORTTYPE_UNAPPROVED_QA 	= 3;
	public final static int REPORTTYPE_PENDING 			= 4;
	public final static int REPORTTYPE_LACK 			= 5;

	//Responsibility
	public static final int RESPONSIBILITY_DEV			= 0;
	public static final int RESPONSIBILITY_TESTER		= 1;
	public static final int RESPONSIBILITY_PTL			= 2;
	public static final int RESPONSIBILITY_PM			= 3;
	public static final int RESPONSIBILITY_EXTERNAL		= 5;
	public static final int RESPONSIBILITY_SQA			= 7;
	public static final int RESPONSIBILITY_PQA			= 8;
    public static final int RESPONSIBILITY_COMTER       = 9;
}