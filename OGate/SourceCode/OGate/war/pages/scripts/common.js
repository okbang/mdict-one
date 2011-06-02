/**
 * Submit the form.
 * @param frmName
 * @param eventId
 * @return
 */
function submitAction(frmName, eventId) {
	var frm = document.forms[frmName];
	frm.eventId.value = eventId;
	frm.submit();
}
/**
 * Submit the form.
 * @param frmName
 * @param screenId
 * @param eventId
 * @return
 */
function submitAction(frmName, screenId, eventId) {
    var frm = document.forms[frmName];
    frm.screenId.value = screenId;
    frm.eventId.value = eventId;
    frm.submit();
}


function submitMenu(frmName, screenId, eventId, menuId) {
    var frm = document.forms[frmName];
    frm.screenId.value = screenId;
    frm.eventId.value = eventId;
    frm.menuId.value = menuId;
    frm.submit();
}

/**
 * Submit form the Navigation bar.
 * @param frmName
 * @param screenId
 * @param eventId
 * @param tabId
 */
function submitNav(frmName, screenId, eventId, tabId) {
    var frm = document.forms[frmName];
    frm.screenId.value = screenId;
    frm.eventId.value = eventId;
    frm.tabId.value = tabId;
    frm.submit();
}