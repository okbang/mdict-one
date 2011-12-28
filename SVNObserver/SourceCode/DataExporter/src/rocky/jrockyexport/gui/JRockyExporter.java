package rocky.jrockyexport.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import rocky.common.LogService;
import rocky.jdbc.AliasConfig;
import rocky.jrockyexport.gui.form.ExpConfigForm;
import rocky.util.ConfigUtil;
import swing2swt.layout.BorderLayout;

public class JRockyExporter implements IClient {

    private String cfgFilePath = null;
    private AliasConfig aliasCfg = null;
    protected Shell shell;
    Composite dbConfigComp;
    Composite exConfigComp;
    

    /**
     * Launch the application
     * @param args
     */
    public static void main(String[] args) {
        boolean isConsoleMode = false; 
        String fileCfgPath = "AliasConfig.xml"; // default configuration path
        
        // Parsing the arguments
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {

                if ("/console".equals(args[i])) {
                    isConsoleMode = true;
                } else if ("-conf".equals(args[i])) {
                    // Get the configuration file in the next argument
                    if (i < args.length - 1) {
                        i++;
                        fileCfgPath = args[i];
                    }
                } else {
                    // Invalid arguments
                    usage();
                    System.exit(1);
                }
            }
        }
        if (isConsoleMode) {
            
        } else {
            try {
                JRockyExporter window = new JRockyExporter();
                window.setConfigPath(fileCfgPath);
                window.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Setting and loading the configuration path.
     * @param fileCfgPath
     */
    private void setConfigPath(String fileCfgPath) {
        this.cfgFilePath = fileCfgPath;
        aliasCfg = ConfigUtil.loadConfiguration(fileCfgPath);
        //init(aliasCfg);
    }

    private void init(AliasConfig aliasCfg) {
        ((DBConfigComp)dbConfigComp).init(aliasCfg);
    }

    private static void usage() {
        System.out.println("JRockyExport [/console] [-conf ConfigurationFile]"
                + "\n/console Running the transport in the console mode."
                + "\n/ConfigurationFile the configuration path.");
    }

    /**
     * Open the window
     */
    public void open() {
        final Display display = Display.getDefault();
        createContents();
        init(aliasCfg);
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }

    /**
     * Create contents of the window
     */
    protected void createContents() {
        shell = new Shell();
        shell.setLayout(new BorderLayout(0, 0));
        //shell.setLayout(new BorderLayout(0, 0));
        shell.setSize(596, 463);
        shell.setText("JRockyExporter");

        final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);

        final TabItem configurationTabItem = new TabItem(tabFolder, SWT.NONE);
        configurationTabItem.setText("Configuration");

        dbConfigComp = new DBConfigComp(tabFolder, SWT.NONE);
        configurationTabItem.setControl(dbConfigComp);
        dbConfigComp.setData(GCons.KD_CFGFILEPATH, cfgFilePath);

        final TabItem exportTabItem = new TabItem(tabFolder, SWT.NONE);
        exportTabItem.setText("Export");

        exConfigComp = new ExpConfigComp(tabFolder, SWT.NONE);
        exportTabItem.setControl(exConfigComp);

        final Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);

        final MenuItem menuFile = new MenuItem(menu, SWT.CASCADE);
        menuFile.setText("&File");

        final Menu menu_1 = new Menu(menuFile);
        menuFile.setMenu(menu_1);

        final MenuItem newSubmenuMenuItem = new MenuItem(menu_1, SWT.CASCADE);
        newSubmenuMenuItem.setText("Open");

        final Menu menu_3 = new Menu(newSubmenuMenuItem);
        newSubmenuMenuItem.setMenu(menu_3);

        final MenuItem menuFileOpenAlias = new MenuItem(menu_3, SWT.NONE);
        menuFileOpenAlias.setData(GCons.MNUID, GCons.MENU_FILE_OPEN_ALIAS);
        menuFileOpenAlias.setAccelerator(SWT.CTRL | 'A');
        menuFileOpenAlias.setText("Database &Alias...");
        menuFileOpenAlias.addSelectionListener(EventProcessor.getInstance(shell, this));

        final MenuItem menuFileOpenScript = new MenuItem(menu_3, SWT.NONE);
        menuFileOpenScript.setData(GCons.MNUID, GCons.MENU_FILE_OPEN_SCRIPT);
        menuFileOpenScript.setAccelerator(SWT.CTRL | 'L');
        menuFileOpenScript.setText("SQ&L Script...");
        menuFileOpenScript.addSelectionListener(EventProcessor.getInstance(shell, this));

        new MenuItem(menu_1, SWT.SEPARATOR);

        final MenuItem menuFileQuit = new MenuItem(menu_1, SWT.NONE);
        menuFileQuit.setData(GCons.MNUID, GCons.MENU_FILE_QUIT);
        menuFileQuit.setAccelerator(SWT.CTRL | 'Q');
        menuFileQuit.setText("&Quit");
        menuFileQuit.addSelectionListener(EventProcessor.getInstance(shell, this));

        final MenuItem menuHelp = new MenuItem(menu, SWT.CASCADE);
        menuHelp.setText("&Help");
        menuHelp.addSelectionListener(EventProcessor.getInstance(shell, this));

        final Menu menu_2 = new Menu(menuHelp);
        menuHelp.setMenu(menu_2);

        final MenuItem menuHelpContent = new MenuItem(menu_2, SWT.NONE);
        menuHelpContent.setData(GCons.MNUID, GCons.MENU_HELPCONENT);
        menuHelpContent.setAccelerator(SWT.F1);
        menuHelpContent.setText("Help &Content");

        new MenuItem(menu_2, SWT.SEPARATOR);

        final MenuItem menuHelpAbout = new MenuItem(menu_2, SWT.NONE);
        menuHelpAbout.setText("&About");
        //
    }
    
    public void setRptConfigFile(ExpConfigForm expCfgFrm) {
        ((ExpConfigComp) exConfigComp).init(expCfgFrm);
    }

    @Override
    public void recResp(String eventId, Object resp) {
        LogService.logDebug(this.getClass() + ".recResp: eventId=" + eventId + "resp=" + resp);
        if (GCons.MENU_FILE_OPEN_SCRIPT.equals(eventId)) {
            ExpConfigForm frmData = (ExpConfigForm) resp;
            setRptConfigFile(frmData);
        }
        
        
    }
}
