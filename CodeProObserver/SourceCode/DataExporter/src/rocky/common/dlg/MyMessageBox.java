package rocky.common.dlg;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import rocky.jrockyexport.gui.GCons;

public class MyMessageBox {
    MessageBox msgBox;
    /**
     * The constructor of MyMessaBox with title, message.
     * @param parent
     * @param style
     * @param titleKey key of the resource
     * @param messageKey key of the resource
     */
    public MyMessageBox(Shell parent, int style, String titleKey, String messageKey) {
        msgBox = new MessageBox(parent, style);
        msgBox.setText(GCons.getString(titleKey));
        msgBox.setMessage(GCons.getString(messageKey));
    }

    public MyMessageBox(Shell parent, int style, String messageKey) {
        msgBox = new MessageBox(parent, style);
        msgBox.setMessage(GCons.getString(messageKey));
        
        // Setting the title by key
        if ((SWT.ICON_INFORMATION & style) == 1) {
            msgBox.setText(GCons.getString(GCons.MK_INFO));
        } else if ((SWT.ICON_ERROR & style) == 1) {
            msgBox.setText(GCons.getString(GCons.MK_ERROR));
        } else if ((SWT.ICON_WARNING & style) == 1) {
            msgBox.setText(GCons.getString(GCons.MK_WARNING));
        } else if ((SWT.ICON_QUESTION & style) == 1) {
            msgBox.setText(GCons.getString(GCons.MK_CONFIRM));
        } else if ((SWT.ICON_WORKING & style) == 1) {
            msgBox.setText(GCons.getString(GCons.MK_WORKING));
        }
        
    }
    
    public static int show(Shell parent, int style, String messageKey) {
        MyMessageBox myMsgBox = new MyMessageBox(parent, style, messageKey);
        return myMsgBox.open();
    }

    public int open() {
        return msgBox.open();
        
    }
}
