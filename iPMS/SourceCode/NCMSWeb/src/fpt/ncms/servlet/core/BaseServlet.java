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
 * @(#)BaseServlet.java 15-Mar-03
 */


package fpt.ncms.servlet.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Class BaseServlet
 * Description: abstract class for controlling NCMS servlet
 * @version 1.0 15-Mar-03
 * @author
 */
public abstract class BaseServlet
        extends HttpServlet implements SingleThreadModel {

    private static final Logger logger =
            Logger.getLogger(
                    fpt.ncms.servlet.core.BaseServlet.class.getName());

    /**
     * BaseServlet
     * Class constructor
     */
    public BaseServlet() {
    }

    /**
     * Get root path
     * @param request
     * @return
     */
    private String getRootPath(HttpServletRequest request) {
        String rootPath = "";
        String servletPath = request.getPathInfo();
        if (servletPath == null) {
          servletPath = request.getServletPath();
        }
        if ((servletPath != null) && (servletPath.trim().length() > 0)) {
            int lastindex = servletPath.lastIndexOf("/");
            if (lastindex >= 0) {
                rootPath = servletPath.substring(0, lastindex + 1);
            }
            else {
                rootPath = new String("");
            }
        }
        return rootPath.trim();
    }

    /**
     * callPage
     * Forward request to specific page
     * @param   response - response object
     * @param   strJSPPage - name of page to forward to
     * @throws  Exception
     */
    protected final void callPage(HttpServletResponse response,
            String strJSPPage, HttpServletRequest request)
            throws Exception {
        getServletContext().getRequestDispatcher(this.getRootPath(request) + strJSPPage).forward(
                request, response);
    }

    /**
     * doGet
     * Perform HTTP GET
     * @param   request - request object
     * @param   response - response object
     */
    public final void doGet(HttpServletRequest request,
            HttpServletResponse response) {
        performTaskCatchingErrors(request, response);
    }

    /**
     * doPost
     * Perform HTTP POST
     * @param   request - request object
     * @param   response - response object
     */
    public final void doPost(HttpServletRequest request,
            HttpServletResponse response) {
        performTaskCatchingErrors(request, response);
    }

    /**
     * getParameter
     * Get parameter
     * @param   request - request object
     * @param   parameterName - name of parameter
     * @param   checkRequestParameters - indicator for req.parameters
     * @param   checkInitParameteres - indicator for init parameters
     * @param   isParameterRequired - indicator for para.requirement
     * @param   defaultValue - default value for parameter
     * @return  parameter
     * @throws  Exception
     */
    public final String getParameter(HttpServletRequest request,
            String parameterName, boolean checkRequestParameters,
            boolean checkInitParameters,
            boolean isParameterRequired, String defaultValue)
            throws Exception {
        String[] parameterValues;
        String paramValue = null;
        if (checkRequestParameters) {
            parameterValues = request.getParameterValues(parameterName);
            if (parameterValues != null) {
                paramValue = parameterValues[0];
            }
        }
        if (checkInitParameters && paramValue == null) {
            paramValue = getServletConfig().getInitParameter(parameterName);
        }
        if (isParameterRequired && paramValue == null) {
            throw new Exception(parameterName
                    + " parameter was not specified.");
        }
        if (paramValue == null) {
            paramValue = defaultValue;
        }
        return paramValue;
    }

    /**
     * handleError
     * Description  handle error
     * @param   request - request object
     * @param   response - response object
     * @param   theException exception to throw
     */
    private void handleError(HttpServletRequest request,
            HttpServletResponse response, Throwable theException) {
        try {
            HttpSession session = request.getSession(true);
            session.invalidate();
            session = request.getSession(true);
            GenericResultBean resultBean = new GenericResultBean();
            resultBean.setMessage("An error has occured, please try later"
                    + "\n\n\n\n<!--" + theException.toString() + "-->");
            session.setAttribute("resultBean", resultBean);
            callPage(response, "/error.jsp", request);
            return;
        }
        catch (Exception exception) {
        }
    }

    /**
     * performTask
     * Abstract method for handle task
     * @param   httpservletrequest - request object
     * @param   httpservletresponse - response object
     * @throws  Throwable
     */
    protected abstract void performTask(HttpServletRequest httpservletrequest,
            HttpServletResponse httpservletresponse);

    /**
     * performTaskCatchingErrors
     * Perform task with specific error handling
     * @param   request - request object
     * @param   response - response object
     */
    private void performTaskCatchingErrors(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            performTask(request, response);
        }
        catch (Exception theException) {
            try {
                HttpSession session = request.getSession(true);
                session.invalidate();
                session = request.getSession(true);
                GenericResultBean resultBean = new GenericResultBean();
                resultBean.setMessage("An error has occured, please try later"
                        + "\n\n\n\n<!--" + theException.toString() + "-->");
                session.setAttribute("resultBean", resultBean);
                callPage(response, "/error.jsp", request);
                return;
            }
            catch (Exception exception) {
            }
            return;
        }
        catch (Throwable e) {
            handleError(request, response, e);
        }
    }

    /**
     * setNoCaching
     * Set HTTP mode to no-cache to avoid problems with refreshing
     * @param   response - response object
     */
    protected static final void setNoCaching(HttpServletResponse res) {
        res.setHeader("Expires", "0");
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Cache-Control", "no-cache");
    }

    /**
     * setRequestAttribute
     * Map attribute to an object
     * @param   strAttrName - name of attribute
     * @param   obj - object for mapping
     * @param   request - request object
     * @throws  ServletException
     * @throws  IOException
     */
    public static final void setRequestAttribute(String strAttrName,
            Object obj, HttpServletRequest request) {
        request.setAttribute(strAttrName, obj);
    }
}