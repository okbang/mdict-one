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

import java.util.*;

public class Constants
{
	public static final String ALL = "All";	//stored  = 0
	
    public static final String STATUS_ALL = "All status";   //stored  = 0
    public static final String STATUS_WORKING = "Permanent/Temporary";   //stored  = '1,2'
	public static final String STAFF = "Permanent";	//stored  = 1
	public static final String COLLABORATOR = "Temporary";	//stored  = 2
	public static final String EXTERNAL_VIEWER = "External viewer";	//stored  = 3
	public static final String OUTPLACED = "Quit";	//stored  = 4
	
    public static String VALUE_STATUS_ALL = "0";
    public static String VALUE_STATUS_WORKING = "1,2";
	public static String VALUE_STAFF = "1";
	public static String VALUE_COLLABORATOR = "2";
	public static String VALUE_EXTERNAL = "3";
	public static String VALUE_OUTPLACED = "4";
	
	public static final String DEVELOPER = "Developer";
	public static final String PROJECT_LEADER = "Project Leader";
	public static final String GROUP_LEADER = "Group Leader";
	public static final String FIST = "FIST";
	public static final String PQA = "PQA";
	public static final String MANAGER = "Manager";
	public static final String EXTERNAL_PL = "External of project level";
	public static final String EXTERNAL_GL = "External of group level";
    public static final String COMMUNICATOR = "Communicator";
	public static final String UNKNOWN = "";
	//ThaiLH
	public static final String GROUP_ALL = "All";

	
	
}

