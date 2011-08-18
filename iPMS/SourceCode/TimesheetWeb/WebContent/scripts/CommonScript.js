function setKeypress_Login(key) {
    if (navigator.appName != "Netscape") {
        key = event.keyCode;
    }
    if (key==13) {
        doLogin();
    }
}

function setKeypress_Search(key) {
    if (navigator.appName != "Netscape") {
        key = event.keyCode;
    }
    if (key==13) {
        doSearch();
    }
}

function setKeypress_Save(key) {
    if (navigator.appName != "Netscape") {
        key = event.keyCode;
    }
    if (key==13) {
        doSave();
    }
}

function StringMatrix(rows, cols) {
    this.cells = new Array(rows);
    this.rows = rows;
    this.cols = cols;
    for (j = 0; j < rows; j++) {
        this.cells[j] = new Array(cols);
    }
    this.getCell = getCell;
    this.setCell = setCell;
    this.toStr = toStr;
    this.ConvertStrToMatrix = ConvertStrToMatrix;
}

function getCell(i, j) {
    return this.cells[i][j];
}

function setCell(i, j, data) {
    this.cells[i][j] = data;
}

function toStr() {
    retStr = this.rows.toString() + ":" + this.cols.toString() + ":";
    for (i = 0; i < this.rows; i++) {
        for (j = 0; j < this.cols; j++) {
            temp = this.cells[i][j];
            len = temp.length;
            retStr = retStr + len.toString() + ":" + temp;
        }
    }
    return retStr;
}

function ConvertStrToMatrix(data) {
    startIndex = 0;
    endIndex = data.indexOf(":", startIndex);
    temp = data.substring(startIndex, endIndex);
    this.rows = parseInt(temp);
    startIndex = endIndex + 1;
    endIndex = data.indexOf(":", startIndex);
    temp = data.substring(startIndex, endIndex);
    this.cols = parseInt(temp);
    this.cells = new Array(this.rows);
    for (i = 0; i < this.rows; i++) {
        this.cells[i] = new Array(this.cols);
    }
    for (i = 0; i < this.rows; i++) {
        for (j = 0; j < this.cols; j++) {
            startIndex = endIndex + 1;
            endIndex = data.indexOf(":", startIndex);
            temp = data.substring(startIndex, endIndex);
            len = parseInt(temp);
            startIndex = endIndex + 1;
            endIndex = endIndex + len;
            temp = data.substring(startIndex, endIndex + 1);
            this.cells[i][j] = temp;
        }
    }
}

function StringMap(datastr) {
    this.strmatrix = new StringMatrix(1, 1);
    this.strmatrix.ConvertStrToMatrix(datastr);
    this.numRows = this.strmatrix.rows;
    this.getRow = getRow;
}

function getRow(keystring) {
    for (i = 0; i < this.numRows; i++) {
        if (this.strmatrix.getCell(i,0) == keystring) {
            tempmatrix = new StringMatrix(1, 1);
            tempmatrix.ConvertStrToMatrix(this.strmatrix.getCell(i, 1));
            return tempmatrix;
        }
    }
    return null;
}

function fireServerEvent(eventSource) {
    eventSource.form.userEvent.value = eventSource.name;
    eventSource.form.submit();
}

function fireLinkServerEvent(name,form) {
    form.userEvent.value = name;
    form.submit();
}

function isValidDate(value) {
    var dateString = value;
    if (dateString == "") {
        return true;
    }
    if (dateString.length !=10 || dateString.charAt(2) != '/' ||  dateString.charAt(5) != '/') {
        return false;
    }
    var month = trimZeroes(dateString.substring(0, 2));
    var day = trimZeroes(dateString.substring(3, 5));
    var year = trimZeroes(dateString.substring(6, 10));

    if (!(isPositiveInteger(month) && isPositiveInteger(day) && isPositiveInteger(year))) {
        return false;
    }
    if (parseInt(month) <= 0 || parseInt(month) > 12) {
        return false;
    }
    var maxFebDays = 28;
    if (isLeapYear(year)) {
        maxFebDays=29;
    }
    var daysInMonth = new Array(31, maxFebDays, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    if (parseInt(day) <=0 || parseInt(day) > daysInMonth[parseInt(month) - 1]) {
        return false;
    }
    return true;
}

function isLeapYear(yr) {
    if ((parseInt(yr) % 4 == 0 && parseInt(yr) % 100 !=0) || parseInt(yr) % 400 == 0) {
        return true;
    }
    return false;
}

function isNonNegativeInteger(value) {
    if (isNaN(value)) {
        return false;
    }
    if (parseInt(value) < 0) {
        return false;
    }
    return true;
}

function isPositiveInteger(value) {
    if (isNaN(value)) {
        return false;
    }
    if (parseInt(value) <= 0) {
        return false;
    }
    return true;
}

function trimZeroes(value) {
    var length=value.length;
    for (i = 0; i < length; i++) {
        if (value.charAt(i) != '0') {
            break;
        }
    }
    if (i >= length) {
        return 0;
    }
    value = value.substring(i, length);
    return value;
}

function trimSpaces(value) {
    var length = value.length;
    for (i = 0; i < length; i++) {
        if (value.charAt(i) != ' ') {
            break;
        }
    }
    var start = i;
    for (i = length - 1; i > start; i--) {
        if (value.charAt(i) != ' ') {
            break;
        }
    }
    var end = length - i - 1;
    value = value.substring(start, length - end);
    return value;
}

function trim(str) {
   return str.replace(/^\s*|\s*$/g,"");
}

function stripNonDigits(str) {
   return str.replace(/[^\d]*/gi,"");
}

function validateRequiredDateField(textField, promptMsg) {
    if (!validateRequiredTextField(textField, promptMsg)) {
        return false;
    }
    if (!isValidDate(textField.value)) {
        alert(promptMsg);
        textField.focus();
        return false;
    }
    return true;
}

function validateRequiredTextField(textField, promptMessage) {
    if ((trimSpaces(textField.value) == "") || (textField.value == null)) {
        alert(promptMessage);
        textField.focus();
        return false;
    }
    return true;
}

function validateRequiredTextArea(textarea, promptMessage) {
    if ((textarea.value == null) || (textarea.value.length == 0)) {
        alert(promptMessage);
        textarea.focus();
        return false;
    }
    return true;
}

function validateRequiredRadioButtonsList(radioField, promptMessage) {
    if (radioField == null) {
        alert("No item Available!");
        return false;
    }
    if (radioField.type == "checkbox") {
        if (!radioField.checked) {
            alert(promptMessage);
            return false;
        }
        radioField.value = "0";
        return true;
    }
    for (i = 0; i < radioField.length; i++) {
        if (radioField[i].checked) {
            return true;
        }
    }
    alert(promptMessage);
    return false;
}

function getSelectedRadioButtonValue(radioField) {
    if (radioField == null) {
        return null;
    }
    for (i = 0; i < radioField.length; i++) {
        if (radioField[i].checked) {
            return radioField[i].value;
        }
    }
    return false;
}

function validateRequiredCheckBox(checkBoxField, promptMessage) {
    if (checkBoxField == null) {
        alert("No item Available!");
        return false;
    }
    if (!checkBoxField.checked) {
        alert(promptMessage);
        return false;
    }
    return true;
}

function validateRequiredCheckBoxGroup(eventSource, checkBoxGroupName, num, promptMessage) {
    var numRows = parseInt(num);
    if (numRows == 0) {
        alert("No items Available!");
        return false;
    }
    var numChecked = 0;
    for (i = 0; i < numRows; i++) {
        var checkBoxGroupItemCheck = "eventSource.form." + checkBoxGroupName + i + ".checked";
        var checkBoxGroupItemSet = "eventSource.form." + checkBoxGroupName + i + ".value";
        if (eval(checkBoxGroupItemCheck)){
            eval(checkBoxGroupItemSet + "=\"yes\"");
            numChecked++;
        }
        else {
            eval(checkBoxGroupItemSet+"=\"no\"");
        }
    }
    if (numChecked == 0) {
        alert(promptMessage);
        return false;
    }
    return true;
}

function validateRequiredCheckBoxGroup1(eventSource, checkBoxGroupName, num, promptMessage) {
    var numRows = parseInt(num);
    if (numRows == 0) {
        alert("No items Available!");
        return false;
    }
    var numChecked = 0;
    for (i = 0; i < numRows; i++) {
        var checkBoxGroupItemCheck = "eventSource.form." + checkBoxGroupName + i + ".checked";
        if (eval(checkBoxGroupItemCheck)) {
            numChecked++;
        }
    }
    if (numChecked == 0) {
        alert(promptMessage);
        return false;
    }
    return true;
}

function validateRequiredCheckBoxGroup2(name, form, checkBoxGroupName, num, promptMessage) {
    var numRows = parseInt(num);
    if (numRows == 0) {
        alert("No items Available!");
        return false;
    }
    var numChecked=0;
    for (i = 0; i < numRows; i++) {
        var checkBoxGroupItemCheck = "form." + checkBoxGroupName + i + ".checked";
        if (eval(checkBoxGroupItemCheck)) {
            numChecked++;
        }
    }
    if (numChecked == 0) {
        alert(promptMessage);
        return false;
    }
    return true;
}

function validateRequiredCheckBoxGroupForHtmlTable(eventSource, tableName, col, promptMessage) {
    var strRows = eval("eventSource.form." + tableName + "Rows.value");
    var numRows = parseInt(strRows);
    if (numRows == 0) {
        alert("No items Available!");
        return false;
    }
    var numChecked=0;
    for (i = 0; i < numRows; i++) {
        var checkBoxGroupItemCheck = "eventSource.form." + tableName + i + "_" + col + ".checked";
        if (eval(checkBoxGroupItemCheck)) {
            numChecked++;
        }
    }
    if (numChecked == 0) {
        alert(promptMessage);
        return false;
    }
    return true;
}

function validateRequiredCheckBoxGroupForHtmlTable1(name, form, tableName, col, promptMessage) {
    var strRows = eval("form." + tableName + "Rows.value");
    var numRows = parseInt(strRows);
    if (numRows == 0) {
        alert("No items Available!");
        return false;
    }
    var numChecked=0;
    for (i = 0; i < numRows; i++) {
        var checkBoxGroupItemCheck = "form." + tableName + i + "_" + col + ".checked";
        if (eval(checkBoxGroupItemCheck)) {
            numChecked++;
        }
    }
    if (numChecked == 0) {
        alert(promptMessage);
        return false;
    }
    return true;
}

function validateRequiredCheckBoxGroupForHtmlTableNoMessage(eventSource, tableName, col) {
    var strRows = eval("eventSource.form." + tableName + "Rows.value");
    var numRows = parseInt(strRows);
    if (numRows == 0) {
        return false;
    }
    var numChecked = 0;
    for (i = 0; i < numRows; i++) {
        var checkBoxGroupItemCheck = "eventSource.form." + tableName + i + "_" + col + ".checked";
        if (eval(checkBoxGroupItemCheck)) {
            numChecked++;
        }
    }
    if (numChecked == 0) {
        return false;
    }
    return true;
}

function validateRequiredCheckBoxGroupForHtmlTableNoMessage1(name, form, tableName, col) {
    var strRows = eval("form." + tableName + "Rows.value");
    var numRows = parseInt(strRows);
    if (numRows == 0) {
        return false;
    }
    var numChecked = 0;
    for (i = 0; i < numRows; i++) {
        var checkBoxGroupItemCheck = "form." + tableName + i + "_" + col + ".checked";
        if (eval(checkBoxGroupItemCheck)) {
            numChecked++;
        }
    }
    if (numChecked == 0) {
        return false;
    }
    return true;
}

function validateEmptyHtmlTable(eventSource, tableName) {
    var strRows = eval("eventSource.form." + tableName + "Rows.value");
    var numRows = parseInt(strRows);
    if (numRows == 0) {
        return false;
    }
    return true;
}

function validateEmptyHtmlTable1(name, form, tableName) {
    var strRows = eval("form." + tableName + "Rows.value");
    var numRows = parseInt(strRows);
    if (numRows == 0) {
        return false;
    }
    return true;
}

function validateRequiredOptionList(optionList, promptMessage) {
    var selectedItem = optionList.options[optionList.options.selectedIndex].value;
    if ((selectedItem == "") || (selectedItem == null)) {
        alert(promptMessage);
        optionList.focus();
        return false;
    }
    return true;
}

function validateRequiredMultipleListBox(multipleListBox, promptMessage) {
    if(multipleListBox == null) {
        alert("No List of Items!");
        return false;
    }
    var numItems = multipleListBox.options.length;
    if (numItems == 0) {
        alert("No Items available in the List!");
        return false;
    }
    var numChecked = 0;
    for (i = 0; i < numItems; i++) {
        if (multipleListBox.options[i].selected || multipleListBox.options[i].defaultSelected) {
            numChecked++;
        }
    }
    if (numChecked == 0) {
        alert(promptMessage);
        return false;
    }
    return true;
}

function validatePositiveIntegerTextField(textField, promptMessage) {
    if (!validateRequiredTextField(textField, promptMessage)) {
        return false;
    }
    if (!isPositiveInteger(textField.value)) {
        alert(promptMessage);
        textField.focus();
        return false;
    }
    return true;
}

function validateNonNegativeIntegerTextField(textField, promptMessage) {
    if (!validateRequiredTextField(textField, promptMessage)) {
        return false;
    }
    if (!isNonNegativeInteger(textField.value)) {
        alert(promptMessage);
        textField.focus();
        return false;
    }
    return true;
}

function populateList(optionList, strList, promptMsg) {
    if (strList == null) {
        displayEmptyList(optionList, promptMsg);
        return;
    }
    var n = strList.cols;
    if (n == 0) {
        displayEmptyList(optionList, promptMsg);
        return;
    }
    optionList.options.length = n + 1;
    optionList.options[0].value = "";
    optionList.options[0].text = promptMsg;
    optionList.options[0].defaultSelected = true;
    optionList.options[0].selected = true;
    for (var k = 0; k < n; k++) {
        strItem = strList.getCell(0, k);
        optionList.options[k + 1].value = strItem;
        optionList.options[k + 1].text = strItem;
        optionList.options[k + 1].defaultSelected = false;
        optionList.options[k + 1].selected = false;
    }
    return;
}

function displayEmptyList(optionList, promptMsg) {
    optionList.options.length = 1;
    optionList.options[0].value = "";
    optionList.options[0].text = promptMsg;
    optionList.options[0].defaultSelected = true;
    optionList.options[0].selected = true;
    return;
}

function numberAllowed() {
    if (window.event.keyCode > 57 || window.event.keyCode < 48) {
        if (window.event.keyCode != 13) {
            window.event.keyCode = 0;
        }
    }
}
/*
function parseDate(strDate) {
    if (strDate == null) {
        return null;
    }
    else if (strDate.length == 0) {
        return null;
    }
    else if (isValidDate(strDate)) {
        return Date.parse(strDate);
    }
    else {
        return null;
    }
}
*/
function daysBetween(strDate1, strDate2) {
    //var date1 = parseDate(strDate1);
    //var date2 = parseDate(strDate2);
    var msPerDay = 24 * 60 * 60 * 1000;
    
    if ((strDate1 == null) || (strDate2 == null)) {
        return null;
    }
    else if ((strDate1.length == 0) || (strDate2.length == 0)) {
        return null;
    }
    else {
        var date1 = Date.parse(strDate1);
        var date2 = Date.parse(strDate2);
        return Math.round((date2 - date1) / msPerDay);
    }
}

function ignoreESCkey(){
	if (window.event.keyCode==27){
		window.event.returnValue=0;				
	}
}