function isDate(s) {
    var sDay, sMonth, sYear, nMonth, nDay, nYear, nSep1, nSep2;
    if (s.length > 8) {
        return false;
    }
    nSep1 = s.indexOf("/");
    if (nSep1 < 0) {
        return false;
    }
    nSep2 = s.lastIndexOf("/");
    if (nSep2 < 0) {
        return false;
    }
    if (nSep1 == nSep2) {
        return false;
    }
    sDay = s.substring(0, nSep1);
    sMonth = s.substring(nSep1 + 1, nSep2);
    sYear = s.substring(nSep2 + 1);
    if (!sMonth.length || !sDay.length || !sYear.length) {
        return false;
    }
    if (isNaN(sMonth) || isNaN(sDay) || isNaN(sYear)) {
        return false;
    }
    nMonth = parseInt(sMonth, 10);
    nDay = parseInt(sDay, 10);
    nYear = parseInt(sYear, 10);
    if (nMonth <= 0 || nDay <= 0 || nYear < 0 ) {
        return false;
    }
    if (nMonth > 12) {
        return false;
    }
    if (nMonth == 1 || nMonth == 3 || nMonth == 5 || nMonth == 7 || nMonth == 8 || nMonth == 10 || nMonth == 12) {
        if (nDay > 31) {
            return false;
        }
    }
    if (nMonth == 4 || nMonth == 6 || nMonth == 9 || nMonth == 11) {
        if (nDay > 30) {
            return false;
        }
    }
    if (nMonth == 2) {
        if ((nYear % 4 == 0) || (nYear == 0)) {
            if (nDay > 29) {
                return false;
            }
        }
        else if (nDay > 28) {
            return false;
        }
    }
    return true;
}

function CompareDate(sStartDate, sEndDate) {
    var f, startDay, startMonth, startYear, endMonth, endDay, endYear, n1, n2, n3, n4;
    n1 = sStartDate.indexOf("/");
    n2 = sStartDate.lastIndexOf("/");
    n3 = sEndDate.indexOf("/");
    n4 = sEndDate.lastIndexOf("/");

    startDay = parseInt(sStartDate.substring(0, n1), 10);
    startMonth = parseInt(sStartDate.substring(n1 + 1, n2), 10);
    startYear = parseInt(sStartDate.substring(n2 + 1), 10);

    endDay = parseInt(sEndDate.substring(0, n3 ), 10);
    endMonth = parseInt(sEndDate.substring(n3 + 1, n4), 10);
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

function getCurrentDate() {
    var d, nCurrMonth, nCurrYear, nCurrDay;

    d = new Date();
    nCurrMonth = (d.getMonth() + 1);
    nCurrDay = d.getDate();
    nCurrYear = d.getFullYear().toString();
    nCurrYear = nCurrYear.substring(2, 4);
    if (nCurrDay < 10) {
        nCurrDay = "0" + nCurrDay;
    }
    if (nCurrMonth < 10) {
        nCurrMonth = "0" + nCurrMonth;
    }
    return nCurrDay + "/" + nCurrMonth + "/" + nCurrYear;
}

function CompareToToday(sDate) {
    var formatter = new SimpleDateFormat("dd/MM/yy");
    alert(formatter.format(new Date()));
    var today, rs;
    today = getCurrentDate();
    rs = CompareDate(sDate, today);
    return rs;
}

function isNumeric(str) {
    var str1 = "0123456789";
    for (i = 0; i < str1.length; i++) {
        if (str1.indexOf(str.charAt(i)) == -1) {
            return false;
        }
    }
    return true;
}

function emptyField(textObj){
    if (textObj.value.length == 0) {
        return true;
    }
    for (var i = 0; i < textObj.value.length; ++i) {
        var ch = textObj.value.charAt(i);
        if (ch != ' ' && ch != '\t') {
            return false;
        }
    }
    return true;
}

function trim(s){
    var i, sRetVal = "";
    i = s.length - 1;
    while (i >= 0 && s.charAt(i) == ' ') {
        i--;
    }

    s = s.substring(0, i + 1);
    i = 0;
    while (i < s.length && s.charAt(i) == ' ') {
        i++;
    }
    return s.substring(i);
}

function isPer(strInt) {
    if (strInt == "") {
        return false;
    }
    if (!isNaN(strInt)) {
        var strTemp1 = (parseFloat(strInt)).toString();
        var strTemp2 = (parseInt(strTemp1)).toString();
        if (strTemp1 == strTemp2) {
            if (strTemp1 <= 100) {
                return true;
            }
        }
    }
    return false;
}

function isFloat(strFloat) {
    if (strFloat == "") {
        return false;
    }

    if (parseFloat(strFloat) <= 0 ) {
        return false;
    }
    if (!isNaN(strFloat)) {
        if (parseFloat(strFloat) < 10000) {
            return true;
        }
        else {
            return false;
        }
    }
    else {
        return false;
    }
}