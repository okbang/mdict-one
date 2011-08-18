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
 * @Title:        ResourceDayBO.java
 * @Description:  
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  November 13, 2002
 */

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import javax.mail.Provider.Type;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.log4j.Logger;

import fpt.dashboard.DeveloperManagementTran.ejb.Developer;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperEJBLocal;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperEJBLocalHome;
import fpt.dashboard.DeveloperManagementTran.ejb.DeveloperHome;
import fpt.dashboard.ProjectManagementTran.ejb.Assign;
import fpt.dashboard.ProjectManagementTran.ejb.AssignEJBLocal;
import fpt.dashboard.ProjectManagementTran.ejb.AssignEJBLocalHome;
import fpt.dashboard.ProjectManagementTran.ejb.AssignHome;
import fpt.dashboard.ProjectManagementTran.ejb.Constants;
import fpt.dashboard.bean.ResourceManagement.ResourceDayBean;
import fpt.dashboard.constant.JNDINaming;
import fpt.dashboard.util.DateUtil.DateUtil;

public class ResourceDayBO {
	private static Logger logger =
		Logger.getLogger(ResourceDayBO.class.getName());

//	private AssignHome assignHome = null;
//	private Assign assign = null;
//	private DeveloperHome developerHome = null;
//	private Developer developer = null;

//  HaiMM =========
	private AssignEJBLocalHome assignHome = null;
	private AssignEJBLocal assign = null;
	private DeveloperEJBLocalHome developerHome = null;
	private DeveloperEJBLocal developer = null;
//  ================

	///////////////////////////////////////////////////////////////

	private String msg = "";
	private Integer projectID;
	private Integer developerID;
	private Integer type;
	private String projectName;
	private String developerName;
	private String begin;
	private String end;
	private String desc;
	private String strID;
	private String from = "01/07/2001";
	private String to = "31/12/2001";
	private String condi = "All";
	int range = 5;
	private int count = 0;
	private int month = 0;
	private int day = 0;
	private int year = 0;
	private int countA = 0;
	private int countOA = 0;
	private int maxA = 0;
	private int maxOA = 0;
	private int max = 0;
	private int usage = 0;
	private Integer[] AssignID = null;
	private Integer[] OtherAssignID = null;
	private Integer[] DeveloperID = null;
	private Integer[] FreeDeveloperID = null;
	private Integer[] BussyDeveloperID = null;
	private Vector tooltip = new Vector();
	private int[] bussyTable = null;
	private boolean flagOther = false;
	private Calendar calendar = Calendar.getInstance();

	////////////////////////////////////////////////////////////////

	public ResourceDayBO() {

	}

	//**************** HELPER Methods ****************************************
	//************************Summary EJB***********************
	//EJB Bean Specific methods ...
	private void getEJBRemote() throws NamingException {
		try {
			if (assignHome == null) {
				Context ic = new InitialContext();
				Object ref = ic.lookup(JNDINaming.ASSIGNMENT);
				assignHome =(AssignEJBLocalHome)(ref);
				assign = (AssignEJBLocal) assignHome.create();
			}
			if (developerHome == null) {
				Context ic = new InitialContext();
				Object ref = ic.lookup(JNDINaming.DEVELOPER);
				developerHome =(DeveloperEJBLocalHome)(ref);
				developer = (DeveloperEJBLocal) developerHome.create();
			}
		} catch (NamingException ex) {
			logger.debug(
				"NamingException occurs in ResourceDayBO.getEJBHome(). "
					+ ex.getResolvedName());
			throw ex;
		} catch (Exception e) {
			logger.debug(e.toString());
			e.printStackTrace();
		}
	} //getEJBRemote

	/**
	 * Get daily resource
	 * @author  Nguyen Thai Son.
	 * @version  November 13, 2002.
	 * @param	beanResourceDay ResourceDayBean.
	 * @exception   Exception    If an exception occurred.
	 */
	public ResourceDayBean getDailyResource(ResourceDayBean beanResourceDay)
		throws Exception {
		try {
			if (beanResourceDay.getMonth() != null) {
				month = Integer.parseInt(beanResourceDay.getMonth()) - 1;
				year = Integer.parseInt(beanResourceDay.getYear()) - 1900;
			} else {
				Date now = new Date();
				month = now.getMonth();
				year = now.getYear();
				beanResourceDay.setMonth(String.valueOf(month + 1));
				beanResourceDay.setYear(String.valueOf(year + 1900));
			}
//            if ((beanResourceDay.getCondi() != null)
//                && (!beanResourceDay.getCondi().equals("FSOFT")))
            if (beanResourceDay.getCondi() != null) {
                condi = beanResourceDay.getCondi();
            }
			else {
                condi = "All";
			}
			beanResourceDay.setCondi(condi);

			int numDay = DateUtil.getNumDate(month + 1, year + 1900);
			from =
				"01/"
					+ String.valueOf(month + 1)
					+ "/"
					+ String.valueOf(year + 1900);
			to =
				String.valueOf(numDay)
					+ "/"
					+ String.valueOf(month + 1)
					+ "/"
					+ String.valueOf(year + 1900);

			getEJBRemote();
			/////////////////////////////////////////////////////////////////////////////

			Collection cGroup = developer.selectGroup();
			Object[] oGroup = cGroup.toArray();
			String[] Group = new String[oGroup.length + 1];
			Group[0] = Constants.GROUP_ALL;
			for (int i = 0; i < oGroup.length; i++) {
				String iTemp = (String) oGroup[i];
				Group[i + 1] = iTemp;
			}
			beanResourceDay.setArrGroup(Group);
			int tmp = 0;
			int j = 0;
			int tmonth = month;
			tmp = numDay;
			calendar.set(year + 1900, month, 1);
			int sT = calendar.get(Calendar.DAY_OF_WEEK);
			int eT = (tmp - (8 - sT)) % 7;
			//get bussy devid
			loadBussyDev();
			Vector BusyTable = new Vector();
			Vector BusyDevID = new Vector();
			Vector BusyDevName = new Vector();
			Vector Tooltip = new Vector();
			String strDevID = "";
			while (nextBussyDev()) {
				strDevID = strDevID + getDeveloperID() + ",";
				loadAssign();
				int[] fillTable = new int[2 * numDay];
				fillTable = genTable(month + 1, year);
				BusyTable.add(fillTable);
				BusyDevID.add(getDeveloperID());
				BusyDevName.add(getDeveloperName());
				Vector tmpTooltip = new Vector();
				tmpTooltip = getTooltip();
				String[] strTooltip = new String[tmpTooltip.size()];
				for (int i = 0; i < tmpTooltip.size(); i++) {
					strTooltip[i] = (String) tmpTooltip.elementAt(i);
				}
				Tooltip.add(strTooltip);
			}
			beanResourceDay.setBusyDevID(BusyDevID);
			beanResourceDay.setBusyDevName(BusyDevName);
			beanResourceDay.setBusyTable(BusyTable);
			beanResourceDay.setTooltip(Tooltip);
			if (strDevID != "") {
				strDevID = strDevID.substring(0, strDevID.length() - 1);
				setStrID(strDevID);
				loadFreeDev();
			} else {
				strDevID = "";
				setStrID(strDevID);
				loadFreeDev();
			}
			Vector FreeDevID = new Vector();
			Vector FreeDevName = new Vector();
			while (nextBussyDev()) { //while1
				FreeDevID.add(getDeveloperID());
				FreeDevName.add(getDeveloperName());
			}
			beanResourceDay.setFreeDevID(FreeDevID);
			beanResourceDay.setFreeDevName(FreeDevName);

		} // end try
		catch (NumberFormatException ex) {
			logger.debug(
				"NumberFormatException occurs in ResourceDayBO.getDailyResource().");
		} catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		} catch (Exception ex) {
			logger.debug(
				"Exception occurs in ResourceDayBO.getDailyResource(): "
					+ ex.toString());
			logger.error(ex);
		}

		////////////////////////////////////////////
		return beanResourceDay;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	* method: public void loadBussyDev() throws RemoteException, SQLException
	* modified: Nguyen Thai Son - Thursday, August 15, 2002
	* description: Add try-catch block and throw exception(s)
	*/
	public void loadBussyDev() throws RemoteException, SQLException {
		int i = 0;
		try {
			Collection cAllKey = assign.selectBussyDeveloper(from, to, condi);
			Object[] oAllKey = cAllKey.toArray();
			BussyDeveloperID = new Integer[oAllKey.length];
			max = oAllKey.length;
			count = 0;
			for (i = 0; i < oAllKey.length; i++) {
				Integer iTemp = (Integer) oAllKey[i];
				BussyDeveloperID[i] = iTemp;
			}
		} catch (SQLException ex) {
			throw ex;
		}
	}

	/**
		* method: public void loadFreeDev() throws RemoteException, SQLException
		* modified: Nguyen Thai Son - Thursday, August 15, 2002
		* description: Add try-catch block and throw exception(s)
	*/
	public void loadFreeDev() throws RemoteException, SQLException {
		int i = 0;
		try {
			Collection cAllKey = assign.selectFreeDeveloper(strID, condi);
			Object[] oAllKey = cAllKey.toArray();
			BussyDeveloperID = new Integer[oAllKey.length];
			max = oAllKey.length;
			count = 0;
			for (i = 0; i < oAllKey.length; i++) {
				Integer iTemp = (Integer) oAllKey[i];
				BussyDeveloperID[i] = iTemp;
			}
		} catch (SQLException ex) {
			throw ex;
		}
	}

	/**
		* method: public void loadAllDev() throws RemoteException, SQLException
		* modified: Nguyen Thai Son - Thursday, August 15, 2002
		* description: Add try-catch block and throw exception(s)
	*/
	public void loadAllDev() throws RemoteException, SQLException {
		int i = 0;
		try {
			Collection cAllKey = developer.selectAllKey("All", Constants.ALL, null, null);
			Object[] oAllKey = cAllKey.toArray();
			BussyDeveloperID = new Integer[oAllKey.length];
			max = oAllKey.length;
			count = 0;
			for (i = 0; i < oAllKey.length; i++) {
				Integer iTemp = (Integer) oAllKey[i];
				BussyDeveloperID[i] = iTemp;
			}
		} catch (SQLException ex) {
			throw ex;
		}
	}

	/**
		* method: public boolean nextBussyDev() throws RemoteException, SQLException
		* modified: Nguyen Thai Son - Thursday, August 15, 2002
		* description: Add try-catch block and throw exception(s)
	*/
	public boolean nextBussyDev() throws RemoteException, SQLException {
		try {
			if ((max < 0) || (count == max)) {
				return false;
			} else {
				developer.loadRow(BussyDeveloperID[count]);
				loadDevFromEJB();
				this.developerID = BussyDeveloperID[count];
				count = count + 1;
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return true;
	}

	/**
		* method: public void loadAssign() throws RemoteException, SQLException
		* modified: Nguyen Thai Son - Thursday, August 15, 2002
		* description: Add try-catch block and throw exception(s)
	*/
	public void loadAssign() throws RemoteException, SQLException {
		try {
			int i = 0;
			flagOther = false;
			assign.setDeveloperID(this.developerID);
			Collection cAllKey = assign.selectByDeveloper();
			Object[] oAllKey = cAllKey.toArray();
			AssignID = new Integer[oAllKey.length];
			maxA = oAllKey.length;
			countA = 0;
			for (i = 0; i < oAllKey.length; i++) {
				Integer iTemp = (Integer) oAllKey[i];
				AssignID[i] = iTemp;
			}
			Collection cAllKey2 = assign.selectOtherByDeveloper();
			Object[] oAllKey2 = cAllKey2.toArray();
			OtherAssignID = new Integer[oAllKey2.length];
			maxOA = oAllKey2.length;
			countOA = 0;
			for (i = 0; i < oAllKey2.length; i++) {
				Integer iTemp = (Integer) oAllKey2[i];
				OtherAssignID[i] = iTemp;
			}
		} catch (SQLException ex) {
			throw ex;
		}
	}

	/**
		* method: public boolean nextAssign() throws RemoteException, SQLException
		* modified: Nguyen Thai Son - Thursday, August 15, 2002
		* description: Add try-catch block and throw exception(s)
	*/
	public boolean nextAssign() throws RemoteException, SQLException {
		try {
			if ((maxA < 0) || (countA == maxA)) {
				flagOther = true;
				if ((maxOA < 0) || (countOA == maxOA)) {
					return false;
				} else {
					assign.loadOtherRow(OtherAssignID[countOA]);
					loadFromEJB();
					countOA = countOA + 1;
				}
			} else {
				assign.loadRow(AssignID[countA]);
				assign.getProjectInfor(assign.getProjectID());
				loadFromEJB();
				countA = countA + 1;
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return true;
	}
	private void reset() {
		final String emptyString = "";
		final String zero = "0";
	}
	public String[] getGroupName() throws RemoteException, SQLException {
		int i = 0;
		String[] temp = null;
		try {
			Collection cAllKey = developer.selectGroup();
			Object[] oAllKey = cAllKey.toArray();
			temp = new String[oAllKey.length];
			for (i = 0; i < oAllKey.length; i++) {
				String iTemp = (String) oAllKey[i];
				temp[i] = iTemp;
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return temp;
	}
	private void loadDevFromEJB() throws RemoteException {
		try {
			setDeveloperName(developer.getName());
		} catch (Exception re) {
			System.err.println(
				"Failed to load DeveloperBean from DeveloperEJB.");
			re.printStackTrace();
		}
	}

	/**
		* method: public void loadFromEJB() throws RemoteException
		* modified: Nguyen Thai Son - Thursday, August 15, 2002
		* description: Add try-catch block and throw exception(s)
	*/
	public void loadFromEJB() throws RemoteException {
		try {
			if (assign.getProjectID() != null) {
				setProjectID(assign.getProjectID().toString());
			}
			setProjectName(assign.getProjectName());
			setBegin(assign.getBegin());
			setEnd(assign.getEnd());
			if (assign.getType() != null) {
				setType(assign.getType().toString());
			}
			if (assign.getDesc() != null) {
				setDesc(assign.getDesc());
			} else {
				setDesc("");
			}
			this.usage = assign.getUsage();
		} catch (Exception re) {
			System.err.println("Failed to load ResBean from AssignEJB.");
			re.printStackTrace();
		}
	}
	public String toString() {
		StringBuffer output = new StringBuffer();
		return output.toString();
	}

	/**
		* method: public int[] genTable(int Month, int Year) throws RemoteException, SQLException
		* modified: Nguyen Thai Son - Thursday, August 15, 2002
		* description: Add try-catch block and throw exception(s)
	*/
	public int[] genTable(int Month, int Year)
		throws RemoteException, SQLException {
		int StartLoop = 0, EndLoop = 0, numAssign = 0;
		int Cassign = 1;
		int numDate = DateUtil.getNumDate(Month, Year + 1900);
		int[] bussyTable1 = null;
		bussyTable1 = new int[2 * numDate];
		int[] tempTable = null;
		tempTable = new int[2 * numDate];
		for (int i = 0; i < numDate; i++) {
			bussyTable1[i] = 6;
			bussyTable1[numDate + i] = 0;
			tempTable[i] = 0;
		}
		try {
			int nowStartDate = 1;
			int nowEndDate = DateUtil.getNumDate(Month, Year);
			tooltip.removeAllElements();
			while (nextAssign()) {
				int assignStartDate = DateUtil.getDateFromString(begin);
				int assignStartMonth = DateUtil.getMonthFromString(begin);
				int assignStartYear = DateUtil.getYearFromString(begin) + 100;
				int assignEndDate = DateUtil.getDateFromString(end);
				int assignEndMonth = DateUtil.getMonthFromString(end);
				int assignEndYear = DateUtil.getYearFromString(end) + 100;
				boolean flag = true;
				if (Year > assignStartYear) {
					StartLoop = 0;
				} else {
					if (Year == assignStartYear) {
						if (assignStartMonth == Month) {
							StartLoop = assignStartDate - 1;
						} else {
							if (assignStartMonth < Month) {
								StartLoop = 0;
							} else {
								flag = false;
							}
						}
					} else {
						flag = false;
					}
				}
				if (Year < assignEndYear) {
					EndLoop = numDate;
				} else {
					if (Year == assignEndYear) {
						if (assignEndMonth == Month) {
							EndLoop = assignEndDate;
						} else {
							if (assignEndMonth > Month) {
								EndLoop = numDate;
							} else {
								flag = false;
							}
						}
					} else {
						flag = false;
					}
				}
				if (flag) {
					String tmpTip = new String();
					String fname = new String();
					switch (this.type.intValue()) {
						case 1 :
							fname = "Onsite";
							break;
						case 2 :
							fname = "Allocated";
							break;
						case 3 :
							fname = "Tentative";
							break;
						case 4 :
							fname = "Training";
							break;
						case 5 :
							fname = "Off";
							break;
					}
					tmpTip = "Type: " + fname + " - ";
					if (flagOther) {
						tmpTip = tmpTip + " Task:" + this.desc + " - ";
					} else {
						tmpTip =
							tmpTip + " Project:" + this.projectName + " - ";
					}
					tmpTip = tmpTip + " From:" + this.begin + " - ";
					tmpTip = tmpTip + " To:" + this.end;
					tmpTip = tmpTip + " Usage:" + this.usage + "% ";
					for (int i = StartLoop; i < EndLoop; i++) {
						//Lamnt3
						//if(!flagOther){
							tempTable[i] += this.usage;
						//}
						//End of Lamnt3
						if (bussyTable1[i] == 6) {
							bussyTable1[i] = this.type.intValue();
							bussyTable1[numDate + i] = Cassign;
						} else {
							numAssign = bussyTable1[numDate + i];
							if (bussyTable1[i] > this.type.intValue()) {
								bussyTable1[i] = this.type.intValue();
							}
							//bussyTable1[i]=0;
							if (numAssign < 10) {
								bussyTable1[numDate + i] =
									Cassign * 10 + numAssign;
							}
							if (numAssign > 10) {
								bussyTable1[numDate + i] =
									Cassign * 100 + numAssign;
							}
						}
					}
					tooltip.add(tmpTip.replace('\n', ' '));
					Cassign++;
				}
			}
		} catch (SQLException ex) {
			throw ex;
		} //ThaiLH
		for (int i = 0; i < numDate; i++) {
			if (tempTable[i] > 100) {
				bussyTable1[i] = -1;
			}
		}
		return bussyTable1;
	}

	/**
		* method: public int[] genMonthTable1(int Month, int Year, int nWeek, int Range2)		throws RemoteException, SQLException
		* modified: Nguyen Thai Son - Thursday, August 15, 2002
		* description: Add try-catch block and throw exception(s)
	*/
	/*
		-Range: number of date
		-Range2: number of month
		-Month=1-12;
	*/
	public int[] genMonthTable1(int Month, int Year, int nWeek, int Range2)
		throws RemoteException, SQLException {
		/*
			Month : start month
			Year : start year
			nWeek : number of week.
			Range2 : number of month
		*/
		int StartLoop = 0, EndLoop = 0;
		int Cassign = 1;
		int sWeek = 0, eWeek = 0, lnumAssign = -1;
		boolean flagCa;
		int numAssign;
		Calendar calendar = Calendar.getInstance();
		int numWeek = nWeek;
		bussyTable = new int[2 * numWeek];
		for (int i = 0; i < numWeek; i++) {
			bussyTable[i] = 6;
		}
		int nowStartDate = 1;
		int tYear = Year;
		if (Month + Range2 > 12) {
			tYear = Year + 1;
		}
		int rMonth = Month + Range2;
		if (Month + Range2 > 12) {
			rMonth = Month + Range2 - 12;
		}
		int nowEndDate = DateUtil.getNumDate(rMonth, tYear + 1900);
		calendar.set(Year, Month - 1, 1);
		int nowWeek = calendar.get(Calendar.WEEK_OF_YEAR) - 1;
		int nowEndWeek = nowWeek + numWeek;
		try {
			Date nowStart = new Date(Year, Month - 1, 1);
			Date nowEnd = new Date(tYear, rMonth - 1, nowEndDate);
			tooltip.removeAllElements();
			int eYWeek = 0;
			while (nextAssign()) {
				int assignStartDate = DateUtil.getDateFromString(begin);
				int assignStartMonth = DateUtil.getMonthFromString(begin);
				int assignStartYear = DateUtil.getYearFromString(begin) + 100;
				Date tAsStartDate =
					new Date(
						assignStartYear,
						assignStartMonth - 1,
						assignStartDate);
				sWeek = assign.getStartWeek().intValue();
				int assignEndDate = DateUtil.getDateFromString(end);
				int assignEndMonth = DateUtil.getMonthFromString(end);
				int assignEndYear = DateUtil.getYearFromString(end) + 100;
				Date tAsEndDate =
					new Date(assignEndYear, assignEndMonth - 1, assignEndDate);
				eWeek = assign.getEndWeek().intValue();
				if (eWeek < sWeek) {
					if ((assignEndMonth == 12) || (assignEndYear > Year)) {
						eWeek = eWeek + 52;
					}
				}
				boolean flag = true;
				if (tAsStartDate.compareTo(nowStart) <= 0) {
					StartLoop = 0;
				} else {
					if (tAsStartDate.compareTo(nowEnd) <= 0) {
						StartLoop = sWeek - nowWeek;
					} else {
						flag = false;
					}
				}
				if (tAsEndDate.compareTo(nowEnd) >= 0) {
					EndLoop = numWeek;
				} else {
					if (tAsEndDate.compareTo(nowStart) >= 0) {
						if (assignEndYear == Year) {
							EndLoop = eWeek - nowWeek + 1;
						} else {
							EndLoop = eWeek + (52 - nowWeek);
						}
					} else {
						flag = false;
					}
				}
				if (flag) {
					//create tip
					String tmpTip = new String();
					String fname = new String();
					switch (this.type.intValue()) {
						case 1 :
							fname = "Onsite";
							break;
						case 2 :
							fname = "Allocated";
							break;
						case 3 :
							fname = "Tentative";
							break;
						case 4 :
							fname = "Training";
							break;
						case 5 :
							fname = "Off";
							break;
					}
					tmpTip = "Type: " + fname + " - ";
					if (flagOther) {
						tmpTip = tmpTip + " Task:" + this.desc + " - ";
					} else {
						tmpTip =
							tmpTip + " Project:" + this.projectName + " - ";
					}
					tmpTip = tmpTip + " From:" + this.begin + " - ";
					tmpTip = tmpTip + " To:" + this.end;
					flagCa = false;
					for (int i = StartLoop; i < EndLoop; i++) {
						if (bussyTable[i] == 6) {
							bussyTable[i] = this.type.intValue();
							bussyTable[numWeek + i] = Cassign;
						} else {
							numAssign = bussyTable[numWeek + i];
							bussyTable[i] = 0;
							if (numAssign < 10) {
								bussyTable[numWeek + i] =
									Cassign * 10 + numAssign;
							}
							if (numAssign > 10) {
								bussyTable[numWeek + i] =
									Cassign * 100 + numAssign;
							}
						}
					}
					tooltip.add(tmpTip.replace('\n', ' '));
					Cassign++;
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return bussyTable;
	}
	public Integer[] getOther() {
		return OtherAssignID;
	}
	public Vector getTooltip() {
		return tooltip;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public Integer getType() {
		return type;
	}
	public String getProjectName() {
		return projectName;
	}
	public Integer getDeveloperID() {
		return developerID;
	}
	public String getDeveloperName() {
		return developerName;
	}
	public String getBegin() {
		return begin;
	}
	public String getEnd() {
		return end;
	}
	public String getCondi() {
		return condi;
	}
	public void setCondi(String str) {
		this.condi = str;
	}
	public void setProjectID(String ID) {
		Integer intTemp = new Integer(ID);
		this.projectID = intTemp;
	}
	public void setType(String Type) {
		Integer intTemp = new Integer(Type);
		this.type = intTemp;
	}
	public void setProjectName(String Name) {
		this.projectName = Name;
	}
	public void setDeveloperID(String ID) {
		Integer intTemp = new Integer(ID);
		this.developerID = intTemp;
	}
	public void setDeveloperName(String Name) {
		this.developerName = Name;
	}
	public void setBegin(String Begin) {
		this.begin = Begin;
	}
	public void setEnd(String End) {
		this.end = End;
	}
	public void setFrom(String End) {
		this.from = End;
	}
	public void setTo(String End) {
		this.to = End;
	}
	public void setStrID(String End) {
		this.strID = End;
	}
	public void setDesc(String Desc) {
		this.desc = Desc.replace('\n', ' ');
		this.desc = this.desc.replace('\r', ' ');
	}
	public int getMax() {
		return max;
	}
}