browserName = navigator.appName;
browserVer = parseInt(navigator.appVersion);

if (browserName == "Netscape" && browserVer >= 3) {
    version = "n3";
}
else {
    if (browserName == "Microsoft Internet Explorer" && browserVer >= 3) {
        version = "n3";
    }
    else {
        version = "n2";
    }
}

if (version == "n3") {
    timesheeton = new Image();
    timesheeton.src = "image/Timesheet1.jpg";
    timesheetoff = new Image();
    timesheetoff.src = "image/Timesheet.jpg";

    approveon = new Image();
    approveon.src = "image/Approve1.jpg";
    approveoff = new Image();
    approveoff.src = "image/Approve.jpg";

    manageron = new Image();
    manageron.src = "image/ViewReport1.jpg";
    manageroff = new Image();
    manageroff.src = "image/ViewReport.jpg";

    changepasswordon = new Image();
    changepasswordon.src = "image/ChangePassword1.jpg";
    changepasswordoff = new Image();
    changepasswordoff.src = "image/ChangePassword.jpg";

    logouton = new Image();
    logouton.src = "image/Logout1.jpg";
    logoutoff = new Image();
    logoutoff.src = "image/Logout.jpg";

    helpon = new Image();
    helpon.src = "image/Help1.jpg";
    helpoff = new Image();
    helpoff.src = "image/Help.jpg";
}

function img_act(imgName) {
    if (version == "n3") {
        imgOn = eval(imgName + "on.src");
        document[imgName].src = imgOn;
    }
}

function img_inact(imgName) {
   if (version == "n3") {
        imgOff = eval(imgName + "off.src");
        document[imgName].src = imgOff;
    }
}

/*
Time and date.
*/
var months = new Array(12);
months[0] = "Jan";
months[1] = "Feb";
months[2] = "Mar";
months[3] = "Apr";
months[4] = "May";
months[5] = "Jun";
months[6] = "Jul";
months[7] = "Aug";
months[8] = "Sep";
months[9] = "Oct";
months[10] = "Nov";
months[11] = "Dec";

var days = new Array(7);
days[0] = "Sun";
days[1] = "Mon";
days[2] = "Tue";
days[3] = "Wed";
days[4] = "Thu";
days[5] = "Fri";
days[6] = "Sat";

var now = new Date();
var day = days[now.getDay()];
var date = now.getDate().toString();
if (date.length < 2) {
    date = "0" + date;
}
var month = months[now.getMonth()];
var year = now.getYear();
if (year < 2000) {
    year = year + 1900;
}
var toDay = day + ", " + date + " " + month +  " " + year +" ";