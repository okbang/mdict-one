var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s) {
	var i;
    for (i = 0; i < s.length; i++) {
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag) {
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++) {
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year) {
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}

function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31;
		if (i==4 || i==6 || i==9 || i==11) {
			this[i] = 30;
		}
		else if (i==2) {
			this[i] = 29;
		}
   }
   return this;
}

function isValidate(strDate) {
	//Valid date format yyyy-mm-dd
	var err = 0;
	var valid = "0123456789-";
	var ok = "yes";
	var temp;
	for (var i=0; i< strDate.length; i++) {
		temp = "" + strDate.substring(i, i+1);
		if (valid.indexOf(temp) == "-1") err = 1;
	}
	if (strDate.length != 10) err=1;
	b = strDate.substring(0, 4); // year
	c = strDate.substring(4, 5); // '-'
	d = strDate.substring(5, 7); // month
	e = strDate.substring(7, 8); // '-'
	f = strDate.substring(8, 10); // date
	if (b<1900 || b>2100) err = 1;
	if (c != '-') err = 1;
	if (d<1 || d>12) err = 1;
	if (e != '-') err = 1;
	if (f<1 || f>31) err = 1;
	if (d==4 || d==6 || d==9 || d==11) {
		if (f==31) err=1;
	}
	if (d==2) {
		var g=parseInt(b/4);
		if (isNaN(g)) {
			err=1;
		}
		if (f>29) err=1;
		if (f==29 && ((b/4)!=parseInt(b/4))) err=1;
	}
	if (err==1) {
		alert('Invalid date format');
		return false;
	}
	return true;
}

function checkDate(input) {
	//Valid date format mm/dd/yyyy
	var validformat=/^\d{2}\/\d{2}\/\d{4}$/ //Basic check for format validity
	var returnval=false;
	if (!validformat.test(input.value)) {
		alert("Invalid Date Format. Please correct and submit again");
	}
	else { //Detailed check for valid date ranges
		var monthfield=input.value.split("/")[0];
		var dayfield=input.value.split("/")[1];
		var yearfield=input.value.split("/")[2];
		var dayobj = new Date(yearfield, monthfield-1, dayfield);
		if ((dayobj.getMonth()+1!=monthfield)||(dayobj.getDate()!=dayfield)||(dayobj.getFullYear()!=yearfield)) {
			alert("Invalid Day, Month, or Year range detected. Please correct and submit again");
		}
		else {
			returnval=true;
		}
	}
	if (returnval==false) input.select();
	return returnval;
}

function doValidate(dtStr) {
	var daysInMonth = DaysArray(12);
	var pos1=dtStr.indexOf(dtCh);
	var pos2=dtStr.indexOf(dtCh, pos1+1);
	var strMonth=dtStr.substring(0, pos1);
	var strDay=dtStr.substring(pos1+1, pos2);
	var strYear=dtStr.substring(pos2+1);

	strYr=strYear;
	if (strDay.charAt(0)=="0" && strDay.length>1) {
		strDay=strDay.substring(1);
	}
	if (strMonth.charAt(0)=="0" && strMonth.length>1) {
		strMonth=strMonth.substring(1);
	}
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) {
			strYr=strYr.substring(1);
		}
	}
	month=parseInt(strMonth);
	day=parseInt(strDay);
	year=parseInt(strYr);

	if (pos1==-1 || pos2==-1){
		alert("The date format should be : mm/dd/yy")
		return false;
	}
	if (strMonth.length<1 || month<1 || month>12){
		alert("Please enter a valid month")
		return false;
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		alert("Please enter a valid day")
		return false;
	}
/*	origin--------------------------------------
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		alert("Please enter a valid 2 digit year between "+minYear+" and "+maxYear)
		return false
	}
*/
	if (strYear.length < 2) {
		alert("Please enter a valid year");
		return false;
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false) {
		alert("Please enter a valid date");
		return false;
	}
	return true;
}

function IsValidTime(timeStr) {
	var timePat = /^(\d{1,2}):(\d{2})(:(\d{2}))?(\s?(AM|am|PM|pm))?$/;
	var matchArray = timeStr.match(timePat);
	if (matchArray == null) {
		alert("Time is not in a valid format (hh:mm)");
		return false;
	}
	hour = matchArray[1];
	minute = matchArray[2];
	second = matchArray[4];

	if (second=="") { second = null; }

	if (hour < 0  || hour > 23) {
		alert("Hour must be between 0 and 23");
		return false;
	}
	if (minute<0 || minute > 59) {
		alert ("Minute must be between 0 and 59");
		return false;
	}
	if (second != null && (second < 0 || second > 59)) {
		alert ("Second must be between 0 and 59");
		return false;
	}
	return true;
}

function ReplaceChar(s) {
    var i = 0;
    var s1 = ""
    var c = "";
    if (s == null) {
        return "";
    }
    else{
        i = s.indexOf("'");
        if (i == -1) {
            return s;
        }
        else {
            for (i = 0; i < s.length; i++) {
                c = s.substring(i, i + 1);
                if (c == "'") {
                    s1 = s1 + "'" + c;
                }
                else {
                    s1 = s1 + c;
                }
            }
            return s1;
        }
    }
}

function dateValidate(txtDate) {
    if (txtDate == null) {
        return false;
    }
    var bInvalid;
    var dateValue;
    var c1, c2, n1, n2;
    bInvalid = false;
    dateValue = txtDate.value;
    n1 = 0;
    n2 = 0;

    if (dateValue.substring(2, 3) == '/') {
        n1 = 2;
    }
    if (dateValue.substring(1, 2) == '/') {
        n1 = 1;
    }
    if (dateValue.substring(3, 4) == '/') {
        n2 = 3;
    }
    if (dateValue.substring(4,5) == '/') {
        n2 = 4;
    }
    if (dateValue.substring(5,6) == '/') {
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
        sMonth = dateValue.substring(0, n1);
        nMonth = parseInt(sMonth, 10);
        sDay = dateValue.substring(n1+1, n2);
        nDay = parseInt(sDay, 10);
        sYear = dateValue.substring(n2+1, dateValue.length);
        nYear = parseInt(sYear, 10);
        if (nYear < 00 || nYear > 99 || isNaN(sYear) || sYear.indexOf('.') != -1) {
            bInvalid = true;
        }
        if ((nMonth < 1 || nMonth > 12 || isNaN(sMonth) || sMonth.indexOf('.') != -1)) {
            bInvalid = true;
        }
        if ((nDay < 1 || nDay > 31 || isNaN(sDay) || sDay.indexOf('.') != -1)) {
            bInvalid = true;
        }

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
        if (isNaN(nDay) || isNaN(nMonth) || isNaN(nYear)) {
            bInvalid = true;
        }
    }
    if (bInvalid) {
        alert('Invalid value for mm/dd/yy');
        txtDate.focus();
        return false;
    }
    else {
        var today = getCurrentDate();
        //var n = CompareDate(dateValue, today);
    }
    return true;
}

function durationValidate(txtDuration) {
    var durationValue;
    durationValue = txtDuration.value;
    if (isNaN(durationValue)) {
        alert('The duration must be a correct number');
        txtDuration.focus();
        return false;
    }
    var nDuration = new Number(durationValue);
    if (nDuration > 8 || nDuration <= 0) {
        alert('The duration can not be more than 8 hours, and can not be zero or negative number');
        txtDuration.focus();
        return false;
    }
    return true;
}

function GetNextDate(txtDate) {
    var bInvalid;
    var dateValue;
    var strResult = "";

    dateValue = txtDate.value;
    n1 = 0;
    n2 = 0;
    n1 = dateValue.indexOf("/");
    n2 = dateValue.lastIndexOf("/");

    nMonth = parseInt(dateValue.substring(0, n1), 10);
    nDay = parseInt(dateValue.substring(n1 + 1, n2), 10);
    nYear = parseInt(dateValue.substring(n2 + 1 ), 10);

    var nNextDay, nNextMonth, nNextYear;

    if (nMonth != 2) {
        if (nDay < 30) {
            nNextDay = nDay + 1;
            nNextMonth = nMonth;
            nNextYear = nYear;
        }
        else if (nDay == 30) {
            if (nMonth == 1 || nMonth == 3 || nMonth == 5 || nMonth == 7 || nMonth == 8 || nMonth == 10 || nMonth == 12) {
                nNextDay = nDay + 1;
                nNextMonth = nMonth;
                nNextYear = nYear;
            }
            else {
                nNextDay = 1;
                nNextMonth = nMonth + 1;
                nNextYear = nYear;
            }
        }
        else {
            if (nMonth < 12) {
                nNextDay = 1;
                nNextMonth = nMonth + 1;
                nNextYear = nYear;
            }
            else {
                nNextDay = 1;
                nNextMonth = 1;
                nNextYear = nYear + 1;
            }
        }
    }
    else {
        if (nDay < 28) {
            nNextDay = nDay + 1;
            nNextMonth = nMonth;
            nNextYear = nYear;
        }
        else if (nDay == 28) {
            if ((nYear/4) == parseInt(nYear / 4, 10)) {
                nNextDay = nDay + 1;
                nNextMonth = nMonth;
                nNextYear = nYear;
            }
            else {
                nNextDay = 1;
                nNextMonth = nMonth + 1;
                nNextYear = nYear;
            }
        }
        else {
            nNextDay = 1;
            nNextMonth = nMonth + 1;
            nNextYear = nYear;
        }
    }

    sNextDay = (nNextDay > 9) ? nNextDay : ("0" + nNextDay);
    sNextMonth = (nNextMonth > 9) ? nNextMonth : ("0" + nNextMonth);
    sNextYear = (nNextYear > 9) ? nNextYear : ("0" + nNextYear);

    strResult = sNextMonth + "/" + sNextDay + "/" + sNextYear;
    return strResult;
}

function compareDate(txtStartDate , txtEndDate) {
    var dStartDate = Date.parse(txtStartDate.value);
    var dEndDate = Date.parse(txtEndDate.value);
    
    if ((dStartDate > dEndDate) || (isNaN(dStartDate)) || (isNaN(dEndDate))) {
        return 1;
    }
    else if (dStartDate < dEndDate) {
        return -1;
    }
    else {
        return 0;
    }

    /*
    var sStartDate = txtStartDate.value;;
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
    */
}    

function getCurrentDate() {
    var d, nCurrMonth, nCurrYear, nCurrDay;
    d = new Date();
    nCurrMonth = (d.getMonth() + 1);
    nCurrDay = d.getDate() ;
    nCurrYear = d.getFullYear().toString();
    nCurrYear = nCurrYear.substring(2, 4);
    if (nCurrDay < 10) {
        nCurrDay = "0" + nCurrDay;
    }
    if (nCurrMonth < 10) {
        nCurrMonth = "0" + nCurrMonth;
    }
    return nCurrMonth + "/" + nCurrDay + "/" + nCurrYear;
}

function isDate(strDate) {
    if (strDate == null) {
        return false;
    }
    
    var sDay, sMonth, sYear, nMonth, nDay, nYear, nSep1, nSep2;
    if (strDate.length > 8) {
        return false;
    }
    nSep1 = strDate.indexOf("/");
    if (nSep1 < 0) {
        return false;
    }
    nSep2 = strDate.lastIndexOf("/");
    if (nSep2 < 0) {
        return false;
    }
    if (nSep1 == nSep2) {
        return false;
    }
    sMonth = strDate.substring(0, nSep1);
    sDay = strDate.substring(nSep1 + 1, nSep2);
    sYear = strDate.substring(nSep2 + 1);
    if (!sMonth.length || !sDay.length || !sYear.length) {
        return false;
    }
    if (isNaN(sMonth) || isNaN(sDay) || isNaN(sYear)) {
        return false;
    }
    nMonth = parseInt(sMonth, 10);
    nDay = parseInt(sDay, 10);
    nYear = parseInt(sYear, 10);
    if (nMonth <= 0 || nDay <= 0 || nYear < 0) {
        return false;
    }
    if (nMonth > 12) {
        return false;
    }
    if (nMonth == 1 || nMonth == 3 || nMonth == 5 || nMonth == 7 || nMonth == 8 || nMonth == 10 || nMonth == 12 ) {
        if (nDay > 31) {
            return false;
        }
    }
    if (nMonth == 4 || nMonth == 6 || nMonth == 9 || nMonth == 11 ) {
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

function toUpperCase() {
    if ( 97 <= window.event.keyCode  && window.event.keyCode  <= 122) {
        window.event.keyCode = window.event.keyCode - 32;
    }
}

function CompareValue(strDate1, strDate2) {
	//Valid date format mm/dd/yy
	var m1;
	var d1;
	var y1;

	var m2;
	var d2;
	var y2;

	var Result;

 if (strDate1.length > 0 && strDate1.length > 0 ) {
	y1 = strDate1.substring(0, 4); // year
	m1 = strDate1.substring(5, 7); // month
	d1 = strDate1.substring(8, 10); // date

	y2 = strDate2.substring(0, 4); // year
	m2 = strDate2.substring(5, 7); // month
	d2 = strDate2.substring(8, 10); // date
	
	Date1 = parseInt(y1, 10) * 10000 + parseInt(m1, 10) * 100 + parseInt(d1, 10);
	Date2 = parseInt(y2, 10) * 10000 + parseInt(m2, 10) * 100 + parseInt(d2, 10);
	
	Result = Date1 - Date2;
	
	if (Result > 0) {
		return 1;
	}
	else {
		return -1;
	}
 }
}