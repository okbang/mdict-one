function isDate(txtDate) {
    var bInvalid;
    var c1, c2, n1, n2;
    var dateValue;

    dateValue = new String(txtDate.value);
    bInvalid = false;
    n1 = 0; n2 = 0;
    if (dateValue.substring(2, 3) == '/') {
        n1 = 2;
    }
    if (dateValue.substring(1, 2) == '/') {
        n1 = 1;
    }
    if (dateValue.substring(3, 4) == '/') {
        n2 = 3;
    }
    if (dateValue.substring(4, 5) == '/') {
        n2 = 4;
    }
    if (dateValue.substring(5, 6) == '/') {
        n2 = 5;
    }
    if (n1 == 0 || n2 == 0) {
        bInvalid = true;
    }
    if (!bInvalid) {
        var nYearLen;
        nYearLen = dateValue.length - n2 - 1;
        if (nYearLen <= 0 || nYearLen > 2) {
            bInvalid = true;
        }
    }
    if (!bInvalid) {
        // month
        sMonth = dateValue.substring(0, n1);
        nMonth = parseInt(sMonth, 10);
        // day
        sDay = dateValue.substring(n1 + 1, n2);
        nDay = parseInt(sDay, 10);
        // year
        sYear = dateValue.substring(n2 + 1, dateValue.length);
        nYear = parseInt(sYear, 10);
        if (nYear < 00 || nYear > 99 || isNaN(sYear) || sYear.indexOf('.') != -1) {
            bInvalid = true;
        }
        if ((nMonth < 1 || nMonth > 12 || isNaN(sMonth) || sMonth.indexOf('.') != -1)) {
            bInvalid = true;
        }
        if ((nDay < 1 ||nDay > 31 || isNaN(sDay) || sDay.indexOf('.') != -1)) {
            bInvalid = true;
        }
         //whatever years acceptable can be put here
        if (nMonth == 4 || nMonth == 6 || nMonth == 9 || nMonth == 11) {
            if (nDay == 31) {
                bInvalid = true;
            }
        }
        if (nMonth == 2) {
            var g = parseInt(nYear / 4, 10);
            if (isNaN(g)) {
                bInvalid = true;
            }
            if (nDay > 29) {
                bInvalid = true;
            }
            if (nDay == 29 && ((nYear / 4) != parseInt(nYear / 4, 10))) {
                bInvalid = true;
            }
        }
    }
    if (bInvalid) {
        alert("Value of " + stripControlName(txtDate.name) + " is invalid!");
        txtDate.focus();
        return false;
    }
    return true;
}

function CompareDate(txtStartDate , txtEndDate) {
    var sStartDate = txtStartDate.value;
    var sEndDate = txtEndDate.value;
    
    var f, startDay, startMonth, startYear, endMonth, endDay, endYear, n1, n2, n3, n4;
    n1 = sStartDate.indexOf("/");
    n2 = sStartDate.lastIndexOf("/");
    n3 = sEndDate.indexOf("/");
    n4 = sEndDate.lastIndexOf("/");

    startMonth = parseInt(sStartDate.substring(0, n1), 10);
    startDay = parseInt(sStartDate.substring(n1 + 1, n2), 10);
    startYear = parseInt(sStartDate.substring(n2 + 1), 10);

    endMonth = parseInt(sEndDate.substring(0, n3), 10);
    endDay = parseInt(sEndDate.substring(n3 + 1, n4), 10);
    endYear = parseInt(sEndDate.substring(n4 + 1), 10);

    if (startYear > endYear) {
        return 1;
    }
    if (startYear < endYear) {
        return -1;
    }
    if (startMonth > endMonth) {
        return 1;
    }
    if (startMonth < endMonth) {
        return -1;
    }
    if (startDay > endDay) {
        return 1;
    }
    if (startDay < endDay) {
        return -1;
    }
    return 0;
}

function textValidate(textBox, minLength, maxLength) {
    var txtValue;
    txtValue = textBox.value;

    txtValue = trimSpaces(txtValue);

    if (txtValue.length >= minLength && txtValue.length <= maxLength) {
        return true;
    }
    if (txtValue.length > maxLength) {
        alert("Length of text in '" + stripControlName(textBox.name) + "' cannot be larger than " + maxLength + " character(s)");
    }
    else if (txtValue.length < minLength && txtValue.length > 0) {
        alert("Length of text in '" + stripControlName(textBox.name) + "' must be larger than " + minLength + " character(s)");
    }
    else {
        alert("" + stripControlName(textBox.name) + " value must not be empty");
    }
    textBox.focus();
    return false;
}

function isNumber(txtNumber) {
    var numberValue;
    numberValue = new String(txtNumber.value);
    if (isNaN(numberValue) || numberValue.indexOf(".", 0) > -1) {
        alert("The value of '" + stripControlName(txtNumber.name) + "' must be a integer number.");
        txtNumber.focus();
        return false;
    }
    return true;
}

function isFloat(txtNumber) {
    var numberValue;
    numberValue = txtNumber.value;
    if (isNaN(numberValue)) {
        alert("The value of '" + stripControlName(txtNumber.name) + "' must be a number.");
        txtNumber.focus();
        return false;
    }
    return true;
}

function isPositiveNumber(numberValue) {
    if (isNaN(numberValue)) {
        return false;
    }
    if (numberValue.indexOf(".", 0) > -1) {
        return false;
    }
    if (numberValue <= 0) {
        return false;
    }
    return true;
}

function isPositiveNumberTextbox(txtNumber) {
    var numberValue;
    numberValue = new String(txtNumber.value);
    if (!isPositiveNumber(numberValue)) {
        alert("The value of '" + stripControlName(txtNumber.name) + "' must be a positive number.");
        txtNumber.focus();
        return false;
    }
    return true;
}

function isPositiveNumberCombobox(cboNumber) {
    var numberValue;
    numberValue = new String(cboNumber.value);
    if (!isPositiveNumber(numberValue)) {
        alert("Please select " + stripControlName(cboNumber.name));
        cboNumber.focus();
        return false;
    }
    return true;
}

function isLessThanCurDate(txtDate) {
    var dateCurrent;
    var dateInput;
    dateCurrent = new Date();
    dateInput = new Date(txtDate.value);
    dateInput.setYear(dateInput.getYear() + 2000);
    if (dateInput.valueOf() <= dateCurrent.valueOf()) {
        return true;
    }
    else {
        alert("Date in " + stripControlName(txtDate.name) + " cannot be a future day");
        txtDate.focus();
        return false;
    }
    return false;
}

function numberAllowed() {
    if (window.event.keyCode > 57 || window.event.keyCode < 48) {
        window.event.keyCode = 0;
    }
}

function stripControlName(name) {
    if ((name.indexOf("txt") == 0) || (name.indexOf("cbo") == 0) || (name.indexOf("hid") == 0)) {
        return name.substring(3, name.length);
    }
    else {
        return name;
    }
}