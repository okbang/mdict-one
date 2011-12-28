package rocky.jrockyexport.gui;

import java.util.ResourceBundle;

public class GCons {
    /** Vietnamese resources. */
    //public static final ResourceBundle RB = ResourceBundle.getBundle("ApplicationResources", new Locale("vi","VN"));
    private static final ResourceBundle RB = ResourceBundle.getBundle("ApplicationResources");
    
    public final static String BTNID = "ID";
    public final static String BTN_SAVE = "SAVE";
    public final static String BTN_TESTCONNECTION = "TESTCONNECTION";
    public final static String BTN_UPDATEALIAS = "UPDATEALIAS";
    public final static String BTN_NEWALIAS = "NEWALIAS";
    public final static String BTN_PREVIEW = "PREVIEW";
    public final static String BTN_EXPORT = "EXPORT";
    public final static String BTN_GENSCRIPT = "GENSCRIPT";
    public static final String MNUID = "ID";
    public static final String MENU_FILE_QUIT = "MENUFLEQUIT";
    public static final String MENU_FILE_OPEN_ALIAS = "MENUFILEOPENALIAS";
    public static final String MENU_FILE_OPEN_SCRIPT = "MENUFILEOPENSCRIPT";
    public static final String MENU_HELPCONENT = "MENUHELPCONTENT";
    
    // Data
    public static final String KD_CFGFILEPATH = "cfgFilePath";
    
    // Define key in the ApplicationResource
    public static final String MK_INFO = "msg.info";
    public static final String MK_ERROR = "msg.error";
    public static final String MK_WARNING = "msg.warning";
    public static final String MK_CONFIRM = "msg.confirm";
    public static final String MK_WORKING = "msg.working";
    
    // Define key for message
    public static final String MK_SAVE_CFG_OK = "msg.001";
    public static final String MK_SAVE_CFG_ERR = "msg.002";
    
    public static final String getString(String key) {
        return RB.getString(key);
    }
}
