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
 * Created on Sep 14, 2005
 * 
 */
package com.fms1.infoclass;

/**
 * @author binhnt 
 *   
 */
public class DefectProductReplanInfo {
	public long wpID;
	public long moduleID;
	public String wpName=null;
	public String moduleName=null;	
	public double[] requirementReview={Double.NaN,Double.NaN};  
	public double[] designReview={Double.NaN,Double.NaN};
	public double[] codeReview={Double.NaN,Double.NaN};
	public double[] unitTest={Double.NaN,Double.NaN};
	public double[] integrationTest={Double.NaN,Double.NaN};
	public double[] systemTest={Double.NaN,Double.NaN};
	public double[] otherReview={Double.NaN,Double.NaN};
	public double[] otherTest={Double.NaN,Double.NaN};
	public double[] others={Double.NaN,Double.NaN};
	public double[] total={Double.NaN,Double.NaN};
	public boolean released=false;
}
