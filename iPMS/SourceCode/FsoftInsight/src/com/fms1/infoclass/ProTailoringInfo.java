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
public final class ProTailoringInfo {
	public long TailoringID=0;
	public String ProcessName=null;
	public int ProcessID=0;
	public String Tailoring_per=null;
	public String Applicable_Cri=null;
    public byte tailStatus = 0;
    public byte tailLyfeCycle = 0;
    public String lyfeCycleName;
	public int action = 0;
	public ProTailoringInfo() {}
	public ProTailoringInfo(int iTailID,int iProID,String ProName, String permission,String applicable,
                            byte vStatus, byte vLyfeCycle) {
		TailoringID=iTailID;
		ProcessName=ProName;
		Tailoring_per=permission;
		Applicable_Cri=applicable;
		ProcessID=iProID;
        tailStatus = vStatus;
        tailLyfeCycle = vLyfeCycle;
	}
    
    public static String parseLifecycle(int type) {
            String valReturn = "Error";
            switch (type) {
                case -1:
                    valReturn = "All";
                    break;
                case 0 :
                    valReturn = "Development";
                    break;
                case 1 :
                    valReturn = "Maintenance";
                    break;
                case 2 :
                    valReturn = "Others";
                    break;
                case 3 :
                    valReturn = "General";
                    break;
                 default: 
                    valReturn = "";   
    
            }
            return valReturn;
    }   
          
    
}
