var YES = 1;
var NO = 0;

var SEPARATOR = ","; // 
function chkEmpty(value) {
	if (value.length == 0) {
		return NO;
	} else {
		return YES;
	}
}

/**
 * 
 * @param str
 * @param length
 * @return
 */
function chkLength(str, length) {
	if (str != null) {
		return (str.length <= length);
	} else {
		return true;
	}
}

function chkInt(value) {
    if (value.length == 0){
        return 1;
    }
    
	if (isNaN(value)) {
		return 0;
	} else if (parseInt(value, 10).toString(10) != value) {
		return 0;
	} else {
		return 1;
	}
}

function chkFloat(value) {
    if (value.length == 0) {
        return 1;
    }
	var str2 = value.replace(/[.]/, "");
	str2 = str2.replace(/-/, "");

	if (str2.search(/[^0-9]/) >= 0) {
        return 1;
	}
	
	if (isNaN(value)) {
		return 0;
	} else if (parseFloat(value,10).toString(10) != value) {
		return 0;
	} else {
		return 1;
	}
}

function chkYMD(value) {
    if (value.length == 0) {
        return 1;
    }

    data = value.split("\/");
	if (data.length !=3 ) {
		return 0;
	}
	if(data[0].length != 4 || data[0].search(/[^0-9]/) >= 0 ||
		(!(data[1].length == 1 || data[1].length == 2)) || data[1].search(/[^0-9]/) >= 0 ||
		(!(data[2].length == 1 || data[2].length == 2)) || data[2].search(/[^0-9]/) >= 0
	) {
		return 0;
	}
	years = parseInt(data[0],10);
	months = parseInt(data[1],10) - 1;
	days = parseInt(data[2],10);
	if (years < 1900) {
		return 0;
	}
	
	var dates = new Date(years, months, days);
	
	if (dates.getYear() < 1900) {
		if (years != dates.getYear() + 1900) {
			return 0;
		}
	} else {
		if (years != dates.getYear()) {
			return 0; 
		}
	}
	if (months != dates.getMonth()) { 
		return 0; 
	}
	if (days != dates.getDate()) { 
		return 0; 
	}

	return 1;
}

/**
 * Get messages from the list. 
 * @param msgList
 * @return
 */
function getMessages(msgList) {
    var MAX_NMSG = 10;
    var MAX_DATA_MESSAGE = "\n[" + msg_more + "]";
    var msgData = "";
    
    for (var i = 0; i < msgList.length; i++){
        if (i >= MAX_NMSG) {
            msgData = msgData + MAX_DATA_MESSAGE;
            break;
        }
        
        if (msgData.length != 0) {
            msgData = msgData + "\n";
        }
        
        msgData = msgData + msgList[i];
    }

    return msgData;
}

/**
 * Format message with arguments
 * Pattern message: XXX {0} {1}... {n}
 * Pattter of method: formatMsg(pattern msg, param0, param1,...)	
 * @return
 */
function formatMsg() {
    if (arguments.length == 0) {
        return "";
    }

    var msg = arguments[0];
    
    try {

        if (arguments.length > 1 ) {
            for (idx = 1; idx <arguments.length; idx++) {
                var param = arguments[idx];
                msg = msg.replace("{" + (idx -1) + "}", param);
            }
        }

    } catch(e) {
    }

    return msg;
}

function displayError(frm, txtStyle, lblStyle, itemArray, msgArray) {
	if (itemArray != null && itemArray.length != 0) {
	
		var item;
		
		for (var i = 0, len = itemArray.length; i < len; i++) {
		
			item = getObject(frm, itemArray[i]);
			initItem(item, txtStyle, lblStyle);
		}
	}

	if (msgArray != null && msgArray.length != 0) {
		alert(getMessages(msgArray));
	}
	
	setFocus(frm, itemArray);
	
}

function getObject(frm, name) {
	var item;
	var temp = name.split(SEPARATOR);
	
	if (temp[1] == null) {
		item = frm.elements[temp[0]];
	} else {
		item = frm.elements[temp[0]][temp[1]];
	}
	
	return item;
}

function initItem(item, txtStyle, lblStyle) {

	if (item.length == undefined) {
	
		if (item.type == "text") {
			if (item.readOnly || item.disabled) {
				item.className = lblStyle;
			} else {
				item.className = txtStyle;
			}
		}
	} else {
		for (var i = 0, len = item.length; i < len; i++) {
		
			if (item[i].type == "text") {
				if (item[i].readOnly || item[i].disabled) {
					item[i].className = lblStyle;
				} else {
					item[i].className = txtStyle;
				}
			}
		}
	}
}

function setFocus(fm, itemArray) {

	try {
		var item;
		for (var i = 0, len = itemArray.length; i < len; i++) {
			item = fncGetObject(fm, itemArray[i]);
			
			if (item.type == "text") {
				item.focus();
				break;
			}
		}
		
	} catch (e) {
	}
}