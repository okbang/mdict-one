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
 
 package com.fms1.web;
/**
 * Define different labels for different languages
 *
 */
public final class Language extends LangType {
	private static Language meENGLISH;
	private static Language meVIETNAMESE;
	private static Language meFRENCH;
	private static Language meRUSSIAN;
	private static Language meGERMAN;
	public String btnOk;
	public String btnCancel;
	public String btnBack;
	public String btnAddnew;
	public String btnUpdate;
	public String btnUpdatePlan;
	public String btnDelete;
	public String lblRole;
	public String lblDescription;
	public String lblName;
	public String titRoleName;
	public String ttlRisk;
	public String hdrProject;
	public String hdrNumPlanned;
	public String hdrNumOccurred;
	public String capRiskList;
	public String capRiskAdd;
	public String capRiskView;
	public String capRiskUpdate;
	public String colCondition;
	public String colConsequence;
	public String colProbability;
	public String colPlannedImpact;
	public String colImpactTo;
	public String colUnit;
	public String colEstImpact;
	public String colActImpact;
	public String colMitigation;
	public String colContingency;
	public String colTrigger;
	public String colDevID;
	public String colAssDate;
	public String colStatus;
	public String colActRisk;
	public String colActAction;
	public String colImpact;
	public String colExposure;
	public String colUnplanned;
	//UserProfiles
	public String userProfile_lblTitle;
	public String userProfile_cboCriteriaName;
	public String userProfile_cboCriteriaID;
	public String userProfile_btnSearch;
	public String userProfile_lblTableCaption;
	public String userProfile_lblColID;
	public String userProfile_lblColName;
	public String userProfile_lblColDesc;
	public String userProfile_lblColWU;
	public String userProfile_lblPage;
	public String userProfile_lblNext;
	public String userProfile_lblPrev;
	public String userProfile_msgError;
	//UserProfileMng
	public String userProfileMng_lblUserID;
	public String userProfileMng_lblOldPassword;
	public String userProfileMng_lblNewPassword;
	public String userProfileMng_lblPassword;
	public String userProfileMng_lblConfirmPassword;
	public String userProfileMng_lblUserName;
	public String userProfileMng_lblDescription;
	public String userProfileMng_lblWUName;
	public String userProfileMng_lblRoleName;
	public String userProfileMng_msgUserID;
	public String userProfileMng_msgOldPassword; 
	public String userProfileMng_msgNewPassword; 
	public String userProfileMng_msgConfirmPassword;
	public String userProfileMng_msgSpaceCharacterPassword;
	public String userProfileMng_msgSixCharacterPassword ;
	public String userProfileMng_msgPasswordDifferentUserID;
	public String userProfileMng_msgInvalidConfirmPassword;
	public String userProfileMng_msgInvalidPassword;	
	public String userProfileMng_msgPassword;
	public String userProfileMng_msgName;
	public String userProfileMng_msgWU;
	public String userProfileMng_msgWUAndRight;
	public String userProfileMng_msgAdmin;
	public String userProfileMng_msgDel;
	//UserProfileView
	public String userProfileView_lblTitle;
	public String userProfileView_lblTableCaption1;
	public String userProfileView_lblTableCaption2;
	//UserProfileUpdate
	public String userProfileUpdate_lblTitle;
	public String userProfileUpdate_lblTableCaption;
	//UserProfileADd
	public String userProfileAdd_lblTitle;
	public String userProfileAdd_lblTableCaption;
	public String userProfileAdd_lblErrorMsg;
	//tailoring
	public String tailoringHeader_lblProject;
	public String tailoringHeader_lblTotal;
	public String tailoringHeader_lblTotalTailoring;
	public String tailoringHeader_lblTotalDeviation;
	public String deviationtailoring_lblTitle;
	public String tailoring_lblTitle;
	public String deviation_lblTitle;
	public String tailoringAdd_lblTitle;
	public String tailoringUpdate_lblTitle;
	public String tailoring_lblColModification;
	public String tailoring_lblColAction;
	public String tailoring_lblReason;
	public String tailoring_lblType;
	public String tailoring_lblCategory;
	public String tailoring_lblNote;
	public String tailoring_msgMod;
	public String tailoring_msgModLength;
	public String tailoring_msgReason;
	public String tailoring_msgReasonLength;
	public String tailoring_msgNoteLength;
	public String tailoring_msgCat;
	public String clmnDescription;
	public String clmnName;
	public String msgNoRightWithPage;
	public String ttlRole;
	public String tblCapRole;
	public String tblCapRoleDetails;
	public String tblCapRoleUpdate;
	public String tblCapAddRole;
	public String clmnRoleInfo;
	public String clmnRoleMng;
	public String lblMode;
	public String lblMode1;
	public String lblMode2;
	public String lblMode3;
	public String lblPage;
	public String msgAddRoleName;
	public String errAddRole;
	public String ttlWU;
	public String tblCapWU;
	public String clmnType;
	public String lblNext;
	public String lblPrev;
	public String clmnParentWU;
	public String clmnWUId;
	public String tblCapWUAddnew;
	public String tblCapWUDetails;
	public String tblCapWUUpdate;
	public String errAddWUID;
	public String errAddWUName;
	public String errUpdateWUName;
	public String msgRoleName;
	public String msgWUName;
	public String msgWUID;
	public String msgWUDelete;
	public String msgRoleDelete;
	public String ttlPractice;
	public String tblCapPrac;
	public String clmnNumber;
	public String clmnScenario;
	public String clmnPractice;
	public String clmnCategory;
	public String clmnPracID;
	public String errAddPractice;
	public String tblCapAdd;
	public String tblCapDetails;
	public String tblCapUpdate;
	public String msgPracticeID;
	public String msgPracticeDelete;
	public String msgPracIDNum;
	public String lblProject;
	public String msgPracScen;
	public String msgPracPrac;
	public String tblCapList;
	public String colWUName;
	public String msgPracScen1;
	public String msgPracPrac1;
	public final String processEff = "Process effort";
	public final String processEffByStage = "Process effort for each stage";
	public final String ttlTraining = "Training plan";
	public final String tblCapTrain = "Training plan list";
	public final String tblCapTrainAdd = "Add new training plan";
	public final String tblCapTrainDetails = "Training plan details";
	public final String tblCapTrainUpdate = "Training plan update ";
	public final String clmnParticipant = "Participants";
	public final String clmnWaiver = "Waiver";
	public final String clmnTopic = "Topic";
	public final String clmnEndD = "Planned end date";
	public final String clmnStartD = "Planned start date";
	public final String clmnActualD = "Actual end date";
	public final String errAddTraining = "";
	public final String msgTrainingMandatory = "You must input in mandatory fields";
	public final String msgInvalidDate = "Invalid date format!";
	public String msgInvalidNumber;
	public final String msgGr2 = "Length of this field must be less than 200!";
	public final String msgGr5 = "Length of this field must be less than 500!";
	public final String msgGr50 = "Length of this field must be less than 50!";
	public final String ttlTool = "Tools and Infrastructures";
	public final String tblCapTool = "Tools and infrastructures list";
	public final String tblCapToolAdd = "Add new Tool and infrastructure";
	public final String tblCapToolDetails = "Tool and infrastructure details";
	public final String tblCapToolUpdate = "Update tool and infrastructure";
	public final String errTool = "You made error: Due date must be between start date and base finish date !";
	public final String msgToolDate =
		"Due date must be between planned start date and (re)planned finish date of the project!";
	public final String clmnActivity = "Activity";
	public final String clmnEffort = "Effort";
	public final String clmnCost = "Project cost";
	public final String clmnDueD = "Due date";
	public final String clmnValue = "Value";
	public final String clmnCondition = "Conditions";
	public final String clmnItem = "Item";
	public final String clmnPurpose = "Purpose";
	public final String clmnSource = "Source";
	public final String clmnNote = "Note";
	public final String clmnStatus = "Status";
	public final String ttlFinance = "Finance";
	public final String tblCapFinanPlan = "Financial plan list";
	public final String tblCapPrjCost = "Project cost";
	public final String tblCapTotalCost = "Totals for project cost";
	public final String tblCapFinanUpp = "Update financial plan ";
	public final String tblCapFinanDetails = "Financial plan details";
	public final String tblCapFinanAdd = "Add new financial plan";
	public final String tblCapCostAdd = "Add new project cost";
	public final String tblCapCostUpp = "Update project cost ";
	public final String tblCapCostDetails = "Project cost details";
	public final String clmnTotalEffort = "Total effort";
	public final String clmnTotalLabourCost = "Total labour cost";
	public final String clmnTotalNonLabourCost = "Total non-labour cost";
	public final String clmnTotalBudget = "Total budget";
	public final String clmnTotal = "Total Cost";
	public final String clmnAmount = "Amount";
	public final String errTrain =
		"Start date and end date of training must be between start date and base finish date of project!";
	public final String msgTrainDate =
		"Start date and end date of training must be between start date and base finish date of project!";
	public final String msgDelete = "Are you sure to delete ?";
	public final String msgNaN = "Invalid number format";
	public final String msgMandatory = "This field is mandatory!";
	public final String msgPositive = "Must be greater than 0";
	public final String msgStartEndD = "Start date must be before end date!";
	public final String ttlObjective = "Quality";
	public final String tblCapReviewTest = "Review and test activities list";
	public final String tblCapReviewUpdate = "Update review and test activity";
	public final String tblCapReviewDetails = "Review and test activity details";
	public final String ttlReviewTest = "Review and test activities";
	public final String clmnModunName = "Product";
	public final String clmnWP = "Work product";
	public final String lblWP = "work product";
	public final String clmnConductor = "Conductor";
	public final String clmnReviewer = "Reviewer(s)";
	public final String clmnApprover = "Approver(s)";
	public final String clmnStage = "Stage"; //"&nbsp;Stage&nbsp;"
	public final String clmnPReviewD = "Planned review date";
	public final String clmnAReviewD = "Actual review date";
	public final String clmnPReleaseD = "Planned release date";
	public final String clmnAReleaseD = "Actual release date";
	public final String clmnATestD = "Actual test end date";
	public final String clmnPTestD = "Planned test end date";
	public final String clmnIsTest = "Tested on time ?";
	public final String clmnIsRelease = "Released on time ?";
	public final String clmnIsReview = "Reviewed on time ?";
	public final String clmnDeviation = "Schedule deviation";
	public final String msgModunPReviewD =
		"Planned review date must be between planned start date and planned end date of the project!";
	public final String msgModunPTestD =
		"Planned test date must be between planned start date and planned end date of the project!";
	public final String msgModunPReleaseD =
		"Planned release date must be between planned test date and planned end date of the project!";
	public final String clmnObjective = "Strategy to achieve the quality objectives";
	public final String tblCapObjectiveUpdate = "Update strategy";
	public final String tblCapOtherActDetails = "Quality activity details";
	public final String tblCapOtherActUpdate = "Update quality activity";
	public final String tblCapOtherActAdd = "Add new quality activity";
	public final String tblCapOtherActList = "Other quality activity list";
	public final String ttlOtherAct = "Other quality activities";
	//public String clmnActivity="Activity";
	public final String clmnPStartD = "Planned start date";
	public final String clmnPEndD = "Planned end date";
	public String clmnRPEndD;
	public final String clmnAEndD = "Actual end date";
	public final String clmnAStartD = "Actual start date";
	public final String msgPStartD =
		"Planned start date must be between planned start date and (re)planned end date of project!";
	public final String msgpEndD =
		"Planned end date must be between planned start date of activity and (re)planned end date of project!";
	//Schedule
	public final String clmnTimeliness = "Timeliness";
	public final String clmnRemainD = "Remaining days";
	public final String clmnProject = "Project";
	public final String clmnStageScheduleDev = "Stage schedule deviation";
	public final String clmnDeliveryScheduleDev = "Delivery schedule deviation";
	public final String tblMajorInfo = "Major infomation";
    public final String errAddDar = "You can't add new Dar, because team list is empty";
    public final String errUpdateDar = "You can't update Dar, because team list is empty" ;
	public String ttlIssueLst;
	public String lblIssueLstWUName;
	public String capIssue;
	public String clmnIssueDescription;
	public String clmnIssueStatus;
	public String clmnIssuePriority;
	public String clmnIssueOwner;
	public String clmnIssueDueDate;
	public String lblIssuePage;
	public String btnIssuePrev;
	public String btnIssueNext;
	public String ttlIssueAdd;
	public String ttlIssueUpd;
	public String ttlIssueDet;
	public String clmnIssueType;
	public String clmnIssueCreator;
	public String clmnIssueCreatedDate;
	public String clmnIssueComment;
	public String clmnIssueClosedDate;
	public String clmnIssueReference;
	public String errNoRight;
	public String msgIssueDescEmpty;
	public String msgIssueDescLonger;
	public String msgIssueOwnerEmpty;
	public String msgIssueCreatorEmpty;
	public String msgIssueCreatedInvalid;
	public String msgIssueDueEmpty;
	public String msgIssueDueInvalid;
	public String msgIssueDueAfterStart;
	public String msgIssueCommentLonger;
	public String msgIssueClosedInvalid;
	public String msgIssueClosedAfterCreated;
	public String msgIssueClosedBeforeToday;
	public String msgIssueReferenceEmpty;
	public String ttlWelcome;
	public String ttlOrgHome;
	public String ttlPrjHome;
	public String ttlGrpHome;
	public String ttlAdmHome;
	public String capOrgSel;
	public String capPrjSel;
	public String capGrpSel;
	public String capAdmSel;
	public String colOrgID;
	public String colOrgName;
	public String colPrjID;
	public String colPrjName;
	public String colGrpID;
	public String colGrpName;
	public String colAdmID;
	public String colAdmName;
	public String colUserStatus;
	public String colUserDesignation;
	public String colToolsRole;
    public String emailUser;
    public String userStartDate;
    public String userQuitDate;
    public String cIRegisterTitle;
	public String clmnBaseline;
	public String updateCIRegister;
	public String errorLength50;
	public String noLink;
	public String noSpecialCharacter;
	public String nonAvailable;
	public String ProjectPlan;
	public String workOrder;
	public String changes;
	public String ttlDependency;
	public String ttlTeam;
	public String effortComment;
	public String ttlOverview;
	public String ttlDevDep;
	public String ttlLifecycle;
	public String ttlOrganization;
	public String ttlSignatures;
	public String clnmMilestone;
	public String nbIterations;
	public String nameAlreadyExist;
	public String msgBigText;
	public String msgMediumText;
	public String msgMediumText100;
	public String msgMediumText400;
	public String clmnVerifyBy;
	public String ttlSchedule;
	public String ttlSubcontract;
	public String ttlMetrics;
	public String ttlPerformance;
	public String ttlMetric;
	public String colPlanned;
	public String colActual;
	public String colDeviation;
	public String lblTestEffectiveness;
	public String lblOpenDefects;
	public String lblRemovalEfficiency;
	public String lblOpenWeigthedDefects;
	public String lblReviewEfficiency;
	public String lblTestEfficiency;
	public String lblTotalDefects;
	public String lblTotalWeightedDefects;
	public String lblEstimatedDefects;
	public String lblDefectRate;
	public String lblReEstimatedDefects;
	public String lblLeakage;
	public String lblToRemove;
	public String lblPossibleLeakage;
	public String lblReviewEffectiveness;
	public String capPlanDefReview;
	public String capPlanDefTest;
	public String colProcess;
	public String colRePlanned;
	public String colNorm;
	public String colPlannedFoundByReview;
	public String colRePlannedFoundByReview;
	public String colPlannedFoundByTest;
	public String colRePlannedFoundByTest;
	public String colActualFoundByTest;
	public String colActualFoundByReview;
	public String colReviewDeviation;
	public String colTestDeviation;
	public String colNormTest;
	public String colNormReview;
	public String msgPlanRePlan;
	public String msgDetailAbovePlanned;
	public String lblModule;
	public String msgModuleWP1;
	public String msgModuleWP2;
	public String ttlDefects;
	public String ttlPlannedDefects;
	public String ttlPlanDefectByProduct;
	public String ttlReplanDefectByProduct;
	public String ttlReplanDefectRate;
	public String msgDeleteGal;
	public String ttlAssumption;
	public String ttlContraint;
    public String btnUseForecast;
    public String defects_btnPlanDefect;
    public String btnReplan;
    public String ttlPlanDREByQcStage;
    public String ttlPlanDREPlanDefect;
    public String ttlPlanDREReplanDefect;
    public String ttlPlanDREDefectRate;
    public String ttlPlanDRELeakage;
	
	//be carefull to use instance() instead of constructor
	private Language(final int type) {
		switch (type) {
			case ENGLISH :

				btnOk = "OK";
				btnCancel = "Cancel";
				btnBack = "Back";
				btnAddnew = "Add new";
				btnUpdate = "Update";
				btnUpdatePlan = "Update plan";
				btnDelete = "Delete";
				//UserProfiles
				userProfile_lblTitle = "User Profiles";
				userProfile_cboCriteriaName = "Name";
				userProfile_cboCriteriaID = "ID";
				userProfile_btnSearch = "Search";
				userProfile_lblTableCaption = "User Profiles";
				userProfile_lblColID = "ID";
				userProfile_lblColName = "User Name";
				userProfile_lblColDesc = "Description";
				userProfile_lblColWU = "Work units";
				userProfile_lblPage = "Page";
				userProfile_lblNext = "Next";
				userProfile_lblPrev = "Prev";
				userProfile_msgError = "Sorry, access denied";
				//end UserProfiles
				//UserProfileMng
				userProfileMng_lblUserID = "User ID";
				userProfileMng_lblPassword = "Password";
				userProfileMng_lblOldPassword = "Old Password";
				userProfileMng_lblNewPassword = "New Password";
				userProfileMng_lblConfirmPassword ="Confirm Password";				
				userProfileMng_lblUserName = "User name";
				userProfileMng_lblDescription = "Description";
				userProfileMng_lblWUName = "Work unit name";
				userProfileMng_lblRoleName = "Role name";
				userProfileMng_msgUserID = "You must enter UserID";
				userProfileMng_msgPassword = "You must enter password";
				userProfileMng_msgOldPassword = "You must enter old password";
				userProfileMng_msgNewPassword = "You must enter new password";
				userProfileMng_msgConfirmPassword = "You must enter confirm password";
				userProfileMng_msgSpaceCharacterPassword ="The password is not include space character";
				userProfileMng_msgSixCharacterPassword ="The password must be at least 6 Characters";
				userProfileMng_msgPasswordDifferentUserID="The new password must be different from user ID";
				userProfileMng_msgInvalidConfirmPassword="The password was not correctly confirmed";
				userProfileMng_msgInvalidPassword="The passwords you typed do not match";
				userProfileMng_msgName = "You must enter user name";
				userProfileMng_msgWU = "You cannot chose 2 similar Work unit!";
				userProfileMng_msgWUAndRight = "You must choose one Work unit coresponding to one Right group!";
				userProfileMng_msgAdmin = "It is imposible to grant the admin right for any user!";
				userProfileMng_msgDel = "Are you sure?";
				//end UserProfileView, UserProfileUpdate, UserProfileAddnew
				//UserProfileView
				userProfileView_lblTitle = "User Profile Detail";
				userProfileView_lblTableCaption1 = "User profile detail";
				userProfileView_lblTableCaption2 = "Right group for user by work unit";
				//end UserProfileView
				//UserProfileUpdate
				userProfileUpdate_lblTitle = "Update User Profile";
				userProfileUpdate_lblTableCaption = "Update user profile";
				//end UserProfileUPdate
				//UserProfileADd
				userProfileAdd_lblTitle = "Add User Profile";
				userProfileAdd_lblTableCaption = "User information";
				userProfileAdd_lblErrorMsg = "You made Error: You have choosen an existing userID";
				//end UserProfileAdd
				//tailoring
				tailoringHeader_lblProject = "Project";
				tailoringHeader_lblTotal = "Number of tailoring/deviation";
				tailoringHeader_lblTotalTailoring = "Number of tailoring";
				tailoringHeader_lblTotalDeviation = "Number of deviation";
				deviationtailoring_lblTitle = "Tailoring&Deviation";
				tailoring_lblTitle = "Tailoring";
				deviation_lblTitle = "Deviation";
				tailoringAdd_lblTitle = "Add Tailoring & Deviation";
				tailoringUpdate_lblTitle = "Update Tailoring & Deviation";
				tailoring_lblColModification = "Modification";
				tailoring_lblColAction = "Action";
				tailoring_lblReason = "Reason";
				tailoring_lblType = "Type";
				tailoring_lblCategory = "Category";
				tailoring_lblNote = "Note";
				tailoring_msgMod = "You must enter modification";
				tailoring_msgModLength = "The text for modification cannot be more than 200 characters";
				tailoring_msgReason = "You must enter reason";
				tailoring_msgReasonLength = "The text for reason cannot be more than 200 characters";
				tailoring_msgNoteLength = "The text for note cannot be more than 200 characters";
				tailoring_msgCat = "You must enter the category";
				//end tailoring
				clmnDescription = "Description";
				clmnName = "Role name";
				msgNoRightWithPage = "You have no right with this page!";
				clmnRoleInfo = "Role information";
				clmnRoleMng = "Role management";
				lblMode = "Mode";
				lblMode1 = "No right";
				lblMode2 = "View only right";
				lblMode3 = "Manager right";
				lblPage = "Page";
				msgAddRoleName = "You must input role name!";
				ttlRole = "Roles";
				tblCapRole = "Role List";
				tblCapAddRole = "Add new role";
				tblCapRoleDetails = "Role details";
				tblCapRoleUpdate = "Update role";
				errAddWUID = "Can't add this role: ID exists !";
				errAddWUName = "Can't update this work unit: name exists !";
				errUpdateWUName = "Can't update this role: name exists !";
				clmnType = "Type";
				lblNext = "Next";
				lblPrev = "Prev";
				clmnParentWU = "Parent work unit name";
				clmnWUId = "Work unit ID";
				ttlWU = "Work units ";
				tblCapWU = "Work unit list";
				tblCapWUAddnew = "Add new work unit";
				tblCapWUDetails = "Work unit Details";
				tblCapWUUpdate = "Update work unit";
				msgRoleName = "You must input Role name !";
				msgWUName = "You must input Work unit name !";
				msgWUID = "You must input Work unit ID !";
				msgWUDelete = "Are you sure to delete this Work unit ?";
				msgRoleDelete = "Are you sure to delete this Role ?";
				tblCapPrac = "Practices";
				clmnNumber = "#";
				clmnScenario = "Scenario/Problem";
				clmnPractice = "Practice/Lesson/Suggestion";
				clmnCategory = "Category";
				clmnPracID = "Practice ID";
				errAddPractice = "You made error. This Practice ID exists !";
				ttlPractice = "Practices And Lessons";
				tblCapAdd = "Add new practice and lesson";
				tblCapDetails = "Practice and lesson details.";
				tblCapUpdate = "Update practice and lesson";
				tblCapList = "Practice and lesson list";
				msgPracticeID = "You must input Practice ID !";
				msgPracticeDelete = "Are you sure to delete this Practice ?";
				msgPracIDNum = "Practice ID must be an integer!";
				lblProject = "Project ";
				colWUName = "Work Unit Name ";
				msgPracScen = "Max leng for Scenario is 500 !";
				msgPracPrac = "Max leng for Practice is 500 !";
				msgPracScen1 = "You must input Scenario !";
				msgPracPrac1 = "You must input Practice !";
				ttlIssueLst = "Issues";
				lblIssueLstWUName = "Work Unit Name: ";
				capIssue = "Issues";
				clmnIssueDescription = "Description";
				clmnIssueStatus = "Status";
				clmnIssuePriority = "Priority";
				clmnIssueOwner = "Owner";
				clmnIssueDueDate = "Due Date";
				lblIssuePage = "Page: ";
				btnIssuePrev = "Prev";
				btnIssueNext = "Next";
				ttlIssueAdd = "Add Issue";
				ttlIssueUpd = "Update Issue";
				ttlIssueDet = "Issue Detail";
				clmnIssueType = "Type";
				clmnIssueCreator = "Creator";
				clmnIssueCreatedDate = "Created Date";
				clmnIssueComment = "Comment/Solution";
				clmnIssueClosedDate = "Closed Date";
				clmnIssueReference = "Reference";
				errNoRight = "You have no right in this page!";
				msgIssueDescEmpty = "Description cannot be empty!";
				msgIssueDescLonger = "Description length cannot be longer than 200 characters!";
				msgIssueOwnerEmpty = "Owner cannot not be empty!";
				msgIssueCreatorEmpty = "Creator cannot not be empty!";
				msgIssueCreatedInvalid = "Created date is invalid!";
				msgIssueDueEmpty = "Due date cannot be empty!";
				msgIssueDueInvalid = "Due date is invalid!";
				msgIssueDueAfterStart = "Due date must be after created date!";
				msgIssueCommentLonger = "Comment length cannot be longer than 200 characters!";
				msgBigText = "This field's maximum size is 4000 characters! Currently :";
				msgIssueClosedInvalid = "Closed date is invalid!";
				msgIssueClosedAfterCreated = "Closed date must be after created date!";
				msgIssueClosedBeforeToday = "Closed date must be today or before!";
				msgIssueReferenceEmpty = "Reference length cannot be longer than 50 characters!";
				ttlWelcome = "Welcome to ";
				ttlOrgHome = "Organization Home";
				ttlPrjHome = "Project Home";
				ttlGrpHome = "Group Home";
				ttlAdmHome = "Admin Home";
				capOrgSel = "Please select an organization";
				capPrjSel = "Please select a project";
				capGrpSel = "Please select a group";
				capAdmSel = "Please select an administrator";
				colOrgID = "Organization ID";
				colOrgName = "Organization Name";
				colPrjID = "Project ID";
				colPrjName = "Project Name";
				colGrpID = "Group ID";
				colGrpName = "Group Name";
				colAdmID = "Admin ID";
				colAdmName = "Admin Name";
				colUserStatus = "Status";
				colUserDesignation = "Position";
				colToolsRole = "Tools role";
                emailUser = "Email";
                userStartDate = "Start date";
                userQuitDate = "Quit date";
				cIRegisterTitle = "C.I. Register";
				updateCIRegister = "Update C.I. Register";
				clmnBaseline = "Baseline";
				errorLength50 = "The length of this field must be 50 characters max.";
				noLink = "Please use the application to access to this page";
				noSpecialCharacter = "Please do not use '<'";
				nonAvailable = "N/A";
				ProjectPlan = "Project plan: ";
				workOrder = "Work Order: ";
				changes = "Changes";
				ttlDependency = "Critical Dependencies";
				ttlTeam = "Team";
				ttlRisk = "Risks";
				hdrProject = "Project: ";
				hdrNumPlanned = "Number of planned risks ";
				hdrNumOccurred = "Number of occurred risks ";
				capRiskList = "Risks";
				capRiskAdd = "Add new risk";
				capRiskView = "Risk details";
				capRiskUpdate = "Update risk";
				colCondition = "Condition";
				colConsequence = "Consequence";
				colProbability = "Probability";
				colPlannedImpact = "Estimated impact";
				colImpactTo = "Impact to";
				colUnit = "Unit";
				colEstImpact = "Estimated impact";
				colImpact="Impact";
				colMitigation = "Mitigation";
				colContingency = "Contingency";
				colTrigger = "Trigger";
				colDevID = "Assignee";
				colAssDate = "Last Assessed date";
				colStatus = "Status";
				colActRisk = "Actual risk scenario";
				colActAction = "Actual action";
				colActImpact = "Actual impact";
				colExposure = "Exposure";
				colUnplanned = "Unplanned";
				effortComment = "Unless specified, the unit for effort metrics is person.day";
				clmnRPEndD = "Re-planned end date";
				ttlOverview = "Overview";
				ttlDevDep = "Deliverables and Dependencies";
				ttlLifecycle = "Lifecyle";
				ttlOrganization = "Organization";
				ttlSignatures = "Signatures";
				clnmMilestone = "Milestone";
				nbIterations = "Number of iterations";
				msgInvalidNumber = "Invalid number";
				nameAlreadyExist = " already exists, please choose another name.";
				msgMediumText = "The length of this field must be below 200 characters, currently :";
				msgMediumText100 = "The length of this field must be below 100 characters, currently :";
				msgMediumText400 = "The length of this field must be below 400 characters, currently :";
				clmnVerifyBy = "Verify by";
				ttlSchedule = "Schedule: ";
				ttlSubcontract = "Subcontract";
				ttlMetrics = "Metrics";
				ttlMetric = "Metric";
				ttlPerformance = "Performance";
				colPlanned = "Planned";
				colRePlanned = "Re-planned";
				colActual = "Actual";
				colDeviation = "Deviation";
				//defects
				lblTestEffectiveness = "Test effectiveness";
				lblReviewEffectiveness = "Review effectiveness";
				lblOpenDefects = "Open defects";
				lblRemovalEfficiency = "Defect removal efficiency";
				lblOpenWeigthedDefects = "Open weighted defects";
				lblReviewEfficiency = "Review efficiency";
				lblTestEfficiency = "Test efficiency";
				lblTotalDefects = "Total defects";
				lblTotalWeightedDefects = "Total weighted defects";
				lblEstimatedDefects = "Estimated defects";
				lblDefectRate = "Defect rate";
				lblReEstimatedDefects = "Re-estimated defects";
				lblLeakage = "Leakage";
				lblToRemove = "To be removed defects";
				lblPossibleLeakage = "Possible leakage";
				capPlanDefReview= "Weighted pre-release review defects by process";
				capPlanDefTest= "Weighted pre-release test defects by process";
				colProcess= "Process";
				colNormTest="Norm of wd found by test";
				colNormReview="Norm of wd found by review";
				colPlannedFoundByReview="Planned found by review";
				colRePlannedFoundByReview="Re-planned found by review";
				colPlannedFoundByTest="Planned found by test";
				colRePlannedFoundByTest="Re-planned found by test";
				colActualFoundByTest="Actual found by test";
				colActualFoundByReview="Actual found by review";
				colReviewDeviation="Review deviation";
				colTestDeviation="Test deviation";
				msgPlanRePlan="Planned value is mandatory when re-planned value is defined";
				msgDetailAbovePlanned="The sum of defect by activity must be equal or below (Re)planned defects";
				lblModule="Product";
				msgModuleWP1="In order to plan defects for product";
				msgModuleWP2="\n please plan first for defects of work product";
				ttlDefects="Defects";
				ttlPlannedDefects="Planned defect listing";
				ttlPlanDefectByProduct="Plan defect by product";
				ttlReplanDefectByProduct="Replan defect by product";
				ttlReplanDefectRate="Replan defect rate";
				msgDeleteGal="Are you sure to delete this item?";
				ttlAssumption="Assumption";
				ttlContraint="Constraint";
	            btnUseForecast="Use forecast";
                defects_btnPlanDefect="Plan Defect";
                btnReplan="Replan";
                ttlPlanDREByQcStage="Plan Defect Removal Efficiency";
                ttlPlanDREPlanDefect="Plan Defect By Stage";
                ttlPlanDREReplanDefect="Replan Defect";
                ttlPlanDREDefectRate="Defect Rate";
                ttlPlanDRELeakage="Leakage";
				break;
			case VIETNAMESE :
				colWUName = "Tên &#272;&#417;n v&#7883;";
				btnOk = "&#272;&#7891;ng ?";
				btnCancel = "B&#7887; qua";
				btnBack = "Quay l&#7841;i";
				btnAddnew = "Thêm m&#7899;i";
				btnUpdate = "C&#7853;p nh&#7853;t";
				btnDelete = "Xo?b&#7887;";
				lblRole = "Vai tr?";
				lblDescription = "M?t&#7843;";
				lblName = "Tên";
				titRoleName = "Danh sách các vai tr?hi&#7879;n c? ";
				msgNoRightWithPage = "B&#7841;n không c?quy&#7873;n trên trang này.";
				clmnDescription = "M?t&#7843;";
				clmnName = "Tên";
				msgNoRightWithPage = "B&#7841;n không c?quy&#7873;n trên trang này!";
				clmnRoleInfo = "Thông tin";
				clmnRoleMng = "C&#7845;p quy&#7873;n";
				lblMode = "Quy&#7873;n";
				lblMode1 = "Không c?quy&#7873;n";
				lblMode2 = "Ch&#7881; &#273;&#432;&#7907;c phép xem";
				lblMode3 = "Quy&#7873;n &#273;&#7847;y &#273;&#7911;";
				lblPage = "Trang";
				msgAddRoleName = "B¹n ph¶i nhËp tªn ®· !";
				errAddRole =
					"B&#7841;n không th&#7875; c&#7853;p nh&#7853;t nhóm quy&#7873;n này &#273;&#432;&#7907;c. &#272;?t&#7891;n m?nhóm quy&#7873;n nh&#432; v&#7853;y&nbsp; !";
				ttlRole = "Các nhóm quy&#7873;n.";
				tblCapRole = "Nhóm quy&#7873;n";
				tblCapAddRole = "Thêm m&#7897;t nhóm quy&#7873;n m&#7899;i.";
				tblCapRoleDetails = "Thông tin c&#7911;a nhóm quy&#7873;n.";
				tblCapRoleUpdate = "C&#7853;p nh&#7853;t nhóm quy&#7873;n.";
				clmnType = "Lo&#7841;i";
				lblNext = "Trang ti&#7871;p";
				lblPrev = "Trang tr&#432;&#7899;c";
				clmnParentWU = "Work unit c&#7845;p trên";
				clmnWUId = "M?Work unit";
				ttlWU = "Các Work unit";
				tblCapWU = "Work unit";
				tblCapWUAddnew = "Thêm m&#7897;t Work unit";
				tblCapWUDetails = "Thông tin Work unit";
				tblCapWUUpdate = "C&#7853;p nh&#7853;t Work unit";
				errAddWUName =
					"B&#7841;n không th&#7875; thêm Work unit này &#273;&#432;&#7907;c. &#272;?t&#7891;n t&#7841;i tên&nbsp; !";
				errAddWUID =
					"B&#7841;n không th&#7875; c&#7853;p nh&#7853;t Work unit này &#273;&#432;&#7907;c. &#272;?t&#7891;n m?Work unit nh&#432; v&#7853;y&nbsp; !";
				errUpdateWUName =
					"B&#7841;n không th&#7875; c&#7853;p nh&#7853;t Work unit này &#273;&#432;&#7907;c. &#272;?t&#7891;n t&#7841;i tên&nbsp; ! ";
				msgWUName = "Ban phai nhap ten cua Work unit !";
				msgWUID = "Ban phai nhap ma cua Work unit !";
				msgWUDelete = "Ban co chac chan xoa Work unit nay khong ?";
				msgRoleDelete = "Ban co chac chan xoa nhom quyen nay khong ?";
				msgRoleName = "Ban phai nhap ten cua nhom quyen!";
				clmnNumber = "STT";
				clmnScenario = "V&#7845;n &#273;&#7873; g&#7863;p ph&#7843;i";
				clmnPractice = "G&#7907;i ? gi&#7843;i quy&#7871;t";
				clmnCategory = "Phân lo&#7841;i";
				clmnPracID = "M?";
				errAddPractice = "Không th&#7875; thêm m&#7909;c này, &#273;?c?m?!";
				ttlPractice = "Danh m&#7909;c các bài h&#7885;c.";
				tblCapPrac = "V&#7845;n &#273;&#7873;";
				tblCapAdd = "Thêm m&#7899;i";
				tblCapDetails = "Chi ti&#7871;t c&#7909; th&#7875;";
				tblCapUpdate = "C&#7853;p nh&#7853;t v&#7845;n &#273;&#7873;";
				msgPracticeID = "Ban phai nhap ma bai toan !";
				msgPracticeDelete = "Ban co chac chan xoa bai toan nay khong ?";
				msgPracIDNum = "Ma bai toan phai la so nguyen!";
				lblProject = "D&#7921; án ";
				tblCapList = "Danh sách";
				msgPracScen = "Tong chieu dai cua Scenario phai nho hon 500 !";
				msgPracPrac = "Tong chieu dai cua Practice phai nho hon 500 !";
				msgPracScen1 = "Ban phai nhap Scenario !";
				msgPracPrac1 = "Ban phai nhap Practice !";
				//cuongph---------------------------------------------------------
				//UserProfiles
				userProfile_lblTitle = "Thông tin ng&#432;&#7901;i dùng";
				userProfile_cboCriteriaName = "Tên";
				userProfile_cboCriteriaID = "M?";
				userProfile_btnSearch = "T?";
				userProfile_lblTableCaption = "Thông tin ng&#432;&#7901;i dùng";
				userProfile_lblColID = "M?";
				userProfile_lblColName = "Tên ng&#432;&#7901;i dùng";
				userProfile_lblColDesc = "Ghi ch?";
				userProfile_lblColWU = "&#272;&#417;n v&#7883;";
				userProfile_lblPage = "Trang";
				userProfile_lblNext = "Trang sau";
				userProfile_lblPrev = "Trang tr&#432;&#7899;c";
				userProfile_msgError = "B&#7841;n không c?quy&#7873;n trong trang này";
				//end UserProfiles
				//UserProfileMng
				userProfileMng_lblUserID = "M?ng&#432;&#7901;i dùng*";
				userProfileMng_lblPassword = "M&#7853;t kh&#7849;u*";
				userProfileMng_lblUserName = "Tên ng&#432;&#7901;i dùng*";
				userProfileMng_lblDescription = "Ghi ch?";
				userProfileMng_lblWUName = "Tên &#273;&#417;n v&#7883;";
				userProfileMng_lblRoleName = "Tên nhóm quy&#7873;n";
				userProfileMng_msgUserID = "Ban phai nhap ma nguoi dung";
				userProfileMng_msgPassword = "Ban phai nhap mat khau";
				userProfileMng_msgName = "Ban phai nhap ten nguoi dung";
				userProfileMng_msgWU = "Ban  khong duoc chon hai don vi giong nhau";
				userProfileMng_msgWUAndRight = "Ban phai chon mot don vi tuong ung voi mot nhom quyen";
				userProfileMng_msgAdmin = "Khong duoc gan quyen quan tri cho bat ky ai!";
				userProfileMng_msgDel = "Ban co chac chan khong?";
				//end UserProfileView, UserProfileUpdate, UserProfileAddnew
				//UserProfileView
				userProfileView_lblTitle = "Chi ti&#7871;t thông tin ng&#432;&#7901;i dùng";
				userProfileView_lblTableCaption1 = "Thông tin";
				userProfileView_lblTableCaption2 =
					"Nhóm quy&#7873;n c&#7911;a ng&#432;&#7901;i dùng theo &#273;&#417;n v&#7883;";
				//end UserProfileView
				//UserProfileUpdate
				userProfileUpdate_lblTitle = "C&#7853;p nh&#7853;t thông tin ng&#432;&#7901;i dùng";
				userProfileUpdate_lblTableCaption = "C&#7853;p nh&#7853;t thông tin ng&#432;&#7901;i dùng";
				//end UserProfileUPdate
				//UserProfileADd
				userProfileAdd_lblTitle = "Thêm ng&#432;&#7901;i dùng";
				userProfileAdd_lblTableCaption = "Thông tin ng&#432;&#7901;i dùng";
				userProfileAdd_lblErrorMsg = "M?s&#7889; b&#7841;n ch&#7885;n &#273;?t&#7891;n t&#7841;i";
				//end UserProfileAdd
				//tailoring
				tailoringHeader_lblProject = "D&#7921; án";
				tailoringHeader_lblTotal = "S&#7889; l&#432;&#7907;ng s&#7917;a &#273;&#7893;i/khác bi&#7879;t";
				tailoringHeader_lblTotalTailoring = "S&#7889; l&#432;&#7907;ng s&#7917;a &#273;&#7893;i";
				tailoringHeader_lblTotalDeviation = "S&#7889; l&#432;&#7907;ng khác bi&#7879;t";
				tailoring_lblTitle = "S&#7917;a &#273;&#7893;i &amp; khác bi&#7879;t";
				tailoringAdd_lblTitle = "Thêm s&#7917;a &#273;&#7893;i &amp; khác bi&#7879;t";
				tailoringUpdate_lblTitle = "Qu&#7843;n l? s&#7917;a &#273;&#7893;i &amp; khác bi&#7879;t";
				tailoring_lblColModification = "Giai &#273;o&#7841;n/Qu?tr?h/Tài li&#7879;u";
				tailoring_lblColAction = "Thêm/S&#7917;a/Xo?";
				tailoring_lblReason = "L? do ch&#7881;nh s&#7917;a/khác bi&#7879;t";
				tailoring_lblType = "Ki&#7875;u";
				tailoring_lblCategory = "Ch&#7911;ng lo&#7841;i";
				tailoring_lblNote = "Ghi ch?";
				tailoring_msgMod = "Ban phai nhap sua doi";
				tailoring_msgModLength = "Sua doi khong duoc qua 200 ky tu";
				tailoring_msgReason = "Ban phai nhap ly do";
				tailoring_msgReasonLength = "Ly do khong duoc qua 200 ky tu";
				tailoring_msgNoteLength = "Ghi chu khong duoc qua 200 ky tu";
				tailoring_msgCat = "Ban phai nhap chung loai";
				//end tailoring
				//end cuongph---------------------------------------------------------
				/*********************DucHM***************************/
				ttlIssueLst = "Vi&#7879;c ph&#7909;";
				lblIssueLstWUName = "Tên &#273;&#417;n v&#7883;: ";
				capIssue = "Vi&#7879;c ph&#7909;";
				clmnIssueDescription = "M?t&#7843;";
				clmnIssueStatus = "Tr&#7841;ng thái";
				clmnIssuePriority = "M&#7913;c &#432;u tiên";
				clmnIssueOwner = "Ng&#432;&#7901;i s&#7903; h&#7919;u";
				clmnIssueDueDate = "H&#7841;n gi&#7843;i quy&#7871;t";
				lblIssuePage = "Trang: ";
				btnIssuePrev = "Tr&#432;&#7899;c";
				btnIssueNext = "Sau";
				ttlIssueAdd = "Thêm vi&#7879;c ph&#7909;";
				ttlIssueUpd = "C&#7853;p nh&#7853;t vi&#7879;c ph&#7909;";
				ttlIssueDet = "Chi ti&#7871;t vi&#7879;c ph&#7909;";
				clmnIssueType = "Ki&#7875;u";
				clmnIssueCreator = "Ng&#432;&#7901;i t&#7841;o";
				clmnIssueCreatedDate = "Ngày t&#7841;o";
				clmnIssueComment = "Ch?thú€h";
				clmnIssueClosedDate = "Ngày &#273;óng";
				clmnIssueReference = "Tham kh&#7843;o";
				errNoRight = "B&#7841;n không c?quy&#7873;n trong trang này!";
				msgIssueDescEmpty = "Mo ta khong the trong!";
				msgIssueDescLonger = "Do dai truong mo ta khong the qua 200 ky tu!";
				msgIssueOwnerEmpty = "Nguoi so huu khong the rong!";
				msgIssueCreatorEmpty = "Nguoi tao khong the rong!";
				msgIssueCreatedInvalid = "Ngay tao khong hop le!";
				msgIssueDueEmpty = "Han hoan thanh khong the rong!";
				msgIssueDueInvalid = "Han hoan thanh khong hop le!";
				msgIssueDueAfterStart = "Han hoan thanh phai sau ngay lap!";
				msgIssueCommentLonger = "Chu thich khong the qua 200 ky tu!";
				msgIssueClosedInvalid = "Ngay dong khong hop le!";
				msgIssueClosedAfterCreated = "Ngay dong phai sau ngay tao!";
				msgIssueClosedBeforeToday = "Ngay dong phai la hom nay hoac sau do!";
				msgIssueReferenceEmpty = "Tham khao khong the qua 50 ky tu!";
				ttlWelcome = "Xin chào, &#273;ây l? ";
				ttlOrgHome = "Trang ch&#7911; c&#417; quan";
				ttlPrjHome = "Trang ch&#7911; d&#7921; án";
				ttlGrpHome = "Trang ch&#7911; nhóm";
				ttlAdmHome = "Trang ch&#7911; qu&#7843;n tr&#7883;";
				capOrgSel = "Hãy ch&#7885;n m&#7897;t c&#417; quan";
				capPrjSel = "Hãy ch&#7885;n m&#7897;t d&#7921; án";
				capGrpSel = "Hãy ch&#7885;n m&#7897;t nhóm";
				capAdmSel = "Hãy ch&#7885;n m&#7897;t qu&#7843;n tr&#7883; viên";
				colOrgID = "M?c&#417; quan";
				colOrgName = "Tên&nbsp; c&#417; quan";
				colPrjID = "M?d&#7921; án";
				colPrjName = "Tên&nbsp; d&#7921; án";
				colGrpID = "M?nhóm";
				colGrpName = "Tên&nbsp; nhóm";
				colAdmID = "M?qu&#7843;n tr&#7883; viên";
				colAdmName = "Tên&nbsp; qu&#7843;n tr&#7883; viên";
				colUserStatus = "Ch&#7913;c v&#7909;";
				colUserDesignation = "B&#7893; nhi&#7879;m";
				break;
			case FRENCH :
				break;
			case GERMAN :
				break;
			case RUSSIAN :
				break;
		}
	}
	/**
	 * returns existing instance of object (or creates one on the first call),
	 * that are share for all users
	 * Avoids to create one language instance for each user
	 */
	public static Language instance(int language) {
		switch (language) {
			case ENGLISH :
				if (meENGLISH == null)
					meENGLISH = new Language(ENGLISH);
				return meENGLISH;
			case VIETNAMESE :
				if (meVIETNAMESE == null)
					meVIETNAMESE = new Language(VIETNAMESE);
				return meVIETNAMESE;
			case FRENCH :
				if (meFRENCH == null)
					meFRENCH = new Language(FRENCH);
				return meFRENCH;
			case GERMAN :
				if (meGERMAN == null)
					meENGLISH = new Language(GERMAN);
				return meGERMAN;
			case RUSSIAN :
				if (meRUSSIAN == null)
					meRUSSIAN = new Language(RUSSIAN);
				return meRUSSIAN;
			default :
				if (meENGLISH == null)
					meENGLISH = new Language(ENGLISH);
				return meENGLISH;
		}
	}
}