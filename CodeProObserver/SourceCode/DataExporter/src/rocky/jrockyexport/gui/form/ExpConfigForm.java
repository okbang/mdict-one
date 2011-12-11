package rocky.jrockyexport.gui.form;

import java.util.ArrayList;
import java.util.List;

import rocky.common.Constant;

public class ExpConfigForm {
    private int selectedReportIndex;
    private List<ReportConfigForm> rptCfgFormList;
    
    public int getSelectedReportIndex() {
        return selectedReportIndex;
    }
    public void setSelectedReportIndex(int selectedReportIndex) {
        this.selectedReportIndex = selectedReportIndex;
    }
    public List<ReportConfigForm> getRptCfgFormList() {
        return rptCfgFormList;
    }
    public void setRptCfgFormList(List<ReportConfigForm> rptCfgFormList) {
        this.rptCfgFormList = rptCfgFormList;
    }
    
    public ReportConfigForm getSelectedReportConfig() {
        return rptCfgFormList.get(selectedReportIndex);
    }
    
    public ReportConfigForm getReportConfig(int index) {
        return rptCfgFormList.get(index);
    }
    
    public String[] getReportNames() {
        List<String> rptNames = new ArrayList<String>();
        for (ReportConfigForm rptCfg : rptCfgFormList) {
            rptNames.add(rptCfg.getName());
        }
        return rptNames.toArray(Constant.BLANK_STRS);
    }
}
