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
 
 package com.fms1.infoclass.group;

public class DefectTypeInfo {
	
    public long nProjectId;
    public String strCode;
    public String strGroupName;
    public double dProjectSize;
    public int nRequirementDefect;
    public int nFeatureDefect;
    public int nCodingLogicDefect;
    public int nBusinessDefect;
    public int nOtherFuncDefect;
    public int nInterfaceDefect;
    public int nCodingStandardDefect;
    public int nDocumentDefect;
    public int nDesignDefect;
    public int nOtherDefect;
    
    public DefectTypeInfo()
    {
	    nProjectId = 0;
	    strCode = "";
	    strGroupName = "";
	    dProjectSize = 0;
	    nRequirementDefect = 0;
	    nFeatureDefect = 0;
	    nCodingLogicDefect = 0;
	    nBusinessDefect = 0;
	    nOtherFuncDefect = 0;
	    nInterfaceDefect = 0;
	    nCodingStandardDefect = 0;
	    nDocumentDefect = 0;
	    nDesignDefect = 0;
	    nOtherDefect = 0;
    }

	public DefectTypeInfo (long p1, String p2, String p3, double p4, int p5,
	    int p6, int p7, int p8, int p9, int p10, int p11, int p12, int p13, int p14)
	{
	    nProjectId = p1;
	    strCode = p2;
	    strGroupName = p3;
	    dProjectSize = p4;
	    nRequirementDefect = p5;
	    nFeatureDefect = p6;
	    nCodingLogicDefect = p7;
	    nOtherFuncDefect = p8;
	    nInterfaceDefect = p9;
	    nCodingStandardDefect = p10;
	    nDocumentDefect = p11;
	    nDesignDefect = p12;
	    nOtherDefect = p13;
	    nBusinessDefect = p14;
	}

    public int getTotalWeightedDefect() {
        return nRequirementDefect + nFeatureDefect + nCodingLogicDefect + nBusinessDefect + 
        		nOtherFuncDefect + nInterfaceDefect + nCodingStandardDefect + nDocumentDefect + 
        		nDesignDefect + nOtherDefect;
    }
	
}
