function getCookieVal(offset) {
    var endstr = document.cookie.indexOf(";", offset);
    if (endstr == -1) {
        endstr = document.cookie.length;
    }
    return unescape(document.cookie.substring(offset, endstr));
}

function GetCookie (name) {
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;
    while (i < clen) {
        var j = i + alen;
        if (document.cookie.substring(i, j) == arg) {
            return getCookieVal(j);
        }
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0) {
            break;
        }
    }
    return null;
}

function SetCookie (name, value) {
    var argv = SetCookie.arguments;
    var argc = SetCookie.arguments.length;
    var expires = (argc > 2) ? argv[2] : null;
    var path = (argc > 3) ? argv[3] : null;
    var domain = (argc > 4) ? argv[4] : null;
    var secure = (argc > 5) ? argv[5] : false;
    document.cookie = name + "=" + escape (value)
            + ((expires == null) ? "" : ("; expires=" + expires.toGMTString()))
            + ((path == null) ? "" : ("; path=" + path))
            + ((domain == null) ? "" : ("; domain=" + domain))
            + ((secure == true) ? "; secure" : "");
}

function DeleteCookie(name) {
    var exp = new Date();
//    FixCookieDate (exp);
    exp.setTime (exp.getTime() - 1);
    var cval = GetCookie (name);
    if (cval != null) {
        document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString();
    }
}

function LogForm(oForm, strType) {
    var i, coll;
    if (oForm == null) {
        return  false;
    }
    var thenewdate = new Date ();
    thenewdate.setTime(thenewdate.getTime() + (5 * 24 * 60 * 60 * 1000));
    coll = oForm.elements;
    for (i = 0; i < coll.length; i++) {
        oControlType = oForm.elements(i).type;
        //if (oControlType=='text'||oControlType=='password'||oControlType=='checkbox'||oControlType=='select-one')
        if (oControlType == 'select-one') {
            switch (strType.toLowerCase()) {
                case "delete":
                    DeleteCookie(oForm.elements(i).name);
                    break;
                case "save":
                    SetCookie(oForm.elements(i).name,oForm.elements(i).value,thenewdate);
                    if (oControlType == 'checkbox') {
                        SetCookie(oForm.elements(i).name + "checked", oForm.elements(i).checked, thenewdate);
                    }
                    break;
                case "load":
                    if (GetCookie(oForm.elements(i).name) != null) {
                        oForm.elements(i).value = GetCookie(oForm.elements(i).name);
                    }
                    if (oControlType == 'checkbox') {
                        oForm.elements(i).checked = eval(GetCookie(oForm.elements(i).name + "checked"));
                    }
                    break;
            }
        }
    }
    return true;
}