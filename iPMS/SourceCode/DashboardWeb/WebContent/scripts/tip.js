var fcolor = "#FFCF00";
var backcolor = "#000080";
var textcolor = "#000000";
var capcolor = "#FFFFFF";
var width = "350";
var border = "1";
var offsetx = 10;
var offsety = 10;

ns4 = (document.layers)? true: false;
ie = (document.all)? true: false;
var x = 0;
var y = 0;
var snow = 0;
var sw = 0;
var cnt = 0;
var dir = 1;

if ((ns4) || (ie)) {
    if (ns4) {
        over = document.overDiv;
    }
    if (ie) {
        over = overDiv.style;
    }
    document.onmousemove = mouseMove;
    if (ns4) {
        document.captureEvents(Event.MOUSEMOVE);
    }
}

function drc(text, title) {
    dtc(1,text,title);
}

function nd() {
    if (cnt >= 1) {
        sw = 0;
    }
    if ((ns4) || (ie)) {
        if (sw == 0) {
            snow = 0;
            hideObject(over);
        }
        else {
            cnt++;
        }
    }
}

function dtc(d, text, title) {
    txt = "<TABLE WIDTH=" + width + " BORDER=0 CELLPADDING=" + border + " CELLSPACING=0 BGCOLOR=\"" + backcolor + "\"><TR><TD><TABLE WIDTH=100% BORDER=0 CELLPADDING=0 CELLSPACING=0><TR><TD><SPAN ID=\"PTT\"><B><FONT STYLE=\"FONT-FAMILY: Verdana; FONT-SIZE: xx-small;\" COLOR=\"" + capcolor + "\">" + title + "</FONT></B></SPAN></TD></TR></TABLE><TABLE WIDTH=100% BORDER=0 CELLPADDING=2 CELLSPACING=0 BGCOLOR=\"" + fcolor + "\"><TR><TD><SPAN ID=\"PST\"><FONT STYLE=\"FONT-FAMILY: Verdana, Arial; FONT-SIZE: 7pt;\" COLOR=\"" + textcolor + "\">" + text + "</FONT><SPAN></TD></TR></TABLE></TD></TR></TABLE>"
    layerWrite(txt);
    dir = d;
    disp();
}

function disp() {
    if ((ns4) || (ie)) {
        var z = parseInt(x, 10) + parseInt(offsetx, 10) + parseInt(width, 10);
        if (snow == 0) {
            if (z>770) {
                dir=0;
            }
            else {
                dir=1;
            }
            if (dir == 1) {
                moveTo(over, x + offsetx, y + offsety);

            }
            else {
                moveTo(over, x - offsetx - width, y + offsety);
            }
            showObject(over);
            snow = 1;
        }
    }
}

function mouseMove(e) {
    if (ns4) {
        x = e.pageX;
        y = e.pageY
    }
    if (ie) {
        x = event.x + document.body.scrollLeft;
        y = event.y + document.body.scrollTop;
    }
    var z = parseInt(x, 10) + parseInt(offsetx, 10) + parseInt(width, 10);
    if (snow) {
        if (z>770) {
            dir = 0;
        }
        else {
            dir = 1;
        }
        if (dir == 1) {
            moveTo(over, x + offsetx, y + offsety);
        }
        else {
            moveTo(over, x - offsetx - width, y + offsety);
        }
    }
}

function layerWrite(txt) {
    if (ns4) {
        var lyr = document.overDiv.document;
        lyr.write(txt);
        lyr.close();
    }
    else if (ie) {
        document.all["overDiv"].innerHTML = txt;
    }
}

function showObject(obj) {
    if (ns4) {
        obj.visibility = "show";
    }
    else if (ie) {
        obj.visibility = "visible";
        obj.filter="alpha(opacity=87)";
    }
}

function hideObject(obj) {
    if (ns4) {
        obj.visibility = "hide";
    }
    else if (ie) {
        obj.filter = "alpha(opacity=0)";
        obj.visibility = "hidden";
    }
}

function moveTo(obj, xL, yL) {
    obj.left = xL;
    obj.top = yL;
}