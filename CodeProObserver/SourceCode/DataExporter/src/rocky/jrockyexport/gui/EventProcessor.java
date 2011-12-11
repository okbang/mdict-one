package rocky.jrockyexport.gui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import rocky.common.CommonUtil;
import rocky.common.LogService;
import rocky.common.dlg.MyMessageBox;
import rocky.export.ReportItemConfigurationInfo;
import rocky.export.SqlConfigurationUtil;
import rocky.jdbc.AliasConfig;
import rocky.jrockyexport.gui.form.ExpConfigForm;
import rocky.jrockyexport.gui.form.ReportConfigForm;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class EventProcessor implements SelectionListener {
//    private static EventProcessor defaultProcessor = null;
    private static EventProcessor shellProcessor = null;
    private Shell shell;
    private Composite comp;
    private IClient client; // this client receive response from this processor
    
    private EventProcessor(Shell shell) {
        this.shell = shell;
    }
    
    private EventProcessor(Composite comp) {
        this.comp = comp;
        this.shell = comp.getShell();
    }
    
//    public static EventProcessor getInstance() {
//        if (defaultProcessor == null) {
//            defaultProcessor = new EventProcessor(null);
//        }
//        return defaultProcessor;
//    }
    
    public static SelectionListener getInstance(Shell shell, IClient client) {
        if (shellProcessor == null) {
            shellProcessor = new EventProcessor(shell);
        } else {
            shellProcessor.shell = shell;
        }
        shellProcessor.client = client;
        
        return shellProcessor;
    }

    public static SelectionListener getInstance(Composite comp, IClient client) {
        if (shellProcessor == null) {
            shellProcessor = new EventProcessor(comp);
        } else {
            shellProcessor.comp = comp;
        }
        shellProcessor.client = client;
        
        return shellProcessor;
    }
    
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
    }

    @Override
    public void widgetSelected(SelectionEvent selEvent) {
        // Get identifier of click component
        String componentId;
        Object srcObj = selEvent.getSource();
        
        if (srcObj instanceof MenuItem) {
            MenuItem mnuItem = (MenuItem) srcObj;
            componentId = (String) mnuItem.getData(GCons.BTNID);
        } else if (srcObj instanceof Button) {
            Button btnItem = (Button) srcObj;
            componentId = (String) btnItem.getData(GCons.BTNID);
        } else {
            componentId = (String) selEvent.widget.getData(GCons.BTNID);
        }
        
        if (GCons.BTN_SAVE.equals(componentId)) { // OK button is clicked
            if (processSave()) {
                MyMessageBox.show(selEvent.display.getShells()[0], SWT.OK | SWT.ICON_INFORMATION, GCons.MK_SAVE_CFG_OK);
            } else {
                MyMessageBox.show(selEvent.display.getShells()[0], SWT.OK | SWT.ICON_ERROR, GCons.MK_SAVE_CFG_ERR);
            }

        } else if (GCons.BTN_TESTCONNECTION.equals(componentId)) { // Test Connection button is clicked
            processTestConnection();
        } else if (GCons.BTN_UPDATEALIAS.equals(componentId)) { // Update alias button is clicked
            processUpdateAlias();
        } else if (GCons.BTN_NEWALIAS.equals(componentId)) { // New alias button is clicked
            processNewAlias();
        } else if (GCons.BTN_PREVIEW.equals(componentId)) { // New alias button is clicked
            processPreview();
        } else if (GCons.BTN_EXPORT.equals(componentId)) { // New alias button is clicked
            processExport();
        } else if (GCons.BTN_GENSCRIPT.equals(componentId)) { // New alias button is clicked
            processGenScript();
        }
        // Process menu events
        if (GCons.MENU_FILE_QUIT.equals(componentId)) {
            processMenuFileQuit();
        } else if (GCons.MENU_FILE_OPEN_ALIAS.equals(componentId)) {
            FileDialog fileDlg = new FileDialog(shell, SWT.NONE);
            String filePath = fileDlg.open();
            LogService.logDebug("filePath = " + filePath);
            
            if (CommonUtil.NNandNB(filePath)) {
                processMenuFileOpenAlias(filePath);
                //loadConfiguration();
            }
        } else if (GCons.MENU_FILE_OPEN_SCRIPT.equals(componentId)) {
            FileDialog fileDlg = new FileDialog(shellProcessor.shell, SWT.NONE);
            String filePath = fileDlg.open();
            LogService.logDebug("filePath = " + filePath);
            
            if (CommonUtil.NNandNB(filePath)) {
                processMenuFileOpenScript(filePath);
                //loadConfiguration();
            }
        }

    }

    private void processMenuFileQuit() {
        shell.dispose();
        
    }
    private void processMenuFileOpenAlias(String filePath) {
        
        
    }
    
    private void processMenuFileOpenScript(String filePath) {
        ExpConfigForm frmData = new ExpConfigForm();
        List<ReportItemConfigurationInfo> rptItemCfgInfoList = SqlConfigurationUtil.parse(filePath);

        List<ReportConfigForm> rptCfgFrmList = new ArrayList<ReportConfigForm>();
        ReportConfigForm rptCfgFrm;
        for (ReportItemConfigurationInfo rptItemCfgInfo : rptItemCfgInfoList) {
            rptCfgFrm = new ReportConfigForm();
            rptCfgFrm.setName(rptItemCfgInfo.getId());
            rptCfgFrm.setQueryMap(rptItemCfgInfo.getQueryMap());
            rptCfgFrm.setRptEntry(rptItemCfgInfo.getName());

            rptCfgFrmList.add(rptCfgFrm);
        }
        frmData.setRptCfgFormList(rptCfgFrmList);
        client.recResp(GCons.MENU_FILE_OPEN_SCRIPT, frmData);
    }
    
    private void processGenScript() {
        // TODO Auto-generated method stub
        
    }

    private void processExport() {
        // TODO Auto-generated method stub
        
    }

    private void processNewAlias() {
        // TODO Auto-generated method stub
        
    }

    private void processPreview() {
        // TODO Auto-generated method stub
        
    }

    private void processUpdateAlias() {
        // TODO Auto-generated method stub
        
    }

    private void processTestConnection() {
        // TODO Auto-generated method stub
        
    }

    /**
     * Process Save button from the DBConfigComp.
     */
    private boolean processSave() {
        boolean saveOK = true;
        LogService.logDebug("processSave.START");
        //IForm frm = (IForm) comp;
        AliasConfig frmData = (AliasConfig) comp.getData();
        // Save the alias configuration
        XStream streamer = new XStream(new DomDriver());
        try {
            streamer.toXML(frmData, new FileOutputStream((String)comp.getData(GCons.KD_CFGFILEPATH)));
        } catch (FileNotFoundException fnfe) {
            LogService.logError(fnfe);
            saveOK = false; 
        }
        
        LogService.logDebug("processSave.END");
        return saveOK;
    }

}
