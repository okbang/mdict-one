-- DEFINE setting can be changed to allow &'s (ampersands) to be used in text
SET DEFINE ~

INSERT INTO dual values (0)
/
INSERT INTO GROUPS a (a.group_id, a.groupname, a.isoperationgroup, a.leader, a.description)
 VALUES(32,'FSOFT','','','COMPANY NAME')
/
INSERT INTO WORKUNIT a (a.workunitid, a.type, a.parentworkunitid, a.tableid,
       a.workunitname, a.protected) VALUES (0,2,'',0,'Insight Admin','')
/
INSERT INTO WORKUNIT a (a.workunitid, a.type, a.parentworkunitid, a.tableid,
       a.workunitname, a.protected) VALUES (132,0,0,1,'FSOFT',1)
/      
-- a.developer_id, 
INSERT INTO DEVELOPER a (a.name, a.group_name, a.account, a.designation,a.role, a.password, a.status, a.email, a.begin_date, a.quit_date,
       a.org_position_code, a.unit_id, a.user_type_code,a.location,
       a.phone_no, a.alternative_email, a.password_hint, a.disabled,
       a.password_date, a.change_pw, a.account_expired,
       a.account_locked, a.credentials_expired, a.password_backup,
       a.staff_id)
       VALUES('System Administrator','FSOFT','SYSADMIN',
	   'System Administrator',1000100000,'a159b7ae81ba3552af61e9731b20870515944538',1,
	   '',sysdate,'','QA',132,'INTERNAL_USER','','','','',0,'',0,'N','N','N','','')
/
INSERT INTO rightgroup a(a.rightgroupid, a.mnglevel, a.description, a.protected)
    VALUES('admin','Project Home','Administrator',1)
/
update DEVELOPER a set a.developer_id=1 where a.account='SYSADMIN'
/
 INSERT INTO rightgroupofuserbyworkunit a (a.workunitid, a.rightgroupid, a.manual, a.developerid)
	VALUES(132,'admin','',1)
/
insert into organization a (a.org_id, a.orgname) values(1,'FSOFT')
/
-- Thach comment.START
-- insert into organization a (a.org_id, a.orgname) values(2,'TEMP')
-- Thach comment.END

---de chay cho DMS






INSERT INTO ACTIVITY_TYPE(NAME, AT_ID, CODE, DISABLED)
  VALUES('Audit', 4, 'AUDIT', 0)
/
INSERT INTO ACTIVITY_TYPE(NAME, AT_ID, CODE, DISABLED)
  VALUES('Inspection', 3, 'INSPECTION', 0)
/
INSERT INTO ACTIVITY_TYPE(NAME, AT_ID, CODE, DISABLED)
  VALUES('Review', 2, 'REVIEW', 0)
/
INSERT INTO ACTIVITY_TYPE(NAME, AT_ID, CODE, DISABLED)
  VALUES('Test', 1, 'TEST', 0)
/
INSERT INTO AP_SIZEUNIT(UNIT_ID, NAME)
  VALUES(1, 'Pages')
/
INSERT INTO AP_SIZEUNIT(UNIT_ID, NAME)
  VALUES(2, 'UCP')
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(1, 'Normal application', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(2, 'Web application', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(3, 'Web application & Web engineering', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(4, 'Develop GUIs', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(5, 'Unit Test', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(6, 'Back-end', '0')
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(7, 'Business Management', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(8, 'Management information system', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(9, 'Intelligent data & Information Retrieval', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(10, 'E-Commercial & M-business', '0')
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(11, 'Middle ware', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(12, 'Other', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(14, 'Mainframe application', NULL)
/
INSERT INTO APPLICATION_TYPE(APPLICATION_ID, TYPE_NAME, STATUS)
  VALUES(15, 'Embedded Software', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(2, 'Banking and Finance', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(3, 'Distribution and supply chain ', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(5, 'Education ', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(6, 'Government & Public Service', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(7, 'Healthcare', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(8, 'IT service', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(9, 'Manufacturing', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(10, 'Media', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(12, 'Retail', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(15, 'Telecommunication ', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(16, 'Utility', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(17, 'Automative', NULL)
/
INSERT INTO BUSINESS_DOMAIN(DOMAIN_ID, DOMAIN_NAME, STATUS)
  VALUES(18, 'Other', NULL)
/
INSERT INTO DEFECT_PRIORITY(DP_ID, NAME)
  VALUES(1, '1-Immediately')
/
INSERT INTO DEFECT_PRIORITY(DP_ID, NAME)
  VALUES(2, '2-High')
/
INSERT INTO DEFECT_PRIORITY(DP_ID, NAME)
  VALUES(3, '3-Normal')
/
INSERT INTO DEFECT_PRIORITY(DP_ID, NAME)
  VALUES(4, '4-Low')
/
INSERT INTO DEFECT_SEVERITY(DEFS_ID, NAME, WEIGHT)
  VALUES(1, '1-Fatal', 10)
/
INSERT INTO DEFECT_SEVERITY(DEFS_ID, NAME, WEIGHT)
  VALUES(2, '2-Serious', 5)
/
INSERT INTO DEFECT_SEVERITY(DEFS_ID, NAME, WEIGHT)
  VALUES(3, '3-Medium', 3)
/
INSERT INTO DEFECT_SEVERITY(DEFS_ID, NAME, WEIGHT)
  VALUES(4, '4-Cosmetic', 1)
/
INSERT INTO DEFECT_STATUS(DS_ID, NAME)
  VALUES(1, '1-Error')
/
INSERT INTO DEFECT_STATUS(DS_ID, NAME)
  VALUES(2, '2-Assigned')
/
INSERT INTO DEFECT_STATUS(DS_ID, NAME)
  VALUES(3, '3-Pending')
/
INSERT INTO DEFECT_STATUS(DS_ID, NAME)
  VALUES(4, '4-Tested')
/
INSERT INTO DEFECT_STATUS(DS_ID, NAME)
  VALUES(5, '5-Accepted')
/
INSERT INTO DEFECT_STATUS(DS_ID, NAME)
  VALUES(6, '6-Cancelled')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(1, '01-Functionality (Other)')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(2, '02-User Interface')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(3, '03-Performance')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(4, '04-Design issue')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(5, '05-Coding standard')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(6, '06-Document')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(7, '07-Data & Database integrity')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(8, '08-Security & Access Control')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(9, '09-Portability')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(10, '10-Other')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(11, '11-Tools')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(12, '011-Req misunderstanding')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(13, '012-Feature missing')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(14, '013-Coding logic')
/
INSERT INTO DEFECT_TYPE(DT_ID, NAME)
  VALUES(15, '014-Business logic')
/

INSERT INTO HOLIDAY_TYPE(HOLIDAY_TYPE_CODE, DESCRIPTION)
  VALUES('HOLIDAY', 'ON HOLIDAY')
/
INSERT INTO HOLIDAY_TYPE(HOLIDAY_TYPE_CODE, DESCRIPTION)
  VALUES('OFFDAY', 'NON-WORKING DAY')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('10-Unit test', 10, 'UNIT_TEST', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('11-Integration test', 11, 'INTEGRATION_TEST', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('12-System test', 12, 'SYSTEM_TEST', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('13-Acceptance test', 13, 'ACCEPTANCE_TEST', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('14-Regression test', 14, 'REGRESSION_TEST', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('15-After Release test', 15, 'AFTER_RELEASE_TEST', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('16-Other test', 16, 'OTHER_TEST', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('17-Prototype test', 17, 'PROTOTYPE_TEST', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('20-Document review', 20, 'DOCUMENT_REVIEW', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('21-Code review', 21, 'CODE_REVIEW', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('22-After Release review', 22, 'AFTER_RELEASE_REVIEW', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('23-Prototype review', 23, 'PROTOTYPE_REVIEW', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('24-Other review', 24, 'OTHER_REVIEW', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('30-Quality Gate inspection', 30, 'QUALITY_GATE_INSPECTION', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('31-Final inspection', 31, 'FINAL_INSPECTION', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('32-Other inspection', 32, 'OTHER_INSPECTION', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('33-UT Inspection', 33, 'UT_INSPECTION', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('40-Baseline audit', 40, 'BASELINE_AUDIT', NULL, 0, 'DMS')
/
INSERT INTO QC_ACTIVITY(NAME, QA_ID, CODE, ORDER_NUMBER, DISABLED, APP_NAME)
  VALUES('41-Other audit', 41, 'OTHER_AUDIT', NULL, 0, 'DMS')
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('BOD                                               ', 'null', 'Directors, Vice Directors', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('Communicator                                      ', 'null', 'Business communication and translation', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('Customer                                          ', 'null', 'Customer Representative', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('Customer JAL-G9                                   ', 'null', 'Customer of project JIT-PalletControl', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('Customer_DAM Prj                                  ', 'null', 'Customer of G12', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('DP Team                                           ', 'null', 'Defect Prevention Team', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('External user                                     ', 'null', 'External user', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('GL                                                ', 'null', 'Group Leader', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('Manager                                           ', 'null', 'Vice of GLs, GLs, BOD, Head of support groups', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('PD                                                ', 'null', 'Project Director', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('PL                                                ', 'null', 'Project Leader', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('PQA                                               ', 'null', 'Process assurance', 2)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('PQA staff                                         ', 'null', 'Regular PQA staff', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('Partner                                           ', 'null', 'Customer Representative', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('Programmer                                        ', 'null', 'Developer', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('QAG                                               ', 'null', 'Operation Group-QA', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('SEPG Head                                         ', 'null', 'The Head of SEPG', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('SQA                                               ', 'null', 'Product assurance', 2)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('Senior developer                                  ', 'null', 'Analysis and Design, Code critical modules', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('TeamMember                                        ', 'null', 'System default role for team members', 2)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('Tester                                            ', 'null', 'Tester', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('Visitor                                           ', 'null', 'Visitor', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('archived manager                                  ', 'project archive', 'Project archive', NULL)
/
INSERT INTO RIGHTGROUP(RIGHTGROUPID, MNGLEVEL, DESCRIPTION, PROTECTED)
  VALUES('guest                                             ', 'null', 'Guest', NULL)
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(1, '1-Initiation')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(2, '2-Definition')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(3, '3-Solution')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(4, '4-Construction')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(5, '5-Transition')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(6, '6-Termination')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(7, 'Release 1')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(8, 'Release 2')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(9, 'Release 3')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(10, 'Release 4')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(11, 'Release 5')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(12, 'Release 6')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(13, 'Initiation&Definition')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(14, 'Solution&Construction')
/
INSERT INTO STAGE(STAGE_ID, NAME)
  VALUES(15, 'Definition&Solution')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(1, 'Coding convention could be inherited from the previous project', 'The project is a continuation of a project', 4, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(2, 'Code review may be done on sampling basis', 'Short project', 4, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(3, 'The project could invite external code reviewers', 'Small project team', 4, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(4, 'Do review code for 50% of program', 'Medium-complexity programs', 4, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(5, 'Do not conduct independent unit test and do one-person review for the first program', 'Skill level of the developer is high', 7, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(6, 'Record only the number of defects', 'Cosmetic defects in code review or unit test', 4, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(7, 'The project could conduct the minimum CM', 'Short project', 8, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(8, 'The project could have external configuration manager', 'Small project team', 8, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(9, 'The project could conduct the minimum CM. The promotion Next-build could be skipped. The CI promotion status could be skipped', 'Very small project team', 8, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(10, 'The CI promotion status could be skipped', 'Very short project', 8, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(11, 'The change request form and configuration control register could be in a different format provided that the required information is adequately collected.', 'Configuration register in a different format supplied by customer ', 8, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(12, 'Change management process needs to be followed, only if at least 10 more test cases are added to the base lined version', 'Base lined test plan of the project needs to be updated with more test cases', 8, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(13, 'It has to be ensured that the basic requirements of the CM process like Library maintenance, Version control, Configuration Status accounting are taken care of.', 'A configuration management tool beside VSS/PVCS may be used', 8, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(14, 'Project could use the customer versioning rule for the customer supplied work product', 'Customer provides a work product', 8, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(15, 'The start-up baseline could be inherited from the last baseline of the previous project', 'The project is a continuation of a project', 8, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(16, 'The prototype could be prepared and demonstrated', 'Users are not clear about the proposed system ', 1, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(17, 'The SQA does sample checks. SQC is to do the pre-delivery check for each delivery. The sampling criteria should be mentioned in the SQA plan or in the project plan. The SQA pre-delivery verification coverage is 25% as minimum', 'Deliveries are made on a continuous basis. Pre-delivery verification by SQA for all deliveries is not practicable', 5, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(18, 'Only architecture design could be conducted', 'Short project', 3, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(19, 'The project could invite external designers', 'Small project team', 3, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(20, 'The project could skip the requirement documents but the acceptance criteria must be verified by SQA', 'Customer provides Design documents', 3, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(21, 'The prototype could be prepared and demonstrated', 'The design components are highly critical', 3, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(22, 'The design could include only new and/or changed features and could refer to the last ones', 'The project is a continuation of a project', 3, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(23, 'Record only the number of defects', 'Cosmetic defects in review', 3, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(24, 'Instead of project plan in doc format, use a schedule in MS project format and shorted project database template to plan project. The neccesary parts of Project Plan should be included in WO. Project could record effort, schedule and defect data only. Pro', 'Very short project', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(25, 'Effort and schedules may be estimated directly from a work breakdown structure. The project could use Excel for tracking.  Milestone review could be conducted together with progress review. Project report/meeting should have periodicity not longer 2 weeks', 'Short project', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(26, 'Instead of project plan in doc format, use a schedule in MS project format and project database template to plan project. The neccesary parts of Project Plan should be included in WO. Project progress could be reviewed within the team by using minplan and', 'Very small project team', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(27, 'One project member could take on some project roles. The project could require the external experts for some tasks. But the project must have a team leader who is the backup project leader. The project formal meeting could be conducted for kick-of, milestone and post mortem only', 'Small project team', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(28, 'The new team structure could be followed with VP OPS approval', 'The team structure differs from standards', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(29, 'The project could follow a different estimation for size with the approval of the VP OPS but the instruction for estimation must be documented.', 'If the size of the project cannot be estimated with the standards being followed', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(30, 'The estimates can be based on industry standards or on values suggested by experienced personnel and are to be reviewed periodically.', 'Estimation values are not available', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(31, 'The estimation process should be documented with clear assumptions and should be approved by VP OPS', 'Estimation process for a particular kind of project/environment is not available', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(32, 'The project could follow estimation standards partially. Other parts of estimation must be conducted according to the instruction approved by VP OPS', 'Estimation for the size of the project can be done only partially by standards being followed', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(33, 'The project could follow customer required standards and processes. Keep records of these requirements', 'Customer requires to follow the different standards and processes', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(34, 'The project could prepare new mean and acceptance limits for project indicators', 'New technology or application domain', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(35, 'The project needs not update the project plan but needs to record these changes', 'The changes in the project execution impact less than 10% of project schedule or project effort', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(36, 'Change management process needs to be followed. The plans will be updated on a periodic basis. The revision criteria should be mentioned in the project plan', 'Change requirements are made on a continuous basis. The revision of plans may not be feasible.', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(37, 'Size could be tracked in these terms provided that it is a project requirement.', 'Different size tracking tool/mechanism is applied due to project specific requirements', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(38, 'The project must prepare a supporting plan for this stage before post mortem meeting', 'The support stage is included in PLC', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(39, 'All documents required as the Quality gates could be applied in the Final inspection gate', 'Short project', 12, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(40, 'The offline reviews could be applied. The project could require the external experts for reviewing the critical work items. The SQA could be an external member', 'Small project team', 12, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(41, 'The allocated SQA will review the sample of the deliverables on a periodic basis. SQC shall check all the deliveries. The sampling criteria should be mentioned in the SQA plan or in the project plan. The SQA review coverage is 50% as minimum. And SQA pre-delivery verification is 25% as minimun.', 'Deliveries are made on a continuous basis. SQA review of all deliverables may not be feasible.', 12, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(42, 'Project could skip internal audit but the quality control should be done weekly', 'Very short project', 13, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(43, 'The checklist for requirement analysis could be used instead of creating a requirement analysis document.', 'Very short project', 2, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(44, 'The project could invite external analysts', 'Small project team', 2, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(45, 'The project could conduct only requirement analysis', 'Customer provides URD', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(46, 'The checklist for requirement analysis could be used instead of creating a SRS document', 'Requirements are simple
', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(47, 'The checklist for requirement analysis could be used instead of creating a SRS document', 'Technical proposal covers all SRS contents', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(48, 'The prototype could be prepared and demonstrated', 'The requirements are highly critical', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(49, 'The requirements analysis document could include only new and/or changed requirements and could refer to the last ones', 'The project is a continuation of a project', 2, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(50, 'The checklist for requirement analysis could be used instead of creating a requirement analysis document.', 'Change requirements with the estimated impact costing less than 5% of project effort or less than 10 person days', 2, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(51, 'The checklist for requirement analysis could be used instead of creating a requirement analysis document', 'Requirements are simple', 2, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(52, 'Record only the number of defects', 'Cosmetic defects in review', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(53, 'Peer review preparation time need not be given', 'Requests with turn around time less than 24 hrs', 6, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(54, 'Test case/Test data could be inherited from the previous project. Create test case/test data for new requirements only', 'The project is a continuation of a project', 7, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(55, 'Test plan could be prepared in task list form', 'Short project', 7, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(56, 'The project could invite external testers', 'Small project team', 7, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(57, 'Do not generate test database', 'Test data are available from existing system', 7, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(58, 'The additional testing phases to be used', 'Additional testing phases are required for the products before final release. ', 7, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(59, 'The defect types definition could be tailored to suit the requirements of the project. But it should reflect the severity in the same order. Using classification should be documented in the project plan', 'Definition for the defect types is different from standard.', 7, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(60, 'The project could skip regression test', 'Cosmetic defects in test', 7, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(61, 'Test report could be skipped in case tester is a project member', 'Very small project team', 7, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(62, 'Project could have only 2 environments (Develop and NextBuild)', 'Test and release environments are at customer''s site', 8, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(63, 'Summary review of changes in the design of the stage will be conducted at the end of each stage', 'Changes in design with the estimated impact costing less than 1% of project effort or less than 2 person days', 3, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(64, 'Project Plan can be combinated with WO and MPP', 'Maintenance project', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(65, 'Project could skip the final inspection for these releases but the verification should be done at the quality gates of the stages', 'Emergency release for fixing cosmetic bugs', 12, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(67, 'Project SQA has to work with Project Managers to create a suitable SQA checklist and get approval of SQA leader before implementing', 'A SQA checklist is not applicable for  project', 12, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(68, 'Summary review of changes in the requirements of the stage will be conducted at the end of each stage', 'Change requirements with the estimated impact costing less than 1% of project effort or less than 2 person days', 2, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(69, 'Project can maintain the requirement sheet with the reference to more detail sheets or docs', 'There are some detail levels of requirements', 2, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(70, 'Do not need to record these IPs in the improvement proposal template but in the IP database', 'Small impact or low prioprity improvement proposals', 27, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(71, 'For bugs unit test can be conducted on the description of these PNs without test cases. The regression test could be skipped. Test cases could be updated in the final release', 'The business and the system are mastered by the team and PNs are simple', 7, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(72, 'Adding a new stage can be made between every two stages of the SLC. Splitting one of the stages to some sub-stages (iterations). One iteration must be no shorter than 1 week', 'The project is long', 1500, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(73, 'Merge can be conducted for stages but the project should have at least 2 stages. All documents required as the Quality gates by these stages should be applied', 'The project is short', 1500, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(74, 'Merge can be conducted for all stages. All documents required as the Quality gates should be applied', 'The project is very short', 1500, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(75, 'Drop one or more stages can be conducted except Initiation and Termination stages, but the work products  of the dropped stages should be verified.', 'The project team is small', 1500, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(76, 'Delete the stages that should be applied except Initiation and Termination stages. The Quality gate of the previous stage in the PLC should verify the completeness of all customer submitted products', 'Customer submits all mandatory work products of a stage', 1500, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(77, 'Add a new work product', 'New work product is needed', 1500, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(78, 'Remove the work products from list of work products. Define the work product that includes the content of the removed one', 'The essential content of a work product can be embedded in other products', 1500, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(79, 'Remove the work products of these processes from the list of work products of the project', 'The project scope doesn’t include some of software processes', 1500, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(80, 'Select new SLC with the consultancy of SEPG', 'The SLC model is not feasible for the project', 1500, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(81, 'follow customer''s template for test case and test plan', 'Customers require to use their template', 7, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(82, 'Re-use test case and test plan from previous project       ', 'Maintenance Project                     ', 7, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(83, ' Project only conduct unit test/integration test/system test', 'Customers require and unit test/intergration test/system test can be done by the customer.', 7, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(84, 'Follow customer''s coding convention', 'Customers require', 4, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(85, 'follow customer''s template', 'Customers require', 18, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(86, 'Follow customer''s template for Functional spec', 'Customers require', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(87, ' reuse the ADD from old project', 'Maintenance Project       ', 3, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(88, 'Release Note is in form of mails to customer', 'the release note in mail contains what to be included in the release only', 5, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(89, 'Design documents follow the customer''s template', 'Customers require', 3, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(90, 'Reuse design documents from previous project', 'Maintainance project', 3, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(91, 'ADD and DDD are merged in one document which used template provided by the customer and Detail design can be included in HLD', 'Customers require/short project', 3, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(92, 'ADD and DDD can be skipped', 'Customer did not request and Provide the Functional Spec and Design/ implementation project', 3, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(93, 'Keep name and version of products of previous project', 'for tracking and maintain', 18, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(94, 'SRS,URD is not developed', 'Customer provides  requirements description in the form of SRS', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(95, 'Re-use SRS from previous project', 'Maintainance project', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(96, 'SRS format follows customer''s template', 'Customers require', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(97, 'SRS is not created', 'Short project', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(98, 'Q&A list is developed and maintained for recording all questions and answers exchange between project team and customer.', 'it is helpful for requirement undestanding, tracking.', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(99, 'Merge SRS and high level design in URD document', 'small project', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(100, 'Use customer''s test case/Test Plan', 'customers require and provide test case/Test plan', 7, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(101, 'Test case is created under the form of checklist', 'Short project', 7, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(102, 'ADD and DDD can not be created', 'User supply spec that covers detail requirements and architecture.', 3, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(103, 'The CM plan is included in the WO', 'Short project', 8, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(104, 'Mpp file includes only engineering tasks, management and QA tasks are planned in FI', 'It is used to cumminicate with customer.', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(105, 'QA tasks and review tasks are not included in Project schedule file, they are stated in Fsoft-Insight', 'Projects use Fsoft Insight for managing', 12, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(106, 'Interim release need no final inspection', 'Interim might not include all the required features. Its main purpose is to discover early any requirement misunderstandings', 12, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(107, 'URD and SRS are wrapped in use cases', 'Customer requires', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(108, 'Apply all tailorings for Very short project, small team size.', 'Very short project, small team size.', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(109, 'Report customer status of bug and change using B & P voucher templated supplied by customer when release the product', 'Customer required', 7, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(110, 'Use customer''s defined template  for tracking progress of the project instead of mpp file', 'Customer required ', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(111, 'We used customer''s spread sheet to manage change requirements', 'Customer requires', 2, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(112, 'The project skip design, coding, deployment activities. The stages are divided based on the delivery time of test report and report the result', 'testing project', 1500, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(113, 'Final inspection is conducted every week for the releases in the week. SQA review for docs of the release is at the final inspection time.', 'Deliveries for CRs are made on a continuous basis. Pre-delivery verification by SQA for all deliveries is not practicable', 12, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(114, 'Pilot for SLET, use SLET template', 'pilot for SLET', 26, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(115, 'Support process: follow the process of customer''s process document', 'pure support project ', 1500, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(116, 'No Solution stage in FSOFT software lifecycle', 'No need to create Architecture Design document as customer provide  their detail design', 1500, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(117, 'The minutes of project progress review meetings are kept track in Excel file', 'Reduce the time for writting the meeting minutes. ', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(118, 'Proposal includes estimation', 'Proposal sent to customer has defined Estimation very clearly.', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(119, 'Use customer''s Weekly Report instead of weekly meeting minute', 'Team size is small', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(120, 'Defect prevention actions could implemented in project kick-off meeting only but their results have to been reported in postmortem report', 'Very short project', 12, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(121, 'Record of the changes. Accumulate those changes and update correspondent Cis periodically. But revision criteria and revision plan to be documented
', '"Changes are made on a continuous basic. The revision of CIs is not feasible. 
or Changes are not necessary to incorporate immediately to the Cis"
', 8, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(122, 'Remove the correspondent stage except Initiation and Termination stages.
', 'Customer supplies all mandatory work products of a stage
', 1500, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(123, 'Define the new cycle with the consultancy of SEPG
', 'The standard lifecyle does not suitable for the project
', 1500, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(124, 'Defect prevention actions could implemented in project kick-off meeting only but their results have to been reported in postmortem report.
', 'Very short project
', 28, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(125, 'Do not need to record these IPs in the improvement proposal template but in the IP database
', 'Small impact or low prioprity improvement proposals
', 29, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(126, 'The quality gate of previous stage could verify completeness of all customer supplied products
', 'Customer supplies all mandatory work products of a stage
', 12, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(127, 'Different size tracking tool/mechanism is applied
', 'Different size tracking tool/mechanism is applied due to project specific requirements
', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(128, 'Tools could be used to replace templates and forms if they cover necessary information or data defined in the templates and forms
', 'The standards and forms could be replaced by tools
', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(129, 'Targeted value of project metrics could be different from Fsoft norm, but rationale should be documented and approved by Head of SEPG
', 'Specific project characteristic
', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(130, 'Add the work product 
', 'New work product is needed
', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(131, 'Do not apply PPM but use ETC
', 'Very short project
', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(132, 'Do not apply PPM for effort and defect but use ETC
', 'Project lifecycle has less than 4 stages
', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(133, 'Do not apply the product defect PPM
', 'The project has less than 3 deliverables
', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(138, 'Effort may be estimated directly from WBS.
', 'Short project
', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(139, 'The project may be used excel file for tracking project instead of Project schedule.', 'Short project', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(142, '1. Instead of project plan, project could have only WO and project schedule (WO and project schedule are mandatory WPs of Project Plan)
2. Project could record effort, schedule and defect data only 
3. Project reports/meetings could be done as necessary but the periodicity is not longer 2 weeks/time.
4. Metrics tracking can be done on event-driven basis for project without milestone review
', 'Very short project
', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(143, '1. Instead of project plan, project could have only WO and project schedule (WO and project schedule are mandatory WPs of Project Plan)
2. Project progress review could be done through task management activities. Record of progress review meeting could be recorded not inform of meeting minutes but in other forms
', 'Very small project team
', 9, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(144, 'Milestone review could be conducted together with progress review', 'Short project
', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(145, 'Project report/meeting should have periodicity not longer 02weeks/time', 'Short project
', 9, '1', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(146, 'Quality control could vary in form of controlling frequency, items to be controlled and effort spent, etc for various project characteristic. But the specifics to be pointed out in Quality control plan
', 'Different level of skill and qualification, process maturity and characteristic and priority
', 12, '0', '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(147, '1. Effort may be estimated directly from WBS.
2. The project maybe use excel file for tracking project instead of Project schedule.
3. Milestone review could be conducted together with progress review
4. Project report/meeting should have periodicity not longer 02weeks/time
', 'Short project
', 9, NULL, '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(148, 'Record only the number of defects
', 'Cosmetic defects in review
', 2000, NULL, '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(149, 'The process/guidelines/templates/checklists could be customized to fit project requirements and get review and approval of SEPG Head and their manager before implementing
', 'The standard guidelines/process/templates/checklists do not cover project specific requirements
', 2000, NULL, '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(150, 'Develop/Purchase/Use tool to automate activities
', 'The activities could be automated
', 2000, NULL, '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(151, 'Templates and forms could be in different format with the standard ones, but the modified must cover all content of the standard ones
', 'Format of standard templates/checklist is not suitable
', 2000, NULL, '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(152, 'Remove the work products from list of work products. Define the work product that includes the content of the removed one
', 'The essential content of a work product could be embeded in the other work products
', 2000, NULL, '3')
/
INSERT INTO TAILORING(TAIL_ID, TAIL_PER, APP_CRI, PROCESS_ID, STATUS, CATEGORY)
  VALUES(153, 'The project does not need to develop the work products. But verification on the work products must be conducted using defined criteria to ensure they are met with the project requirements. 
', 'Work products are developed/supplied by the parties outside the project
', 2000, NULL, '3')
/
INSERT INTO TYPEOFWORK(TOW_ID, NAME)
  VALUES(1, 'Study')
/
INSERT INTO TYPEOFWORK(TOW_ID, NAME)
  VALUES(2, 'Create')
/
INSERT INTO TYPEOFWORK(TOW_ID, NAME)
  VALUES(3, 'Review')
/
INSERT INTO TYPEOFWORK(TOW_ID, NAME)
  VALUES(4, 'Test')
/
INSERT INTO TYPEOFWORK(TOW_ID, NAME)
  VALUES(5, 'Correct')
/
INSERT INTO TYPEOFWORK(TOW_ID, NAME)
  VALUES(6, 'Translate')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Acceptance note', 0, 10, 26, 'ACCEPTANCE_NOTE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Architectural design', NULL, 5, 3, 'ARCHITECTURAL_DESIGN', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Audit program', NULL, 46, 26, 'AUDIT_PROGRAM', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Audit record', NULL, 45, 26, 'AUDIT_RECORD', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Audit report', NULL, 36, 26, 'AUDIT_REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Baseline Audit', NULL, 78, NULL, 'BASELINE_AUDIT', 0, NULL)
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Baseline report', NULL, 27, 26, 'BASELINE_REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('CM Plan', NULL, 30, 26, 'CM_PLAN', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Change request', NULL, 31, 26, 'CHANGE_REQUEST', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Coding convention', NULL, 43, 4, 'CODING_CONVENTION', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Configuration Status Report', NULL, 32, 26, 'CONFIGURATION_STATUS_REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Contract', NULL, 11, 26, 'CONTRACT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Customer Satisfaction Survey', NULL, 33, 26, 'CUSTOMER_SATISFACTION_SURVEY', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('DAR Report', NULL, 79, NULL, 'DAR_REPORT', 0, NULL)
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('DP Plan', NULL, 80, NULL, 'DP_PLAN', 0, NULL)
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('DP report', NULL, 58, 26, 'DP_REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Database', NULL, 62, 26, 'DATABASE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Deployment package', NULL, 63, 26, 'DEPLOYMENT_PACKAGE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Design prototype', 1, 42, 3, 'DESIGN_PROTOTYPE', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Detailed design', NULL, 8, 3, 'DETAILED_DESIGN', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Final inspection', NULL, 81, NULL, 'FINAL_INSPECTION', 0, NULL)
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Handover note', NULL, 59, 26, 'HANDOVER_NOTE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('IP', NULL, 47, 26, 'IP', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('IP database', NULL, 48, 26, 'IP_DATABASE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Installation manual', NULL, 29, 4, 'INSTALLATION_MANUAL', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Integration test case', NULL, 69, 26, 'INTEGRATION_TEST_CASE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Integration test plan', NULL, 6, 26, 'INTEGRATION_TEST_PLAN', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Integration test report', NULL, 13, 26, 'INTEGRATION_TEST_REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Meeting minutes', NULL, 22, 26, 'MEETING_MINUTES', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Others', NULL, 20, 26, 'OTHERS', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('PCB', NULL, 39, 26, 'PCB', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('PQA report', NULL, 37, 26, 'PQA_REPORT', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Pilot plan', NULL, 49, 26, 'PILOT_PLAN', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Pilot record', NULL, 50, 26, 'PILOT_RECORD', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Pilot report', NULL, 51, 26, 'PILOT_REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Plan', NULL, 52, 26, 'PLAN', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Process assets', NULL, 40, 26, 'PROCESS_ASSETS', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Process database', NULL, 41, 26, 'PROCESS_DATABASE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Program', NULL, 53, 26, 'PROGRAM', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Project assets', NULL, 28, 26, 'PROJECT_ASSETS', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Project database', NULL, 24, 26, 'PROJECT_DATABASE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Project plan', NULL, 4, 26, 'PROJECT_PLAN', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Project record', NULL, 23, 26, 'PROJECT_RECORD', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Project report', NULL, 12, 26, 'PROJECT_REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Proposal', NULL, 25, 26, 'PROPOSAL', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Prototype', NULL, 82, NULL, 'PROTOTYPE', 0, NULL)
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('QA report', NULL, 83, NULL, 'QA_REPORT', 0, NULL)
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('QA review record', NULL, 84, NULL, 'QA_REVIEW_RECORD', 0, NULL)
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('QDS', NULL, 17, 26, 'QDS', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Quality control report', NULL, 66, 26, 'QUALITY_CONTROL_REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Quality gate record', NULL, 67, 26, 'QUALITY_GATE_RECORD', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Record', NULL, 54, 26, 'RECORD', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Release note', NULL, 14, 26, 'RELEASE_NOTE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Report', NULL, 55, 26, 'REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Requirement prototype', 1, 21, 2, 'REQUIREMENT_PROTOTYPE', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Resource and environment', NULL, 35, 26, 'RESOURCE_AND_ENVIRONMENT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Review TestCase', 0, 77, NULL, 'REVIEW TESTCASE', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Review record', NULL, 26, 26, 'REVIEW_RECORD', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('SQA report', NULL, 38, 26, 'SQA_REPORT', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('SRS', NULL, 3, 2, 'SRS', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Service Level Agreement', NULL, 60, 26, 'SERVICE_LEVEL_AGREEMENT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Software module', 1, 44, 4, 'SOFTWARE_MODULE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Software package', 1, 9, 4, 'SOFTWARE_PACKAGE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Support Diary', NULL, 34, 26, 'SUPPORT_DIARY', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('System description', NULL, 16, 4, 'SYSTEM_DESCRIPTION', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('System test case', NULL, 7, 26, 'SYSTEM_TEST_CASE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('System test plan', NULL, 76, 26, 'SYSTEM_TEST_PLAN', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('System test report', NULL, 74, 26, 'SYSTEM_TEST_REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Test data', NULL, 71, 26, 'TEST_DATA', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Test plan', NULL, 85, NULL, 'TEST_PLAN', 0, NULL)
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Test script', 1, 72, 26, 'TEST_SCRIPT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Traceability matrix', NULL, 61, 26, 'TRACEABILITY_MATRIX', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Training course', NULL, 19, 26, 'TRAINING_COURSE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Training material', NULL, 56, 26, 'TRAINING_MATERIAL', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Training records', NULL, 64, 26, 'TRAINING_RECORDS', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('URD', NULL, 2, 2, 'URD', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Unit test case', NULL, 68, 26, 'UNIT_TEST_CASE', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Unit test plan', NULL, 75, 26, 'UNIT_TEST_PLAN', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Unit test report', NULL, 73, 26, 'UNIT_TEST_REPORT', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('Use case', NULL, 18, 26, 'USE_CASE', 1, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('User manual', NULL, 15, 4, 'USER_MANUAL', 0, 'DMS')
/
INSERT INTO WORKPRODUCT(NAME, HAS_LOC, WP_ID, PROCESS, CODE, DISABLED, APP_NAME)
  VALUES('WO', NULL, 1, 26, 'WO', 0, 'DMS')
/
INSERT INTO LANGUAGE_CODE(LANG_CODE, DESCRIPTION)
  VALUES('en', 'English')
/
INSERT INTO LANGUAGE_CODE(LANG_CODE, DESCRIPTION)
  VALUES('ja', 'Japanese')
/
INSERT INTO LANGUAGE_CODE(LANG_CODE, DESCRIPTION)
  VALUES('vn', 'Vietnamese')
/  
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(173, 'PROJECT_STATUS', 'ON_GOING', 'en', 'On-going', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(174, 'PROJECT_STATUS', 'CLOSED', 'en', 'Closed', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(175, 'PROJECT_STATUS', 'CANCELLED', 'en', 'Cancelled', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(176, 'PROJECT_STATUS', 'TENTATIVE', 'en', 'Tentative', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(177, 'PROJECT_TYPE', 'EXTERNAL', 'en', 'External', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(178, 'PROJECT_TYPE', 'INTERNAL', 'en', 'Internal', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(179, 'PROJECT_TYPE', 'MISC', 'en', 'Misc', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(180, 'PROJECT_CATEGORY', 'DEVELOPMENT', 'en', 'Development', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(181, 'PROJECT_CATEGORY', 'MAINTENANCE', 'en', 'Maintenance', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(182, 'PROJECT_CATEGORY', 'OTHER', 'en', 'Others', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(183, 'USER_TYPE', 'INTERNAL_USER', 'en', 'Internal User', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(184, 'USER_TYPE', 'EXTERNAL_USER', 'en', 'External User', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(185, 'USER_TYPE', 'END_USER', 'en', 'End User', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(186, 'USER_TYPE', 'CUSTOMER', 'en', 'Customer', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(187, 'PROJECT_STATUS', 'ON_GOING', 'ja', '???', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(188, 'PROJECT_STATUS', 'CLOSED', 'ja', '??', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(189, 'PROJECT_STATUS', 'CANCELLED', 'ja', '?????', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(190, 'PROJECT_STATUS', 'TENTATIVE', 'ja', '?', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(191, 'PROJECT_TYPE', 'EXTERNAL', 'ja', '?????', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(192, 'PROJECT_TYPE', 'INTERNAL', 'ja', '????', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(193, 'PROJECT_TYPE', 'MISC', 'ja', '??', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(194, 'PROJECT_CATEGORY', 'DEVELOPMENT', 'ja', '??', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(195, 'PROJECT_CATEGORY', 'MAINTENANCE', 'ja', '??', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(196, 'PROJECT_CATEGORY', 'OTHER', 'ja', '???', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(197, 'USER_TYPE', 'INTERNAL_USER', 'ja', '????', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(198, 'USER_TYPE', 'EXTERNAL_USER', 'ja', '?????', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(199, 'USER_TYPE', 'END_USER', 'ja', '???????', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(200, 'USER_TYPE', 'CUSTOMER', 'ja', '?????', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(201, 'PROJECT_TYPE', 'PUBLIC', 'en', 'Public project', 0)
/
INSERT INTO GENERAL_REFERENCE(GENERAL_REF_ID, GROUP_CODE, ITEM_CODE, LANG_CODE, DESCRIPTION, DISABLED)
  VALUES(202, 'PROJECT_TYPE', 'AAAAA', 'en', 'aaaa', 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(1, 'Project home                                      ', 31, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(2, 'Group reports                                     ', 25, 2)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(3, 'Work Order                                        ', 32, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(4, 'Requirements                                      ', 35, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(5, 'Effort                                            ', 37, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(6, 'Customer Complaints                               ', 40, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(7, 'Defects                                           ', 43, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(8, 'Project issues                                    ', 45, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(11, 'Organization issues                               ', 11, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(12, 'Group home                                        ', 17, 2)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(13, 'Organization PCB                                 ', 7, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(14, 'Organization plan                                 ', 3, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(15, 'Organization human index                          ', 4, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(16, 'Admin home                                      ', 52, 4)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(17, 'Organization process                              ', 6, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(18, 'Finance                                           ', 9, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(19, 'Group PCB                                         ', 23, 2)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(20, 'Infrastruture                                     ', 5, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(21, 'Group issues                                      ', 26, 2)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(22, 'User Profiles                                     ', 53, 4)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(23, 'Organization product                              ', 8, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(24, 'Group plan                                        ', 19, 2)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(25, 'Group human Index                                 ', 21, 2)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(26, 'Organization reports                              ', 10, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(27, 'Group product                                     ', 24, 2)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(28, 'Project plan                                      ', 33, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(29, 'Project parameters                                ', 34, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(30, 'Schedule                                          ', 36, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(31, 'Size                                              ', 38, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(32, 'CI Tracking                                       ', 39, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(33, 'Tailoring Deviation                               ', 41, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(34, 'Practice and Lessons                              ', 42, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(35, 'NCS                                               ', 44, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(36, 'Project reports                                   ', 46, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(37, 'Roles                                             ', 54, 4)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(38, 'SEPG home                                         ', 59, 5)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(39, 'FIST home                              ', 61, 5)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(40, 'Organization home                                 ', 1, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(41, 'Group process                                     ', 22, 2)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(42, 'Work Unit                                         ', 55, 4)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(43, 'Risks', 47, 3)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(44, 'Parameters', 56, 4)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(45, 'Org Point', 57, 4)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(46, 'Organization norms', 2, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(47, 'Organization monitoring', 12, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(48, 'Group monitoring', 26, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(49, 'Group norms', 18, 2)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(50, 'Process Assets', 13, 1)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(51, 'Project Archive', 58, 4)
/
INSERT INTO PAGE(PAGEID, PAGENAME, ORDR, TYPE)
  VALUES(52, 'Human Resource', 20, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 3, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 3, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 3, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 3, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 3, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 3, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 1, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 3, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 3, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 3, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 2, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('BOD                                               ', 3, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Communicator                                      ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer                                          ', 1, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer JAL-G9                                   ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Customer_DAM Prj                                  ', 1, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('DP Team                                           ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 3, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('External user                                     ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 1, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 3, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('GL                                                ', 2, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 1, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Manager                                           ', 2, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 1, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 1, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 1, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 1, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 1, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 3, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PD                                                ', 2, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 3, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 2, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PL                                                ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 3, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA                                               ', 2, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 3, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('PQA staff                                         ', 2, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 2, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 2, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 2, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 2, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 2, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 2, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 2, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 2, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Partner                                           ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 3, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 1, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Programmer                                        ', 2, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 2, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 3, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('QAG                                               ', 1, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 2, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 2, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SEPG Head                                         ', 3, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 1, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 1, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 3, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('SQA                                               ', 2, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Senior developer                                  ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('TeamMember                                        ', 1, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Tester                                            ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 2, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('Visitor                                           ', 1, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 52)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 51)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 50)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 49)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 48)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 47)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('admin                                             ', 3, 39)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 40)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 45)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 46)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 14)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 15)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 20)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 17)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 13)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 23)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 18)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 26)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 11)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 12)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 24)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 25)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 41)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 19)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 27)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 2)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 21)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 1)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 3)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 28)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 29)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 4)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 30)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 5)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 31)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 32)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 6)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 33)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 34)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 7)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 35)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 8)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 36)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 43)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 16)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 22)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 37)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 42)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 1, 44)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 38)
/
INSERT INTO RIGHTFORPAGE(RIGHTGROUPID, PRIVILEGE, PAGEID)
  VALUES('guest                                             ', 2, 39)
/
INSERT INTO CONTRACT_TYPE(CONTRACT_TYPE_ID, CONTRACT_TYPE_NAME, CONTRACT_TYPE_DESCRIPTION, CONTRACT_TYPE_STATUS)
  VALUES(1, 'Body Shopping', NULL, 0)
/
INSERT INTO CONTRACT_TYPE(CONTRACT_TYPE_ID, CONTRACT_TYPE_NAME, CONTRACT_TYPE_DESCRIPTION, CONTRACT_TYPE_STATUS)
  VALUES(2, 'Fixed Price', NULL, 0)
/
INSERT INTO CONTRACT_TYPE(CONTRACT_TYPE_ID, CONTRACT_TYPE_NAME, CONTRACT_TYPE_DESCRIPTION, CONTRACT_TYPE_STATUS)
  VALUES(3, 'Time Material', NULL, 0)
/
INSERT INTO CONTRACT_TYPE(CONTRACT_TYPE_ID, CONTRACT_TYPE_NAME, CONTRACT_TYPE_DESCRIPTION, CONTRACT_TYPE_STATUS)
  VALUES(4, 'Other', NULL, 0)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(3, '2nd Generation default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(4, 'Java', NULL, TO_DATE('2003-05-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(6, '4th Generation default', NULL, TO_DATE('2007-12-21 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(7, '5th Generation default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(8, 'AAS Macro', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(9, 'ABAP/4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(10, 'ACCEL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(11, 'Microsoft Access', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(12, 'ActiveWorks', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(13, 'ACTOR', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(14, 'Acumen', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(15, 'Ada', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(16, 'Ada 83', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(17, 'Ada 95', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(18, 'ADR/DL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(19, 'ADR/IDEAL/PDL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(20, 'ADS/Batch', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(21, 'ADS/Online', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(22, 'AI shell default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(23, 'AI SHELLS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(24, 'Algol', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(25, 'ALGOL 68', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(26, 'ALGOL W', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(27, 'Alliance', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(28, 'AMBUSH', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(29, 'AML', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(30, 'AMPPL II', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(31, 'Amtrix', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(32, 'ANSI BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(33, 'ANSI COBOL 74', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(34, 'ANSI COBOL 85', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(35, 'ANSI SQL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(36, 'ANSWER/DB', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(38, 'APL 360/370', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(39, 'APL default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(40, 'APL*PLUS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(41, 'APPLESOFT BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(42, 'Application Builder', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(43, 'Application Manager', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(44, 'APS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(45, 'APT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(46, 'APTools', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(47, 'ARC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(48, 'Ariel', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(49, 'ARITY', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(50, 'Arity PROLOG', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(51, 'ART', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(52, 'ART Enterprise', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(53, 'Artemis', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(54, 'ART-IM', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(55, 'AS/SET', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(56, 'ASI/INQUIRY', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(57, 'ASK Windows', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(58, 'ASP', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(59, 'Assembler', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(60, 'Assembly (basic)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(61, 'Assembly (macro)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(62, 'Associative default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(63, 'Autocoder', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(65, 'Aztec C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(66, 'BALM', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(67, 'BASE SAS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(68, 'BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(69, 'Basic (ANSI)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(70, 'Basic (Applesoft)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(71, 'BASIC A', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(72, 'Basic assembly', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(73, 'Berkeley PASCAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(74, 'BETTER BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(75, 'BLISS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(76, 'BMSGEN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(77, 'BOEINGCALC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(78, 'Bollero', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(79, 'BTEQ', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(80, 'BusinessWare', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(81, 'C', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(83, 'C (Microsoft)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(84, 'C Set 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(85, 'C#', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(86, 'C++', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(87, 'C++ (default)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(88, 'C++ (Microsoft v7)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(89, 'C++ (Symantec)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(90, 'C++ Builder 1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(91, 'C++ Builder 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(92, 'C++ Builder 3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(93, 'C++ Builder 4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(94, 'C86Plus', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(95, 'CA-dBFast', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(96, 'CA-EARL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(97, 'CAST', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(98, 'CBasic', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(99, 'CDADL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(100, 'CELLSIM', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(101, 'Centerline C++', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(102, 'CGI', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(103, 'CHILI', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(104, 'CHILL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(105, 'CICS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(106, 'CLARION', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(107, 'CLASCAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(108, 'CLI', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(109, 'Clipper', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(110, 'Clipper DB', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(111, 'CLOS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(112, 'CLOUT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(113, 'CMS2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(114, 'CMSGEN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(115, 'Cobol', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(116, 'Cobol (Fujitsu)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(117, 'Cobol (Microfocus)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(118, 'Cobol (Realia)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(119, 'Cobol (Visual)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(120, 'COBOL II', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(121, 'Cobol/400', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(122, 'COBRA', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(123, 'CodeCenter', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(124, 'Cofac', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(125, 'COGEN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(126, 'COGNOS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(127, 'COGO', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(128, 'ColdFusion 4.5', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(129, 'COMAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(130, 'COMIT II', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(131, 'Common LISP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(132, 'Concurrent PASCAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(133, 'Conductor', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(134, 'CONNIVER', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(135, 'CORAL 66', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(136, 'CORVET', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(137, 'CorVision', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(138, 'CPL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(140, 'Crystal Reports', NULL, NULL, 'Pages', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(141, 'CSL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(142, 'CSP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(143, 'CSSL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(144, 'CULPRIT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(145, 'CxPERT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(146, 'CYGNET', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(147, 'Data base default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(148, 'Dataflex', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(149, 'DataGate', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(150, 'Datatrieve', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(151, 'DB/2 SQL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(152, 'dBase III', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(153, 'dBase IV', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(154, 'DCL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(155, 'Decision support default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(156, 'DEC-RALLY', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(157, 'Delphi 1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(158, 'Delphi 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(159, 'Delphi 3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(160, 'Delphi 4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(161, 'Delphi 5', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(162, 'Delphi 6', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(163, 'DL/1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(164, 'DNA-4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(165, 'Domino Formula', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(166, 'Domino Script', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(167, 'DOS Batch Files', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(168, 'DSP Assembly', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(169, 'DTABL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(170, 'DTIPT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(171, 'DYANA', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(172, 'DYNAMO-III', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(173, 'EASEL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(174, 'EASY', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(175, 'EASYTRIEVE +', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(176, 'Eclipse', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(177, 'EDA/SQL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(178, 'ED-Scheme 3.4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(179, 'EIFFEL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(180, 'EnableNet', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(181, 'ENFORM', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(183, 'Ensemble', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(184, 'Enterprise Int. Template', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(185, 'EPOS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(186, 'e-Process Framework', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(187, 'Erlang', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(188, 'ESF', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(189, 'ESPADVISOR', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(190, 'ESPL/I', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(191, 'EUCLID', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(193, 'EXCEL 1-2', NULL, TO_DATE('2004-01-19 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'words', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(197, 'EXSYS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(198, 'Extended Common LISP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(199, 'EZNOMAD', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(200, 'Facets', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(201, 'FactoryLink IV', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(202, 'FAME', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(203, 'FileMaker Pro', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(204, 'FLAVORS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(205, 'FLEX', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(206, 'FlexGen', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(207, 'Focus', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(208, 'FOIL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(209, 'Forte', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(210, 'Forth', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(211, 'FORTRAN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(212, 'Fortran (default)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(213, 'Fortran 66', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(214, 'Fortran 77', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(215, 'Fortran 90', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(216, 'Fortran 95', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(217, 'FORTRAN II', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(218, 'Foundation', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(219, 'FoxPro 1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(220, 'FoxPro 2.5', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(221, 'FRAMEWORK', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(222, 'Fujitsu COBOL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(223, 'G++', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(225, 'GAMMA', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(226, 'GCC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(227, 'Genascript', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(228, 'GENER/OL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(229, 'Geneva', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(230, 'GENEXUS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(231, 'GENIFER', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(232, 'GeODE 2.0', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(233, 'GFA Basic', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(234, 'GML', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(235, 'Golden Common LISP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(236, 'GPSS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(237, 'GUEST', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(238, 'Guru', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(239, 'GW BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(240, 'HAHTSite', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(241, 'Haskell', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(242, 'High C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(243, 'HLEVEL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(244, 'HP BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(245, 'HP Changengine', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(246, 'HTML 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(247, 'HTML 3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(248, 'HTML 4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(249, 'Huron', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(250, 'IBM (VA Java 1)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(251, 'IBM (VA Java 2)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(252, 'IBM (VA Java 3)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(253, 'IBM ADF I', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(254, 'IBM ADF II', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(255, 'IBM Advanced BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(256, 'IBM CICS/VS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(257, 'IBM Compiled BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(258, 'IBM Visual Age', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(259, 'IBM VS COBOL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(260, 'IBM VS COBOL II', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(261, 'IBM Websphere', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(262, 'ICES', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(263, 'ICON', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(264, 'IDMS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(265, 'IEF', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(266, 'IEW', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(267, 'IFPS/PLUS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(268, 'IMPRS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(269, 'InConcert', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(270, 'INFORMIX', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(271, 'INGRES', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(272, 'INQUIRE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(273, 'INSIGHT2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(274, 'INSTALL/1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(275, 'INTELLECT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(276, 'Interbase SQL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(277, 'InterDev (Microsoft)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(278, 'INTERLISP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(279, 'Interpreted BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(280, 'Interpreted C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(281, 'IQLISP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(282, 'IQRP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(283, 'JANUS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(285, 'Java 2', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(286, 'Java Script', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(287, 'Jbuilder 1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(288, 'Jbuilder 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(290, 'Jbuilder 4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(291, 'JCL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(292, 'Jdeveloper 3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(293, 'JOSS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(294, 'Jovial', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(295, 'KAPPA', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(296, 'KBMS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(297, 'KCL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(298, 'KEE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(299, 'Keyplus', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(300, 'KL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(301, 'KLO', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(302, 'KNOWOL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(303, 'KRL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(304, 'KSH', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(305, 'Kylix', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(306, 'Ladder Logic', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(307, 'LAMBIT/L', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(308, 'Lattice C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(309, 'Liana', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(310, 'LILITH', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(311, 'LINC II', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(312, 'LINGO', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(313, 'Lisp', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(314, 'LOGLISP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(315, 'Logo', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(316, 'LOOPS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(317, 'LOTUS 123 DOS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(318, 'Lotus Formula', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(319, 'Lotus Script', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(320, 'LUCID 3D', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(321, 'LYRIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(322, 'M', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(323, 'macFORTH', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(324, 'MACH1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(325, 'Machine language', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(326, 'Macro assembly', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(327, 'MAESTRO', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(328, 'MAGEC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(329, 'MAGIK', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(330, 'MAKE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(331, 'MANTIS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(332, 'MAPPER', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(333, 'MARK IV', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(334, 'MARK V', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(335, 'MASM', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(336, 'MATHCAD', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(337, 'MDL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(338, 'MENTOR', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(339, 'Mercator', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(340, 'MESA', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(341, 'Microfocus COBOL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(342, 'microFORTH', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(343, 'Microsoft (VJ++)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(344, 'Microsoft C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(346, 'Microsoft VB 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(347, 'Microsoft VB 3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(349, 'Microsoft VB 5', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(350, 'Microsoft VB 6', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(351, 'Microsoft VC++ 4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(352, 'Microsoft VC++ 5', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(353, 'Microsoft VC++ 6', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(354, 'MicroStep', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(355, 'Miranda', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(356, 'Model 204', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(357, 'MODULA 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(358, 'Mosaic', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(359, 'MQ Workflow', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(360, 'MQIntegrator', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(361, 'MS C ++ V. 7', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(362, 'MS Compiled BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(363, 'MSL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(364, 'muLISP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(365, 'MUMPS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(366, 'NASTRAN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(367, 'NATURAL 1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(368, 'NATURAL 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(369, 'NATURAL Construct', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(370, 'Natural language', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(371, 'NetDynamics', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(372, 'NetObjects Fusion', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(373, 'NETRON/CAP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(374, 'NEXPERT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(375, 'NIAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(376, 'NOMAD2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(377, 'Non-procedural default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(378, 'Notes VIP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(379, 'Nroff', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(380, 'OBJECT Assembler', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(381, 'Object Assembly', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(382, 'Object Lisp', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(383, 'Object Logo', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(384, 'Object Pascal', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(385, 'Object Star', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(386, 'Objective-C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(387, 'Object-Oriented default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(388, 'ObjectVIEW', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(389, 'OGL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(390, 'OMNIS 7', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(391, 'OODL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(392, 'OpenPlus', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(393, 'OPS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(394, 'OPS5', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(395, 'Oracle 2000', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(396, 'Oracle 7 PL/SQL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(397, 'Oracle 8 PL/SQL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(398, 'Oracle Dev Tools', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(399, 'Oracle Developer/2000', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(400, 'Oracle Jdeveloper 3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(401, 'OrbixOTM', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(402, 'Oscar', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(403, 'PACBASE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(404, 'PACE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(405, 'Paradox', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(406, 'PARADOX/PAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(407, 'PASCAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(408, 'Pascal (default)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(409, 'PC FOCUS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(410, 'PDL Millenium', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(411, 'PDP-11 ADE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(412, 'PERL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(413, 'Persistance Object B', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(414, 'PILOT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(415, 'PL/1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(416, 'PL/M', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(417, 'PL/S', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(418, 'PLANIT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(419, 'PLANNER', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(420, 'PLANPERFECT 1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(421, 'PLATO', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(422, 'polyFORTH', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(423, 'POP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(424, 'POPLOG', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(425, 'Power BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(426, 'PowerBuilder', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(427, 'POWERHOUSE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(428, 'PPL (Plus)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(429, 'Problem-oriented default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(430, 'Pro-C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(431, 'Procedural default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(432, 'Process Manager', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(433, 'Professional PASCAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(434, 'Program Generator default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(435, 'PROGRESS V4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(436, 'PRO-IV', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(437, 'Prolog', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(438, 'PROSE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(439, 'Prospero', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(440, 'PROTEUS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(441, 'Python', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(442, 'QBasic', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(443, 'QBE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(444, 'QMF', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(445, 'QNIAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(446, 'QUATTRO', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(447, 'QUATTRO PRO', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(448, 'Query default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(449, 'Quick Basic 1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(450, 'Quick Basic 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(451, 'Quick Basic 3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(452, 'Quick C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(453, 'Quickbuild', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(454, 'QUIZ', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(455, 'RALLY', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(456, 'RAMIS II', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(457, 'RapidGen', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(458, 'RATFOR', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(459, 'RDB', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(460, 'Realia COBOL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(461, 'Realizer 1.0', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(462, 'Realizer 2.0', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(463, 'RELATE/3000', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(464, 'Reuse default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(465, 'REXX (MVS)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(466, 'REXX (OS/2)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(467, 'RM BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(468, 'RM COBOL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(469, 'RM FORTRAN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(470, 'Roma BSP 2.0', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(471, 'RPG', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(472, 'RPG I', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(473, 'RPG II', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(474, 'RPG III', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(475, 'RT-Expert 1.4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(476, 'SAIL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(477, 'SAPIENS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(478, 'Sapphire/Web', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(479, 'SAS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(480, 'SAVVY', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(481, 'SBASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(482, 'SCEPTRE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(483, 'SCHEME', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(484, 'Screen painter default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(485, 'SEQUAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(486, 'SHELL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(487, 'SilverStream', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(488, 'SIMPLAN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(489, 'SIMSCRIPT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(490, 'SIMULA', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(491, 'SIMULA 67', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(492, 'Simulation default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(493, 'Skyva', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(494, 'Smalltalk', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(495, 'SMALLTALK 286', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(496, 'SMALLTALK 80', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(497, 'SMALLTALK/V', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(498, 'SNAP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(499, 'SNOBOL2-4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(500, 'SoftScreen', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(501, 'SOLO', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(502, 'SPEAKEASY', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(503, 'Spinnaker PPL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(504, 'S-PLUS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(505, 'Spreadsheet default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(506, 'SPS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(507, 'SPSS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(508, 'SQL', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(509, 'SQL - Windows', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(510, 'SQL (DB/2)', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(511, 'SQL (Interbase)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(512, 'SQL (Oracle 7)', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(513, 'SQL (Oracle 8)', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(514, 'SQL Server 6.5, 7, 2000', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(515, 'SQL Server 7 SQL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(517, 'STRATEGEM', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(518, 'STRESS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(519, 'Strongly typed default', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(520, 'STYLE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(521, 'SUPERBASE 1.3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(522, 'SURPASS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(523, 'Sybase', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(524, 'Symantec C++', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(525, 'Symantic Visual Cafe', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(526, 'SYMBOLANG', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(527, 'Synchroworks', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(528, 'SYNON/2E', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(529, 'System-W', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(530, 'Tandem Access Language', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(531, 'TCL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(532, 'TELON', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(533, 'Tempest Messenger S', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(534, 'Template ETI', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(535, 'TESSARACT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(536, 'THE TWIN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(537, 'THEMIS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(538, 'TIB/Active Enterprise', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(539, 'TI-IEF', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(540, 'Topspeed C ++', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(541, 'TRANSFORM', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(542, 'TRANSLISP PLUS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(543, 'TREET', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(544, 'TREETRAN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(545, 'TRS80 BASIC II', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(546, 'TRUE BASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(547, 'Turbo Assembler', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(548, 'Turbo C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(549, 'Turbo C++', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(550, 'TURBO EXPERT', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(551, 'Turbo Pascal >5', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(552, 'Turbo Pascal 1-4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(553, 'Turbo Pascal 4-5', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(554, 'Turbo Prolog', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(555, 'TURING', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(556, 'TUTOR', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(557, 'TWAICE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(558, 'UCSD PASCAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(559, 'UFO/IMS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(560, 'UHELP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(561, 'UNIFACE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(562, 'UNIX Shell Scripts', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(563, 'VAX ACMS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(564, 'VAX ADE', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(565, 'VB Script', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(566, 'VBA', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(567, 'VECTRAN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(568, 'VHDL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(569, 'Visible C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(570, 'Visible COBOL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(571, 'Visicalc 1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(572, 'Visual 4.0', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(573, 'Visual Age', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(574, 'Visual Age Java 1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(575, 'Visual Age Java 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(576, 'Visual Age Java 3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(583, 'Visual Basic DOS', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(584, 'Visual C++ 4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(585, 'Visual C++ 5', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(586, 'Visual C++ 6', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(587, 'Visual Cafe 1', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(588, 'Visual Cafe 2', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(589, 'Visual Cafe 3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(590, 'Visual Cobol', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(591, 'Visual Foxpro 6', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(592, 'Visual J++ 3', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(593, 'Visual J++ 4', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(594, 'Visual J++ 5', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(595, 'Visual J++ 6', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(596, 'Visual Objects', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(597, 'Visual Prolog', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(598, 'VisualGen', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(599, 'VS-REXX', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(600, 'VULCAN', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(601, 'VZ Programmer', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(602, 'WARP X', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(603, 'WATCOM C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(604, 'WATCOM C/386', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(605, 'Waterloo C', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(606, 'Waterloo PASCAL', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(607, 'WATFIV', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(608, 'WATFOR', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(609, 'WebLogic', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(610, 'webMethods B2B', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(611, 'WebSphere', NULL, NULL, 'LOC', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(612, 'WHIP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(613, 'Wizard', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(614, 'XLISP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(615, 'XML', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(616, 'YACC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(617, 'YACC++', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(618, 'ZBASIC', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(619, 'ZIM', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(620, 'ZLISP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(630, '1032/AFt', NULL, TO_DATE('2008-02-23 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(632, '1st Generation default', NULL, TO_DATE('2008-02-25 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(650, 'Microsoft Word (old)', 'for old projects before jul-05 where we don''t count size of paperwork', TO_DATE('2005-05-27 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'Page', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(651, 'Pascal', NULL, TO_DATE('2003-03-03 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(654, 'Microsoft Excel (old)', 'deprecated rates, for projects before jul-05, where we don''t count paperwork size', TO_DATE('2005-05-27 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'Sheet', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(656, 'Number of', NULL, TO_DATE('2003-12-08 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'Test cases', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(657, 'Microsoft Word', '1 page= 250 words', TO_DATE('2005-05-27 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'Page 250w', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(658, 'Microsoft Excel', '1 sheet =250 cells', TO_DATE('2005-05-27 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'Sheet 250c', 1)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(659, 'PHP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(660, 'ASP.net', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(661, 'VB.net', NULL, NULL, 'LOC', NULL)
/
INSERT INTO LANGUAGE(LANGUAGE_ID, NAME, NOTE, CONV_LAST_UPDATE, SIZE_UNIT, ISRELEVANT)
  VALUES(662, 'JSP', NULL, NULL, 'LOC', NULL)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(1, 'Software', NULL)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(2, 'Requirements', 1)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(3, 'Schedule', 1)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(4, 'Effort', 1)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(5, 'Product quality', 1)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(6, 'Productivity', 1)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(7, 'Product size', 1)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(8, 'HR', NULL)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(9, 'IS', NULL)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(10, 'Process', NULL)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(11, 'Admin', NULL)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(12, 'Technology', NULL)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(13, 'Finance', NULL)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(20, 'SQA', NULL)
/
INSERT INTO METRICGROUP(MGROUP_ID, GROUPNAME, PARENT_ID)
  VALUES(21, 'DP', 20)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.1      ', 'Process Compliance', 'NC/Ob', 100100, 3, '2', 10, NULL, 65)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.10     ', 'Percentage of Satisfied Indicators of the Controlled Process', '%', 101000, NULL, NULL, 10, NULL, 95)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.11     ', 'Percentage of Feasibility Decisions of Management Review', '%', 101100, NULL, NULL, 10, NULL, 97)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.17     ', 'In Progress Projects', 'project', NULL, NULL, NULL, NULL, NULL, 67)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.18     ', 'Non Conforming Product Rate', '%', NULL, NULL, NULL, NULL, NULL, 68)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.2      ', 'Percentage of Repeated NCs', '%', 100200, NULL, NULL, 10, NULL, 90)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.3      ', 'C&P Time', 'Day', 100300, NULL, '2', 10, 'To provide the measurement of supporting ability', 66)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.4      ', 'Percentage of NCs Closed in Time', '%', 100400, NULL, NULL, 10, NULL, 91)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.5      ', 'Response Time for Request on Process', 'hour', 100500, NULL, NULL, 10, NULL, 92)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.6      ', 'Fix Time for Request on Process', 'day', 100600, NULL, NULL, 10, NULL, 98)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.7      ', 'Process Service Satisfaction', 'point', 100700, NULL, NULL, 10, NULL, 93)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.8      ', 'Timeliness of PQA Activity', '%', 100800, NULL, NULL, 10, NULL, 96)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('10.9      ', 'Percentage of Overdue Targets of Plan', '%', 100900, NULL, NULL, 10, NULL, 94)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('12.2.1    ', 'Technology Activeness', NULL, NULL, NULL, NULL, 12, NULL, 69)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('12.2.2    ', 'Technology Response Time ', NULL, NULL, NULL, NULL, 12, NULL, 70)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('12.2.3    ', 'Technology Creation ', NULL, NULL, NULL, NULL, 12, NULL, 71)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('12.2.4    ', 'Technology Learning Time ', NULL, NULL, NULL, NULL, 12, NULL, 72)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('12.3.1    ', 'Technology Motivation', NULL, NULL, NULL, NULL, 12, NULL, 73)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('12.3.2    ', 'Technology Adoption', NULL, NULL, NULL, NULL, 12, NULL, 74)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('13.1      ', 'Billable rate', '%', NULL, NULL, NULL, NULL, NULL, 75)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('13.2      ', 'Revenue', 'USD', NULL, NULL, NULL, NULL, NULL, 76)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('13.3      ', 'Cost of good sold', 'USD', NULL, NULL, NULL, NULL, NULL, 77)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('13.4      ', 'Operation expense', 'USD', NULL, NULL, NULL, NULL, NULL, 78)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('2.1       ', 'Acceptance Rate', '%', 20100, 1, '1', 2, NULL, 1)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('2.2       ', 'Requirement Completeness', '%', 20200, 1, '1', 2, 'Measures the completeness of requirements and how far to reach the destination.', 2)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('2.3       ', 'Requirement Stability', '%', 20300, 3, '1', 2, 'Provides the visibility into whether requirement changes are responsible for cost overruns, schedule delays, and decreased product quality.', 3)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.1      ', 'Timeliness of SQA Activity', '%', 200100, 3, NULL, 20, NULL, 79)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.1.1    ', 'Timeliness of Final Inspection', '%', 200101, 3, NULL, 20, NULL, 80)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.1.2    ', 'Timeliness of DP Activities', '%', 200102, 3, NULL, 21, NULL, 81)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.1.3    ', 'Response Time', 'Hour', 200103, 3, NULL, 20, NULL, 88)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.2      ', 'Rate of Baseline Audit (Conducted/Planned)', '%', 200200, 3, NULL, 20, NULL, 82)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.3      ', 'Effort of DPC by Project', 'Hour/month', 200300, 3, NULL, 21, NULL, 83)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.4      ', 'Effort for Baseline Audit', 'Hour/time', 200400, 3, NULL, 20, NULL, 84)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.5      ', 'Effort for Final Inspection', 'Hour/time', 200500, 3, NULL, 20, NULL, 85)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.6      ', 'Successful Test Coverage', '%', 200600, 3, NULL, 20, NULL, 86)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.7      ', 'Prevention Cost', '%', 200700, 3, NULL, 21, NULL, 87)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('20.8      ', 'SQA Effort for One Project', 'Hour/week', 200800, 3, NULL, 20, NULL, 89)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('3.1       ', 'Timeliness', '%', 30100, 1, '1', 3, 'Measures the ability to satisfy customer in time.', 4)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('3.2       ', 'Response Time', 'Hour', 30200, 2, '2', 3, NULL, 5)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('3.3       ', 'Duration Achievement', '%', 30300, NULL, '2', 3, NULL, 6)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('3.4.1     ', 'Project Schedule Deviation', '%', 30401, NULL, '2', 3, 'Provides information  about how well the project is being performed with respect to its schedule commitments.', 7)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('3.4.2     ', 'Stage Schedule Deviation', '%', 30402, 3, '2', 3, 'Provides information  about how well the project is being performed with respect to its schedule commitments.', 8)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('3.4.3     ', 'Delivery Schedule Deviation', '%', 30403, 2, '2', 3, 'Provides information  about how well the project is being performed with respect to its schedule commitments.', 9)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.0       ', 'Spent Effort', 'person-month', NULL, NULL, NULL, NULL, NULL, 10)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.1       ', 'Effort Efficiency', '%', 40100, NULL, '1', 4, NULL, 254)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.2       ', 'Effort Effectiveness', '%', 40101, 1, '2', 4, 'Provides tracking over the used effort against budgeted.', 11)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.3       ', 'Effort Deviation', '%', 40200, NULL, '2', 4, 'Measures the effectiveness of effort usage, project planning and managing capability.', 12)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.4       ', 'Project Management', '%', 40300, NULL, '2', 4, NULL, 13)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.5       ', 'Quality Cost', '%', 40400, 3, '2', 4, 'Provides the cost that project pays for the quality and the efficiency of quality activities in project', 14)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.1     ', 'Requirement Effort', '%', 40601, NULL, NULL, 4, NULL, 17)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.10    ', 'Project Monitoring Effort', '%', 40610, NULL, NULL, 4, NULL, 26)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.11    ', 'Training Effort', '%', 40611, NULL, NULL, 4, NULL, 27)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.12    ', 'Other Effort', '%', 40612, NULL, NULL, 4, NULL, 28)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.2     ', 'Design Effort', '%', 40602, NULL, NULL, 4, NULL, 18)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.3     ', 'Coding Effort', '%', 40603, NULL, NULL, 4, NULL, 19)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.4     ', 'Deployment Effort', '%', 40604, NULL, NULL, 4, NULL, 20)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.5     ', 'Customer Support Effort', '%', 40605, NULL, NULL, 4, NULL, 21)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.6     ', 'Test Effort', '%', 40606, NULL, NULL, 4, NULL, 22)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.7     ', 'C.M. Effort', '%', 40607, NULL, NULL, 4, NULL, 23)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.8     ', 'Quality Control Effort', '%', 40608, NULL, NULL, 4, NULL, 24)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.6.9     ', 'Project Planning Effort', '%', 40609, NULL, NULL, 4, NULL, 25)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.7.1     ', 'Initiation Stage Effort', '%', 40701, NULL, NULL, 4, NULL, 29)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.7.2     ', 'Definition Stage Effort', '%', 40702, NULL, NULL, 4, NULL, 30)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.7.3     ', 'Solution Stage Effort', '%', 40703, NULL, NULL, 4, NULL, 31)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.7.4     ', 'Construction Stage Effort', '%', 40704, NULL, NULL, 4, NULL, 32)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.7.5     ', 'Transition Stage Effort', '%', 40705, NULL, NULL, 4, NULL, 33)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.7.6     ', 'Termination Stage Effort', '%', 40706, NULL, NULL, 4, NULL, 34)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.8       ', 'Correction Cost', '%', 40500, 2, '2', 4, 'Correction Cost', 15)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9       ', 'Translation Cost', '%', 40501, 3, '1', 4, NULL, 16)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.1.1   ', 'Requirement Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 99)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.1.2   ', 'Requirement Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 100)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.1.3   ', 'Requirement Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 101)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.1.4   ', 'Requirement Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 102)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.1.5   ', 'Requirement Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 103)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.1.6   ', 'Requirement Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 104)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.10.1  ', 'Project Monitoring Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 153)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.10.2  ', 'Project Monitoring Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 154)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.10.3  ', 'Project Monitoring Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 155)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.10.4  ', 'Project Monitoring Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 156)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.10.5  ', 'Project Monitoring Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 157)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.10.6  ', 'Project Monitoring Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 158)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.11.1  ', 'Training Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 159)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.11.2  ', 'Training Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 160)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.11.3  ', 'Training Effort for Sefinition', '%', 40911, NULL, NULL, 4, NULL, 161)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.11.4  ', 'Training Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 162)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.11.5  ', 'Training Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 163)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.11.6  ', 'Training Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 164)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.12.1  ', 'Other Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 165)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.12.2  ', 'Other Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 166)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.12.3  ', 'Other Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 167)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.12.4  ', 'Other Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 168)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.12.5  ', 'Other Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 169)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.12.6  ', 'Other Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 170)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.2.1   ', 'Design Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 105)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.2.2   ', 'Design Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 106)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.2.3   ', 'Design Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 107)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.2.4   ', 'Design Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 108)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.2.5   ', 'Design Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 109)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.2.6   ', 'Design Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 110)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.3.1   ', 'Coding Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 111)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.3.2   ', 'Coding Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 112)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.3.3   ', 'Coding Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 113)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.3.4   ', 'Coding Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 114)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.3.5   ', 'Coding Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 115)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.3.6   ', 'Coding Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 116)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.4.1   ', 'Deployment Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 117)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.4.2   ', 'Deployment Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 118)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.4.3   ', 'Deployment Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 119)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.4.4   ', 'Deployment Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 120)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.4.5   ', 'Deployment Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 121)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.4.6   ', 'Deployment Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 122)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.5.1   ', 'Customer Support Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 123)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.5.2   ', 'Customer Support Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 124)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.5.3   ', 'Customer Support Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 125)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.5.4   ', 'Customer Support Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 126)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.5.5   ', 'Customer Support Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 127)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.5.6   ', 'Customer Support Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 128)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.6.1   ', 'Test Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 129)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.6.2   ', 'Test Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 130)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.6.3   ', 'Test Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 131)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.6.4   ', 'Test Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 132)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.6.5   ', 'Test Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 133)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.6.6   ', 'Test Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 134)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.7.1   ', 'Configuration Management Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 135)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.7.2   ', 'Configuration Management Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 136)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.7.3   ', 'Configuration Management Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 137)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.7.4   ', 'Configuration Management Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 138)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.7.5   ', 'Configuration Management Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 139)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.7.6   ', 'Configuration Management Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 140)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.8.1   ', 'Quality Control Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 141)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.8.2   ', 'Quality Control Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 142)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.8.3   ', 'Quality Control Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 143)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.8.4   ', 'Quality Control Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 144)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.8.5   ', 'Quality Control Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 145)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.8.6   ', 'Quality Control Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 146)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.9.1   ', 'Project Planning Effort for Initiation', '%', 40911, NULL, NULL, 4, NULL, 147)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.9.2   ', 'Project Planning Effort for Definition', '%', 40911, NULL, NULL, 4, NULL, 148)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.9.3   ', 'Project Planning Effort for Solution', '%', 40911, NULL, NULL, 4, NULL, 149)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.9.4   ', 'Project Planning Effort for Construction', '%', 40911, NULL, NULL, 4, NULL, 150)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.9.5   ', 'Project Planning Effort for Transition', '%', 40911, NULL, NULL, 4, NULL, 151)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('4.9.9.6   ', 'Project Planning Effort for Termination', '%', 40911, NULL, NULL, 4, NULL, 152)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.1       ', 'Defect Rate', 'WDef/UCP', 50100, 2, '2', 5, 'Provides the trend and the measurement of product quality', 35)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.1.1     ', 'LOC Defect Rate', 'WDef/KLOC', 50101, NULL, '2', 5, NULL, 255)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.1.1.1   ', 'Unit test Defect Rate', 'WDef/KLOC', 50111, NULL, '2', 5, NULL, 256)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.1.1.2   ', 'Integration Test Defect Rate', 'WDef/KLOC', 50112, NULL, '2', 5, NULL, 257)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.1.1.3   ', 'System Test Defect Rate', 'WDef/KLOC', 50113, NULL, '2', 5, NULL, 258)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.1.1  ', 'Requirement Review for Initiation', '%', 50911, NULL, NULL, 5, NULL, 178)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.1.2  ', 'Requirement Review for Definition', '%', 50912, NULL, NULL, 5, NULL, 179)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.1.3  ', 'Requirement Review for Solution', '%', 50913, NULL, NULL, 5, NULL, 180)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.1.4  ', 'Requirement Review for Construction', '%', 50914, NULL, NULL, 5, NULL, 181)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.1.5  ', 'Requirement Review for Transition', '%', 50915, NULL, NULL, 5, NULL, 182)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.1.6  ', 'Requirement Review for Termination', '%', 50916, NULL, NULL, 5, NULL, 183)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.2.1  ', 'Design Review for Initiation', '%', 50921, NULL, NULL, 5, NULL, 184)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.2.2  ', 'Design Review for Definition', '%', 50922, NULL, NULL, 5, NULL, 185)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.2.3  ', 'Design Review for Solution', '%', 50923, NULL, NULL, 5, NULL, 186)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.2.4  ', 'Design Review for Construction', '%', 50924, NULL, NULL, 5, NULL, 187)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.2.5  ', 'Design Review for Transition', '%', 50925, NULL, NULL, 5, NULL, 188)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.2.6  ', 'Design Review for Termination', '%', 50926, NULL, NULL, 5, NULL, 189)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.3.1  ', 'Code Review for Initiation', '%', 50931, NULL, NULL, 5, NULL, 190)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.3.2  ', 'Code Review for Definition', '%', 50932, NULL, NULL, 5, NULL, 191)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.3.3  ', 'Code Review for Solution', '%', 50933, NULL, NULL, 5, NULL, 192)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.3.4  ', 'Code Review for Construction', '%', 50934, NULL, NULL, 5, NULL, 193)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.3.5  ', 'Code Review for Transition', '%', 50935, NULL, NULL, 5, NULL, 194)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.3.6  ', 'Code Review for Termination', '%', 50936, NULL, NULL, 5, NULL, 195)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.4.1  ', 'Unit Test for Initiation', '%', 50941, NULL, NULL, 5, NULL, 196)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.4.2  ', 'Unit Test for Definition', '%', 50942, NULL, NULL, 5, NULL, 197)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.4.3  ', 'Unit Test for Solution', '%', 50943, NULL, NULL, 5, NULL, 198)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.4.4  ', 'Unit Test for Construction', '%', 50944, NULL, NULL, 5, NULL, 199)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.4.5  ', 'Unit Test for Transition', '%', 50945, NULL, NULL, 5, NULL, 200)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.4.6  ', 'Unit Test for Termination', '%', 50946, NULL, NULL, 5, NULL, 201)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.5.1  ', 'Integration Test for Initiation', '%', 50951, NULL, NULL, 5, NULL, 202)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.5.2  ', 'Integration Test for Definition', '%', 50952, NULL, NULL, 5, NULL, 203)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.5.3  ', 'Integration Test for Solution', '%', 50953, NULL, NULL, 5, NULL, 204)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.5.4  ', 'Integration Test for Construction', '%', 50954, NULL, NULL, 5, NULL, 205)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.5.5  ', 'Integration Test for Transition', '%', 50955, NULL, NULL, 5, NULL, 206)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.5.6  ', 'Integration Test for Termination', '%', 50956, NULL, NULL, 5, NULL, 207)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.6.1  ', 'System Test for Initiation', '%', 50961, NULL, NULL, 5, NULL, 208)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.6.2  ', 'System Test for Definition', '%', 50962, NULL, NULL, 5, NULL, 209)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.6.3  ', 'System Test for Solution', '%', 50963, NULL, NULL, 5, NULL, 210)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.6.4  ', 'System Test for Construction', '%', 50964, NULL, NULL, 5, NULL, 211)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.6.5  ', 'System Test for Transition', '%', 50965, NULL, NULL, 5, NULL, 212)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.6.6  ', 'System Test for Termination', '%', 50966, NULL, NULL, 5, NULL, 213)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.7.1  ', 'Other Review for Initiation', '%', 50971, NULL, NULL, 5, NULL, 242)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.7.2  ', 'Other Review for Definition', '%', 50972, NULL, NULL, 5, NULL, 243)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.7.3  ', 'Other Review for Solution', '%', 50973, NULL, NULL, 5, NULL, 244)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.7.4  ', 'Other Review for Construction', '%', 50974, NULL, NULL, 5, NULL, 245)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.7.5  ', 'Other Review for Transition', '%', 50975, NULL, NULL, 5, NULL, 246)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.7.6  ', 'Other Review for Termination', '%', 50976, NULL, NULL, 5, NULL, 247)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.8.1  ', 'Other Test for Initiation', '%', 50981, NULL, NULL, 5, NULL, 248)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.8.2  ', 'Other Test for Definition', '%', 50982, NULL, NULL, 5, NULL, 249)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.8.3  ', 'Other Test for Solution', '%', 50983, NULL, NULL, 5, NULL, 250)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.8.4  ', 'Other Test for Construction', '%', 50984, NULL, NULL, 5, NULL, 251)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.8.5  ', 'Other Test for Transition', '%', 50985, NULL, NULL, 5, NULL, 252)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.8.6  ', 'Other Test for Termination', '%', 50986, NULL, NULL, 5, NULL, 253)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.9.1  ', 'Others for Initiation', '%', 50991, NULL, NULL, 5, NULL, 214)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.9.2  ', 'Others for Definition', '%', 50992, NULL, NULL, 5, NULL, 215)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.9.3  ', 'Others for Solution', '%', 50993, NULL, NULL, 5, NULL, 216)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.9.4  ', 'Others for Construction', '%', 50994, NULL, NULL, 5, NULL, 217)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.9.5  ', 'Others for Transition', '%', 50995, NULL, NULL, 5, NULL, 218)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.10.9.6  ', 'Others for Termination', '%', 50996, NULL, NULL, 5, NULL, 219)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.11.1    ', 'Defect Rate by URD', 'WD/UCP', 50977, NULL, NULL, 5, NULL, 220)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.11.10   ', 'Defect Rate by Other', 'WD/UCP', 50985, NULL, NULL, 5, NULL, 229)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.11.2    ', 'Defect Rate by SRS', 'WD/UCP', 50978, NULL, NULL, 5, NULL, 221)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.11.3    ', 'Defect Rate by RPrototype', 'WD/UCP', 50979, NULL, NULL, 5, NULL, 222)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.11.4    ', 'Defect Rate by ADD', 'WD/UCP', 50980, NULL, NULL, 5, NULL, 223)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.11.5    ', 'Defect Rate by DDD', 'WD/UCP', 50981, NULL, NULL, 5, NULL, 224)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.11.6    ', 'Defect Rate by DPrototype', 'WD/UCP', 50982, NULL, NULL, 5, NULL, 225)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.11.7    ', 'Defect Rate by Software Package', 'WD/UCP', 50983, NULL, NULL, 5, NULL, 226)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.11.8    ', 'Defect Rate by TC_TData', 'WD/UCP', 50983, NULL, NULL, 5, NULL, 227)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.11.9    ', 'Defect Rate by User Manual', 'WD/UCP', 50984, NULL, NULL, 5, NULL, 228)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.12.1    ', 'Defect Removal Efficiency by URD', '%', 50986, NULL, NULL, 5, NULL, 230)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.12.10   ', 'Defect Removal Efficiency by Other', '%', 50995, NULL, NULL, 5, NULL, 239)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.12.2    ', 'Defect Removal Efficiency by SRS', '%', 50987, NULL, NULL, 5, NULL, 231)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.12.3    ', 'Defect Removal Efficiency by RPrototype', '%', 50988, NULL, NULL, 5, NULL, 232)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.12.4    ', 'Defect Removal Efficiency by ADD', '%', 50989, NULL, NULL, 5, NULL, 233)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.12.5    ', 'Defect Removal Efficiency by DDD', '%', 50990, NULL, NULL, 5, NULL, 234)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.12.6    ', 'Defect Removal Efficiency by DPrototype', '%', 50991, NULL, NULL, 5, NULL, 235)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.12.7    ', 'Defect Removal Efficiency by Software Package', '%', 50992, NULL, NULL, 5, NULL, 236)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.12.8    ', 'Defect Removal Efficiency by TC_TData', '%', 50993, NULL, NULL, 5, NULL, 237)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.12.9    ', 'Defect Removal Efficiency by User Manual', '%', 50994, NULL, NULL, 5, NULL, 238)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.2       ', 'Leakage', 'WDef/UCP', 50200, 1, '2', 5, 'Provides the trend and the measurement of product quality', 36)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.3       ', 'Defect Removal Efficiency', '%', 50300, NULL, '1', 5, 'Provides the efficiency of review and test activities', 37)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.3.1     ', 'Review Efficiency', '%', 50301, NULL, '0', 5, NULL, 38)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.3.2     ', 'Test Efficiency', '%', 50302, NULL, '0', 5, NULL, 39)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.4       ', 'Quality Achievement', '%', 50400, NULL, '2', 5, NULL, 40)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.5       ', 'Customer Satisfaction', 'Point', 50500, 1, '1', 5, 'Provides the customer satisfaction and measures the success of project', 41)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.6       ', 'Test Case Density', 'TC/KLOC', 50600, NULL, '1', 5, NULL, 261)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.6.1     ', 'Unit Test Case Density', 'TC/KLOC', 50601, NULL, '1', 5, NULL, 262)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.6.2     ', 'Integration Test Case Density', 'TC/KLOC', 50602, NULL, '1', 5, NULL, 263)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.6.3     ', 'System Test Case Density', 'TC/KLOC', 50603, NULL, '1', 5, NULL, 264)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.7       ', 'Customer Complaints', '#', 50700, 3, '1', 5, NULL, 42)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.8       ', 'Overdue NCs&Obs', '#', NULL, 3, '1', 5, NULL, 420)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.8.1     ', 'Defects from Requirements', '%', 50701, NULL, NULL, 5, NULL, 43)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.8.2     ', 'Defects from Design', '%', 50702, NULL, NULL, 5, NULL, 44)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.8.3     ', 'Defects from Coding', '%', 50703, NULL, NULL, 5, NULL, 45)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.8.4     ', 'Defects from Other', '%', 50704, NULL, NULL, 5, NULL, 46)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.9.1.1   ', 'Requirement Review', '%', 50871, NULL, NULL, 5, NULL, 171)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.9.1.2   ', 'Design Review', '%', 50872, NULL, NULL, 5, NULL, 172)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.9.1.3   ', 'Code Review', '%', 50873, NULL, NULL, 5, NULL, 173)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.9.1.4   ', 'Unit Test', '%', 50874, NULL, NULL, 5, NULL, 174)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.9.1.5   ', 'Integration Test', '%', 50875, NULL, NULL, 5, NULL, 175)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.9.1.6   ', 'System Test', '%', 50876, NULL, NULL, 5, NULL, 176)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.9.1.7   ', 'Other Review', '%', 50877, NULL, NULL, 5, NULL, 240)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.9.1.8   ', 'Other Test', '%', 50878, NULL, NULL, 5, NULL, 241)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('5.9.1.9   ', 'Others', '%', 50879, NULL, NULL, 5, NULL, 177)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('6.1       ', 'UCP Productivity', 'UCP/pd', 60100, 3, '1', 6, 'Provides the measurement about general productivity in project.', 47)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('6.1.1     ', 'LOC Productivity', 'LOC/pd', 60101, NULL, '1', 6, NULL, 259)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('6.2       ', 'Review Effectiveness', 'WDef/pd', 60200, 2, '1', 6, 'Provides  a measurement about the productivity of review and test activities', 48)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('6.3       ', 'Test Effectiveness', 'WDef/pd', 60300, 2, '1', 6, 'Provides  a measurement about the productivity of review and test activities', 49)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('6.4       ', 'UCP Productivity Achievement', '%', 60400, NULL, '1', 6, 'Provides the measurement about general productivity in project.', 50)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('6.4.1     ', 'LOC Productivity Achievement', '%', 60401, NULL, '1', 6, NULL, 260)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('7.0       ', 'Total Size', 'UCP', 70000, 2, NULL, NULL, 'Provides tracking  over the size of software products against estimated. This metric  is also the base/also serves as the base  for other metrics, for example productivity, or quality of product.', 51)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('7.1       ', 'Size Achievement', '%', 70100, NULL, '2', 7, NULL, 52)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('7.2       ', 'Size Deviation', '%', 70200, NULL, '2', 7, 'Provides information about how well the estimation is performed.', 53)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('8.12      ', 'Turnover Rate', '%', 81200, 3, NULL, NULL, NULL, 54)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('8.13      ', 'Skill Set', 'Point', 81300, 3, NULL, NULL, NULL, 55)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('8.14      ', 'ERI', 'month', 81400, 3, NULL, NULL, NULL, 56)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('8.34      ', 'Total Staff', 'person', NULL, NULL, NULL, 8, NULL, 57)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('8.34.6    ', 'Total Developers', 'person', NULL, NULL, NULL, 8, NULL, 58)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('8.34.7    ', 'Total Temporary Staff', 'person', NULL, NULL, NULL, 8, NULL, 59)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('8.37      ', 'Busy Rate', '%', NULL, NULL, NULL, 8, NULL, 60)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('9.1.1     ', 'Network Down Time', 'Min', 90101, 2, NULL, NULL, NULL, 61)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('9.10      ', 'Internet Bandwidth', 'Mbps', NULL, NULL, NULL, 9, NULL, 64)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('9.5.1     ', 'Fixed Time', 'Min', 90501, 2, NULL, NULL, NULL, 62)
/
INSERT INTO METRIC_DES(METRIC_ID, METRIC_NAME, UNIT, DISPLAY_INDEX, HOT_PRIORITY, COLOR_TYPE, MGROUP_ID, METRIC_DESC, ID)
  VALUES('9.9       ', 'Perfect Time', 'Day/month', 90900, 2, NULL, NULL, NULL, 63)
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('AUDIT', 'BASELINE_AUDIT')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('AUDIT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('INSPECTION', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('INSPECTION', 'UT_INSPECTION')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('INSPECTION', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('INSPECTION', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('REVIEW', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('REVIEW', 'CODE_REVIEW')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('REVIEW', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('REVIEW', 'PROTOTYPE_REVIEW')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('REVIEW', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('TEST', 'UNIT_TEST')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('TEST', 'INTEGRATION_TEST')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('TEST', 'SYSTEM_TEST')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('TEST', 'ACCEPTANCE_TEST')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('TEST', 'REGRESSION_TEST')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('TEST', 'AFTER_RELEASE_TEST')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('TEST', 'OTHER_TEST')
/
INSERT INTO QCACTIVITY_ACTIVITYTYPE(TYPE_CODE, QC_CODE)
  VALUES('TEST', 'PROTOTYPE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ACCEPTANCE_NOTE', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ACCEPTANCE_NOTE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ACCEPTANCE_NOTE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ACCEPTANCE_NOTE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ACCEPTANCE_NOTE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ACCEPTANCE_NOTE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ARCHITECTURAL_DESIGN', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ARCHITECTURAL_DESIGN', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ARCHITECTURAL_DESIGN', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ARCHITECTURAL_DESIGN', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ARCHITECTURAL_DESIGN', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ARCHITECTURAL_DESIGN', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('ARCHITECTURAL_DESIGN', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_PROGRAM', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_PROGRAM', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_PROGRAM', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_PROGRAM', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_PROGRAM', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_RECORD', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_RECORD', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_RECORD', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_RECORD', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_RECORD', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('AUDIT_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('BASELINE_REPORT', 'BASELINE_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('BASELINE_REPORT', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('BASELINE_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('BASELINE_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('BASELINE_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('BASELINE_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CHANGE_REQUEST', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CHANGE_REQUEST', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CHANGE_REQUEST', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CHANGE_REQUEST', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CHANGE_REQUEST', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CM_PLAN', 'BASELINE_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CM_PLAN', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CM_PLAN', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CM_PLAN', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CM_PLAN', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CM_PLAN', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CODING_CONVENTION', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CODING_CONVENTION', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CODING_CONVENTION', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CODING_CONVENTION', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CODING_CONVENTION', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CODING_CONVENTION', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CODING_CONVENTION', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CONFIGURATION_STATUS_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CONFIGURATION_STATUS_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CONFIGURATION_STATUS_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CONFIGURATION_STATUS_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CONFIGURATION_STATUS_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CONTRACT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CONTRACT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CONTRACT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CONTRACT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CONTRACT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CUSTOMER_SATISFACTION_SURVEY', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CUSTOMER_SATISFACTION_SURVEY', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CUSTOMER_SATISFACTION_SURVEY', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CUSTOMER_SATISFACTION_SURVEY', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('CUSTOMER_SATISFACTION_SURVEY', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'ACCEPTANCE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'UT_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'AFTER_RELEASE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'REGRESSION_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'SYSTEM_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'INTEGRATION_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DATABASE', 'UNIT_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DEPLOYMENT_PACKAGE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DEPLOYMENT_PACKAGE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DEPLOYMENT_PACKAGE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DEPLOYMENT_PACKAGE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DEPLOYMENT_PACKAGE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DESIGN_PROTOTYPE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DESIGN_PROTOTYPE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DESIGN_PROTOTYPE', 'PROTOTYPE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DESIGN_PROTOTYPE', 'PROTOTYPE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DESIGN_PROTOTYPE', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DESIGN_PROTOTYPE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DESIGN_PROTOTYPE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DESIGN_PROTOTYPE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DESIGN_PROTOTYPE', 'OTHER_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DETAILED_DESIGN', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DETAILED_DESIGN', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DETAILED_DESIGN', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DETAILED_DESIGN', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DETAILED_DESIGN', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DETAILED_DESIGN', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DETAILED_DESIGN', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DP_REPORT', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DP_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DP_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DP_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DP_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('DP_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('HANDOVER_NOTE', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('HANDOVER_NOTE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('HANDOVER_NOTE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('HANDOVER_NOTE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('HANDOVER_NOTE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('HANDOVER_NOTE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INSTALLATION_MANUAL', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INSTALLATION_MANUAL', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INSTALLATION_MANUAL', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INSTALLATION_MANUAL', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INSTALLATION_MANUAL', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INSTALLATION_MANUAL', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INSTALLATION_MANUAL', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_CASE', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_CASE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_CASE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_CASE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_CASE', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_CASE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_CASE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_PLAN', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_PLAN', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_PLAN', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_PLAN', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_PLAN', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_PLAN', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_REPORT', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_REPORT', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('INTEGRATION_TEST_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('IP', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('IP', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('IP', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('IP', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('IP', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('IP_DATABASE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('IP_DATABASE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('IP_DATABASE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('IP_DATABASE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('IP_DATABASE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('MEETING_MINUTES', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('MEETING_MINUTES', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('MEETING_MINUTES', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('MEETING_MINUTES', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('MEETING_MINUTES', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('MEETING_MINUTES', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('OTHERS', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('OTHERS', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('OTHERS', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('OTHERS', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('OTHERS', 'OTHER_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('OTHERS', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PCB', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PCB', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PCB', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PCB', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PCB', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PCB', 'OTHER_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_PLAN', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_PLAN', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_PLAN', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_PLAN', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_PLAN', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_RECORD', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_RECORD', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_RECORD', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_RECORD', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_RECORD', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PILOT_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PLAN', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PLAN', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PLAN', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PLAN', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PLAN', 'OTHER_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PLAN', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PQA_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PQA_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PQA_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PQA_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PQA_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROCESS_ASSETS', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROCESS_ASSETS', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROCESS_ASSETS', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROCESS_ASSETS', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROCESS_ASSETS', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROCESS_DATABASE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROCESS_DATABASE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROCESS_DATABASE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROCESS_DATABASE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROCESS_DATABASE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROGRAM', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROGRAM', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROGRAM', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROGRAM', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROGRAM', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_ASSETS', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_ASSETS', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_ASSETS', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_ASSETS', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_ASSETS', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_DATABASE', 'BASELINE_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_DATABASE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_DATABASE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_DATABASE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_DATABASE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_PLAN', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_PLAN', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_PLAN', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_PLAN', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_PLAN', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_PLAN', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_PLAN', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_RECORD', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_RECORD', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_RECORD', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_RECORD', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_RECORD', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_RECORD', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_REPORT', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_REPORT', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROJECT_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROPOSAL', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROPOSAL', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROPOSAL', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROPOSAL', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROPOSAL', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROPOSAL', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('PROPOSAL', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QDS', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QDS', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QDS', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QDS', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QDS', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_CONTROL_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_CONTROL_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_CONTROL_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_CONTROL_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_CONTROL_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_GATE_RECORD', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_GATE_RECORD', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_GATE_RECORD', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_GATE_RECORD', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_GATE_RECORD', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('QUALITY_GATE_RECORD', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RECORD', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RECORD', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RECORD', 'OTHER_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RECORD', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RECORD', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RECORD', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RELEASE_NOTE', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RELEASE_NOTE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RELEASE_NOTE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RELEASE_NOTE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RELEASE_NOTE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RELEASE_NOTE', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RELEASE_NOTE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REPORT', 'OTHER_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REQUIREMENT_PROTOTYPE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REQUIREMENT_PROTOTYPE', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REQUIREMENT_PROTOTYPE', 'PROTOTYPE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REQUIREMENT_PROTOTYPE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REQUIREMENT_PROTOTYPE', 'PROTOTYPE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REQUIREMENT_PROTOTYPE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REQUIREMENT_PROTOTYPE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REQUIREMENT_PROTOTYPE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REQUIREMENT_PROTOTYPE', 'OTHER_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RESOURCE_AND_ENVIRONMENT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RESOURCE_AND_ENVIRONMENT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RESOURCE_AND_ENVIRONMENT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RESOURCE_AND_ENVIRONMENT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RESOURCE_AND_ENVIRONMENT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('RESOURCE_AND_ENVIRONMENT', 'OTHER_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REVIEW_RECORD', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REVIEW_RECORD', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REVIEW_RECORD', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REVIEW_RECORD', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REVIEW_RECORD', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('REVIEW_RECORD', 'OTHER_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SERVICE_LEVEL_AGREEMENT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SERVICE_LEVEL_AGREEMENT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SERVICE_LEVEL_AGREEMENT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SERVICE_LEVEL_AGREEMENT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SERVICE_LEVEL_AGREEMENT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'CODE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'AFTER_RELEASE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'REGRESSION_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'INTEGRATION_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'SYSTEM_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'UT_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'UNIT_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'ACCEPTANCE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_MODULE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'CODE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'UNIT_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'AFTER_RELEASE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'REGRESSION_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'ACCEPTANCE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'SYSTEM_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'INTEGRATION_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SOFTWARE_PACKAGE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SQA_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SQA_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SQA_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SQA_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SQA_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SRS', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SRS', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SRS', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SRS', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SRS', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SRS', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SRS', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SUPPORT_DIARY', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SUPPORT_DIARY', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SUPPORT_DIARY', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SUPPORT_DIARY', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SUPPORT_DIARY', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_DESCRIPTION', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_DESCRIPTION', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_DESCRIPTION', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_DESCRIPTION', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_DESCRIPTION', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_DESCRIPTION', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_DESCRIPTION', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_CASE', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_CASE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_CASE', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_CASE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_CASE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_CASE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_CASE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_PLAN', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_PLAN', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_PLAN', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_PLAN', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_PLAN', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_PLAN', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_REPORT', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('SYSTEM_TEST_REPORT', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_DATA', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_DATA', 'UT_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_DATA', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_DATA', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_DATA', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_DATA', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_DATA', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_DATA', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'REGRESSION_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'AFTER_RELEASE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'ACCEPTANCE_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'SYSTEM_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'INTEGRATION_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'UNIT_TEST')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TEST_SCRIPT', 'UT_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRACEABILITY_MATRIX', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRACEABILITY_MATRIX', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRACEABILITY_MATRIX', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRACEABILITY_MATRIX', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRACEABILITY_MATRIX', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_COURSE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_COURSE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_COURSE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_COURSE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_COURSE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_MATERIAL', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_MATERIAL', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_MATERIAL', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_MATERIAL', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_MATERIAL', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_RECORDS', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_RECORDS', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_RECORDS', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_RECORDS', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('TRAINING_RECORDS', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_CASE', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_CASE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_CASE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_CASE', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_CASE', 'UT_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_CASE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_CASE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_CASE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_PLAN', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_PLAN', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_PLAN', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_PLAN', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_PLAN', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_PLAN', 'UT_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_PLAN', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_REPORT', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_REPORT', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_REPORT', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_REPORT', 'UT_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_REPORT', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_REPORT', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_REPORT', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('UNIT_TEST_REPORT', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('URD', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('URD', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('URD', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('URD', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('URD', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('URD', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USER_MANUAL', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USER_MANUAL', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USER_MANUAL', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USER_MANUAL', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USER_MANUAL', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USER_MANUAL', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USER_MANUAL', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USE_CASE', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USE_CASE', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USE_CASE', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USE_CASE', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USE_CASE', 'FINAL_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USE_CASE', 'AFTER_RELEASE_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('USE_CASE', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('WO', 'DOCUMENT_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('WO', 'OTHER_AUDIT')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('WO', 'OTHER_REVIEW')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('WO', 'OTHER_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('WO', 'QUALITY_GATE_INSPECTION')
/
INSERT INTO QCACTIVITY_WORKPRODUCT(WP_CODE, QC_CODE)
  VALUES('WO', 'AFTER_RELEASE_REVIEW')
/
quit