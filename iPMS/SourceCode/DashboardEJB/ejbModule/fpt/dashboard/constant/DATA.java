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
 
 package fpt.dashboard.constant;
public class DATA
{
	public static final String GROUP_ALL = "All";
	public static final String PROJECT_TYPE_EXTERNAL = "External";
	public static final String PROJECT_TYPE_INTERNAL = "Internal";
	public static final String PROJECT_TYPE_ALL = "All types";		//ThaiLH
	public static final String PROJECT_TYPE_PUBLIC = "Public";

	public static final String PROJECT_STATUS_ONGOING = "On-going";
	public static final String PROJECT_STATUS_TENTATIVE = "Tentative";
	public static final String PROJECT_STATUS_CLOSED = "Closed";
	public static final String PROJECT_STATUS_CANCELLED = "Cancelled";
	public static final String PROJECT_STATUS_ALL = "All status"; 	//ThaiLH

	public static final String PROJECT_CATEGORY_DEVELOPMENT = "Development";
	public static final String PROJECT_CATEGORY_MAINTENANCE = "Maintenance";
	public static final String PROJECT_CATEGORY_OTHERS = "Others";     //MinhPT
	public static final String PROJECT_CATEGORY_ALL = "All categories";//ThaiLH
	//ThaiLH
	public static final String ROLE_DEVELOPER = "1000000000";
	public static final String ROLE_PROJECTLEADER = "1100000000";
	public static final String ROLE_GROUPLEADER = "1110000000";
	public static final String ROLE_UNKNOWN = "0001000000";
	public static final String ROLE_PQA = "0000100000";
	public static final String ROLE_MANAGER = "0000010000";
	//external user of project level
	public static final String ROLE_EXTERNAL_PL = "0000001000";
	//external user of group level
	public static final String ROLE_EXTERNAL_GL = "0000001100";
	//communicator
	public static final String ROLE_COMMUNICATOR = "0000000010";

	public static final String ROLENAME_DEVELOPER = "Developer";
	public static final String ROLENAME_PROJECTLEADER = "Project leader";
	public static final String ROLENAME_GROUPLEADER = "Group leader";
	public static final String ROLENAME_UNKNOWN = "";
	public static final String ROLENAME_PQA = "PQA/SQA";
	public static final String ROLENAME_MANAGER = "Manager";
	//external user of project level
	public static final String ROLENAME_EXTERNAL_PL = "External of project";
	//external user of group level
	public static final String ROLENAME_EXTERNAL_GL = "External of group";
	//communicator
	public static final String ROLENAME_COMMUNICATOR = "Communicator";

	public static final int LOGIN_PL = 1; //Project Leader
	public static final int LOGIN_PQA = 2; //PQA
	public static final int LOGIN_UNKNOWN = 3; //Unknown
	public static final int LOGIN_EXTERNAL_GL = 4;
	//External user by group level
	public static final int LOGIN_EXTERNAL_PL = 5;
	//External user by group level

	//ThaiLH
	public static final String PROJECT_EXTERNAL = "0";
	public static final String PROJECT_INTERNAL = "8";
	public static final String PROJECT_ALL = "-1";
	public static final String PROJECT_PUBLIC = "9";

	public static final String PROJECT_VALUE_STATUS_ONGOING = "0";
	public static final String PROJECT_VALUE_STATUS_TENTATIVE = "3";
	public static final String PROJECT_VALUE_STATUS_CLOSED = "1";
	public static final String PROJECT_VALUE_STATUS_CANCELLED = "2";
	public static final String PROJECT_VALUE_STATUS_ALL = "-1";

	public static final String PROJECT_VALUE_CATEGORY_DEVELOPMENT = "0";
	public static final String PROJECT_VALUE_CATEGORY_MAINTENANCE = "1";
	public static final String PROJECT_VALUE_CATEGORY_OTHERS = "2";  //MinhPT
	public static final String PROJECT_VALUE_CATEGORY_ALL = "-1";
	//End of ThaiLH

}

