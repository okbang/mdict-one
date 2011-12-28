package rocky.jrockyexport.gui.form;

import rocky.common.Constant;

public class FormUtil {
    
    public static String formatNum(Integer num) {
        String strVal = Constant.BLANK;
        if (num != null && num != -1) {
            strVal = num.toString();
        }
        
        return strVal;
    }
}
