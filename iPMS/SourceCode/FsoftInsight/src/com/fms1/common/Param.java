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
 
 package com.fms1.common;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Hashtable;
import java.util.Vector;

import com.fms1.tools.Db;
import com.fms1.web.*;
import com.fms1.infoclass.*;

/**
 * Logic related to size estimation methods/conversion rates, and administrator defined content.
 *
 */
final class Param {
    // This hash table is for language reference by language_id
    private static Hashtable languageMap;
    
	/**
	 * get list of estimation method
	 * @return Vector of EstimationMethodInfo
	 */
	public static final Vector getEstimationMethodList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM estimation_method ORDER BY NAME";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final EstimationMethodInfo info = new EstimationMethodInfo();
				info.methodID = rs.getLong("METHOD_ID");
				info.name = rs.getString("NAME").trim();
				info.note = rs.getString("NOTE");
				info.isrelevant = rs.getInt("ISRELEVANT");
				list.addElement(info);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return list;
		}
	}

	public static final Vector getContractTypeList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM CONTRACT_TYPE ORDER BY CONTRACT_TYPE_ID";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final ContractTypeInfo info = new ContractTypeInfo();
				info.contracttypeID = rs.getLong("CONTRACT_TYPE_ID");
				info.contracttypeName =
					rs.getString("CONTRACT_TYPE_NAME").trim();
				if (rs.getString("CONTRACT_TYPE_DESCRIPTION") == null)
					info.contracttypeDescription = "";
				else
					info.contracttypeDescription =
						rs.getString("CONTRACT_TYPE_DESCRIPTION").trim();
				info.contracttypeStatus = rs.getByte("CONTRACT_TYPE_STATUS");
				list.addElement(info);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return list;
		}
	}

	public static final Vector getBizDomainList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM BUSINESS_DOMAIN ORDER BY DOMAIN_NAME";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final BizDomainInfo info = new BizDomainInfo();
				info.domainID = rs.getLong("DOMAIN_ID");
				info.name = rs.getString("DOMAIN_NAME").trim();
				info.domainStatus = rs.getByte("STATUS");
				list.addElement(info);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return list;
		}
	}
	public static final boolean setBizDomain(final BizDomainInfo info) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE BUSINESS_DOMAIN SET DOMAIN_NAME = ?, STATUS = ? WHERE DOMAIN_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, info.name);
			prepStmt.setByte(2, info.domainStatus);
			prepStmt.setLong(3, info.domainID);
			prepStmt.executeUpdate();
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static final boolean delBizDomain(final long bdid) {
		return Db.delete(bdid, "DOMAIN_ID", "BUSINESS_DOMAIN");

	}
	public static final boolean addBizDomain(final BizDomainInfo info) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT DISTINCT MAX(DOMAIN_ID) MAX_DOMAIN_ID FROM BUSINESS_DOMAIN";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			final int bizdomainID;
			if (rs.next())
				bizdomainID = rs.getInt("MAX_DOMAIN_ID") + 1;
			else
				bizdomainID = 0;
			sql =
				"INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME) VALUES (?, ?)";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, bizdomainID);
			prepStmt.setString(2, info.name);
			prepStmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	public static final Vector getAppTypeList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM APPLICATION_TYPE ORDER BY TYPE_NAME";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final AppTypeInfo info = new AppTypeInfo();
				info.apptypeID = rs.getInt("APPLICATION_ID");
				info.name = rs.getString("TYPE_NAME").trim();
				info.appStatus = rs.getByte("STATUS");
				list.addElement(info);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
		return null;
	}
	public static final boolean addProTailoring(final ProTailoringInfo info) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO TAILORING(TAIL_ID, TAIL_PER,APP_CRI,PROCESS_ID, CATEGORY) Values ((SELECT NVL(MAX(TAIL_ID)+1,1) from TAILORING),?,?,?,?)";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, info.Tailoring_per);
			prepStmt.setString(2, info.Applicable_Cri);
			prepStmt.setInt(3, info.ProcessID);
			prepStmt.setByte(4, info.tailLyfeCycle);
			prepStmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	public static final boolean setProTailoring(final ProTailoringInfo info) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE TAILORING SET TAIL_PER = ?, APP_CRI=?,PROCESS_ID=?, STATUS = ?, CATEGORY = ? WHERE TAIL_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, info.Tailoring_per);
			prepStmt.setString(2, info.Applicable_Cri);
			prepStmt.setInt(3, info.ProcessID);
			prepStmt.setByte(4, info.tailStatus);
			prepStmt.setByte(5, info.tailLyfeCycle);
			prepStmt.setLong(6, info.TailoringID);
			prepStmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static final Vector getproTailoringListExisted(long tailID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT B.CODE FROM PROJECT_TAILORING A, PROJECT B WHERE A.PROCESS_TAIL_ID=? AND A.PROJECT_ID=B.PROJECT_ID";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, tailID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final TailoringInfo info = new TailoringInfo();
				info.projectCode = rs.getString("CODE");
				list.addElement(info);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
		return null;
	}
	public static final boolean delProTailoring(final long tailid) {

		return Db.delete(tailid, "TAIL_ID", "TAILORING");

	}
	public static final Vector getProTailoringList() {
		return getProTailoringList(-1);
	}
	public static final Vector getProTailoringList(long processID) {
		
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		boolean serchByPro=processID>0;
		String strwhere= serchByPro?"AND TAIL.PROCESS_ID= ?" :"";
        if ((processID != ProcessInfo.GENERAL) && (processID != -1) && (ParamCaller.checkAddTail)) {
            strwhere = " AND TAIL.PROCESS_ID= "+processID+" UNION SELECT TAIL.TAIL_ID,TAIL.ACTION,TAIL.TAIL_PER,TAIL.APP_CRI,PRO.NAME,TAIL.PROCESS_ID, TAIL.STATUS, TAIL.CATEGORY " 
                       + " FROM TAILORING TAIL, PROCESS PRO" 
                       + " WHERE PRO.PROCESS_ID(+) = TAIL.PROCESS_ID AND TAIL.PROCESS_ID=" + ProcessInfo.GENERAL;  
        }
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT TAIL.TAIL_ID,TAIL.ACTION,TAIL.TAIL_PER,TAIL.APP_CRI,PRO.NAME,TAIL.PROCESS_ID AS F2, TAIL.STATUS, TAIL.CATEGORY AS F1" 
                   + " FROM TAILORING TAIL, PROCESS PRO "
                   + " WHERE PRO.PROCESS_ID(+) = TAIL.PROCESS_ID AND NOTE = 0 "
			       + strwhere + " ORDER BY F1, F2";
            prepStmt = conn.prepareStatement(sql);
            
            System.out.println(prepStmt.toString());
            rs = prepStmt.executeQuery();
			while (rs.next()) {
				ProTailoringInfo info = new ProTailoringInfo();
				info.TailoringID = rs.getLong("TAIL_ID");
				info.ProcessID=rs.getInt("F2");
                switch (info.ProcessID) {
                    case ProcessInfo.FSOFT_SLC :
                         info.ProcessName="FSOFT SLC";   
                         break;
                    case ProcessInfo.GENERAL :
                         info.ProcessName="General";   
                         break;
                    default :
                         info.ProcessName = ProcessInfo.getProcessName(info.ProcessID); 
                } 
				info.Tailoring_per = rs.getString("TAIL_PER");
				info.Applicable_Cri = rs.getString("APP_CRI");
                info.tailStatus = rs.getByte("STATUS");
                info.action = rs.getByte("ACTION"); // Added by HaiMM
                if (rs.getObject("F1") == null) {
                    info.tailLyfeCycle = -2;
                } else {
                    info.tailLyfeCycle = rs.getByte("F1");
                }
                info.lyfeCycleName = ProTailoringInfo.parseLifecycle(info.tailLyfeCycle);
                list.addElement(info);
			}
       }
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, prepStmt, rs);
				return list;
			}
			
	}

	public static final boolean setContractType(final ContractTypeInfo info) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE CONTRACT_TYPE SET CONTRACT_TYPE_NAME = ?, CONTRACT_TYPE_DESCRIPTION = ?, CONTRACT_TYPE_STATUS = ? WHERE CONTRACT_TYPE_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, info.contracttypeName);
			prepStmt.setString(2, info.contracttypeDescription);
			prepStmt.setByte(3, info.contracttypeStatus);
			prepStmt.setLong(4, info.contracttypeID);
			prepStmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static final boolean addContractType(final ContractTypeInfo info) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT DISTINCT MAX(CONTRACT_TYPE_ID) MAX_CONTRACT_TYPE_ID FROM CONTRACT_TYPE";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			final int contracttypeID;
			if (rs.next())
                contracttypeID = rs.getInt("MAX_CONTRACT_TYPE_ID") + 1;
			else
                contracttypeID = 0;
			sql =
				"INSERT INTO CONTRACT_TYPE(CONTRACT_TYPE_ID, CONTRACT_TYPE_NAME, CONTRACT_TYPE_DESCRIPTION) VALUES (?, ?, ?)";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, contracttypeID);
			prepStmt.setString(2, info.contracttypeName);
            prepStmt.setString(3, info.contracttypeDescription);
			prepStmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
    public static final boolean delContractType(final long contracttypeID) {
        return Db.delete(contracttypeID, "CONTRACT_TYPE_ID", "CONTRACT_TYPE");
    }
    
	public static final boolean setAppType(final AppTypeInfo info) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE APPLICATION_TYPE SET TYPE_NAME = ?, STATUS = ? WHERE APPLICATION_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, info.name);
			prepStmt.setByte(2, info.appStatus);
			prepStmt.setLong(3, info.apptypeID);
			prepStmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static final boolean delAppType(final long atid) {
		return Db.delete(atid, "APPLICATION_ID", "APPLICATION_TYPE");

	}
	public static final boolean addAppType(final AppTypeInfo info) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT DISTINCT MAX(APPLICATION_ID) MAX_APPLICATION_ID FROM APPLICATION_TYPE";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			final int apptypeID;
			if (rs.next())
				apptypeID = rs.getInt("MAX_APPLICATION_ID") + 1;
			else
				apptypeID = 0;
			sql =
				"INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME) VALUES (?, ?)";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, apptypeID);
			prepStmt.setString(2, info.name);
			prepStmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	public static final Vector getRelevantEstimationMethodList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final Vector list = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT * FROM estimation_method WHERE ISRELEVANT = 1 ORDER BY NAME";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final EstimationMethodInfo info = new EstimationMethodInfo();
				info.methodID = rs.getLong("METHOD_ID");
				info.name = rs.getString("NAME").trim();
				info.note = rs.getString("NOTE");
				info.isrelevant = rs.getInt("ISRELEVANT");
				list.addElement(info);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
		return null;
	}
	public static final EstimationMethodInfo getEstimationMethod(final int methodID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final EstimationMethodInfo info = new EstimationMethodInfo();
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM estimation_method WHERE METHOD_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, methodID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				info.methodID = rs.getInt("METHOD_ID");
				info.name = rs.getString("NAME");
				info.note = rs.getString("NOTE");
				info.isrelevant = rs.getInt("ISRELEVANT");
			}
			return info;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	public static final boolean addEstimationMethod(final EstimationMethodInfo emi) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			final int methodID = (int)ServerHelper.getNextSeq("ESTIMATION_METHOD_SEQ");
			sql =
				"INSERT INTO estimation_method(METHOD_ID, NAME, NOTE) VALUES (?, ?, ?)";

			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, methodID);
			prepStmt.setString(2, emi.name);
			prepStmt.setString(3, emi.note);
			prepStmt.executeUpdate();
			return true;
		}
		catch (SQLException ex){
			ex.printStackTrace();
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static final boolean setEstimationMethod(final EstimationMethodInfo emi) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE estimation_method SET NAME = ?, NOTE = ? WHERE METHOD_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, emi.name);
			prepStmt.setString(2, emi.note);
			prepStmt.setLong(3, emi.methodID);
			prepStmt.executeUpdate();
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static final boolean delEstimationMethod(final long emid) {
		Db.delete(emid, "METHOD_ID", "CONVERSION");
		Db.delete(emid, "METHOD_ID", "estimation_method");
		return true;

	}
	/**
	 * get list of language
	 * @return Vector of LanguageInfo
	 */
	public static final Vector getLanguageList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final Vector list = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM LANGUAGE ORDER BY NAME";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final LanguageInfo info = new LanguageInfo();
				info.languageID = rs.getInt("LANGUAGE_ID");
				info.name = rs.getString("NAME");
				info.note = rs.getString("NOTE");
				info.lastUpdate = rs.getDate("CONV_LAST_UPDATE");
				info.sizeUnit = rs.getString("SIZE_UNIT");
				info.isrelevant = rs.getInt("ISRELEVANT");
				list.addElement(info);
			}
			return list;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		} 
		catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	/**
	 * get list of language
	 * @return Vector of LanguageInfo
	 */
	public static final Vector getRelevantLanguageList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final Vector list = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM LANGUAGE WHERE ISRELEVANT = 1 ORDER BY NAME";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final LanguageInfo info = new LanguageInfo();
				info.languageID = rs.getInt("LANGUAGE_ID");
				info.name = rs.getString("NAME");
				info.note = rs.getString("NOTE");
				info.lastUpdate = rs.getDate("CONV_LAST_UPDATE");
				info.sizeUnit = rs.getString("SIZE_UNIT");
				info.isrelevant = rs.getInt("ISRELEVANT");
				list.addElement(info);
			}
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
    
    /**
     * Clear language list
     */
    public static synchronized void clearLanguages() {
        if (languageMap != null) {
            languageMap.clear();
            languageMap = null;
        }
    }
	/**
     * Get language information by language Id
     * @param langID
     * @return
     */
    public static final synchronized LanguageInfo getLanguage(final int langID) {
        // If not loaded the languages to hashtable then load them
        if (languageMap == null || languageMap.size() <= 0) {
            // 22-Aug-07: there are 6xx records in language table
            languageMap = new Hashtable(1000);
            Connection conn = null;
            Statement stm = null;
            String sql = null;
            ResultSet rs = null;
            try {
                conn = ServerHelper.instance().getConnection();
                sql = "SELECT * FROM LANGUAGE";
                stm = conn.createStatement();
                rs = stm.executeQuery(sql);
                while (rs.next()) {
                    LanguageInfo info = new LanguageInfo();
                    info.languageID = rs.getInt("LANGUAGE_ID");
                    info.name = rs.getString("NAME");
                    info.note = rs.getString("NOTE");
                    info.lastUpdate = rs.getDate("CONV_LAST_UPDATE");
                    info.sizeUnit = rs.getString("SIZE_UNIT");
                    info.isrelevant = rs.getInt("ISRELEVANT");
                    // Put to hash table using languageId as key
                    languageMap.put(new Integer(info.languageID), info);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
            finally {
                ServerHelper.closeConnection(conn, stm, rs);
            }
        }
        return (LanguageInfo) languageMap.get(new Integer(langID));
	}
    /**
     * Get language name by language Id
     * @param languageId
     * @return
     */
    public static String getLanguageName(int languageId) {
    	LanguageInfo languageInfo = getLanguage(languageId);
		return (languageInfo == null) ? "" : languageInfo.name;
    }
    
	public static final boolean setLanguage(final LanguageInfo li) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE LANGUAGE SET NAME = ?, NOTE = ?, CONV_LAST_UPDATE = ?,SIZE_UNIT=?, ISRELEVANT=? WHERE LANGUAGE_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, li.name);
			prepStmt.setString(2, li.note);
			prepStmt.setDate(3, li.lastUpdate);
			prepStmt.setString(4, li.sizeUnit);
			if (li.isrelevant == 1){
				prepStmt.setInt(5,li.isrelevant);
			}
			else{
				prepStmt.setNull(5,Types.VARCHAR);
			}
			prepStmt.setInt(6, li.languageID);
			try {
				prepStmt.executeUpdate();
			}
			catch (SQLException f) {
				return false;
			}
			Parameters.updateParameter("CONV_LAST_UPDATE", li.lastUpdate);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
            // Release languages from hashtable (reload on next get language by Id)
            clearLanguages();
		}
	}
	/**
	 * get list of conversion
	 * @return Vector of ConversionInfo
	 */
	public static final Vector getConversionList(final String searchedLangName) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final Vector list = new Vector();
		try {
			String condition = null;
			if (searchedLangName != null)
				condition = " AND UPPER(LANGUAGE.NAME) LIKE UPPER(?) ";
			else
				condition = "";
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT  CONVERSION_ID,LANGUAGE.NAME LANG_NAME,LANGUAGE.SIZE_UNIT , ESTIMATION_METHOD.NAME METHOD_NAME ,SLOC,LANGUAGE.LANGUAGE_ID,CONVERSION.METHOD_ID, LANGUAGE.NOTE, LANGUAGE.ISRELEVANT COMMONUSED "
					+ " FROM CONVERSION, LANGUAGE, ESTIMATION_METHOD"
					+ " WHERE CONVERSION.LANGUAGE_ID = LANGUAGE.LANGUAGE_ID "
					+ " AND CONVERSION.METHOD_ID = ESTIMATION_METHOD.METHOD_ID "
					+ condition
					+ " GROUP BY LANGUAGE.NAME ,LANGUAGE.SIZE_UNIT , ESTIMATION_METHOD.NAME  ,SLOC,LANGUAGE.LANGUAGE_ID,CONVERSION.METHOD_ID,CONVERSION_ID,LANGUAGE.NOTE, LANGUAGE.ISRELEVANT "
					+ " ORDER BY LANG_NAME";
			prepStmt = conn.prepareStatement(sql);
			if (searchedLangName != null)
				prepStmt.setString(1, "%" + searchedLangName.trim() + "%");
			//because '%?%' doesn't work
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final ConversionInfo info = new ConversionInfo();
				info.setConversionID(rs.getLong("CONVERSION_ID"));
				BigDecimal bdTemp = rs.getBigDecimal("SLOC");
				if (bdTemp == null)
					info.sloc = Double.NaN;
				else
					info.sloc = bdTemp.doubleValue();
				info.languageID = rs.getInt("LANGUAGE_ID");
				info.methodID = rs.getInt("METHOD_ID");
				info.note = rs.getString("NOTE");
				info.method = rs.getString("METHOD_NAME");
				info.language = rs.getString("LANG_NAME");
				info.sizeUnit = rs.getString("SIZE_UNIT");
				if (rs.getString("COMMONUSED") == null){
					info.setCommonUsed(0);
				}
				else{
					info.setCommonUsed(1);
				}
				list.addElement(info);
			}
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	public static final ConversionInfo addConversion(final ConversionInfo conversion) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			if (conversion.languageID == -1) {
				final long lLanguageID = ServerHelper.getNextSeq("LANGUAGE_SEQ");
				sql =
					"INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE,SIZE_UNIT, ISRELEVANT) "
						+ "VALUES (NVL((select max(LANGUAGE_ID)+1 from LANGUAGE ),1), ?, ?, ?, ?, ?)";
				prepStmt = conn.prepareStatement(sql);
				
				//prepStmt.setLong(1, lLanguageID);
				prepStmt.setString(1, conversion.language);
				prepStmt.setString(2, conversion.note);
				prepStmt.setDate(3, conversion.lastUpdate);
				prepStmt.setString(4, conversion.sizeUnit);
				if (conversion.getCommonUsed() == 1){
					prepStmt.setInt(5, conversion.getCommonUsed());
				}
				else{
					prepStmt.setNull(5, Types.INTEGER);
				}
				prepStmt.executeUpdate();
				conversion.languageID = lLanguageID;
				final java.sql.Date today =
					new Date(new java.util.Date().getTime());
				Parameters.updateParameter("CONV_LAST_UPDATE", today);
				return conversion;
			}
			long lConversionID = ServerHelper.getNextSeq("CONVERSION_SEQ");
			sql =
				"INSERT INTO CONVERSION(CONVERSION_ID, SLOC, LANGUAGE_ID, METHOD_ID) "
					+ "VALUES (?, ?, ?, ?)";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, lConversionID);
			if (Double.isNaN(conversion.sloc))
				prepStmt.setNull(2, java.sql.Types.DOUBLE);
			else
				prepStmt.setDouble(2, conversion.sloc);
			prepStmt.setLong(3, conversion.languageID);
			prepStmt.setLong(4, conversion.methodID);
			prepStmt.executeUpdate();
			return conversion;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	public static final boolean setConversion(final ConversionInfo ci) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE CONVERSION SET SLOC = ? WHERE LANGUAGE_ID = ? AND METHOD_ID = ? ";
			prepStmt = conn.prepareStatement(sql);
			if (Double.isNaN(ci.sloc))
				prepStmt.setNull(1, java.sql.Types.DOUBLE);
			else
				prepStmt.setDouble(1, ci.sloc);
			prepStmt.setLong(2, ci.languageID);
			prepStmt.setLong(3, ci.methodID);
			if (prepStmt.executeUpdate() == 1)
				return true;
			else
				return (addConversion(ci) != null);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static final boolean delConversionByLanguage(final int languageID) {
		Db.delete(languageID, "LANGUAGE_ID", "CONVERSION");
		boolean retval = Db.delete(languageID, "LANGUAGE_ID", "LANGUAGE");
		final java.sql.Date today = new Date(new java.util.Date().getTime());
		Parameters.updateParameter("CONV_LAST_UPDATE", today);

        // Release languages from hashtable (reload on next get language by Id)
        clearLanguages();
		return retval;
	}

	public static final boolean setCompletenessRate(final CompletenessRateInfo cri) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			final java.sql.Date today =
				new Date(new java.util.Date().getTime());
			conn = ServerHelper.instance().getConnection();
			sql =
			"UPDATE COMPLETENESS_RATE SET VALUE = ?, LAST_UPDATE = ? WHERE STATUSID = ? AND PROJECT_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setDouble(1, cri.value);
			prepStmt.setDate(2, today);
			prepStmt.setLong(3, cri.statusID);
			prepStmt.setLong(4, cri.projectID);
			int result = prepStmt.executeUpdate();
			if (result < 1) {
				sql =
				"INSERT INTO COMPLETENESS_RATE (VALUE , LAST_UPDATE ,STATUSID , PROJECT_ID) VALUES( ?,  ? , ? , ?)";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setDouble(1, cri.value);
				prepStmt.setDate(2, today);
				prepStmt.setLong(3, cri.statusID);
				prepStmt.setLong(4, cri.projectID);
				prepStmt.executeUpdate();
			}
			//Parameters.instance().updateParameter("COMPL_RATE_LAST_UPDATE", today);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public final static Vector getCompletenessRateList(ProjectInfo pinf) {

		Connection conn = null;
		Statement stmt = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT count(*) NCOUNT FROM COMPLETENESS_RATE WHERE PROJECT_ID = "
					+ pinf.getProjectId();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int nCount = 0;
			if (rs.next())
				nCount = rs.getInt("NCOUNT");
			rs.close();
			stmt.close();
			NormInfo nrmInf = null;
			double sum = 0;
			CompletenessRateInfo info;
			if (nCount == 0) {
				//get from norm
				Vector normList = Norms.getEffortDstrByProcRCR(pinf);

				for (int i = 0;
					i < MetricDescInfo.completionMetrics.length;
					i++) {
					nrmInf =
						NormInfo.getNormByMetricID(
							MetricDescInfo.completionMetrics[i],
							normList);
					info = new CompletenessRateInfo();
					info.statusID = RequirementInfo.statusList[i];
					info.projectID = pinf.getProjectId();
					info.lifecycleID = pinf.getLifecycleId();
					if (nrmInf != null) {

						sum += nrmInf.average;
						info.lastUpdate = nrmInf.date;
					}
					info.value = sum;

					info.status = RequirementInfo.getStatusName(i);
					info.lifecycle = pinf.getLifecycle();
					list.addElement(info);
				}

			} else {

				sql =
					"SELECT COMPLETENESS_RATE.STATUSID, VALUE, LAST_UPDATE"
						+ " FROM COMPLETENESS_RATE "
						+ " WHERE PROJECT_ID = ? "
						+ " ORDER BY COMPLETENESS_RATE.LAST_UPDATE DESC";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setLong(1, pinf.getProjectId());
//				prepStmt.setInt(2, pinf.getLifecycleID());
				rs = prepStmt.executeQuery();
				while (rs.next()) {
					info = new CompletenessRateInfo();
					info.statusID = rs.getInt("STATUSID");
					info.projectID = pinf.getProjectId();
//					info.lifecycleID = rs.getInt("LIFECYCLE_ID");
					info.value = rs.getDouble("VALUE");
					info.lastUpdate = rs.getDate("LAST_UPDATE");
					info.status = RequirementInfo.getStatusName(info.statusID);
					info.lifecycle = pinf.getLifecycle();
					list.addElement(info);
				}
			}
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	
	// Added by HaiMM
	public static int getActionByTail(final int tailoringId){
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int actionId = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT action FROM Tailoring WHERE tail_id = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, tailoringId);
			rs = stm.executeQuery();
			if (rs.next()) {
				actionId = rs.getInt("actionType");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return actionId;
		}
	}

}
