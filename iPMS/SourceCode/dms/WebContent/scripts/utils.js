function doLogout(form) {
    form.hidAction.value = "DMSHomePage";
    form.action = "DMSServlet";
    form.submit();
}

function doSetupEnvironment(form) {
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "SetupEnvironment";
    form.action = "DMSServlet";
    form.submit();
}
//added by MinhPT

//All Project Status
var sAllStatus = "-1";
//ID for the SetupEnvironment page of SQA is "0" 
var sSetupEnvironment = "0";
/**
 * @Description  : Select Project
 * @Author       : Pham Tuan Minh
 * @Create date  : November 27, 2002
 */
function doChangeProject(sHidAction,sHidActionDetail,sHidTypeOfView){
    var form = document.forms[0];
    var strProjectValue = form.cboProjectList.value;
    form.hidAction.value = sHidAction.toString() ;
    if (sHidActionDetail != "") 
        if(form.hidActionDetail != null) 
            form.hidActionDetail.value = sHidActionDetail.toString();
            
    if (sHidTypeOfView != "")
        if(form.hidTypeOfView != null) 
            form.hidTypeOfView.value = sHidTypeOfView.toString(); 
    form.action = "DMSServlet";
    if (form.numPage != null) {
        form.numPage.value = 0;
    }
    form.submit();
}