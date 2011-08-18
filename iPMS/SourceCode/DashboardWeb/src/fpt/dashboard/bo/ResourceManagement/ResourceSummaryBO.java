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
 
 package fpt.dashboard.bo.ResourceManagement;

/**
 * @Title:        ResourceSummaryBO.java
 * @Description:
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  November 13, 2002
 */

import java.util.Collection;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Logger;

import fpt.dashboard.DeveloperManagementTran.ejb.Developer;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperEJBLocal;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperEJBLocalHome;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperHome;
import fpt.dashboard.ProjectManagementTran.ejb.Summary;
import fpt.dashboard.ProjectManagementTran.ejb.SummaryEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.SummaryEJBLocalHome;
import fpt.dashboard.ProjectManagementTran.ejb.SummaryHome;
import fpt.dashboard.ProjectManagementTran.ejb.SummaryInfo;
import fpt.dashboard.bean.ResourceManagement.ResourceSummaryBean;
import fpt.dashboard.constant.JNDINaming;
import fpt.dashboard.util.StringUtil.StringMatrix;
import fpt.dashboard.util.StringUtil.StringVector;

public class ResourceSummaryBO
{
//	private SummaryHome homeSummary = null;
//	private Summary objSummary = null;
//  private DeveloperHome developerHome = null;
//  private Developer developer = null;

// HaiMM ============
	private SummaryEJBLocalHome homeSummary = null;
	private SummaryEJBLocal objSummary = null;
	
	private DeveloperEJBLocalHome developerHome = null;
	private DeveloperEJBLocal developer = null;
//  =================
	private static Logger logger = Logger.getLogger(ResourceSummaryBO.class.getName());

	public ResourceSummaryBO()
	{

	}

	//**************** HELPER Methods ****************************************
	//************************Summary EJB***********************
	//EJB Bean Specific methods ...
	private void getEJBHome() throws NamingException
	{
		try
		{
			if (homeSummary == null)
			{
				Context ic = new InitialContext();
				Object ref = ic.lookup(JNDINaming.SUMMARY);
				homeSummary = (SummaryEJBLocalHome)(ref);
			}
            if (developerHome == null)
            {
                Context ic = new InitialContext();
                Object ref = ic.lookup(JNDINaming.DEVELOPER);
                developerHome = (DeveloperEJBLocalHome)(ref);
            }
		}
		catch (NamingException ex)
		{
			logger.error("NamingException occurs in ResourceSummaryBO.getEJBHome(). " + ex.getResolvedName());
			throw ex;
		}
	} //getEJBHome
	private SummaryEJBLocal getEJBRemote() throws Exception
	{
		try
		{
			objSummary = (SummaryEJBLocal)homeSummary.create();
			developer = (DeveloperEJBLocal)developerHome.create();
			return objSummary;
		}
		catch (Exception e)
		{
			logger.error(e.toString());
			return null;
		}
	} //getEJBRemote

	/**
	 * Get resource summary
	 * @author  Nguyen Thai Son.
	 * @version  November 13, 2002.
	 * @param	strNowMonth String: the current month
	 * @param	strNowYear String: the current year
	 * @exception   Exception    If an exception occurred.
	 */
	public ResourceSummaryBean getResourceSummary(String strNowMonth,
            String strNowYear, String strGroupType) throws Exception
	{
		ResourceSummaryBean beanResourceSummary = new ResourceSummaryBean();

		try
		{
			int nowMonth = 0;
			int nowYear = 0;
			int y = 0;
			if (strNowMonth == null || strNowMonth.equals(""))
			{
				java.util.Date now = new java.util.Date();
				nowMonth = now.getMonth() + 1;
				if (now.getYear() > 100)	nowYear = now.getYear() - 100;
				else									nowYear = now.getYear();
				y = now.getYear();
			}
			else
			{
				nowMonth = Integer.parseInt(strNowMonth);
				y = Integer.parseInt(strNowYear) - 1900;
				if (y > 100)	nowYear = y - 100;
				else				nowYear = y;
			}

			getEJBHome();
			getEJBRemote();
			/////////////////////////////////////////////////////////////////////////////

			int i = 0;
			String old_group = "";
			int old_type = 6;
            int type[] = { 0, 0, 0, 0, 0, 0, 0 };
            int summary[] = { 0, 0 };
            final int TOTAL=8;  // Number of assignment types + 1;
			int total = 0;
			int k = 0;

			Collection listResult = objSummary.getSummaryReport(nowMonth, nowYear, strGroupType);
            java.lang.Object[] arrResult = listResult.toArray();

            StringMatrix smResult = new StringMatrix();
			int nRow = 0;
			total = 0;
			for (int nIndex = 0; nIndex < arrResult.length; nIndex++)
			{
				SummaryInfo info = (SummaryInfo)arrResult[nIndex];
				if (!old_group.equals(info.getGroupName()))
				{
					if (!old_group.equals(""))
					{
						smResult.setCell(nRow - 1, TOTAL, String.valueOf(total));
						total = 0;
					}
					StringVector tmpVector = new StringVector(13);
					tmpVector.setCell(0, info.getGroupName());
					for (k = 1; k <= TOTAL; k++)
					{
						tmpVector.setCell(k, "0");
					}
					tmpVector.setCell(info.getType(), String.valueOf(info.getCount()));
					for (k = 1; k <= 2; k++)
					{
                        int developers = developer.countDev(info.getGroupName(), String.valueOf(k));
						tmpVector.setCell(k + TOTAL, String.valueOf(developers));
                        summary[k-1] += developers;
					}
					smResult.addRow(tmpVector);
					nRow++;
				}// end if
				else
				{
					smResult.setCell(nRow - 1, info.getType(), String.valueOf(info.getCount()));
				}
				old_group = info.getGroupName();
				old_type = info.getType();
				type[old_type - 1] += info.getCount();
				total += info.getCount();
			}// end for

			smResult.setCell(nRow - 1, TOTAL, String.valueOf(total));
			StringVector tmpVector = new StringVector(13);
			//tmpVector.setCell(0, "FSOFT");
            tmpVector.setCell(0, "All");
			tmpVector.setCell(1, String.valueOf(type[0]));
			tmpVector.setCell(2, String.valueOf(type[1]));
			tmpVector.setCell(3, String.valueOf(type[2]));
			tmpVector.setCell(4, String.valueOf(type[3]));
			tmpVector.setCell(5, String.valueOf(type[4]));
			tmpVector.setCell(6, String.valueOf(type[5]));
            tmpVector.setCell(7, String.valueOf(type[6]));
            tmpVector.setCell(8, String.valueOf(type[0] + type[1] + type[2] + type[3] + type[4] + type[5] + type[6]));
			for (k = 1; k <= 2; k++)
			{
                //tmpVector.setCell(k + TOTAL, String.valueOf(
                //      developer.countDev("All", String.valueOf(k))));
                tmpVector.setCell(k + TOTAL, String.valueOf(summary[k-1]));
			}
			smResult.addRow(tmpVector);

			beanResourceSummary.setSummaryList(smResult);
			beanResourceSummary.setNowMonth(String.valueOf(nowMonth));
			beanResourceSummary.setNowYear(String.valueOf(y + 1900));

		}// end try
		catch (NumberFormatException ex)
		{
			logger.debug("NumberFormatException occurs in ResourceSummaryBO.getResourceSummary().");
		}
		catch (javax.naming.NamingException e)
		{
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			logger.debug("Exception occurs in ResourceSummaryBO.getResourceSummary(): " + ex.toString());
			logger.error(ex);
		}

		////////////////////////////////////////////
		return beanResourceSummary;
	}
}