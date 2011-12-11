package rocky.common.dlg;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class RockyMessageBox extends Dialog implements Listener {
    private static final int PUSH_POS_Y = 35;
    /* Constants */
    final static int PUSH_WIDTH = 40;
    final static int PUSH_HEIGHT = 25;
    
    /** Horizontal spacing between 2 buttons. */
    final static int HORN_SPACING = 10;
    
    Label labMsg;
    Shell dlgShell;
    Button btnOK;
    Button btnCancel;
    
    int selectedCodeButton = SWT.NONE;

    int style;
    /**
     * Dialog Information.
     * @param parent the parent shell
     * @param title the tile of the dialog
     * @param message the content message
     * @param style flag to display buttons
     * @param eventHandler handle after the button is selected
     */
    public RockyMessageBox(Shell parent, String title, String message, int style) {
        super(parent);
        this.style = style;
        
        dlgShell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
        dlgShell.setText(title);
        
        // Calculate the width of the dialog
        int width = message.length() * new GC(dlgShell).getFontMetrics().getAverageCharWidth() + 40;
        
        dlgShell.setSize(width, 100);
        
        // Set the position at the center of the parent
        int x = (parent.getSize().x - dlgShell.getSize().x) / 2;
        int y = (parent.getSize().y - dlgShell.getSize().y) / 2;
        dlgShell.setLocation(x + parent.getLocation().x, y + parent.getLocation().y);
        
        labMsg = new Label(dlgShell, SWT.NONE);
        labMsg.setText(message);
        labMsg.setBounds(10, 5, width, 20);
        
        if (((style & SWT.OK) > 0) && ((style & SWT.CANCEL) > 0)) { // Display both of OK and Cancel button
            btnOK = new Button(dlgShell, SWT.PUSH);
            btnOK.setBounds((dlgShell.getSize().x - PUSH_WIDTH * 2 - HORN_SPACING)/2, PUSH_POS_Y, PUSH_WIDTH, PUSH_HEIGHT);
            btnOK.setText("&OK");
            btnOK.setData("code", SWT.OK);
            btnOK.addListener(SWT.Selection, this);

            btnCancel = new Button(dlgShell, SWT.PUSH);
            btnCancel.setBounds(btnOK.getBounds().x + PUSH_WIDTH + HORN_SPACING, PUSH_POS_Y, PUSH_WIDTH, PUSH_HEIGHT);
            btnCancel.setText("&Cancel");
            btnCancel.addListener(SWT.Selection, this);
            btnCancel.setData("code", SWT.CANCEL);
        } else if (((style & SWT.OK) > 0)) { // Display OK button only 
            btnOK = new Button(dlgShell, SWT.PUSH);
            btnOK.setBounds((dlgShell.getSize().x - PUSH_WIDTH) /2 , PUSH_POS_Y, PUSH_WIDTH, PUSH_HEIGHT);
            btnOK.setData("code", SWT.OK);
            btnOK.setText("&OK");
            btnOK.addListener(SWT.Selection, this);
            
        } else if (((style & SWT.CANCEL) > 0)) { // Display OK button only 
            btnCancel = new Button(dlgShell, SWT.PUSH);
            btnCancel.setBounds((dlgShell.getSize().x - PUSH_WIDTH) /2 , PUSH_POS_Y, PUSH_WIDTH, PUSH_HEIGHT);
            btnCancel.setText("&Cancel");
            btnCancel.addListener(SWT.Selection, this);
            btnCancel.setData("code", SWT.CANCEL);
        }  
    }
    
    private void resize() {
        String message = labMsg.getText();
        // Calculate the width of the dialog
        int width = message.length() * new GC(dlgShell).getFontMetrics().getAverageCharWidth() + 40;
        
        dlgShell.setSize(width, 100);
        
        // Set the position at the center of the parent
        Shell parent = this.getParent();
        int x = (parent.getSize().x - dlgShell.getSize().x) / 2;
        int y = (parent.getSize().y - dlgShell.getSize().y) / 2;
        dlgShell.setLocation(x + parent.getLocation().x, y + parent.getLocation().y);
        
        labMsg = new Label(dlgShell, SWT.NONE);
        labMsg.setText(message);
        labMsg.setBounds(10, 5, width, 20);
        
        if (((style & SWT.OK) > 0) && ((style & SWT.CANCEL) > 0)) { // Display both of OK and Cancel button
            btnOK = new Button(dlgShell, SWT.PUSH);
            btnOK.setBounds((dlgShell.getSize().x - PUSH_WIDTH * 2 - HORN_SPACING)/2, PUSH_POS_Y, PUSH_WIDTH, PUSH_HEIGHT);

            btnCancel.setBounds(btnOK.getBounds().x + PUSH_WIDTH + HORN_SPACING, PUSH_POS_Y, PUSH_WIDTH, PUSH_HEIGHT);
        } else if (((style & SWT.OK) > 0)) { // Display OK button only 
            btnOK.setBounds((dlgShell.getSize().x - PUSH_WIDTH) /2 , PUSH_POS_Y, PUSH_WIDTH, PUSH_HEIGHT);
        } else if (((style & SWT.CANCEL) > 0)) { // Display OK button only 
            btnCancel.setBounds((dlgShell.getSize().x - PUSH_WIDTH) /2 , PUSH_POS_Y, PUSH_WIDTH, PUSH_HEIGHT);
        }  
    }
    
    public int open () {
        dlgShell.open();
        
        Display display = dlgShell.getDisplay();
        while (!dlgShell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
        }
        return selectedCodeButton;
    }
    
    public int open (boolean modal) {
        dlgShell.open();
        if (modal) {
            Display display = dlgShell.getDisplay();
            while (!dlgShell.isDisposed()) {
                if (!display.readAndDispatch())
                    display.sleep();
            }
        }
        return selectedCodeButton;
    }
    
    public void close() {
        if ((dlgShell != null) && (dlgShell.getShell() != null) && (!dlgShell.getShell().isDisposed()) && dlgShell.isVisible()) {
            
            dlgShell.close();
        }
        //dlgShell.setVisible(false);
    }
    
    public void setMessage(String message) {
        if ((dlgShell != null) && (dlgShell.getShell() != null) && (!dlgShell.getShell().isDisposed())) {
            resize();
            labMsg.setText(message);
            labMsg.redraw();
        }
    }
    
    @Override
    public void handleEvent(Event event) {
        selectedCodeButton = (Integer) event.widget.getData("code");
        dlgShell.close();
    }
}
