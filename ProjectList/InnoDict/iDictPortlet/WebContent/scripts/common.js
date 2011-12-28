/**
 * Get the HTML form.
 * @param namespace
 * @param frmName
 * @return
 */
function getForm(namespace, frmName) {
	return document.forms[namespace + frmName];
}

/**
 * Submit the form.
 * @param namespace
 * @param frmName
 * @param eventId
 * @return
 */
function submitAction(namespace, frmName, eventId) {
	var frm = getForm(namespace, frmName);
	frm.eventId.value = eventId;
	frm.submit();
}

/**
 * Submit the form of dynamic Menu
 * @param namespace
 * @param frmName
 * @param eventId
 * @param itemId
 * @return
 */
function submitMenu(namespace, frmName, eventId, itemId) {
	var frm = getForm(namespace, frmName);
	frm.eventId.value = eventId;
	frm.itemId.value = itemId;
	frm.submit();
}
