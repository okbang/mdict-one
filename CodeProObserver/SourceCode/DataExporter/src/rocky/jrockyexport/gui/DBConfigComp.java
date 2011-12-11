package rocky.jrockyexport.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rocky.common.CommonUtil;
import rocky.common.LogService;
import rocky.jdbc.AliasConfig;
import rocky.jdbc.AliasInfo;
import rocky.jrockyexport.gui.form.FormUtil;
import rocky.sql.RockySql;

public class DBConfigComp extends Composite implements ModifyListener, IClient {
    private AliasConfig frmData;
    private Combo cboAlias;
    private Text txtPort;
    private Text txtPassword;
    private Text txtUserName;
    private Text txtDatabaseName;
    private Text txtServerName;
    private Combo cboServerType;
    private String cfgFilePath;
    
    /**
     * Create the composite
     * @param parent
     * @param style
     */
    public DBConfigComp(Composite parent, int style) {
        super(parent, style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);

        final Label aliasLabel = new Label(this, SWT.NONE);
        aliasLabel.setText("&Alias:");

        cboAlias = new Combo(this, SWT.NONE);
        cboAlias.addModifyListener(this);
        final GridData gd_cboAlias = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_cboAlias.widthHint = 115;
        cboAlias.setLayoutData(gd_cboAlias);

        final Label serverTypeLabel = new Label(this, SWT.NONE);
        serverTypeLabel.setText("Server &type:");

        cboServerType = new Combo(this, SWT.NONE);
        cboServerType.setItems(RockySql.SERVE_TYPE_NAMES);
        final GridData gd_cboServerType = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_cboServerType.widthHint = 143;
        cboServerType.setLayoutData(gd_cboServerType);

        final Label servernameLabel = new Label(this, SWT.NONE);
        servernameLabel.setText("Server &name:");

        txtServerName = new Text(this, SWT.BORDER);
        txtServerName.setTextLimit(128);
        final GridData gd_txtServerName = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_txtServerName.widthHint = 160;
        txtServerName.setLayoutData(gd_txtServerName);

        final Label portLabel = new Label(this, SWT.NONE);
        portLabel.setText("&Port:");

        txtPort = new Text(this, SWT.BORDER);
        txtPort.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent event) {
                Text text = (Text)event.widget;
                String value = text.getText();
                if (CommonUtil.NNandNB(value)) {
                    char endChar = value.charAt(value.length() - 1);
                    if (!Character.isDigit(endChar)) {
                        value = value.substring(0, value.length() - 1);
                        text.setText(value);
                    }
                }
            }
        });
        txtPort.setTextLimit(6);
        final GridData gd_txtPort = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_txtPort.widthHint = 59;
        txtPort.setLayoutData(gd_txtPort);

        final Label databaseNameLabel = new Label(this, SWT.NONE);
        databaseNameLabel.setText("&Database name:");

        txtDatabaseName = new Text(this, SWT.BORDER);
        txtDatabaseName.setTextLimit(32);
        final GridData gd_txtDatabaseName = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        txtDatabaseName.setLayoutData(gd_txtDatabaseName);

        final Label userNameLabel = new Label(this, SWT.NONE);
        userNameLabel.setText("&User name:");

        txtUserName = new Text(this, SWT.BORDER);
        txtUserName.setTextLimit(32);
        final GridData gd_txtUserName = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_txtUserName.widthHint = 130;
        txtUserName.setLayoutData(gd_txtUserName);

        final Label passwordLabel = new Label(this, SWT.NONE);
        passwordLabel.setText("&Password:");

        txtPassword = new Text(this, SWT.BORDER | SWT.PASSWORD);
        txtPassword.setTextLimit(32);
        final GridData gd_txtPassword = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_txtPassword.widthHint = 130;
        txtPassword.setLayoutData(gd_txtPassword);

        final Composite composite = new Composite(this, SWT.NONE);
        final GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gd_composite.heightHint = 52;
        gd_composite.widthHint = 356;
        composite.setLayoutData(gd_composite);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 8;
        composite.setLayout(gridLayout_1);

        final Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(18, SWT.DEFAULT));
        label.setText("          ");

        final Button saveButton = new Button(composite, SWT.NONE);
        saveButton.addSelectionListener(EventProcessor.getInstance(this, this));
        saveButton.setData(GCons.BTNID, GCons.BTN_SAVE);
        saveButton.setText("&Save");

        final Label label_1 = new Label(composite, SWT.NONE);
        label_1.setLayoutData(new GridData());
        label_1.setText("  ");

        final Button testConnectionButton = new Button(composite, SWT.NONE);
        testConnectionButton.setData(GCons.BTNID, GCons.BTN_TESTCONNECTION);
        testConnectionButton.addSelectionListener(EventProcessor.getInstance(this, this));
        testConnectionButton.setText("Test &Connection");

        final Label label_2 = new Label(composite, SWT.NONE);
        label_2.setText("  ");

        final Button updateAliasButton = new Button(composite, SWT.NONE);
        updateAliasButton.setData(GCons.BTNID, GCons.BTN_UPDATEALIAS);
        updateAliasButton.addSelectionListener(EventProcessor.getInstance(this, this));
        updateAliasButton.setText("&Update Alias");

        final Label label_3 = new Label(composite, SWT.NONE);
        label_3.setText("  ");

        final Button newAliasButton = new Button(composite, SWT.NONE);
        newAliasButton.setData(GCons.BTNID, GCons.BTN_NEWALIAS);
        updateAliasButton.addSelectionListener(EventProcessor.getInstance(this, this));
        newAliasButton.setText("&New alias");
        new Label(this, SWT.NONE);
        //
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
    
    /**
     * Initial data for the form.
     * @param frm
     */
    public void init(AliasConfig frm) {
        this.frmData = frm;
        List<AliasInfo> listAliasInfos;
        AliasInfo selectedAlias; 
        
        listAliasInfos = frm.getListAliases();
        // Set data for component combo box Alias
        for (AliasInfo ai : listAliasInfos) {
            cboAlias.add(ai.getAliasName());
        }
        cboAlias.select(frm.getSelectedAliasIndex());
        
        // Set data for combo-box Server Type
        cboServerType.setItems(RockySql.SERVE_TYPE_NAMES);
        
        cboServerType.select(frm.getSelectedAlias().getServerType());
        
        selectedAlias = listAliasInfos.get(frm.getSelectedAliasIndex());
        txtServerName.setText(selectedAlias.getServerName());
        txtPort.setText(FormUtil.formatNum(selectedAlias.getPort()));
        txtDatabaseName.setText(selectedAlias.getDatabaseName());
        txtUserName.setText(selectedAlias.getUserName());
        txtPassword.setText(selectedAlias.getPassword());
    }
    
    /**
     * Update changes in the current alias
     */
    private void updateData() {
       int selectedAliasIndex = cboAlias.getSelectionIndex();
       AliasInfo alias = frmData.getAlias(selectedAliasIndex);
       
       // Update the alias
       alias.setServerName(txtServerName.getText());
       alias.setServerType(cboServerType.getSelectionIndex());
       alias.setDatabaseName(txtDatabaseName.getText());
       if (CommonUtil.NNandNB(txtPort.getText())) {
           alias.setPort(Integer.valueOf(txtPort.getText()));
       } else {
           alias.setPort(-1);
       }
       alias.setUserName(txtUserName.getText());
       alias.setPassword(txtPassword.getText());
    }
    /**
     * Implement IForm.
     */
    //@Override
    /*
    public Object getData() {
        // call update current alias
        frmData.setSelectedAliasIndex(cboAlias.getSelectionIndex());
        return frmData;
    }
    */

    @Override
    public Object getData() {
     // call update current alias
        updateData();
        frmData.setSelectedAliasIndex(cboAlias.getSelectionIndex());
        return frmData;
    }

    @Override
    public void modifyText(ModifyEvent modEvt) {
        LogService.logDebug("modifyText.START");
        if (modEvt.widget instanceof Combo) {
            Combo cbo = (Combo) modEvt.widget;
            processChangeAlias(cbo.getSelectionIndex());
        }
        

        LogService.logDebug("modifyText.END");
    }
    
    protected void processChangeAlias(int selectedIdx) {
        LogService.logDebug("processChangeAlias.START:selectedIdx=" + selectedIdx);
        LogService.logDebug("processChangeAlias.END");
    }
    
    public static void setComboData(Combo cboBox, List listValues, int selectedIdx) {
        for (Object obj : listValues) {
            cboBox.add(obj.toString());
        }
        cboBox.select(selectedIdx);
    }
    
    public static void setComboData(Combo cboBox, List listValues, String property, int selectedIdx) {
        String val = null;
        for (Object obj : listValues) {
            // Invoke the read property method
            try {
                val = (String) obj.getClass().getMethod(property, null).invoke(obj, null);
            } catch (IllegalArgumentException e) {
                LogService.logError(e);
            } catch (SecurityException e) {
                LogService.logError(e);
            } catch (IllegalAccessException e) {
                LogService.logError(e);
            } catch (InvocationTargetException e) {
                LogService.logError(e);
            } catch (NoSuchMethodException e) {
                LogService.logError(e);
            }
            cboBox.add(val);
        }
        cboBox.select(selectedIdx);
    }

    public void setCfgFilePath(String cfgFilePath) {
        this.cfgFilePath = cfgFilePath;
    }

    @Override
    public void recResp(String eventId, Object resp) {
        // TODO Auto-generated method stub
        
    }
}
