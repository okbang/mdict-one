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
 * Created on Jan 5, 2005
 *
 */
package fpt.dms.bean.DefectManagement;

/**
 * @author trungtn
 */
public class DefectAttachDataBean {
    private String strAttachId;
    private String strFileName;
    private String strDefectId;
    private String strDeveloperId;
    private int n_Size;
    private byte[] arr_Content;
    
    /**
     * @return
     */
    public byte[] getContent() {
        return arr_Content;
    }

    /**
     * @return
     */
    public int getSize() {
        return n_Size;
    }

    /**
     * @return
     */
    public String getAttachId() {
        return strAttachId;
    }

    /**
     * @return
     */
    public String getDefectId() {
        return strDefectId;
    }

    /**
     * @return
     */
    public String getDeveloperId() {
        return strDeveloperId;
    }

    /**
     * @return
     */
    public String getFileName() {
        return strFileName;
    }

    /**
     * @param bs
     */
    public void setContent(byte[] bs) {
        arr_Content = bs;
    }

    /**
     * @param i
     */
    public void setSize(int i) {
        n_Size = i;
    }

    /**
     * @param string
     */
    public void setAttachId(String string) {
        strAttachId = string;
    }

    /**
     * @param string
     */
    public void setDefectId(String string) {
        strDefectId = string;
    }

    /**
     * @param string
     */
    public void setDeveloperId(String string) {
        strDeveloperId = string;
    }

    /**
     * @param string
     */
    public void setFileName(String string) {
        strFileName = string;
    }

}
