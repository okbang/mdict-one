/* 23-Jul-2007: Fixed this function to compatible with IE 7.
 */
function appendOption(ctrlSelect, strValue, strText, strLabel, bSelected) {
    var optNew = document.createElement("option");
    var txtNode = document.createTextNode(strText);
    optNew.setAttribute("value",strValue);
    // strLabel is avoided for compatible with IE 7.
    optNew.appendChild(txtNode);
    ctrlSelect.appendChild(optNew);
    if (bSelected == true) {
        optNew.selected = true;
    }
}

/* 23-Jul-2007: Removed this function to compatible with IE 7,
 avoid problem on set selected property of dynamic combo box */
/*function appendOption(ctrlSelect, strValue, strText, strLabel, bSelected) {
    var optNew = document.createElement('option');
    optNew.value = strValue;
    optNew.text = strText;
    // strLabel is avoided for compatible with IE 7
    //optNew.label = strLabel;

    if (bSelected == true) {
        optNew.selected = true;
    }
    try {
        ctrlSelect.add(optNew, null); // standards compliant; doesn't work in IE
    }
    catch(ex) {
        ctrlSelect.add(optNew); // IE only
    }
}*/

function selectProcess(nRowNum, currentProcessID, nCurrentProduct) {
	var myElement;
	var form = document.forms[0];
	var k = 0;
	while (form.Product[nRowNum].options.length > 0) {
		form.Product[nRowNum].options[0] = null;
	}
	while ((k < nNumberOfProcess) && (currentProcessID != arrProcessID[k])) {
		k++;
	}
	if (k < nNumberOfProcess) {
 		for (j = 0; j < arrNumRelative[k]; j++) {
 			// 20-Jul-2007: fixed mapping function to compatible with multi-browsers
 			if (nCurrentProduct != arrWPID[k][j]) {
				appendOption(form.Product[nRowNum], arrWPID[k][j], arrWPName[k][j], null, false);
			}
 			else {
 				appendOption(form.Product[nRowNum], arrWPID[k][j], arrWPName[k][j], null, true);
 			}
		}
	}
	if (form.Product[nRowNum].options.length <= 0) {
		// 20-Jul-2007: fixed mapping function to compatible with multi-browsers
		appendOption(form.Product[nRowNum], 0, "", null, false);
	}
}

// Select Work Product lists by Processes
function setProductList() {
	var objProcess = document.forms[0].Process;
	var objProduct = document.forms[0].Product;
	for (var i = 0; i < objProcess.length - 1; i++) {
		selectProcess(i, objProcess[i].value, objProduct[i].value);
	}
}

function resetAddNew() {
	var form = document.forms[0];
	for (var nRowNum = 0; nRowNum < form.Product.length - 1; nRowNum++) {
		while (form.Product[nRowNum].options.length > 0) {
			form.Product[nRowNum].options[0] = null;
		}
		appendOption(form.Product[nRowNum], 0, "", null, false);

		/*var myElement = document.createElement("option");
		myElement.value = 0;
		myElement.text = "";
		form.Product[nRowNum].add(myElement);*/
	}
}

function resetUpdate() {
	var objProcess = document.forms[0].oldProcess;
	var objProduct = document.forms[0].oldProduct;
	for (var i = 0; i < objProcess.length - 1; i++) {
		selectProcess(i, objProcess[i].value, objProduct[i].value);
	}
}
