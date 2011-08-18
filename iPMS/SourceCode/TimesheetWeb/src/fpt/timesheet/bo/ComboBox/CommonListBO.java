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
 
 package fpt.timesheet.bo.ComboBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;

import fpt.timesheet.ApproveTran.ejb.common.CommonListEJBLocal;
import fpt.timesheet.ApproveTran.ejb.common.CommonListEJBLocalHome;
//import fpt.timesheet.ApproveTran.ejb.common.CommonListHome;
import fpt.timesheet.ApproveTran.ejb.common.CommonListModel;
//import fpt.timesheet.ApproveTran.ejb.common.CommonListRemote;
import fpt.timesheet.constant.JNDI;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;

public class CommonListBO {
	private static Logger logger = Logger.getLogger(CommonListBO.class.getName());
	
	private static ArrayList DevList;
    private static ArrayList PQANameList;
    private static ArrayList WorkTypeList;
    private static ArrayList ProductList;
    private static ArrayList ProcessList;
    private static ArrayList KpaList;
    private static ArrayList GroupList;

	private CommonListEJBLocalHome objHome;   
	private CommonListEJBLocal objRemote;

	/**
	 * Method getCommonListEJB.
	 * @throws NamingException
	 */
	private void getCommonListEJB() throws NamingException {
		try {
			if (objHome == null) {
				Context ic = new InitialContext();
				java.lang.Object objref = ic.lookup(JNDI.COMMON_LIST);
//				objHome = (CommonListHome) PortableRemoteObject.narrow(objref, CommonListHome.class);
				objHome = (CommonListEJBLocalHome)(objref);
				if (objRemote == null) {
					objRemote = objHome.create();
				}
			}
		}
		catch (NamingException ex) {
			logger.debug("CommonListBO.getCommonListEJB() error! -- " + ex.getMessage() + "---" + ex.getResolvedName());
			throw ex;
		}
		catch (Exception ex) {
			logger.debug("CommonListBO.getCommonListEJB() error! -- " + ex.getMessage());
			ex.printStackTrace();
		}
	}

    /**
	 * Method CommonListBO()
	 */
	public CommonListBO() {

        try {
			getCommonListEJB();

            PQANameList = new ArrayList();
            WorkTypeList = new ArrayList();
            ProductList = new ArrayList();
            ProcessList = new ArrayList();
            KpaList = new ArrayList();
            GroupList = new ArrayList();

            Collection commonList = objRemote.getCommonList();
            if (commonList == null || commonList.isEmpty()) {
            }
            Iterator it = commonList.iterator();
            String strKey = "";
            while (it.hasNext()) {
                CommonListModel clmData = (CommonListModel) it.next();
				strKey = clmData.getKey();

				if (strKey.equalsIgnoreCase("PQA")) {
					PQANameList.add(clmData);
				}
                else if (strKey.equalsIgnoreCase("WorkType")) {
                    WorkTypeList.add(clmData);
                }
                else if (strKey.equalsIgnoreCase("Product")) {
                    ProductList.add(clmData);
                }
                else if (strKey.equalsIgnoreCase("Process")) {
                    ProcessList.add(clmData);
                }
                else if (strKey.equalsIgnoreCase("Kpa")) {
                    KpaList.add(clmData);
                }
                else if (strKey.equalsIgnoreCase("Group")) {
                    GroupList.add(clmData);
                }
            }
            // release resource
            objRemote = null;
        }
		catch (javax.naming.NamingException e) {
			logger.debug("Couldn't locate " + e.getResolvedName());
			e.printStackTrace();
		}
		catch (Exception ex) {
			logger.debug("Exception occurs in CommonListBO(): " + ex.toString());
			logger.error(ex);
		}
    }

    /**
     * Method ConvertToStrMatrix
	 * @param arrList
	 * @return mtxList
	 */
	private StringMatrix ConvertToStrMatrix(ArrayList arrList) {
        int nSize = arrList.size();
        StringMatrix mtxList = new StringMatrix(nSize, 2);
        Iterator it = arrList.iterator();

        int i = 0;
        while (it.hasNext()) {
            CommonListModel clmData = (CommonListModel) it.next();
            mtxList.setCell(i, 0, clmData.getCode());
            mtxList.setCell(i, 1, clmData.getName());
            i++;
        }
        return mtxList;
    }

	public StringMatrix getPQANameList() {
		return ConvertToStrMatrix(PQANameList);
	}

    public StringMatrix getTypeOfWorkList() {
        return ConvertToStrMatrix(WorkTypeList);
    }

    public StringMatrix getProductList() {
        return ConvertToStrMatrix(ProductList);
    }

    public StringMatrix getProcessList() {
        return ConvertToStrMatrix(ProcessList);
    }

    public StringMatrix getKpaList() {
        return ConvertToStrMatrix(KpaList);
    }

    public StringMatrix getGroupList() {
        return ConvertToStrMatrix(GroupList);
    }

}