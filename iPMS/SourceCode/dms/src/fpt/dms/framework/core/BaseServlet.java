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
 
 package fpt.dms.framework.core;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet
{
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


    public void callPage(HttpServletResponse response, String strJSPPage, HttpServletRequest request)
        throws Exception
    {
//        response.addHeader("Pragma", "No-cache");
//        response.addHeader("Cache-control", "no-cache");
//        response.addDateHeader("Expires", -1);
        getServletContext().getRequestDispatcher(this.getRootPath(request) + strJSPPage).forward(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        performTaskCatchingErrors(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        performTaskCatchingErrors(request, response);
    }

    public String getParameter(HttpServletRequest request, String parameterName, boolean checkRequestParameters, boolean checkInitParameters, boolean isParameterRequired, String defaultValue)
        throws Exception
    {
        String parameterValues[] = null;
        String paramValue = null;
        if(checkRequestParameters)
        {
            parameterValues = request.getParameterValues(parameterName);
            if(parameterValues != null)
                paramValue = parameterValues[0];
        }
        if(checkInitParameters && paramValue == null)
            paramValue = getServletConfig().getInitParameter(parameterName);
        if(isParameterRequired && paramValue == null)
            throw new Exception(parameterName + " parameter was not specified.");
        if(paramValue == null)
            paramValue = defaultValue;
        return paramValue;
    }

    public void handleError(HttpServletRequest request, HttpServletResponse response, Throwable theException)
    {
        try
        {
            HttpSession session = request.getSession(true);
            session.invalidate();
            session = request.getSession(true);
            GenericResultBean resultBean = new GenericResultBean();
            logger.info("Error in performTask(): " + theException.toString());
            resultBean.setMessage("An error has occured, please try later\n\n\n\n<!--" + theException.toString() + "-->");
            session.setAttribute("resultBean", resultBean);
            callPage(response, "/error.jsp", request);
            return;
        }
        catch(Exception exception)
        {
            logger.error("Servlet exception handler throw exception:", exception);
        }
    }

    public abstract void performTask(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws Throwable;

    protected void performTaskCatchingErrors(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            performTask(request, response);
        }
        catch(Exception theException)
        {
            try
            {
                HttpSession session = request.getSession(true);
                session.invalidate();
                session = request.getSession(true);
                GenericResultBean resultBean = new GenericResultBean();
                logger.info("Error in performTask(): " + theException.toString());
                resultBean.setMessage("An error has occured, please try later\n\n\n\n<!--" + theException.toString() + "-->");
                session.setAttribute("resultBean", resultBean);
                callPage(response, "/error.jsp", request);
                return;
            }
            catch(Exception exception)
            {
                logger.error("Servlet exception handler throw exception:", exception);
            }
            return;
        }
        catch(Throwable e)
        {
            handleError(request, response, e);
        }
    }

    /**
     * Redirect exception to an error page.
     * @author  Nguyen Thai Son.
     * @version  10 April, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   errorPageURL String: the URL to the error page.
     * @param   e Throwable: a throwable storing exception.
     * @exception   IOException    If an input or output exception occurred.
     * @exception   ServletException    If a servlet exception occurred.
     */
    protected void sendErrorRedirect(
        HttpServletRequest request,
        HttpServletResponse response,
        String errorPageURL,
        Throwable e)
        throws ServletException, IOException {
        request.setAttribute("javax.servlet.jsp.jspException", e);
        getServletConfig()
            .getServletContext()
            .getRequestDispatcher(errorPageURL)
            .forward(request, response);
    }

    public void setNoCaching(HttpServletResponse res)
    {
        res.setHeader("Expires", "0");
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Cache-Control", "no-cache");
    }

    public void setRequestAttribute(String strAttrName, Object obj, HttpServletRequest request)
        throws ServletException, IOException
    {
        request.setAttribute(strAttrName, obj);
    }

    public BaseServlet()
    {
    }

    private static Logger logger;
    static Class class$fpt$dms$framework$core$DMSServlet;

    static
    {
        logger = Logger.getLogger(fpt.dms.framework.core.BaseServlet.class.getName());
    }
}
