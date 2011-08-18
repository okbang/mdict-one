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
 
 package com.fms1.infoclass;
public final class WorkProductInfo{
	public static final int BASELINE_REPORT = 27;
	public static final int WO = 1;
	public static final int URD = 2;
	public static final int SRS = 3;
	public static final int PP = 4;
	public static final int ARCHITECTURAL_DESIGN = 5;
	public static final int TEST_PLAN = 6;
	public static final int TEST_CASE_DATA = 7;
	public static final int DETAILED_DESIGN = 8;
	public static final int SOFTWARE_PACKAGE = 9;
	public static final int RELEASE_NOTE = 14;
	public static final int USER_MANUAL = 15;
	public static final int REQUIREMENT_PROTOTYPE = 21;
	public static final int DESIGN_PROTOTYPE = 42;
	public static final int INTEGRATION_TEST_CASE = 69;
	public static final int SYSTEM_TEST_CASE = 7;
	public static final int UNIT_TEST_CASE = 68;
	public long workProductID;
	public String workProductName;
	public double plannedDefects = Double.NaN;
	public double rePlannedDefects = Double.NaN;
}
