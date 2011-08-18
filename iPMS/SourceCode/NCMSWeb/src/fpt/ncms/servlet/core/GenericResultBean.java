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
 * @(#)GenericResultBean.java 15-Mar-03
 */


package fpt.ncms.servlet.core;

import javax.servlet.http.HttpServletRequest;

/**
 * Class GenericResultBean
 * Description  abstract class for bean result
 * @version 1.0 15-Mar-03
 * @author
 */
public final class GenericResultBean {
    private boolean m_bResult;
    /** Message to deliver */
    private String m_strMessage;

    /**
     * getMessage
     * Get a message to deliver
     * @return  message to deliver
     */
    public final String getMessage() {
        return m_strMessage;
    }

    /**
     * getParameterValue
     * Get a parameter
     * @param   request - request object
     * @param   paramName - name of a parameter
     * @return  value of a parameter
     */
    public static final String getParameterValue(HttpServletRequest request,
            String paramName) {
        String returnString = new String("");
        try {
            returnString = request.getParameter(paramName);
        }
        catch (Exception _ex) {
        }
        return returnString;
    }

    /**
     * getResult
     * Get a bean result
     * @return  result
     */
    public final boolean getResult() {
        return m_bResult;
    }

    /**
     * setMessage
     * Set a message to deliver
     * @param   newValue - message to deliver
     */
    public final void setMessage(String newValue) {
        m_strMessage = newValue;
    }

    /**
     * setResult
     * Set a bean result
     * @param   bSuccess - result
     */
    public final void setResult(boolean bSuccess) {
        m_bResult = bSuccess;
    }

    /**
     * GenericResultBean
     * Class constructor
     */
    public GenericResultBean() {
        m_bResult = false;
        m_strMessage = new String();
    }
}