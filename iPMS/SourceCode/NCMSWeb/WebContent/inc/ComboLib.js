/**
 * Function      addField
 * Parameter     objDest:    The name of the combo or list
 *               sFields:    Comma seperated list of fields will bed added to the commbo
 * Description   This function will add several item to a list
 * Author        Tran Xuan Khoi
 * Date          25 Oct 2001                                                 
 */
function addFields(objDest, sFields) {   
    var ss = new Array();      
    var oDest = objDest;
    var arraySize;
    if (sFields != "") {
        // Split at each space character.
        ss = sFields.split(",");
        arraySize = ss.length;
        for( i = 0; i < arraySize; i++) {
            var item = document.createElement("OPTION");        
            item.value = ss[i];
            item.text = ss[i];
            oDest.add(item);
        }
    }   
    return;
}

/**
 * Function      listSelectField
 * Parameter     objSource:  The name of the combo or list
 * Return value  list of selected items in the combo with comma seperated format             
 * Description   This function will list all selected items in a combo               
 * Author        Tran Xuan Khoi
 * Date          25 Oct 2001                                                 
 */
function listSelectField(objSource) {
    var oSource = objSource;
    var strFields = "";
    var len = oSource.length;
    for (i = 0; i < len; i++) {
        if (oSource.item(i).selected == true) {
            value = oSource.item(i).value;
            text = oSource.item(i).text;         
            strFields = strFields + text + ",";
        }
    }
    if(strFields != "") {
        strFields = strFields.substring(0,strFields.length - 1);
    }   
    return strFields
}

/**
 * Function      removeField
 * Parameter     objSource:  The name of the combo or list
 * Description   This function will remove all selected items from a list
 * Author        Tran Xuan Khoi
 * Date          25 Oct 2001                                                 
 */
function removeField(objSource) {
    var oSource = objSource;
    var len = oSource.length ;  
    for (i = len - 1; i >= 0; i--) {
        if (oSource.item(i).selected == true) {
            oSource.remove(i);
        }
    }
}

/**
 * Function      move
 * Parameter     objSource:  The name of source list
 *               objDest:    The name of the destination list
 * Description   This function will move all selected items from a list to another list
 * Author        Tran Xuan Khoi
 * Date          25 Oct 2001                                                 
 */
function move(objSource,objDest) {
    var strFields = listSelectField(objSource);
    addFields(objDest, strFields);
    removeField(objSource);
}

/**
 * Function      getList
 * Parameter     objSource:  The name of source list
 * Return value  A string include list item
 * Description   This function will convert list items from a list to a string
 * Author        Nguyen Hoang Trung
 * Date          29 Oct 2001                                                 
 */
function getList(objSource) {
    var oSource = objSource;
    var strFields = "";
    var len = oSource.length;
    for (i = 0; i < len; i++) {
        text = oSource.item(i).text;         
        strFields = strFields + text + ",";
    }
    if(strFields != "") {
        strFields = strFields.substring(0,strFields.length - 1);
    }   
    return strFields
}

/**
 * Function      moveList
 * Parameter     objSource:  The name of source list
 * Description   This function will remove all item in sFields from objSource list
 * Author        Nguyen Hoang Trung
 * Date          29 Oct 2001                                                 
 */
function removeFields(objSource, sFields) {
    var ss = new Array();      
    var oSource = objSource;
    var listSize;
    var arraySize;
    if (sFields != "") {
         // Split at each comma character.
         ss = sFields.split(",");
         arraySize = ss.length;
         for (i = 0; i < arraySize; i++)
             for (j= oSource.length - 1; j >= 0; j--)
                 if (oSource.item(j).text == ss[i]) {
                     oSource.remove(j);
                 }
    }   
    return;
}