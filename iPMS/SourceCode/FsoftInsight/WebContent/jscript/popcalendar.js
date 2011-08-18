//  written by Tan Ling Wee on 2 Dec 2001
//  last updated 21 May 2005
//  email : fuushikaden@yahoo.com
//  website : www.pengz.com
//  TabSize: 4
//
//  modified by ALQUANTO 30 July 2003 - german language included.
//                                    - modified languageLogic with the ISO-2letter-strings
//                                    - changes in in showCalendar: defaultLanguage is already set...
//                                    - js and html corrected... more xhtml-compliant... simplier css
//  email: popcalendar@alquanto.de
//
//  modified by PinoToy 25 July 2003  - new logic for multiple languages (English, Spanish and ready for more).
//                                    - changes in popUpMonth & popDownMonth methods for hidding    popup.
//                                    - changes in popDownYear & popDownYear methods for hidding    popup.
//                                    - new logic for disabling dates in    the past.
//                                    - new method showCalendar, dynamic    configuration of language, enabling past & position.
//                                    - changes in the styles.
//  email  : pinotoy@yahoo.com
//
//  modified by Tu Ngoc Trung 27 October 2004
//                                    - Highlight selected date, change color of Sunday, Saturday, today
//                                    - Fix bug: click goto current month when Month list or Year list is openning
//                                    - Use CSS for coloring
//                                    - Support short date format (but has some issues due to javascript limitations)
//                                    - Check valid input date
//                                    - Ignore case sensitive when compare month string values
//                                    - Replace string 'Presione para seleccionar un año' with
//                                          'Presione para seleccionar un an~o' because sometime IE parse it wrongly so this script error
//                                    - 21 May 2005: Replace getYear() by getFullYear()
//  email  : tungoctrung@yahoo.com
//
//  Please do not remove any comment above, you can insert new comment for new features added

var language = 'en';    // Default Language: en - english ; es - spanish; de - german
var enablePast = 0;     // 0 - disabled ; 1 - enabled
var fixedX = -1;        // x position (-1 if to appear below control)
var fixedY = -1;        // y position (-1 if to appear below control)
var startAt = 1;        // 0 - sunday ; 1 - monday
var showWeekNumber = 1; // 0 - don't show; 1 - show
var showToday = 1;      // 0 - don't show; 1 - show
var imgDir = 'image/';     // directory for calendar images ... e.g. var imgDir="/img/"
var dayName = '';

var gotoString = {
    en : 'Go To Current Month',
    es : 'Ir al Mes Actual',
    de : 'Gehe zu aktuellem Monat'
};
var todayString = {
    en : 'Today is',
    es : 'Hoy es',
    de : 'Heute ist'
};
var weekString = {
    en : 'Wk',
    es : 'Sem',
    de : 'KW'
};
var scrollLeftMessage = {
    en : 'Click to scroll to previous month. Hold mouse button to scroll automatically.',
    es : 'Presione para pasar al mes anterior. Deje presionado para pasar varios meses.',
    de : 'Klicken um zum vorigen Monat zu gelangen. Gedrückt halten, um automatisch weiter zu scrollen.'
};
var scrollRightMessage = {
    en : 'Click to scroll to next month. Hold mouse button to scroll automatically.',
    es : 'Presione para pasar al siguiente mes. Deje presionado para pasar varios meses.',
    de : 'Klicken um zum nächsten Monat zu gelangen. Gedrückt halten, um automatisch weiter zu scrollen.'
};
var selectMonthMessage = {
    en : 'Click to select a month.',
    es : 'Presione para seleccionar un mes',
    de : 'Klicken um Monat auszuwählen'
};
var selectYearMessage = {
    en : 'Click to select a year.',
    es : 'Presione para seleccionar un an~o',
    de : 'Klicken um Jahr auszuwählen'
};
var selectDateMessage = {       // do not replace [date], it will be replaced by date.
    en : 'Select [date] as date.',
    es : 'Seleccione [date] como fecha',
    de : 'Wähle [date] als Datum.'
};
var monthName = {
    en : new Array('January','February','March','April','May','June','July','August','September','October','November','December'),
    es : new Array('Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'),
    de : new Array('Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember')
};
var monthName2 = {
    en : new Array('Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'),
    es : new Array('Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic'),
    de : new Array('Jan','Feb','Mrz','Apr','Mai','Jun','Jul','Aug','Sep','Okt','Nov','Dez')
};

if (startAt==0) {
    dayName = {
        en : new Array('Sun','Mon','Tue','Wed','Thu','Fri','Sat'),
        es : new Array('Dom','Lun','Mar','Mie','Jue','Vie','Sab'),
        de : new Array('So','Mo','Di','Mi','Do','Fr','Sa')
    };
} else {
    dayName = {
        en : new Array('Mon','Tue','Wed','Thu','Fri','Sat','Sun'),
        es : new Array('Lun','Mar','Mie','Jue','Vie','Sab','Dom'),
        de : new Array('Mo','Di','Mi','Do','Fr','Sa','So')
    };
}

var crossobj, crossMonthObj, crossYearObj,
    monthSelected, yearSelected, dateSelected,
    omonthSelected, oyearSelected, odateSelected,
    monthConstructed, yearConstructed,
    intervalID1, intervalID2,
    timeoutID1, timeoutID2,
    ctlToPlaceValue, ctlNow,
    dateFormat,
    nStartingYear,
    selDayAction,
    isPast,
    isValidInput,
    oldBgColor = '';

var visYear  = 0;
var visMonth = 0;
var bPageLoaded = false;
var ie  = document.all;
var dom = document.getElementById;
var ns4 = document.layers;
var today    = new Date();
var dateNow  = today.getDate();
var monthNow = today.getMonth();
//var yearNow  = today.getYear();
var yearNow  = today.getFullYear();
var timeNow  =  today.getHours() + ":" + today.getMinutes() +":" + today.getSeconds();
var centuryNow = Math.floor(yearNow / 100);
var shortYearBound = 30;    // If year value is two digits then it specify for current century if year value between 00 and 29
var imgsrc   = new Array('pcaldrop1.gif','pcaldrop2.gif','pcalleft1.gif','pcalleft2.gif','pcalright1.gif','pcalright2.gif');
var img      = new Array();
var bShow    = false;

/* hides <select> and <applet> objects (for IE only) */
function hideElement( elmID, overDiv ) {
    if(ie) {
        for(i = 0; i < document.all.tags( elmID ).length; i++) {
            obj = document.all.tags( elmID )[i];
            if(!obj || !obj.offsetParent) continue;

            // Find the element's offsetTop and offsetLeft relative to the BODY tag.
            objLeft   = obj.offsetLeft;
            objTop    = obj.offsetTop;
            objParent = obj.offsetParent;

            while(objParent.tagName.toUpperCase() != 'BODY') {
                objLeft  += objParent.offsetLeft;
                objTop   += objParent.offsetTop;
                objParent = objParent.offsetParent;
            }

            objHeight = obj.offsetHeight;
            objWidth  = obj.offsetWidth;

            if((overDiv.offsetLeft + overDiv.offsetWidth) <= objLeft);
            else if((overDiv.offsetTop + overDiv.offsetHeight) <= objTop);
            /* CHANGE by Charlie Roche for nested TDs*/
            else if(overDiv.offsetTop >= (objTop + objHeight + obj.height));
            /* END CHANGE */
            else if(overDiv.offsetLeft >= (objLeft + objWidth));
            // Added by Tu Ngoc Trung, detect Vertical side
            else if(overDiv.offsetTop >= (objTop + objHeight));
            else {
                obj.style.visibility = 'hidden';
            }
        }
    }
}

/*
* unhides <select> and <applet> objects (for IE only)
*/
function showElement(elmID) {
    if(ie) {
        for(i = 0; i < document.all.tags( elmID ).length; i++) {
            obj = document.all.tags(elmID)[i];
            if(!obj || !obj.offsetParent) continue;
            obj.style.visibility = '';
        }
    }
}

function HolidayRec (d, m, y, desc) {
    this.d = d;
    this.m = m;
    this.y = y;
    this.desc = desc;
}

var HolidaysCounter = 0;
var Holidays = new Array();

function addHoliday (d, m, y, desc) {
    Holidays[HolidaysCounter++] = new HolidayRec (d, m, y, desc);
}

if (dom) {
    for (i=0;i<imgsrc.length;i++) {
        img[i] = new Image;
        img[i].src = imgDir + imgsrc[i];
    }
    document.write ('<div onclick="bShow=true" id="calendar" style="z-index:+999;position:absolute;visibility:hidden;"><table width="'+((showWeekNumber==1)?200:190)+'" class="pcalTbl"><tr bgcolor="#000066"><td><table width="'+((showWeekNumber==1)?208:188)+'"><tr><td class="pcalCaption"><font color="#ffffff' + '' /*C9D3E9*/ +'"><b><span id="caption"></span></b></font></td><td align="right"><a href="javascript:hideCalendar()"><img src="'+imgDir+'pcalclose.gif" width="10" height="13" border="0" /></a></td></tr></table></td></tr><tr><td style="padding:3px" bgcolor="#ffffff"><span id="content"></span></td></tr>');

    if (showToday == 1) {
        document.write ('<tr bgcolor="#f0f0f0"><td style="padding:5px" align="center"><span id="lblToday"></span></td></tr>');
    }

    document.write ('</table></div><div id="selectMonth" style="z-index:+999;position:absolute;visibility:hidden;"></div><div id="selectYear" style="z-index:+999;position:absolute;visibility:hidden;"></div>');
}

var styleAnchor = 'text-decoration:none;color:black;';
var styleLightBorder = 'border:1px solid #a0a0a0;';

function swapImage(srcImg, destImg) {
    if (ie) document.getElementById(srcImg).setAttribute('src',imgDir + destImg);
}

function init() {
    if (!ns4)
    {
        /*if (!ie) {  // Why???????
            yearNow += 1900;
        }*/

        crossobj=(dom)?document.getElementById('calendar').style : ie? document.all.calendar : document.calendar;
        hideCalendar();

        crossMonthObj = (dom) ? document.getElementById('selectMonth').style : ie ? document.all.selectMonth : document.selectMonth;

        crossYearObj = (dom) ? document.getElementById('selectYear').style : ie ? document.all.selectYear : document.selectYear;

        monthConstructed = false;
        yearConstructed = false;

        if (showToday == 1) {
            document.getElementById('lblToday').innerHTML = '<font color="#000066">' + todayString[language] + ' <a onmousemove="window.status=\''+gotoString[language]+'\'" onmouseout="window.status=\'\'" title="'+gotoString[language]+'" style="'+styleAnchor+'" href="javascript:monthSelected=monthNow;yearSelected=yearNow;constructCalendar();">'+dayName[language][(today.getDay()-startAt==-1)?6:(today.getDay()-startAt)]+', ' + dateNow + ' ' + monthName[language][monthNow].substring(0,3) + ' ' + yearNow + '</a></font>';
        }

        sHTML1 = '<span id="spanLeft" class="pcalSelBtn" onmouseover="swapImage(\'changeLeft\',\'pcalleft2.gif\');this.style.borderColor=\'#8af\';window.status=\''+scrollLeftMessage[language]+'\'" onclick="decMonth()" onmouseout="clearInterval(intervalID1);swapImage(\'changeLeft\',\'pcalleft1.gif\');this.style.borderColor=\'#36f\';window.status=\'\'" onmousedown="clearTimeout(timeoutID1);timeoutID1=setTimeout(\'StartDecMonth()\',500)" onmouseup="clearTimeout(timeoutID1);clearInterval(intervalID1)">&nbsp;<img id="changeLeft" src="'+imgDir+'pcalleft1.gif" width="10" height="11" border="0"></span>&nbsp;';
        sHTML1 += '<span id="spanRight" class="pcalSelBtn" onmouseover="swapImage(\'changeRight\',\'pcalright2.gif\');this.style.borderColor=\'#8af\';window.status=\''+scrollRightMessage[language]+'\'" onmouseout="clearInterval(intervalID1);swapImage(\'changeRight\',\'pcalright1.gif\');this.style.borderColor=\'#36f\';window.status=\'\'" onclick="incMonth()" onmousedown="clearTimeout(timeoutID1);timeoutID1=setTimeout(\'StartIncMonth()\',500)" onmouseup="clearTimeout(timeoutID1);clearInterval(intervalID1)">&nbsp;<img id="changeRight" src="'+imgDir+'pcalright1.gif" width="10" height="11" border="0"></span>&nbsp;';
        sHTML1 += '<span id="spanMonth" class="pcalSelBtn" onmouseover="swapImage(\'changeMonth\',\'pcaldrop2.gif\');this.style.borderColor=\'#8af\';window.status=\''+selectMonthMessage[language]+'\'" onmouseout="swapImage(\'changeMonth\',\'pcaldrop1.gif\');this.style.borderColor=\'#36f\';window.status=\'\'" onclick="popUpMonth()"></span>&nbsp;';
        sHTML1 += '<span id="spanYear" class="pcalSelBtn" onmouseover="swapImage(\'changeYear\',\'pcaldrop2.gif\');this.style.borderColor=\'#8af\';window.status=\''+selectYearMessage[language]+'\'" onmouseout="swapImage(\'changeYear\',\'pcaldrop1.gif\');this.style.borderColor=\'#36f\';window.status=\'\'" onclick="popUpYear()"></span>&nbsp;';

        document.getElementById('caption').innerHTML = sHTML1;

        bPageLoaded=true;
    }
}

function hideCalendar() {
    monthConstructed = false;
    yearConstructed = false;
    crossobj.visibility = 'hidden';
    if (crossMonthObj != null) crossMonthObj.visibility = 'hidden';
    if (crossYearObj  != null) crossYearObj.visibility = 'hidden';
    showElement('SELECT');
    showElement('APPLET');
}

function padZero(num) {
    return (num < 10) ? '0' + num : num;
}

function constructDate(d,m,y, t) {
    sTmp = dateFormat;
    sTmp = sTmp.replace ('dd','<e>');
    sTmp = sTmp.replace ('d','<d>');
    sTmp = sTmp.replace ('<e>',padZero(d));
    sTmp = sTmp.replace ('<d>',d);
    sTmp = sTmp.replace ('mmmm','<p>');
    sTmp = sTmp.replace ('mmm','<p>');
    sTmp = sTmp.replace ('Mon','<p>');
    sTmp = sTmp.replace ('mm','<n>');
    sTmp = sTmp.replace ('m','<m>');
    sTmp = sTmp.replace ('<m>',m+1);
    sTmp = sTmp.replace ('<n>',padZero(m+1));
    sTmp = sTmp.replace ('<o>',monthName[language][m]);
    sTmp = sTmp.replace ('<p>',monthName2[language][m]);
    sTmp = sTmp.replace ('yyyy',y);
    sTmp = sTmp.replace ('yy',padZero(y%100));
    return sTmp.replace ('tt',t);

}

/*// Convert full format year to short format "yy"
function convertToShortYear(iValue) {
    var iYear = iValue % 100;
    if (iYear < 10) {
        return "0" + iYear;
    }
    else {
        return "" + iYear;
    }
}*/

// Convert short format year "yy" to full format
function convertToFullYear(iValue) {
    // short format year is may be >= (shortYearBound) but <= current year in short format
    if (iValue <= (yearNow % 100)) {
        return centuryNow * 100 + iValue;
    }
    // Lower than (shortYearBound) => this year in current century
    else if ((iValue < shortYearBound) && (iValue >= 0)) {
        return centuryNow * 100 + iValue;
    }
    // Other, it's a previous century year
    else if ((iValue <= 99) && (iValue >= shortYearBound)) {
        return (centuryNow - 1) * 100 + iValue;
    }
}

function isInteger(strNumber) {
    if (strNumber == null) {
        return false;
    }
    else if (strNumber.length <= 0) {
        return false;
    }
    else {
        for (var i = 0; i < strNumber.length; i++) {
            if (strNumber.substring(i, i+1) < "0" ||
                strNumber.substring(i, i+1) > "9")
            {
                return false;
            }
        }
    }
    return true;
}

function closeCalendar() {
    var timed = new Date();
    var tt = timed.getHours() + ":" + timed.getMinutes() + ":" +timed.getSeconds() + "";
    //alert(typeof tt + "is type of dataeSelected");
    //alert(tt  +"is the return value");
    hideCalendar();

    ctlToPlaceValue.value = constructDate(dateSelected,monthSelected,yearSelected, tt );
}

/*** Month Pulldown ***/
function StartDecMonth() {
    intervalID1 = setInterval("decMonth()",80);
}

function StartIncMonth() {
    intervalID1 = setInterval("incMonth()",80);
}

function incMonth () {
    monthSelected++;
    if (monthSelected > 11) {
        monthSelected = 0;
        yearSelected++;
    }
    constructCalendar();
}

function decMonth () {
    monthSelected--;
    if (monthSelected < 0) {
        monthSelected = 11;
        yearSelected--;
    }
    constructCalendar();
}

function selectMonth(iMonth) {
    monthConstructed = false;
    monthSelected = iMonth;
    constructCalendar();
    popDownMonth();
}

function constructMonth() {
    popDownYear()
    if (!monthConstructed) {
        sHTML = "";
        for (i=0; i<12; i++) {
            sName = monthName[language][i];
            if (i == monthSelected){
                sName = '<b>' + sName + '</b>';
            }
            sHTML += '<tr><td id="m' + i + '" class="pcalList" onmouseover="this.style.backgroundColor=\'#909090\'" onmouseout="this.style.backgroundColor=\'\'" onclick="selectMonth(' + i + ');event.cancelBubble=true">&nbsp;' + sName + '&nbsp;</td></tr>';
        }

        document.getElementById('selectMonth').innerHTML = '<table width="60" class="pcalTblList" cellspacing="0" onmouseover="clearTimeout(timeoutID1)" onmouseout="clearTimeout(timeoutID1);timeoutID1=setTimeout(\'popDownMonth()\',100);event.cancelBubble=true">' + sHTML + '</table>';
//writeWindow(document.getElementById('selectMonth').innerHTML);
        monthConstructed = true;
    }
}

function popUpMonth() {
    if (visMonth == 1) {
        popDownMonth();
        visMonth--;
    } else {
        constructMonth();
        crossMonthObj.visibility = (dom||ie) ? 'visible' : 'show';
        crossMonthObj.left = parseInt(crossobj.left) + 50;
        crossMonthObj.top = parseInt(crossobj.top) + 26;
        hideElement('SELECT', document.getElementById('selectMonth'));
        hideElement('APPLET', document.getElementById('selectMonth'));
        visMonth++;
    }
}

function popDownMonth() {
    crossMonthObj.visibility = 'hidden';
    visMonth = 0;
}

/*** Year Pulldown ***/
function incYear() {
    for (i=0; i<7; i++) {
        newYear = (i + nStartingYear) + 1;
        if (newYear == yearSelected)
            txtYear = '<span style="color:#006;font-weight:bold;">&nbsp;' + newYear + '&nbsp;</span>';
        else
            txtYear = '<span style="color:#006;">&nbsp;' + newYear + '&nbsp;</span>';
        document.getElementById('y'+i).innerHTML = txtYear;
    }
    nStartingYear++;
    bShow=true;
}

function decYear() {
    for (i=0; i<7; i++) {
        newYear = (i + nStartingYear) - 1;
        if (newYear == yearSelected)
            txtYear = '<span style="color:#006;font-weight:bold">&nbsp;' + newYear + '&nbsp;</span>';
        else
            txtYear = '<span style="color:#006;">&nbsp;' + newYear + '&nbsp;</span>';
        document.getElementById('y'+i).innerHTML = txtYear;
    }
    nStartingYear--;
    bShow=true;
}

function selectYear(nYear) {
    yearSelected = parseInt(nYear + nStartingYear);
    yearConstructed = false;
    constructCalendar();
    popDownYear();
}

function constructYear() {
    popDownMonth();
    sHTML = '';
    if (!yearConstructed) {
        sHTML = '<tr><td align="center" class="pcalList" onmouseover="this.style.backgroundColor=\'#909090\'" onmouseout="clearInterval(intervalID1);this.style.backgroundColor=\'\'" onmousedown="clearInterval(intervalID1);intervalID1=setInterval(\'decYear()\',30)" onmouseup="clearInterval(intervalID1)">-</td></tr>';

        j = 0;
        nStartingYear = yearSelected - 3;
        for ( i = (yearSelected-3); i <= (yearSelected+3); i++ ) {
            sName = i;
            if (i == yearSelected) sName = '<b>' + sName + '</b>';
            sHTML += '<tr><td id="y' + j + '" class="pcalList" onmouseover="this.style.backgroundColor=\'#909090\'" onmouseout="this.style.backgroundColor=\'\'" onclick="selectYear('+j+');event.cancelBubble=true">&nbsp;' + sName + '&nbsp;</td></tr>';
            j++;
        }

        sHTML += '<tr><td align="center" class="pcalList" onmouseover="this.style.backgroundColor=\'#909090\'" onmouseout="clearInterval(intervalID2);this.style.backgroundColor=\'\'" onmousedown="clearInterval(intervalID2);intervalID2=setInterval(\'incYear()\',30)" onmouseup="clearInterval(intervalID2)">+</td></tr>';

        document.getElementById('selectYear').innerHTML = '<table width="34" cellspacing="0" class="pcalTblList" onmouseover="clearTimeout(timeoutID2)" onmouseout="clearTimeout(timeoutID2);timeoutID2=setTimeout(\'popDownYear()\',100)">' + sHTML + '</table>';
//writeWindow(document.getElementById('selectYear').innerHTML);

        yearConstructed = true;
    }
}

function popDownYear() {
    clearInterval(intervalID1);
    clearTimeout(timeoutID1);
    clearInterval(intervalID2);
    clearTimeout(timeoutID2);
    crossYearObj.visibility= 'hidden';
    visYear = 0;
}

function popUpYear() {
    var leftOffset
    if (visYear==1) {
        popDownYear();
        visYear--;
    } else {
        constructYear();
        crossYearObj.visibility = (dom||ie) ? 'visible' : 'show';
        leftOffset = parseInt(crossobj.left) + document.getElementById('spanYear').offsetLeft;
        if (ie) leftOffset += 6;
        crossYearObj.left = leftOffset;
        crossYearObj.top = parseInt(crossobj.top) + 26;
        visYear++;
    }
}

/*** calendar ***/
function WeekNbr(n) {
    // Algorithm used:
    // From Klaus Tondering's Calendar document (The Authority/Guru)
    // http://www.tondering.dk/claus/calendar.html
    // a = (14-month) / 12
    // y = year + 4800 - a
    // m = month + 12a - 3
    // J = day + (153m + 2) / 5 + 365y + y / 4 - y / 100 + y / 400 - 32045
    // d4 = (J + 31741 - (J mod 7)) mod 146097 mod 36524 mod 1461
    // L = d4 / 1460
    // d1 = ((d4 - L) mod 365) + L
    // WeekNumber = d1 / 7 + 1

    year = n.getFullYear();
    month = n.getMonth() + 1;
    if (startAt == 0) {
        day = n.getDate() + 1;
    } else {
        day = n.getDate();
    }

    a = Math.floor((14-month) / 12);
    y = year + 4800 - a;
    m = month + 12 * a - 3;
    b = Math.floor(y/4) - Math.floor(y/100) + Math.floor(y/400);
    J = day + Math.floor((153 * m + 2) / 5) + 365 * y + b - 32045;
    d4 = (((J + 31741 - (J % 7)) % 146097) % 36524) % 1461;
    L = Math.floor(d4 / 1460);
    d1 = ((d4 - L) % 365) + L;
    week = Math.floor(d1/7) + 1;

    return week;
}

function changeBgColor(ctl, theColor) {
    oldBgColor = ctl.style.backgroundColor;
    ctl.style.backgroundColor = theColor;
}
function recoverBgColor(ctl) {
    ctl.style.backgroundColor = oldBgColor;
}

function constructCalendar () {
    var aNumDays = Array (31,0,31,30,31,30,31,31,30,31,30,31);
    var dateMessage;
    var startDate = new Date (yearSelected,monthSelected,1);
    var endDate;
    if (monthSelected==1) {
        endDate = new Date (yearSelected,monthSelected+1,1);
        endDate = new Date (endDate - (24*60*60*1000));
        numDaysInMonth = endDate.getDate();
    } else {
        numDaysInMonth = aNumDays[monthSelected];
    }

    datePointer = 0;
    dayPointer = startDate.getDay() - startAt;

    if (dayPointer<0) dayPointer = 6;

    sHTML = '<table border="0" class="pcalTblContent"><tr>';

    if (showWeekNumber == 1) {
        sHTML += '<td width="17"><b>' + weekString[language] + '</b></td><td width="1" rowspan="7" bgcolor="#d0d0d0" style="padding:0px"><img src="'+imgDir+'divider.gif" width="1"></td>';
    }

    for (i = 0; i<7; i++) {
        sHTML += '<td width="27" align="right"><b><font color="#000066">' + dayName[language][i] + '</font></b></td>';
    }

    sHTML += '</tr><tr>';

    if (showWeekNumber == 1) {
        sHTML += '<td align="right">' + WeekNbr(startDate) + '&nbsp;</td>';
    }

    for ( var i=1; i<=dayPointer;i++ ) {
        sHTML += '<td>&nbsp;</td>';
    }

    for ( datePointer=1; datePointer <= numDaysInMonth; datePointer++ ) {
        dayPointer++;
        sHTML += '<td align="right">';
        sStyle=styleAnchor;
        if ((datePointer == odateSelected) && (monthSelected == omonthSelected) && (yearSelected == oyearSelected)) {
            sStyle+=styleLightBorder
        }

        sHint = '';
        for (k = 0;k < HolidaysCounter; k++) {
            if ((parseInt(Holidays[k].d) == datePointer)&&(parseInt(Holidays[k].m) == (monthSelected+1))) {
                if ((parseInt(Holidays[k].y)==0)||((parseInt(Holidays[k].y)==yearSelected)&&(parseInt(Holidays[k].y)!=0))) {
                    sStyle+= 'background-color:#fdd;';
                    sHint += sHint=="" ? Holidays[k].desc : "\n"+Holidays[k].desc;
                }
            }
        }

        sHint = sHint.replace('/\"/g', '&quot;');

        dateMessage = 'onmousemove="window.status=\''+selectDateMessage[language].replace('[date]',constructDate(datePointer,monthSelected,yearSelected, timeNow))+'\'" onmouseover="changeBgColor(this, \'#ffff66\')" onmouseout="window.status=\'\';recoverBgColor(this)" ';


        //////////////////////////////////////////////
        //////////  Modifications PinoToy  //////////
        //////////////////////////////////////////////
        if (enablePast == 0 && ((yearSelected < yearNow) || (monthSelected < monthNow) && (yearSelected == yearNow) || (datePointer < dateNow) && (monthSelected == monthNow) && (yearSelected == yearNow))) {
            selDayAction = '';
            isPast = 1;
            dateMessage = '';   // Remove highlight features
        } else {
            selDayAction = 'href="javascript:dateSelected=' + datePointer + ';closeCalendar();"';
            isPast = 0;
        }

        // Highlight current selected date by change background
        if ((isValidInput == true) && (datePointer == dateSelected) &&
            (omonthSelected == monthSelected) && (oyearSelected == yearSelected))
        {
            sStyle += "background-color:#cccccc;";
        }

        if ((datePointer == dateNow) && (monthSelected == monthNow) && (yearSelected == yearNow)) { ///// today
            sHTML += "<b><a "+dateMessage+" title=\"" + sHint + "\" style='"+sStyle+"' "+selDayAction+"><font color=#0000ff>&nbsp;" + datePointer + "</font>&nbsp;</a></b>";
        } else if (dayPointer % 7 == (startAt * -1)+1) {                                    ///// SI ES DOMINGO - Sunday
            if (isPast==1)
                sHTML += "<a "+dateMessage+" title=\"" + sHint + "\" style='"+sStyle+"' "+selDayAction+">&nbsp;<font color=#909090>" + datePointer + "</font>&nbsp;</a>";
            else
                sHTML += "<a "+dateMessage+" title=\"" + sHint + "\" style='"+sStyle+"' "+selDayAction+">&nbsp;<font color=#ff0000>" + datePointer + "</font>&nbsp;</a>";
        } else if ((dayPointer % 7 == (startAt * -1)+7 && startAt==1) || (dayPointer % 7 == startAt && startAt==0)) {   ///// SI ES SABADO - Saturday
            if (isPast==1)
                sHTML += "<a "+dateMessage+" title=\"" + sHint + "\" style='"+sStyle+"' "+selDayAction+">&nbsp;<font color=#909090>" + datePointer + "</font>&nbsp;</a>";
            else
                sHTML += "<a "+dateMessage+" title=\"" + sHint + "\" style='"+sStyle+"' "+selDayAction+">&nbsp;<font color=#cc3300>" + datePointer + "</font>&nbsp;</a>";
        } else {                                                                            ///// CUALQUIER OTRO DIA
            if (isPast==1)
                sHTML += "<a "+dateMessage+" title=\"" + sHint + "\" style='"+sStyle+"' "+selDayAction+">&nbsp;<font color=#909090>" + datePointer + "</font>&nbsp;</a>";
            else
                sHTML += "<a "+dateMessage+" title=\"" + sHint + "\" style='"+sStyle+"' "+selDayAction+">&nbsp;<font color=#000066>" + datePointer + "</font>&nbsp;</a>";
        }

        sHTML += '';
        if ((dayPointer+startAt) % 7 == startAt) {
            sHTML += '</tr><tr>';
            if ((showWeekNumber == 1) && (datePointer < numDaysInMonth)) {
                sHTML += '<td align="right">' + (WeekNbr(new Date(yearSelected,monthSelected,datePointer+1))) + '&nbsp;</td>';
            }
        }
    }

    document.getElementById('content').innerHTML   = sHTML
    document.getElementById('spanMonth').innerHTML = '&nbsp;' + monthName[language][monthSelected] + '&nbsp;<img id="changeMonth" src="'+imgDir+'pcaldrop1.gif" width="10" height="10" border="0">'
    document.getElementById('spanYear').innerHTML  = '&nbsp;' + yearSelected    + '&nbsp;<img id="changeYear" src="'+imgDir+'pcaldrop1.gif" width="12" height="10" border="0">';
}

function selectToday() {
    popDownMonth();
    popDownYear();
    monthConstructed = false;
    monthSelected = monthNow;
    yearConstructed = false;
    yearSelected = yearNow;
    constructCalendar();
}
function showCalendar(ctl, ctl2, format, lang, past, fx, fy, isClearInvalid) {
    if (lang != null && lang != '') language = lang;
    if (past != null) enablePast = past;
    else enablePast = 0;
    if (fx != null) fixedX = fx;
    else fixedX = -1;
    if (fy != null) fixedY = fy;
    else fixedY = -1;

    if (showToday == 1) {      	
        document.getElementById('lblToday').innerHTML = '<font color="#000066">' + todayString[language] + ' <a style="color:#0000ff" onmousemove="window.status=\''+gotoString[language]+'\'" onmouseout="window.status=\'\'" title="'+gotoString[language]+'" style="'+styleAnchor+'" href="javascript:selectToday();">'+dayName[language][(today.getDay()-startAt==-1)?6:(today.getDay()-startAt)]+', ' + dateNow + ' ' + monthName[language][monthNow].substring(0,3) + ' ' + yearNow + '</a></font>';
    }
    popUpCalendar(ctl, ctl2, format, isClearInvalid);
}

function popUpCalendar(ctl, ctl2, format, isClearInvalid) {
    var leftpos = 0;
    var toppos  = 0;
    var aFormat;
    
    if (bPageLoaded) {    	
        if (crossobj.visibility == 'hidden') {
        	
            ctlToPlaceValue = ctl2;
            dateFormat = format;
            formatChar = '/';
            aFormat = dateFormat.split(formatChar);
            if (aFormat.length < 3) {
                formatChar = '-';
                aFormat = dateFormat.split(formatChar);
                if (aFormat.length < 3) {
                    formatChar = '.';
                    aFormat = dateFormat.split(formatChar);
                    if (aFormat.length < 3) {
                        formatChar = ' ';
                        aFormat = dateFormat.split(formatChar);
                        if (aFormat.length < 3) {
                            formatChar = '';                    // invalid date format
                        }
                    }
                }
            }

            tokensChanged = 0;
            if (formatChar != "") {
                aData = ctl2.value.split(formatChar);           // use user's date

                for (i=0; i<3; i++) {
                    if ((aFormat[i] == "d") || (aFormat[i] == "dd")) {
                        if (isInteger(aData[i])) {
                            dateSelected = parseInt(aData[i], 10);
                        } else {
                            dateSelected = NaN;
                        }

                        tokensChanged++;
                    } else if ((aFormat[i] == "m") || (aFormat[i] == "mm")) {
                        if (isInteger(aData[i])) {
                            monthSelected = parseInt(aData[i], 10) - 1;
                        } else {
                            monthSelected = NaN;
                        }

                        tokensChanged++;
                    } else if (aFormat[i] == "yyyy") {
                        if (isInteger(aData[i])) {
                            yearSelected = parseInt(aData[i], 10);
                        } else {
                            yearSelected = NaN;
                        }

                        tokensChanged++;
                    } else if (aFormat[i] == "yy") {
                        if (isInteger(aData[i])) {
                            yearSelected = convertToFullYear(parseInt(aData[i], 10));
                        } else {
                            yearSelected = NaN;
                        }

                        tokensChanged++;
                    } else if (aFormat[i] == "Mon") {
                        for (j = 0; j < 12; j++) {
                            if ((aData[i] != null) && (aData[i].toLowerCase() == monthName2[language][j].toLowerCase())) {
                                monthSelected=j;
                                tokensChanged++;
                            }
                        }
                    } else if (aFormat[i] == "mmm") {
                        for (j = 0; j < 12; j++) {
                            if ((aData[i] != null) && (aData[i].toLowerCase() == monthName2[language][j].toLowerCase())) {
                                monthSelected=j;
                                tokensChanged++;
                            }
                        }
                    } else if (aFormat[i] == "mmmm") {
                        for (j=0; j<12; j++) {
                            if ((aData[i] != null) && (aData[i].toLowerCase() == monthName2[language][j].toLowerCase())) {
                                monthSelected = j;
                                tokensChanged++;
                            }
                        }
                    }
                }
            }

            isValidInput = true;
            if ((tokensChanged != 3) || isNaN(dateSelected) || isNaN(monthSelected) || isNaN(yearSelected) ||
                (monthSelected > 11) || (dateSelected > 31))
            {
                dateSelected  = dateNow;
                monthSelected = monthNow;
                yearSelected  = yearNow;
                isValidInput = false;
                if (isClearInvalid == true) {
                    ctlToPlaceValue.value = "";
                }
            }

            odateSelected  = dateSelected;
            omonthSelected = monthSelected;
            oyearSelected  = yearSelected;

            aTag = ctl;
            do {
                aTag     = aTag.offsetParent;
                leftpos += aTag.offsetLeft;
                toppos  += aTag.offsetTop;
            } while (aTag.tagName != 'BODY');

            crossobj.left = (fixedX == -1) ? ctl.offsetLeft + leftpos : fixedX;
            crossobj.top = (fixedY == -1) ? ctl.offsetTop + toppos + ctl.offsetHeight + 2 : fixedY;
            //constructCalendar (1, monthSelected, yearSelected, dateSelected);
            constructCalendar ();

            // Debug
//writeWindow(document.getElementById('calendar').innerHTML);
            //--------------------------

            crossobj.visibility = (dom||ie) ? "visible" : "show";

            hideElement('SELECT', document.getElementById('calendar'));
            hideElement('APPLET', document.getElementById('calendar'));

            bShow = true;
        } else {
            hideCalendar();
            if (ctlNow!=ctl) popUpCalendar(ctl, ctl2, format);
        }
        ctlNow = ctl;
    }
}

//------- Test output by open new window --------
//function writeWindow(txt) {
//    alert("length=" + txt.length);
//    var winCalendar = window.open("about:blank");
//    winCalendar.document.write(txt);
//}
//-----------------------------------------------

document.onkeypress = function hidecal1 () {
    if (event.keyCode == 27) hideCalendar();
}
document.onclick = function hidecal2 () {
    if (!bShow) hideCalendar();
    bShow = false;
}

if(ie) {
    init();
} else {
	if (document.addEventListener) {
  		document.addEventListener("DOMContentLoaded", init, false);
	}
    window.onload = init;    
}