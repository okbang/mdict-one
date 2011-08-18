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
 * Created on Apr 24, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.fms1.common;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Calendar;
import com.fms1.infoclass.*;
import com.fms1.tools.Db;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;
import com.fms1.web.ServerHelper;
/**
 * @author TienHM08
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Evaluations {
	
	
	
	public static final void query(String query) {
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareCall(query);	
			rs = stm.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
	/*public static final ResultSet getAssignmentByProjectId(int projectId) {
		String query = "select distinct developer_id,response from assignment where project_id = "+projectId;
		return query(query);
	}
	
	public static final ResultSet getDuration(int projectId,int developerId){
		String query = "select duration from timesheet where project_id ="+projectId+" and developer_id="+developerId;
		return query(query);
	}
	
	public static final ResultSet getMaxTeamEvaluationId(){
		String query= "select count(team_evaluation_id) from team_evaluation";
		return query(query); 
	}
	
	public static final ResultSet getInTeamEvaluation(int projectId,int developerId){
		String query ="select effect,cost from team_evaluation where project_id="+projectId+" and developer_id="+developerId;
		return query(query);
	}*/
	
	public static final void deleteTeamEvaluation(int projectId){
		String query = "delete from team_evaluation where project_id = " +projectId;
		query(query);
	}
	
	public static final int countDevInProject(int projectId){
		String query = "select count(developer_id) as abc from assignment where project_id="+projectId;
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		int result=0;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareCall(query);	
			rs = stm.executeQuery();
			while(rs.next()) {
				result =  rs.getInt("ABC");
			}
		} catch (SQLException e) {
			return 0;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return result;
		}
	}
	
	public static final String getPPPositionCode(int response){
		String query = "SELECT name FROM responsibility  where responsibility_id = " + response ;
		
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String result = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareCall(query);	
			rs = stm.executeQuery();
			while(rs.next()) {
				result =  rs.getString("NAME");
			}
		} catch (SQLException e) {
			return null;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return result;
		}
	}
	
	public static final float sumDuration(int projectID, int developerID){
		String query = "select sum(duration)/8 as result from timesheet where developer_id = "+developerID+" and project_id = "+projectID;
		
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		float result = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareCall(query);	
			rs = stm.executeQuery();
			while(rs.next()) {
				result =  rs.getFloat("RESULT");
			}
		} catch (SQLException e) {
			return 0;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return result;
		}
	}
	
	/*public static final ResultSet getEffectCostNote(int projectID,int developerID){
		String query = "SELECT effect, cost, note FROM team_evaluation  where project_id = "+projectID+"  and developer_id = "+developerID;
		return query(query);
	}
	
	public static final ResultSet getNameDev(int developerID){
		String query = "SELECT name FROM developer where developer_id = "+developerID;
		return query(query);
	}*/
	
	public static final List getTeamEvaluation(int prjID) {
		int numberDevInPrj = countDevInProject(prjID);
		List teamEvaluationInfo = new ArrayList();
		
		int size = 0;
		try {
			
			String query = "select distinct developer_id,response from assignment where project_id = "+prjID;
			Connection conn = null;
			PreparedStatement stm = null;
			ResultSet rs = null;
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareCall(query);	
			rs = stm.executeQuery();
			
			while(rs.next()) {
				size++;
			}
			if (size > 0) {
				conn = ServerHelper.instance().getConnection();
				stm = conn.prepareCall(query);	
				rs = stm.executeQuery();
				int[] dev_id_all = new int[size];
				int[] res_all = new int[size];
				int[] dev_id = new int[numberDevInPrj];
				boolean[] flag = new boolean[size];
				int check = 0 ; 
				while(rs.next()) {
					dev_id_all[check] = rs.getInt("DEVELOPER_ID");
					res_all[check] = rs.getInt("RESPONSE");
					flag[check] = true;
					check++;
				}
				ServerHelper.closeConnection(conn, stm, rs);
				for (int i=0;i<size;i++){
					TeamEvaluationInfo temp = new TeamEvaluationInfo();
					if (flag[i]==true) {
						temp.setDeveloperID(dev_id_all[i]);
						int subSize = 0 ; 
						for (int k=0;k<size;k++)
							if (dev_id_all[k]==dev_id_all[i]) subSize++;
						int[] tempResSub = new int[subSize];
						int posTempResSub = 0 ; 
						for (int j=i;j<size;j++) {
							if (dev_id_all[i]==dev_id_all[j]) {
								flag[j] = false;
								tempResSub[posTempResSub++] = res_all[j];
							}
						}
						temp.setResponse(tempResSub);
						temp.setProjectID(prjID);
						teamEvaluationInfo.add(temp);
					}
					
				}
				
				for (int i=0;i<teamEvaluationInfo.size();i++) {
					TeamEvaluationInfo temp1 = (TeamEvaluationInfo)teamEvaluationInfo.get(i);
					String[] tempHP = new String[temp1.getResponse().length];
					String[] tempNote = new String[temp1.getResponse().length]; 
					String[] tempPositionCode = new String[temp1.getResponse().length]; 
					for (int j=0;j<temp1.getResponse().length;j++) {
						tempPositionCode[j] = getPPPositionCode(temp1.getResponse()[j]);
					}
					temp1.setRole(tempPositionCode);
					
					temp1.setPercentAttend(sumDuration(temp1.getProjectID(),temp1.getDeveloperID()));
					
					for (int k=0;k<temp1.getResponse().length;k++) {
					
						String query1 = "SELECT effect, cost, note FROM team_evaluation  where role= '"+ temp1.getRole()[k] +"' and project_id = '"+temp1.getProjectID()+"'  and developer_id = "+temp1.getDeveloperID();
						Connection conn1 = null;
						PreparedStatement stm1 = null;
						conn1 = ServerHelper.instance().getConnection();
						stm1 = conn1.prepareCall(query1);	
						ResultSet keepValue = stm1.executeQuery();
						tempHP[k]="";
						tempNote[k]="";
						while(keepValue.next()) {
							
							String keep1 = keepValue.getString("EFFECT");
							float keep2 = keepValue.getFloat("COST");
							String keep3 = keepValue.getString("NOTE");
							if (keep1!=null)
								tempHP[k] = keep1;
							
							temp1.setPc(keep2);
							
							if (keep3!=null)
								tempNote[k] = keep3;
						}
						
						ServerHelper.closeConnection(conn1, stm1, keepValue);
					}
					
					temp1.setHq(tempHP);
					temp1.setNote(tempNote);
					
					String query2 = "SELECT name,staff_id FROM developer where developer_id = "+temp1.getDeveloperID();
					Connection conn2 = null;
					PreparedStatement stm2 = null;
					conn2 = ServerHelper.instance().getConnection();
					stm2 = conn2.prepareCall(query2);	
					ResultSet keepValue2 = stm2.executeQuery();
					
					while(keepValue2.next()){
						temp1.setName(keepValue2.getString("NAME"));
						temp1.setStaffID(keepValue2.getString("STAFF_ID"));
					}
					ServerHelper.closeConnection(conn2, stm2, keepValue2);
				}
				float sumEffort = 0 ;
				for (int k=0;k<teamEvaluationInfo.size();k++) {
					TeamEvaluationInfo temp2 =(TeamEvaluationInfo)(teamEvaluationInfo.get(k)); 
					sumEffort +=  temp2.getPercentAttend();
				}
				for (int k=0;k<teamEvaluationInfo.size();k++) {
					TeamEvaluationInfo temp3 =(TeamEvaluationInfo)(teamEvaluationInfo.get(k));
					float value = (temp3.getPercentAttend()/sumEffort)*100;
					temp3.setPercentAttend(round(value,2));
				}
				return teamEvaluationInfo;
			}
			else {
				ServerHelper.closeConnection(conn, stm, rs);
				return null;
				  
			} 
		}
		catch(Exception e){
			e.printStackTrace(); 
		}
		return null;
	}
	public static final void updateTeamEva(int prjID,String devID,String HP,String PC,String note,String role){
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		long position = 1;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareCall("select max(team_evaluation_id) as max from team_evaluation");	
			rs = stm.executeQuery();
			while(rs.next()) position = rs.getLong("MAX");
			position++;
			stm = conn.prepareCall("select * from team_evaluation where project_id = "+prjID+" and role= '"+role+"' and developer_id= "+devID);
			rs = stm.executeQuery();
			while(rs.next()) {
				stm = conn.prepareCall("update team_evaluation set effect='"+HP+"',cost='"+PC+"',note='"+note+"' where project_id ="+prjID+" and role= '"+role+"' and developer_id= "+devID);
				stm.executeQuery();
				ServerHelper.closeConnection(conn, stm, rs);
				return;
			}
			stm = conn.prepareCall("insert into team_evaluation values('"+position+"','"+prjID+"','"+devID+"','"+role+"','"+HP+"','"+PC+"','"+note+"')");
			stm.executeQuery();
		} catch (SQLException e) {
			ServerHelper.closeConnection(conn, stm, rs);
			e.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}		
	}
	public static float round(float number, int digit)
	{
		if (digit > 0)
		{
			int temp = 1, i;
			for (i = 0; i < digit; i++)
			temp = temp*10;
			number = number*temp;
			number = Math.round(number);
			number = number/temp;
			return number;
		}
		else
			return 0;
	}
}
