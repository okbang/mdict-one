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
 
 package fpt.dashboard.servlet;

/**
 * @Title:        ResourceManagementServlet.java
 * @Description:  Locate and forward the requests from the control pages.
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  October 23, 2002
 * @Modified date:
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import fpt.dashboard.bean.UserInfoBean;
import fpt.dashboard.bean.ResourceManagement.AssignmentDetailBean;
import fpt.dashboard.bean.ResourceManagement.ResourceDayBean;
import fpt.dashboard.bean.ResourceManagement.ResourceProjectBean;
import fpt.dashboard.bean.ResourceManagement.ResourceSummaryBean;
import fpt.dashboard.bean.ResourceManagement.ResourceWeekBean;
import fpt.dashboard.bo.ProjectManagememt.AssignmentBO;
import fpt.dashboard.bo.ResourceManagement.ResourceDayBO;
import fpt.dashboard.bo.ResourceManagement.ResourceProjectBO;
import fpt.dashboard.bo.ResourceManagement.ResourceSummaryBO;
import fpt.dashboard.bo.ResourceManagement.ResourceWeekBO;
import fpt.dashboard.constant.DATA;
import fpt.dashboard.constant.DB;
import fpt.dashboard.servlet.core.BaseServlet;
import fpt.dashboard.util.CommonUtil.CommonUtil;
//import fpt.dashboard.util.CommonUtil.Rsa;

public class ResourceManagementServlet  extends BaseServlet
{
    private static Logger logger = Logger.getLogger(ResourceManagementServlet.class.getName());
    /**
     * ResourceManagementServlet constructor.
     */
    public ResourceManagementServlet()

    {
    }
    /**
     * Process client request by action.
     * @author  Nguyen Thai Son
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        try
        {
            String strAction = request.getParameter("hidActionDetail");

            if (DB.VIEW_RESOURCE.equals(strAction))
            {
                int nResult = getResourceList(request, response);
                if (nResult == 1)   //External user
                    callPage(response, DB.JSP_RESOURCE_WEEK, request);
                else callPage(response, DB.JSP_RESOURCE_SUMMARY, request);
            }
            else if (DB.RESOURCE_WEEKLY.equals(strAction))
            {
                getResourceWeek(request, response);
                callPage(response, DB.JSP_RESOURCE_WEEK, request);
            }
            else if (DB.RESOURCE_DAILY.equals(strAction))
            {
                getResourceDay(request, response);
                callPage(response, DB.JSP_RESOURCE_DAY, request);
            }
            else if (DB.RESOURCE_PROJECT.equals(strAction))
            {
                getResourceProject(request, response);
                callPage(response, DB.JSP_RESOURCE_PROJECT, request);
            }
            else if (DB.VIEW_ASSIGNMENT_DETAIL.equals(strAction))
            {
                getAssignmentDetail(request, response);
                callPage(response, DB.JSP_ASSIGNMENT_DETAIL, request);
            }

            else
            {
                //sendErrorRedirect(request, response, DB.JSP_ERROR, new Exception("Invalid action: strAction = " + strAction));
                getLoginForm(request, response);
                callPage(response, DB.JSP_LOGIN, request);
            }
            return;
        }
        catch (Exception exception)
        {
            logger.debug("Exception in ResourceManagementServlet.performTask().");
            logger.error("ResourceManagementServlet - performTask(): ", exception);
            try
            {
                sendErrorRedirect(request, response, DB.JSP_ERROR, exception);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
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
    private void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e)
        throws ServletException, IOException
    {
        request.setAttribute("javax.servlet.jsp.jspException", e);
        getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
    }

    /**
     * Get a form to login
     * @author  Nguyen Thai Son
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    private void getLoginForm(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            session.invalidate();
            setNoCaching(response);
        }
        session = request.getSession(true);

//        Rsa rsa = new Rsa(160);
//        session.setAttribute("rsa", rsa);
        UserInfoBean beanUserInfo = new UserInfoBean();
        session.setAttribute("beanUserInfo", beanUserInfo);
    }

    /**
     * Reset session variables
     * @author  Nguyen Thai Son.
     * @version 24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   IOException    If an input or output exception occurred.
     * @exception   ServletException    If a servlet exception occurred.
     */
    private UserInfoBean checkSessionVariables(HttpServletRequest request, HttpServletResponse response) throws ServletException, Exception
    {
        UserInfoBean beanUserInfo = new UserInfoBean();
    
        try
        {
            HttpSession session = request.getSession();
            beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    
            if (beanUserInfo == null)
            {
                logger.debug("ResourceManagementServlet.checkSessionVariables(): Invalid session.");
    
                getLoginForm(request, response);
                //display result page
                callPage(response, DB.JSP_LOGIN, request);
            }
        }
        catch (Exception exception)
        {
            logger.error("Exception in checkSessionVariables().", exception);
            sendErrorRedirect(request, response, DB.JSP_ERROR, exception);
        }
    
        return beanUserInfo;
    }
    
    /**
     * getResourceList
     * @author  Nguyen Thai Son
     * @version November 13, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    private int getResourceList(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        int nResult = -1;

        UserInfoBean beanUserInfo = checkSessionVariables(request, response);

        // User is external at group level or project level
        if ((Integer.toString(DATA.LOGIN_EXTERNAL_GL).equals(beanUserInfo.getRole()))
                || (Integer.toString(DATA.LOGIN_EXTERNAL_PL).equals(beanUserInfo.getRole())))
        {
            getResourceWeek(request, response);
            nResult = 1;
        }
        else
        {
            String strNowMonth = CommonUtil.getParameter(request, "month","month"); // request.getParameter("month");
            String strNowYear = CommonUtil.getParameter(request, "year","year");  //request.getParameter("year");
            String strGroupType = CommonUtil.getParameter(request, "cboGroupType","cboGroupType");//request.getParameter("cboGroupType");
            
            ResourceSummaryBean beanResourceSummary = new ResourceSummaryBean();
            ResourceSummaryBO boSummary = new ResourceSummaryBO();
            beanResourceSummary = boSummary.getResourceSummary(strNowMonth, strNowYear, strGroupType);
            request.setAttribute("beanResourceSummary", beanResourceSummary);
        }

        return nResult;
    }

    /**
     * Get a weekly resource report
     * @author  Nguyen Thai Son
     * @version November 13, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    private void getResourceWeek(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        UserInfoBean beanUserInfo = checkSessionVariables(request, response);

        String strGroup = request.getParameter("hidGroup");
        String strMonth = request.getParameter("Month");
        String strYear = request.getParameter("Year");
        String strRange = request.getParameter("range");
        if ((strGroup == null) || ("".equals(strGroup))) {
            strGroup = request.getParameter("cboGroup");
        }

        // User is external at group level or project level
        if (Integer.toString(DATA.LOGIN_EXTERNAL_GL).equals(beanUserInfo.getRole())
                || Integer.toString(DATA.LOGIN_EXTERNAL_PL).equals(beanUserInfo.getRole()))
            strGroup = beanUserInfo.getGroupName();

        ResourceWeekBean beanResourceWeek = new ResourceWeekBean();
        ResourceWeekBO boWeek = new ResourceWeekBO();

        beanResourceWeek.setMonth(strMonth);
        beanResourceWeek.setYear(strYear);
        beanResourceWeek.setRang(strRange);
        beanResourceWeek.setCondi(strGroup);
        beanResourceWeek = boWeek.getWeeklyResource(strGroup, beanResourceWeek);

        request.setAttribute("beanResourceWeek", beanResourceWeek);
    }

    /**
     * Get a daily resource report
     * @author  Nguyen Thai Son
     * @version November 13, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    private void getResourceDay(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        UserInfoBean beanUserInfo = checkSessionVariables(request, response);

        String strMonth = request.getParameter("dMonth");
        String strYear = request.getParameter("Year");
        String strGroup = request.getParameter("cboGroup");

        // User is external at group level or project level
        if (Integer.toString(DATA.LOGIN_EXTERNAL_GL).equals(beanUserInfo.getRole())
                || Integer.toString(DATA.LOGIN_EXTERNAL_PL).equals(beanUserInfo.getRole()))
            strGroup = beanUserInfo.getGroupName();

        ResourceDayBean beanResourceDay = new ResourceDayBean();
        ResourceDayBO boDay = new ResourceDayBO();

        beanResourceDay.setMonth(strMonth);
        beanResourceDay.setYear(strYear);
        beanResourceDay.setCondi(strGroup);
        beanResourceDay = boDay.getDailyResource(beanResourceDay);

        request.setAttribute("beanResourceDay", beanResourceDay);
    }

    /**
     * Get a project resource report weekly
     * @author  Tu Ngoc Trung
     * @version April 02, 2004
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    private void getResourceProject(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        UserInfoBean beanUserInfo = checkSessionVariables(request, response);
        beanUserInfo.setTypeOfView(request.getParameter("hidActionDetail"));
        HttpSession session = request.getSession();
        session.setAttribute("beanUserInfo", beanUserInfo);
        
        ResourceProjectBean beanResourceProject = new ResourceProjectBean();
        if (request.getParameter("Month") != null) {
            beanResourceProject.setFromMonth(Integer.parseInt(request.getParameter("Month")));
        }
        if (request.getParameter("Year") != null) {
            beanResourceProject.setFromYear(Integer.parseInt(request.getParameter("Year")));
        }
        if (request.getParameter("bMonth") != null) {
            beanResourceProject.setToMonth(Integer.parseInt(request.getParameter("bMonth")));
        }
        if (request.getParameter("bYear") != null) {
            beanResourceProject.setToYear(Integer.parseInt(request.getParameter("bYear")));
        }
        
        // User is external at group level or project level
        if (Integer.toString(DATA.LOGIN_EXTERNAL_GL).equals(beanUserInfo.getRole()) ||
            Integer.toString(DATA.LOGIN_EXTERNAL_PL).equals(beanUserInfo.getRole()))
        {
            beanResourceProject.setGroupName(beanUserInfo.getGroupName());
        }
        else if (request.getParameter("cboGroup") != null) {
            beanResourceProject.setGroupName(request.getParameter("cboGroup"));
            session.setAttribute("cboGroup", request.getParameter("cboGroup"));
        }
        else if (session.getAttribute("cboGroup") != null) {
            beanResourceProject.setGroupName((String) session.getAttribute("cboGroup"));
        }

        beanResourceProject.genWeeksList();
//        Calendar cal;
//        CalendarUtil calUtil = new CalendarUtil();
//        for (int i = 0; i < beanResourceProject.getWeeksList().size(); i++) {
//            cal = (Calendar) beanResourceProject.getWeeksList().get(i);
//            System.out.println("Date(" + i + "):" + calUtil.format(cal));
//        }
        
        ResourceProjectBO boProject = new ResourceProjectBO();
        beanResourceProject = boProject.getProjectResource(beanResourceProject);
        request.setAttribute("beanResourceProject", beanResourceProject);
    }

    /**
     * Get assignment detail
     * @author  Nguyen Thai Son
     * @version November 13, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    private void getAssignmentDetail(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        checkSessionVariables(request, response);

        AssignmentDetailBean beanAssignmentDetail = new AssignmentDetailBean();

        beanAssignmentDetail.setDeveloperID(request.getParameter("hidAssignmentID"));
        beanAssignmentDetail.setFrom(request.getParameter("from"));
        beanAssignmentDetail.setTo(request.getParameter("to"));

        AssignmentBO boAssignment = new AssignmentBO();
        beanAssignmentDetail = boAssignment.getAssignmentDetail(beanAssignmentDetail);

        /////////////////////////////////////////////////
        request.setAttribute("beanAssignmentDetail", beanAssignmentDetail);
    }
}