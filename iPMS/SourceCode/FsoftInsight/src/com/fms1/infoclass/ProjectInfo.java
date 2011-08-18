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
 
 package com.fms1.infoclass;
import java.util.Collection;
import com.fms1.common.Project;
import com.fms1.tools.CommonTools;
import java.sql.Date;
import com.fms1.web.*;

public class ProjectInfo {
	/**
	* Get all info in project table
	*/
	private Collection AssignmentList = null;
	
	public static final int LIFECYCLE_DEVELOPMENT = 0;
	public static final int LIFECYCLE_MAINTENANCE = 1;
	public static final int LIFECYCLE_OTHER = 2;
	public static final int [] lifecycles = {LIFECYCLE_DEVELOPMENT,LIFECYCLE_MAINTENANCE,LIFECYCLE_OTHER};
	public static final int STATUS_CLOSED = 1;
	public static final int STATUS_CANCELLED = 2;
	public static final int STATUS_TENTATIVE = 3;
	public static final int STATUS_ONGOING = 0;
    public static final int STATUS_ARCHIVE = 4;
    public static final int PROJECT_INTERNAL = 8;
    public static final int PROJECT_EXTERNAL = 0;

	public static final String INTERNAL = "Internal";
	public static final String EXTERNAL = "External";
	public static final String PUBLIC = "Public";
    //HuyNH2 add code for project archive   
	public static final String [] statuses = {"On-going","Closed","Cancelled","Tentative","Archive"};
	
	// Add by HaiMM
	public int riskNo = -1;

    // end 
	public ProjectInfo() {
		this.projectId = 0;
        this.code = "";
		this.name = "";
		this.groupName = "";
        this.divisionName = "";
		this.customer = "";
		this.secondCustomer = "";
		this.planStartDate = null;
		this.planFinishDate = null;
		this.scopeAndObjective = "";
		this.leader = "";
        this.leaderName = "";
        this.status = -1;
        this.rank = "";
        this.projectType = "";
        this.projectLevel = "";
        this.applicationType = "";
        this.businessDomain = "";
        this.contractType = "";
        this.startDate = null;
        this.lastUpdate = null;
        this.description = null;
        this.riskNo = -1; // Add by HaiMM
        this.typeCustomer = 0 ; 
        this.typeCustomer2 = 0 ;
	}
	private int lifecycleId;
	private String lifecycle;
	private String projectType;
	private int type;
	private long workUnitId;
	private long parent;
	private long grandParent;
	private long projectId;
	private String code;
	private String name;
	private String groupName;
    private String divisionName;
    private String projectLevel;
    private String rank;
	private Date startDate;
    private Date planStartDate;
	private long perCompleted;
	private String leader;
    private String leaderName;
	private String description;
	private String scopeAndObjective;
	private int status;
	private String statusName;
	private String customer;
	private String secondCustomer;
    private String businessDomain;
    private String applicationType;
    private String contractType;
    
    // HuyNH2 add some line code for project archive
    private double archiveStatus = 0; // default for not archive                    
    // end
	private Date baseFinishDate;
	private Date planFinishDate;
	private Date plannedFinishDate;
	private Date actualFinishDate;
	private Date actualStartDate;
    private String actualFinishDateString;
	private double baseEffort = Double.NaN;
	private double planEffort = Double.NaN;
    private double plannedEffort = Double.NaN;//=replan if replan otherwise =base
	private long actualEffort;
    // HUYNH2 add billable effort
    private double baseBillableEffort = Double.NaN;
    private double planBillableEffort = Double.NaN;
    private double plannedBillableEffort = Double.NaN;//=replan if replan otherwise =base
    private double actualBillableEffort;
    // End add billable effort
    private double planCalendarEffort= Double.NaN;
    private double replanCalendarEffort = Double.NaN;
    private double plannedCalendarEffort = Double.NaN;   // if replanned then = replan otherwise = plan

	private long scheduleStatus;
	private long effortStatus;
	private Date lastUpdate = new Date(0);
	private int applyPPM;
	private String reason;
	private double totalProductivityLoc = 0;
	private double totalQualityLoc = 0;
	// landd add 
	private boolean isExternal = false;
	
	private int typeCustomer ; 
	private int typeCustomer2 ; 
	
    /**
     * Show all Properties
     */
	public boolean getExternalStatus() {
		return this.isExternal;
	}
	public void setExternalStatus(boolean bEx) {
		this.isExternal = bEx;
	}
    
    public int getLifecycleId() {
    	return this.lifecycleId;
    }
    public void setLifecycleId(int iLifeCycleId) {
    	this.lifecycleId = iLifeCycleId;
    }

    public String getLifecycle() {
    	return this.lifecycle;
    }
    public void setLifecycle(String strLifecycle) {
    	if (strLifecycle == null) {
    		this.lifecycle = "";
    	}
    	else {
    		this.lifecycle = strLifecycle;
    	}
    }

    public String getProjectType() {
    	return this.projectType;
    }
    public void setProjectType(String strProjectType) {
    	if (strProjectType == null) {
    		this.projectType = "";
    	} else {
    		this.projectType = strProjectType;
    	}
    }
    public int getType(){
    	return this.type;
    }
    public void setType(int type){
    	this.type = type;
    }

    public long getWorkUnitId() {
    	return this.workUnitId;
    }
    public void setWorkUnitId(long lWorkUnitId) {
    	this.workUnitId = lWorkUnitId;
    }

    public long getParent() {
    	return this.parent;
    }
    public void setParent(long longParent) {
    	this.parent = longParent;
    }

    public long getGrandParent() {
    	return this.grandParent;
    }
    public void setGrandParent(long lGrandParent) {
    	this.grandParent = lGrandParent;
    }

    public long getProjectId() {
    	return this.projectId;
    }
    public void setProjectId(long lProjectId) {
    	this.projectId = lProjectId;
    }

    public String getProjectCode() {
    	return this.code;
    }
    public void setProjectCode(String strProjectCode) {
    	if (strProjectCode == null) {
    		this.code = "";
    	} else {
    		this.code = strProjectCode;
    	}
    }

    public String getProjectName() {
    	return this.name;
    }
    public void setProjectName(String strProjectName) {
    	if (strProjectName ==  null) {
    		this.name = "";
    	} else {
    		this.name = strProjectName;
    	}
    }

    public String getContractType() {
        return this.contractType;
    }
    public void setContractType(String strContractType) {
        if (strContractType == null) {
            this.contractType = "";
        } else {
            this.contractType = strContractType;
        }
    }
    
    public String getProjectLevel() {
        return this.projectLevel;
    }
    public void setProjectLevel(String strProjectLevel) {
        if (strProjectLevel ==  null) {
            this.projectLevel = "";
        } else {
            this.projectLevel = strProjectLevel;
        }
    }
    
    public String getGroupName() {
    	return this.groupName;
    }
    public void setGroupName(String strGroupName) {
		if (strGroupName == null) {
			this.groupName = "";
		} else {
			this.groupName = strGroupName;
		}
    }
    
    public String getDivisionName() {
        return this.divisionName;
    }
    public void setDivisionName(String strDivisionName) {
        if (strDivisionName == null){
            this.divisionName = "";
        } else {
            this.divisionName = strDivisionName;
        }
    }

    public String getProjectRank() {
    	return this.rank;
    }
    public void setProjectRank(String strProjectRank) {
    	if (strProjectRank == null) {
    		this.rank = "";
    	} else {
    		this.rank = strProjectRank;
    	}
    }

    public Date getStartDate() {
    	return this.startDate;
    }
    public void setStartDate(Date dtStartDate) {
    	this.startDate = dtStartDate;
    }

    public Date getPlanStartDate() {
    	return this.planStartDate;
    }
    public void setPlanStartDate(Date dtPlanStartDate) {
    	this.planStartDate = dtPlanStartDate;
    }

    public long getPerComplete() {
    	return this.perCompleted;
    }
    public void setPerComplete(long lPerComplete) {
    	this.perCompleted = lPerComplete;
    }
	
	public String getScopeAndObjective() {
		return this.scopeAndObjective;
	}
	public void setScopeAndObjective(String strScopeAndObjective) {
		if (strScopeAndObjective == null) {
			this.scopeAndObjective = "";
		} else {
			this.scopeAndObjective = strScopeAndObjective;
		}
	}

    public String getDescription() {
    	return this.description;
    }
    public void setDescription(String strDescription) {
    	if (strDescription == null) {
    		this.description = "";
    	} else {
    		this.description = strDescription;
    	}
    }

    public String getLeader() {
    	return this.leader;
    }
    public void setLeader(String strLeader) {
    	if (strLeader == null) {
    		this.leader = "";
    	} else {
    		this.leader = strLeader;
    	}
    }

    public String getLeaderName() {
        return this.leaderName;
    }
    public void setLeaderName(String strLeaderName) {
        if (strLeaderName == null) {
            this.leaderName = "";
        } else {
            this.leaderName = strLeaderName;
        }
    }
    
    public double getArchiveStatus() {
    	return this.archiveStatus;
    }
    public void setArchiveStatus(double dArchiveStatus) {
    	this.archiveStatus = dArchiveStatus;
    }

    public Date getBaseFinishDate() {
    	return this.baseFinishDate;
    }
    public void setBaseFinishDate(Date dtBaseFinishDate) {
    	this.baseFinishDate = dtBaseFinishDate;
    }

    public Date getPlanFinishDate() {
    	return this.planFinishDate;
    }
    public void setPlanFinishDate(Date dtPlanFinishDate) {
    	this.planFinishDate = dtPlanFinishDate;
    }

	public Date getPlannedFinishDate() {
		return this.plannedFinishDate;
	}
	public void setPlannedFinishDate(Date dtplannedFinishDate) {
		this.plannedFinishDate = dtplannedFinishDate;
	}

    public Date getActualFinishDate() {
    	return this.actualFinishDate;
    }
    public void setActualFinishDate(Date dtActualFinishDate) {
    	this.actualFinishDate = dtActualFinishDate;
    }
    
	public Date getActualStartDate() {
		return this.actualStartDate;
	}
	public void setActualStartDate(Date dtActualStartDate) {
		this.actualStartDate = dtActualStartDate;
	}

    public String getactualFinishDateString() {
    	return this.actualFinishDateString;
    }
    public void setActualFinishDateString(String strActualFinishDateString) {
    	if (strActualFinishDateString == null) {
    		this.actualFinishDateString = "";
    	} else {
    		this.actualFinishDateString = strActualFinishDateString;
    	}
    }

    public double getBaseEffort() {
    	return this.baseEffort;
    }
    public void setBaseEffort(double dBaseEffort) {
    	this.baseEffort = dBaseEffort;
    }

    public double getPlanEffort() {
    	return this.planEffort;
    }
    public void setPlanEffort(double dPlanEffort) {
    	this.planEffort = dPlanEffort;
    }

    public double getPlannedEffort() {
    	return this.plannedEffort;
    }
    public void setPlannedEffort(double dPlannedEffort) {
    	this.plannedEffort = dPlannedEffort;
    }

    public long getActualEffort() {
    	return this.actualEffort;
    }
    public void setActualEffort(long lActualEffort) {
    	this.actualEffort = lActualEffort;
    }

    public double getBaseBillableEffort() {
    	return this.baseBillableEffort;
    }
    public void setBaseBillableEffort(double dBaseBillableEffort) {
    	this.baseBillableEffort = dBaseBillableEffort;
    }

    public double getPlanBillableEffort() {
    	return this.planBillableEffort;
    }
    public void setPlanBillableEffort(double dPlanBillableEffort) {
		this.planBillableEffort = dPlanBillableEffort;    	
    }

    public double getPlannedBillableEffort() {
    	return this.plannedBillableEffort;
    }
    public void setPlannedBillableEffort(double dPlannedBillableEffort) {
    	this.plannedBillableEffort = dPlannedBillableEffort;
    }

    public double getActualBillableEffort() {
    	return this.actualBillableEffort;
    }
    public void setActualBillableEffort(double dActualBillableEffort) {
    	this.actualBillableEffort = dActualBillableEffort;
    }

    public double getPlanCalendarEffort() {
    	return this.planCalendarEffort;
    }
    public void setPlanCalendarEffort(double dPlanCalendarEffort) {
    	this.planCalendarEffort = dPlanCalendarEffort;
    }

    public double getReplanCalendarEffort() {
    	return this.replanCalendarEffort;
    }
    public void setReplanCalendarEffort(double dReplanCalendarEffort) {
    	this.replanCalendarEffort = dReplanCalendarEffort;
    }

    public double getPlannedCalendarEffort() {
    	return this.plannedCalendarEffort;
    }
    public void setPlannedCalendarEffort(double dPlannedCalendarEffort) {
    	this.plannedCalendarEffort = dPlannedCalendarEffort;
    }

    public int getStatus() {
    	return this.status;
    }
    public void setStatus(int iStatus) {
    	this.status = iStatus;
    }

    public String getStatusName() {
    	return this.statusName;
    }
    public void setStatusName(String strStatusName) {
    	this.statusName = strStatusName;
    }

    public long getScheduleStatus() {
    	return this.scheduleStatus;
    }
    public void setScheduleStatus(long lScheduleStatus) {
    	this.scheduleStatus = lScheduleStatus;
    }

    public long getEffortStatus() {
    	return this.effortStatus;
    }
    public void setEffortStatus(long lEffortStatus) {
    	this.effortStatus = lEffortStatus;
    }

    public Date getLastUpdate() {
    	return this.lastUpdate;
    }
    public void setLastUpdate(Date dtLastUpdate) {
    	this.lastUpdate = dtLastUpdate;
    }

    public String getCustomer() {
    	return this.customer;
    }
    public void setCustomer(String strCustomer) {
    	if (strCustomer == null  || strCustomer == "") {
    		this.customer = "";
    	} else {
    		this.customer = strCustomer;
    	}
    }

    public String getSecondCustomer(){
    	return this.secondCustomer;
    }
    public void setSecondCustomer(String strSecondCustomer) {
    	if (strSecondCustomer == null || strSecondCustomer == "") {
    		this.secondCustomer = "";
    	} else {
    		this.secondCustomer = strSecondCustomer;
    	}
    }
    
    public String getBusinessDomain() {
        return this.businessDomain;
    }
    public void setBusinessDomain(String strBusinessDomain) {
        if (strBusinessDomain == null || strBusinessDomain == "") {
            this.businessDomain = "";
        } else {
            this.businessDomain = strBusinessDomain;
        }
    }
    
    public String getApplicationType() {
        return this.applicationType;
    }
    public void setApplicationType(String strApplicationType) {
        if (strApplicationType == null) {
            this.applicationType = "";
        } else {
            this.applicationType = strApplicationType;
        }
    }

    public int getApplyPPM() {
    	return this.applyPPM;
    }
    public void setApplyPPM(int iApplyPPM) {
    	this.applyPPM = iApplyPPM;
    }

    public String getReason() {
    	return this.reason;
    }
	public void setReason(String strReason) {
		if (strReason == null) {
			this.reason = "";
		} else {
			this.reason = strReason;
		}
	}

	public double getTotalProductivityLoc() {
		return this.totalProductivityLoc;
	}
	public void setTotalProductivityLoc(double productivityLoc) {
		this.totalProductivityLoc = productivityLoc;
	}

	public double getTotalQualityLoc() {
		return this.totalQualityLoc;
	}
	public void setTotalQualityLoc(double qualityLoc) {
		this.totalQualityLoc = qualityLoc;
	}

    /**
     * Get durration metric, based on project planned dates, project information
     * should loaded (ie. called: ProjectInfo pinf = new ProjectInfo(prjId))
     * in order to call this function 
     * @return
     */
    public MetricInfo getDurationMetric() {
        MetricInfo durationMetric = new MetricInfo();
        try {
            // Code get from Project.getPerformanceMetrics()
            double targetDuration = Double.NaN;
            double reTargetDuration = Double.NaN;
            double plannedDuration = Double.NaN;
            java.util.Date today = new java.util.Date();
        
            if ((planStartDate != null) && (baseFinishDate != null)) {
                targetDuration =
                    CommonTools.dateDiff(planStartDate, baseFinishDate);
            }
            if ((planStartDate != null) && (planFinishDate != null)) {
                reTargetDuration =
                    CommonTools.dateDiff(planStartDate, planFinishDate);
            }
            plannedDuration =
                (reTargetDuration > 0) ? reTargetDuration : targetDuration;

            if (startDate != null) {
                durationMetric.actualValue =
                    CommonTools.dateDiff(startDate,
                        (actualFinishDate != null) ? actualFinishDate : today);
            }

            durationMetric.plannedValue = targetDuration;
            durationMetric.rePlannedValue = reTargetDuration;
            if ((plannedDuration > 0) && (!Double.isNaN(durationMetric.actualValue))) {
                durationMetric.deviation =
                    (durationMetric.actualValue - plannedDuration) * 100d / plannedDuration;
            }
            durationMetric.name = "Duration";
            durationMetric.unit = "Days";
            // Get norm
            NormInfo schedDeviationNorm = com.fms1.common.Norms.getNorm(
                projectId, MetricDescInfo.PROJECT_SCHEDULE_DEVIATION);
            durationMetric.lcl = schedDeviationNorm.lcl;
            durationMetric.ucl = schedDeviationNorm.ucl;
            durationMetric.colorType = schedDeviationNorm.colorType;
        } catch (NullPointerException e) {
            System.err.println("ProjectInfo.getDurationMetric(): project infomation may not loaded");
        } catch (Exception e) {
            System.err.println("ProjectInfo.getDurationMetric(): error found");
            e.printStackTrace();
        } finally {
            return durationMetric;
        }
    }
    /**
     * Convert Lifecycle type into Lifecycle string
     * @param type
     * @return String
     */
	public static String parseLifecycle(String type) {
		try {
			return parseLifecycle(Integer.parseInt(type));
		}
		catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	public static String parseLifecycle(int type) {
		String valReturn = "Error";
		switch (type) {
			case LIFECYCLE_DEVELOPMENT :
				valReturn = "Development";
				break;
			case LIFECYCLE_MAINTENANCE :
				valReturn = "Maintenance";
				break;
			case LIFECYCLE_OTHER :
				valReturn = "Others";
				break;
		}
		return valReturn;
	}
	/**
	 * Convert ProjectType(number: 0, 8) into ProjectType string 
	 * @param type
	 * @return String
	 */
	public static String parseType(String type) {
		String valReturn = "Error";
		try {
			int intType = Integer.parseInt(type);
			if (intType == 8)
				valReturn = INTERNAL;
			else if (intType == 0)
				valReturn = EXTERNAL;
			else
				valReturn = PUBLIC;	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return valReturn;
		}
	}
	public static String parseStatus(String status) {
		try {
			int intStatus = Integer.parseInt(status);
			return parseStatus(intStatus);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "N/A";
		}
	}
	public static String parseStatus(int status) {
		String valReturn = "Error";
		try {
			int intStatus = status;
			switch (intStatus) {
				case STATUS_ONGOING:
					valReturn = "On-going";
					break;
				case STATUS_TENTATIVE:
					valReturn = "Tentative";
					break;
				case STATUS_CLOSED:
					valReturn = "Closed";
					break;
				case STATUS_CANCELLED:
					valReturn = "Cancelled";
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return valReturn;
		}
	}
    /**
     * Get latest start date
     * @return
     */
    public java.sql.Date getLatestStartDate() {
        java.sql.Date dtStartDate;
        if (startDate != null) {
            dtStartDate = startDate;
		} else {
        	 dtStartDate = planStartDate;
        }
        return dtStartDate;
    }
    /**
     * Get latest finish date
     * @return
     */
    public java.sql.Date getLatestFinishDate() {
        java.sql.Date finishDate;
        if (actualFinishDate != null) {
            finishDate = actualFinishDate;
        } else if (plannedFinishDate != null) {
            finishDate = plannedFinishDate;
        } else if (planFinishDate != null) {
            finishDate = planFinishDate;
        } else {
            finishDate = baseFinishDate;
        }
        return finishDate;
    }
    /**
     * Get latest Billable Effort
     * @return
     */
    public double getLatestBillableEffort() {
        double effort = Double.NaN;
        if (!Double.isNaN(actualBillableEffort)) {
            effort = actualBillableEffort;
        } else if (!Double.isNaN(plannedBillableEffort)) {
            effort = plannedBillableEffort;
        } else if (!Double.isNaN(planBillableEffort)) {
            effort = planBillableEffort;
        } else {
            effort = baseBillableEffort;
        }
        return effort;
    }
	
    public void setAssignmentList(Collection list) {
		if (list != null) {
			this.AssignmentList = list;
		}
	}
	public Collection getAssignmentList() {
		return this.AssignmentList;
	}
	/**
	 * @return
	 */
	public int getTypeCustomer() {
		return typeCustomer;
	}

	/**
	 * @return
	 */
	public int getTypeCustomer2() {
		return typeCustomer2;
	}

	/**
	 * @param i
	 */
	public void setTypeCustomer(int i) {
		typeCustomer = i;
	}

	/**
	 * @param i
	 */
	public void setTypeCustomer2(int i) {
		typeCustomer2 = i;
	}

}