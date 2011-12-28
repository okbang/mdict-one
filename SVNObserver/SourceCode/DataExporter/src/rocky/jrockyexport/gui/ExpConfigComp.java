package rocky.jrockyexport.gui;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import rocky.jrockyexport.gui.form.ExpConfigForm;
import rocky.jrockyexport.gui.form.ReportConfigForm;

public class ExpConfigComp extends Composite implements SelectionListener, IClient {

    private static final int COL_VAL = 1;
    //private Text txtQuery;
    private Table table;
    private Text txtOutSheet;
    private Combo cboReport;
    private ExpConfigForm frmData;
    /**
     * Create the composite
     * @param parent
     * @param style
     */
    public ExpConfigComp(Composite parent, int style) {
        super(parent, style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);

        final Label reportLabel = new Label(this, SWT.NONE);
        reportLabel.setText("Report:");

        cboReport = new Combo(this, SWT.NONE);
        cboReport.addSelectionListener(this);
        cboReport.setTextLimit(128);
        final GridData gd_cboReport = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_cboReport.widthHint = 161;
        cboReport.setLayoutData(gd_cboReport);

        final Label outSheetLabel = new Label(this, SWT.NONE);
        outSheetLabel.setText("Out sheet:");

        txtOutSheet = new Text(this, SWT.BORDER);
        txtOutSheet.setTextLimit(64);
        final GridData gd_txtOutSheet = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_txtOutSheet.widthHint = 189;
        txtOutSheet.setLayoutData(gd_txtOutSheet);

        final Label queryLabel = new Label(this, SWT.NONE);
        queryLabel.setText("Query:");

//        txtQuery = new Text(this, SWT.WRAP | SWT.V_SCROLL | SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
//        txtQuery.setTextLimit(1024);
//        final GridData gd_txtQuery = new GridData(SWT.FILL, SWT.CENTER, true, false);
//        gd_txtQuery.heightHint = 275;
//        txtQuery.setLayoutData(gd_txtQuery);
        table = new Table(this, SWT.BORDER);
        final GridData gd_table = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_table.heightHint = 280;
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLayoutData(gd_table);

        final TableColumn keyColumn = new TableColumn(table, SWT.NONE);
        keyColumn.setWidth(95);
        keyColumn.setText("Key");

        final TableColumn valueColumn = new TableColumn(table, SWT.NONE);
        valueColumn.setWidth(349);
        valueColumn.setText("Value");

        final Composite composite = new Composite(this, SWT.NONE);
        final GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gd_composite.widthHint = 450;
        gd_composite.heightHint = 42;
        composite.setLayoutData(gd_composite);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 6;
        composite.setLayout(gridLayout_1);

        final Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(141, SWT.DEFAULT));
        label.setText("  ");

        final Button previewButton = new Button(composite, SWT.NONE);
        previewButton.setData(GCons.BTNID, GCons.BTN_PREVIEW);
        previewButton.addSelectionListener(EventProcessor.getInstance(this, this));
        previewButton.setToolTipText("Preview the Excel content.");
        previewButton.setText("&Preview");

        final Label label_1 = new Label(composite, SWT.NONE);
        label_1.setText("  ");

        final Button exportButton = new Button(composite, SWT.NONE);
        exportButton.setData(GCons.BTNID, GCons.BTN_EXPORT);
        exportButton.addSelectionListener(EventProcessor.getInstance(this, this));
        exportButton.setToolTipText("Export to Excel file.");
        exportButton.setText("&Export");

        final Label label_2 = new Label(composite, SWT.NONE);
        label_2.setText("  ");

        final Button generateScriptButton = new Button(composite, SWT.NONE);
        generateScriptButton.setData(GCons.BTNID, GCons.BTN_GENSCRIPT);
        generateScriptButton.addSelectionListener(EventProcessor.getInstance(this, this));
        generateScriptButton.setToolTipText("Generate the runnable batch script.");
        generateScriptButton.setText("&Generate Script");
        new Label(this, SWT.NONE);
        //
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
    
    public void init(ExpConfigForm rptCfgFrm) {
        this.frmData = rptCfgFrm;
        // Setting values for combo box of reports
        cboReport.setItems(rptCfgFrm.getReportNames());
        
    }
    
    @Override
    public void recResp(String eventId, Object resp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void widgetSelected(SelectionEvent event) {
        Object srcObj = event.getSource();
        //Processing combo box Report change event.
        if ((srcObj == cboReport) && (frmData != null)) {
            int selectedIndex = cboReport.getSelectionIndex();
            ReportConfigForm rptCfg = frmData.getReportConfig(selectedIndex);
            Map<String, String> queryMap = rptCfg.getQueryMap();

            // Setting value of Outsheet
            txtOutSheet.setText(rptCfg.getRptEntry());
            
            // Setting value for table of query
            table.removeAll();

            String key;
            String value;
            TableItem itemTable;
            for (Iterator<String> it = queryMap.keySet().iterator(); it.hasNext();) {
                key = it.next();
                value = queryMap.get(key);
                itemTable = new TableItem(table, SWT.BORDER);
                itemTable.setText(key);
                itemTable.setText(COL_VAL, value);
            }
        }
        
    }

}
