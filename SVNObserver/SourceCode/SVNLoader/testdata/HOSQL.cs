using System;
using CMS.Framework.DataAccess;
using CMS.SIP.Library.Alarm;
using CMS.SIP.Library.Session;
using System.Data;
using System.Collections.Generic;
using System.Diagnostics;
using CMS.SIP.Library.Entity;
using CMS.Framework.Common.Exceptions;

namespace CMS.SIP.Library.HierarchyObject
{
    /// <summary>
    /// Provides embodiment of table, it can be used as a base object
    /// To use it, user need to create a new record in HierachyView table
    /// and define some config in ConfigOptions column as below:
    ///     TableName(Required): Name of table
    ///     KeyColumn(Required): Key column of that table
    ///     Identity(Required): Key column is identity or not
    ///     NameColumn(Required): Name of column will display in Icon mode
    ///     Icon(Required): Display icon of HOSQL
    ///     ConnectionString(Required): connection string to database
    ///     SQLCriteria(Option): Filter condition to get selected data
    ///     BaseName(Option): Name of HOSQL object, in order to get resource key and Action dynamic
    /// </summary>
    [Serializable]
    public class HOSQL : BaseHierarchyObject
    {
        #region local constant

        // object description resource keys

        protected string HOSQL_ADD = "_ADD";
        protected string HOSQL_DEL = "_DEL";
        protected string HOSQL_SHOWPOPUP = "NAMEDACTION_SHOWPOPUP";

        // object description resource keys
        protected string HOSQL_APPLICATION_NAME = "_APPLICATION_NAME";
        protected string HOSQL_FILE_OPTIONS = "_FILE_OPTIONS";
        protected string HOSQL_FOLDER_NAME = "_FOLDER_NAME";
        protected string HOSQL_FOLDER_OPTIONS = "_FOLDER_OPTIONS";

        protected string HOSQL_ADD_MESSAGE = "_ADD_MESSAGE"; // action prompt
        protected string HOSQL_DEL_MESSAGE = "_DEL_MESSAGE"; // action prompt
        protected string HOSQL_NO_DATA_MESSAGE = "_NO_DATA_MESSAGE";
        protected string HOSQL_DEL_FAILED_MESSAGE = "_DEL_FAILED_MESSAGE"; // action prompt


        private const string HELP_KEY_WORD = "HOSQL";
        private const string DEFAULT_BASE_NAME = "HOSQL";

        private string m_SQLCriteria;
        private const string APPLICATION_ID = "APPLICATION_ID";

        #endregion

        #region Constructor / Initialisation

        /// <summary>
        /// Constructor
        /// </summary>
        public HOSQL()
        {
            // Set up local SQLFolderList (as an internal data source) 
        }

        /// <summary>
        /// Set up this object
        /// </summary>
        /// <param name="node"></param>
        /// <param name="fullPath">Hierarchy position</param>
        /// /// <param name="permissions"></param>
        public override void Init(HierarchyNode node, String fullPath, Permissions permissions)
        {
            base.Init(node, fullPath, permissions);

            ConfigOptions configOptions = base.ViewEntry.ConfigOptions;
            var tableName = (string)configOptions.GetProperty("TableName");
            var isIdentity = (string)configOptions.GetProperty("Identity");
            var keyCol = (string)configOptions.GetProperty("KeyColumn");
            var nameCol = (string)configOptions.GetProperty("NameColumn");
            var iconCol = (string)configOptions.GetProperty("Icon");
            var connectionString = (string)configOptions.GetProperty("ConnectionString");
            var baseName = (string)configOptions.GetProperty("BaseName");
            m_SQLCriteria = (string)configOptions.GetProperty("SQLCriteria");
            var filterValues = (string)configOptions.GetProperty("FilterValues");

            //get Application ID in hierachy entry
            object valueStr = Permissions.GetValue(APPLICATION_ID);
            int valueInt = 0;
            if (valueStr != null)
            {
                if (Int32.TryParse(valueStr.ToString(), out valueInt))
                {
                    base.ViewEntry.AppID = valueInt;
                }
                else if (base.ViewEntry.AppID < 0)
                {
                    base.ViewEntry.AppID = HierarchyObjectConstants.SIP_APPLICATION_ID;
                }
            }

            string message = string.Empty;
            bool isInValid = ValidateConfig(tableName, keyCol, nameCol, iconCol, connectionString, isIdentity, out message);

            if (isInValid)
            {
                throw new Exception(message);
            }

            // Set default HOSQL Name
            if (string.IsNullOrEmpty(baseName))
            {
                baseName = DEFAULT_BASE_NAME;
            }

            HOSQL_DEL = baseName + HOSQL_DEL;
            HOSQL_ADD = baseName + HOSQL_ADD;
            HOSQL_APPLICATION_NAME = baseName + HOSQL_APPLICATION_NAME;
            HOSQL_FILE_OPTIONS = baseName + HOSQL_FILE_OPTIONS;
            HOSQL_FOLDER_NAME = baseName + HOSQL_FOLDER_NAME;
            HOSQL_FOLDER_OPTIONS = baseName + HOSQL_FOLDER_OPTIONS;
            HOSQL_ADD_MESSAGE = baseName + HOSQL_ADD_MESSAGE; // action prompt
            HOSQL_DEL_MESSAGE = baseName + HOSQL_DEL_MESSAGE; // action prompt
            HOSQL_NO_DATA_MESSAGE = baseName + HOSQL_NO_DATA_MESSAGE;
            HOSQL_DEL_FAILED_MESSAGE = baseName + HOSQL_DEL_FAILED_MESSAGE; // action prompt

            if (!string.IsNullOrEmpty(filterValues))
            {
                SQLFolderList = new SqlDataAccess(connectionString, tableName, keyCol, nameCol, iconCol, isIdentity, filterValues);
                Schema = new SqlDataAccess(connectionString, tableName, keyCol, nameCol, iconCol, isIdentity, filterValues);
            }
            else
            {
                SQLFolderList = new SqlDataAccess(connectionString, tableName, keyCol, nameCol, iconCol, isIdentity);
                Schema = new SqlDataAccess(connectionString, tableName, keyCol, nameCol, iconCol, isIdentity);
            }


            Actions.Add(new SqlAction(HierarchyObjectConstants.NAMEDACTION_ADD, GetText(HOSQL_ADD), HierarchyObjectConstants.ICON_ACTION_ADD, GetText(HOSQL_ADD_MESSAGE), string.Empty, string.Empty, SQLFolderList.TranslatedKeyCol, FullPath));
            m_ChildActions.Add(new Action(HierarchyObjectConstants.NAMEDACTION_DEL, GetText(HOSQL_DEL), SQLFolderList.TranslatedKeyCol));

            HelpKeyWord = HELP_KEY_WORD;

            Info.ApplicationName = string.Format(GetText(HOSQL_APPLICATION_NAME), Info.Name);
            Info.FolderName = string.Format(GetText(HOSQL_FOLDER_NAME), Info.Name);
            Info.FolderOptionsText = string.Format(GetText(HOSQL_FOLDER_OPTIONS), Info.Name);
            Info.FileOptionsText = string.Format(GetText(HOSQL_FILE_OPTIONS), Info.Name);
            if (string.IsNullOrEmpty(this.ViewEntry.Icon))
                this.ViewEntry.Icon = HierarchyObjectConstants.ICON_HOSQL;

            AddAction();
        }
        /// <summary>
        /// Todo: Validate Configuration Options 
        /// </summary>
        /// <param name="tableName">Table Name</param>
        /// <param name="keyCol">Key Column</param>
        /// <param name="nameCol">Name Column</param>
        /// <param name="iconCol">Icon</param>
        /// <param name="connectionString">connection String</param>
        /// <param name="isIdentity">Identity</param>
        /// <param name="message">Message</param>
        /// <returns></returns>
        private bool ValidateConfig(string tableName, string keyCol, string nameCol, string iconCol, string connectionString, string isIdentity, out string message)
        {
            message = string.Empty;
            List<string> messages = new List<string>();
            bool isInValid = false;

            if (string.IsNullOrEmpty(tableName))
            {
                isInValid = true;
                messages.Add("Table Name");
            }
            if (string.IsNullOrEmpty(keyCol))
            {
                isInValid = true;
                messages.Add("Key Column");
            }

            if (string.IsNullOrEmpty(nameCol))
            {
                isInValid = true;
                messages.Add("Name Column");
            }
            if (string.IsNullOrEmpty(iconCol))
            {
                isInValid = true;
                messages.Add("Icon");
            }

            if (string.IsNullOrEmpty(connectionString))
            {
                isInValid = true;
                messages.Add("Connection String");
            }

            if (string.IsNullOrEmpty(isIdentity))
            {
                isInValid = true;
                messages.Add("Identity property");
            }

            if (isInValid)
            {
                message = "Configuration is missing " + string.Join(", ", messages.ToArray());
            }
            return isInValid;
        }

        #endregion

        #region Properties

        /// <summary>
        /// Get/Set SQLFolderList object
        /// </summary>
        public SqlDataAccess SQLFolderList
        {
            get;
            set;
        }

        /// <summary>
        /// Get/Set schema object
        /// </summary>
        public SqlDataAccess Schema
        {
            get;
            set;
        }

        #endregion

        #region Methods

        /// <summary>
        /// Add actions for HOSQL
        /// </summary>
        protected virtual void AddAction()
        {
        }

        /// <summary>
        /// Return contents of the current node
        /// </summary>
        public override IHierarchyDataAccess ShowFolderContent()
        {
            try
            {
                HierarchyViewFlags flag = base.Folder_Permission;
                SQLFolderList.ResetFields(this);
                SQLFolderList.GetFieldSpec().NoDataMessage = GetText(HOSQL_NO_DATA_MESSAGE);
                SQLFolderList.GetFieldSpec().PreferredView = this.ViewEntry.GUIViewName;

                SQLFolderList.SetUpData(flag, m_SQLCriteria, false);

                return SQLFolderList;
            }
            catch (Exception ex)
            {
                throw new ActionResult(GetText(HierarchyObjectConstants.SIP_HOSQL_BAD_CONNECTION),
                    FullPath, "Show Folder")
                    {
                        Icon = HierarchyObjectConstants.ICON_DIALOG_ERROR
                    };
            }

        }

        /// <summary>
        /// Return contents of the structure table
        /// </summary>
        public IHierarchyDataAccess CreateStructureOfTable()
        {
            try
            {
                HierarchyViewFlags flag = base.Folder_Permission;
                Schema.ResetFields(this);
                Schema.GetStructureOfTable(flag);
                return Schema;
            }
            catch (Exception ex)
            {
                throw new ActionResult(GetText(HierarchyObjectConstants.SIP_HOSQL_BAD_CONNECTION),
                    FullPath, "Create Schema")
                {
                    Icon = HierarchyObjectConstants.ICON_DIALOG_ERROR
                };
            }

        }

        /// <summary>
        /// Apply the named action
        /// </summary>
        /// <param name="actionName">The action name to apply</param>
        /// <param name="parameter">Any action property</param>
        public override ActionResult DoNamedAction(String actionName, string parameter)
        {
            ActionResult result = null;
            switch (actionName)
            {
                case HierarchyObjectConstants.NAMEDACTION_DEL:
                    result = Action_Delete(parameter);
                    break;
                default:
                    result = base.DoNamedAction(actionName, parameter);
                    break;
            }
            return result;
        }

        /// <summary>
        /// Action Delete
        /// </summary>
        /// <param name="keyValue"></param>
        /// <returns></returns>
        private ActionResult Action_Delete(string keyValue)
        {
            ActionResult result;

            try
            {
                SQLFolderList.DeleteRecord(keyValue);
                result = new ActionResult
                {
                    Type = ActionResultType.GoToFolder,
                    NewLocation = ExecutedPath
                };
            }
            catch (Exception ex)
            {
                // log entry
                //AlarmUtility.Debug(TraceLevel.Error, 0, string.Empty, ex.Message);

                result = new ActionResult(GetText(HOSQL_DEL_FAILED_MESSAGE), FullPath, "Delete") { Icon = HierarchyObjectConstants.ICON_DIALOG_ERROR };
            }
            return result;
        }

        /// <summary>
        /// Submit changes of data that this object manages.
        /// </summary>
        /// <param name="dataAccess">The <see cref="IHierarchyDataAccess"/> object contains the changes.</param>
        /// <returns></returns>
        public override ActionResult SubmitData(IHierarchyDataAccess dataAccess)
        {
            try
            {
                //Create new all fieldName, translateFieldNames etc
                if (SQLFolderList.GetFieldSpec().FieldNames == null)
                {
                    SQLFolderList.ResetFields(this);
                    SQLFolderList = (SqlDataAccess)CreateStructureOfTable();
                }
                SQLFolderList.SubmitRecord(dataAccess, m_SQLCriteria);
                ActionResult result = new ActionResult
                {
                    Type = ActionResultType.GoToFolder,
                    NewLocation = HierarchyObjectConstants.PATH_CURRENT,
                };
                return result;
            }
            catch (DataDuplicateException ex)
            {
                return new ActionResult(GetText(HierarchyObjectConstants.SIP_HO_DUPLICATED_DATA), FullPath, "Submit Data");
            }
            catch (Exception ex)
            {
                var failMessage = GetText(HierarchyObjectConstants.SIP_HO_BAD_ACTION_MESSAGE); // To Translate
                AlarmUtility.Audit(HierarchyObjectConstants.SIP_APPLICATION_ID, AlarmID.HO_BAD_ACTION, FullPath, string.Empty, "Submit Data", string.Empty,
                        ex.Message, string.Empty, ex.StackTrace);

                return new ActionResult()
                {
                    Type = ActionResultType.DisplayOK,
                    Icon = HierarchyObjectConstants.ICON_WARNING,
                    Message = failMessage,
                    NewLocation = HierarchyObjectConstants.PATH_CURRENT
                };
            }
        }
        #endregion
    }
}
